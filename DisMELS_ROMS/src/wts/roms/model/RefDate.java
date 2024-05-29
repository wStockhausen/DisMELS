/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author William.Stockhausen
 */
public class RefDate {
    private static String refDateString = "1900-01-01 00:00:00";
    private static TimeZone tz = TimeZone.getTimeZone("GMT");
    private static Calendar cal = new GregorianCalendar(tz);
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Date refDate = null;

    public static Date getRefDate(){
        if (refDate==null){
           dateFormat.setTimeZone(tz); 
        }
        return refDate;
    }
    
}
