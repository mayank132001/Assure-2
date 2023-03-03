package com.app.effistay.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void log(String msg) {
        Log.v("Assure", msg);
    }

    public static boolean checkDates(String endDate) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dfDate.format(c.getTime());
        boolean b = false;

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                b = false;  // If start date is before end date.
            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                b = true;  // If two dates are equal.
            } else {
                b = false; // If start date is after the end date.
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static boolean compareDate(String startDate, String endDate) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            String d1 = dfDate.format(dfDate.parse(startDate));
            String d2 = dfDate.format(dfDate.parse(endDate));
            Utils.log("==== checkout date " + dfDate.format(dfDate.parse(startDate)) + " " + dfDate.format(dfDate.parse(endDate)));
            if (d1.equals(d2)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static boolean checkDates(String startDate, String endDate) {
        SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                b = false;  // If start date is before end date.
            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                b = true;  // If two dates are equal.
            } else {
                b = false; // If start date is after the end date.
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static boolean isDateNextDay(String start, String end) {
        try {
            SimpleDateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date startDate = dfDate.parse(start);
            Date endDate = dfDate.parse(end);
            //milliseconds
            long different = startDate.getTime() - endDate.getTime();

            System.out.println("++ startDate : " + startDate);
            System.out.println("++ endDate : " + endDate);
            System.out.println("++  different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;
            log("==== aa " + String.format("%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds));

            if (elapsedMinutes > 0 || elapsedHours > 0 || elapsedDays > 0) {
                return true;
            } else {
                return false;
            }

//            System.out.printf(
//                    "%d days, %d hours, %d minutes, %d seconds%n",
//                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
