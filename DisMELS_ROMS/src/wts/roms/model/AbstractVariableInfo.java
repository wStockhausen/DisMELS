/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Properties;

/**
 * Abstract class to facilitate concrete VariableInfo classes.
 * 
 * Set methods throw PropertyChange events if newVal != oldVal.
 * 
 * @author William.Stockhausen
 */
public abstract class AbstractVariableInfo implements Serializable {
    /**property: variable info has been checked */
    public static final String PROP_Checked = "checked";
    /**property: name in ROMS dataset*/
    public static final String PROP_NameInROMS  = "nameInROMSdataset";
    public final static String PROP_Name        = "internalVariableName";
    public final static String PROP_Field       = "isSpatialField";
    public final static String PROP_MaskType    = "maskType";
    public final static String PROP_Description = "description";
            
    /**flag indicating variable info has been checked */
    protected boolean checked = false;
    /** a description of this variable */
    protected String description = "";
    /** flag indicating whether the variable is a spatial field */
    protected boolean isField = false;
    /** the mask type to use for interpolation */
    protected String maskType = ModelTypes.MASKTYPE_NONE;
    /** the internal name used in the code */
    protected String name = null;
    /** the variable name in the ROMS dataset */
    protected String romsName = null;

    /**object to support throwing PropertyChange's */ 
    protected PropertyChangeSupport propSupport = null;
    
    protected AbstractVariableInfo() {
    }

    /**
     * Returns the description for this required variable.
     *
     * @return - the description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Sets the description.
     * 
     * @param descr 
     */
    protected void setDescription(String descr){
        String oldVal = this.description;
        this.description = descr;
        propSupport.firePropertyChange(PROP_Description,oldVal,descr);
    }

    /**
     * Returns the name of the mask field (or null) associated with this variable
     * (used for variables that represent spatial fields).
     * @return
     */
    public String getMaskType() {
        return maskType;
    }
    
    /**
     * Sets the mask field name.
     * 
     * @param mask 
     */
    protected void setMaskType(String mask){
        String oldVal = this.maskType;
        this.maskType = mask;
        propSupport.firePropertyChange(PROP_MaskType,oldVal,mask);
    }

    /**
     * Returns the internal name used for this variable.
     *
     * @return - the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the internal variable name.
     * 
     * @param name 
     */
    protected void setName(String name){
        String oldVal = this.name;
        this.name = name;
        propSupport.firePropertyChange(PROP_Name,oldVal,name);
    }

    /**
     * Returns the name used for this variable in the ROMS dataset.
     *
     * @return - the name in the ROMS dataset
     */
    public String getNameInROMSDataset() {
        return romsName;
    }

    /**
     * Sets the variable name in the ROMS dataset corresponding to the internal
     * name.
     *
     * @param romsName
     */
    public void setNameInROMSDataset(String romsName) {
        String oldVal = this.romsName;
        this.romsName = romsName;
        propSupport.firePropertyChange(CriticalVariableInfo.PROP_NameInROMS, oldVal, romsName);
    }

    /**
     * Returns flag indicating whether the ROMS name in the dataset has been
     * assigned to the critical variable
     *
     * @return
     */
    public boolean isChecked() {
        return checked;
    }

    /**
     * Sets the 'checked' flag.
     *
     * @param b
     */
    public void setChecked(boolean b) {
        checked = b;
        propSupport.firePropertyChange(CriticalVariableInfo.PROP_Checked, null, checked);
    }

    /**
     * Returns the flag indicating whether the variable is a spatial field.
     *
     * @return
     */
    public boolean isSpatialField() {
        return isField;
    }
    
    /**
     * Sets the flag indicating whether the variable is a spatial field.
     * 
     * @param isField 
     */
    protected void setSpatialField(boolean isField){
        boolean oldVal = this.isField;
        this.isField = isField;
        propSupport.firePropertyChange(PROP_Field,oldVal,isField);
    }
    
    /**
     * Reads variable info from a Properties object.
     * @param p
     * @param var
     * @param version 
     */
    public void readProperties(Properties p, String var, String version){
//        logger.info("Reading properties");
        if (version.equals("1.0")){
            setChecked(     Boolean.parseBoolean(p.getProperty(var+"."+PROP_Checked)));
            setSpatialField(Boolean.parseBoolean(p.getProperty(var+"."+PROP_Field)));
            setNameInROMSDataset(p.getProperty(var+"."+PROP_NameInROMS));
            setName(             p.getProperty(var+"."+PROP_Name));
            setMaskType(         p.getProperty(var+"."+PROP_MaskType));
            setDescription(      p.getProperty(var+"."+PROP_Description));
        }
//        logger.info("Done reading properties");
    }
    
    public void writeProperties(Properties p, String var, String version){
//        logger.info("Writing properties");
        if (version.equals("1.0")){
            p.put(var+"."+PROP_Checked,    Boolean.toString(checked));
            p.put(var+"."+PROP_Field,      Boolean.toString(isField));
            if (romsName!=null)    p.put(var+"."+PROP_NameInROMS, romsName);
            if (name!=null)        p.put(var+"."+PROP_Name,       name);
            if (maskType!=null)    p.put(var+"."+PROP_MaskType,   maskType);
            if (description!=null) p.put(var+"."+PROP_Description,description);
        }
    }
    
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        propSupport.removePropertyChangeListener(l);
    }    
}
