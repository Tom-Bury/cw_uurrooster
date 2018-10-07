package com.bury.tom.uurrooster;

import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private ArrayList<SettingsCourseEntry> allCourses;
    private ArrayList<SettingsCourseEntry> enrolledCourses;
    private SettingsCourseEntryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.preferences = getSharedPreferences(PreferencesUtils.SHARED_PREFS, MODE_PRIVATE);


        this.allCourses = PreferencesUtils.getCoursesFromPreferencesAsArrayList(
                PreferencesUtils.PREFS_KEY_ALL_COURSES, this.preferences);
        this.enrolledCourses= PreferencesUtils.getCoursesFromPreferencesAsArrayList(
                PreferencesUtils.PREFS_KEY_MY_COURSES, this.preferences);

        this.adapter = new SettingsCourseEntryAdapter(this, allCourses, enrolledCourses);
        ListView listView = findViewById(R.id.settings_list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingsCourseEntry clickedEntry = (SettingsCourseEntry) parent.getItemAtPosition(position);
                SettingsCourseEntryAdapter adapter = (SettingsCourseEntryAdapter) parent.getAdapter();

                if (enrolledCourses == null) {
                    enrolledCourses = new ArrayList<>();
                }

                if (enrolledCourses.contains(clickedEntry)) {
                    enrolledCourses.remove(clickedEntry);
                    adapter.removeEntry(clickedEntry);
                    view.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.white));
                }
                else {
                    enrolledCourses.add(clickedEntry);
                    adapter.addEntry(clickedEntry);
                    view.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.colorAccent));
                }

                PreferencesUtils.addCoursesToPreferences(PreferencesUtils.PREFS_KEY_MY_COURSES,
                        enrolledCourses,getSharedPreferences(PreferencesUtils.SHARED_PREFS,MODE_PRIVATE ));
            }
        });



    }






}
