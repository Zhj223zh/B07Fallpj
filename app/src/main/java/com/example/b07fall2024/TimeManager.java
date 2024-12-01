package com.example.b07fall2024;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import java.util.Timer;
import java.util.TimerTask;

public class TimeManager {
    private Timer quizTimer;
    private int totalTimeInMin = 3;
    private int seconds = 59;
    private TextView timerTextView;
    private Activity activity;

    public TimeManager(TextView timerTextView,Activity activity){
        this.timerTextView = timerTextView;
        this.activity = activity;
    }

    //Source: How to create a quiz app in android studio by Learnoset- learn coding online
    public void startTimer() {
        quizTimer = new Timer();
        quizTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (seconds == 0) {
                    if (totalTimeInMin > 0) {
                        totalTimeInMin--;
                        seconds = 59;  // Reset seconds to 59 when a new minute starts
                    } else {
                        // Time is up
                        quizTimer.purge();
                        quizTimer.cancel();
                        activity.runOnUiThread(() -> {
                            Toast.makeText(timerTextView.getContext(), "Time Over", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    seconds--;
                }

                // Update the timer on the UI thread
                activity.runOnUiThread(() -> {
                    // Format the timer as MM:SS
                    timerTextView.setText(String.format("%02d:%02d", totalTimeInMin, seconds));
                });
            }
        }, 1000, 1000); // Schedule the task to run every second (1000 ms)
    }
    public void stopTimer(){
        if(quizTimer != null){
            quizTimer.cancel();
        }
    }


}
