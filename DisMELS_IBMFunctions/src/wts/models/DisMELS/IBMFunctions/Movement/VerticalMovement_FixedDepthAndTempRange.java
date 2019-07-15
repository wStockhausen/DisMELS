/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Movement;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMMovementFunctionInterface;

/**
 * This class provides an implementation of vertical movement within
 * fixed, "preferred", depth and temperature ranges.  
 * 
 * <p>
 * When inside the preferred depth and temperature ranges, 
 * vertical movement follows an uncorrelated random walk.  
 * When outside the preferred range, a vertical swimming speed 
 * (externally calculated) is applied in the direction that 
 * would move the individual toward the preferred range.If the two
 * ranges don't overlap, then one (user-determined) takes precedence.
 * </p>
 * <pre>
 * 
 * Function type: 
 *      vertical movement
 * Parameters (by key):
 *      minDepth         - Double: min preferred depth (m)
 *      maxDepth         - Double: max preferred depth (m)
 *      minDistOffBottom - Double: min distance off bottom (m)
 *      minTemp          - Double: min preferred temperature (deg C)
 *      maxTemp          - Double: max preferred temperature (deg C)
 *      rpw              - Double: random walk parameter w/in preferred depth range ([distance]^2/[time])
 *      tempTakesPrecedence - Boolean: flag indicating that the temperature range takes precedence over the depth range
 * Variables:
 *   double[] with elements:
 *      dt      - [0] - integration time step (s)
 *      depth   - [1] - current depth of individual, relative to mean sea level (m)
 *      bathyym - [2] - bathymetric depth at location (m)
 *      temp    - [3] - temperature at location
 *      w       - [4] - active vertical swimming speed outside preferred depth range (m/s)
 * Value:
 *      Double: vertical swimming speed (same units as w)
 * Calculation:
 *      eps  = N(0,sigV) [random draw from a normal distribution)
 *      v    = w*delta(outside preferred depth range) + sqrt(rpw/dt)*eps;
 * </pre>
 * 
 * @author William Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMMovementFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class VerticalMovement_FixedDepthAndTempRange extends AbstractIBMFunction 
                                            implements IBMMovementFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Swimming speed";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Vertical movement";
    /** function description */
    public static final String DEFAULT_descr = "Vertical movement within a (fixed) preferred depth/temperature range";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of vertical movement relative to a"+
            "\n\t* fixed, \"preferred\", depth and temperature ranges.  When inside the preferred range, vertical movement"+
            "\n\t* is described as an uncorrelated random walk.  When outside the preferred range,"+
            "\n\t* a vertical swimming speed (externally calculated) is applied"+
            "\n\t* in the direction that would move the individual toward the preferred depth range."+
            "\n\t* "+
            "\n\t* Function type: "+
            "\n\t*      vertical movement"+
            "\n\t* Parameters (by key):"+
            "\n\t*      minDepth         - Double: min preferred depth (m)"+
            "\n\t*      maxDepth         - Double: max preferred depth (m)"+
            "\n\t*      minDistOffBottom - Double: min distance off bottom (m)"+
            "\n\t*      minTemp          - Double: min preferred temperature (deg C)"+
            "\n\t*      maxTemp          - Double: max preferred temperature (deg C)"+
            "\n\t*      rpw              - Double - random walk parameter w/in preferred depth range (m/s)"+
            "\n\t*      tempTakesPrecedence - Boolean: flag indicating that the temperatue range takes precedence"+
            "\n\t* Variables:"+
            "\n\t*   double[] with elements:"+
            "\n\t*      dt     - [0] - integration time step (s)"+
            "\n\t*      depth  - [1] - current depth of individual, relative to mean sea level (m)"+
            "\n\t*      bathym - [2] - bathymetric  depth at location (m)"+
            "\n\t*      temp -   [3] - temperature at location"+
            "\n\t*      w      - [4] - active vertical swimming speed outside preferred depth range (m/s)"+
           "\n\t* Value:"+
            "\n\t*      Double: vertical swimming speed"+
            "\n\t* Calculation:"+
            "\n\t*      eps  = N(0,1) [random draw from a normal distribution)"+
            "\n\t*      v    = w*delta(outside preferred depth range) + sqrt(rpw/dt)*eps;"+
            "\n\t* "+
            "\n\t* author: William Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 7;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /* name associated with parameter minDepth */
    public static final String PARAM_minDepth = "min preferred depth (m)";
    /* the parameter's value */
    protected double minDepth;
    /* name associated with parameter maxDepth */
    public static final String PARAM_maxDepth = "max preferred depth (m)";
    /* the parameter's value */
    protected double maxDepth;
    /* name associated with parameter minDistOffBottom */
    public static final String PARAM_minDistOffBottom = "min distance off bottom (m)";
    /* the parameter's value */
    protected double minDistOffBottom = 0.0;
    /* name associated with parameter minTemp */
    public static final String PARAM_minTemp = "min preferred temp (deg C)";
    /* the parameter's value */
    protected double minTemp;
    /* name associated with parameter maxTemp */
    public static final String PARAM_maxTemp = "max preferred depth (deg C)";
    /* the parameter's value */
    protected double maxTemp;
    /** key to set random walk parameter */
    public static final String PARAM_rpw = "random walk parameter (m^2/s)";    
    /** value of random walk parameter */
    private double rpw = 0;
    /* name associated with parameter tempTakesPrecedence */
    public static final String PARAM_TempTakesPrecedence = "temp takes precedence";
    /* the parameter's value */
    protected boolean tempTakesPrecedence = true;
        
    /** constructor for class */
    public VerticalMovement_FixedDepthAndTempRange(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key;
        key = PARAM_minDepth;         addParameter(key, Double.class,key);
        key = PARAM_maxDepth;         addParameter(key, Double.class,key);
        key = PARAM_minDistOffBottom; addParameter(key, Double.class,key);
        key = PARAM_minTemp;          addParameter(key, Double.class,key);
        key = PARAM_maxTemp;          addParameter(key, Double.class,key);
        key = PARAM_rpw;              addParameter(key, Double.class,key);
        key = PARAM_TempTakesPrecedence; addParameter(key, Double.class,key);
    }
    
    @Override
    public VerticalMovement_FixedDepthAndTempRange clone(){
        VerticalMovement_FixedDepthAndTempRange clone = new VerticalMovement_FixedDepthAndTempRange();
        clone.setFunctionType(getFunctionType());
        clone.setFunctionName(getFunctionName());
        clone.setDescription(getDescription());
        clone.setFullDescription(getFullDescription());
        for (String key: getParameterNames()) clone.setParameterValue(key,getParameter(key).getValue());
//        for (String key: getSubfunctionNames()) clone.setSubfunction(key,(IBMFunctionInterface)getSubfunction(key).clone());
        return clone;
    }
    
    /**
     * Sets the parameter value corresponding to the key associated with param.
     * 
     * @param param - the parameter key (name)
     * @param value - its value
     * @return <code>true</code> if parameter was set successfully
     */
    @Override
    public boolean setParameterValue(String param,Object value){
        if (super.setParameterValue(param, value)){
            switch (param) {
                case PARAM_minDepth:
                    minDepth = ((Double) value);
                    break;
                case PARAM_maxDepth:
                    maxDepth = ((Double) value);
                    break;
                case PARAM_minDistOffBottom:
                    minDistOffBottom = ((Double) value);
                    break;
                case PARAM_minTemp:
                    minTemp = ((Double) value);
                    break;
                case PARAM_maxTemp:
                    maxTemp = ((Double) value);
                    break;
                case PARAM_TempTakesPrecedence:
                    tempTakesPrecedence = ((Boolean) value);
                    break;
                case PARAM_rpw:
                    rpw = ((Double) value);
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the input variables.
     * 
     * @param vars - the inputs variables as a double[] array with elements
     * <pre>
     *      dt      - [0] - integration time step (s)
     *      depth   - [1] - current depth of individual, relative to mean sea level (m)
     *      bathym  - [2] - bathymetric depth at location (m)
     *      temp    - [3] - temperature at location (m)
     *      w       - [4] - active vertical swimming speed outside preferred depth range (m/s)
     * </pre>
     * @return     - Double: individual active vertical movement velocity
     */
    @Override
    public Double calculate(Object vars) {
        double[] dbls = (double[]) vars;
        int k = 0;
        double adt               = Math.abs(dbls[k++]);//time step
        double depth             = dbls[k++];//current depth
        double bathym            = dbls[k++];//bathymetric depth
        double temp              = dbls[k++];//temperature
        double vertSwimmingSpeed = dbls[k++];//vertical swimming speed if outside preferred range

        //determine vertical movement & calc indiv. W
        double w = 0;
        double rDOB = bathym - minDistOffBottom;//realized depth corresponding to min distance off bottom
        double rMnD = Math.min(minDepth, rDOB); //realized min depth--stay min dist above bottom
        double rMxD = Math.min(maxDepth, rDOB); //realized max depth--stay min dist above bottom
        if (tempTakesPrecedence) {//staying in temp range takes priority
            if (temp<minTemp) {//too cold, swim up
                w =  vertSwimmingSpeed*(1.0-Math.exp(-(minTemp-temp)/10.0));
            } else
            if (temp>maxTemp) {//too warm, swim down
                w =  -vertSwimmingSpeed*(1.0-Math.exp(-(temp-maxTemp)/10.0));
            } else //temp range good but...
            if (depth<rMnD) {//too shallow, swim down
                w = -vertSwimmingSpeed*(1.0-Math.exp(-(rMnD-depth)/1.0));
            } else
            if (depth>rMxD) {//too deep, swim up
                w =  vertSwimmingSpeed*(1.0-Math.exp(-(depth-rMxD)/1.0));
            }
            //otherwise, just right
        } else {//staying in depth range takes priority
            if (depth<rMnD) {//too shallow, swim down
                w = -vertSwimmingSpeed*(1.0-Math.exp(-(rMnD-depth)/1.0));
            } else 
            if (depth>rMxD) {//too deep, swim up
                w =  vertSwimmingSpeed*(1.0-Math.exp(-(depth-rMxD)/1.0));
            } else //depth range good but...
            if (temp<minTemp) {//too cold, swim up
                w =  vertSwimmingSpeed*(1.0-Math.exp(-(minTemp-temp)/10.0));
            } else
            if (temp>maxTemp) {//too warm, swim down
                w =  -vertSwimmingSpeed*(1.0-Math.exp(-(maxTemp-temp)/10.0));
            } 
            //otherwise, just right
        }
        if ((rpw>0)&&(adt>0)) w += rng.computeNormalVariate()*Math.sqrt(rpw/adt);//add in ramdom walk
        return w;
    }
}
