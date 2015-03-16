/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMAttributes;

/**
 * Abstract class for model function parameter values. 
 * 
 * @author William.Stockhausen
 */
public abstract class IBMAttribute {
    
    /** parameter key */
    public final String key;
    /** parameter shortName */
    public final String shortName;
    
    /**
     * Constructor.
     * @param key  - user-friendly key for parameter 
     * @param shortName - shortName of parameter
     */
    protected IBMAttribute(String key, String shortName){
        this.key = key;
        this.shortName = shortName;
    }
    
    @Override
    public abstract IBMAttribute clone();
    
    /**
     * Returns class of the attribute value.
     * @return 
     */
    public abstract Class getValueClass();
    
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
}
