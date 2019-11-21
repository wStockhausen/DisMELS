/*
 * TestUtilities.java
 *
 * Created on December 22, 2005, 10:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.utilities;

import wts.models.utilities.DateTime;
import wts.models.utilities.CalendarJulian;

/**
 *
 * @author William Stockhausen
 */
public class TestCalendar {
    String cc = ",";
    
    /** Creates a new instance of TestUtilities */
    public TestCalendar() {
    }
    
    public void testJulianCalendar(long s) {
        System.out.println("\nTest JC1: secs input");
        CalendarJulian cal = new CalendarJulian();
        CalendarJulian cal1 = new CalendarJulian();
        cal.setTimeOffset(s);
        DateTime date = cal.getDate();
        cal1.setDate(date.year,date.month,date.dom,date.hour,date.min,date.sec);
        System.out.println(date.toString());
        System.out.println(cal1.toString());
    }
    
    public void testJulianCalendar(int yr, int mon, int day, int hr, int min, int sec) {
        System.out.println("\nTest JC2: date input");
        CalendarJulian cal = new CalendarJulian();
        CalendarJulian cal1 = new CalendarJulian();
        cal.setDate(yr,mon,day,hr,min,sec);
        long s = cal.getTimeOffset();
        System.out.println("s = "+s);
        cal1.setTimeOffset(s);
        System.out.println(cal.toString());
        System.out.println(cal1.toString());
    }
    
    public void testJulianCalendar(int yr, double yday) {
        System.out.println("\nTest JC3: yday input");
        CalendarJulian cal = new CalendarJulian();
        CalendarJulian cal1 = new CalendarJulian();
        cal.setDate(yr,yday);
        DateTime date = cal.getDate();
        cal1.setDate(date.year,date.month,date.dom,date.hour,date.min,date.sec);
        System.out.println(cal.toString());
        System.out.println(cal1.toString());
    }
    
    public void runTests() {
        testJulianCalendar(1968,5,23,0,0,0);
        CalendarJulian ref = new CalendarJulian();
        ref.setDate(1968,5,23,0,0,0);
        testJulianCalendar(940334400L);
        testJulianCalendar(1998,3,10,0,0,0);
        testJulianCalendar(942667200L);
        testJulianCalendar(1998,4,6,0,0,0);
        testJulianCalendar(942926400L);
        testJulianCalendar(945259200L);
        testJulianCalendar(945518400L);
        testJulianCalendar(947851200L);
        testJulianCalendar(948110400L);
        testJulianCalendar(950443200L);
        testJulianCalendar(1995,28.5);
    }
    
    public static void main(String args[]) {
        TestCalendar t = new TestCalendar();
        t.runTests();
    }

}
