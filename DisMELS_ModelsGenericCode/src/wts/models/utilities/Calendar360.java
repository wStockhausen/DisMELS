/*
 * Calendar365.java
 *
 * Created on February 28, 2006, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.utilities;

/**
 *  ROMS calendar object for calendar with 360 days/year.
 *  Note: the ref. date is 0001-01-01 00:00:00.
 *
 * @author William Stockhausen
 */
public class Calendar360 implements CalendarIF {
    
    /* number of seconds in year (360 days) */
    private static final long secInYear = 31104000;
    /* number of days in month */
    private static final int daysInMon = 30;
            
    private DateTime refDate; //0001-01-01 00:00:00.
    private long timeOffset = 0;
    private int yr = 0;
    private int mon = 1;
    private int dom = 1;
    private int doy = 1;
    private int hr  = 0;
    private int min = 0;
    private int sec = 0;
    
    /** Creates a new instance of Calendar365 */
    public Calendar360() {
        refDate = new DateTime();
        refDate.year  = 1;
        refDate.month = 1;
        refDate.dom   = 1;
        refDate.hour  = 0;
        refDate.min   = 0;
        refDate.sec   = 0;
    }
    
    private void computeDate() {
        yr  = (int) (timeOffset/secInYear)+1;
        doy = (int)((timeOffset-secInYear*yr)/secInDay)+1;
        mon = doy/daysInMon+1;
        dom = doy-daysInMon*(mon-1);
        
        long t = timeOffset-(secInYear*(yr-1)+secInDay*(doy-1)); //seconds in day
        hr  = (int)(t/secInHr);
        min = (int)((t-secInHr*hr)/secInMin);
        sec = (int)(t-secInMin*(t/secInMin));
    }
    
    public void addTimeOffset(long s) {
        timeOffset += s;
    }

    public DateTime getRefDate() {
        return refDate;
    }

    public void setRefDate(DateTime date) {
        //cannot change refDate
    }

    public void setRefDate(int yr, int mon, int day, int hr, int min, int sec) {
        //cannot change refDate
    }
    
    public DateTime getDate() {
        DateTime date  = new DateTime();
        date.year  = yr;
        date.doy   = doy;
        date.month = mon;
        date.dom   = dom;
        date.hour  = hr;
        date.min   = min;
        date.sec   = sec;
        return date;
    }
    
    public void setDate(int yr, int mon, int day, int hr, int min, int sec) {
        //Compute day of year
        doy  = (int) (daysInMon*(mon-1)+day);
        //Compute time offset from ref time
        timeOffset = secInYear*(yr-1)+secInDay*(doy-1)+
                     secInHr*hr+secInMin*min+sec;
        //Set other variables
        this.yr  = yr;
        this.mon = mon;
        this.dom = day;
        this.hr  = hr;
        this.min  = min;
        this.sec = sec;
    }
    
    public void setDate(int yr, double yday) {
        timeOffset = secInYear*(yr-1)+(long)(secInDay*yday);
        computeDate();
    }

    public void setDate(DateTime dt) {
        setDate(dt.year, dt.month, dt.dom, dt.hour, dt.min, dt.sec);
    }

    public long getTimeOffset() {
        return timeOffset;
    }
    
    public void setTimeOffset(long s) {
        timeOffset = s;
        computeDate();
    }
    
    public int getYear() {
        return yr;
    }
    
     /**
     *  Returns the year-day (1-366)
     */
   public int getDayOfYear() {
        return doy;
    }
    
    /**
     *  Returns the year-day (1-366, with decimal portion = fraction of day)
     */
    public double getYearDay() {
        double yday = ((double)(doy*secInDay+secInHr*hr+secInMin*min+sec))/secInDay;
        return yday;
    }
    
    /**
     *  Returns the month (1-12) corresponding to the current time
     */
    public int getMonth() {
        return mon;
    }
    
    public int getDayOfMonth() {
        return dom;
    }
    
    public int getHourOfDay() {
        return hr;
    }
    
    public int getMinOfHour() {
        return min;
    }
    
    public int getSecOfMin() {
        return sec;
    }
    
    public boolean isLeapYear() {
        return false;
    }
    
    /**
     *  Returns date/time as string
     */
    public String getDateTimeString() {
        String str = getDate().getDateTimeString();
        return str;
    }
}
