/*
 * CalendarIF.java
 *
 * Created on February 28, 2006, 3:16 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.utilities;

/**
 *
 * @author William Stockhausen
 */
public interface CalendarIF {
    
    /* number of seconds in day */
    public static final long secInDay  = 86400L;
    /* number of seconds in an hour */
    public static final long secInHr   = 3600L;
    /* number of seconds in a minute */
    public static final long secInMin  = 60L;
    
    /* doy at start of month in non-leap years */
    public static final int[] iyd = 
            new int[] {1,32,60,91,121,152,182,213,244,274,305,335,366};
    /* doy at start of month in leap years */
    public static final int[] iydL = 
            new int[] {1,32,61,92,122,153,183,214,245,275,306,336,367};
            
   /**
    *   Add s seconds to the current date
    */ 
    public void addTimeOffset(long s);
        
    /**
     * Sets the current date/time as a DateTime object.
     */
    public wts.models.utilities.DateTime getDate();
    
    /**
     * Sets the current date/time.
     */
    public void setDate(int yr, int mon, int day, int hr, int min, int sec);
    
    /**
     * Sets the current date/time.
     */
    public void setDate(wts.models.utilities.DateTime datetime);
    
    /**
     * Sets the current time.
     */
    public void setDate(int yr, double yday);
    
    /**
     * Gets the time reference as a DateTime object
     */
    public wts.models.utilities.DateTime getRefDate();
    
    /**
     *  Set reference point date/time based on calendar date.
     * 
     * @param date  - wts.roms.model.DateTime object with reference point info.
     */
    public void setRefDate(wts.models.utilities.DateTime date);

    /**
     *  Set reference point date/time based on calendar date.
     *@param int yr  - year (xxxx)
     *@param int mon - month (1-12)
     *@param int day - day of month (1-31)
     *@param int hr  - hour of day (0-23)
     *@param int min - minute of hour (0-59)
     *@param int sec - seconds of minute (0-59)
     */
    public void setRefDate(int yr, int mon, int day, int hr, int min, int sec);
    
    /**
     * Gets the current time as an offset (in seconds) from the reference date/time.
     */
    public long getTimeOffset();
    
    /**
     * Sets the current time based on an offset in seconds from
     *  the reference date/time.
     */
    public void setTimeOffset(long s);
    
   /**
    *  Returns the year-day (1-366)
    */
   public int getDayOfYear();
    
    /**
     *  Returns the day-of-the-month (1-31)
     */
    public int getDayOfMonth();
    
    /**
     *  Returns the month (1-12) corresponding to the current time
     */
    public int getMonth();
    
    /**
     *  Returns the calendar year.
     */
    public int getYear();
    
    /**
     *  Returns the year-day (1-366, with decimal portion = fraction of day)
     */
    public double getYearDay();

    /**
     *  Returns hour-of-day (0-23) corresponding to current date
     */
    public int getHourOfDay();

    /**
     *  Returns minute-of-hour (0-59) corresponding to current date
     */
    public int getMinOfHour();

    /**
     *  Returns second-of-minute (0-59) corresponding to current date
     */
    public int getSecOfMin();
    
    /**
     *  Returns true if current date corresponds to a leap year
     */
    public boolean isLeapYear();
    
    /**
     *  Returns date/time as string
     */
    public String getDateTimeString();
}
