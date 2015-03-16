/*
 * StateChangedEventListener.java
 *
 * Created on December 9, 2005, 11:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.ui.events;

import wts.roms.ui.events.StateChangedEvent;
import java.util.EventListener;

/**
 *
 * @author William Stockhausen
 */
public interface StateChangedEventListener extends EventListener {
    public void stateChanged(StateChangedEvent ev);
}
