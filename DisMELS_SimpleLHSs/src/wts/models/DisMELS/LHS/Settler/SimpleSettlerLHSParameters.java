/*
 * SimpleSettlerLHSParameters.java
 *
 * Created on March 20, 2012
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.Settler;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterBoolean;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterDouble;

/**
 * DisMELS class representing attributes for "simple" settler life stages (think flatfish).
 * 
 * @author William Stockhausen
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageParametersInterface.class)
public class SimpleSettlerLHSParameters extends wts.models.DisMELS.framework.AbstractLHSParameters {
    
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
    public static final String PROP_maxStageDuration       = "max stage duration (d)";
    public static final String PROP_minSettlementDepth     = "min settlement depth (m)";
    public static final String PROP_maxSettlementDepth     = "max settlement depth (m)";
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
                                         PROP_maxStageDuration,
                                         PROP_minSettlementDepth,
                                         PROP_maxSettlementDepth,
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
     * Creates a new instance of GenericLHSParameters
     */
    public SimpleSettlerLHSParameters() {
        super("");
        createMapToParameters();
        propertySupport =  new PropertyChangeSupport(this);
    }
    
    /**
     * Creates a new instance of GenericLHSParameters
     */
    public SimpleSettlerLHSParameters(String typeName) {
        super(typeName);
        createMapToParameters();
        propertySupport =  new PropertyChangeSupport(this);
    }

    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        SimpleSettlerLHSParameters clone = null;
        try {
            clone = (SimpleSettlerLHSParameters) super.clone();
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
     *  Creates an instance of SimpleSettlerLHSParameters.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     *              This should be typeName followed by parameter value (as Strings)
     *              in the same order as the keys.
     */
    @Override
    public SimpleSettlerLHSParameters createInstance(final String[] strv) {
        SimpleSettlerLHSParameters params;
        int c = 0;
        params = new SimpleSettlerLHSParameters(strv[c++]);
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
        params.setValue(PROP_maxStageDuration,      Double.valueOf(strv[c++]));
        params.setValue(PROP_minSettlementDepth,    Double.valueOf(strv[c++]));
        params.setValue(PROP_maxSettlementDepth,    Double.valueOf(strv[c++]));
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
    protected void createMapToParameters() {
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
        mapParams.put(PROP_maxStageDuration,      new IBMParameterDouble(PROP_maxStageDuration,PROP_maxStageDuration,Double.POSITIVE_INFINITY));
        mapParams.put(PROP_minSettlementDepth,    new IBMParameterDouble(PROP_minSettlementDepth,PROP_minSettlementDepth,new Double(0)));
        mapParams.put(PROP_maxSettlementDepth,    new IBMParameterDouble(PROP_maxSettlementDepth,PROP_maxSettlementDepth,Double.POSITIVE_INFINITY));
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
        getValue(PROP_maxStageDuration,d)+cc+
        getValue(PROP_minSettlementDepth,d)+cc+
        getValue(PROP_maxSettlementDepth,d)+cc+
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
                PROP_maxStageDuration+cc+
                PROP_minSettlementDepth+cc+
                PROP_maxSettlementDepth+cc+
                PROP_stageTransRate+cc+
                PROP_growthRate+cc+
                PROP_mortalityRate+cc+
                PROP_useRandomGrowth+cc+
                PROP_useRandomMortality+cc+
                PROP_useRandomTransitions;
        return str;
    }
    
    /**
     * This function does nothing because this class does not use IBMFunctions.
     */
    @Override
    protected void createMapToPotentialFunctions() {
        //do nothing
    }

}
