/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.beans.PropertyChangeSupport;

/**
 *
 * @author William.Stockhausen
 */
public class OptionalVariableInfo extends AbstractVariableInfo {
    
    public OptionalVariableInfo(){
        propSupport = new PropertyChangeSupport(this);
    }
    
    public OptionalVariableInfo(String name, String description){
        this.name = name;
        this.description = description;
        propSupport = new PropertyChangeSupport(this);
    }
    
    /**
     * Sets the description.
     * 
     * @param descr 
     */
    @Override
    public void setDescription(String descr){
        super.setDescription(descr);
    }
    
    /**
     * Sets the mask field name.
     * 
     * @param mask 
     */
    @Override
    public void setMaskType(String mask){
        super.setMaskType(mask);
    }
    
    /**
     * Sets the internal variable name.
     * 
     * @param name 
     */
    @Override
    public void setName(String name){
        super.setName(name);
    }
    
    /**
     * Sets the flag indicating whether the variable is a spatial field.
     * 
     * @param isField 
     */
    @Override
    public void setSpatialField(boolean isField){
        super.setSpatialField(isField);
    }
}
