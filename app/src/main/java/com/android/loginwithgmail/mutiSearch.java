package com.android.loginwithgmail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class mutiSearch extends Activity {
    private WebView browser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_search);
        browser = (WebView) findViewById(R.id.web);
            String url="http://bangkokonholiday.com/mutiSearch.php";
            browser.getSettings().setJavaScriptEnabled(true);
            browser.loadUrl(url);
            browser.setWebViewClient(new WebViewClient());
    }
    @Override
    public void onBackPressed(){
        if(browser.canGoBack()){
            browser.goBack();
        }else {
            super.onBackPressed();
        }
    }

}
