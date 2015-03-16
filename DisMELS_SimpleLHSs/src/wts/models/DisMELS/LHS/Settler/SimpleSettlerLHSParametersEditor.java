/*
 * SimpleSettlerLHSParametersEditor.java
 *
 * Created on January 6, 2006, 2:03 PM
 */

package wts.models.DisMELS.LHS.Settler;

import java.beans.*;

/**
 * @author William Stockhausen
 */
public class SimpleSettlerLHSParametersEditor extends PropertyEditorSupport 
                                       implements java.beans.PropertyChangeListener {
    
    private SimpleSettlerLHSParameters newObj;
    private SimpleSettlerLHSParametersCustomizer objCustomizer;
    private final String tb = "\t";
    private final String nl = "\n";
    private final String cc = ",";
    private final String qt = "\"";
    
    public SimpleSettlerLHSParametersEditor() {
        super();
        objCustomizer = new SimpleSettlerLHSParametersCustomizer();
        objCustomizer.addPropertyChangeListener(this);
    }

    /**
     * Overrides PropertyEditorSupport.getAsText()
     *  @return - null, since ModelControllerBean cannot be represented as text
     */
    public String getAsText() {
        return null;
    }

    public java.awt.Component getCustomEditor() {
	return objCustomizer;
    }
    
    public String getJavaInitializationString() {
        String str = "";
                     
        return str;
    }

    public String[] getTags() {
        return null;
    }
    
    public Object getValue() {
        return newObj;
    }
    
    public boolean isPaintable() {
        return false;
    }
    
    public void setAsText(String text)
                    throws IllegalArgumentException {
        throw new IllegalArgumentException("cannot set SimpleSettlerLHSParameters object using text.");
    }
    
    public void setValue(Object obj) {
        if (obj instanceof SimpleSettlerLHSParameters) {
            SimpleSettlerLHSParameters oldObj = (SimpleSettlerLHSParameters) obj;
            newObj = (SimpleSettlerLHSParameters) oldObj.clone();
            objCustomizer.setObject(newObj);
        }
    }
    
    public boolean supportsCustomEditor() {
	return true;
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
        firePropertyChange();
    }
}
