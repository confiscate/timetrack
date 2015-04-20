package com.example.henry.timetrack;

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
    public final static String EXTRA_MESSAGE = "com.example.henry.MESSAGE";
    public CognitoCachingCredentialsProvider credentialsProvider;
    public CognitoSyncManager syncClient;
    private String[] hours = new String[24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //authenticateAWS();
        //syncAWS()

        setContentView(R.layout.activity_main);

        fillHoursLog();
    }

    private void authenticateAWS() {
        credentialsProvider = new CognitoCachingCredentialsProvider(
                this, // Context
                "", // Identity Pool ID
                null // Region
        );

        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                this,
                Regions.US_EAST_1, // Region
                credentialsProvider);
    }

    private void syncAWS() {
        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("lemontea");
        dataset.put("vita", "hentest2");

        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
            }
        });
    }

    private void fillHoursLog() {
        Calendar curTime = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:00 aa    dd-MM-yyyy");
        for (int i = 0; i < 24; i++) {
            hours[i] = dateFormat.format(curTime.getTime());
            curTime.add(Calendar.HOUR, -1);
        }
        final ListView hoursLog = (ListView) findViewById(R.id.hourslog);
        ArrayAdapter<String> hoursLogArrayAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, hours);
        hoursLog.setAdapter(hoursLogArrayAdapter);

        final Intent intent = new Intent(this, DisplayMessageActivity.class);
        hoursLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (position < hours.length && hours[position] != null) {
                    String message = "hmm " + hours[position];
                    intent.putExtra(EXTRA_MESSAGE, message);
                    startActivity(intent);
                }
            }
        });
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
        if (((ToggleButton) view).isChecked()) {

        }
    }
}
