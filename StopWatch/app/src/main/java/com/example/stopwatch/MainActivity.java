package com.example.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int sec=0;
    private boolean isRunning;
    private boolean wasRunning;


    private Button mButtonStart;
    private Button mButtonReset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            sec=savedInstanceState.getInt("sec");
            isRunning=savedInstanceState.getBoolean("isRunning");
            wasRunning=savedInstanceState.getBoolean("wasRunning");
        }
        mButtonStart=findViewById(R.id.button_start);
        mButtonReset=findViewById(R.id.btn_reset);

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRunning){
                    pauseTimer();
                }else {
                    running_Timer();
                }


            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();

            }
        });

        //running_Timer();
    }

    private void pauseTimer() {
        isRunning=false;
        updateButton();
    }

    private void resetTimer() {
        isRunning=false;
        sec=0;
        running_Timer();
        updateButton();
    }

    private void updateButton(){
        if(isRunning){
            mButtonStart.setText("Pause");
            mButtonReset.setVisibility(View.INVISIBLE);
        }else {

            mButtonStart.setText("Resume");
            mButtonReset.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("sec",sec);
        outState.putBoolean("wasRunning",wasRunning);
        outState.putBoolean("isRunning",isRunning);
    }

    private void running_Timer()
    {
        final TextView t_View = (TextView)findViewById(R.id.text_view_time);
        final Handler handle = new Handler();
        handle.post(new Runnable() {
            @Override
            public void run()
            {
                int hrs = sec / 3600;
                int mins = (sec % 3600) / 60;
                int secs = sec % 60;
                String time_t = String .format(Locale.getDefault(), "    %d:%02d:%02d   ", hrs,mins, secs);
                t_View.setText(time_t);
                if (isRunning) {
                    sec++;
                }
                handle.postDelayed(this, 1000);
            }
        });

        isRunning=true;
        updateButton();
    }
}