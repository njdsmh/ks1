package com.example.ks1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

public class WebViewTest extends AppCompatActivity {
    String TAG="xushitao";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_test);

        WebView webView=(WebView) findViewById(R.id.web_view);

        EditText et=(EditText)findViewById(R.id.edittext_2);

        String url=et.getText().toString();
        Log.d(TAG, "onClick: "+url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.baidu.com");
//        webView.loadUrl(url);

        Button button=(Button) findViewById(R.id.button_2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url=et.getText().toString();
                Log.d(TAG, "onClick: "+url);
                webView.loadUrl(url);
            }
        });
    }
}