/*
 * DateTime.java
 *
 * Created on February 28, 2006, 3:09 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.utilities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author William Stockhausen
 */
public class DateTime extends Object 
                      implements Cloneable, Serializable {
    
    public static final String PROP_Year = "year";
    public static final String PROP_Mon  = "month";
    public static final String PROP_DOM  = "day of month";
    public static final String PROP_DOY  = "day of year";
    public static final String PROP_Hour = "hour";
    public static final String PROP_Min  = "minute";
    public static final String PROP_Sec  = "second";    
    private transient PropertyChangeSupport propertySupport;
    
    private static String d = "-";
    private static String c = ":";
    
    public int year;
    public int doy;
    public int month;
    public int dom;
    public int hour;
    public int min;
    public int sec;
    
    /**
     * Creates a new instance of DateTime
     */
    public DateTime() {
        propertySupport = new PropertyChangeSupport(this);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    @Override
    public Object clone() {
        try {
            DateTime clone = (DateTime) super.clone();
            clone.propertySupport = new PropertyChangeSupport(clone);
            clone.dom   = dom;
            clone.doy   = doy;
            clone.hour  = hour;
            clone.min   = min;
            clone.month = month;
            clone.sec   = sec;
            clone.year  = year;
            return clone;
        } catch (java.lang.CloneNotSupportedException exc) {
            System.out.println("DateTime: Problem cloning");
            System.out.println(exc.toString());
        }
        return null;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int val) {
        int oldVal = year;
        year = val;
        propertySupport.firePropertyChange(PROP_Year,oldVal,val);
    }
    
    public int getMonth() {
        return month;
    }
    
    public void setMonth(int val) {
        int oldVal = month;
        month = val;
        propertySupport.firePropertyChange(PROP_Mon,oldVal,val);
    }
    
    public int getDOM() {
        return dom;
    }
    
    public void setDOM(int val) {
        int oldVal = dom;
        dom = val;
        propertySupport.firePropertyChange(PROP_DOM,oldVal,val);
    }
    
    public int getDOY() {
        return doy;
    }
    
    public void setDOY(int val) {
        int oldVal = doy;
        doy = val;
        propertySupport.firePropertyChange(PROP_DOY,oldVal,val);
    }
    
    public int getHour() {
        return hour;
    }
    
    public void setHour(int val) {
        int oldVal = hour;
        hour = val;
        propertySupport.firePropertyChange(PROP_Hour,oldVal,val);
    }
    
    public int getMinute() {
        return min;
    }
    
    public void setMinute(int val) {
        int oldVal = min;
        min = val;
        propertySupport.firePropertyChange(PROP_Min,oldVal,val);
    }
    
    public int getSecond() {
        return sec;
    }
    
    public void setSecond(int val) {
        int oldVal = sec;
        sec = val;
        propertySupport.firePropertyChange(PROP_Sec,oldVal,val);
    }
    
    /**
     *  Returns date/time as string
     */
    public String getDateTimeString() {
        String str = year+d+month+d+dom+" "+hour+c+min+c+sec;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            java.util.Date jDate = sdf.parse(str);
            str = sdf.format(jDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return str;
    }
    
    /**
     *
     */
    public static DateTime parseDateTime(String str) {
        DateTime dt = new DateTime();
        dt = parseDateTime(str,dt);
        return dt;
    }
    
    /**
     *
     */
    public static DateTime parseDateTime(String str,DateTime dt) {
        String[] strv = str.split(" ");
        dt = parseDate(strv[0],dt);
        dt = parseTime(strv[1],dt);
        return dt;
    }
    
    /**
     *
     */
    public static DateTime parseDate(String str) {
        DateTime dt = new DateTime();
        dt = parseDate(str,dt);
        return dt;
    }
    
    /**
     *
     */
    public static DateTime parseDate(String str,DateTime dt) {
        String[] datev = str.split("-");
        dt.year  = Integer.parseInt(datev[0]);
        dt.month = Integer.parseInt(datev[1]);
        dt.dom   = Integer.parseInt(datev[2]);
        return dt;
    }
    
    /**
     *
     */
    public static DateTime parseTime(String str) {
        DateTime dt = new DateTime();
        dt = parseTime(str,dt);
        return dt;
    }
    
    /**
     *
     */
    public static DateTime parseTime(String str,DateTime dt) {
        String[] timev = str.split(":");
        dt.hour  = Integer.parseInt(timev[0]);
        dt.min= Integer.parseInt(timev[1]);
        dt.sec= Integer.parseInt(timev[2]);
        return dt;
    }
    
    /**
     * Overrides Object.toString() method.
     */
    @Override
    public String toString() {
        String str = "date = "+year+d+month+d+dom+" "+hour+c+min+c+sec+"\n"+
                     "doy  = "+doy;
        return str;
    }
}
