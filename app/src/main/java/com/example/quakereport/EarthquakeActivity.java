/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.example.quakereport.setting.SettingsActivity;
import com.example.quakereport.web.WebActivity;
import com.example.quakereport.web.WebIntentContract;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    /**
     * 地震 loader ID 的常量值。可选择任意整数。
     * 仅当使用多个 loader 时该设置才起作用。
     */
    private static int EARTHQUAKE_LOADER_ID = 1;

    /**
     * 日志标签
     */
    private static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * URL for earthquake data from the USGS dataset
     */
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    /**
     * 地震列表的适配器
     */
    private EarthquakeAdapter earthquakeAdapter;

    /**
     * 列表为空时显示的 空视图
     */
    private TextView emptyView;

    /**
     * 加载指示符
     */
    private ProgressBar loadSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "TEST: onCreate() called ...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // 在布局中查找 {@link ListView} 的引用
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // 为 ListView 绑定空视图，在无数据时自动显示
        emptyView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(emptyView);

        // 创建新适配器，将空地震列表作为输入
        earthquakeAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // 在 {@link ListView} 上设置适配器
        // 以便可以在用户界面中填充列表
        earthquakeListView.setAdapter(earthquakeAdapter);

        // 在 ListView 上设置项目单击监听器，该监听器会向 Web 浏览器发送 intent，
        // 打开包含有关所选地震详细信息的网站。
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 查找单击的当前地震
                Earthquake currentEarthquake = earthquakeAdapter.getItem(position);

                // 将字符串 URL 转换成 URI 对象（传递到 Intent 构造函数中）
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // 创建新 intent 以查看地震 URI（显式）
                Intent intent = new Intent(getBaseContext(), WebActivity.class);
                intent.putExtra(WebIntentContract.S_EARTHQUAKE_URI, earthquakeUri.toString());
                intent.putExtra(WebIntentContract.S_EARTHQUAKE_PLACE, currentEarthquake.getPlace());

                // 发送 intent 以启动新活动
                startActivity(intent);
            }
        });

        // If there is a network connection, fetch data
        if (checkNetworkConnection()) {
            // 引用 LoaderManager，以便与 loader 进行交互。
            LoaderManager loaderManager = getSupportLoaderManager();

            // 初始化 loader。传递上面定义的整数 ID 常量并作为捆绑
            // 传递 null。为 LoaderCallbacks 参数（由于
            // 此活动实现了 LoaderCallbacks 接口而有效）传递此活动。
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            loadSpinner = findViewById(R.id.loading_spinner);
            loadSpinner.setVisibility(View.GONE);
            emptyView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called ...");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // 地震最小震级的偏好设置
        String minMagnitude = sharedPrefs.getString(getString(
                R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // 按照地震发生的最近时间排序偏好
        String orderBy = sharedPrefs.getString(getString(
                R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        // 按照地震发生的最近时间排序偏好
        String limit = sharedPrefs.getString(getString(
                R.string.settings_limit_key),
                getString(R.string.settings_limit_default));

        // 最多显示地震个数偏好
        String limit = sharedPrefs.getString(getString(
                R.string.settings_limit_key),
                getString(R.string.settings_limit_default));

        // 构造 USGS 请求地址
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", limit);
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        Log.i(LOG_TAG, "TEST: onLoadFinished() called ...");

        // Set empty state text to display "No earthquakes found."
        emptyView.setText(R.string.no_earthquakes);

        // 因数据已加载，隐藏加载指示符
        loadSpinner = findViewById(R.id.loading_spinner);
        loadSpinner.setVisibility(View.GONE);

        // 清除之前地震数据的适配器
        earthquakeAdapter.clear();

        // 如果存在 {@link Earthquake} 的有效列表，则将其添加到适配器的
        // 数据集。这将触发 ListView 执行更新。
        if (earthquakes != null && !earthquakes.isEmpty()) {
            earthquakeAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "TEST: onLoaderReset() called ...");

        // 重置 Loader，以便能够清除现有数据。
        earthquakeAdapter.clear();
    }

    /**
     * Check for connectivity status
     *
     * @return Network connection status
     */
    private boolean checkNetworkConnection() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Check for connectivity status
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

}
