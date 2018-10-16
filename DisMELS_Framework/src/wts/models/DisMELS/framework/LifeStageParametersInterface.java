/*
 * LifeStageParametersInterface.java
 *
 * Created on March 10, 2006, 4:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import java.util.Set;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;

/**
 * Interface defining general functionality for LifeStageParameter classes.
 * 
 * @author William Stockhausen
 */
public interface LifeStageParametersInterface extends LifeStageDataInterface {
    
    /**
     * Sets the LHS type name for the instance.
     * 
     * @param name - the type name for the life stage
     */
    public void setTypeName(String name);
    
    /**
     * Returns the unique names (keys) associated with the IBM parameters used in the 
     * life stage.
     * 
     * @return - the set of keys identifying the IBMParameters
     */
    public Set<String> getIBMParameterKeys();
    
    /**
     * Returns the IBMParameter object corresponding to the given key.
     * 
     * @param key - the key (name) associated with parameter
     * 
     * @return    - the IBMParameter instance
     */
    public IBMParameter getIBMParameter(String key);
    
    /**
     * Returns the set of life stage function categories.
     * 
     * @return   - the function categories
     */
    public Set<String> getIBMFunctionCategories();
    
    /**
     * Returns the unique names (keys) associated with the potential IBM functions 
     * for use in the life stage process category.
     *
     * @param cat - the function category
     * 
     * @return - the set of keys used to identify the IBMFunctions within the category
     */
    public Set<String> getIBMFunctionKeysByCategory(String cat);
    
    /**
     * Returns the IBMFunctionInterface object corresponding to the 
     * given category and key (function name) identifying the function within the category.
     * 
     * @param cat  - usage category as String
     * @param key - key (function name) identifying function within the category
     * 
     * @return   - the corresponding IBMFunction, or null if the category or key is invalid
     */
    public IBMFunctionInterface getIBMFunction(String cat, String key);
    
    /**
     * Sets the IBMFunctionInterface object corresponding to the 
     * given category and key (function name).
     * 
     * @param cat  - usage category 
     * @param key - the key (function name) identifying the function with the category
     * @param f    - the IBMFunction to set
     * 
     * @return     - true if successful, false if the category or key is invalid
     */
    public boolean setIBMFunction(String cat, String key, IBMFunctionInterface f);
    
    /**
     * Returns the "selected" IBMFunctionInterface object corresponding to the given category.
     * 
     * @param cat - usage category 
     * 
     * @return   - the IBMFunction instance selected for use in the category
     */
    public IBMFunctionInterface getSelectedIBMFunctionForCategory(String cat);
    
    /**
     * Selects the IBMFunction to use for a given function category based on its key.
     * 
     * @param cat  - function category
     * @param key - the key (function name) identifying the function within the category
     * 
     * @return true or false as to whether the identified function was selected
     */
    public boolean setSelectedIBMFunctionForCategory(String cat, String key);

    /**
     * Creates an instance of a subclass from an array of Strings. 
     * 
     * Probably not the way to go for anything but the simplest implementing classes 
     * (i.e., no IBMFunctions).
     *
     * @param strv - array of Strings used to create a new instance of
     * the implementing class. The interpretation of the array is defined by the
     * implementing class.
     * 
     * @return  a new instance of the implementing class based on the string array.
     */
    public LifeStageParametersInterface createInstance(final String[] strv);
}
