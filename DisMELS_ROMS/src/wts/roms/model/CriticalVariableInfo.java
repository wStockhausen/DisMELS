/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;

/**
 *
 * @author William.Stockhausen
 */
public class CriticalVariableInfo extends AbstractVariableInfo {
    
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
     * This does nothing!!
     * @param desc 
     */
    @Override
    public void setDescription(String desc){logger.info("Chainging description");}
    
    /**
     * This does nothing!!
     * @param type 
     */
    @Override
    public void setMaskType(String type){logger.info("Chainging mask type");}
    
    /**
     * This does nothing!!
     * @param name 
     */
    @Override
    public void setName(String name){logger.info("Chainging name");}
    
    /**
     * This does nothing!!
     * @param b
     */
    @Override
    public void setSpatialField(boolean b){logger.info("Chainging spatial field");}
}
