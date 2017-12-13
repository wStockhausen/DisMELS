/*
 * SimpleBenthicAdultLHSAttributes.java
 */

package wts.models.DisMELS.LHS.BenthicAdult;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.LHS.SimpleLHSs.AbstractSimpleLHSAttributes;

/**
 * DisMELS class representing attributes for "simple" benthic adult life stages (e.g., flatfish)
 * based on extending the AbstractSimpleLHSAttributes class.
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
     * Constructor for a new instance of SimpleBenthicAdultLHSAttributes.
     * 
     * @param typeName - the name of the LHS type to create the attributes for
     */
    public SimpleBenthicAdultLHSAttributes(String typeName) {
        super(typeName);
    }

    /**
     * Clones the object on which it is called.
     * 
     * @return - the SimpleBenthicAdultLHSAttributes clone as an Object
     */
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
    
    /**
     * Creates an instance of SimpleBenthicAdultLHSAttributes with values based
     * on parsing the input array of Strings. 
     * 
     * @param strv - the array of values, as Strings, to use
     * 
     * @return - a new SimpleBenthicAdultLHSAttributes object
     */
    @Override
    public SimpleBenthicAdultLHSAttributes createInstance(final String[] strv) {
        SimpleBenthicAdultLHSAttributes atts = new SimpleBenthicAdultLHSAttributes(strv[0]);
        atts.setValues(strv);
        return atts;
    }
}
