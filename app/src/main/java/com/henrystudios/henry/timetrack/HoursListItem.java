package com.henrystudios.henry.timetrack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by henry on 4/20/15.
 */
public class HoursListItem implements Comparable {
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

    @Override
    public int compareTo(Object other) {
        SimpleDateFormat format = new SimpleDateFormat("h:00 aa dd-MM-yyyy");
        HoursListItem otherItem = (HoursListItem) other;
        try {
            Date otherDate = format.parse(otherItem.hour + " " + otherItem.date);
            Date thisDate = format.parse(hour + " " + date);
            return thisDate.compareTo(otherDate);
        } catch (ParseException e) {
            return 0;
        }
    }
}
