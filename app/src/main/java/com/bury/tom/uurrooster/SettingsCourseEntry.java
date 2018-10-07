package com.bury.tom.uurrooster;

public class SettingsCourseEntry {

    private String courseId;
    private String courseTitle;


    public SettingsCourseEntry(String preferenceEntry) {
        int flag = preferenceEntry.indexOf("|");
        this.courseId = preferenceEntry.substring(0, flag);
        this.courseTitle = preferenceEntry.substring(flag+1);
    }

    public SettingsCourseEntry(String courseId, String courseTitle) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public String getPreferenceEntry() {
        return courseId + "|" + courseTitle;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SettingsCourseEntry) {
            return ((SettingsCourseEntry) obj).getCourseId().equals(this.courseId);
        }
        else return false;
    }
}
