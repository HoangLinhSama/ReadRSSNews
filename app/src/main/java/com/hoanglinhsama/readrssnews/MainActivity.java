package com.hoanglinhsama.readrssnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBarLoad;

    private void mapping() {
        this.progressBarLoad = findViewById(R.id.progressBarLoad);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mapping();
        CountDownTimer countDownTimer = new CountDownTimer(4000, 300) {
            @Override
            public void onTick(long millisUntilFinished) {
                int percent = progressBarLoad.getProgress();
                progressBarLoad.setProgress(percent + 10);
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        }.start();
    }
}