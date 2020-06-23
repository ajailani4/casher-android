package com.example.casher.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.casher.R;

public class NotificationBell extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_bell);

        getSupportActionBar().setTitle(getResources().getString(R.string.notification_bell));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
