package com.bury.tom.uurrooster;

import android.provider.ContactsContract;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RosterEntry {

    private String title;
    private String room;
    private String startingTime;
    private String endingTime;
    private int weekNumber;
    private String date;
    private String courseId;
    private boolean isFirstOfDay;

    public RosterEntry(String title, String room, String startingTime, String endingTime,
                       int weekNumber, String date, String courseId, boolean isFirstOfDay) {
        this.title = title;
        this.room = room;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
        this.weekNumber = weekNumber;
        this.courseId = courseId;
        this.isFirstOfDay = isFirstOfDay;
        this.date = date;
    }


    public String getTitle() {
        return title;
    }

    public String getRoom() {
        return room;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public String getEndingTime() {
        return endingTime;
    }

    public String getDate() {
        return date;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getTime() {
        return this.startingTime + " - " + this.endingTime;
    }

    public boolean isFirstOfDay() {
        return isFirstOfDay;
    }

    public boolean isToday() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = formatter.format(today);
        return getDate().contains(formattedDate);
    }

    public boolean isInThePast() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        Date entryDate = null;
        String entryDateStr = getDate().substring(getDate().length() - 10);


        try {
            entryDate = formatter.parse(entryDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Date parse error", "Could not parse the date of this entry");
            return false;
        }

        boolean entryIsInThePast = today.after(entryDate);
        return entryIsInThePast;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(startingTime);
        result.append("-");
        result.append(endingTime);
        result.append(" : ");
        result.append(title);
        result.append(" | ");
        result.append(room);
        return result.toString();
    }
}
