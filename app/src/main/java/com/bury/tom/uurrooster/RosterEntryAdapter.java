package com.bury.tom.uurrooster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RosterEntryAdapter extends ArrayAdapter<RosterEntry> {

    private ArrayList<RosterEntry> entries;



    public RosterEntryAdapter(@NonNull Context context, ArrayList<RosterEntry> entries) {
        super(context, 0, entries);
        this.entries = entries;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.roster_entry, parent,
                    false);
        }

        RosterEntry currEntry = getItem(position);

        if (currEntry == null) {
            System.out.println("The entry is null...");
            return listItemView;
        }

        RelativeLayout entry = listItemView.findViewById(R.id.whole_entry);

        View weekSeparator = listItemView.findViewById(R.id.week_separator);
        TextView daySeparatorTV = listItemView.findViewById(R.id.day_separator);
        TextView timeTV = listItemView.findViewById(R.id.timeTV);
        TextView roomTV = listItemView.findViewById(R.id.roomTV);
        TextView titleTV = listItemView.findViewById(R.id.titleTV);

        timeTV.setText(currEntry.getTime());
        roomTV.setText(currEntry.getRoom());
        titleTV.setText(currEntry.getTitle());

        weekSeparator.setVisibility(View.GONE);

        if (currEntry.isFirstOfDay() || position == 0) {
            daySeparatorTV.setVisibility(View.VISIBLE);
            daySeparatorTV.setText(currEntry.getDate());


            if (isNewWeek(position)) {
                weekSeparator.setVisibility(View.VISIBLE);
            }
        }
        else {
            daySeparatorTV.setVisibility(View.GONE);
        }


        if (currEntry.isInThePast() && !currEntry.isToday()) {
            titleTV.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_outdated_data));
        }
        else {
            titleTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }



        if (currEntry.isToday()) {
            entry.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

            if (timeOverlap(position)) {
                timeTV.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
            }
            else {
                timeTV.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            }
        }
        else {
            entry.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

            if (timeOverlap(position)) {
                timeTV.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
            }
            else {
                timeTV.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            }
        }

        return listItemView;
    }



    boolean timeOverlap(int currPos) {
        if (currPos == 0) {
            return false;
        }
        RosterEntry currEntry = getItem(currPos);
        RosterEntry prevEntry = getItem(currPos - 1);




        if (currPos < this.entries.size() - 1) {
            RosterEntry nextEntry = getItem(currPos + 1);
//            System.out.println("\n");
//            System.out.println("prev: " + prevEntry.toString());
//            System.out.println("curr: " + currEntry.toString());
//            System.out.printf("next: " + nextEntry.toString());
            return currEntry.overlapsWith(prevEntry) || currEntry.overlapsWith(nextEntry);
        }
        else {
//            System.out.println("\n");
//            System.out.println("prev: " + prevEntry.toString());
//            System.out.println("curr: " + currEntry.toString());
            return currEntry.overlapsWith(prevEntry);
        }
    }


    boolean isNewWeek(int currPos) {

        if (currPos == 0) {
            return false;
        }
        else {
            RosterEntry currEntry = getItem(currPos);
            RosterEntry prevEntry = getItem(currPos - 1);

            String currDate = currEntry.getDate();
            String prevDate = prevEntry.getDate();

            if (dateToDayInt(currDate) < dateToDayInt(prevDate)) {
                return true;
            }
            else {
                return false;
            }

        }
    }

    int dateToDayInt(String date) {
        if (date == null) {
            return -1;
        }

        int space = date.indexOf(' ');
        String dayName = date.substring(0, space);

        int dayInt;
        switch (dayName) {
            case "Maandag":
                dayInt = 0;
                break;
            case "Dinsdag":
                dayInt = 1;
                break;
            case "Woensdag":
                dayInt = 2;
                break;
            case "Donderdag":
                dayInt = 3;
                break;
            case "Vrijdag":
                dayInt = 4;
                break;
            default:
                dayInt = -1;
        }
        return dayInt;
    }


}
