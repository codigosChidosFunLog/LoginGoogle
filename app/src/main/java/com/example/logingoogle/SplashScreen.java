package com.example.logingoogle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        Thread background = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2*1000);
                    startActivity(new Intent(context, LoginActivity.class));
                    finish();
                } catch (Exception e){

                }
            }
        };
        background.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
