package com.hoanglinhsama.readrssnews;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {
    private WebView webViewDisplay; // webview de load giao dien HTML)

    private void mapping() {
        this.webViewDisplay = findViewById(R.id.webViewDisplay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        this.mapping();
        Intent intent = getIntent();
        webViewDisplay.loadUrl(intent.getStringExtra("link")); // load mot website vao ung dung
        webViewDisplay.setWebViewClient(new WebViewClient()); // WebViewClient la noi xu ly khi nguoi dung tuong tac voi website duoc load len trong ung dung, dong lenh nay de website luon duoc mo bang ung dung chu khong phai mo bang trinh duyet mac dinh cua may ao
        webViewDisplay.getSettings().setJavaScriptEnabled(true); // enable JavaScrip de co the xem video tuong tac voi menu,... tren website trong android
    }
}