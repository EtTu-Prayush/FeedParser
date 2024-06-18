package org.example.utils;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * ConvertTime class is used to convert a time string to milliseconds since epoch.
 * It also provides a method to parse the time string and return the month and year.
 */
public class ConvertTime {

    /**
     * Convert a time string to milliseconds since epoch.
     * @param timeStr The time string to convert.
     * @return The time in milliseconds since epoch.
     */
    public long convert(String timeStr) {
        try {
            // Attempt to parse the time string using various common formats
            Date parsedDate = DateUtils.parseDate(timeStr,
                    new String[]{
                            "yyyy-MM-dd'T'HH:mm:ss'Z'",
                            "yyyy-MM-dd HH:mm:ss",
                            "dd/MM/yyyy HH:mm:ss",
                            "EEE, dd MMM yyyy HH:mm:ss Z"
                    });
            return parsedDate.getTime(); // Get milliseconds since epoch
        } catch (ParseException e) {
            System.out.println("Invalid time format: " + timeStr);
            return -1;
        }
    }


    /**
     * Parse a time string and return the month and year.
     * @param timeStr The time string to parse.
     * @return The month and year.
     */
    public String timeParsed(String timeStr) {
        if (timeStr.contains("T")) {
            String[] time = timeStr.split("T");
            String[] date = time[0].split("-");
            String[] time2 = time[1].split(":");
            return getMonth(date[1]) + ", " + date[0] ;
        } else if (timeStr.contains("/")) {
            String[] time = timeStr.split(" ");
            String[] date = time[0].split("/");
            String[] time2 = time[1].split(":");
            return getMonth(date[1]) + ", " + date[2];

        } else {
            String[] time = timeStr.split(" ");
            String[] date = time[0].split("-");
            String[] time2 = time[1].split(":");
            return getMonth(date[1]) + ", " + date[0];
        }
    }

    /**
     * Get the month name from the month number.
     * @param month The month number.
     * @return The month name.
     */
    private String getMonth(String month) {
        switch (month) {
            case "01":
                return "January";
            case "02":
                return "February";
            case "03":
                return "March";
            case "04":
                return "April";
            case "05":
                return "May";
            case "06":
                return "June";
            case "07":
                return "July";
            case "08":
                return "August";
            case "09":
                return "September";
            case "10":
                return "October";
            case "11":
                return "November";
            case "12":
                return "December";
            default:
                return "Invalid month";
        }
    }
}