/*
 * StateChangedSupport.java
 *
 * Created on December 9, 2005, 12:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.ui.events;

import java.awt.Component;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author William Stockhausen
 */
public class StateChangedSupport {

    private Component c;
    private ArrayList stateChangedEventListeners = new ArrayList();
    
    /** Creates a new instance of StateChangedSupport */
    public StateChangedSupport(Component c) {
        this.c = c;
    }
    
    public synchronized void addStateChangedEventListener(StateChangedEventListener el) {
        stateChangedEventListeners.add(el);
    }
    
    public synchronized void removeStateChangedEventListener(StateChangedEventListener el) {
        stateChangedEventListeners.remove(el);
    }
    
    public void fireStateChangedEvent() {
        StateChangedEvent ev = new StateChangedEvent(c);
        ListIterator li = stateChangedEventListeners.listIterator();
        while (li.hasNext()) {
            ((StateChangedEventListener)li.next()).stateChanged(ev);
        }
    }

    public void fireStateChangedEvent(Component component) {
        StateChangedEvent ev = new StateChangedEvent(component);
        ListIterator li = stateChangedEventListeners.listIterator();
        while (li.hasNext()) {
            ((StateChangedEventListener)li.next()).stateChanged(ev);
        }
    }
}
