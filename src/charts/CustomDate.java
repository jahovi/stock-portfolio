package charts;

import java.util.Date;

/**
 * Instances of this class provide custom formated date strings derived from a unix timestamp in milliseconds. 
 * These date strings are used in the chart panels. 
 */
public class CustomDate extends Date {
    private String monthName;
    private String dayOfMonth;
    private String hour;
    private String minute;
    private String year;
    
    /**
     * Constructs a new custom date from a unix timestamp in milliseconds.
     *
     * @param date a unix timestamp in milliseconds
     */
    public CustomDate(long date) {
        // takes unix time in milliseconds
        super(date);
        String[] dateStringParts = this.toString().split(" ");
        monthName = dateStringParts[1];
        dayOfMonth = dateStringParts[2];
        String time = dateStringParts[3];
        String[] timeParts = time.split(":");
        hour = timeParts[0];
        minute = timeParts[1];
        
        year = dateStringParts[5];
    }

    /**
     * Gets the month.
     *
     * @return a three letter abreviation of the month name 
     */
    public String getMonthName() {
        return monthName;
    }

    /**
     * Gets the day of month.
     *
     * @return the day of month in the format dd
     */
    public String getDayOfMonth() {
        return dayOfMonth;
    }

    /**
     * Gets the hour.
     *
     * @return the hour in the format hh
     */
    public String getHour() {
        return hour;
    }

    /**
     * Gets the minute.
     *
     * @return the minute in the format mm
     */
    public String getMinute() {
        return minute;
    }
    
    /**
     * Gets a custom date string in the format mmm dd yy hh:mm
     *
     * @return the custom date string
     */
    public String getCustomDateString() {
        return getMonthName() + " " + getDayOfMonth() + " " + getYearString() + " " + getHour() + ":" + getMinute();
    }
    
    /**
     * Gets a custom date string in the format mmm dd.
     *
     * @return the month and day
     */
    public String getMonthAndDay() {
        // format: monthName dd
        return getMonthName() + " " + getDayOfMonth();
    }
    
    /**
     * Gets the hour and minute in the format hh:mm
     *
     * @return the hour and minute
     */
    public String getHourAndMinute() {
        return getHour() + ":" + getMinute();
    }
    
    /**
     * Gets the year last two digits of the year.
     *
     * @return the year string in the format yy
     */
    public String getYearString() {
        String lastTwoNumbers = year.substring(Math.max(year.length() - 2, 0));
        return lastTwoNumbers;
    }
    
}
