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
 * This class provides an implementation of a random draw from a Normal PDF.
 * Type: 
 *      miscellaneous function
 * Parameters (by key):
 *      mean - Double - mean of distribution
 *      stdv - Double - std. deviation
 * Variables:
 *      null
 * Value:
 *      Double - draw from N(mean, stdv)
 * Calculation:
 *      value   = N(mean,stdv) [random draw from a normal distribution)
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class RandomDrawFromNormalPDF extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "PDF";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Random draw from normal PDF";
    /** function description */
    public static final String DEFAULT_descr = "Random draw from normal PDF";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a random draw from a Normal PDF."+
            "\n\t* Type: "+
            "\n\t*      miscellaneous function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      mean - Double - mean of distribution"+
            "\n\t*      stdv - Double - std. deviation"+
            "\n\t* Variables:"+
            "\n\t*      null"+
            "\n\t* Value:"+
            "\n\t*      Double - draw from N(mean, stdv)"+
            "\n\t* Calculation:"+
            "\n\t*      value   = N(mean,stdv) [random draw from a normal distribution)"+
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
    public static final String PARAM_mean = "mean";
    /** key to set stdvRt parameter */
    public static final String PARAM_stdv = "std. dev.";
    
    /** value of rate parameter */
    private double mean = 0;
    /** value of stdvRt parameter */
    private double stdv = 0;
   
    /** constructor for class */
    public RandomDrawFromNormalPDF(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_mean;addParameter(key,Double.class, "mean");
        key = PARAM_stdv;addParameter(key,Double.class, "std. dev.");
    }
    
    @Override
    public RandomDrawFromNormalPDF clone(){
        RandomDrawFromNormalPDF clone = new RandomDrawFromNormalPDF();
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
                case PARAM_mean:
                    mean    = ((Double) value).doubleValue();
                    break;
                case PARAM_stdv:
                    stdv = ((Double) value).doubleValue();
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
        double rnd = mean; 
        if (stdv>0) rnd = mean+stdv*rng.computeNormalVariate();
        return new Double(rnd);
    }
}
