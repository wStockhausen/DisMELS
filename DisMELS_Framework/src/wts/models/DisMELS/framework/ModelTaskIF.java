/*
 * ModelTaskIF.java
 *
 * Created on January 15, 2006, 12:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

/**
 *
 * @author wstockha
 */
public interface ModelTaskIF {
    
       public void go();
        
        public void stop();
        
        public void pause();
        
        public boolean isDone();
        
        public int getCurrentValue();
        
        public String getMessage();
}
