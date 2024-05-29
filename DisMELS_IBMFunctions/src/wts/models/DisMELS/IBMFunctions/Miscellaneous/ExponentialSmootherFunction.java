/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Miscellaneous;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;

/**
 * This class provides an implementation of an exponential smoothing function.
 * Type: 
 *      generic function
 * Parameters (by key):
 *      constant - Double - the constant value
 * Variables:
 *      vars - null
 * Value:
 *      the smoothed value - Double
 * 
 * @author William Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class ExponentialSmootherFunction extends AbstractIBMFunction {
    
    
    /** function classification */
    public static final String DEFAULT_type = "generic";
    /** user-friendly function name */
    public static final String DEFAULT_name = "exponential smoothing function";
    /** function description */
    public static final String DEFAULT_descr = "returns an exponentially-smoothed value";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of an exponential smoothing function."+
            "\n\t* Type: "+
            "\n\t*      generic function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      tau - Double: the value of the time constant, in days"+
            "\n\t* Variables:"+
            "\n\t*      vars - Double[]: {value to be smoothed, time step (in days)}"+
            "\n\t* Value:"+
            "\n\t*      the smoothed value"+
            "\n\t* author: William Stockhausen"+
            "\n\t**************************************************************************";

    /** number of settable parameters */
    public static final int numParams = 1;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set the time constant parameter */
    public static final String PARAM_timeConstant = "tau (time constant in days)";
    
    /** value of the time constant */
    protected double tau = 0.0;
    /** value of the smoother */
    protected double value = Double.NaN;
    
    /** constructor for class */
    public ExponentialSmootherFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_timeConstant;  addParameter(key,Double.class, key);
    }
    
    @Override
    public ExponentialSmootherFunction clone(){
        ExponentialSmootherFunction clone = new ExponentialSmootherFunction();
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
     * Sets the parameter value corresponding to the key given by param.
     * 
     * @param param - the parameter key (name)
     * @param value - its value
     * @return - true/false whether value was set
     */
    @Override
    public boolean setParameterValue(String param,Object value){
        if (super.setParameterValue(param, value)){
            switch (param) {
                case PARAM_timeConstant:
                    tau = ((Double) value);
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the value of the function.
     * 
     * @param vars - Double[]: {value to be smoothed, time step (in days)}
     * @return     - Double: the smoothed value
     */
    @Override
    public Double calculate(Object vars) {
        Double[] vals = (Double[]) vars;
        if (Double.isNaN(value)){
            value = vals[0];
        } else {
            double lam = -Math.expm1(-vals[1]/tau);//=(1-exp(-dt/tau))
            value = lam*vals[0] + (1.0-lam)*value;
        }
        return value;
    }
}
