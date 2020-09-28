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
    private CountDownTimer countDownTimerBreak;
    private long timeLeftInMillisecondsWork = 1500000;
    private long timeLeftInMillisecondsBreak = 300000;
    private boolean WorkTimerRunning;
    private boolean BreakTimerRunning;
    private boolean WorkOrBreak = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countdownText = findViewById(R.id.countdown_text);
        countdownBreakText = findViewById(R.id.countdown_break_text);
        countdownButton = findViewById(R.id.countdown_button);
        countdownButtonStop = findViewById(R.id.countdown_button_stop);

        countdownButtonStop.setEnabled(false);
        countdownButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startPause();
            }
        });

        countdownButtonStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                stopDone();
            }
        });
    }

    //Functions for start and resume button
    public void startPause(){
        if(WorkOrBreak){
            if(WorkTimerRunning){
                pauseWorkTimer();
            }
            else{
                startWorkTimer();
            }
        }
        else{
            if(BreakTimerRunning){
                pauseBreakTimer();
            }
            else{
                startBreakTimer();
            }
        }
    }

    //Funtions for done and skip button
    public void stopDone(){
        if(WorkOrBreak){
            if(WorkTimerRunning){
                stopWorkTimer();
            }
            else{
                finishWorkTimer();
            }
        }
        else{
            skipBreakTimer();
        }
    }

    //funtion that starts the work timer
    public void startWorkTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMillisecondsWork, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillisecondsWork = l;
                updateWorkTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        countdownButton.setText(("PAUSE"));
        countdownButtonStop.setText("STOP");
        countdownButtonStop.setEnabled(true);
        WorkTimerRunning = true;
        WorkOrBreak = true;
    }

    //function that starts the break timer
    public void startBreakTimer(){
        countDownTimerBreak = new CountDownTimer(timeLeftInMillisecondsBreak, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillisecondsBreak = millisUntilFinished;
                updateBreakTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();
        countdownButton.setText("PAUSE");
        countdownButtonStop.setText("SKIP");
        BreakTimerRunning = true;
        WorkOrBreak = false;
    }

    //pauses work timer
    public void pauseWorkTimer(){
        countDownTimer.cancel();
        countdownButton.setText("RESUME");
        countdownButtonStop.setText("DONE");
        WorkTimerRunning = false;
    }

    //stops the work timer, makes it go back to 25:00 minutes
    public void stopWorkTimer(){
        countDownTimer.cancel();
        countdownText.setText("25:00");
        timeLeftInMillisecondsWork = 1500000;
        countdownButton.setText("START");
        countdownButtonStop.setText("DONE");
        countdownButtonStop.setEnabled(false);
        WorkTimerRunning = false;
    }

    //finishes work timer and starts the break timer
    public void finishWorkTimer(){
        countDownTimer.cancel();
        countdownText.setText("25:00");
        timeLeftInMillisecondsWork = 1500000;
        countdownButton.setText("PAUSE");
        countdownButtonStop.setText("SKIP");
        startBreakTimer();
    }

    //pauses the break timer
    public void pauseBreakTimer(){
        countDownTimerBreak.cancel();
        countdownButton.setText("RESUME");
        BreakTimerRunning = false;
    }

    //skips the break
    public void skipBreakTimer(){
        countDownTimerBreak.cancel();
        countdownBreakText.setText("05:00");
        timeLeftInMillisecondsBreak = 300000;
        countdownButton.setText("START");
        countdownButtonStop.setText("DONE");
        countdownButtonStop.setEnabled(false);
        WorkOrBreak = true;
    }

    //updates the work timer textview every second
    public void updateWorkTimer(){
        int minutes = (int) timeLeftInMillisecondsWork / 60000;
        int seconds = (int) timeLeftInMillisecondsWork % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "" + minutes;
        timeLeftText += ":";
        if(seconds < 10) timeLeftText += "0";
        timeLeftText += seconds;

        countdownText.setText((timeLeftText));
    }

    //updates the break timer textview every second
    public void updateBreakTimer(){
        int minutes = (int) timeLeftInMillisecondsBreak / 60000;
        int seconds = (int) timeLeftInMillisecondsBreak % 60000 / 1000;

        String timeLeft;
        timeLeft = "" + minutes;
        timeLeft += ":";
        if(seconds < 10) timeLeft += "0";
        timeLeft += seconds;

        countdownBreakText.setText((timeLeft));
    }
}