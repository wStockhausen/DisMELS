/*
 * ModelControllerBeanEditor.java
 *
 * Created on January 5, 2006, 12:11 PM
 */

package wts.models.DisMELS.gui;

import java.beans.*;
import wts.models.DisMELS.framework.ModelControllerBean;

/**
 * @author William Stockhausen
 */
public class ModelControllerBeanEditor extends PropertyEditorSupport 
                                        implements java.beans.PropertyChangeListener {
    private ModelControllerBean newObj;
    private ModelControllerBeanCustomizer objCustomizer;
    private final String tb = "\t";
    private final String nl = "\n";
    private final String cc = ",";
    private final String qt = "\"";
    
    public ModelControllerBeanEditor() {
        super();
        objCustomizer = new ModelControllerBeanCustomizer();
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
        String str = nl+
//                        tb+qt+newObj.getFile_ROMSGrid()+qt+cc+nl+
//                        tb+qt+newObj.getFile_ROMSCanonicalDataset()+qt+cc+nl+
                        tb+qt+newObj.getFile_ROMSDataset()+qt+cc+nl+
                        tb+qt+newObj.getFile_InitialAttributes()+qt+cc+nl+
                        tb+qt+newObj.getFile_Results()+qt+cc+nl+
                        tb+newObj.getStartTime()+cc+newObj.getNtEnvironModel()+cc+nl+
                        tb+newObj.getTimeStep()+cc+newObj.getNtBioModel();
                     
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
        throw new IllegalArgumentException("cannot set ModelControllerBean object using text.");
    }
    
    @Override
    public void setValue(Object obj) {
        ModelControllerBean oldObj = (ModelControllerBean) obj;
        newObj = (ModelControllerBean) oldObj.clone();
        objCustomizer.setObject(newObj);
    }
    
    @Override
    public boolean supportsCustomEditor() {
	return true;
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent propertyChangeEvent) {
        firePropertyChange();
    }
}
