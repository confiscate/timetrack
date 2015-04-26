package com.example.henry.timetrack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.*;
import com.amazonaws.regions.Regions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ActionBarActivity {
    public final static String MODIFY_HOUR = "com.example.henry.MODIFY_HOUR";
    public final static String MODIFY_DATE = "com.example.henry.MODIFY_DATE";
    public final static String EXISTING_DESC = "com.example.henry.EXISTING_DESC";
    public final static String EDIT_LATEST = "com.example.henry.EDIT_LATEST";
    public static final int MODIFY_TASK_DESC_REQUEST = 1;

    public static SimpleDateFormat hourFormat = new SimpleDateFormat("h:00 aa");
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    private int modifyPosition = 0;
    private TaskArrayAdapter hoursLogArrayAdapter;
    public static StorageManager storageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageManager = new StorageManager();
        storageManager.pullTasksFromStorage(this);

        ToggleButton notifyToggle = (ToggleButton) findViewById(R.id.togglebutton);
        notifyToggle.setChecked(storageManager.getNotificationsActive(this));

        // If started from a push notification, send user directly to modify the latest task
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra(MainActivity.EDIT_LATEST, false)) {
            HoursListItem[] hoursListItems = storageManager.getHoursListItems();
            Intent latestTaskIntent = new Intent(this, ChangeMessageActivity.class);
            latestTaskIntent.putExtra(MODIFY_HOUR, hoursListItems[0].getHour());
            latestTaskIntent.putExtra(MODIFY_DATE, hoursListItems[0].getDate());
            latestTaskIntent.putExtra(EXISTING_DESC, hoursListItems[0].getTaskDesc());
            modifyPosition = 0;
            startActivityForResult(latestTaskIntent, MODIFY_TASK_DESC_REQUEST);
        }
    }

    private void refreshList() {
        if (hoursLogArrayAdapter != null) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hoursLogArrayAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    void fillHoursLog() {
        Calendar curTime = Calendar.getInstance();
        for (int i = 0; i < 24; i++) {
            storageManager.setHoursListItem(i, new HoursListItem(hourFormat.format(curTime.getTime()),
                    dateFormat.format(curTime.getTime()), ""));
            curTime.add(Calendar.HOUR, -1);
        }
        final ListView hoursLog = (ListView) findViewById(R.id.hourslog);
        hoursLogArrayAdapter = new TaskArrayAdapter(this, storageManager.getHoursListItems());
        hoursLog.setAdapter(hoursLogArrayAdapter);

        final Intent intent = new Intent(this, ChangeMessageActivity.class);
        hoursLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                HoursListItem[] hoursListItems = storageManager.getHoursListItems();
                if (position < hoursListItems.length && hoursListItems[position] != null) {
                    intent.putExtra(MODIFY_HOUR, hoursListItems[position].getHour());
                    intent.putExtra(MODIFY_DATE, hoursListItems[position].getDate());
                    intent.putExtra(EXISTING_DESC, hoursListItems[position].getTaskDesc());
                    modifyPosition = position;
                    startActivityForResult(intent, MODIFY_TASK_DESC_REQUEST);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MODIFY_TASK_DESC_REQUEST && resultCode == Activity.RESULT_OK) {
            String newTaskDesc = data.getStringExtra(ChangeMessageActivity.TASK_DESC);
            storageManager.getHoursListItem(modifyPosition).setTaskDesc(newTaskDesc);
            refreshList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNotificationsToggleClicked(View view) {
        storageManager.setNotificationsActive(((ToggleButton) view).isChecked(), this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        storageManager.pushTasksToStorage(this);
    }
}
