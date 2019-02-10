package com.bury.tom.uurrooster;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HtmlRosterParser {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final int LAST_WEEK_OF_FIRST_SEMESTER = 51;
    private static final int LAST_WEEK_OF_SECOND_SEMESTER = 21;



    public static ArrayList<RosterEntry> formatData(String data, ArrayList<String> enrolledCourses, boolean is2ndSemester) {

        if (data.isEmpty()) {
            return null;
        }

        data = removeFirstBit(data);
        data = removeLastBit(data);

        ArrayList<RosterEntry> roster = new ArrayList<>();


        String startWeek = "<h2>Week";
        String endWeek = "<h2>Week 40</h2><hr>";
        String startHours = "<tr><td>";
        String startRoom = "</td><td>in</td></td><td>";
        String endRoom = "</td><td>:</td>";
        String startCourseId = "<td><a href=\"http://onderwijsaanbod.kuleuven.be/syllabi/";
        String endCourseId = ".htm\",";
        String startTitle = "<font";
        String endTitle = "</font>";

        int lastWeek = is2ndSemester ? LAST_WEEK_OF_SECOND_SEMESTER : LAST_WEEK_OF_FIRST_SEMESTER;

        int weekIndex = 0;
        String currDate = null;
        boolean first = false;

        int i = 0;

        while (weekIndex <= lastWeek) {

            String currWeek = data.substring(i, i + startWeek.length());

            if (currWeek.equals(startWeek)) {
                String thisWeek= data.substring(i + startWeek.length() + 1, i + startWeek.length() + 3);
                if (thisWeek.contains("<")) thisWeek = thisWeek.substring(0, 1);

                weekIndex = Integer.valueOf(thisWeek)+1;
                i += endWeek.length();
                continue;
            }

            String currDay = data.substring(i, i + 6);

            if (currDay.equals("<i><b>")) {
                int dayEndIndex = data.indexOf(":</i>", i);
                currDate = data.substring(i + 6, dayEndIndex);
                first = true;
            }


            if (data.substring(i, i + startHours.length()).equals(startHours)) {
                String startingTime = data.substring(i + 8, i + 13);
                String endingTime = data.substring(i + 20, i + 25);

                int roomStartIndex = data.indexOf(startRoom, i) + startRoom.length();
                int roomEndIndex = data.indexOf(endRoom, i);
                String room = data.substring(roomStartIndex, roomEndIndex);

                while (room.contains("  ")) {
                    room = room.replaceAll("  ", " ");
                }

                int courseIdStartIndex = data.indexOf(startCourseId, i) + startCourseId.length() + 2;
                int courseIdEndIndex = data.indexOf(endCourseId, i);
                String courseId = data.substring(courseIdStartIndex, courseIdEndIndex);

                int startTitleIndex = data.indexOf(">", data.indexOf(startTitle, i)) + 1;
                int endTitleIndex = data.indexOf(endTitle, i);
                String courseTitle = data.substring(startTitleIndex, endTitleIndex);

                RosterEntry currCourse = new RosterEntry(courseTitle, room, startingTime, endingTime,
                        weekIndex, currDate, courseId, first);


                if (enrolledCourses.contains(courseId)) {
                    roster.add(currCourse);
                    first = false;
                }

            }

            i++;
        }

        printRoster(roster);

        return roster;
    }


    private static String removeFirstBit(String data) {
        int beginIndex = data.indexOf("<h2>Week ");
        String trimmedData = data.substring(beginIndex);
        return trimmedData;
    }

    private static String removeLastBit(String data) {
        int beginIndex = data.indexOf("(<i>Opmerking</i>:");
        Log.e(LOG_TAG, "index: " + beginIndex);
        String trimmedData = data.substring(0, beginIndex);
        return trimmedData;
    }



    public static ArrayList<SettingsCourseEntry> fetchAllCourses(String data, boolean is2ndSemester) {

        if (data.isEmpty()) {
            return null;
        }

        data = removeFirstBit(data);
        data = removeLastBit(data);

        ArrayList<SettingsCourseEntry> allCourses = new ArrayList<>();
        Set<String> addedCourses = new HashSet<>();


        String startWeek = "<h2>Week";
        String endWeek = "<h2>Week 40</h2><hr>";
        String startCourseId = "<td><a href=\"http://onderwijsaanbod.kuleuven.be/syllabi/";
        String endCourseId = ".htm\",";
        String startTitle = "<font";
        String endTitle = "</font>";

        int weekIndex = 0;

        int lastWeek = is2ndSemester ? LAST_WEEK_OF_SECOND_SEMESTER : LAST_WEEK_OF_FIRST_SEMESTER;

        int i = 0;

        while (weekIndex <= lastWeek) {

            String currWeek = data.substring(i, i + startWeek.length());

            if (currWeek.equals(startWeek)) {
                String thisWeek= data.substring(i + startWeek.length() + 1, i + startWeek.length() + 3);
                if (thisWeek.contains("<")) thisWeek = thisWeek.substring(0, 1);

                weekIndex = Integer.valueOf(thisWeek)+1;
                i += endWeek.length();
                continue;
            }

            if (data.substring(i, i + startCourseId.length()).equals(startCourseId)) {
                int courseIdStartIndex = data.indexOf(startCourseId, i) + startCourseId.length() + 2;
                int courseIdEndIndex = data.indexOf(endCourseId, i);
                String courseId = data.substring(courseIdStartIndex, courseIdEndIndex);



                if (!addedCourses.contains(courseId)) {
                    int startTitleIndex = data.indexOf(">", data.indexOf(startTitle, i)) + 1;
                    int endTitleIndex = data.indexOf(endTitle, i);
                    String courseTitle = data.substring(startTitleIndex, endTitleIndex);

                    int lastPartTitle = courseTitle.indexOf(":");

                    if (lastPartTitle != -1 && !courseTitle.contains("Capita")) {
                        courseTitle = courseTitle.substring(0, lastPartTitle);
                    }

                    allCourses.add(new SettingsCourseEntry(courseId, courseTitle));
                    addedCourses.add(courseId);
                }



            }

            i++;

        }

        return allCourses;
    }

    private static void printRoster(ArrayList<RosterEntry> roster) {
        System.out.printf("=== CURRENT ROSTER ===\n");
        for (RosterEntry r : roster) {
            System.out.println(r.toString());
        }
    }




    public static ArrayList<RosterEntry> formatData2(String data, ArrayList<String> enrolledCourses, boolean is2ndSemester) {

        if (data.isEmpty()) {
            return null;
        }

        ArrayList<RosterEntry> roster = new ArrayList<>();

        String startWeek = "<h2>Week";
        String startHours = "<tr><td>";
        String startRoom = "</td><td>in</td></td><td>";
        String endRoom = "</td><td>:</td>";
        String startCourseId = "<td><a href=\"http://onderwijsaanbod.kuleuven.be/syllabi/";
        String endCourseId = ".htm\",";
        String startTitle = "<font";
        String endTitle = "</font>";
        String stop = "</html>";
        int lastWeek = is2ndSemester ? LAST_WEEK_OF_SECOND_SEMESTER : LAST_WEEK_OF_FIRST_SEMESTER;

        int weekIndex = 0;
        String currDate = null;
        boolean first = false;
        int stopIndex = data.indexOf(stop);

        int i = data.indexOf(startWeek);

        System.out.println("starting while loop with index " + i);
        System.out.println("and stopindex " + stopIndex);

        while (i <= stopIndex - 10) {

            String currWeek = data.substring(i, i + startWeek.length());

            if (currWeek.equals(startWeek)) {
                String thisWeek= data.substring(i + startWeek.length() + 1, i + startWeek.length() + 3);
                if (thisWeek.contains("<")) thisWeek = thisWeek.substring(0, 1);

                weekIndex = Integer.valueOf(thisWeek)+1;
                i++;
                continue;
            }

            String currDay = data.substring(i, i + 6);

            if (currDay.equals("<i><b>")) {
                int dayEndIndex = data.indexOf(":</i>", i);
                currDate = data.substring(i + 6, dayEndIndex);
                first = true;
            }


            if (data.substring(i, i + startHours.length()).equals(startHours)) {
                String startingTime = data.substring(i + 8, i + 13);
                String endingTime = data.substring(i + 20, i + 25);

                int roomStartIndex = data.indexOf(startRoom, i) + startRoom.length();
                int roomEndIndex = data.indexOf(endRoom, i);
                String room = data.substring(roomStartIndex, roomEndIndex);

                while (room.contains("  ")) {
                    room = room.replaceAll("  ", " ");
                }

                int courseIdStartIndex = data.indexOf(startCourseId, i) + startCourseId.length() + 2;
                int courseIdEndIndex = data.indexOf(endCourseId, i);
                String courseId = data.substring(courseIdStartIndex, courseIdEndIndex);

                int startTitleIndex = data.indexOf(">", data.indexOf(startTitle, i)) + 1;
                int endTitleIndex = data.indexOf(endTitle, i);
                String courseTitle = data.substring(startTitleIndex, endTitleIndex);

                RosterEntry currCourse = new RosterEntry(courseTitle, room, startingTime, endingTime,
                        weekIndex, currDate, courseId, first);


                if (enrolledCourses.contains(courseId)) {
                    roster.add(currCourse);
                    first = false;
                }

            }

            i++;
        }

//        printRoster(roster);

        return roster;
    }

}



