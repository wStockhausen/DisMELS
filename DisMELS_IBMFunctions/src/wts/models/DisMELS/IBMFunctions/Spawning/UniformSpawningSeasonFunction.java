/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Spawning;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.IBMFunctions.Miscellaneous.OnOffDOYFunction;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;
import wts.models.utilities.MathFunctions;

/**************************************************************************
* This class provides an implementation of a uniform distribution for
*  population spawning activity over a season.
* Type: 
*      spawning function
* Parameters (by key):
*      start - Double - starting day-of-year for range
*      stop  - Double - ending day-of-year for range
*      peakDOY - Double - peak of spawning
*      width   - Double - width of spawning peak
*      randomize - Boolean - flag to randomize spawning time within season
* Variables:
*      vars - double[] {t}
*      t    - day-of-year
* Value:
*      time to spawning (days), if t in spawning season
*      otherwise Double.POSITIVE_INFINITY
* author: William.Stockhausen
**************************************************************************/
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class UniformSpawningSeasonFunction extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "Spawning";
    /** user-friendly function name */
    public static final String DEFAULT_name = "uniform spawning season";
    /** function description */
    public static final String DEFAULT_descr = "uniform spawning season";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a uniform distribution for spawning"+
            "\n\t*  activity over a season."+
            "\n\t* Type: "+
            "\n\t*      spawning function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      on  - Double - day of year on which spawning starts"+
            "\n\t*      off - Double - day of year on which spawning ends"+
            "\n\t*      randomize - Boolean - flag to randomize spawning time"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[] {t}"+
            "\n\t*      t    - day-of-year"+
            "\n\t* Value:"+
            "\n\t*      day of year on which individual spawns"+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";

    /** number of settable parameters */
    public static final int numParams = 3;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set start parameter */
    public static final String PARAM_on = "starting day-of-year";
    /** key to set stop parameter */
    public static final String PARAM_off  = "stopping day-of-year";    
    /** flag to randomize spawning time */
    public static final String PARAM_randomize = "randomize spawning time?";
    
    /** value of start parameter */
    private double on = 0.0;
    /** value of stop parameter */
    private double off = 0.0;
    /**value of randomize flag */
    private boolean randomize = true;
    
    private double spdur = 0.0;
    
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();
    
    /** on/off function for spawning season */
    private OnOffDOYFunction onoff = null;
    
    /** constructor for class */
    public UniformSpawningSeasonFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_on;       addParameter(key,Double.class, key);
        key = PARAM_off;      addParameter(key,Double.class, key);
        key = PARAM_randomize;addParameter(key,Double.class, key);
    }
    
    @Override
    public UniformSpawningSeasonFunction clone(){
        UniformSpawningSeasonFunction clone = new UniformSpawningSeasonFunction();
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
                case PARAM_on:
                    on = MathFunctions.mod(((Double) value).doubleValue(),366.0);
                    onoff.setParameterValue(OnOffDOYFunction.PARAM_on, value);
                    break;
                case PARAM_off:
                    off = MathFunctions.mod(((Double) value).doubleValue(),366.0);
                    onoff.setParameterValue(OnOffDOYFunction.PARAM_off, value);
                    break;
            }
            if (on<=off){
                spdur = off-on;
            } else {
                spdur = on-off;
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
     * @return     - Double - next day of year on which spawning occurs
     */
    @Override
    public Double calculate(Object vars) {
        double res = MathFunctions.mod(on+spdur/2,366.0);
        if (randomize) {
            res = MathFunctions.mod(on+spdur*rng.computeUniformVariate(0.0, 1.0),366.0);
        }
        return res;
    }
}
