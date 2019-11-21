/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.SwimmingBehavior;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMMovementFunctionInterface;

/**
 * This class provides an implementation of size-based swimming speed.
 * Function type: 
 *      Swimming speed
 * Parameters (by PARAM_key):
 *      alpha - Double - swimming speed at nominal size [m/s]
 *      nomZ  - Double - nominal size [size units]
 *      beta  - Double - exponential coefficient
 *      rpw   - Double - random walk parameter (i.e., diffusion constant)
 * Variables:
 *      vars - double[]{dt,z}.
 *      dt   - integration time step [s]
 *      z    - double - size [same units as nominal size]
 * Value:
 *      v - Double - swimming speed at size z [m/s]
 * Calculation:
 *      eps  = N(0,1) [random draw from a normal distribution)
 *      v    = alpha*(z^beta)+eps*sqrt(rpw/dt);
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMMovementFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class PowerLawSwimmingSpeedFunction extends AbstractIBMFunction 
                                            implements IBMMovementFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Swimming speed";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Power law-based swimming speed function";
    /** function description */
    public static final String DEFAULT_descr = "v = alpha*(z/nomZ)^beta+N(0,sqrt(rpw/dt))";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of size-based swimming speed."+
            "\n\t* Function type: Swimming speed"+
            "\n\t* Parameters (by PARAM_key):"+
            "\n\t*      alpha - Double - swimming speed at nominal size [m/s]"+
            "\n\t*      nomZ  - Double - nominal size [size units]"+
            "\n\t*      beta  - Double - exponential coefficient"+
            "\n\t*      rpw   - Double - random walk parameter (i.e., diffusion constant) [m^2/s]"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[]{dt,z}."+
            "\n\t*      dt   - integration time step [s]"+
            "\n\t*      z    - double - size [same units as nominal size]"+
            "\n\t* Value:"+
            "\n\t*      v - Double - swimming speed at size z [m/s]"+
            "\n\t* Calculation:"+
            "\n\t*      eps  = N(0,1) [random draw from a normal distribution)"+
            "\n\t*      v    = alpha*(z/nomZ)^beta+eps*sqrt(rpw/dt);"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of parameters */
    public static final int numParams = 4;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    
    /** key to set linear swimming parameter */
    public static final String PARAM_alpha = "swimming speed at nominal size (m/s)";
    /** key to set linear swimming parameter */
    public static final String PARAM_nomZ = "nominal size [size units]";
    /** key to set exponential swimming parameter */
    public static final String PARAM_beta = "exponential coefficient";
    /** key to set exponential swimming parameter */
    public static final String PARAM_rpw = "random walk parameter [m^2/s]";
    
    /** value of linear coefficient */
    private double alpha = 0;
    /** value of nominal size */
    private double nomZ = 0;
    /** value of exponential coefficient */
    private double beta = 0;
    /** value of random walk parameter */
    private double rpw = 0;
    
    /** constructor for class */
    public PowerLawSwimmingSpeedFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key;
        key = PARAM_alpha; addParameter(key, Double.class,key);
        key = PARAM_nomZ;  addParameter(key, Double.class,key);
        key = PARAM_beta;  addParameter(key, Double.class,key);
        key = PARAM_rpw;  addParameter(key, Double.class,key);
    }
    
    @Override
    public PowerLawSwimmingSpeedFunction clone(){
        PowerLawSwimmingSpeedFunction clone = new PowerLawSwimmingSpeedFunction();
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
                case PARAM_alpha:
                    alpha    = ((Double) value).doubleValue();
                    break;
                case PARAM_nomZ:
                    nomZ    = ((Double) value).doubleValue();
                    break;
                case PARAM_beta:
                    beta    = ((Double) value).doubleValue();
                    break;
                case PARAM_rpw:
                    rpw    = ((Double) value).doubleValue();
                    break;
            }
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current params 
     * and the input variable.
     * 
     * @param vars - the inputs variables as a double[].
     *      dt - [0] - integration time step
     *      z  - [1] - size of individual
     * 
     * @return     - the function value (v) as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double[] dvars = (double[]) vars;//cast object to required double[]
        double rnd = 0; 
        if ((rpw>0)&&(dvars[0]>0)) rnd = Math.sqrt(rpw/dvars[0])*rng.computeNormalVariate();
        return new Double(alpha*Math.pow(dvars[1]/nomZ, beta)+rnd);
    }
}
