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
 *
 * @author William Stockhausen
 */
public interface LifeStageParametersInterface extends LifeStageDataInterface {

    /**
     *  Creates an instance of a subclass.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     */
    public LifeStageParametersInterface createInstance(final String[] strv);
    
    /**
     * Returns the IBMFunctionInterface object corresponding to the 
     * given category and name.
     * 
     * @param cat  - usage category 
     * @param name - function name
     * @return   - the model function
     */
    public IBMFunctionInterface getIBMFunction(String cat, String name);
    
    /**
     * Returns the IBMFunctionInterface object corresponding to the 
     * given category and name.
     * 
     * @param cat  - usage category 
     * @param name - function name
     * @param f    - the function to set
     * @return     - true if successful
     */
    public boolean setIBMFunction(String cat, String name, IBMFunctionInterface f);
    
    /**
     * Returns the IBMFunctionInterface object corresponding to the given category.
     * 
     * @param cat - usage category 
     * @return   - the model function
     */
    public Set<String> getIBMFunctionCategories();
    
    /**
     * Returns the unique names (keys) associated with the IBM functions used in the
     * life stage.
     *
     * @param cat the value of cat
     * @return - the
     */
    public Set<String> getIBMFunctionNamesByCategory(String cat);
    
    /**
     * Returns the "selected" IBMFunctionInterface object corresponding to the given category.
     * 
     * @param cat - usage category 
     * @return   - the model function
     */
    public IBMFunctionInterface getSelectedIBMFunctionForCategory(String cat);
    
    /**
     * Selects the IBMFunction to use for a given function category.
     * 
     * @param cat  - function category
     * @param name - function name
     */
    public void selectIBMFunctionForCategory(String cat, String name);
    
    /**
     * Returns the unique names (keys) associated with the IBM function parameters used in the 
     * life stage.
     * 
     * @return - the set of parameter names
     */
    public Set<String> getIBMParameterNames();
    
    /**
     * Returns the IBMParameter object corresponding to the name.
     * 
     * @param name - name associated with model function
     * @return    - the IBMParameter instance
     */
    public IBMParameter getIBMParameter(String name);
    
    /**
     * Sets the LHS type name for the instnace.
     * 
     * @param name 
     */
    public void setTypeName(String name);
}
