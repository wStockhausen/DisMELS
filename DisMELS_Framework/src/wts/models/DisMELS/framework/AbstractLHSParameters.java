/*
 * AbstractLHSParameters.java
 *
 * Created on January 18, 2006, 2:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import java.beans.PropertyChangeSupport;
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
    /** class logger */
    private static final Logger logger = Logger.getLogger(AbstractLHSParameters.class.getName());
    
    /* LHS type name assigned to the instance. (Serialized)*/
    protected String typeName = null;    
    /* map of IBM parameters by parameter name. (Serialized) */
    protected Map<String,IBMParameter> mapParams = null;
    /** map of maps of IBMFunctions within each category. (Serialized) */
    protected Map<String,Map<String,IBMFunctionInterface>> mapOfPotentialFunctionsByCategory = null;
    /* map of selected IBM functions by category name. (Serialized) */
    protected Map<String,IBMFunctionInterface> mapOfSelectedFunctionsByCategory = null;
    
    /** Utility field used by bound properties.  */
    protected transient PropertyChangeSupport propertySupport;
    
    /** 
     * Assigns the LHS type name to the constructed subclass instance.
     * 
     * NOTE: use this constructor when instantiating mapParams, 
     * mapOfPotentialFunctionsByCategory, and mapOfSelectedFunctionsByCategory in
     * a subclass's own constructor.
     * 
     *@param typeName - the LHS type name as a String.
     */
    protected AbstractLHSParameters(String typeName) {
        this.typeName = typeName;
    }
    
    /**
     * Assigns the LHS type name to the constructed subclass instance and sets
     * the initial sizes for the IBM parameters (mapParams), potential functions by
     * category (mapOfPotentialFunctionsByCategory), and selected functions by
     * category (mapOfSelectedFunctionsByCategory) maps.
     * 
     * @param typeName - the LHS type name as a String.
     * @param numParams - the number of IBMParameters to be defined
     * @param numFunctionCats - the number of function categories to be defined
     */
    protected AbstractLHSParameters(String typeName, int numParams, int numFunctionCats){
        this.typeName = typeName;
        mapParams = new LinkedHashMap<>(2*numParams);
        mapOfPotentialFunctionsByCategory = new LinkedHashMap<>(2*numFunctionCats);        
        mapOfSelectedFunctionsByCategory  = new LinkedHashMap<>(2*numFunctionCats);
    }

    /**
     *  ABSTRACT METHOD: This method should create and return instance of the subclass it
     * is called on.
     *
     * @param strv - array of Strings used to create the new instance. 
     * 
     * @return  a new instance of the implementing class based on array of Strings.
     */
    @Override
    public abstract LifeStageParametersInterface createInstance(final String[] strv);
     
    /**
     * This method should fill in mapParams with the IBMParameters incorporated
     * in the life stage.
     * 
     * Implementing classes that don't use IBMParameters should throw an UnsupportedOperationException.
     * 
     * Subclasses defining IBMParameters should override this method to create the
     * Map &lt String,IBMParameters &gt mapParams object.
     */
    protected abstract void createMapToParameters();
     
    /**
     * This method should create the Map &lt String, &lt String,IBMFunctionInterface &gt &gt
     * mapOfPotentialFunctionsByCategory, to the IBMFunctions incorporated in the life stage,
     * by category.
     * 
     * Implementing classes that don't use IBMFunctions should throw an UnsupportedOperationException.
     * 
     * Subclasses using IBMFunctions should override this method to create the
     * mapOfPotentialFunctionsByCategory object.
     */
    protected abstract void createMapToPotentialFunctions();
    
    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.removePropertyChangeListener(l);
    }

    /**
     * Sets the LHS type name for the instance.
     * 
     * @param name 
     */
    @Override
    public void setTypeName(String name){
        typeName = name;
    }
    
    /**
     * Returns the unique names (keys) associated with the IBM parameters used in the 
     * life stage (or an empty set if no IBMParamters are defined).
     * 
     * @return - the set of parameter names (keys)
     */
    @Override
    public Set<String> getIBMParameterKeys(){
        if (mapParams==null) return emptySet;
        return mapParams.keySet();
    }
    
    /**
     * Returns the IBMParameter object corresponding to the name.
     * 
     * This method DOES NOT need to be overridden by subclasses (but can be).
     * 
     * @param name - name associated with the IBM parameter
     * 
     * @return     - the IBM parameter instance
     */
    @Override
    public IBMParameter getIBMParameter(String name){
        return mapParams.get(name);
    }
    
    /**
     * Returns the set of function categories.
     * 
     * This method throws an UnsupportedOperationException if IBMFunctions are
     * not used by the implementing class.
     * 
     * @return   - the function categories, as a Set<String>
     */
    @Override
    public Set<String> getIBMFunctionCategories(){
        if (mapOfPotentialFunctionsByCategory==null)
            throw new UnsupportedOperationException("Not supported yet.");
        return mapOfPotentialFunctionsByCategory.keySet();
    }
    
    /**
     * Returns the unique names (keys) associated with the IBM functions used in the
     * life stage for the given category (cat).
     * 
     * This method returns an empty Set if IBMFunctions are not used by the implementing class,
     * or if the category identified by cat is not defined.
     * 
     * @param cat the function category
     * 
     * @return - the set of function names (keys) defined for the category
     */
    @Override
    public Set<String> getIBMFunctionKeysByCategory(String cat){
        if (mapOfPotentialFunctionsByCategory==null) 
             throw new UnsupportedOperationException("Not supported yet.");    
        if (mapOfPotentialFunctionsByCategory.get(cat)==null) return emptySet;
        return mapOfPotentialFunctionsByCategory.get(cat).keySet();
    }
    
    /**
     * Returns the IBMFunctionInterface object corresponding to the 
     * given category and key (function name) identifying the function within the category.
     * 
     * NOTE: This method throws an UnsupportedOperationException if IBMFunctions are
     * not used by the implementing class.
     * 
     * @param cat  - usage category as String
     * @param key - key (function name) identifying function within the category
     * 
     * @return   - the corresponding IBMFunction, or null if the category or key is not defined
     */
    @Override
    public IBMFunctionInterface getIBMFunction(String cat, String key){
        if (mapOfPotentialFunctionsByCategory==null)
            throw new UnsupportedOperationException("Not supported yet.");
        if (mapOfPotentialFunctionsByCategory.containsKey(cat)){
            return mapOfPotentialFunctionsByCategory.get(cat).get(key);
        }
        return null;
    }
    
    /**
     * Sets the IBMFunctionInterface object corresponding to the 
     * given category and key (function name).
     * 
     * This method throws an UnsupportedOperationException if IBMFunctions are
     * not used by the implementing class.
     * 
     * @param cat  - usage category 
     * @param key - the key (function name) identifying the function with the category
     * @param f    - the function to set
     * 
     * @return     - true if successful, false if the category or key is invalid
     */
    @Override
    public boolean setIBMFunction(String cat, String key, IBMFunctionInterface f){
        if (mapOfPotentialFunctionsByCategory==null)
            throw new UnsupportedOperationException("Not supported yet.");
        if (mapOfPotentialFunctionsByCategory.containsKey(cat)){
            if (mapOfPotentialFunctionsByCategory.get(cat).containsKey(key)){
                mapOfPotentialFunctionsByCategory.get(cat).put(key,f);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the "selected" ModelFunctionInterface object corresponding to the category 
     * associated with input "cat".
     * 
     * @param cat - name of the function category
     * 
     * @return   - the "selected" model function
     */
    @Override
    public IBMFunctionInterface getSelectedIBMFunctionForCategory(String cat){
         if (mapOfPotentialFunctionsByCategory==null) 
             throw new UnsupportedOperationException("Not supported yet.");    
        //logger.info("Getting selected IBM parameter function for category '"+cat+"'");
        IBMFunctionInterface f = mapOfSelectedFunctionsByCategory.get(cat);
        if (f==null){
            //logger.info("--No selected IBM parameter function found for category '"+cat+"'");
            //logger.info("----Available functions are:");
            Map<String,IBMFunctionInterface> mapOfPotentialFunctions = mapOfPotentialFunctionsByCategory.get(cat);
            //for (String key: mapOfPotentialFunctions.keySet()) logger.info("------'"+key+"'.");
            String key = mapOfPotentialFunctions.keySet().iterator().next();
            //logger.info("----Selected function set to '"+key+"'.");
            f = mapOfPotentialFunctions.get(key);
            mapOfSelectedFunctionsByCategory.put(cat, f);
        } else {
            //logger.info("----Found selected function '"+f.getFunctionName()+"'.");
        }
        //logger.info("--Selected function is '"+f.getFunctionName()+"'.");
        //logger.info("Finished getting selected IBM parameter function for category '"+cat+"'");
        return f;
    }
    
    /**
     * Sets the selected IBMFunction to use for a given function category.
     * 
     * This method throws an UnsupportedOperationException if IBMFunctions have
     * not been defined.
     * 
     * @param cat - String identifying function category
     * @param key - String identifying function within category to select
     * 
     * @return true if the identified function has been selected.
     */
    @Override
    public boolean setSelectedIBMFunctionForCategory(String cat, String key){
        if (mapOfPotentialFunctionsByCategory==null) 
            throw new UnsupportedOperationException("Not supported yet.");
        boolean res = false;
        if (mapOfPotentialFunctionsByCategory.containsKey(cat)){
            IBMFunctionInterface ifi = mapOfPotentialFunctionsByCategory.get(cat).get(key);
            mapOfSelectedFunctionsByCategory.put(cat,ifi);
            res = (ifi!=null);
        }
        return res;
    }
       
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//      The following are implemented to extend LifeStageDataInterface
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
    
    /**
     * Sets parameter value identified by the key and fires a property change.
     * @param key   - key identifying parameter to be set
     * @param value - value to set
     */
    @Override
    public void setValue(String key, Object value) {
        if (mapParams.containsKey(key)) {
            IBMParameter p = mapParams.get(key);
            Object old = p.getValue();
            p.setValue(value);
            propertySupport.firePropertyChange(key,old,value);
        }
    }
    
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
     * Gets the keys to the IBMParameters.
     * 
     * @return - keys to the map of parameters as a String array.
     */
    @Override
    public String[] getKeys(){
        String[] a = null;
        return mapParams.keySet().toArray(a);
    }
    
    /**
     * Gets the value of the parameter identified by the key as a Boolean.
     * 
     * @param key - String identifying parameter
     * @param b - dummy boolean value
     * @return 
     */
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
}
