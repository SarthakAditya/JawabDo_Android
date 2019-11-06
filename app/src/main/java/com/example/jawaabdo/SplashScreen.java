package com.example.jawaabdo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {
    private static int splash_timeout=300;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent =new Intent(SplashScreen.this, com.example.jawaabdo.MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        }, splash_timeout);
    }
}
