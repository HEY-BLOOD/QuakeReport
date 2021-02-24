package com.example.quakereport;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import com.example.quakereport.util.QueryUtilsPlus;

import java.util.List;

/**
 * 通过使用 AsyncTask 执行
 * 给定 URL 的网络请求，加载地震列表。
 */
public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

    /**
     * 日志消息标签
     */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    /**
     * 查询 URL
     */
    private String url;

    /**
     * 构建新 {@link EarthquakeLoader}。
     * <p>
     * 活动的 @param 上下文
     * 要从中加载数据的 @param url
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: onStartLoading() called ...");

        // Loader 初始化完成后，立即强制启动
        forceLoad();
    }

    /**
     * 这位于后台线程上。
     */
    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG, "TEST: loadInBackground() called ...");

        if (null == url) {
            return null;
        }

        // 执行网络请求、解析响应和提取地震列表。
        List<Earthquake> earthquakes = QueryUtilsPlus.fetchEarthquakeData(url);
        return earthquakes;
    }
}
