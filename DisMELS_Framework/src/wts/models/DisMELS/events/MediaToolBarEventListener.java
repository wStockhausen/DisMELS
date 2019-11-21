/*
 * MediaToolBarEventListener.java
 *
 * Created on July 14, 2006, 9:42 PM
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
public interface MediaToolBarEventListener extends EventListener {
    
    public void play(MediaToolBarEvent ev);
    public void stop(MediaToolBarEvent ev);
    public void rewind(MediaToolBarEvent ev);
    public void stepBackward(MediaToolBarEvent ev);
    public void stepForward(MediaToolBarEvent ev);
    public void fastForward(MediaToolBarEvent ev);
    
}
