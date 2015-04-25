package com.example.henry.timetrack;

import com.amazonaws.mobileconnectors.cognito.Dataset;

import java.util.Map;

/**
 * Created by henry on 4/20/15.
 */
public class HoursListItem {
    private String hour;
    private String date;
    private String taskDesc;

    public HoursListItem(String hour, String date, String taskDesc) {
        this.hour = hour;
        this.date = date;
        this.taskDesc = taskDesc;
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

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }
}
