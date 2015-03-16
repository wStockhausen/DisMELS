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
 * This class provides an implementation of an inverse power law mortality function.
 * Type: 
 *      mortality function
 * Parameters (by key):
 *      rate     - Double - mortality rate (1/day) at the standard covariate value
 *      stdVal   - Double - the standard covariate value
 *      exponent - Double - inverse power law exponent
 *      sigRate  - Double - std. deviation in random component of mortality
 * Variables:
 *      vars - Double - the covariate value (val)
 * Value:
 *      mortality rate - Double
 * Calculation:
 *      mortality_rate = Max[rate*(stdVal/val)^exponent+stdv*N(0,1)),0];
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class InversePowerLawMortalityRate extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "Mortality";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Inverse power law mortality rate";
    /** function description */
    public static final String DEFAULT_descr = "Inverse power law rate with additive random noise: m = m0*(z0/z)^a + stdv*N(0,1)";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of an inverse power law mortality function."+
            "\n\t* Type: mortality function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      rate     - Double - mortality rate (1/day) at the standard covariate value"+
            "\n\t*      stdVal   - Double - the standard covariate value"+
            "\n\t*      exponent - Double - inverse power law exponent"+
            "\n\t*      sigRate  - Double - std. deviation in random component of mortality"+
            "\n\t* Variables:"+
            "\n\t*      vars - Double - the covariate value (val)"+
            "\n\t* Value:"+
            "\n\t*      mortality rate - Double"+
            "\n\t* Calculation:"+
            "\n\t*      mortality_rate = Max[rate*(stdVal/val)^exponent+stdv*N(0,1)),0];"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 4;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set rate parameter */
    public static final String PARAM_rate = "standardized mortality rate (m0) [1/day]";
    /** key to set standard covariate value parameter */
    public static final String PARAM_stdVal = "covariate standard value (z0)";
    /** key to set exponent parameter */
    public static final String PARAM_exponent = "exponent (a)";
    /** key to set stdvRt parameter */
    public static final String PARAM_stdvRt = "std. dev. of rate (stdv)";
    
    /** value of rate parameter */
    private double rate = 0;
    /** value of standard covariate value parameter */
    private double stdVal = 0;
    /** value of exponent parameter */
    private double exponent = 0;
    /** value of stdvRt parameter */
    private double stdvRt = 0;
   
    /** constructor for class */
    public InversePowerLawMortalityRate(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_rate;    addParameter(key,Double.class, key);
        key = PARAM_stdVal;  addParameter(key,Double.class, key);
        key = PARAM_exponent;addParameter(key,Double.class, key);
        key = PARAM_stdvRt;  addParameter(key,Double.class, key);
    }
    
    @Override
    public InversePowerLawMortalityRate clone(){
        InversePowerLawMortalityRate clone = new InversePowerLawMortalityRate();
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
                    rate = ((Double) value).doubleValue();
                    break;
                case PARAM_stdVal:
                    stdVal = ((Double) value).doubleValue();
                    break;
                case PARAM_exponent:
                    exponent = ((Double) value).doubleValue();
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
     * @param vars - Double - the value of the covariate
     * @return     - Double - the corresponding mortality rate (per day) 
     */
    @Override
    public Double calculate(Object vars) {
        double val = (Double) vars;
        double res = rate*Math.pow(stdVal/val,exponent); 
        if (stdvRt>0) res += stdvRt*rng.computeNormalVariate();
        return new Double(Math.max(0.0,res));
    }
}
