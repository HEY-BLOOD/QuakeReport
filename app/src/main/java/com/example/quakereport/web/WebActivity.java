package com.example.quakereport.web;

import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quakereport.R;

/**
 * 用于显示地震的网页链接的活动
 */
public class WebActivity extends AppCompatActivity {

    /**
     * 加载地震URL 的 WebView
     */
    WebView mWebView;

    /**
     * 网页设置
     */
    WebSettings mWebSettings;

    /**
     * WebView ，网页客户端，相当于一个浏览器
     */
    WebViewClient mWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        // Set activity title
        String place = getIntent().getStringExtra(WebIntentContract.S_EARTHQUAKE_PLACE);
        setTitle(place);

        // Get web uri to string
        mWebView = (WebView) findViewById(R.id.webview);
        String uri = getIntent().getStringExtra(WebIntentContract.S_EARTHQUAKE_URI);

        // Supporting JavaScript
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        // Android WebView loadUrl
        mWebView.loadUrl(uri);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // First checks if the WebView can go back.
        // If the user has navigated away from the first page loaded inside the WebView, then the WebView can go back.
        // The WebView maintains a browsing history just like a normal browser.
        // If there is no history then it will result in the default behavior of back button i.e. exiting the app.
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}