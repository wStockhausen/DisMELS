/*
 * Calendar365.java
 *
 * Created on February 28, 2006, 1:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.utilities;

/**
 *  ROMS calendar object for calendar with 365.25 days/year.
 *  Note: the ref. date is 0001-01-01 00:00:00.
 *
 * @author William Stockhausen
 */
public class Calendar365 implements CalendarIF {
    
    /* number of seconds in leap year (366 days) */
    private static final long secInLeapYear = 31557600;
    /* number of seconds in non-leap year (365 days) */
    private static final long secInYear     = 31536000;
            
    private DateTime refDate; //0001-01-01 00:00:00.
    private long timeOffset;
    private int yr  = 1;
    private int mon = 1;
    private int dom = 1;
    private int doy = 1;
    private int hr  = 0;
    private int min = 0;
    private int sec = 0;
    private boolean isLeap = false;
    
    /**
     * Creates a new instance of Calendar365
     */
    public Calendar365() {
        refDate = new DateTime();
        refDate.year  = 1;
        refDate.month = 1;
        refDate.dom   = 1;
        refDate.hour  = 0;
        refDate.min   = 0;
        refDate.sec   = 0;
    }
    
    private void computeDate() {
        //compute number of COMPLETED leap years
        int numLeaps = (int)(timeOffset/(3*secInYear+secInLeapYear));
        //compute number of seconds following end of last leap year
        long sPostLeap = timeOffset-(3*secInYear+secInLeapYear)*numLeaps;
        //compute number of complete years corresponding to sPostLeap
        //note that sPostLeap/secInYear could be 4 if we're at last day of leap year,
        //but only 3 years have really been completed
        int remYrs = Math.max((int)(sPostLeap/secInYear),3); 
        //now compute the year
        yr  = 4*numLeaps+remYrs;
        //compute the day-of-year
        long sPostYear = sPostLeap-secInYear*remYrs;//elapsed time since start of year
        doy = (int)(sPostYear/secInDay)+1;
        
        //compute month and day-of-month
        int[] mns = null;
        if (isLeapYear()) {mns = iydL;}
        else {mns = iyd;}
        mon = 0;
        for (int m=1;m<mns.length;m++) {
            if (doy<mns[m]) {
                mon = m;
                break;
            }
        }
        dom = doy-mns[mon-1]+1;
        
        long t = sPostYear-secInDay*(doy-1); //seconds in day
        hr  = (int)(t/secInHr);
        min = (int)((t-secInHr*hr)/secInMin);
        sec = (int)(t-secInMin*(t/secInMin));
    }
    
    public void addTimeOffset(long s) {
        timeOffset += s;
    }

    public long getTimeOffset() {
        return timeOffset;
    }
    
    public void setDate(int yr, int mon, int day, int hr, int mn, int sec) {
        //Compute day of year
        int numLeaps = yr/4;         //number of leap years 
        int lastLeap = 4*(numLeaps); //last leap year
        int postLeap = yr-lastLeap;  //0 if leap year
        doy  = 0;            //day of year (1-366)
        if (postLeap==0) {
            isLeap = true;
            doy = iydL[mon-1]+day-1;
        } else {
            isLeap = false;
            doy = iyd[mon-1]+day-1;
        }
        //compute the time offset
        //compute no. secs in up to current year
        long yrSecs = numLeaps*(3*secInYear+secInLeapYear);
        if (postLeap==0) yrSecs -= secInLeapYear; //yr IS a leap year, but it's not over yet
        if (postLeap==1) {} //do nothing
        if (postLeap>1) yrSecs += secInYear*(postLeap-1);//add in non-leap years
        //now compute time offset
        timeOffset = yrSecs+secInDay*doy+secInHr*hr+secInMin*mn+sec;
        //Set other variables
        this.yr  = yr;  //year
        this.mon = mon; //month
        this.dom = day; //day-of-month
        this.hr  = hr;  //hour-of-day
        this.min = min; //minute-of-hour
        this.sec = sec; //second-of-minute
    }
    
    public void setDate(int yr, double yday) {
        timeOffset = secInYear*yr+(long)(secInDay*yday);
        computeDate();
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
    
    public boolean isLeapYear() {
        boolean isLeap = (yr-4*(yr/4)==0);
        return isLeap;
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

    public void setDate(DateTime dt) {
        setDate(dt.year, dt.month, dt.dom, dt.hour, dt.min, dt.sec);
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
    
    /**
     *  Returns date/time as string
     */
    public String getDateTimeString() {
        String str = getDate().getDateTimeString();
        return str;
    }
}
