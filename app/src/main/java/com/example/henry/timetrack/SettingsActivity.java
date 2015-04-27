package com.example.henry.timetrack;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.example.henry.timetrack.notifications.NotificationsManager;


public class SettingsActivity extends ActionBarActivity {
    private int notifyId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void sendPushNotification(View view) {
        NotificationsManager.sendNotification(this);
    }
}
