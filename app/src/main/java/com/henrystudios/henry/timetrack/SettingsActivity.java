package com.henrystudios.henry.timetrack;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.henry.timetrack.R;
import com.henrystudios.henry.timetrack.notifications.NotificationsManager;


public class SettingsActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    private int notifyId = 1;
    private StorageManager storageManager = new StorageManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateTimeBetweenNotifySpinner();
    }

    private void updateTimeBetweenNotifySpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.time_between_reminders);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_between_notify, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int selectedPosition = adapter.getPosition(storageManager.getNotificationsPeriod(this));
        spinner.setSelection(selectedPosition);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String item = (String) parent.getItemAtPosition(pos);
        storageManager.setNotificationPeriod(item, this);
    }

    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void sendPushNotification(View view) {
        NotificationsManager.sendNotification(this);
    }
}
