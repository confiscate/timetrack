package com.example.henry.timetrack;

import android.app.Activity;
import android.content.Intent;
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
import java.util.List;

public class MainActivity extends ActionBarActivity {
    public final static String MODIFY_HOUR = "com.example.henry.MODIFY_HOUR";
    public final static String MODIFY_DATE = "com.example.henry.MODIFY_DATE";
    public final static String EXISTING_DESC = "com.example.henry.EXISTING_DESC";
    public static final int MODIFY_TASK_DESC_REQUEST = 1;

    public CognitoCachingCredentialsProvider credentialsProvider;
    public CognitoSyncManager syncClient;
    private HoursListItem[] hoursListItems = new HoursListItem[24];
    private SimpleDateFormat hourFormat = new SimpleDateFormat("h:00 aa");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Dataset notificationDataset;
    private Dataset hourTaskDataset;
    private int modifyPosition = 0;
    private String[] hoursDisplay = new String[24];
    private ArrayAdapter<String> hoursLogArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authenticateAWS();
        setContentView(R.layout.activity_main);
        fillHoursLog();
    }

    private void authenticateAWS() {
        credentialsProvider = new CognitoCachingCredentialsProvider(
                this, // Context
                "", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );

        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                this,
                Regions.US_EAST_1, // Region
                credentialsProvider);

        // Create a record in a dataset and synchronize with the server
        hourTaskDataset = syncClient.openOrCreateDataset("hourTaskDataset");
        synchronizeDataset(hourTaskDataset);
        notificationDataset = syncClient.openOrCreateDataset("notificationDataset");
        synchronizeDataset(notificationDataset);
    }

    private void refreshList() {
        for (int i = 0; i < 24; i++) {
            hoursDisplay[i] = hoursListItems[i].getDisplayMessage();
        }
        if (hoursLogArrayAdapter != null) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hoursLogArrayAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void synchronizeDataset(Dataset dataset) {
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
            }
        });
    }

    private void fillHoursLog() {
        Calendar curTime = Calendar.getInstance();
        for (int i = 0; i < 24; i++) {
            hoursListItems[i] = new HoursListItem(hourFormat.format(curTime.getTime()),
                    dateFormat.format(curTime.getTime()), hourTaskDataset);
            hoursDisplay[i] = hoursListItems[i].getDisplayMessage();
            curTime.add(Calendar.HOUR, -1);
        }
        final ListView hoursLog = (ListView) findViewById(R.id.hourslog);
        hoursLogArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, hoursDisplay);
        hoursLog.setAdapter(hoursLogArrayAdapter);

        final Intent intent = new Intent(this, ChangeMessageActivity.class);
            hoursLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    if (position < hoursListItems.length && hoursListItems[position] != null) {
                        intent.putExtra(MODIFY_HOUR, hoursListItems[position].getHour());
                        intent.putExtra(MODIFY_DATE, hoursListItems[position].getDate());
                        String key = hoursListItems[position].getKey();
                        intent.putExtra(EXISTING_DESC, hourTaskDataset.get(key));
                        modifyPosition = position;
                        startActivityForResult(intent, MODIFY_TASK_DESC_REQUEST);
                    }
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MODIFY_TASK_DESC_REQUEST && resultCode == Activity.RESULT_OK) {
            String key = hoursListItems[modifyPosition].getKey();
            String newTaskDesc = data.getStringExtra(ChangeMessageActivity.TASK_DESC);
            if (hourTaskDataset != null) {
                if (newTaskDesc.equals("")) {
                    hourTaskDataset.remove(key);
                } else {
                    hourTaskDataset.put(key, newTaskDesc);
                }
                hourTaskDataset.synchronize(new DefaultSyncCallback() {
                    @Override
                    public void onSuccess(Dataset dataset, List newRecords) {
                        refreshList();
                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onNotificationsToggleClicked(View view) {
        Boolean on = ((ToggleButton) view).isChecked();
        notificationDataset.put("on", on.toString());
        synchronizeDataset(notificationDataset);
        if (on) {

        }
    }
}
