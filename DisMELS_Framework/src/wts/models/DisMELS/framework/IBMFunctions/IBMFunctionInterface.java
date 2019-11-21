/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMFunctions;

import java.util.Set;

/**
 * Model functions reflecting biological processes included in IBMs should
 * implement this interface to enable "pluggability".
 * 
 * @author William.Stockhausen
 */
public interface IBMFunctionInterface {
    
    /**
     * Returns a deep clone of the function.
     * 
     * @return 
     */
    public Object clone();
   
    /**
     * Gets the "type", i.e. the classification, of this model function.
     * 
     * @return the "type" of the model function
     */
    public String getFunctionType();
    
    /**
     * Sets the function type, i.e. the classification, of this model function.
     * @param type 
     */
    public void setFunctionType(String type);
   
    /**
     * Gets the user-friendly name of this model function.
     * 
     * @return the name of the model function
     */
    public String getFunctionName();
   
    /**
     * Sets the function name.
     * @param name 
     */
    public void setFunctionName(String name);
   
    /**
     * Gets a description of this model function.
     * 
     * @return the description
     */
    public String getDescription();
   
    /**
     * Sets the (brief) description of the function
     * @param descr 
     */
    public void setDescription(String descr);
    
    /**
     * Gets a full description of this model function.
     * 
     * @return the full description
     */
    public String getFullDescription();
    
    /**
     * Sets the full description of the function.
     * @param fdescr 
     */
    public void setFullDescription(String fdescr);
    
    /**
     * Returns true if the function has parameters that can be set.
     * 
     * @return - true/false
     */
    public boolean hasParameters();
    
    /**
     * Gets the set of parameter names used in the function.
     * 
     * @return - the names as a Set
     */
    public Set<String> getParameterNames();
    
    /**
     * Returns the description of the parameter associated with the given name.
     * 
     * @param name - theparameter name
     * @return  - the description of the parameter
     */
    public String getParameterDescription(String name);
    
    /**
     * Sets the description for the parameter associated with the given name.
     * 
     * @param name
     * @param descr 
     */
    public void setParameterDescription(String name, String descr);
    
    /**
     * Gets the ModelFunctionParameter assigned to the given name.
     * @param name
     * @return 
     */
    public IBMParameter getParameter(String name);
    
    /**
     * Sets the value of the function parameter identified by the 'name' key prior to calculating 
     * the function's value (see calculate).
     * 
     * @param name - name of the parameter to set
     * @param value - key/value map of the parameters' values 
     */
    public boolean setParameterValue(String name, Object value);
    
    /**
     * Returns true if the function contains subfunctions.
     * 
     * @return - true/false
     */
    public boolean hasSubfunctions();
    
    /**
     * Returns the names of the subfunctions used in this function
     * @return 
     */
    public Set<String> getSubfunctionNames(); 
    
    /**
     * Returns the subfunction associated with the given key.
     * 
     * @param key - subfunction name
     * @return - the subfunction
     */
    public IBMFunctionInterface getSubfunction(String key);
    
    /**
     * Associates an instance of an IBMFunction with the given subfunction name. Implementations
     * should PROBABLY check that the new instance and the old instance (if any) are members of the 
     * same IBMFunction class.
     * 
     * @param name
     * @param ifi
     * @return 
     */
    public boolean setSubfunction(String name, IBMFunctionInterface ifi);
    
    /**
     * This method calculates the "value" of the function based on a List of
     * input variable values.  
     * 
     * @param vars - an Object encapsulating the values of input variables
     * @return     - an Object encapsulating the values of the function
     */
    public Object calculate(Object vars);

    /**
     *  Creates an instance of a subclass.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     */
    public IBMFunctionInterface createInstance(final String[] strv);
    
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
}
