package com.amazon.reminder.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ReminderModel {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private String title;
    private Date date;
    private Date time;
    private Long id = null;
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTime() {
        if (time == null) {
            return null;
        }
        return timeFormat.format(time);
    }

    public void setTime(int hour, int minute) {
        try {
            this.time = timeFormat.parse(hour + ":" + minute);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setTime(String time) {
        try {
            this.time = timeFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDateValue() {
        if (date == null) {
            return null;
        }

        return dateFormat.format(date);
    }

    public void setDateValue(String date) {
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void setDateValue(int year, int month, int days) {
        try {
            this.date = dateFormat.parse(days + "-" + month + "-" + year);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDateInString() {
        Log.i("amazon", ""+date);
        Log.i("amazon", ""+time);
        return dateFormat.format(date) + " " +
                timeFormat.format(time);
    }

    @Override
    public int hashCode() {

        return Objects.hash(date, time);
    }
}
