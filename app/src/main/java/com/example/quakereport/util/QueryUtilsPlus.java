package com.example.quakereport.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.quakereport.Earthquake;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Better helper methods related to requesting and receiving earthquake data from USGS.
 **/
public class QueryUtilsPlus extends QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtilsPlus.class.getSimpleName();

    /**
     * {@link OkHttpClient} to send HTTP request
     */
    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    private QueryUtilsPlus() {
    }

    /**
     * 使用 OkHttp第三方库发送请求以获取地震数据
     *
     * @param url HTTP 请求地址
     * @return 带有 JSON格式的字符串
     * @throws IOException IO异常
     */
    private static String getJsonResponse(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = mOkHttpClient.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    /**
     * 查询 USGS数据集并返回 {@link Earthquake} 对象的列表。（使用第三方库 OkHttp请求网络数据）
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        Log.i(LOG_TAG, "TEST: fetchEarthquakeData() called ...");

        String jsonResponse = null;
        try {
            jsonResponse = getJsonResponse(requestUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 从 JSON 响应提取相关域，返回 {@link Earthquake} 的列表
        return extractFeatureFromJson(jsonResponse);
    }


    /**
     * 返回通过 Gson库解析 JSON响应后的 {@link Earthquake} 对象列表。
     *
     * @param earthquakeJSON 经 HTTP 请求返回的 {@link JSONObject} 响应字符串
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

            // Gson 对象，用于解析（反序列化） Json格式字符串
            Gson gson = new Gson();

            // 针对 earthquakeArray 中的每个地震，创建 {@link Earthquake} 对象
            for (int i = 0; i < earthquakeArray.length(); i++) {
                // 获取地震列表中位置 i 处的单一地震
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // 针对给定地震，提取与名为 "properties" 的键关联的 JSONObject，
                // 该键表示该地震所有属性的列表。
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                // 从 properties JSON对象中解析与实体类属性名相同的值
                Earthquake earthquake = gson.fromJson(properties.toString(), Earthquake.class);

                Log.i(LOG_TAG, earthquake.toString());

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
}
