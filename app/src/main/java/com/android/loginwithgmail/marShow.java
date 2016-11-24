package com.android.loginwithgmail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class marShow extends Activity {
    private WebView browser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mar_show);
        browser = (WebView) findViewById(R.id.web);
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String id = intent.getStringExtra("id");
            String url="http://bangkokonholiday.com/market.php?idMarket="+id;
            browser.getSettings().setJavaScriptEnabled(true);
            browser.loadUrl(url);
            browser.setWebViewClient(new WebViewClient());
        }else {
            Intent intentback = new Intent(marShow.this,Maps.class);
            startActivity(intentback);
        }
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
