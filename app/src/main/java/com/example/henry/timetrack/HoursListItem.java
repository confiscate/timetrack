package com.example.henry.timetrack;

import com.amazonaws.mobileconnectors.cognito.Dataset;

/**
 * Created by henry on 4/20/15.
 */
public class HoursListItem {
    private String hour;
    private String date;
    private Dataset hourTaskDataset;

    public HoursListItem(String hour, String date, Dataset hourTaskDataset) {
        this.hour = hour;
        this.date = date;
        this.hourTaskDataset = hourTaskDataset;
    }

    public String getKey() {
        return this.hour + " " + this.date;
    }

    public String getHour() {
        return hour;
    }

    public String getDate() {
        return date;
    }

    public String getDisplayMessage() {
        String taskDesc = hourTaskDataset == null ? "" : hourTaskDataset.get(hour + " " + date);
        return hour + "     " + date + (taskDesc == null ? "" : "          " + taskDesc);
    }
}
