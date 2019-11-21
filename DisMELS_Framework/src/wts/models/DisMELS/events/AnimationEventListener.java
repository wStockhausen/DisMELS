/*
 * AnimationEventListener.java
 *
 * Created on June 13, 2006, 3:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.events;

import java.util.EventListener;

/**
 *
 * @author William Stockhausen
 */
public interface AnimationEventListener extends EventListener {

    /**
     * Classes implementing this method should update their graphics to reflect
     * any changes changes associated with the animation event.
     * 
     * @param ev - the animation event
     */
    void updateGraphics(AnimationEvent ev);
    
}
