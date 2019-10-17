/*
 * SimplePelagicLHSAttributes
 *
 * Created on March 20, 2012
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.PelagicStages;

import java.beans.PropertyChangeSupport;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.LHS.SimpleLHSs.AbstractSimpleLHSAttributes;

/**
 * DisMELS class representing attributes for "simple" pelagic life stages (eggs, larvae).
 * 
 * @author William Stockhausen
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class SimplePelagicLHSAttributes extends AbstractSimpleLHSAttributes {
   
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     * This creates a new instance of SimplePelagicLHSAttributes WITH type name="NULL".
     */
    public SimplePelagicLHSAttributes(){
        super("NULL");
    }
    
    /**
     * Creates a new instance of SimplePelagicLHSAttributes with type name 'typeName'.
     */
    public SimplePelagicLHSAttributes(String typeName) {
        super(typeName);
    }
    
    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        SimplePelagicLHSAttributes clone = new SimplePelagicLHSAttributes(typeName);
        for (String key: keys) clone.setValue(key,this.getValue(key));
        clone.propertySupport = new PropertyChangeSupport(clone);
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
    public SimplePelagicLHSAttributes createInstance(final String[] strv) {
        SimplePelagicLHSAttributes atts = new SimplePelagicLHSAttributes(strv[0]);//this sets atts.typeName
        atts.setValues(strv);
        return atts;
    }
}
