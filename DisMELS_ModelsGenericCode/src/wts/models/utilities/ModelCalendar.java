/*
 * ModelCalendar.java
 *
 * Created on March 7, 2006, 10:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.utilities;

/**
 *
 * @author William Stockhausen
 */
public class ModelCalendar {
    
    private static CalendarIF cal;
    
    /**
     * Creates a new instance of ModelCalendar
     */
    private ModelCalendar() {
    }
    
    public static CalendarIF getCalendar() {
        return cal;
    }
    
    public static void setCalendar(CalendarIF c) {
        cal = c;
    }
}
