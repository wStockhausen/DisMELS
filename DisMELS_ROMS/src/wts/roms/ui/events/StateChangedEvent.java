/*
 * StateChangedEvent.java
 *
 * Created on December 9, 2005, 11:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.ui.events;

import java.util.EventObject;

/**
 *
 * @author William Stockhausen
 */
public class StateChangedEvent extends EventObject {
    
    /** Creates a new instance of StateChangedEvent */
    public StateChangedEvent(java.awt.Component source) {
        super(source);
    }
    
}
