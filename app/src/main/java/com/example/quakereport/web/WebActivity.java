package com.example.quakereport.web;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quakereport.R;

/**
 * 用于显示地震的网页
 */
public class WebActivity extends AppCompatActivity {

    /**
     * 加载地震URL 的 WebView
     */
    WebView webView;

    /**
     * 网页设置
     */
    WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String place = getIntent().getStringExtra(WebIntentContract.S_EARTHQUAKE_PLACE);
        setTitle(place);

        webView = (WebView) findViewById(R.id.webview);
        String uri = getIntent().getStringExtra(WebIntentContract.S_EARTHQUAKE_URI);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(uri);
    }

}