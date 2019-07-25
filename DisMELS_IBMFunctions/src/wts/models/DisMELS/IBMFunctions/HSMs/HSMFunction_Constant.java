/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.HSMs;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;

/**
 * This class provides a constant-value implementation of an HSM function.
 * <pre>
 * Type: 
 *      HSM (habitat suitability model or index)
 * Parameters (by key):
 *      value - Double - value to return
 * Variables:
 *      vars - null
 * Value:
 *      Double - returns the value, regardless of specified position
 * </pre>
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class HSMFunction_Constant extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "HSM";
    /** user-friendly function name */
    public static final String DEFAULT_name = "constant HSM";
    /** function description */
    public static final String DEFAULT_descr = "returns a constant value for the HSM regardless of the specified position";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides a constant-value implementation of an HSM function."+
            "\n\t* Type: "+
            "\n\t*      HSM"+
            "\n\t* Parameters (by key):"+
            "\n\t*      value"+
            "\n\t* Variables:"+
            "\n\t*      vars - any Object or null"+
            "\n\t* Value:"+
            "\n\t*      Double - the value, regardless of specified position"+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";

    /** number of settable parameters */
    public static final int numParams = 1;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set the fileName parameter */
    public static final String PARAM_value = "the value";
    
    /** value of the constant */
    protected double value = 0.0;
    
    /** flag to print debugging information */
    public static boolean debug = false;
    
    /** constructor for class */
    public HSMFunction_Constant(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_value;  addParameter(key,Double.class, key);
    }
    
    @Override
    public HSMFunction_Constant clone(){
        HSMFunction_Constant clone = new HSMFunction_Constant();
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
     * @param value - its new value
     * @return 
     */
    @Override
    public boolean setParameterValue(String param, Object value){
        boolean res = false;
        if (super.setParameterValue(param, value)){
            switch (param) {
                case PARAM_value:
                    this.value = (Double) value;
                    res = true;
                    break;
            }
            return res;
        }
        return res;
    }

    /**
     * Calculates the value of the function.
     * 
     * @param vars any Object or null
     * 
     * @return Double with HSM value as single element.
     */
    @Override
    public Double calculate(Object vars) {
        if (debug) System.out.println("\tStarting HSMFunction_Constant.calculate(pos)");
        if (debug) System.out.println("\tFinished HSMFunction_NetCdF.calculate(pos)");
        return value;
    }
}
