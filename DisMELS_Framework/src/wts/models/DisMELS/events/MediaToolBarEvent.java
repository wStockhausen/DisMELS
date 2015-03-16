/*
 * MediaToolBarEvent.java
 *
 * Created on July 14, 2006, 9:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.events;

import java.util.EventObject;
import wts.models.DisMELS.gui.MediaToolBar;

/**
 *
 * @author William Stockhausen
 */
public class MediaToolBarEvent extends EventObject {
    
    /** Creates a new instance of MediaToolBarEvent */
    public MediaToolBarEvent(MediaToolBar source) {
        super(source);
    }
    
    public String toString() {
        String str = "MediaToolBarEvent: "+super.toString();
        return str;
    }
    
}
