/*
 * GenericLHSParameters.java
 *
 * Created on March 20, 2012
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.PelagicStages;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterBoolean;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterDouble;

/**
 * DisMELS class representing attributes for "simple" pelagic life stages (eggs, larvae).
 * 
 * @author William Stockhausen
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageParametersInterface.class)
public class SimplePelagicLHSParameters extends wts.models.DisMELS.framework.AbstractLHSParameters {
    
    public static final String PROP_isSuperIndividual      = "is super indivdual";
    public static final String PROP_hasPreferredDepthDay   = "has preferred depth day";
    public static final String PROP_minDepthDay            = "min depth day (m)";
    public static final String PROP_maxDepthDay            = "max depth day (m)";
    public static final String PROP_willAttachDay          = "will attach to bottom (day)";
    public static final String PROP_hasPreferredDepthNight = "has preferred depth night";
    public static final String PROP_minDepthNight          = "min depth night (m)";
    public static final String PROP_maxDepthNight          = "max depth night (m)";
    public static final String PROP_willAttachNight        = "will attach to bottom (night)";
    public static final String PROP_vertSwimmingSpeed      = "vertical swimming speed (m/s)";
    public static final String PROP_vertDiffusion          = "vertical diffusion (m^2/s)";
    public static final String PROP_horizDiffusion         = "horizontal diffusion (m^2/s)";
    public static final String PROP_minStageDuration       = "min stage duration (d)";
    public static final String PROP_maxStageDuration       = "max stage duration (d)";
    public static final String PROP_minStageSize           = "min stage size (cm)";
    public static final String PROP_stageTransRate         = "stage transition rate (1/d)";
    public static final String PROP_growthRate             = "growth rate (cm/d)";
    public static final String PROP_mortalityRate          = "mortality rate (1/d)";
    public static final String PROP_useRandomGrowth        = "use random growth";
    public static final String PROP_useRandomMortality     = "use random mortality";
    public static final String PROP_useRandomTransitions   = "use random transitions";
   
    /** The 'keys' used to store the parameter values */
    private static final String[] keys = {PROP_isSuperIndividual,
                                         PROP_hasPreferredDepthDay,
                                         PROP_minDepthDay,
                                         PROP_maxDepthDay,
                                         PROP_willAttachDay,
                                         PROP_hasPreferredDepthNight,
                                         PROP_minDepthNight,
                                         PROP_maxDepthNight,
                                         PROP_willAttachNight,
                                         PROP_vertSwimmingSpeed,
                                         PROP_vertDiffusion,
                                         PROP_horizDiffusion,
                                         PROP_minStageDuration,
                                         PROP_maxStageDuration,
                                         PROP_minStageSize,
                                         PROP_stageTransRate,
                                         PROP_growthRate,
                                         PROP_mortalityRate,
                                         PROP_useRandomGrowth,
                                         PROP_useRandomMortality,
                                         PROP_useRandomTransitions};
    
    /**
     * Utility field used by bound properties.
     */
    private PropertyChangeSupport propertySupport;
    
    /**
     * Creates a new instance of SimplePelagicLHSParameters
     */
    public SimplePelagicLHSParameters() {
        super("");
        createMapToValues();
        propertySupport =  new PropertyChangeSupport(this);
    }
    
    /**
     * Creates a new instance of GenericLHSParameters
     */
    public SimplePelagicLHSParameters(String typeName) {
        super(typeName);
        createMapToValues();
        propertySupport =  new PropertyChangeSupport(this);
    }

    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        SimplePelagicLHSParameters clone = null;
        try {
            clone = (SimplePelagicLHSParameters) super.clone();
            for (int i=0;i<keys.length;i++) {
                clone.setValue(keys[i],this.getValue(keys[i]));
            }
            clone.propertySupport = new PropertyChangeSupport(clone);
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return clone;
    }

    /**
     *  Creates an instance of SimplePelagicLHSParameters.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     *              This should be typeName followed by parameter value (as Strings)
     *              in the same order as the keys.
     */
    @Override
    public SimplePelagicLHSParameters createInstance(final String[] strv) {
        SimplePelagicLHSParameters params;
        int c = 0;
        params = new SimplePelagicLHSParameters(strv[c++]);
        params.setValue(PROP_isSuperIndividual,     Boolean.valueOf(strv[c++]));
        params.setValue(PROP_hasPreferredDepthDay,  Boolean.valueOf(strv[c++]));
        params.setValue(PROP_minDepthDay,           Double.valueOf(strv[c++]));
        params.setValue(PROP_maxDepthDay,           Double.valueOf(strv[c++]));
        params.setValue(PROP_willAttachDay,         Boolean.valueOf(strv[c++]));
        params.setValue(PROP_hasPreferredDepthNight,Boolean.valueOf(strv[c++]));
        params.setValue(PROP_minDepthNight,         Double.valueOf(strv[c++]));
        params.setValue(PROP_maxDepthNight,         Double.valueOf(strv[c++]));
        params.setValue(PROP_willAttachNight,       Boolean.valueOf(strv[c++]));
        params.setValue(PROP_vertSwimmingSpeed,     Double.valueOf(strv[c++]));
        params.setValue(PROP_vertDiffusion,         Double.valueOf(strv[c++]));
        params.setValue(PROP_horizDiffusion,        Double.valueOf(strv[c++]));
        params.setValue(PROP_minStageDuration,      Double.valueOf(strv[c++]));
        params.setValue(PROP_maxStageDuration,      Double.valueOf(strv[c++]));
        params.setValue(PROP_minStageSize,          Double.valueOf(strv[c++]));
        params.setValue(PROP_stageTransRate,        Double.valueOf(strv[c++]));
        params.setValue(PROP_growthRate,            Double.valueOf(strv[c++]));
        params.setValue(PROP_mortalityRate,         Double.valueOf(strv[c++]));
        params.setValue(PROP_useRandomGrowth,       Boolean.valueOf(strv[c++]));
        params.setValue(PROP_useRandomMortality,    Boolean.valueOf(strv[c++]));
        params.setValue(PROP_useRandomTransitions,  Boolean.valueOf(strv[c++]));
        return params;
    }
    
    /**
     * This creates the basic parameters mapParams.
     */
    @Override
    protected void createMapToValues() {
        mapParams = new HashMap<>();
        mapParams.put(PROP_isSuperIndividual,     new IBMParameterBoolean(PROP_isSuperIndividual,PROP_isSuperIndividual,false));
        mapParams.put(PROP_hasPreferredDepthDay,  new IBMParameterBoolean(PROP_hasPreferredDepthDay,PROP_hasPreferredDepthDay,true));
        mapParams.put(PROP_minDepthDay,           new IBMParameterDouble(PROP_minDepthDay,PROP_minDepthDay,new Double(5)));
        mapParams.put(PROP_maxDepthDay,           new IBMParameterDouble(PROP_maxDepthDay,PROP_maxDepthDay,new Double(20)));
        mapParams.put(PROP_willAttachDay,         new IBMParameterBoolean(PROP_willAttachDay,PROP_willAttachDay,false));
        mapParams.put(PROP_hasPreferredDepthNight,new IBMParameterBoolean(PROP_hasPreferredDepthNight,PROP_hasPreferredDepthNight,true));
        mapParams.put(PROP_minDepthNight,         new IBMParameterDouble(PROP_minDepthNight,PROP_minDepthNight,new Double(80)));
        mapParams.put(PROP_maxDepthNight,         new IBMParameterDouble(PROP_maxDepthNight,PROP_maxDepthNight,new Double(100)));
        mapParams.put(PROP_willAttachNight,       new IBMParameterBoolean(PROP_willAttachNight,PROP_willAttachNight,false));
        mapParams.put(PROP_vertSwimmingSpeed,     new IBMParameterDouble(PROP_vertSwimmingSpeed,PROP_vertSwimmingSpeed,new Double(0.01)));
        mapParams.put(PROP_vertDiffusion,         new IBMParameterDouble(PROP_vertDiffusion,PROP_vertDiffusion,new Double(0)));
        mapParams.put(PROP_horizDiffusion,        new IBMParameterDouble(PROP_horizDiffusion,PROP_horizDiffusion,new Double(0)));
        mapParams.put(PROP_minStageDuration,      new IBMParameterDouble(PROP_minStageDuration,PROP_minStageDuration,new Double(0)));
        mapParams.put(PROP_maxStageDuration,      new IBMParameterDouble(PROP_maxStageDuration,PROP_maxStageDuration,Double.POSITIVE_INFINITY));
        mapParams.put(PROP_minStageSize,          new IBMParameterDouble(PROP_minStageSize,PROP_minStageSize,new Double(0)));
        mapParams.put(PROP_stageTransRate,        new IBMParameterDouble(PROP_stageTransRate,PROP_stageTransRate,new Double(0)));
        mapParams.put(PROP_growthRate,            new IBMParameterDouble(PROP_growthRate,PROP_growthRate,new Double(0)));
        mapParams.put(PROP_mortalityRate,         new IBMParameterDouble(PROP_mortalityRate,PROP_mortalityRate,new Double(0)));
        mapParams.put(PROP_useRandomGrowth,       new IBMParameterBoolean(PROP_useRandomGrowth,PROP_useRandomGrowth,false));
        mapParams.put(PROP_useRandomMortality,    new IBMParameterBoolean(PROP_useRandomMortality,PROP_useRandomMortality,false));
        mapParams.put(PROP_useRandomTransitions,  new IBMParameterBoolean(PROP_useRandomTransitions,PROP_useRandomTransitions,false));
    }

    /**
     * Returns a CSV string representation of the parameter values.
     * This method should be overriden by subclasses that add additional parameters, 
     * possibly calling super.getCSV() to get an initial csv string to which 
     * additional field values could be appended.
     * 
     *@return - CSV string parameter values
     */
    @Override
    public String getCSV() {
        String str = typeName+cc+
        getValue(PROP_isSuperIndividual,b)+cc+
        getValue(PROP_hasPreferredDepthDay,b)+cc+
        getValue(PROP_minDepthDay,d)+cc+
        getValue(PROP_maxDepthDay,d)+cc+
        getValue(PROP_willAttachDay,b)+cc+
        getValue(PROP_hasPreferredDepthNight,b)+cc+
        getValue(PROP_minDepthNight,d)+cc+
        getValue(PROP_maxDepthNight,d)+cc+
        getValue(PROP_willAttachNight,b)+cc+
        getValue(PROP_vertSwimmingSpeed,d)+cc+
        getValue(PROP_vertDiffusion,d)+cc+
        getValue(PROP_horizDiffusion,d)+cc+
        getValue(PROP_minStageDuration,d)+cc+
        getValue(PROP_maxStageDuration,d)+cc+
        getValue(PROP_minStageSize,d)+cc+
        getValue(PROP_stageTransRate,d)+cc+
        getValue(PROP_growthRate,d)+cc+
        getValue(PROP_mortalityRate,d)+cc+
        getValue(PROP_useRandomGrowth,b)+cc+
        getValue(PROP_useRandomMortality,b)+cc+
        getValue(PROP_useRandomTransitions,b);
        return str;
    }
                
    /**
     * Returns the comma-delimited string corresponding to the parameters
     * to be used as a header for a csv file.  
     * This should be overriden by subclasses that add additional parameters, 
     * possibly calling super.getCSVHeader() to get an initial header string 
     * to which additional field names could be appended.
     * Use getCSV() to get the string of actual parameter values.
     *
     *@return - String of CSV header names
     */
    @Override
    public String getCSVHeader() {
        String str = "LHS type name"+cc+
                PROP_isSuperIndividual+cc+
                PROP_hasPreferredDepthDay+cc+
                PROP_minDepthDay+cc+
                PROP_maxDepthDay+cc+
                PROP_willAttachDay+cc+
                PROP_hasPreferredDepthNight+cc+
                PROP_minDepthNight+cc+
                PROP_maxDepthNight+cc+
                PROP_willAttachNight+cc+
                PROP_vertSwimmingSpeed+cc+
                PROP_vertDiffusion+cc+
                PROP_horizDiffusion+cc+
                PROP_minStageDuration+cc+
                PROP_maxStageDuration+cc+
                PROP_minStageSize+cc+
                PROP_stageTransRate+cc+
                PROP_growthRate+cc+
                PROP_mortalityRate+cc+
                PROP_useRandomGrowth+cc+
                PROP_useRandomMortality+cc+
                PROP_useRandomTransitions;
        return str;
    }

    /**
     * Gets the parameter keys.
     * 
     * @return - keys as String array.
     */
    @Override
    public String[] getKeys(){
        return keys;
    }

    /**
     * Sets parameter value identified by the key and fires a property change.
     * @param key   - key identifying attribute to be set
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

    @Override
    protected void createMapToSelectedFunctions() {
        //do nothing
    }

    @Override
    public Set<String> getIBMFunctionNamesByCategory(String cat) {
        return mapOfSelectedFunctionsByCategory.keySet();
    }

    @Override
    public Set<String> getIBMParameterNames() {
        return mapParams.keySet();
    }
}
