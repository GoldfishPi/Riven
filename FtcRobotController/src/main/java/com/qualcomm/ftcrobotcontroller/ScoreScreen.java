package com.qualcomm.ftcrobotcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by goldfishpi on 11/21/15.
 */
public class ScoreScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scores);

        String url = "http://www.twitch.tv/GoldfishPi";
        WebView view = (WebView) this.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(url);
    }
}
