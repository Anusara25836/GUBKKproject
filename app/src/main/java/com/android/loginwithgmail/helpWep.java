package com.android.loginwithgmail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class helpWep extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpweb);
        final Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String checkweb = intent.getStringExtra("checkweb");
            WebView browser;
            switch (checkweb) {
                case "travel":
                    browser = (WebView) findViewById(R.id.web);
                    browser.getSettings().setJavaScriptEnabled(true);
                    browser.loadUrl("http://bangkokonholiday.com/bts.html");
                    break;
                case "conversation":
                    browser = (WebView) findViewById(R.id.web);
                    browser.getSettings().setJavaScriptEnabled(true);
                    browser.loadUrl("http://bangkokonholiday.com/conver.html");
                    break;
                case "souvenir":
                    browser = (WebView) findViewById(R.id.web);
                    browser.getSettings().setJavaScriptEnabled(true);
                    browser.loadUrl("http://bangkokonholiday.com/souvenir.html");
                    break;
                case "festival":
                    browser = (WebView) findViewById(R.id.web);
                    browser.getSettings().setJavaScriptEnabled(true);
                    browser.loadUrl("http://bangkokonholiday.com/cal.html");
                    break;
            }

        }
    }
}
