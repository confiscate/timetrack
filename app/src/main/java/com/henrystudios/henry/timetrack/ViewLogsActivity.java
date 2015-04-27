package com.henrystudios.henry.timetrack;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.example.henry.timetrack.R;


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
