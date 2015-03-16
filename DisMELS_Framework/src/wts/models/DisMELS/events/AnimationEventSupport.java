/*
 * AnimationEventSupport.java
 *
 * Created on June 13, 2006, 3:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.events;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author William Stockhausen
 */
public class AnimationEventSupport {
    
    private ArrayList eventListeners = new ArrayList();
    
    /** Creates a new instance of AnimationEventSupport */
    public AnimationEventSupport() {
    }
    
    public synchronized void addAnimationEventListener(AnimationEventListener el) {
        eventListeners.add(el);
    }
    
    public synchronized void removeAnimationEventListener(AnimationEventListener el) {
        eventListeners.remove(el);
    }
    
    public void fireAnimationEvent() {
        AnimationEvent ev = new AnimationEvent();
        ListIterator li = eventListeners.listIterator();
        while (li.hasNext()) {
            ((AnimationEventListener)li.next()).updateGraphics(ev);
        }
    }
}
