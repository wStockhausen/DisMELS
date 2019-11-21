/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Miscellaneous;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;

/**
 * This class provides an implementation of a logistic function.
 * Type: 
 *      generic function
 * Parameters (by key):
 *      z50   - Double - x at f(x) = 50% 
 *      slope - Double - slope at f(x) = 50% 
 * Variables:
 *      vars - double[]{x}
 *      x - independent variable
 * Value:
 *      f(x) - Double
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class LogisticFunction extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "generic";
    /** user-friendly function name */
    public static final String DEFAULT_name = "logistic function";
    /** function description */
    public static final String DEFAULT_descr = "generic logistic function";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a generic logistic function."+
            "\n\t* Type: "+
            "\n\t*      generic function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      x50   - Double - x at f(x) = 50%"+
            "\n\t*      slope - Double - slope at f(x) = 50%"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[]{x}"+
            "\n\t*      x    - independent variable"+
            "\n\t* Value:"+
            "\n\t*      f(x) = 1/(1+exp[-slope*(x-x50)])"+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";

    /** number of settable parameters */
    public static final int numParams = 2;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set size at 50% maturity parameter */
    public static final String PARAM_x50 = "x at f(x) = 50%";
    /** key to set slope at 50% maturity parameter */
    public static final String PARAM_slope = "slope at x50";
    
    /** value of z50 parameter */
    private double x50 = 0.0;
    /** value of slope parameter */
    private double slope = 1.00;
   
    /** constructor for class */
    public LogisticFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_x50;   addParameter(key,Double.class, key);
        key = PARAM_slope; addParameter(key,Double.class, key);
    }
    
    @Override
    public LogisticFunction clone(){
        LogisticFunction clone = new LogisticFunction();
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
                case PARAM_x50:
                    x50 = ((Double) value).doubleValue();
                    break;
                case PARAM_slope:
                    slope = ((Double) value).doubleValue();
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the value of the function given the current parameters 
     * and the input variable.
     * 
     * @param vars - null
     * @return     - Double - 1/(1+exp[-slope*(x-x50)])
     */
    @Override
    public Double calculate(Object vars) {
        double[] dvars = (double[]) vars;
        double x = dvars[0];
        return new Double(1.0/(1.0+Math.exp(-slope*(x-x50))));
    }
}
