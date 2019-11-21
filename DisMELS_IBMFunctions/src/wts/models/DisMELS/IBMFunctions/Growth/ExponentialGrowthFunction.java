/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Growth;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * This class provides an implementation of an individual growth function.
 * Type: 
 *      Individual growth function
 * Parameters (by key):
 *      rate    - Double - linear growth rate ([size]/[time])
 *      sigRate - Double - std. deviation in random component to growth ([size]/[time])
 * Variables:
 *      vars - double[]{dt,z0}.
 *      dt - double - time interval   ([time])
 *      z0 - double - size at time t0 ([size])
 * Value:
 *      z(dt) - Double - size at time t+dt
 * Calculation:
 *      eps   = N(0,sigRate) [random draw from a normal distribution)
 *      z(dt) = z0 + dt*(rate+eps);
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class ExponentialGrowthFunction extends AbstractIBMFunction implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Individual growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Exponential growth";
    /** function description */
    public static final String DEFAULT_descr = "Exponential growth with additive random noise";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of an exponential growth function."+
            "\n\t* Type: "+
            "\n\t*      Individual growth function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      rate    - Double - intrinsic growth rate (1/[time])"+
            "\n\t*      sigRate - Double - std. deviation in random component to growth (1/[time])"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[]{dt,z0}."+
            "\n\t*      dt - double - time interval   ([time])"+
            "\n\t*      z0 - double - size at time t0 ([size])"+
            "\n\t* Value:"+
            "\n\t*      z(dt) - Double - size at time t+dt"+
            "\n\t* Calculation:"+
            "\n\t*      eps   = N(0,sigRate) [random draw from a normal distribution)"+
            "\n\t*      z(dt) = z0*exp[dt*(rate+eps)]"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 2;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set rate parameter */
    public static final String PARAM_rate = "rate";
    /** key to set stdvRt parameter */
    public static final String PARAM_stdvRt = "std. dev.";
    
    /** value of rate parameter */
    private double rate = 0;
    /** value of stdvRt parameter */
    private double stdvRt = 0;
   
    /** constructor for class */
    public ExponentialGrowthFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_rate;   addParameter(key,Double.class, "exponential growth rate");
        key = PARAM_stdvRt; addParameter(key,Double.class, "std. dev. in growth rate");
    }
    
    @Override
    public ExponentialGrowthFunction clone(){
        ExponentialGrowthFunction clone = new ExponentialGrowthFunction();
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
                    rate    = ((Double) value).doubleValue();
                    break;
                case PARAM_stdvRt:
                    stdvRt = ((Double) value).doubleValue();
                    break;
            }
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter params 
     * and the input variable.
     * 
     * @param vars - the inputs variables, dt and z0, as a double[].
     * @return     - the function value (z[dt]) as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double dt = lvars[i++];
        double z0 = lvars[i++];
        double rnd = 0; 
        if (stdvRt>0) rnd = rng.computeNormalVariate(); 
        Double res = new Double(z0*Math.exp((rate+rnd*stdvRt)*dt));//TODO: use cv rather than sigRate?
        return res;
    }
}
