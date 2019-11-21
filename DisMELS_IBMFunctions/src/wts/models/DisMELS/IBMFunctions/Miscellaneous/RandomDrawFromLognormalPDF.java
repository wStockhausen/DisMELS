/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Miscellaneous;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;

/**
 * This class provides an implementation of a random draw from a Lognormal PDF.
 * Type: 
 *      miscellaneous function
 * Parameters (by key):
 *      median - Double - median of distribution
 *      cv     - Double - coefficient of variation
 * Variables:
 *      null
 * Value:
 *      Double - draw from L(median, cv)
 * Calculation:
 *      value   = L(median,cv) [random draw from a lognormal distribution)
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class RandomDrawFromLognormalPDF extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "PDF";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Random draw from a lognormal PDF";
    /** function description */
    public static final String DEFAULT_descr = "Constant mortality rate with additive random noise";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a random draw from a Lognormal PDF."+
            "\n\t* Type: PDF"+
            "\n\t* Parameters (by key):"+
            "\n\t*      median - Double - median of distribution"+
            "\n\t*      cv     - Double - coefficient of variation"+
            "\n\t* Variables:"+
            "\n\t*      null"+
            "\n\t* Value:"+
            "\n\t*      Double - draw from L(median, cv)"+
            "\n\t* Calculation:"+
            "\n\t*      value   = L(median,cv) [random draw from a lognormal distribution)"+
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
    public static final String PARAM_median = "median";
    /** key to set stdvRt parameter */
    public static final String PARAM_cv = "cv";
    
    /** value of rate parameter */
    private double median = 0;
    /** value of cv parameter */
    private double cv = 0;
    
    /** ln-scale standard deviation */
    private double stdv = 0;
   
    /** constructor for class */
    public RandomDrawFromLognormalPDF(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_median;addParameter(key,Double.class, "median");
        key = PARAM_cv;addParameter(key,Double.class, "cv");
    }
    
    @Override
    public RandomDrawFromLognormalPDF clone(){
        RandomDrawFromLognormalPDF clone = new RandomDrawFromLognormalPDF();
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
                case PARAM_median:
                    median    = ((Double) value).doubleValue();
                    break;
                case PARAM_cv:
                    cv = ((Double) value).doubleValue();
                    stdv = 0.0;//TODO: fill in calculation
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
     * @return     - the random draw as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double rnd = median; 
        if (cv>0) rnd = median*Math.exp(stdv*rng.computeNormalVariate());
        return new Double(rnd);
    }
}
