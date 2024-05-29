/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Spawning;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;

/**
 * This class provides an implementation of batch spawning activity with"
 *  a poisson-distributed recovery period
 * Type: 
 *      spawning function
 * Parameters (by key):
 *      minRecoveryPeriod - Double - minimum recovery period (d)
 *      meaRecoveryPeriod - Double - mean recovery period (d)
 *      randomize         - Boolean - randomize recovery period
 * Variables:
 *      vars - any Object or null
 * Value:
 *      t - time to next spawning event(d)
 *      t ~ Poisson[meanRP,1/(meanRP-minRP)]
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class BatchSpawningFunction extends AbstractIBMFunction {
    
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();
    
    /** function classification */
    public static final String DEFAULT_type = "Spawning";
    /** user-friendly function name */
    public static final String DEFAULT_name = "batch spawning function";
    /** function description */
    public static final String DEFAULT_descr = "batch spawning function";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of batch spawning activity with"+
            "\n\t*  a poisson-distributed recovery period."+
            "\n\t* Type: "+
            "\n\t*      spawning function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      minRecoveryPeriod - Double - minimum recovery period (d)"+
            "\n\t*      meaRecoveryPeriod - Double - mean recovery period (d)"+
            "\n\t*      randomize         - Boolean - randomize recovery period "+
            "\n\t* Variables:"+
            "\n\t*      vars - any Object or null"+
            "\n\t* Value:"+
            "\n\t*      t - time to next spawning event(d)"+
            "\n\t*      t ~ Poisson[meanRP,1/(meanRP-minRP)]"+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";

    /** number of settable parameters */
    public static final int numParams = 2;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set min recovery period parameter */
    public static final String PARAM_minRecoveryPeriod = "minimum recovery period (d)";
    /** key to set mean recovery period parameter */
    public static final String PARAM_meanRecoveryPeriod = "mean recovery period (d)";
    /** key to set stochastic flag */
    public static final String PARAM_randomize = "randomize recovery period";
    
    /** value of min recovery period parameter */
    private double minRP = 0.0;
    /** value of mean recovery period parameter */
    private double meanRP = 0.0;
    /** value of mean recovery period parameter */
    private boolean randomize = false;
    
    /** constructor for class */
    public BatchSpawningFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_minRecoveryPeriod; addParameter(key,Double.class, key);
        key = PARAM_meanRecoveryPeriod;  addParameter(key,Double.class, key);
        key = PARAM_randomize;  addParameter(key,Boolean.class, key);        
    }
    
    @Override
    public BatchSpawningFunction clone(){
        BatchSpawningFunction clone = new BatchSpawningFunction();
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
                case PARAM_minRecoveryPeriod:
                    minRP = ((Double) value);
                    break;
                case PARAM_meanRecoveryPeriod:
                    meanRP = ((Double) value);
                    break;
                case PARAM_randomize:
                    randomize = ((Boolean) value);
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameters.
     * 
     * @param vars - any Object or null
     * @return     - Double - time to next spawning event
     */
    @Override
    public Double calculate(Object vars) {
        double res = meanRP;
        if (randomize){
            res = minRP-Math.log(rng.computeUniformVariate(0.0, 1.0))*(meanRP-minRP);
        }
        return res;
    }
}
