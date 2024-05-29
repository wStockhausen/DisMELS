/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.logging.Logger;

/**
 * Encapsulates a collection of CriticaModelVariable instances reflecting 
 * time-varying or 3D variables in a ROMS model dataset. 
 * 
 * These encompass 3D and/or time-varying variables that are REQUIRED to be in all 
 * ROMS model datasets, with the assumption that that the name used in the 
 * ROMS dataset to refer to the variable is the same as the "internal" name used in DisMELS.
 * 
 * @author William.Stockhausen
 */
public class CriticalModelVariablesInfo extends AbstractVariablesInfo {
    
    /** class logger */
    private static final Logger logger = Logger.getLogger(CriticalModelVariablesInfo.class.getName());
    /** version */
    public static final String version = "1.0";
    
    /** the singleton instance */
    private static CriticalModelVariablesInfo instance = null;
    
    /**
     * Static method (creates and) returns the singleton instance of this class.
     * 
     * @return - the singleton 
     */
    public static CriticalModelVariablesInfo getInstance(){
        if (instance==null) instance = new CriticalModelVariablesInfo();
        return instance;
    }
    
    private CriticalModelVariablesInfo(){
        super(20);
        constructCVIs();        
        propertySupport = new PropertyChangeSupport(this);
    }
    
    /**
     * Resets the collection of CriticalVariableInfo instances to their default values.
     */
    @Override
    public void reset(){
        logger.info("-Starting reset()");
        throwPCEs = false;
        mapAVI.clear();
        constructCVIs();
        throwPCEs = true;
        propertySupport.firePropertyChange(PROP_RESET, null, null);
        logger.info("-Done reset()");
    }
    
    /**
     * Convenience method to construct a CriticalVariableInfo instance.
     * 
     * @param name - name of variable ("internal" and name in ROMS dataset are same)
     * @param isField - flag indicating whether this variable is a spatial field
     * @param mask - name of mask field (use one of the ModelTypes.MASKTYPEs)
     * @param description - description of field
     */
    private void constructCVI(String name, boolean isField, String mask, String description){
        CriticalVariableInfo cvi = new CriticalVariableInfo(name, isField, mask, description);
        cvi.setNameInROMSDataset(name);//set as default
        cvi.addPropertyChangeListener(this); 
        mapAVI.put(name,cvi);
    }
    
    private void constructCVIs(){
        String name; String desc; boolean isField; String mask;
        //oceantime
        name = "ocean_time";
        desc = "ROMS model time variable (in seconds)";
        isField = false;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //hc
        name = "hc";
        desc = "critical depth (m)";
        isField = false;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //Cs_w
        name = "sc_w";
        desc = "vertical scaling parameters";
        isField = false;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //Cs_w
        name = "Cs_w";
        desc = "vertical scaling parameters";
        isField = false;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //zeta
        name = "zeta";
        desc = "free surface height (2D)";
        isField = true;
        mask    = ModelTypes.MASKTYPE_RHO;
        constructCVI(name,isField,mask,desc);
        //u
        name = "u";
        desc = "x-component of horizontal velocity (3D)";
        isField = true;
        mask    = ModelTypes.MASKTYPE_U;
        constructCVI(name,isField,mask,desc);
        //v
        name = "v";
        desc = "y-component of horizontal velocity (3D)";
        isField = true;
        mask    = ModelTypes.MASKTYPE_V;
        constructCVI(name,isField,mask,desc);
        //omega
        name = "omega";
        desc = "scaled vertical momentum flux (3D)";
        isField = true;
        mask    = ModelTypes.MASKTYPE_RHO;
        constructCVI(name,isField,mask,desc);
        //salt (salinity)
        name = "salt";
        desc = "salinity field (3D)";
        isField = true;
        mask    = ModelTypes.MASKTYPE_RHO;
        constructCVI(name,isField,mask,desc);
        //temp (temperature)
        name = "temp";
        desc = "temperature (3D)";
        isField = true;
        mask    = ModelTypes.MASKTYPE_RHO;
        constructCVI(name,isField,mask,desc);
    }
        
    /**
     * Returns the critical variable information associated with a variable name.
     * 
     * @param name = the variable's name ("internal" and ROMS dataset names are same)
     * 
     * @return - the associated CritivalVariableInfo object
     */
    @Override
    public CriticalVariableInfo getVariableInfo(String name){
        return (CriticalVariableInfo) super.getVariableInfo(name);
    }
    
    /**
     * Read in stored properties.
     * 
     * @param p 
     */
    public void readProperties(Properties p){
        throwPCEs = false;
        logger.info("Reading properties");
        String clazz = this.getClass().getName();
        String version = p.getProperty(clazz+"_version");
        if ((version!=null)&&(version.equals("1.0"))){
            int n = Integer.parseInt(p.getProperty(clazz+"_"+"vars","0"));
            logger.info("Reading n = "+n);
            for (int i=0;i<n;i++){
                String str = clazz+"_var"+i+".";
                boolean checked = Boolean.parseBoolean(p.getProperty(str+AbstractVariableInfo.PROP_Checked));
                String romsName = p.getProperty(str+AbstractVariableInfo.PROP_NameInROMS);
                String varName  = p.getProperty(str+AbstractVariableInfo.PROP_Name);
                boolean isField   = Boolean.parseBoolean(p.getProperty(str+AbstractVariableInfo.PROP_Field));
                String maskType = p.getProperty(str+AbstractVariableInfo.PROP_MaskType);
                String descr    = p.getProperty(str+AbstractVariableInfo.PROP_Description);
                logger.info("Creating VI "+varName+"; rn = "+romsName+"; mt = "+maskType+"; de = "+descr);
                CriticalVariableInfo cvi = new CriticalVariableInfo(varName, isField, maskType, descr);
                cvi.setChecked(checked);
                cvi.setNameInROMSDataset(romsName);
                mapAVI.put(varName, cvi);
            }   
        }
        throwPCEs = true;
        propertySupport.firePropertyChange(PROP_RESET, null, null);
        logger.info("Done reading properties");
    }
    
    /**
     * Writes property values.
     * 
     * @param p 
     */
    @Override
    public void writeProperties(Properties p){
        logger.info("Writing properties");
        String clazz = this.getClass().getName();
        p.put(clazz+"_version", "1.0"); 
        Set<String> names = mapAVI.keySet();
        logger.info("Writing "+names.size()+" VIs to properties");
        writeProperties(p, this.getClass(), version);
        logger.info("Done writing properties");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.info("PropertyChange detected: "+evt.toString());
        if (throwPCEs) {
            propertySupport.firePropertyChange(evt);//propagate event up chain
            logger.info("Threw PropertyChange");
        } else {
            logger.info("Ignored PropertyChange");
        }
    }

    /**
     * This method does nothing! All critical model variables are already defined.
     * @param name
     * @param desc 
     */
    @Override
    protected void addVariable(String name, String desc) {
        //do nothing
    }
}
