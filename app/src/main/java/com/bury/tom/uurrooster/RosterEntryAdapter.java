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


    public RosterEntryAdapter(@NonNull Context context, ArrayList<RosterEntry> entries) {
        super(context, 0, entries);
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

        if (currEntry.isFirstOfDay()) {
            daySeparatorTV.setVisibility(View.VISIBLE);
            daySeparatorTV.setText(currEntry.getDate());

            if (currEntry.getDate().contains("Maandag") && position != 0) {
                weekSeparator.setVisibility(View.VISIBLE);
            }
        }
        else {
            daySeparatorTV.setVisibility(View.GONE);
        }

        if (currEntry.isToday()) {
            entry.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }
        else {
            entry.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        if (currEntry.isInThePast() && !currEntry.isToday()) {
            titleTV.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_outdated_data));
        }
        else {
            titleTV.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }



        return listItemView;
    }

}
