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

    public Time getStartingTime() {
        return new Time(startingTime);
    }

    public Time getEndingTime() {
        return new Time(endingTime);
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
        if (getDate() == null) {
            return false;
        }
        return getDate().contains(formattedDate);
    }

    public boolean isInThePast() {
        if (getDate() == null) {
            return false;
        }

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
        result.append(date);
        result.append(" | ");
        result.append(startingTime);
        result.append("-");
        result.append(endingTime);
        result.append(" : ");
        result.append(title);
        result.append(" | ");
        result.append(room);
        return result.toString();
    }

//    boolean overlapsWith(RosterEntry other) {
//        if (other.getDate() != this.getDate()) {
//            return false;
//        }
//
//        if (this.getEndingHour() < other.getStartingHour() ||
//            other.getEndingHour() < this.getStartingHour() ||
//            this.getEndingTime() == other.getStartingTime() ||
//            other.getEndingTime() == this.getStartingTime()) {
//            return false;
//        }
//        else {
//            return true;
//        }
//    }

    boolean overlapsWith(RosterEntry other) {
        if (other.getDate() != this.getDate()) {
            return false;
        }
//        System.out.println("\n");
//        System.out.println("this start: " + this.getStartingTime().toString());
//        System.out.println("this end: " + this.getEndingTime().toString());
//        System.out.println("other start: " + other.getStartingTime().toString());
//        System.out.println("other end: " + other.getEndingTime().toString());


        if (this.getEndingTime().isBefore(other.getStartingTime()) ||
            other.getEndingTime().isBefore(this.getStartingTime())) {
            return false;
        }
        else {
            return true;
        }
    }



}
