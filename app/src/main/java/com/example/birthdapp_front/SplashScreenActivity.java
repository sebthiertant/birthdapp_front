package com.example.birthdapp_front;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            startActivity(new Intent(this, ListActivity.class));
        } catch (Exception e) {
            e.getMessage();
        }

        finish();
    }
}