package com.hoanglinhsama.readrssnews;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progressBarLoad;
    private TextView textViewTime, textViewDate;

    private void mapping() {
        this.progressBarLoad = findViewById(R.id.progressBarLoad);
        this.textViewTime = findViewById(R.id.textViewTime);
        this.textViewDate = findViewById(R.id.textViewDate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mapping();
        SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat simpleDateFormatDate = new SimpleDateFormat("dd/MM/yyyy");
        CountDownTimer countDownTimerTime = new CountDownTimer(10000, 1000) { // dung countdowntimer de cai dat dong ho dang chay
            @Override
            public void onTick(long millisUntilFinished) {
                Calendar calendar = Calendar.getInstance();
                textViewTime.setText(simpleDateFormatTime.format(calendar.getTime())); // time thi thay doi moi giay
                textViewDate.setText(simpleDateFormatDate.format(calendar.getTime())); // date thi it thay doi
            }

            @Override
            public void onFinish() {
                this.start();
            }
        }.start();
        CountDownTimer countDownTimerProgress = new CountDownTimer(4000, 300) {
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