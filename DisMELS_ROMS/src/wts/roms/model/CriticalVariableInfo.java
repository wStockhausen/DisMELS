/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;

/**
 * Class describing the map between a critical DisMELS variable and an
 * associated variable in a ROMS model file. Since critical DisMELS variables are predefined
 * up to the field name in the ROMS file, the only instance variable that can be set is
 * the name in the ROMS dataset (which defaults to the DisMELS variable name).
 * 
 * @author William.Stockhausen
 */
public class CriticalVariableInfo extends AbstractVariableInfo {
    
    /** class-level logger */
    private static final Logger logger = Logger.getLogger(CriticalVariableInfo.class.getName());
    
    /**
     * Creates an object using the given inputs.  
     * Only the roms name and the "checked" status can be changed.
     * 
     * @param name
     * @param isField
     * @param mask
     * @param description 
     */
    public CriticalVariableInfo(String name, boolean isField, String mask, String description) {
        this.name = name;
        this.maskType = mask;
        this.isField = isField;
        this.description = description;
        propSupport = new PropertyChangeSupport(this);
    }
    
    /**
     * This does nothing--can't change this for CriticalVariableInfo
     * @param desc 
     */
    @Override
    public void setDescription(String desc){logger.info("Description fixed--can't change");}
    
    /**
     * This does nothing--can't change this for CriticalVariableInfo
     * @param type 
     */
    @Override
    public void setMaskType(String type){logger.info("Mask type fixed--can't change");}
    
    /**
     * This does nothing--can't change this for CriticalVariableInfo
     * @param name 
     */
    @Override
    public void setName(String name){logger.info("Internal name fixed--can't change");}
    
    /**
     * This does nothing--can't change this for CriticalVariableInfo
     * @param b
     */
    @Override
    public void setSpatialField(boolean b){logger.info("Spatial field fixed--can't change");}
}
