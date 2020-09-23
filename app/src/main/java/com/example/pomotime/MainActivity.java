package com.example.pomotime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView countdownText;
    private TextView countdownBreakText;
    private Button countdownButton;
    private Button countdownButtonStop;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 1500000;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdownText = findViewById(R.id.countdown_text);
        countdownBreakText = findViewById(R.id.countdown_break_text);
        countdownButton = findViewById(R.id.countdown_button);
        countdownButtonStop = findViewById(R.id.countdown_button_stop);

        countdownButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startStop();
            }
        });
    }

    public void startStop(){
        if(timerRunning){
            stopTimer();
        }
        else{
            startTimer();
        }
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliseconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        countdownButton.setText(("PAUSE"));
        countdownButtonStop.setText("STOP");
        timerRunning = true;
    }

    public void stopTimer(){
        countDownTimer.cancel();
        countdownButton.setText("START");
        countdownButtonStop.setText("DONE");
        timerRunning = false;
    }

    public void updateTimer(){
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if(seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText((timeLeftText));
    }
}