package com.android.loginwithgmail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class facshowweb extends Activity {
    private WebView browser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facshowweb);
        browser = (WebView) findViewById(R.id.web);
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String id = intent.getStringExtra("id");
            //Toast.makeText(facshowweb.this,id , Toast.LENGTH_SHORT).show();
            String url="http://bangkokonholiday.com/fac.php?idFacilitate="+id;
            browser.getSettings().setJavaScriptEnabled(true);
            browser.loadUrl(url);
            browser.setWebViewClient(new WebViewClient());

        }else {
            Intent intentback = new Intent(facshowweb.this,helpMap.class);
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
