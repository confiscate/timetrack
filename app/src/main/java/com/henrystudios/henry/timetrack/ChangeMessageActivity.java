package com.henrystudios.henry.timetrack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.henrystudios.henry.timetrack.R;


public class ChangeMessageActivity extends ActionBarActivity {
    public static String TASK_DESC = "com.henrystudios.henry.TASK_DESC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String hour = intent.getStringExtra(MainActivity.MODIFY_HOUR);
        String date = intent.getStringExtra(MainActivity.MODIFY_DATE);
        String existingDesc = intent.getStringExtra(MainActivity.EXISTING_DESC);

        ((TextView) findViewById(R.id.task_placeholder_hour)).setText(hour);
        ((TextView) findViewById(R.id.task_placeholder_date)).setText(date);
        if (existingDesc != null) {
            EditText editText = (EditText) findViewById(R.id.edit_message);
            editText.setText(existingDesc);
            editText.setSelection(existingDesc.length());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onSave(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        Intent result = new Intent();
        result.putExtra(TASK_DESC, message == null ? "" : message);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    public void onCancel(View view) {
        finish();
    }
}
