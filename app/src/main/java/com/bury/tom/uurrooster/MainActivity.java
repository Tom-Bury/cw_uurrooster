package com.bury.tom.uurrooster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    private final String EFFECTIVE_ROSTER_URL_1 =  "https://people.cs.kuleuven.be/~btw/roosters1819/cws_semester_1.html";
    private final String EFFECTIVE_ROSTER_URL_2 =  "https://people.cs.kuleuven.be/~btw/roosters1819/cws_semester_2.html";
    private final int LAST_WEEK_FIRST_SEMESTER = 21;

    private ConnectivityManager cm;
    private ArrayList<RosterEntry> roster;
    private RosterEntryAdapter rosterEntryAdapter;
    private SharedPreferences preferences;

    private static final String[] MY_COURSES = {
            "H04G1BN", "H04H5B", "H04J9B", "G0B23A", "H04I4A", "G0K32A",
            "H0N03AE", "H0N08AE", "H0N00AE", "H0N05AE", "H0N05AN", "G0K32AN"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        this.preferences = this.getSharedPreferences(PreferencesUtils.SHARED_PREFS, MODE_PRIVATE);

        checkForConnectivityAndStartLoading();
    }

    private void checkForConnectivityAndStartLoading() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Start the AsyncTask to fetch the earthquake data
            System.out.println("You're connected!");
            getSupportLoaderManager().initLoader(0, null, this);
        } else {
            // Display no internet empty list message
            String oldData = PreferencesUtils.getSavedDataFromPreferences(preferences);

            if (oldData != null) {
                displayData(oldData);
            } else {
                TextView emptyList = findViewById(R.id.testTextView);
                emptyList.setText("No internet & no saved data :c");
                emptyList.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Display no internet empty list message
        String oldData = PreferencesUtils.getSavedDataFromPreferences(preferences);

        if (oldData != null) {
            displayData(oldData);
        } else {
            TextView emptyList = findViewById(R.id.testTextView);
            emptyList.setText("No internet & no saved data :c");
            emptyList.setVisibility(View.VISIBLE);
        }


    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable Bundle bundle) {
        String url;
        if (is2ndSemester()) {
            url = EFFECTIVE_ROSTER_URL_2;
        }
        else {
            url = EFFECTIVE_ROSTER_URL_1;
        }

        return new RosterLoader(this, url);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String result) {

        System.out.println("This is the result from the loader: ");
        System.out.println(result + "\n\n\n");
        ArrayList<SettingsCourseEntry> allCourses = HtmlRosterParser.fetchAllCourses(result, is2ndSemester());
        PreferencesUtils.addCoursesToPreferences(PreferencesUtils.PREFS_KEY_ALL_COURSES, allCourses, preferences);


        ArrayList<String> enrolledCourses = PreferencesUtils.getCourseIDs(PreferencesUtils.PREFS_KEY_MY_COURSES, preferences);
        ArrayList<String> allCoursesAgain = PreferencesUtils.getCourseIDs(PreferencesUtils.PREFS_KEY_ALL_COURSES, preferences);

        if (enrolledCourses == null) {
            enrolledCourses = allCoursesAgain;
        }

        PreferencesUtils.addDataToPreferences(result,preferences);

        this.roster = HtmlRosterParser.formatData2(result, enrolledCourses, is2ndSemester());
        this.rosterEntryAdapter = new RosterEntryAdapter(this, this.roster);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(this.rosterEntryAdapter);
        listView.setDivider(null);

        ProgressBar loaderSpinner = findViewById(R.id.loading_spinner);
        loaderSpinner.setVisibility(View.GONE);
    }


    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        TextView testTextView = findViewById(R.id.testTextView);
        testTextView.setText("Loader reset");
    }


    private void displayData(String data) {
        ArrayList<String> enrolledCourses = PreferencesUtils.getCourseIDs(PreferencesUtils.PREFS_KEY_MY_COURSES, preferences);
        ArrayList<String> allCoursesAgain = PreferencesUtils.getCourseIDs(PreferencesUtils.PREFS_KEY_ALL_COURSES, preferences);

        if (enrolledCourses == null || enrolledCourses.size() == 0) {
            enrolledCourses = allCoursesAgain;
        }

        this.roster = HtmlRosterParser.formatData2(data, enrolledCourses, is2ndSemester());
        this.rosterEntryAdapter = new RosterEntryAdapter(this, this.roster);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(this.rosterEntryAdapter);
        listView.setDivider(null);

        ProgressBar loaderSpinner = findViewById(R.id.loading_spinner);
        loaderSpinner.setVisibility(View.GONE);

    }


    /*
    OPTIONS MENU
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private boolean is2ndSemester() {
        return  getCurrentWeek() <= LAST_WEEK_FIRST_SEMESTER;
    }

    private static int getCurrentWeek() {
        Calendar currDate = Calendar.getInstance();
        return currDate.get(Calendar.WEEK_OF_YEAR);
    }
}
