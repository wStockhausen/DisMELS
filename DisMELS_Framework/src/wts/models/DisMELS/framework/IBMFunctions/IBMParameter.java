/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMFunctions;

import java.io.Serializable;

/**
 * Abstract class for model function parameter values. 
 * 
 * @author William.Stockhausen
 */
public abstract class IBMParameter implements Serializable {
    
    /** parameter name */
    protected String name;
    /** parameter description */
    protected String description;
    
    /**
     * Constructor.
     * @param name  - user-friendly name for parameter 
     * @param descr - description of parameter
     */
    protected IBMParameter(String name, String descr){
        this.name = name;
        this.description = descr;
    }
    
    @Override
    public abstract IBMParameter clone();
    
    /**
     * Gets the value for the parameter.
     * @return - the parameter value as an Object
     */
    public abstract Object getValue();
    
    /**
     * Gets the value for the parameter as a String.
     * @return - the parameter value as represented by a String
     */
    public abstract String getValueAsString();
    
    /**
     * Sets the value for the parameter.
     * 
     * @param value 
     */
    public abstract void setValue(Object value);
    
    /**
     * Parses the value of the string to set the value of the parameter.
     * 
     * @param str 
     */
    public abstract void parseValue(String str);

    //these are required for Java Beans-like lonngterm persistence */
    public final String getName(){return name;}
    public final void setName(String name){this.name = name;}
    public final String getDescription(){return description;}
    public final void setDescription(String descr){this.description = descr;}
}
