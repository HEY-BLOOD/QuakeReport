package com.example.quakereport.util;

import android.util.Log;

import com.example.quakereport.Earthquake;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @create: 2021-02-24 23:57
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
     * @param url
     * @return 带有 JSON格式的字符串
     * @throws IOException
     */
    private static String getJsonResponse(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = mOkHttpClient.newCall(request).execute()) {
            return response.body().string();
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

        // 从 JSON 响应提取相关域并创建 {@link Earthquake} 的列表
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        return earthquakes;
    }
}
