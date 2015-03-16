/*
 * CalendarJulian.java
 *
 * Created on February 28, 2006, 5:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.utilities;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author William Stockhausen
 */
public class CalendarGregorian implements CalendarIF {
    
    private GregorianCalendar grgCal;
    private GregorianCalendar refCal;

    /** Creates a new instance of CalendarJulian */
    public CalendarGregorian() {
        TimeZone tz = TimeZone.getTimeZone("GMT+0000");
        //set up "current" time
        grgCal = new GregorianCalendar(tz);
//        grgCal.setGregorianChange(new java.util.Date(Long.MAX_VALUE));//make Julian Calendar
        //set up time reference
        refCal = new GregorianCalendar(tz);
//        refCal.setGregorianChange(new java.util.Date(Long.MAX_VALUE));//make Julian Calendar
        setRefDate(1900,1,1,0,0,0);
    }

    /**
     *  Adds s seconds to current date/time
     */
//    @Override
    public void addTimeOffset(long s) {
        grgCal.add(Calendar.SECOND,(int) s);
    }
    
    /**
     * Gets the time reference as a DateTime object
     */
//    @Override
    public wts.models.utilities.DateTime getRefDate() {
        wts.models.utilities.DateTime date = new DateTime();
        date.year  = refCal.get(Calendar.YEAR);
        date.month = refCal.get(Calendar.MONTH)+1;
        date.dom   = refCal.get(Calendar.DAY_OF_MONTH);
        date.doy   = refCal.get(Calendar.DAY_OF_YEAR);
        date.hour  = refCal.get(Calendar.HOUR_OF_DAY);
        date.min   = refCal.get(Calendar.MINUTE);
        date.sec   = refCal.get(Calendar.SECOND);
        return date;
    }
    
    /**
     *  Set reference point date/time based on calendar date.
     * 
     * @param date  - wts.roms.model.DateTime object with reference point info.
     */
//    @Override
    public void setRefDate(wts.models.utilities.DateTime date) {
        setRefDate(date.year, date.month, date.dom, date.hour, date.min, date.sec);
    }

    /**
     *  Set reference point date/time based on calendar date.
     *@param int yr  - year (xxxx)
     *@param int mon - month (1-12)
     *@param int day - day of month (1-31)
     *@param int hr  - hour of day (0-23)
     *@param int min - minute of hour (0-59)
     *@param int sec - seconds of minute (0-59)
     */
//    @Override
    public void setRefDate(int yr, int mon, int day, int hr, int min, int sec) {
        //set refCal. Note that month is 0-11 in GregorianCalendar, not 1-12
        refCal.set(yr,mon-1,day,hr,min,sec);
        refCal.set(Calendar.MILLISECOND,0);
        refCal.getTimeInMillis(); //do this to calc the time offset
    }

    /**
     *  Set date/time based on calendar date.
     *@param int yr  - year (xxxx)
     *@param int mon - month (1-12)
     *@param int day - day of month (1-31)
     *@param int hr  - hour of day (0-23)
     *@param int min - minute of hour (0-59)
     *@param int sec - seconds of minute (0-59)
     */
//    @Override
    public void setDate(int yr, int mon, int day, int hr, int min, int sec) {
        //set julCal. Note that month is 0-11 in GregorianCalendar, not 1-12
        grgCal.set(yr,mon-1,day,hr,min,sec);
        grgCal.set(Calendar.MILLISECOND,0);
    }
    
    /**
     *  Set date/time based on calendar date.
     * 
     * @param date  - wts.roms.model.DateTime object with reference point info.
     */
//    @Override
    public void setDate(wts.models.utilities.DateTime date) {
        setDate(date.year, date.month, date.dom, date.hour, date.min, date.sec);
    }
    
    /**
     *  Set date/time based on calendar year and year-day.
     * 
     * @param yr  - calendar year.
     * @param yday - year-day.
     */
//    @Override
    public void setDate(int yr, double yday) {
        grgCal.set(yr,0,1,0,0,0); //set to 00:00:00 Jan 1, year
        grgCal.set(Calendar.MILLISECOND,0);//need to do this to zero-out millis
        int doy = (int) Math.floor(yday);
        int sec = (int)((yday-doy)*secInDay);
        grgCal.add(Calendar.DAY_OF_YEAR,doy-1);//doy runs 1-366, so need to -1
        grgCal.add(Calendar.SECOND,sec);
    }

    /**
     *  Returns elapsed time from reference point in seconds.
     */
//    @Override
    public long getTimeOffset() {
        return (grgCal.getTimeInMillis()-refCal.getTimeInMillis())/1000L; //time offset in seconds
    }

    /**
     *  Sets date/time based on elapsed time (s) since start of epoch
     */
//    @Override
    public void setTimeOffset(long s) {
        grgCal.setTimeInMillis(s*1000L+refCal.getTimeInMillis());
    }

    /**
     *  Returns the year corresponding to current date
     */
//    @Override
    public int getYear() {
        int year = grgCal.get(Calendar.YEAR);
        return year;
    }

    /**
     *  Returns the day-of-year corresponding to current date (1-366)
     */
//    @Override
    public int getDayOfYear() {
        int doy = grgCal.get(Calendar.DAY_OF_YEAR);
        return doy;
    }

    /**
     *  Returns the year-day corresponding to current date (1-366.999)
     */
//    @Override
    public double getYearDay() {
        double yday = getDayOfYear()+
                (getHourOfDay()*secInHr+getMinOfHour()*secInMin+getSecOfMin())/((double) secInDay);
        return yday;
    }

    /**
     *  Returns month (1-12) corresponding to current date
     */
//    @Override
    public int getMonth() {
        int mon = grgCal.get(Calendar.MONTH)+1;//Calendar returns months as 0-11
        return mon;
    }

    /**
     *  Returns day-of-month (1-31) corresponding to current date
     */
//    @Override
    public int getDayOfMonth() {
        int dom = grgCal.get(Calendar.DAY_OF_MONTH);
        return dom;
    }

    /**
     *  Returns hour-of-day (0-23) corresponding to current date
     */
//    @Override
    public int getHourOfDay() {
        int hour = grgCal.get(Calendar.HOUR_OF_DAY);
        return hour;
    }

    /**
     *  Returns minute-of-hour (0-59) corresponding to current date
     */
//    @Override
    public int getMinOfHour() {
        int min = grgCal.get(Calendar.MINUTE);
        return min;
    }

    /**
     *  Returns second-of-minute (0-59) corresponding to current date
     */
//    @Override
    public int getSecOfMin() {
        int sec = grgCal.get(Calendar.SECOND);
        return sec;
    }

    /**
     *  Returns the current date as a wts.models.roms.DateTime object.
     */
//    @Override
    public DateTime getDate() {
        wts.models.utilities.DateTime date = new DateTime();
        date.year  = getYear();
        date.month = getMonth();
        date.dom   = getDayOfMonth();
        date.doy   = getDayOfYear();
        date.hour  = getHourOfDay();
        date.min   = getMinOfHour();
        date.sec   = getSecOfMin();
        return date;
    }
    
    /**
     *  Returns true if current date corresponds to a leap year
     */
//    @Override
    public boolean isLeapYear() {
        int leap = getYear()-4*(getYear()/4);
        return (leap==0);
    }
    
    /**
     *  Returns date/time as string
     */
    public String getDateTimeString() {
        String str = getDate().getDateTimeString();
        return str;
    }
    
    @Override
    public String toString() {
//        String str = getDate().toString()+"\n"+
//                     refCal.toString()+"\n"+
//                     getTimeOffset()+"\n"+
//                     (julCal.getTimeInMillis()-refCal.getTimeInMillis());
        String str = getDate().toString()+"\n"+
                     getTimeOffset();
        return str;
    }
}
