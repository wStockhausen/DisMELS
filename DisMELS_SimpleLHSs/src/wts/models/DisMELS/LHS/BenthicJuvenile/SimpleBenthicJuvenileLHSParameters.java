/*
 * SimpleBenthicJuvenileLHSParameters.java
 *
 * Created on March 20, 2012
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.BenthicJuvenile;

import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterBoolean;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterDouble;

/**
 * DisMELS class representing attributes for "simple" benthic juvenile life stages (think flatfish).
 * 
 * @author William Stockhausen
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageParametersInterface.class)
public class SimpleBenthicJuvenileLHSParameters extends wts.models.DisMELS.framework.AbstractLHSParameters {
    
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
    public static final String PROP_useRandomGrowth        = "use random growth";
    public static final String PROP_useRandomMortality     = "use random mortality";
    public static final String PROP_useRandomTransitions   = "use random transitions";
   
    /** The 'keys' used to store the parameter values */
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
                                         PROP_useRandomGrowth,
                                         PROP_useRandomMortality,
                                         PROP_useRandomTransitions};
    
    /**
     * Creates a new instance of GenericLHSParameters
     */
    public SimpleBenthicJuvenileLHSParameters() {
        super("");
        createMapToParameters();
        propertySupport =  new PropertyChangeSupport(this);
    }
    
    /**
     * Creates a new instance of GenericLHSParameters
     */
    public SimpleBenthicJuvenileLHSParameters(String typeName) {
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
        SimpleBenthicJuvenileLHSParameters clone = null;
        try {
            clone = (SimpleBenthicJuvenileLHSParameters) super.clone();
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
     *  Creates an instance of SimpleBenthicJuvenileLHSParameters.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     *              This should be typeName followed by parameter value (as Strings)
     *              in the same order as the keys.
     */
    @Override
    public SimpleBenthicJuvenileLHSParameters createInstance(final String[] strv) {
        SimpleBenthicJuvenileLHSParameters params;
        int c = 0;
        params = new SimpleBenthicJuvenileLHSParameters(strv[c++]);
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
                PROP_horizDiffusion+cc+
                PROP_minDepth+cc+
                PROP_maxDepth+cc+
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
     * This function does nothing because this class does not use IBMFunctions.
     */
    @Override
    protected void createMapToPotentialFunctions() {
        //do nothing
    }
}
