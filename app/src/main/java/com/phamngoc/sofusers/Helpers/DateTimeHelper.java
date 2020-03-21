package com.phamngoc.sofusers.Helpers;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class DateTimeHelper {

    public static String GetDateFromTimeStand(long time){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time * 1000);
        String daylastactive = DateFormat.format("dd MMM yyyy", cal).toString();
        return daylastactive;
    }
}
