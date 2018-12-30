package com.amazon.reminder.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class ReminderModel {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final static SimpleDateFormat completeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    private String title;
    private Date date;
    private Date time;
    private Integer id = null;
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

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateInString() {
        if (date == null || time == null) {
            return null;
        }
        return dateFormat.format(date) + " " +
                timeFormat.format(time);
    }

    public int getTimeHashCode() {
        return Objects.hash(date, time);
    }

    public Calendar getCompleteDate() {
        String dateStr =  getDateInString();
        if (dateStr == null) {
            return null;
        }
        try {
            Date date = completeFormat.parse(dateStr);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected parse error");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReminderModel that = (ReminderModel) o;
        return enabled == that.enabled &&
                Objects.equals(title, that.title) &&
                Objects.equals(date, that.date) &&
                Objects.equals(time, that.time) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date, time, id, enabled);
    }

    @Override
    public String toString() {
        return "ReminderModel{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", id=" + id +
                ", enabled=" + enabled +
                '}';
    }
}
