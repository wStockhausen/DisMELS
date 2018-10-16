/*
 * SimpleBenthicAdultLHSParameters.java
 *
 * Created on January 18, 2006, 4:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.BenthicAdult;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterBoolean;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterDouble;

/**
 *
 * @author William Stockhausen
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageParametersInterface.class)
public class SimpleBenthicAdultLHSParameters extends wts.models.DisMELS.framework.AbstractLHSParameters {
    
    public static final String PROP_isSuperIndividual      = "is super indivdual";
    public static final String PROP_horizDiffusion         = "horizontal diffusion (m^2/s)";
    public static final String PROP_minDepth               = "min depth (m)";
    public static final String PROP_maxDepth               = "max depth (m)";
    public static final String PROP_minStageDuration       = "min stage duration (d)";
    public static final String PROP_maxStageDuration       = "max stage duration (d)";
    public static final String PROP_minStageSize           = "min stage size (cm)";
    public static final String PROP_stageTransRate         = "stage transition rate (1/d)";
    public static final String PROP_growthRate             = "growth rate (cm/d)";
    public static final String PROP_mortalityRate          = "mortality rate (1/d)";
    public static final String PROP_firstDaySpawning       = "first day spawning";
    public static final String PROP_lengthSpawningSeason   = "length of spawning season (d)";
    public static final String PROP_fecundity              = "fecundity";
    public static final String PROP_isBatchSpawner         = "is batch spawner";
    public static final String PROP_recoveryPeriod         = "recovery period (d)";
    public static final String PROP_batchPeriod            = "batch period (d)";
    public static final String PROP_useRandomGrowth        = "use random growth";
    public static final String PROP_useRandomMortality     = "use random mortality";
    public static final String PROP_useRandomTransitions   = "use random transitions";
    public static final String PROP_useRandomSpawning      = "use random spawning";
   
    private static final String[] keys = {PROP_isSuperIndividual,
                                         PROP_horizDiffusion,
                                         PROP_minDepth,
                                         PROP_maxDepth,
                                         PROP_minStageDuration,
                                         PROP_maxStageDuration,
                                         PROP_minStageSize,
                                         PROP_stageTransRate,
                                         PROP_growthRate,
                                         PROP_mortalityRate,
                                         PROP_firstDaySpawning,
                                         PROP_lengthSpawningSeason,
                                         PROP_fecundity,
                                         PROP_isBatchSpawner,
                                         PROP_recoveryPeriod,
                                         PROP_batchPeriod,
                                         PROP_useRandomGrowth,
                                         PROP_useRandomMortality,
                                         PROP_useRandomTransitions,
                                         PROP_useRandomSpawning};
    
    /**
     * Utility field used by bound properties.
     */
    private PropertyChangeSupport propertySupport;
    
    /**
     * Creates a new instance of GenericLHSParameters
     */
    public SimpleBenthicAdultLHSParameters() {
        super("");
        createMapToParameters();
        propertySupport =  new PropertyChangeSupport(this);
    }
    
    /**
     * Creates a new instance of GenericLHSParameters
     */
    public SimpleBenthicAdultLHSParameters(String typeName) {
        super(typeName);
        createMapToParameters();
        propertySupport =  new PropertyChangeSupport(this);
    }

    @Override
    public Object clone() {
        SimpleBenthicAdultLHSParameters clone = null;
        try {
            clone = (SimpleBenthicAdultLHSParameters) super.clone();
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
     *  Creates an instance of SimpleBenthicAdultLHSParameters.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     *              This should be typeName followed by parameter value (as Strings)
     *              in the same order as the keys.
     */
    @Override
    public SimpleBenthicAdultLHSParameters createInstance(final String[] strv) {
        SimpleBenthicAdultLHSParameters params = null;
        int c = 0;
        params = new SimpleBenthicAdultLHSParameters(strv[c++]);
        params.setValue(PROP_isSuperIndividual,     Boolean.valueOf(strv[c++]));
        params.setValue(PROP_horizDiffusion,        Double.valueOf(strv[c++]));
        params.setValue(PROP_minDepth,              Double.valueOf(strv[c++]));
        params.setValue(PROP_maxDepth,              Double.valueOf(strv[c++]));
        params.setValue(PROP_minStageDuration,      Double.valueOf(strv[c++]));
        params.setValue(PROP_maxStageDuration,      Double.valueOf(strv[c++]));
        params.setValue(PROP_minStageSize,          Double.valueOf(strv[c++]));
        params.setValue(PROP_stageTransRate,        Double.valueOf(strv[c++]));
        params.setValue(PROP_growthRate,            Double.valueOf(strv[c++]));
        params.setValue(PROP_mortalityRate,         Double.valueOf(strv[c++]));
        params.setValue(PROP_firstDaySpawning,      Double.valueOf(strv[c++]));
        params.setValue(PROP_lengthSpawningSeason,  Double.valueOf(strv[c++]));
        params.setValue(PROP_fecundity,             Double.valueOf(strv[c++]));
        params.setValue(PROP_isBatchSpawner,        Boolean.valueOf(strv[c++]));
        params.setValue(PROP_recoveryPeriod,        Double.valueOf(strv[c++]));
        params.setValue(PROP_batchPeriod,           Double.valueOf(strv[c++]));
        params.setValue(PROP_useRandomGrowth,       Boolean.valueOf(strv[c++]));
        params.setValue(PROP_useRandomMortality,    Boolean.valueOf(strv[c++]));
        params.setValue(PROP_useRandomTransitions,  Boolean.valueOf(strv[c++]));
        params.setValue(PROP_useRandomSpawning,     Boolean.valueOf(strv[c++]));
        return params;
    }
    
    /**
     * This creates the basic parameters mapParams.
     */
    @Override
    protected void createMapToParameters() {
        mapParams = new HashMap<>();
        mapParams.put(PROP_isSuperIndividual,     new IBMParameterBoolean(PROP_isSuperIndividual,PROP_isSuperIndividual,false));
        mapParams.put(PROP_horizDiffusion,        new IBMParameterDouble(PROP_horizDiffusion,PROP_horizDiffusion,new Double(0)));
        mapParams.put(PROP_minDepth,              new IBMParameterDouble(PROP_minDepth,PROP_minDepth,new Double(5)));
        mapParams.put(PROP_maxDepth,              new IBMParameterDouble(PROP_maxDepth,PROP_maxDepth,new Double(20)));
        mapParams.put(PROP_minStageDuration,      new IBMParameterDouble(PROP_minStageDuration,PROP_minStageDuration,new Double(0)));
        mapParams.put(PROP_maxStageDuration,      new IBMParameterDouble(PROP_maxStageDuration,PROP_maxStageDuration,Double.POSITIVE_INFINITY));
        mapParams.put(PROP_minStageSize,          new IBMParameterDouble(PROP_minStageSize,PROP_minStageSize,new Double(0)));
        mapParams.put(PROP_stageTransRate,        new IBMParameterDouble(PROP_stageTransRate,PROP_stageTransRate,new Double(0)));
        mapParams.put(PROP_growthRate,            new IBMParameterDouble(PROP_growthRate,PROP_growthRate,new Double(0)));
        mapParams.put(PROP_mortalityRate,         new IBMParameterDouble(PROP_mortalityRate,PROP_mortalityRate,new Double(0)));
        mapParams.put(PROP_useRandomGrowth,       new IBMParameterBoolean(PROP_useRandomGrowth,PROP_useRandomGrowth,false));
        mapParams.put(PROP_useRandomMortality,    new IBMParameterBoolean(PROP_useRandomMortality,PROP_useRandomMortality,false));
        mapParams.put(PROP_useRandomTransitions,  new IBMParameterBoolean(PROP_useRandomTransitions,PROP_useRandomTransitions,false));
        mapParams.put(PROP_firstDaySpawning,      new IBMParameterDouble(PROP_firstDaySpawning,PROP_firstDaySpawning,new Double(0)));
        mapParams.put(PROP_lengthSpawningSeason,  new IBMParameterDouble(PROP_lengthSpawningSeason,PROP_lengthSpawningSeason,new Double(365)));
        mapParams.put(PROP_fecundity,             new IBMParameterDouble(PROP_fecundity,PROP_fecundity,new Double(0)));
        mapParams.put(PROP_isBatchSpawner,        new IBMParameterBoolean(PROP_isBatchSpawner,PROP_isBatchSpawner,false));
        mapParams.put(PROP_recoveryPeriod,        new IBMParameterDouble(PROP_recoveryPeriod,PROP_recoveryPeriod,new Double(0)));
        mapParams.put(PROP_batchPeriod,           new IBMParameterDouble(PROP_batchPeriod,PROP_batchPeriod,new Double(0)));
        mapParams.put(PROP_useRandomSpawning,     new IBMParameterBoolean(PROP_useRandomSpawning,PROP_useRandomSpawning,false));
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
        getValue(PROP_horizDiffusion,d)+cc+
        getValue(PROP_minDepth,d)+cc+
        getValue(PROP_maxDepth,d)+cc+
        getValue(PROP_minStageDuration,d)+cc+
        getValue(PROP_maxStageDuration,d)+cc+
        getValue(PROP_minStageSize,d)+cc+
        getValue(PROP_stageTransRate,d)+cc+
        getValue(PROP_growthRate,d)+cc+
        getValue(PROP_mortalityRate,d)+cc+
        getValue(PROP_firstDaySpawning,d)+cc+
        getValue(PROP_lengthSpawningSeason,d)+cc+
        getValue(PROP_fecundity,d)+cc+
        getValue(PROP_isBatchSpawner,b)+cc+
        getValue(PROP_recoveryPeriod,d)+cc+
        getValue(PROP_batchPeriod,d)+cc+
        getValue(PROP_useRandomGrowth,b)+cc+
        getValue(PROP_useRandomMortality,b)+cc+
        getValue(PROP_useRandomTransitions,b)+cc+
        getValue(PROP_useRandomSpawning,b);
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
                PROP_horizDiffusion+cc+
                PROP_minDepth+cc+
                PROP_maxDepth+cc+
                PROP_minStageDuration+cc+
                PROP_maxStageDuration+cc+
                PROP_minStageSize+cc+
                PROP_stageTransRate+cc+
                PROP_growthRate+cc+
                PROP_mortalityRate+cc+
                PROP_firstDaySpawning+cc+
                PROP_lengthSpawningSeason+cc+
                PROP_fecundity+cc+
                PROP_isBatchSpawner+cc+
                PROP_recoveryPeriod+cc+
                PROP_batchPeriod+cc+
                PROP_useRandomGrowth+cc+
                PROP_useRandomMortality+cc+
                PROP_useRandomTransitions+cc+
                PROP_useRandomSpawning;
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
