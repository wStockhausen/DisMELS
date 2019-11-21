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
import wts.models.utilities.MathFunctions;

/**
 * This class provides an implementation of a function that tests whether
 * the input day-of-year is with a given range of days.
 * Type: 
 *      generic function
 * Parameters (by key):
 *      start - Double - starting day-of-year for range
 *      stop  - Double - ending day-of-year for range
 * Variables:
 *      vars - double[] {input day-of-year}
 * Value:
 *      Double: 1 if start<= input <= stop, otherwise 0
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class OnOffDOYFunction extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "generic";
    /** user-friendly function name */
    public static final String DEFAULT_name = "day-of-year range";
    /** function description */
    public static final String DEFAULT_descr = "tests if day-of-year is within a given range";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a function that tests whether"+
            "\n\t* the input day-of-year is with a given range of days."+
            "\n\t* Type: "+
            "\n\t*      generic function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      start - Double - starting day-of-year for range"+
            "\n\t*      stop  - Double - ending day-of-year for range"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[] {input day-of-year}"+
            "\n\t* Value:"+
            "\n\t*      1 if start<= input <= stop, otherwise 0"+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";

    /** number of settable parameters */
    public static final int numParams = 2;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set start parameter */
    public static final String PARAM_on = "starting day-of-year";
    /** key to set stop parameter */
    public static final String PARAM_off  = "stopping day-of-year";
    
    /** value of start parameter */
    private double on = 0.0;
    /** value of stop parameter */
    private double off = 0.0;
   
    /** constructor for class */
    public OnOffDOYFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_on;  addParameter(key,Double.class, key);
        key = PARAM_off; addParameter(key,Double.class, key);
    }
    
    @Override
    public OnOffDOYFunction clone(){
        OnOffDOYFunction clone = new OnOffDOYFunction();
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
        Double val = MathFunctions.mod((Double) value, 366.0);
        if (super.setParameterValue(param, val)){
            switch (param) {
                case PARAM_on:
                    on = ((Double) val).doubleValue();
                    break;
                case PARAM_off:
                    off = ((Double) val).doubleValue();
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameters 
     * and the input variable.
     * 
     * @param vars - double[]{t} - the day-of-year to test
     * @return     - Double - 0 or 1
     */
    @Override
    public Double calculate(Object vars) {
        double res = 0.0;
        double t = ((double[])vars)[0];
        t = MathFunctions.mod(t, 366.0);
        if (on<=off){
            if ((on<=t)&&(t<=off)) res = 1.0;
        } else {
            res = 1.0;
            if ((off<t)&&(t<on)) res = 0.0;
        }
        return res;
    }
}
