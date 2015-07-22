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
 *
 * @author William.Stockhausen
 */
public class OptionalModelVariablesInfo extends AbstractVariablesInfo {
    
    /** */
    private static final Logger logger = Logger.getLogger(OptionalModelVariablesInfo.class.getName());
    
    /** version */
    public static final String version = "1.0";
    
    /** the singleton instance */
    private static OptionalModelVariablesInfo instance = null;
    
    /**
     * Static method (creates and) returns the singleton instance of this class.
     * 
     * @return - the singleton 
     */
    public static OptionalModelVariablesInfo getInstance(){
        if (instance==null) instance = new OptionalModelVariablesInfo();
        return instance;
    }
    
    private OptionalModelVariablesInfo(){
        super(20);
        propertySupport = new PropertyChangeSupport(this);
    }
    
    /**
     * Resets the collection of CriticalVariableInfo instances to their default values.
     */
    @Override
    public void reset(){
        throwPCEs = false;
        mapAVI.clear();
        throwPCEs = true;
        propertySupport.firePropertyChange(PROP_RESET, null, null);
    }
    
    /**
     * Convenience method to construct an OptionalVariableInfo instance.
     * @param name
     * @param desc
     */
    
    @Override
    public void addVariable(String name,String desc){
        OptionalVariableInfo ovi = new OptionalVariableInfo(name,desc);
        ovi.setNameInROMSDataset(name);//set as default
        ovi.addPropertyChangeListener(this);
        mapAVI.put(name,ovi);
        if (throwPCEs) propertySupport.firePropertyChange(PROP_VARIABLE_ADDED,null,ovi);
    }
    
    /**
     * Returns the OptionalVariableInfo instance associated with the internal variable name.
     * 
     * @param name - internal variable name 
     * @return 
     */
    @Override
    public OptionalVariableInfo removeVariable(String name){
        logger.info("Removing ovi "+name);
        return (OptionalVariableInfo) super.removeVariable(name);
    }
    
    /**
     * Returns the variable information associated with a variable name.
     * 
     * @param name = the variable's name
     * @return -
     */
    @Override
    public OptionalVariableInfo getVariableInfo(String name){
        return (OptionalVariableInfo) super.getVariableInfo(name);
    }
    
    /**
     * Read in stored properties. 
     * TODO: probably don't want to do this-->write to a file in the WorkingDirectory.
     * 
     * @param p 
     */
    @Override
    public void readProperties(Properties p){
        throwPCEs = false;
        logger.info("reading properties");
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
                OptionalVariableInfo ovi = new OptionalVariableInfo(varName, descr);
                ovi.setChecked(checked);
                ovi.setNameInROMSDataset(romsName);
                ovi.setSpatialField(isField);
                ovi.setMaskType(maskType);
                mapAVI.put(varName, ovi);
            }       
        }
        throwPCEs = true;
        propertySupport.firePropertyChange(PROP_RESET, null, null);
        logger.info("reading properties");
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
        if (evt.getPropertyName().equals(OptionalVariableInfo.PROP_Name)){
            //variable name has changed, so need to remove associated vi under the
            //old name and "refile" it under the new name.
            String old = (String) evt.getOldValue();
            AbstractVariableInfo ovi = mapAVI.remove(old);//remove under old name
            mapAVI.put(ovi.getName(),ovi);                //put under new name
            if (throwPCEs) propertySupport.firePropertyChange(PROP_VARIABLE_RENAMED,old,ovi.getName());//propagate event up chain
        }
    }
}
