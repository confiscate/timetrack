package com.example.henry.timetrack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


public class ChangeMessageActivity extends ActionBarActivity {
    public static String TASK_DESC = "com.example.henry.TASK_DESC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String hour = intent.getStringExtra(MainActivity.MODIFY_HOUR);
        String date = intent.getStringExtra(MainActivity.MODIFY_DATE);

        setContentView(R.layout.activity_display_message);
        ((TextView) findViewById(R.id.task_placeholder_hour)).setText(hour);
        ((TextView) findViewById(R.id.task_placeholder_date)).setText(date);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        Intent result = new Intent();
        result.putExtra(TASK_DESC, message == null ? "" : message);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
