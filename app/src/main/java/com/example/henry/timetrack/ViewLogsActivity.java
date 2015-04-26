package com.example.henry.timetrack;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class ViewLogsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_logs);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView hoursLog = (ListView) findViewById(R.id.viewlog_hourslog);
        StorageManager storageManager = new StorageManager();
        TaskArrayAdapter hoursLogArrayAdapter =
                new TaskArrayAdapter(this, storageManager.getAllListItems(this));
        hoursLog.setAdapter(hoursLogArrayAdapter);
    }
}
