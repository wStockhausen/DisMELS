/*
 * SimpleBenthicJuvenileLHSParametersEditor.java
 *
 * Created on January 6, 2006, 2:03 PM
 */

package wts.models.DisMELS.LHS.BenthicJuvenile;

import java.beans.*;

/**
 * @author William Stockhausen
 */
public class SimpleBenthicJuvenileLHSParametersEditor extends PropertyEditorSupport 
                                       implements java.beans.PropertyChangeListener {
    
    private SimpleBenthicJuvenileLHSParameters newObj;
    private SimpleBenthicJuvenileLHSParametersCustomizer objCustomizer;
    private final String tb = "\t";
    private final String nl = "\n";
    private final String cc = ",";
    private final String qt = "\"";
    
    public SimpleBenthicJuvenileLHSParametersEditor() {
        super();
        objCustomizer = new SimpleBenthicJuvenileLHSParametersCustomizer();
        objCustomizer.addPropertyChangeListener(this);
    }

    /**
     * Overrides PropertyEditorSupport.getAsText()
     *  @return - null, since ModelControllerBean cannot be represented as text
     */
    @Override
    public String getAsText() {
        return null;
    }

    @Override
    public java.awt.Component getCustomEditor() {
	return objCustomizer;
    }
    
    @Override
    public String getJavaInitializationString() {
        String str = "";
                     
        return str;
    }

    @Override
    public String[] getTags() {
        return null;
    }
    
    @Override
    public Object getValue() {
        return newObj;
    }
    
    @Override
    public boolean isPaintable() {
        return false;
    }
    
    @Override
    public void setAsText(String text)
                    throws IllegalArgumentException {
        throw new IllegalArgumentException("cannot set SimpleBenthicJuvenileLHSParameters object using text.");
    }
    
    @Override
    public void setValue(Object obj) {
        if (obj instanceof SimpleBenthicJuvenileLHSParameters) {
            SimpleBenthicJuvenileLHSParameters oldObj = (SimpleBenthicJuvenileLHSParameters) obj;
            newObj = (SimpleBenthicJuvenileLHSParameters) oldObj.clone();
            objCustomizer.setObject(newObj);
        }
    }
    
    @Override
    public boolean supportsCustomEditor() {
	return true;
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
        firePropertyChange();
    }
}
