package net.odiak.pinboardapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowseActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "net.odiak.pinboardapp.URL";

    private Toolbar mToolbar;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseActivity.super.onBackPressed();
            }
        });

        Intent intent = getIntent();
        String url = intent.getStringExtra(EXTRA_URL);

        mWebView = (WebView) findViewById(R.id.webView);
        setupWebView();
        mWebView.loadUrl(url);
    }

    private void setupWebView() {
        // Chrome からのインスペクトを有効にする
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);

        // localStorage を有効にする
        settings.setDomStorageEnabled(true);

        // viewport の設定が効くようにする
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);


        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                mToolbar.setTitle(title);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
