/*
 * LifeStageDataInterface.java
 *
 * Created on January 18, 2006, 1:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import java.io.Serializable;

/**
 * Interface for life history stage parameter classes to implement.
 * @author William Stockhausen
 */
public interface LifeStageDataInterface extends Cloneable, Serializable {
    
    public static final String cc = ",";
    public static final Boolean b = true;
    public static final Double  d = new Double(0);
    public static final Integer i = new Integer(0);
    public static final Long    l = new Long(0);
    public static final String  s = "";
        
    /**
     * This method creates a clone of the instance on which it is called.
     */
    public Object clone() throws CloneNotSupportedException;
    
    /**
     * Returns a CSV string representation of the parameter values
     *
     *@return - CSV string of parameter values
     */
    public String getCSV();
    
    /**
     * Returns the header line for a csv file representation
     * of a set of parameters.  Use getCSV() to get the string
     * of actual parameter values.
     *
     *@return - String of CSV header names
     */
    public String getCSVHeader();
    
    /**
     * Gets the type name for the LHS.
     *
     * @return - String value containning the type name.
     */
    public String getTypeName();
        
    /**
     * Gets the property keys corresponding to the param map.
     *
     * @return - String array containning the keys.
     */
    public String[] getKeys();
    
    /**
     * Gets the parameter associated with the key as a Boolean.
     * If the parameter cannot be returned as a Boolean, a null
     * is returned.
     * 
     *@param key - String value containing the parameter key
     *@param b   - instance of a Boolean (req'd to identify method)
     *@return    - parameter value (if Boolean) or null (if not)
     */
    public Boolean getValue(String key, Boolean b);
    
    /**
     * Gets the parameter associated with the key as a Double.
     * If the parameter cannot be returned as a Double, a null
     * is returned.
     * 
     *@param key - String value containing the parameter key
     *@param d   - instance of a Double (req'd to identify method)
     *@return    - parameter value (if Double) or null (if not)
     */
    public Double getValue(String key, Double d);
    
    /**
     * Gets the parameter associated with the key as an Integer.
     * If the parameter cannot be returned as an Integer, a null
     * is returned.
     * 
     *@param key - String value containing the parameter key
     *@param i   - instance of a Integer (req'd to identify method)
     *@return    - parameter value (if Integer) or null (if not)
     */
    public Integer getValue(String key, Integer i);
    
    /**
     * Gets the parameter associated with the key as a Long.
     * If the parameter cannot be returned as a Long, a null
     * is returned.
     * 
     *@param key - String value containing the parameter key
     *@param i   - instance of a Integer (req'd to identify method)
     *@return    - parameter value (if Integer) or null (if not)
     */
    public Long getValue(String key, Long l);
    
    /**
     * Gets the parameter associated with the key as a String.
     * If the parameter cannot be returned as a String, a null
     * is returned.
     * 
     *@param key - String value containing the parameter key
     *@param s   - instance of a String (req'd to identify method)
     *@return    - parameter value (if String) or null (if not)
     */
    public String getValue(String key, String s);
    
    /**
     * Gets the parameter associated with the key as a boolean.
     * If the parameter cannot be returned as a boolean, a null
     * is returned.
     * 
     *@param key - String value containing the parameter key
     *@param b   - instance of a boolean (req'd to identify method)
     *@return    - parameter value (if boolean) or null (if not)
     */
    public boolean getValue(String key, boolean b);
    
    /**
     * Gets the parameter associated with the key as a double.
     * If the parameter cannot be returned as a double, a null
     * is returned.
     * 
     *@param key - String value containing the parameter key
     *@param d   - instance of a double (req'd to identify method)
     *@return    - parameter value (if Double) or null (if not)
     */
    public double getValue(String key, double d);
    
    /**
     * Gets the parameter associated with the key as an int.
     * If the parameter cannot be returned as an int, a null
     * is returned.
     * 
     *@param key - String value containing the parameter key
     *@param i   - instance of a int (req'd to identify method)
     *@return    - parameter value (if int) or null (if not)
     */
    public int getValue(String key, int i);
    
    /**
     * Gets the parameter associated with the key as a long.
     * If the parameter cannot be returned as a long, a null
     * is returned.
     * 
     *@param key - String value containing the parameter key
     *@param s   - instance of a long (req'd to identify method)
     *@return    - parameter value (if long) or null (if not)
     */
    public long getValue(String key, long l);
    
    /**
     * Gets the parameter associated with the key as an Object.
     *
     *@param key - String value containing the parameter key
     *@return    - parameter value as Object
     */
    public Object getValue(String key);
    
    public void setValue(String key, Object value);
//    public void setValue(String key, boolean value);
    public void setValue(String key, double value);
    public void setValue(String key, float value);
    public void setValue(String key, int value);
    public void setValue(String key, long value);
}
