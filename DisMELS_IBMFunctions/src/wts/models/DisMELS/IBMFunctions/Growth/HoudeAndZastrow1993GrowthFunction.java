/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Growth;

import com.wtstockhausen.utils.RandomNumberGenerator;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * This function implements Houde and Zastrow (1993) temperature and weight-dependent
 * growth model.
 * Type: 
 *      Individual growth function
 * Parameters (by key):
 *      a0   - Double - base growth coefficient (nominal value: -0.0203)
 *      b0   - Double - temperature coefficient (nominal value:  0.0106)
 *      maxT - Double - max temperature for growth
 *      sigRate - Double - std. deviation in random component to growth ([size]/[time])
 * Variables:
 *      vars: double[]{dt,w0,T}.
 *      dt - time interval   ([time])
 *      w0 - dry weight at time t0 
 *      T  - temperature
 * Value:
 *      w(dt) - Double - weight at time t+dt
 * Calculation:
 *      g = a0 + b0*T
 *      w(dt) = w(0)*exp(g*dt) if T<=maxT
 * TODO: consider specifying cv, not sigRate.
 * 
 * @author William.Stockhausen
 * Citation:
 * Houde, E.D. and C.E. Zastrow. 1993. Ecosystem- and taxon-specific dynamics and energetic properties 
 * of larval fish assemblages. Bull. Mar. Sci. 53: 290-335. 
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class HoudeAndZastrow1993GrowthFunction extends AbstractIBMFunction 
                                        implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Individual growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Houde and Zastrow's (1993) growth model";
    /** function description */
    public static final String DEFAULT_descr = "Houde and Zastrow's (1993) temperature-dependent growth model";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 4;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set a0 parameter */
    public static final String PARAM_a0 = "a0";
    /** key to set b0 parameter */
    public static final String PARAM_b0 = "b0";
    /** key to set maxT parameter */
    public static final String PARAM_maxT = "max temperature";
    /** key to set sigRate parameter */
    public static final String PARAM_stdvRate = "std. dev.";
    
    /** value of a0 parameter */
    private double a0 = 0;
    /** value of b0 parameter */
    private double b0 = 0;
    /** value of maxT parameter */
    private double maxT = 0;
    /** value of sigRate parameter */
    private double stdvRate = 0;
    
    /** the set of main parameter names */
    protected static final Set<String> setOfParameterNames = new LinkedHashSet<>(8);
    /** the set of subfunction names (empty here) */
    protected static final Set<String> setOfSubfunctionNames = new LinkedHashSet<>(1);
    
    /** constructor for class */
    public HoudeAndZastrow1993GrowthFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_a0;       addParameter(key,Double.class, "base growth rate coefficient");
        key = PARAM_b0;       addParameter(key,Double.class, "base growth rate temperature coefficient");        
        key = PARAM_maxT;     addParameter(key,Double.class, "max temperature (deg C)");
        key = PARAM_stdvRate; addParameter(key,Double.class, "std. dev. in growth rate");
        
        setParameterValue(PARAM_a0, new Double(-0.0203));//from Houde and Zastrow (1993)
        setParameterValue(PARAM_b0, new Double(0.0106));//from Houde and Zastrow (1993)
    }
    
    @Override
    public HoudeAndZastrow1993GrowthFunction clone(){
        HoudeAndZastrow1993GrowthFunction clone = new HoudeAndZastrow1993GrowthFunction();
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
                case PARAM_maxT:
                    maxT    = ((Double) value).doubleValue();
                    break;
                case PARAM_stdvRate:
                    stdvRate = ((Double) value).doubleValue();
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
     *      dt - time step
     *      w0 - initial weight
     *      T  - temperature (deg C)
     * @return  - List with elements
     *      w(dt)  - as Double, valid if T< maxT
     *  (T > maxT) - as Boolean (individual is dead if true).
     */
    @Override
    public List<Object> calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double dt  = lvars[i++];
        double w0  = lvars[i++];
        double T   = lvars[i++];
        double g   = a0+b0*T;
        if (stdvRate>0) g += rng.computeNormalVariate()*stdvRate;//TODO: use cv rather than sigRate?
        ArrayList res = new ArrayList(2);
        res.add(new Double(w0*Math.exp(g*dt)));//assumes g*dt<<1
        res.add((T>maxT));
        return res;
    }
}
