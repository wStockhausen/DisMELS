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
 * 2D grid variables in a ROMS model grid file. 
 * 
 * These encompass 2D grid variables that are REQUIRED to be in all 
 * ROMS model grids, with the assumption that that the name used in the 
 * ROMS grid to refer to the variable is the same as the "internal" name used in DisMELS.
 * 
 * @author William.Stockhausen
 */
public class CriticalGrid2DVariablesInfo extends AbstractVariablesInfo {
    
    /** class logger */
    private static final Logger logger = Logger.getLogger(CriticalGrid2DVariablesInfo.class.getName());
    /** version */
    public static final String version = "1.0";
    
    /** the singleton instance */
    private static CriticalGrid2DVariablesInfo instance = null;
    
    /**
     * Static method (creates and) returns the singleton instance of this class.
     * 
     * @return - the singleton 
     */
    public static CriticalGrid2DVariablesInfo getInstance(){
        if (instance==null) instance = new CriticalGrid2DVariablesInfo();
        return instance;
    }
    
    private CriticalGrid2DVariablesInfo(){
        super(60);
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
     * @param name - name of variable ("internal" and name in ROMS grid are same)
     * @param isField - flag indicating whether this variable is a spatial field
     * @param mask - name of mask field (use one of the ModelTypes.MASKTYPEs)
     * @param description - description of field
     */
    private void constructCVI(String name, boolean isField, String mask, String description){
        logger.info("---Constructing cvi '"+name+"'");
        CriticalVariableInfo cvi = new CriticalVariableInfo(name, isField, mask, description);
        cvi.setNameInROMSDataset(name);//set as default
        cvi.addPropertyChangeListener(this);
        mapAVI.put(name,cvi);
        logger.info("---Done constructing cvi '"+name+"'");
    }
        
    private void constructCVIs(){
        logger.info("--Constructing CVIs");
        String name; String desc; boolean isField; String mask;
        //xl
        name = "xl";
        desc = "grid length (km?) along xi dimension";
        isField = false;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //el
        name = "el";
        desc = "grid length (km?) along eta dimension";
        isField = false;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //spherical
        name = "spherical";
        desc = "flag indicating use of lat/lon coordinates";
        isField = false;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //h
        name = "h";
        desc = "bathymetry";
        isField = true;
        mask    = ModelTypes.MASKTYPE_RHO;
        constructCVI(name,isField,mask,desc);
        //pm
        name = "pm";
        desc = "inverse grid scaling along xi";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //pn
        name = "pn";
        desc = "inverse grid scaling along eta";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //dmde
        name = "dmde";
        desc = "gradient of pm along eta";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //dndx
        name = "dndx";
        desc = "gradient of pn along xi";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //angle
        name = "angle";
        desc = "angle (in deg) from local x axis to eta axis";//TODO: check
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //mask_rho
        name = "mask_rho";
        desc = "mask at rho grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //mask_psi
        name = "mask_psi";
        desc = "mask at psi grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //mask_u
        name = "mask_u";
        desc = "mask at u grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //mask_v
        name = "mask_v";
        desc = "mask at v grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //lat_rho
        name = "lat_rho";
        desc = "latitude at rho grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //lat_psi
        name = "lat_psi";
        desc = "latitude at psi grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //lat_u
        name = "lat_u";
        desc = "latitude at u grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //lat_v
        name = "lat_v";
        desc = "latitude at v grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //lon_rho
        name = "lon_rho";
        desc = "longitude at rho grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //lon_psi
        name = "lon_psi";
        desc = "longitude at psi grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //lon_u
        name = "lon_u";
        desc = "longitude at u grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //lon_v
        name = "lon_v";
        desc = "longitude at v grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //x_rho
        name = "x_rho";
        desc = "x at coordinate rho grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //x_psi
        name = "x_psi";
        desc = "x coordinate at psi grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //x_u
        name = "x_u";
        desc = "x coordinate at u grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //x_v
        name = "x_v";
        desc = "x coordinate at v grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //y_rho
        name = "y_rho";
        desc = "y coordinate at rho grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //y_psi
        name = "y_psi";
        desc = "y coordinate at psi grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //y_u
        name = "y_u";
        desc = "y coordinate at u grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        //y_v
        name = "y_v";
        desc = "y coordinate at v grid points";
        isField = true;
        mask    = ModelTypes.MASKTYPE_NONE;
        constructCVI(name,isField,mask,desc);
        logger.info("--Done constructing CVIs");
    }
    
    /**
     * Returns the critical grid variable information associated with a variable name.
     * 
     * @param name = the variable's name
     * @return -
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
    @Override
    public void readProperties(Properties p){
        logger.info("Reading properties");
        throwPCEs = false;
        String clazz = this.getClass().getName();
        String version = p.getProperty(clazz+"_version");
        if ((version!=null)&&(version.equals("1.0"))){
            int n = Integer.parseInt(p.getProperty(clazz+"_"+"vars","0"));
            logger.info("Reading n = "+n);
            mapAVI.clear();
            for (int i=0;i<n;i++){
                String str = clazz+"_var"+i+".";
                boolean checked = Boolean.parseBoolean(p.getProperty(str+AbstractVariableInfo.PROP_Checked));
                String romsName = p.getProperty(str+AbstractVariableInfo.PROP_NameInROMS);
                String varName  = p.getProperty(str+AbstractVariableInfo.PROP_Name);
                boolean isField   = Boolean.parseBoolean(p.getProperty(str+AbstractVariableInfo.PROP_Field));
                String maskType = p.getProperty(str+AbstractVariableInfo.PROP_MaskType);
                String descr    = p.getProperty(str+AbstractVariableInfo.PROP_Description);
                logger.info("Creating 2DVI "+varName+"; rn = "+romsName+"; mt = "+maskType+"; de = "+descr);
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
     * This method does NOTHING!! All critical grid variables are already defined.
     * 
     * @param name
     * @param desc 
     */
    @Override
    protected void addVariable(String name, String desc) {
        //do nothing
    }
}
