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
 * This class provides an implementation of vertical movement within a
 * fixed, "preferred", range of depths described in distance off the bottom.  
 * 
 * When inside the preferred depth range, vertical movement is described as an 
 * uncorrelated random walk.  When outside the preferred range, a vertical 
 * swimming speed (externally calculated) is applied in the direction that 
 * would move the individual toward the preferred depth range. 
 * 
 * Function type: 
 *      vertical movement
 * Parameters (by key):
 *      maxDepth         - Double: max depth (m)
 *      minDistOffBottom - Double: min distance off bottom (m)
 *      maxDistOffBottom - Double: max distance off bottom (m)
 *      rpw              - Double: random walk parameter w/in preferred depth range ([distance]^2/[time])
 * Variables:
 *      dt          - [0] - integration time step
 *      depth       - [1] - current depth of individual (m)
 *      total depth - [2] - total depth at location     (m)
 *      w           - [3] - active vertical swimming speed outside preferred depth range
 * Value:
 *      Double: vertical swimming speed (same units as w)
 * Calculation:
 *      eps  = N(0,sigV) [random draw from a normal distribution)
 *      v    = w*delta(outside preferred depth range) + sqrt(rpw/dt)*eps;
 * 
 * @author William Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMMovementFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class VerticalMovement_FixedOffBottomRange extends AbstractIBMFunction 
                                            implements IBMMovementFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Swimming speed";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Vertical movement";
    /** function description */
    public static final String DEFAULT_descr = "Vertical movement within a (fixed) preferred off-bottom range";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of vertical movement relative to a"+
            "\n\t* fixed, 'preferred', range of depths described in distance off the bottom.  "+
            "\n\t* When inside the preferred range, vertical movement is described as an uncorrelated"+
            "\n\t* random walk.  When outside the preferred range,a vertical swimming speed"+
            "\n\t* (externally calculated) is applied in the direction that would move the individual"+
            "\n\t*  toward the preferred depth range."+
            "\n\t* "+
            "\n\t* Function type: "+
            "\n\t*      vertical movement"+
            "\n\t* Parameters (by key):"+
            "\n\t*      maxDepth         - Double:"+
            "\n\t*      minDistOffBottom - Double:"+
            "\n\t*      maxDistOffBottom - Double:"+
            "\n\t*      rpw              - Double - random walk parameter w/in preferred depth range ([distance]^2/[time])"+
            "\n\t* Variables:"+
            "\n\t*      dt          - [0] - integration time step"+
            "\n\t*      depth       - [1] - current depth of individual"+
            "\n\t*      total depth - [2] - total depth at location"+
            "\n\t*      w           - [3] - active vertical swimming speed outside preferred depth range"+
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
    public static final int numParams = 4;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /* name associated with parameter maxDepth */
    public static final String PARAM_maxDepth = "max depth (m)";
    /* the parameter's value */
    protected double maxDepth;
    /* name associated with parameter minDistOffBottom */
    public static final String PARAM_minDistOffBottom = "min depth (m)";
    /* the minDistOffBottom parameter's value */
    protected double minDistOffBottom;
    /* name associated with parameter maxDepth */
    public static final String PARAM_maxDistOffBottom = "off-bottom depth (m)";
    /* the parameter's value */
    protected double maxDistOffBottom = 0.0;
    /** key to set random walk parameter */
    public static final String PARAM_rpw = "random walk parameter (m^2/s)";    
    /** value of random walk parameter */
    private double rpw = 0;
        
    /** constructor for class */
    public VerticalMovement_FixedOffBottomRange(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key;
        key = PARAM_maxDepth;         addParameter(key, Double.class,key);
        key = PARAM_minDistOffBottom; addParameter(key, Double.class,key);
        key = PARAM_maxDistOffBottom; addParameter(key, Double.class,key);
        key = PARAM_rpw;              addParameter(key, Double.class,key);
    }
    
    @Override
    public VerticalMovement_FixedOffBottomRange clone(){
        VerticalMovement_FixedOffBottomRange clone = new VerticalMovement_FixedOffBottomRange();
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
                case PARAM_maxDepth:
                    maxDepth = ((Double) value).doubleValue();
                    break;
                case PARAM_minDistOffBottom:
                    minDistOffBottom = ((Double) value).doubleValue();
                    break;
                case PARAM_maxDistOffBottom:
                    maxDistOffBottom = ((Double) value).doubleValue();
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
     * @return     - Double: individual active vertical movement velocity
     */
    @Override
    public Double calculate(Object vars) {
        double[] dbls = (double[]) vars;
        int k = 0;
        double adt               = Math.abs(dbls[k++]);//time step
        double depth             = dbls[k++];//current depth
        double totalDepth        = dbls[k++];//total depth
        double vertSwimmingSpeed = dbls[k++];//vertical swimming speed if outside preferred range

        //determine vertical movement & calc indiv. W
        double w = 0;
        if (depth<totalDepth-maxDistOffBottom) {
            w = -vertSwimmingSpeed*(1.0-Math.exp(-((totalDepth-maxDistOffBottom)-depth)/10.0));
        } else 
        if ((depth>maxDepth)||(depth>(totalDepth-minDistOffBottom))) {
            w =  vertSwimmingSpeed*(1.0-Math.exp(-(Math.min(depth-totalDepth, depth-(totalDepth-minDistOffBottom)))/10.0));
        }
        if ((rpw>0)&&(adt>0)) w += rng.computeNormalVariate()*Math.sqrt(rpw/adt);//add in ramdom walk
        return w;
    }
}
