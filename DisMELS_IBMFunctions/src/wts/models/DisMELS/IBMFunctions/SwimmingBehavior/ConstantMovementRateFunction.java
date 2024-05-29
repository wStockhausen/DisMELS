/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.SwimmingBehavior;

import com.wtstockhausen.utils.RandomNumberGenerator;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMMovementFunctionInterface;

/**
 * This class provides an implementation of a constant swimming speed.
 * Function type: 
 *      Swimming speed
 * Parameters (by key):
 *      rate - Double - mean swimming speed [m/s]
 *      rpw  - Double - random walk parameter [m^2/s]
 * Variables:
 *      double[]{dt} where
 *      dt   - time step [s]
 * Value:
 *      v - Double - swimming speed [m/s]
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMMovementFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class ConstantMovementRateFunction extends AbstractIBMFunction 
                                            implements IBMMovementFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "movement";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Constant movement rate";
    /** function description */
    public static final String DEFAULT_descr = "v = rate + sqrt(rpw/dt)*N(0,1) [m/s]";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a stochastic swimming speed."+
            "\n\t* Function type: "+
            "\n\t*      Swimming speed"+
            "\n\t* Parameters (by key):"+
            "\n\t*      rate - Double - mean swimming speed [m/s]"+
            "\n\t*      rpw  - Double - random walk parameter [m^2/s]"+
            "\n\t* Variables:"+
            "\n\t*      double[]{dt} "+
            "\n\t       dt   - time step [s]"+
            "\n\t* Value:"+
            "\n\t*      v - Double - swimming speed [m/s]"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of parameters */
    public static final int numParams = 2;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    
    /** key to set constant movement parameter */
    public static final String PARAM_rate = "rate (m/s)";
    /** key to set random walk parameter */
    public static final String PARAM_rpw = "random walk parameter (m^2/s)";
    
    /** value of movement rate parameter */
    private double rate = 0;
    /** value of random walk parameter */
    private double rpw = 0;
    
    public static final Logger logger = Logger.getLogger(ConstantMovementRateFunction.class.getName());
    
    /** constructor for class */
    public ConstantMovementRateFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key;
        key = PARAM_rate; addParameter(key, Double.class,key);
        key = PARAM_rpw;  addParameter(key, Double.class,key);
    }
    
    @Override
    public ConstantMovementRateFunction clone(){
        ConstantMovementRateFunction clone = new ConstantMovementRateFunction();
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
                case PARAM_rate:
                    rate    = ((Double) value);
                    break;
                case PARAM_rpw:
                    rpw    = ((Double) value);
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the value of the function.
     * 
     * @param vars - double[]{dt} [s].
     * @return     - movement rate as a Double [m/s]
     */
    @Override
    public Double calculate(Object vars) {
        if (rpw<=0) return rate;
        double dt = ((double[]) vars)[0];
        double rnd = 0.0; 
        if (dt>0) rnd = Math.sqrt(rpw/Math.abs(dt))*rng.computeNormalVariate();
//        logger.info("----RNG : "+rng.toString()+"----seed: "+rng.getSeed());
//        logger.info("------value: "+rnd);
        return rate+rnd;
    }
}
