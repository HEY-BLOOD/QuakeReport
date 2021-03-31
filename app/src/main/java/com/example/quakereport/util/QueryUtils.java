package com.example.quakereport.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.quakereport.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * 从给定字符串 URL 返回新 URL 对象。
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * 向给定 URL 进行 HTTP 请求，并返回字符串作为响应。
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (null == url) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            // 如果请求成功（响应代码 200），
            // 则读取输入流并解析响应。
            if (200 == urlConnection.getResponseCode()) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
            if (null != inputStream) {
                // 关闭输入流可能会抛出 IOException，这就是 makeHttpRequest(URL url) 方法签名
                // 指定可能抛出 IOException 的原因。
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * 将 {@link InputStream} 转换为包含
     * 来自服务器的整个 JSON 响应的字符串。
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (null != inputStream) {
            InputStream in;
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (null != line) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * 返回通过解析 JSON响应后的 {@link Earthquake} 对象列表。
     */
    public static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        // 如果 JSON 字符串为空或 null，将提早返回。
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // 创建一个可以添加地震的空 ArrayList
        List<Earthquake> earthquakes = new ArrayList<>();

        // 尝试解析 JSON 响应字符串。如果格式化 JSON 的方式存在问题，
        // 则将抛出 JSONException 异常对象。
        // 捕获该异常以便应用不会崩溃，并将错误消息打印到日志中。
        try {

            // 通过 JSON 响应字符串创建 JSONObject
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // 提取与名为 "features" 的键关联的 JSONArray，
            // 该键表示特征（或地震）列表。
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            // 针对 earthquakeArray 中的每个地震，创建 {@link Earthquake} 对象
            for (int i = 0; i < earthquakeArray.length(); i++) {
                // 获取地震列表中位置 i 处的单一地震
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // 针对给定地震，提取与名为 "properties" 的键关联的 JSONObject，
                // 该键表示该地震所有属性的
                // 列表。
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                // 提取名为 "mag" 的键的值
                double magnitude = properties.getDouble("mag");

                // 提取名为 "place" 的键的值
                String location = properties.getString("place");

                // 提取名为 "time" 的键的值
                long time = properties.getLong("time");

                // 提取名为 "url" 的键的值
                String url = properties.getString("url");

                // 使用 JSON 响应中的震级、位置、时间和 url，
                // 创建新的 {@link Earthquake} 对象。
                Earthquake earthquake = new Earthquake(magnitude, location, time, url);

                // 将该新 {@link Earthquake} 添加到地震列表。
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            // 在 "try" 块中执行上述任一语句时若系统抛出错误，
            // 则在此处捕获异常，以便应用不会崩溃。在日志消息中打印
            // 来自异常的消息。
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // 返回地震列表
        return earthquakes;
    }

    /**
     * 查询 USGS 数据集并返回 {@link Earthquake} 对象的列表。
     *
     * @deprecated 仅仅使用标准的 Java类{@link HttpURLConnection}来发起 HTTP请求，
     * 和标准的 JSON类{@link JSONObject}来解析 JSON格式的字符串。
     */
    @Deprecated
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        Log.i(LOG_TAG, "TEST: fetchEarthquakeData() called ...");

        // 创建 URL 对象
        URL url = createUrl(requestUrl);

        // 执行 URL 的 HTTP 请求并接收返回的 JSON 响应
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // 从 JSON 响应提取相关域，返回 {@link Earthquake} 的列表
        return extractFeatureFromJson(jsonResponse);
    }

}