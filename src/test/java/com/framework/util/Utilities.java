package com.framework.util;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.Random;

public class Utilities {
    public static String GetCurrentDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String GetRandomNumber() {
        Random rand = new Random();
        int value = rand.nextInt(999999);
        return String.valueOf(value);
    }

}
