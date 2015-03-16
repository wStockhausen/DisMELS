/*
 * AbstractLHSParameters.java
 *
 * Created on January 18, 2006, 2:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import java.util.*;
import java.util.logging.Logger;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;

/**
 *
 * @author William Stockhausen
 */
public abstract class AbstractLHSParameters implements LifeStageParametersInterface {
    
    /** provides an empty set for subclasses to return when not supporting IBM parameters or functions */
    private static final Set<String> emptySet = new HashSet<>(1);
    
    private static final Logger logger = Logger.getLogger(AbstractLHSParameters.class.getName());
    
    /* LHS type name assigned to the instance. (Serialized)*/
    protected String typeName = null;    
    /* map of IBM parameters by parameter name. (Serialized) */
    protected Map<String,IBMParameter> mapParams = null;
    /** map of IBMFunctions within categories. (Serialized) */
    protected Map<String,Map<String,IBMFunctionInterface>> mapOfPotentialFunctionsByCategory = null;
    /* map of selected IBM functions by category name. (Serialized) */
    protected Map<String,IBMFunctionInterface> mapOfSelectedFunctionsByCategory = null;
    
    /** 
     * Assigns the LHS type name to the constructed subclass instance.
     * Subclasses should call this constructor with a valid LHS type name from
     * all constructors to set the type name.
     * 
     *@param typeName - the LHS type name as a String.
     */
    protected AbstractLHSParameters(String typeName) {
        this.typeName = typeName;
    }
    
    protected AbstractLHSParameters(String typeName, int numParams, int numFunctionCats){
        this.typeName = typeName;
        mapParams = new LinkedHashMap<>(2*numParams);
        mapOfPotentialFunctionsByCategory = new LinkedHashMap<>(2*numFunctionCats);        
        mapOfSelectedFunctionsByCategory  = new LinkedHashMap<>(2*numFunctionCats);
    }

    /**
     *  Creates an instance of a subclass.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     */
    @Override
    public abstract LifeStageParametersInterface createInstance(final String[] strv);
     
    /**
     * This method should create the Map, mapParams, to the IBMParameters incorporated
     * in the life stage.
     * 
     * The DEFAULT IMPLEMENTATION is to throw an UnsupportedOperationException.
     * 
     * Subclasses using IBMParameters should override this method to create the
     * mapParams Map object.
     */
    protected void createMapToValues(){
        throw new UnsupportedOperationException("Not supported yet.");    
    }
     
    /**
     * This method should create the Map, mapSelectedFunctions, to the selected 
     * IBMFunctions by category incorporated in the life stage. 
     * 
     * The DEFAULT IMPLEMENTATION is to throw an UnsupportedOperationException.
     * 
     * Subclasses using IBMFunctions should override this method to create the
     * mapSelectedFunctions Map object.
     */
    protected void createMapToSelectedFunctions(){
        throw new UnsupportedOperationException("Not supported yet.");    
    }
    
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//      The following are implemented to extend LifeStageParametersInterface
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     *  This method should be overridden by extending classes.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    /**
     * Returns a CSV string representation of the attribute values.
     * This method should be overriden by subclasses that add additional attributes, 
     * possibly calling super.getCSV() to get an initial csv string to which 
     * additional field values could be appended.
     * 
     *@return - CSV string attribute values
     */
    @Override
    public abstract String getCSV();
    
    /**
     * Returns the comma-delimited string corresponding to the attributes
     * to be used as a header for a csv file.  
     * This should be overriden by subclasses that add additional attributes, 
     * possibly calling super.getCSVHeader() to get an initial header string 
     * to which additional field names could be appended.
     * Use getCSV() to get the string of actual attribute values.
     *
     *@return - String of CSV header names
     */
    @Override
    public abstract String getCSVHeader();
    
    /**
     * Gets the LHS type name assigned to the instance.
     *
     * @return - the LHS type name as a String.
     */
    @Override
    public String getTypeName() {return typeName;}
       
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//      The following are implemented to extend ParamMapIF
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * Gets the parameter keys.
     * This method is abstract to force dispatch of method to overriding method
     * in subclasses.
     * 
     * @return - keys as String array.
     */
    @Override
    public abstract String[] getKeys();
    
    @Override
    public Boolean getValue(String key, Boolean b) {
        Boolean v = null;
        try {
            v = (Boolean) mapParams.get(key).getValue();
        } catch (ClassCastException exc) {            
        }
        return v;
    }

    @Override
    public Double getValue(String key, Double d) {
        Double v = null;
        try {
            v = (Double) mapParams.get(key).getValue();
        } catch (ClassCastException exc) {            
        }
        return v;
    }

    @Override
    public Integer getValue(String key, Integer i) {
        Integer v = null;
        try {
            v = (Integer) mapParams.get(key).getValue();
        } catch (ClassCastException exc) {            
        }
        return v;
    }

    @Override
    public Long getValue(String key, Long l) {
        Long v = null;
        try {
            v = (Long) mapParams.get(key).getValue();
        } catch (ClassCastException exc) {            
        }
        return v;
    }

    @Override
    public boolean getValue(String key, boolean b) throws ClassCastException {
        boolean v = ((Boolean) mapParams.get(key).getValue()).booleanValue();
        return v;
    }

    @Override
    public double getValue(String key, double d) throws ClassCastException {
        double v = ((Double) mapParams.get(key).getValue()).doubleValue();
        return v;
    }

    @Override
    public int getValue(String key, int i) throws ClassCastException {
        int v = ((Integer) mapParams.get(key).getValue()).intValue();
        return v;
    }

    @Override
    public long getValue(String key, long l) throws ClassCastException {
        long v = ((Long) mapParams.get(key).getValue()).longValue();
        return v;
    }

    @Override
    public String getValue(String key, String s) {
        String v = null;
        try {
            v = (String) mapParams.get(key).getValue();
        } catch (ClassCastException exc) {            
        }
        return v;
    }

    @Override
    public Object getValue(String key) {
        return mapParams.get(key).getValue();
    }
    
    /**
     * Sets parameter identified by key to input value.
     * This method is abstract to ensure that subclasses that wish to provide
     * property change support can do so even on instances that are cast
     * to AbstractLHSParameters.
     * 
     * @param key   - key identifying parameter to be set
     * @param value - value to set
     */
    @Override
    public abstract void setValue(String key, Object value);
    
//    @Override
//    public void setValue(String key, boolean value) {
//        setValue(key, value);
//    }
//    
    @Override
    public void setValue(String key, double value) {
        setValue(key,new Double(value));
    }
    
    @Override
    public void setValue(String key, float value) {
        setValue(key,new Float(value));
    }
    
    @Override
    public void setValue(String key,int value) {
        setValue(key,new Integer(value));
    }
    
    @Override
    public void setValue(String key, long value) {
        setValue(key,new Long(value));
    }
    
    /**
     * Returns the IBMParameter object corresponding to the name.
     * 
     * This method DOES NOT need to be overridden by subclasses (but can be).
     * 
     * @param name - name associated with the IBM parameter
     * @return     - the IBM parameter instance
     */
    @Override
    public IBMParameter getIBMParameter(String name){
        return mapParams.get(name);
    }
    
    /**
     * Returns the unique names (keys) associated with the IBM parameters used in the 
     * life stage. 
     * 
     * As a DEFAULT IMPLEMENTATION, this method returns an empty Set; 
     * 
     * This method SHOULD BE OVERRIDDEN by subclasses that use IBMParameters.
     * 
     * @return - the set of parameter names
     */
    @Override
    public Set<String> getIBMParameterNames(){return emptySet;}
    
    /**
     * Returns the IBMFunctionInterface object corresponding to the 
     * given category and function key. 
     * 
     * As a DEFAULT IMPLEMENTATION, this method throws an UnsupportedOperationException 
     * 
     * This method SHOULD BE OVERRIDDEN by subclasses that use IBMFunctions.
     * 
     * @param cat  - usage category 
     * @param name - function name
     * @return   - the model function
     */
    @Override
    public IBMFunctionInterface getIBMFunction(String cat, String key){
        throw new UnsupportedOperationException("Not supported yet.");    
    }
    
    /**
     * Returns the IBMFunctionInterface object corresponding to the 
     * given category and name.
     * 
     * @param cat  - usage category 
     * @param name - function name
     * @param f    - the function to set
     * @return     - true if successful
     */
    @Override
    public boolean setIBMFunction(String cat, String name, IBMFunctionInterface f){
        if (mapOfPotentialFunctionsByCategory.containsKey(cat)){
            if (mapOfPotentialFunctionsByCategory.get(cat).containsKey(name)){
                mapOfPotentialFunctionsByCategory.get(cat).put(name,f);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the IBMFunctionInterface object corresponding to the given category.
     * 
     * As a DEFAULT IMPLEMENTATION, this method returns an empty Set. 
     * 
     * This method SHOULD BE OVERRIDDEN by subclasses that use IBMFunctions.
     * 
     * @param cat - usage category 
     * @return   - the model function
     */
    @Override
    public Set<String> getIBMFunctionCategories(){return emptySet;}
    
    /**
     * Returns the unique names (keys) associated with the IBM functions used in the
     * life stage.
     * 
     * As a DEFAULT IMPLEMENTATION, this method returns an empty Set. 
     * 
     * This method SHOULD BE OVERRIDDEN by subclasses that use IBMFunctions.
     * 
     * @param cat the value of cat
     * @return - the
     */
    @Override
    public Set<String> getIBMFunctionNamesByCategory(String cat){return emptySet;}
    
    /**
     * Returns the ModelFunctionInterface object corresponding to the category 
     * associated with input "cat".
     * 
     * @param cat - name of the function category
     * @return   - the model function
     */
    @Override
    public IBMFunctionInterface getSelectedIBMFunctionForCategory(String cat){
        IBMFunctionInterface f = mapOfSelectedFunctionsByCategory.get(cat);
        if (f==null){
            logger.info("No selected IBM parameter function found for category '"+cat+"'");
            logger.info("Available categories are:");
            for (String key: mapOfSelectedFunctionsByCategory.keySet()){
                logger.info("\tcategory '"+key+"'");
                IBMFunctionInterface sf = mapOfSelectedFunctionsByCategory.get(key);
                logger.info("\t\tselected function is of type "+sf.getClass().getName());
                if (key.equals(cat)) {
                    logger.info("\tsetting selected fcuntion for category");
                    f = sf;
                }
            }
        }
        return f;
    }
    
    /**
     * Selects the IBMFunction to use for a given function category.
     * 
     * As a DEFAULT IMPLEMENTATION, this method throws an UnsupportedOperationException. 
     * 
     * This method SHOULD BE OVERRIDDEN by subclasses that use IBMFunctions.
     * 
     * @param cat
     * @param key 
     */
    @Override
    public void selectIBMFunctionForCategory(String cat, String key){
        throw new UnsupportedOperationException("Not supported yet.");    
    }
    
    @Override
    public void setTypeName(String name){
        typeName = name;
    }
}
