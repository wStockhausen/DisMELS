/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Movement;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMMovementFunctionInterface;

/**
 * This class provides an implementation of a buoyancy-driven vertical ascension rate
 * for eggs.
 * Function type: 
 *      movement
 * Parameters (by PARAM_key):
 *      rpw - Double - random walk parameter ([distance]^2/[time])
 * Variables:
 *   @param vars - the inputs variables as a double[].
 *      [1] - dt   - integration time step [s]
 *      [1] - d    - egg diameter [mm]
 *      [2] - drho - density difference (egg - seawater) [kg/m^3]
 *      [3] - temp - water temperature [deg C]
 * Value:
 *      v - Double - ascension rate [m/s]
 * Calculation:
 *      based on Sundby (1983)
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMMovementFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class EggAscensionRate extends AbstractIBMFunction 
                                            implements IBMMovementFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "vertical movement";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Buoyancy-driven ascension rate for eggs";
    /** function description */
    public static final String DEFAULT_descr = "Buoyancy-driven ascension rate for eggs (m/s). Based on Sundby (1983).";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a buoyancy-driven vertical ascension rate"+
            "\n\t* for eggs."+
            "\n\t* Function type: movement"+
            "\n\t* Parameters (by PARAM_key):"+
            "\n\t*      rpw - Double - random walk parameter (m^2/s)"+
            "\n\t* Variables:"+
            "\n\t*   @param vars - the inputs variables as a double[]."+
            "\n\t*      [1] - dt   - integration time step [s]"+
            "\n\t*      [1] - d    - egg diameter [mm]"+
            "\n\t*      [2] - drho - density difference (egg - seawater) [kg/m^3]"+
            "\n\t*      [3] - temp - water temperature [deg C]"+
            "\n\t* Value:"+
            "\n\t*      v - Double - ascension rate [m/s]"+
            "\n\t* Calculation:"+
            "\n\t*      based on Sundby (1983)"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 1;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;
    
    /** key to set random walk parameter */
    public static final String PARAM_rpw = "random walk parameter (m^2/s)";
    
    /** value of random walk parameter */
    private double rpw = 0;
    
    /** acceleration due to gravity (m/s^2) */
    private static final double g = 9.80665;
    private static final double KI   = 19.0;//see Sundby (1983), Sci. Mar. 61(S1):159-176.
    private static final double zeta = 0.4;//see Sundby (1983), Sci. Mar. 61(S1):159-176.
    
    /** constructor for class */
    public EggAscensionRate(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key;
        key = PARAM_rpw;  addParameter(key, Double.class,key);
    }
    
    @Override
    public Object clone(){
        return new EggAscensionRate();
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
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter params 
     * and the input variable.
     * 
     * @param vars - the inputs variables as a double[].
     *      [0] - dt   - integration time step [s]
     *      [1] - d    - egg diameter [mm]
     *      [2] - drho - density difference (egg - seawater) [kg/m^3]
     *      [3] - temp - water temperature [deg C]
     * @return     - ascension rate [m/s] as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double[] dvars = (double[]) vars;//cast object to required double[]
        int k = 0;
        double dt   = Math.abs(dvars[k++]);//time step [s]
        double d    = dvars[k++];//egg diameter [mm]
        double drho = dvars[k++];//density anomaly (egg - seawater) [kg/m^3]
        double temp = dvars[k++];//temperature [deg C]
        double nu = (2.414e-5)*Math.pow(10.0,247.8/(133.15+temp));//[kg/(m*s)]; see wikipedia page ("viscosity of water")
        double Re = g*(d*d*d)*drho/(nu*nu);//Reynold's number
        Double res;
        if (Re<0.5){
            //in Stokes regime, use Stokes equation
            res = Re*nu/d;
        } else {
            //assume intermediate Reynolds number (0.5<Re<1000)
            double D = Math.pow((9*nu*nu)/(g*drho),0.33333333333);//as given in Sundby (1983)
            res = KI*(1000*d-zeta*D)*Math.pow(drho*drho/nu,0.333333333333);//from Dallavalle (1948) as given in Sundby (1983)
        }
        if ((rpw>0)&&(dvars[0]>0)) res += Math.sqrt(rpw/dt)*rng.computeNormalVariate();//add in random walk component
        return res;
    }
}
