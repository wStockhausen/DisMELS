/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMFunctions;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author William.Stockhausen
 */
public abstract class AbstractIBMFunction implements IBMFunctionInterface {

    /** the parameters */
    protected final Map<String,IBMParameter> params;
    /** subfunctions */
    protected final Map<String,IBMFunctionInterface> subfuncs;
    
    /** function type */
    private String type = "";
    /** function name */
    private String name = "";
    /** function description */
    private String descr = "";
    /** full description of function */
    private String fullDescr = "";
    
    protected final PropertyChangeSupport propSupport;
    
    /**
     * 
     * @param numParams
     * @param numSubFuncs 
     */
    protected AbstractIBMFunction(int numParams,int numSubFuncs){
        params      = new LinkedHashMap<>(2*numParams);
        subfuncs    = new LinkedHashMap<>(2*numSubFuncs);
        propSupport = new PropertyChangeSupport(this);
    }
    
    /**
     * 
     * @param numParams
     * @param numSubFuncs
     * @param type
     * @param name
     * @param descr
     * @param fullDescr 
     */
    protected AbstractIBMFunction(int numParams,int numSubFuncs, String type, String name, String descr, String fullDescr){
        params      = new LinkedHashMap<>(2*numParams);
        subfuncs    = new LinkedHashMap<>(2*numSubFuncs);
        this.type      = type;
        this.name      = name;
        this.descr     = descr;
        this.fullDescr = fullDescr;
        propSupport = new PropertyChangeSupport(this);
    }
    
    /**
     * Adds a parameter (name, description and class) to the set of parameters.
     * 
     * @param name
     * @param cls 
     * @param description
     */
    protected final void addParameter(String name, Class cls, String description){
        if (cls.equals(boolean.class)) params.put(name,new IBMParameterBoolean(name,description)); else
        if (cls.equals(Boolean.class)) params.put(name,new IBMParameterBoolean(name,description)); else
        if (cls.equals(double.class))  params.put(name,new IBMParameterDouble(name,description)); else
        if (cls.equals(Double.class))  params.put(name,new IBMParameterDouble(name,description)); else
//        if (cls.equals(float.class))   params.put(name,new IBMParameterFloat(name,description)); else
//        if (cls.equals(Float.class))   params.put(name,new IBMParameterFloat(name,description)); else
//        if (cls.equals(int.class))     params.put(name,new IBMParameterInteger(name,description)); else
//        if (cls.equals(Integer.class)) params.put(name,new IBMParameterInteger(name,description)); else
//        if (cls.equals(long.class))    params.put(name,new IBMParameterLong(name,description)); else
//        if (cls.equals(Long.class))    params.put(name,new IBMParameterLong(name,description)); else
        if (cls.equals(String.class))  params.put(name,new IBMParameterString(name,description));
    }   
    
    /**
     * Adds a parameter (name, description and class) to the set of parameters.
     * 
     * @param name
     * @param value 
     * @param description
     */
    protected final void addParameter(String name, Object value, String description){
        if (value.getClass().equals(boolean.class)) params.put(name,new IBMParameterBoolean(name,description, (boolean) value)); else
        if (value.getClass().equals(Boolean.class)) params.put(name,new IBMParameterBoolean(name,description, (Boolean) value)); else
        if (value.getClass().equals(double.class))  params.put(name,new IBMParameterDouble( name,description, (double) value)); else
        if (value.getClass().equals(Double.class))  params.put(name,new IBMParameterDouble( name,description, (Double) value)); else
//        if (value.getClass().equals(float.class))   params.put(name,new IBMParameterFloat( name,description,  (float) value)); else
//        if (value.getClass().equals(Float.class))   params.put(name,new IBMParameterFloat( name,description,  (Float) value)); else
//        if (value.getClass().equals(int.class))     params.put(name,new IBMParameterInteger( name,description,(int) value)); else
//        if (value.getClass().equals(Integer.class)) params.put(name,new IBMParameterInteger( name,description,(Integer) value)); else
//        if (value.getClass().equals(long.class))    params.put(name,new IBMParameterLong( name,description,   (long) value)); else
//        if (value.getClass().equals(Long.class))    params.put(name,new IBMParameterLong( name,description,   (Long) value)); else
        if (value instanceof String)                params.put(name,new IBMParameterString( name,description, (String) value));
    }   
    
    /**
     * Add the subfunction subFunc to the map of subfunctions using the key "name".
     * @param name
     * @param subFunc 
     */
    protected final void addSubfunction(String name, IBMFunctionInterface subFunc){
        subfuncs.put(name, subFunc);
    }
    
    /**
     * Associates an instance of an IBMFunction with the given subfunction name. Implementations
     * should PROBABLY check that the new instance and the old instance (if any) are members of the 
     * same IBMFunction class.
     * 
     * @param name
     * @param ifi
     * @return 
     */
    @Override
    public boolean setSubfunction(String name, IBMFunctionInterface ifi){
        if (subfuncs.containsKey(name)){
            subfuncs.put(name,ifi);
            return true;
        }
        return false;
    }
    
    @Override
    public abstract Object clone();
   
    /**
     * Gets the "type", i.e. the classification, of this model function.
     * Subclasses need to implement a concrete version of this method.
     * 
     * @return the "type" of the model function
     */
    @Override
    public final String getFunctionType(){return type;}
    
    /**
     * Sets the function type, i.e. the classification, of this model function.
     * @param type 
     */
    @Override
    public final void setFunctionType(String type){this.type = type;}
   
    /**
     * Gets the user-friendly name of this model function.
     * Subclasses need to implement a concrete version of this method.
     * 
     * @return the "type" of the model function
     */
    @Override
    public final String getFunctionName(){return name;}
    
    /**
     * Sets the function name.
     * @param name 
     */
    @Override
    public final void setFunctionName(String name){this.name = name;}
   
    /**
     * Gets a (brief) description of this model function.
     * Subclasses need to implement a concrete version of this method.
     * 
     * @return a (brief) description of the model function
     */
    @Override
    public final String getDescription(){return descr;}
    
    /**
     * Sets the (brief) description of the function.
     * @param descr 
     */
    @Override
    public final void setDescription(String descr){this.descr=descr;}
   
    /**
     * Gets a full description of this model function.
     * Subclasses need to implement a concrete version of this method.
     * 
     * @return a full description of the model function
     */
    @Override
    public final String getFullDescription(){return fullDescr;}
    
    /**
     * Sets the full description of the function.
     * @param fdescr 
     */
    @Override
    public final void setFullDescription(String fdescr){fullDescr=fdescr;}
    
    /**
     * Returns true if the function has parameters that can be set.
     * 
     * @return - true/false
     */
    @Override
    public final boolean hasParameters(){return !params.isEmpty();}
    
    /**
     * Gets the ModelFunctionParameter assigned to the given name.
     * @param  name - the name of the parameter
     * @return - the ModelFunctionParameter associated with the name
     */
    @Override
    public final IBMParameter getParameter(String name){return params.get(name);}
    
    @Override
    public final Set<String> getParameterNames(){return params.keySet();}

    
    /**
     * Returns the description of the parameter associated with the given name.
     * 
     * @param name - the parameter name
     * @return  - the description of the parameter
     */
    @Override
    public final String getParameterDescription(String name){
        return params.get(name).getDescription();
    }
    
    /**
     * Sets the description for the parameter associated with the given name.
     * 
     * @param name
     * @param descr 
     */
    @Override
    public final void setParameterDescription(String name, String descr){
        params.get(name).setDescription(descr);
    }
    
    
    /**
     * Sets the value of the function parameter identified by the 'name' key prior to calculating 
     * the function's value (see calculate).
     * 
     * @param name - name of the parameter to set
     * @param value - key/value map of the parameters' values 
     */
    @Override
    public boolean setParameterValue(String name, Object value) {
        IBMParameter mfp = params.get(name);
        mfp.setValue(value);
        return true;
    }
    
    /**
     * Adds a PropertyChangeListener instance to list of objects to be notified of 
     * PropertyChanges.
     * 
     * @param listener - object to add
     */
    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        propSupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Removes a PropertyChangeListener instance from list of objects to be notified of 
     * PropertyChanges.
     * 
     * @param listener - object to remove
     */
    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        propSupport.removePropertyChangeListener(listener);
    }
    
    /**
     * This method calculates the "value" of the function based on a List of
     * input variable params.  
     * 
     * @param vars - an Object encapsulating the params of input variables
     * @return     - an Object encapsulating the params of the function
     */
    @Override
    public abstract Object calculate(Object vars);
    
    /**
     * Returns true if the function contains subfunctions.
     * 
     * @return - true/false
     */
    @Override
    public final boolean hasSubfunctions(){return !subfuncs.isEmpty();}
    
    /**
     * Retruns the names of the subfunctions used in this function
     * @return 
     */
    @Override
    public final Set<String> getSubfunctionNames(){return subfuncs.keySet();} 
    
    /**
     * Returns the subfunction associated with the given key.
     * 
     * @param key - subfunction name
     * @return - the subfunction
     */
    @Override
    public final IBMFunctionInterface getSubfunction(String key){return subfuncs.get(key);}

    /**
     *  Creates an instance of a subclass.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     */
    @Override
    public IBMFunctionInterface createInstance(final String[] strv){
        throw new UnsupportedOperationException("Not supported yet");
    }
    
    /**
     * Returns a CSV string representation of the parameter values
     *
     *@return - CSV string of parameter values
     */
    @Override
    public String getCSV(){return "";}
    
    /**
     * Returns the header line for a csv file representation
     * of a set of parameters.  Use getCSV() to get the string
     * of actual parameter values.
     *
     *@return - String of CSV header names
     */
    @Override
    public String getCSVHeader(){return "";}
}
