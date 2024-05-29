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
 * This function implements Folkvord's (2005) temperature and weight-dependent
 * growth model. 
 * Type: 
 *      Individual growth function
 * Parameters (by key):
 *      a0 - Double - base growth coefficient
 *      b0 - Double - temperature coefficient
 *      a1 - Double - ln(dw)^1 growth coefficient
 *      b1 - Double - ln(dw)^1 temperature coefficient
 *      a2 - Double - ln(dw)^2 base growth coefficient
 *      b2 - Double - ln(dw)^2 temperature coefficient
 *      a3 - Double - ln(dw)^3 base growth coefficient
 *      b3 - Double - ln(dw)^3 temperature coefficient
 *      sigRate - Double - std. deviation in random component to growth ([size]/[time])
 * Variables:
 *      vars - double[]{dt,dw0,T}.
 *      dt - time interval   ([time])
 *      dw - dry weight at time t0 
 *      T  - temperature
 * Value:
 *      dw(dt) - Double - dry weight at time t+dt
 * Calculation:
 * 
 * TODO: consider specifying cv, not sigRate.
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class Folkvord2005GrowthFunction extends AbstractIBMFunction 
                                        implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Individual growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Folkvord's (2005) growth model";
    /** function description */
    public static final String DEFAULT_descr = "Folkvord's (2005) temperature-dependent growth model";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 9;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set a0 parameter */
    public static final String PARAM_a0 = "a0";
    /** key to set b0 parameter */
    public static final String PARAM_b0 = "b0";
    /** key to set a1 parameter */
    public static final String PARAM_a1 = "a1";
    /** key to set b1 parameter */
    public static final String PARAM_b1 = "b1";
    /** key to set a2 parameter */
    public static final String PARAM_a2 = "a2";
    /** value of a2 parameter */
    /** key to set b2 parameter */
    public static final String PARAM_b2 = "b2";
    /** key to set a3 parameter */
    public static final String PARAM_a3 = "a3";
    /** key to set b3 parameter */
    public static final String PARAM_b3 = "b3";
    /** key to set sigRate parameter */
    public static final String PARAM_stdvRt = "std. dev.";
    
    /** value of a0 parameter */
    private double a0 = 0;
    /** value of b0 parameter */
    private double b0 = 0;
    
    /** value of a1 parameter */
    private double a1 = 0;
    /** value of b1 parameter */
    private double b1 = 0;
    
    private double a2 = 0;
    /** value of b2 parameter */
    private double b2 = 0;
    
    /** value of a3 parameter */
    private double a3 = 0;
    /** value of b3 parameter */
    private double b3 = 0;
    
    /** value of sigRate parameter */
    private double stdvRt = 0;
    
    /** constructor for class */
    public Folkvord2005GrowthFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        //base coefficints
        key = PARAM_a0;  addParameter(key,Double.class, "base growth rate coefficient");
        key = PARAM_b0;  addParameter(key,Double.class, "base growth rate temperature coefficient");
        //ln[dw)]^1 coefficints
        key = PARAM_a1;  addParameter(key,Double.class, "ln[dw)] growth rate coefficient");
        key = PARAM_b1;  addParameter(key,Double.class, "ln[dw)] growth rate temperature coefficient");
        //ln[dw)]^2 coefficints
        key = PARAM_a2;  addParameter(key,Double.class, "ln[dw)]^2 growth rate coefficient");
        key = PARAM_b2;  addParameter(key,Double.class, "ln[dw)]^2 growth rate temperature coefficient");
        //ln[dw)]^3 coefficints
        key = PARAM_a3;  addParameter(key,Double.class, "ln[dw)]^3 growth rate coefficient");
        key = PARAM_b3;  addParameter(key,Double.class, "ln[dw)]^3 growth rate temperature coefficient");
        
        key = PARAM_stdvRt;  addParameter(key,Double.class, "std. dev. in growth rate");
    }
    
    @Override
    public Folkvord2005GrowthFunction clone(){
        Folkvord2005GrowthFunction clone = new Folkvord2005GrowthFunction();
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
                case PARAM_a0:
                    a0    = ((Double) value).doubleValue();
                    break;
                case PARAM_b0:
                    b0    = ((Double) value).doubleValue();
                    break;
                case PARAM_a1:
                    a1    = ((Double) value).doubleValue();
                    break;
                case PARAM_b1:
                    b1    = ((Double) value).doubleValue();
                    break;
                case PARAM_a2:
                    a2    = ((Double) value).doubleValue();
                    break;
                case PARAM_b2:
                    b2    = ((Double) value).doubleValue();
                    break;
                case PARAM_a3:
                    a3    = ((Double) value).doubleValue();
                    break;
                case PARAM_b3:
                    b3    = ((Double) value).doubleValue();
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
     * @param vars - the inputs variables, as a double[]:
     *      dt  - time step
     *      dw0 - initial dry weight
     *      T   - temperature (deg C)
     * @return     - the function value (dw[dt]) as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double dt  = lvars[i++];
        double dw0 = lvars[i++];
        double T   = lvars[i++];
        double c0v = a0+b0*T;
        double c1v = a1+b1*T;
        double c2v = a2+b2*T;
        double c3v = a3+b3*T;
        double lndw = Math.log(dw0);
        double sgr = c0v+c1v*lndw+c2v*Math.pow(lndw,2)+c3v*Math.pow(lndw,3);
        double g = Math.log(1+sgr/100.0);
        if (stdvRt>0) g += rng.computeNormalVariate()*stdvRt;//TODO: use cv rather than sigRate?
        Double res = new Double(dw0*Math.exp(g*dt));//assumes g*dt<<1
        return res;
    }
}
