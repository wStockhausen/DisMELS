/*
 * SimpleBenthicAdultLHSAttributes.java
 *
 * Created on January 18, 2006, 6:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.BenthicAdult;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.LHS.SimpleLHSs.AbstractSimpleLHSAttributes;

/**
 * DisMELS class representing attributes for "simple" benthic adult life stages (e.g., flatfish).
 * 
 * @author William Stockhausen
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class SimpleBenthicAdultLHSAttributes extends AbstractSimpleLHSAttributes {
   
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     * This creates a new instance of SimplePelagicLHSAttributes WITH type name="NULL".
     */
    public SimpleBenthicAdultLHSAttributes() {
        super("NULL");
    }

    /**
     * Creates a new instance of SimpleBenthicAdultLHSAttributes
     */
    public SimpleBenthicAdultLHSAttributes(String typeName) {
        super(typeName);
//        createMap();
//        propertySupport =  new PropertyChangeSupport(this);
    }

    @Override
    public Object clone() {
        SimpleBenthicAdultLHSAttributes clone = null;
        try {
            clone = (SimpleBenthicAdultLHSAttributes) super.clone();
            clone.map = (HashMap<String,Object>) map.clone();
            for (int i=0;i<keys.length;i++) {
                clone.setValue(keys[i],this.getValue(keys[i]));
            }
            clone.propertySupport = new PropertyChangeSupport(clone);
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return clone;
    }
    
    @Override
    public SimpleBenthicAdultLHSAttributes createInstance(final String[] strv) {
        SimpleBenthicAdultLHSAttributes atts = new SimpleBenthicAdultLHSAttributes(strv[0]);
        atts.setValues(strv);
        return atts;
    }
}
