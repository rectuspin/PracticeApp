package com.example.praciceapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SecondActivity extends AppCompatActivity {

    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        webView=(WebView) findViewById(R.id.view_web);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.naver.com/");
    }
}

