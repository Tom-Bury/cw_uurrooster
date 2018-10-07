package com.bury.tom.uurrooster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingsCourseEntryAdapter extends ArrayAdapter<SettingsCourseEntry> {

    private ArrayList<SettingsCourseEntry> enrolledCourses;


    public SettingsCourseEntryAdapter(@NonNull Context context, ArrayList<SettingsCourseEntry> enrties, ArrayList<SettingsCourseEntry> enrolled) {
        super(context, 0, enrties);
        this.enrolledCourses = enrolled;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.settings_entry, parent,
                    false);
        }

        SettingsCourseEntry currEntry = getItem(position);

        RelativeLayout wholeEntry = listItemView.findViewById(R.id.settings_whole_entry);

        TextView titleTV = listItemView.findViewById(R.id.settings_course_title);
        TextView idTV = listItemView.findViewById(R.id.settings_course_id);

        titleTV.setText(currEntry.getCourseTitle());
        idTV.setText(currEntry.getCourseId());

        if (enrolledCourses == null) {
            enrolledCourses = new ArrayList<>();
            return listItemView;
        }

        if (enrolledCourses.contains(currEntry)) {
            wholeEntry.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }
        else {
            wholeEntry.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        return listItemView;
    }

    public void addEntry(SettingsCourseEntry entry){
        this.enrolledCourses.add(entry);
    }

    public void removeEntry(SettingsCourseEntry entry) {
        this.enrolledCourses.remove(entry);
    }
}
