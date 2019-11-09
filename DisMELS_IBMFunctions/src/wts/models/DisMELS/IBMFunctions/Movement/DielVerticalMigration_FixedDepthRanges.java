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
 * This class provides an implementation of diel vertical migration between
 * fixed "preferred" depth ranges.  When inside the time-specific preferred
 * depth range, vertical movement describes an uncorrelated random walk.  When outside
 * the preferred range, a vertical swimming speed (externally calculated) is applied
 * in the direction that would move the individual toward the preferred depth range.
 * 
 * Function type: 
 *      vertical movement
 * Parameters (by key):
 *      hasPreferredDepthRangeDay - boolean
 *      willAttachDay - boolean
 *      minDepthDay - Double
 *      maxDepthDay - Double
 *      hasPreferredDepthRangeNight - boolean
 *      willAttachNight - boolean
 *      minDepthNight - Double
 *      maxDepthNight - Double
 *      rpw - Double - random walk parameter w/in preferred depth range ([distance]^2/[time])
 * Variables:
 *      dt          - [0] - integration time step
 *      depth       - [1] - current depth of individual
 *      total depth - [2] - total depth at location
 *      w           - [3] - active vertical swimming speed outside preferred depth range
 *      lightLevel  - [4] - value >= 0 indicates daytime, otherwise night 
 * Value:
 *      double[] where
 *      [1]: vertical swimming speed
 *      [2]: flag indicating whether individual is attached to the bottom (value>0) and cannot be advected horizontally.
 * Calculation:
 *      eps  = N(0,sigV) [random draw from a normal distribution)
 *      v    = alpha*(z^beta)*exp(eps);
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMMovementFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class DielVerticalMigration_FixedDepthRanges extends AbstractIBMFunction 
                                            implements IBMMovementFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Vertical movement";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Diel vertical migration between (fixed) preferred depth ranges";
    /** function description */
    public static final String DEFAULT_descr = "Diel vertical migration between (fixed) preferred depth ranges";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of diel vertical migration between"+
            "\n\t* fixed 'preferred' depth ranges.  When inside the time-specific preferred"+
            "\n\t* depth range, vertical movement describes an uncorrelated random walk.  When outside"+
            "\n\t* the preferred range, a vertical swimming speed (externally calculated) is applied"+
            "\n\t* in the direction that would move the individual toward the preferred depth range."+
            "\n\t* "+
            "\n\t* Function type: "+
            "\n\t*      vertical movement"+
            "\n\t* Parameters (by key):"+
            "\n\t*      hasPreferredDepthRangeDay - boolean"+
            "\n\t*      willAttachDay - boolean"+
            "\n\t*      minDepthDay   - Double"+
            "\n\t*      maxDepthDay   - Double"+
            "\n\t*      hasPreferredDepthRangeNight - boolean"+
            "\n\t*      willAttachNight - boolean"+
            "\n\t*      minDepthNight   - Double"+
            "\n\t*      maxDepthNight   - Double"+
            "\n\t*      rpw - Double - random walk parameter w/in preferred depth range ([distance]^2/[time])"+
            "\n\t* Variables:"+
            "\n\t*      dt          - [0] - integration time step"+
            "\n\t*      depth       - [1] - current depth of individual"+
            "\n\t*      total depth - [2] - total depth at location"+
            "\n\t*      w           - [3] - active vertical swimming speed outside preferred depth range"+
            "\n\t*      lightLevel  - [4] - value >= 0 indicates daytime, otherwise night "+
            "\n\t* Value:"+
            "\n\t*      double[] where"+
            "\n\t*      [1]: vertical swimming speed"+
            "\n\t*      [2]: flag indicating whether individual is attached to the bottom (value>0) and cannot be advected horizontally."+
            "\n\t* Calculation:"+
            "\n\t*      eps  = N(0,1) [random draw from a normal distribution)"+
            "\n\t*      v    = w*delta(outside preferred depth range) + sqrt(rpw/dt)*eps;"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 9;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /* name associated with parameter hasPreferredDepthRangeDay */
    public static final String PARAM_hasPreferredDepthRangeDay = "has preferred daytime depth range?";
    /* the parameter's value */
    protected boolean hasPreferredDepthRangeDay;
    /* name associated with parameter willAttachDay */
    public static final String PARAM_willAttachDay = "will attach to bottom (day)";
    /* the parameter's value */
    protected boolean willAttachDay;
    /* name associated with parameter minDepthDay */
    public static final String PARAM_minDepthDay = "min daytime depth (m)";
    /* the parameter's value */
    protected double minDepthDay;
    /* name associated with parameter maxDepthDay */
    public static final String PARAM_maxDepthDay = "max daytime depth (m)";
    /* the parameter's value */
    protected double maxDepthDay;
    
    /* name associated with parameter hasPreferredDepthRangeNight */
    public static final String PARAM_hasPreferredDepthRangeNight = "has preferred nighttime depth range?";
    /* the parameter's value */
    protected boolean hasPreferredDepthRangeNight;
    /* name associated with parameter willAttachNight */
    public static final String PARAM_willAttachNight        = "will attach to bottom (night)";
    /* the parameter's value */
    protected boolean willAttachNight;
    /* name associated with parameter minDepthNight */
    public static final String PARAM_minDepthNight          = "min nighthtime depth (m)";
    /* the parameter's value */
    protected double minDepthNight;
    /* name associated with parameter maxDepthNight */
    public static final String PARAM_maxDepthNight          = "max nighttime depth (m)";
    /* the parameter's value */
    protected double maxDepthNight;
    
    /** key to set random walk parameter */
    public static final String PARAM_rpw = "random walk parameter (m^2/s)";    
    /** value of random walk parameter */
    private double rpw = 0;
        
    /** constructor for class */
    public DielVerticalMigration_FixedDepthRanges(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key;
        key = PARAM_hasPreferredDepthRangeDay;  addParameter(key, Boolean.class,key);
        key = PARAM_willAttachDay;              addParameter(key, Boolean.class,key);
        key = PARAM_minDepthDay;                addParameter(key, Double.class,key);
        key = PARAM_maxDepthDay;                addParameter(key, Double.class,key);
        key = PARAM_hasPreferredDepthRangeNight;addParameter(key, Boolean.class,key);
        key = PARAM_willAttachNight;            addParameter(key, Boolean.class,key);
        key = PARAM_minDepthNight;              addParameter(key, Double.class,key);
        key = PARAM_maxDepthNight;              addParameter(key, Double.class,key);
        key = PARAM_rpw;                        addParameter(key, Double.class,key);
    }
    
    @Override
    public DielVerticalMigration_FixedDepthRanges clone(){
        DielVerticalMigration_FixedDepthRanges clone = new DielVerticalMigration_FixedDepthRanges();
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
     * @return 
     */
    @Override
    public boolean setParameterValue(String param,Object value){
        if (super.setParameterValue(param, value)){
            switch (param) {
                case PARAM_hasPreferredDepthRangeDay:
                    hasPreferredDepthRangeDay = ((Boolean) value).booleanValue();
                    break;
                case PARAM_willAttachDay:
                    willAttachDay = ((Boolean) value).booleanValue();
                    break;
                case PARAM_minDepthDay:
                    minDepthDay = ((Double) value).doubleValue();
                    break;
                case PARAM_maxDepthDay:
                    maxDepthDay = ((Double) value).doubleValue();
                    break;
                case PARAM_hasPreferredDepthRangeNight:
                    hasPreferredDepthRangeNight = ((Boolean) value).booleanValue();
                    break;
                case PARAM_willAttachNight:
                    willAttachNight = ((Boolean) value).booleanValue();
                    break;
                case PARAM_minDepthNight:
                    minDepthNight = ((Double) value).doubleValue();
                    break;
                case PARAM_maxDepthNight:
                    maxDepthNight = ((Double) value).doubleValue();
                    break;
                case PARAM_rpw:
                    rpw = ((Double) value).doubleValue();
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter params 
     * and the input variable.
     * 
     * @param vars - the inputs variables as a double[] array with elements
     *                  dt          - [0] - integration time step
     *                  depth       - [1] - current depth of individual
     *                  total depth - [2] - total depth at location
     *                  w           - [3] - active vertical swimming speed outside preferred depth range
     *                  lightLevel  - [4] - value >= 0 indicates daytime, otherwise night 
     * @return     - double[] with elements
     *              w        - individual active vertical movement velocity
     *              attached - flag indicating whether individual is attached to bottom(< 0) or not (>0)
     */
    @Override
    public double[] calculate(Object vars) {
        double[] res = new double[2];
        double[] dbls = (double[]) vars;
        int k = 0;
        double adt               = Math.abs(dbls[k++]);//time step
        double depth             = dbls[k++];//current depth
        double totalDepth        = dbls[k++];//total depth
        double vertSwimmingSpeed = dbls[k++];//vertical swimming speed if outside preferred range
        boolean isDaytime        = (dbls[k++]>0);//flag indicating daytime (true) or night (false)

        //determine daytime/nighttime for vertical migration & calc indiv. W
        double w = 0;
        double attached = 1.0;
        if (isDaytime&&willAttachDay&&(depth>(totalDepth-1))) {
            //set indiv on bottom and don't let it move
            attached = -1.0;
        } else 
        if (!isDaytime&&willAttachNight&&(depth>(totalDepth-1))) {
            //set indiv on bottom and don't let it move
            attached = -1.0;
        } else {
            //System.out.println("Depth before predictor step = "+depth);
            //calculate deterministic swimming rate for current position.
            //note that the sign is reversed if timestep (dt) is < 0
            //to maintain correct depth range behavior when running model backwards
            if (isDaytime) {
                if (depth<minDepthDay) {
                    w = -vertSwimmingSpeed*(1.0-Math.exp(-(minDepthDay-depth)/10.0));
                } else 
                if (depth>maxDepthDay) {
                    w =  vertSwimmingSpeed*(1.0-Math.exp(-(depth-maxDepthDay)/10.0));
                }
    //            str = id+" Daytime "+depth+" "+minDepthDay+" "+maxDepthDay+" "+w;
            } else {
                if (depth<minDepthNight) {
                    w = -vertSwimmingSpeed*(1.0-Math.exp(-(minDepthNight-depth)/10.0));
                } else 
                if (depth>maxDepthNight) {
                    w =  vertSwimmingSpeed*(1-Math.exp(-(depth-maxDepthNight)/10.0));
                } 
    //            str = id+" Nighttime "+depth+" "+minDepthNight+" "+maxDepthNight+" "+w;
            }
            //System.out.print(str);
            if ((rpw>0)&&(adt>0)) w += rng.computeNormalVariate()*Math.sqrt(rpw/adt);//add in ramdom walk
        }
        res[0] = w;
        res[1] = attached;
        return res;
    }
}
