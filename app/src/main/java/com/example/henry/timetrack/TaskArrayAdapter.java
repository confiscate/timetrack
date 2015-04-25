package com.example.henry.timetrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by silvio on 4/25/15.
 */
public class TaskArrayAdapter extends ArrayAdapter<HoursListItem> {
    private final Context context;
    private final HoursListItem[] dateTasks;

    public TaskArrayAdapter(Context context, HoursListItem[] dateTasks) {
        super(context, R.layout.list_row, dateTasks);
        this.context = context;
        this.dateTasks = dateTasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row, parent, false);
        TextView dateView = (TextView) rowView.findViewById(R.id.row_date);
        TextView hourView = (TextView) rowView.findViewById(R.id.row_hour);
        TextView taskView = (TextView) rowView.findViewById(R.id.row_task);
        dateView.setText(this.dateTasks[position].getDate());
        hourView.setText(this.dateTasks[position].getHour());
        taskView.setText(this.dateTasks[position].getTaskDesc());
        return rowView;
    }
}
