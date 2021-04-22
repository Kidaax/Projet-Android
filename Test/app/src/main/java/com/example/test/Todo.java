package com.example.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Todo {
    private int id;
    private String name;
    private boolean isDone;
    private long dateInMs;

    public Todo(int id, String name, long date) {
        this.id = id;
        this.name = name;
        this.dateInMs = date;
    }

    public Long getDate() {
        return dateInMs;
    }

    public Calendar getDateAsCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateInMs);
        return calendar;
    }

    public void setDay(int year, int month, int dayOfMonth) {
        Calendar calendar = getDateAsCalendar();
        calendar.set(year, month, dayOfMonth);
        this.dateInMs = calendar.getTimeInMillis();
    }

    public String getFormattedDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String day = sdf.format(dateInMs);
        return day;
    }

    public void setTime(int hour, int minute) {
        Calendar calendar = getDateAsCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        this.dateInMs = calendar.getTimeInMillis();
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String day = sdf.format(dateInMs);
        return day;

    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "name='" + name + '\''
                ;
    }
}


