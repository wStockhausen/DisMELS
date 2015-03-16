/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Growth;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * This function implements Buckley et al.s (2006) temperature, photo-period and 
 * weight-dependent growth model.
 * Type: 
 *      Individual growth function
 * Parameters (by key):
 *      a0   - Double - base growth coefficient (nominal value: -0.0203)
 *      b0   - Double - temperature coefficient (nominal value:  0.0106)
 *      maxT - Double - max temperature for growth
 *      sigRate - Double - std. deviation in random component to growth ([size]/[time])
 * Variables:
 *      vars: double[]{dt,P0,T,PP}.
 *      dt - time step
 *      P0 - initial weight in terms of protein
 *      T  - temperature (deg C)
 *      PP - photo-period (fraction of time step it's light)
 * Value:
 *      P(dt) - Double - protein weight at time t+dt
 * Calculation:
 *      g = a0+a1*ln(PP)+a2*T+a3*PP
 *      P(dt) = P0*exp(g*dt) if T<=maxT
 * TODO: consider specifying cv, not sigRate.
 * 
 * @author William.Stockhausen
 * Citation:
 * Buckley LJ, E.M. Caldarone, R.G. Lough, and  J.M. St. Onge-Burns.2006. Ontogenetic and seasonal trends 
 * in recent growth rates of Atlantic cod and haddock larvae on Georges Bank: effects of photoperiod and temperature. MEPS 325:205–226.
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class Buckley2006GrowthFunction extends AbstractIBMFunction 
                                        implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Individual growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Buckley et al.'s (2006) growth model";
    /** function description */
    public static final String DEFAULT_descr = "Buckley et al.'s (2006) temperature-dependent growth model";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 5;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set a0 parameter */
    public static final String PARAM_a0 = "a0";
    /** key to set a1 parameter */
    public static final String PARAM_a1 = "a1";
    /** key to set a2 parameter */
    public static final String PARAM_a2 = "a2";
    /** key to set a3 parameter */
    public static final String PARAM_a3 = "a3";
    /** key to set sigRate parameter */
    public static final String PARAM_stdvRt = "std. dev. of rate";
    
    /** value of a0 parameter */
    private double a0 = 0;    
    /** value of a1 parameter */
    private double a1 = 0;    
    /** value of a2 parameter */
    private double a2 = 0;    
    /** value of a3 parameter */
    private double a3 = 0;
    
    /** value of sigRate parameter */
    private double stdvRt = 0;
    
    /** constructor for class */
    public Buckley2006GrowthFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        //base coefficient
        key = PARAM_a0;  addParameter(key, Double.class, "base growth rate coefficient");
        //ln[P]^1 coefficient
        key = PARAM_a1;  addParameter(key, Double.class, "ln[P] growth rate coefficient");
        //T (temperature) coefficient
        key = PARAM_a2;  addParameter(key, Double.class, "temperature coefficient");
        //photo-period coefficient
        key = PARAM_a3;  addParameter(key, Double.class, "photo-period coefficient");
        
        key = PARAM_stdvRt; addParameter(key, Double.class, "std. dev. in growth rate");
    }
    
    @Override
    public Buckley2006GrowthFunction clone(){
        Buckley2006GrowthFunction clone = new Buckley2006GrowthFunction();
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
                case PARAM_a0:
                    a0    = ((Double) value).doubleValue();
                    break;
                case PARAM_a1:
                    a1    = ((Double) value).doubleValue();
                    break;
                case PARAM_a2:
                    a2    = ((Double) value).doubleValue();
                    break;
                case PARAM_a3:
                    a3    = ((Double) value).doubleValue();
                    break;
                case PARAM_stdvRt:
                    stdvRt = ((Double) value).doubleValue();
                    break;
            }
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter params 
     * and the input variable.
     * 
     * @param vars - the inputs variables, as a double[]:
     *      dt - time step
     *      P0 - initial weight in terms of protein
     *      T  - temperature (deg C)
     *      PP - photo-period (fraction of time step it's light)
     * @return - the function value (P[dt]) as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double dt  = lvars[i++];
        double P0  = lvars[i++];
        double T   = lvars[i++];
        double PP  = lvars[i++];
        double g = a0+a1*Math.log(PP)+a2*T+a3*PP;//specific growth rate
        if (stdvRt>0) g += rng.computeNormalVariate()*stdvRt;//TODO: use cv rather than sigRate?
        Double res = new Double(P0*Math.exp(g*dt));//assumes g*dt<<1
        return res;
    }
}
