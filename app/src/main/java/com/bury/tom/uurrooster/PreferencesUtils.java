package com.bury.tom.uurrooster;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PreferencesUtils {

    public static final String SHARED_PREFS = "kuleuven_roster_preferences";
    public static final String PREFS_KEY_ALL_COURSES = "all_courses";
    public static final String PREFS_KEY_MY_COURSES = "my_courses";
    public static final String PREFS_KEY_OFFLINE_DATA = "offline_data";
    public static final String PREFS_KEY_SEMESTER = "selected_semester_1";



    /*
    PREFERENCES
     */

    public static void addCoursesToPreferences(String key, ArrayList<SettingsCourseEntry> courses, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> coursesSet = new HashSet<>();

        for (SettingsCourseEntry course : courses) {
            coursesSet.add(course.getPreferenceEntry());
        }

        while (!editor.putStringSet(key, coursesSet).commit()) {

        };
    }

    public static void addDataToPreferences(String data, SharedPreferences prefs) {
        SharedPreferences.Editor editor = prefs.edit();

        while (!editor.putString(PREFS_KEY_OFFLINE_DATA, data).commit()) {

        };

    }

    public static String getSavedDataFromPreferences(SharedPreferences prefs) {
        return prefs.getString(PREFS_KEY_OFFLINE_DATA, null);
    }

    public static ArrayList<String> getCourseIDs(String key, SharedPreferences prefs) {
        ArrayList<String> courseIds = new ArrayList<>();
        Set<String> courseSet = prefs.getStringSet(key, null);

        if (courseSet == null) {
            return null;
        }

        for (String course : courseSet) {
            courseIds.add(new SettingsCourseEntry(course).getCourseId());
        }

        return courseIds;
    }

    public static ArrayList<SettingsCourseEntry> getCoursesFromPreferencesAsArrayList(String key, SharedPreferences prefs) {
        ArrayList<SettingsCourseEntry> courses = new ArrayList<>();
        Set<String> courseSet = prefs.getStringSet(key, null);

        if (courseSet == null) {
            return null;
        }

        for (String course : courseSet) {
            courses.add(new SettingsCourseEntry(course));
        }

        return courses;
    }

}
