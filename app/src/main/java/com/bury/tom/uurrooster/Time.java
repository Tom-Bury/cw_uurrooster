package com.bury.tom.uurrooster;

public class Time {

    private int hour;
    private int min;

    public Time(int hour, int min) {
        this.hour = hour;
        this.min = min;
    }

    public Time(String time) {
        this.hour = Integer.valueOf(time.substring(0,2));
        this.min = Integer.valueOf(time.substring(3));
    }

    boolean isBefore(Time other) {
        return (this.hour < other.hour ||
                this.hour == other.hour && this.min <= other.min);
    }

    @Override
    public String toString() {
        return hour + ":" + min;
    }
}
