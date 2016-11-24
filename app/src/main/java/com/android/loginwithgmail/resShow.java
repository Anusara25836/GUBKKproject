package com.android.loginwithgmail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class resShow extends Activity {
private WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_show);
        browser = (WebView) findViewById(R.id.web);
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String id = intent.getStringExtra("id");
            String url="http://bangkokonholiday.com/res.php?idRestaurant="+id;
            browser.getSettings().setJavaScriptEnabled(true);
            browser.loadUrl(url);
            browser.setWebViewClient(new WebViewClient());
        }else {
            Intent intentback = new Intent(resShow.this,Maps.class);
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
