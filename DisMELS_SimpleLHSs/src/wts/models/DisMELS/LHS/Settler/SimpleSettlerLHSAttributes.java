/*
 * SimpleSettlerLHSAttributes.java
 *
 * Created on March 20, 2012
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.Settler;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.LHS.SimpleLHSs.AbstractSimpleLHSAttributes;

/**
 * DisMELS class representing attributes for "simple" settler life stages (think flatfish).
 * 
 * @author William Stockhausen
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class SimpleSettlerLHSAttributes extends AbstractSimpleLHSAttributes {
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     * This creates a new instance of SimpleSettlerLHSAttributes WITH type name="NULL".
     */
    public SimpleSettlerLHSAttributes(){
        super("NULL");
    }
    
    /**
     * Creates a new instance of SimpleSettlerLHSAttributes with type name 'typeName'.
     */
    public SimpleSettlerLHSAttributes(String typeName) {
        super(typeName);
    }
    
    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        SimpleSettlerLHSAttributes clone = null;
        try {
            clone = (SimpleSettlerLHSAttributes) super.clone();
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

    /**
     * Returns a new instance constructed from the values of the string[].
     * The first value in the string vector must be the type name.
     * Values are set internally by calling setValues(strv) on the new instance.
     * @param strv - vector of values (as Strings) 
     * @return - the new instance
     */
    @Override
    public SimpleSettlerLHSAttributes createInstance(final String[] strv) {
        SimpleSettlerLHSAttributes atts = new SimpleSettlerLHSAttributes(strv[0]);//this sets atts.typeName
        atts.setValues(strv);
        return atts;
    }
}
