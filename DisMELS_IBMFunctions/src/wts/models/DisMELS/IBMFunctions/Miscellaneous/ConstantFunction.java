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
 * This class provides an implementation of a constant function.
 * Type: 
 *      generic function
 * Parameters (by key):
 *      constant - Double - the constant value
 * Variables:
 *      vars - null
 * Value:
 *      the constant - Double
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class ConstantFunction extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "generic";
    /** user-friendly function name */
    public static final String DEFAULT_name = "constant function";
    /** function description */
    public static final String DEFAULT_descr = "returns a constant value";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a constant function."+
            "\n\t* Type: "+
            "\n\t*      generic function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      constant - Double - the constant value"+
            "\n\t* Variables:"+
            "\n\t*      vars - any Object or null"+
            "\n\t* Value:"+
            "\n\t*      the constant value"+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";

    /** number of settable parameters */
    public static final int numParams = 1;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set the constant parameter */
    public static final String PARAM_constant = "the constant value";
    
    /** value of the constant parameter */
    private double constant = 0.0;
   
    /** constructor for class */
    public ConstantFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_constant;  addParameter(key,Double.class, key);
    }
    
    @Override
    public ConstantFunction clone(){
        ConstantFunction clone = new ConstantFunction();
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
                case PARAM_constant:
                    constant = ((Double) value).doubleValue();
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the value of the function.
     * 
     * @param vars - any Object or null
     * 
     * @return     - Double - the constant value
     */
    @Override
    public Double calculate(Object vars) {
        return new Double(constant);
    }
}
