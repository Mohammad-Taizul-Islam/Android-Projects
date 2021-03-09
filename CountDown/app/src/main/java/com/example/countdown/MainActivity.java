package com.example.countdown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {




    private TextView mTextViewCountDown;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private EditText mEditTextTime;
    private Button mButtonSet;

    
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private long mStartTimeMillis;

    //KEYS
    public static final String SharedPreferenceKey="sPreference";
    public static final String LEFT_IN_MILLIS="left_in_millis";
    public static final String TIMER_RUNNING="timer_running";
    public static final String END_TIME="end_time";
    public static final String START_TIME_MILLIS="start_time_in_millis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextTime=findViewById(R.id.edit_text_number);
        mButtonSet=findViewById(R.id.button_set);
        mTextViewCountDown=findViewById(R.id.text_view_coundown);
        mButtonStartPause=findViewById(R.id.btn_start_pause);
        mButtonReset=findViewById(R.id.btn_reset);
        
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=mEditTextTime.getText().toString();
                if(input.length()==0){
                    Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput=Long.parseLong(input)*60000;
                if(millisInput == 0 || millisInput < 0){
                    Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                mEditTextTime.setText("");
            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimerRunning){
                    pauseTimer();
                }else {
                    startTimer();
                }
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    resetTimer();
            }
        });
    }
    private void setTime(long milliseconds){
        mStartTimeMillis=milliseconds;
        resetTimer();
    }
    private void startTimer(){
        mEndTime=System.currentTimeMillis()+mTimeLeftInMillis;
        mCountDownTimer=new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis=millisUntilFinished;
                updateCountDownText();
                updateWatchInterface();
            }

            @Override
            public void onFinish() {
                mTimerRunning=false;
                updateWatchInterface();
                if(!mTimerRunning){

                }
            }
        }.start();
        mTimerRunning=true;
        updateWatchInterface();
    }
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning=false;
        updateWatchInterface();
    }

    private void resetTimer(){
        mTimeLeftInMillis=mStartTimeMillis;
        updateCountDownText();
        updateWatchInterface();

    }

    private void updateCountDownText(){
        int hours=(int)((mTimeLeftInMillis/1000)/3600);
        int minutes=(int)(((mTimeLeftInMillis/1000)%3600)/60);
        int seconds=(int)(mTimeLeftInMillis/1000)%60;
        String timeLeftFormatted;
        if(hours>0){
            timeLeftFormatted=String.format(Locale.getDefault(),"%d : %02d : %02d",hours,minutes,seconds);
        }else {
            timeLeftFormatted=String.format(Locale.getDefault(),"%02d : %02d",minutes,seconds);
        }
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface(){
        if(mTimerRunning){
            mEditTextTime.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
            mButtonReset.setVisibility(View.VISIBLE);

        }else {
            mEditTextTime.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");
            if(mTimeLeftInMillis < 1000){
                mButtonStartPause.setVisibility(View.INVISIBLE);
            }else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }
            if(mTimeLeftInMillis < mStartTimeMillis){
                mButtonReset.setVisibility(View.VISIBLE);
            }else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }

        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences=getSharedPreferences(SharedPreferenceKey,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(START_TIME_MILLIS,mStartTimeMillis);
        editor.putLong(LEFT_IN_MILLIS,mTimeLeftInMillis);
        editor.putLong(END_TIME,mEndTime);
        editor.putBoolean(TIMER_RUNNING,mTimerRunning);
        editor.apply();

        if(mCountDownTimer != null){
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences=getSharedPreferences(SharedPreferenceKey,MODE_PRIVATE);
        mStartTimeMillis=sharedPreferences.getLong(START_TIME_MILLIS,600000);
        mTimeLeftInMillis=sharedPreferences.getLong(LEFT_IN_MILLIS,mStartTimeMillis);
        mTimerRunning=sharedPreferences.getBoolean(TIMER_RUNNING,false);
        updateCountDownText();
        updateWatchInterface();
        if(mTimerRunning){
            mEndTime=sharedPreferences.getLong(END_TIME,0);
            mTimeLeftInMillis=mEndTime-System.currentTimeMillis();

            if(mTimeLeftInMillis < 0){
                mTimeLeftInMillis=0;
                mTimerRunning=false;
                updateCountDownText();
                updateWatchInterface();
            }else {
                startTimer();
            }
        }

    }


}