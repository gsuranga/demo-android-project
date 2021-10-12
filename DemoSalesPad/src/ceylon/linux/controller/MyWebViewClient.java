package ceylon.linux.controller;

import android.webkit.WebView;
import android.webkit.WebViewClient;

class MyWebViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        return false;
    }
}