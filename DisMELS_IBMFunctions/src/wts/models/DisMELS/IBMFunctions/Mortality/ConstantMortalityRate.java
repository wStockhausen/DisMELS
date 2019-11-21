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
 * This class provides an implementation of a constant mortality function.
 * Type: 
 *      mortality function
 * Parameters (by key):
 *      rate    - Double - mortality rate (1/day)
 *      sigRate - Double - std. deviation in random component of mortality
 * Variables:
 *      vars - null
 * Value:
 *      mortality rate - Double
 * Calculation:
 *      mortality_rate = Max[mean_rate+stdv*N(0,1)),0];
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class ConstantMortalityRate extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "Mortality";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Constant mortality rate";
    /** function description */
    public static final String DEFAULT_descr = "Constant mortality rate with additive random noise";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a constant mortality rate function."+
            "\n\t* Type: "+
            "\n\t*      mortality function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      rate    - Double - mortality rate (1/day)"+
            "\n\t*      sigRate - Double - std. deviation in random component of mortality"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[]{} or null"+
            "\n\t* Value:"+
            "\n\t*      mortality rate"+
            "\n\t* Calculation:"+
            "\n\t*      mortality_rate = Max[mean_rate+stdv*N(0,1)),0];"+
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
    public static final String PARAM_rate = "mean mortality rate [1/day]";
    /** key to set stdvRt parameter */
    public static final String PARAM_stdvRt = "std. dev. of rate";
    
    /** value of rate parameter */
    private double rate = 0;
    /** value of stdvRt parameter */
    private double stdvRt = 0;
   
    /** constructor for class */
    public ConstantMortalityRate(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_rate;  addParameter(key,Double.class, key);
        key = PARAM_stdvRt;addParameter(key,Double.class, key);
    }
    
    @Override
    public ConstantMortalityRate clone(){
        ConstantMortalityRate clone = new ConstantMortalityRate();
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
     * @param vars - null
     * @return     - Double - the corresponding mortality rate (per day) 
     */
    @Override
    public Double calculate(Object vars) {
        double res = rate; 
        if (stdvRt>0) res += stdvRt*rng.computeNormalVariate();
        return new Double(Math.max(0.0,res));//must be >= 0 
    }
}
