package com.ra1ph.gcmexample;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 29.10.13
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class HtmlActivity extends Activity {
    private final static String URL="http://app.wifix.ru/banner.html";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.html_activity);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl(URL);
    }
}
