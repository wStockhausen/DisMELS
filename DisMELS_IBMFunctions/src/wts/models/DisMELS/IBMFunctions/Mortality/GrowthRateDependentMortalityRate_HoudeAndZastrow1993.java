/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Mortality;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;

/**
 * This class provides an implementation of a weight-specific growth rate-dependent mortality rate function
 * for feeding-stage larvae from Houde & Zastrow (1993).
 * Type: 
 *      mortality function
 * Parameters (by key):
 *      alpha - Double - mortality rate at T=0 (1/day).          Default value = -0.0131
 *      beta  - Double - linear coefficient in weight-specifc G. Default value =  1.2170
 *      sigRate - Double - std. deviation in random component of mortality
 * Variables:
 *      vars - Double - temperature (deg C)
 * Value:
 *      mortality rate - Double
 * Calculation:
 *      mortality_rate = Max[alpha+beta*G + stdv*N(0,1)),0];
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class GrowthRateDependentMortalityRate_HoudeAndZastrow1993 extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "Mortality";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Growth rate-dependent mortality rate (Houde and Zastrow, 1993)";
    /** function description */
    public static final String DEFAULT_descr = "Growth rate-dependent mortality rate with additive random noise from Houde and Zastrow (1993)";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a weight-specific growth rate-dependent mortality rate function"+
            "\n\t* for feeding-stage larvae from Houde & Zastrow (1993)."+
            "\n\t* Type: "+
            "\n\t*      mortality function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      alpha - Double - intercept.                              Default value = -0.0131"+
            "\n\t*      beta  - Double - linear coefficient in weight-specifc G. Default value =  1.2170"+
            "\n\t*      sigRate - Double - std. deviation in random component of mortality"+
            "\n\t* Variables:"+
            "\n\t*      vars - Double - temperature (deg C)"+
            "\n\t* Value:"+
            "\n\t*      mortality rate - Double"+
            "\n\t* Calculation:"+
            "\n\t*      mortality_rate = Max[alpha+beta*G + stdv*N(0,1)),0];"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 3;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set rate parameter */
    public static final String PARAM_alpha = "intercept";
    /** key to set rate parameter */
    public static final String PARAM_beta = "linear coefficient in G";
    /** key to set stdvRt parameter */
    public static final String PARAM_stdvRt = "std. dev. of rate";
    
    /** value of constant coefficient parameter */
    private double alpha = -0.0131;
    /** value of linear coefficient parameter */
    private double beta = 1.2170;
    /** value of stdvRt parameter */
    private double stdvRt = 0;
   
    /** constructor for class */
    public GrowthRateDependentMortalityRate_HoudeAndZastrow1993(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_alpha; addParameter(key,Double.class, key);
        key = PARAM_beta;  addParameter(key,Double.class, key);
        key = PARAM_stdvRt;addParameter(key,Double.class, key);
    }
    
    @Override
    public GrowthRateDependentMortalityRate_HoudeAndZastrow1993 clone(){
        GrowthRateDependentMortalityRate_HoudeAndZastrow1993 clone = new GrowthRateDependentMortalityRate_HoudeAndZastrow1993();
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
                case PARAM_alpha:
                    alpha    = ((Double) value).doubleValue();
                    break;
                case PARAM_beta:
                    beta    = ((Double) value).doubleValue();
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
     * @param vars - Double - temperature (deg C)
     * @return     - Double - the corresponding mortality rate (per day) 
     */
    @Override
    public Double calculate(Object vars) {
        double T = (Double) vars;
        double res = alpha+beta*T; 
        if (stdvRt>0) res += stdvRt*rng.computeNormalVariate();
        return new Double(Math.max(0.0,res));//must be >= 0 
    }
}
