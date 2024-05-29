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
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;

/**
 * This class provides an implementation of a generic power law function.
 * Type: 
 *      generic function
 * Parameters (by key):
 *      stdVal   - Double - mortality rate (1/day) at the standard covariate value
 *      stdX     - Double - the standard covariate value
 *      exponent - Double - inverse power law exponent
 *      cv       - Double - std. deviation in random component of mortality
 * Variables:
 *      vars - Double - the covariate value (x)
 * Value:
 *      f(x) - Double
 * Calculation:
 *      stdv = sqrt(ln[1+cv^2])
 *      f(x) = Max[stdVal*(x/stdX)^exponent+stdv*N(0,1)),0];
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class PowerLawFunction extends AbstractIBMFunction {
    
    /** nominal function classification */
    public static final String DEFAULT_type = "generic";
    /** nominal user-friendly function name */
    public static final String DEFAULT_name = "power law function";
    /** nominal function description */
    public static final String DEFAULT_descr = "power law function with multiplicative random noise: m = m0*[(x/x0)^a]*exp[stdv*N(0,1)]";
    /** nominal full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a generic power law function."+
            "\n\t* Type: generic function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      stdVal   - Double - value at the standard covariate value"+
            "\n\t*      stdX     - Double - the standard covariate value (x0)"+
            "\n\t*      exponent - Double - power law exponent"+
            "\n\t*      sigRate  - Double - cv of the random component"+
            "\n\t* Variables:"+
            "\n\t*      vars - Double - the covariate value (x)"+
            "\n\t* Value:"+
            "\n\t*      - Double"+
            "\n\t* Calculation:"+
            "\n\t*      stdv = sqrt(ln[1+cv^2])"+
            "\n\t*      f(x) = stdVal*[(x/stdX)^exponent]*exp{stdv*N(0,1)),0]"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 4;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set standard value parameter */
    public static final String PARAM_stdVal = "standardized value at x0";
    /** key to set standard covariate value parameter */
    public static final String PARAM_stdX = "standard value of covariate (x0)";
    /** key to set exponent parameter */
    public static final String PARAM_exponent = "power law exponent (a)";
    /** key to set cv parameter */
    public static final String PARAM_cv = "cv of random component";
    
    /** value of the standard value parameter */
    private double stdVal = 0;
    /** value of standard covariate value parameter */
    private double stdX = 0;
    /** value of exponent parameter */
    private double exponent = 0;
    /** value of cv parameter */
    private double cv = 0;
    
    private double stdv = 0;
   
    /** constructor for class */
    public PowerLawFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        
        String key; 
        key = PARAM_stdVal;  addParameter(key,Double.class, key);
        key = PARAM_stdX;    addParameter(key,Double.class, key);
        key = PARAM_exponent;addParameter(key,Double.class, key);
        key = PARAM_cv;      addParameter(key,Double.class, key);
    }
    
    @Override
    public PowerLawFunction clone(){
        PowerLawFunction clone = new PowerLawFunction();
        clone.setFunctionType(getFunctionType());
        clone.setFunctionName(getFunctionName());
        clone.setDescription(getDescription());
        clone.setFullDescription(getFullDescription());
        for (String key: getParameterNames()) {
            IBMParameter op = getParameter(key);
            IBMParameter np = clone.getParameter(key);
            np.setDescription(op.getDescription());
            np.setValue(op.getValue());
        }
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
                case PARAM_stdVal:
                    stdVal = ((Double) value).doubleValue();
                    break;
                case PARAM_stdX:
                    stdX = ((Double) value).doubleValue();
                    break;
                case PARAM_exponent:
                    exponent = ((Double) value).doubleValue();
                    break;
                case PARAM_cv:
                    cv = ((Double) value).doubleValue();
                    stdv = Math.sqrt(Math.log(1+cv*cv));
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter params 
     * and the input variable.
     * 
     * @param x - Double - the value of the covariate
     * @return  - Double - the corresponding power law function value 
     */
    @Override
    public Double calculate(Object x) {
        double xv = (Double) x;
        double res = stdVal*Math.pow(xv/stdX,exponent); 
        if (cv>0) res *= Math.exp(stdv*rng.computeNormalVariate());
        return new Double(res);
    }
}
