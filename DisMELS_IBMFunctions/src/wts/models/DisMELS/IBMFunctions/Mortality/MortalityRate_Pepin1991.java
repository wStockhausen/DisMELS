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
 * This class provides an implementation of Pepin's (1991) growth rate- and size-specifc mortality function.
 * Type: 
 *      mortality function
 * Parameters (by key):
 *      rate     - Double - mortality rate constant (1/day) at the standard growth rate and size
 *      stdG     - Double - the standard grwoth rate
 *      stdL     - Double - the standard size
 *      exponent - Double - the exponent
 *      stdvRate - Double - std. deviation in random component of mortality
 * Variables:
 *      vars - double[] - {G, L}, current size-specifc growth rate and size 
 * Value:
 *      mortality rate - Double
 * Calculation:
 *      mortality_rate = Max{rate*[(G/stdG)^(1-exponent)]/[(L/stdL)^(1+exponent)]+stdvRate*N(0,1)),0];
 * 
 * @author William.Stockhausen
 * Citation:
 * Pepin, P. 1991. Effect of temperature and size on development, mortality, and survival rates 
 * of the pelagic early life history stages of marine fish. CJFAS. 48:503-518.
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class MortalityRate_Pepin1991 extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "Mortality";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Pepin's (1991) mortality rate";
    /** function description */
    public static final String DEFAULT_descr = "Pepin's (1991) size- and growth rate-specific mortality rate";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of Pepin's (1991) growth rate- and size-specifc mortality function."+
            "\n\t* Type: "+
            "\n\t*      mortality function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      rate     - Double - mortality rate constant (1/day) at the standard growth rate and size"+
            "\n\t*      stdG     - Double - the standard grwoth rate"+
            "\n\t*      stdL     - Double - the standard size"+
            "\n\t*      exponent - Double - the exponent"+
            "\n\t*      stdvRate - Double - std. deviation in random component of mortality"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[] - {G, L}, current size-specifc growth rate and size "+
            "\n\t* Value:"+
            "\n\t*      mortality rate - Double"+
            "\n\t* Calculation:"+
            "\n\t*      mortality_rate = Max{rate*[(G/stdG)^(1-exponent)]/[(L/stdL)^(1+exponent)]+stdvRate*N(0,1)),0];"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 5;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set rate parameter */
    public static final String PARAM_rate = "standardized mortality rate (m0) [1/day]";
    /** key to set standard size-specific growth rate */
    public static final String PARAM_stdG = "standard size-specific growth rate";
    /** key to set standard size */
    public static final String PARAM_stdL = "standard size";
    /** key to set exponent parameter */
    public static final String PARAM_exponent = "exponent (a)";
    /** key to set stdvRt parameter */
    public static final String PARAM_stdvRt = "std. dev. of rate (stdv)";
    
    /** value of rate parameter */
    private double rate = 0;
    /** value of standard size-specific growth rate */
    private double stdG = 1;
    /** value of standard size */
    private double stdL = 1;
    /** value of exponent parameter */
    private double exponent = 0;
    /** value of stdvRt parameter */
    private double stdvRt = 0;
   
    /** constructor for class */
    public MortalityRate_Pepin1991(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_rate;    addParameter(key,Double.class, key);
        key = PARAM_stdG;    addParameter(key,Double.class, key);
        key = PARAM_stdL;    addParameter(key,Double.class, key);
        key = PARAM_exponent;addParameter(key,Double.class, key);
        key = PARAM_stdvRt;  addParameter(key,Double.class, key);
    }
    
    @Override
    public MortalityRate_Pepin1991 clone(){
        MortalityRate_Pepin1991 clone = new MortalityRate_Pepin1991();
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
                case PARAM_stdG:
                    stdG = ((Double) value).doubleValue();
                    break;
                case PARAM_stdL:
                    stdL = ((Double) value).doubleValue();
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
     * @param vars - double{} - {G,L}
     * @return     - Double - the corresponding mortality rate (per day) 
     */
    @Override
    public Double calculate(Object vars) {
        double[] dvars = (double[]) vars;
        double G = dvars[0];
        double L = dvars[1];
        double res = rate*Math.pow(G/stdG,1.0D-exponent)/Math.pow(L/stdL,1.0D+exponent); 
        if (stdvRt>0) res += stdvRt*rng.computeNormalVariate();
        return new Double(Math.max(0.0,res));
    }
}
