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
 * This function implements the von Bertalanffy growth function.
 * Type: 
 *      Individual growth function
 * Parameters (by key):
 *      Linf - Double - asymptotic size
 *      K    - Double - growth rate
 *      sigRate - Double - std. deviation in random component to growth ([size]/[time])
 * Variables:
 *      vars: double[]{dt,z}.
 *      dt - time step
 *      z - initial size
 * Value:
 *      z(dt) - Double - size at time t+dt
 * Calculation:
 *      z(dt) = z(0)+(Linf-z)exp(-K*dt)+sigRate*sqrt(dt)
 * TODO: consider specifying cv, not sigRate.
 * 
 * @author William.Stockhausen
 * Citation:
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class vonBertalanffyGrowthFunction extends AbstractIBMFunction 
                                        implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Individual growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "von Bertalanffy growth function";
    /** function description */
    public static final String DEFAULT_descr = "von Bertalanffy growth function";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This function implements the von Bertalanffy growth function."+
            "\n\t* Type: "+
            "\n\t*      Individual growth function"+
            "\n\t* Parameters (by key):"+
            "\n\t*      Linf - Double - asymptotic size"+
            "\n\t*      K    - Double - growth rate"+
            "\n\t*      sigRate - Double - std. deviation in random component to growth ([size]/[time])"+
            "\n\t* Variables:"+
            "\n\t*      vars: double[]{dt,z}."+
            "\n\t*      dt - time step"+
            "\n\t*      z - initial size"+
            "\n\t* Value:"+
            "\n\t*      z(dt) - Double - size at time t+dt"+
            "\n\t* Calculation:"+
            "\n\t*      z(dt) = z(0)+(Linf-z)exp(-K*dt)+sigRate*sqrt(dt)"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 4;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set Linf parameter */
    public static final String PARAM_Linf = "Linf";
    /** key to set K parameter */
    public static final String PARAM_K = "K";
    /** key to set sigRate parameter */
    public static final String PARAM_stdvRt = "std. dev. of rate";
    
    /** value of Linf parameter */
    private double Linf = 0;    
    /** value of K parameter */
    private double K = 0;    
    
    /** value of sigRate parameter */
    private double stdvRt = 0;
    
    /** constructor for class */
    public vonBertalanffyGrowthFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        //asymptotic size
        key = PARAM_Linf;  addParameter(key, Double.class, "asymptotic size");
        //growth rate coefficient
        key = PARAM_K;  addParameter(key, Double.class, "growth rate coefficient");
        
        key = PARAM_stdvRt; addParameter(key, Double.class, "std. dev. in growth rate");
    }
    
    @Override
    public vonBertalanffyGrowthFunction clone(){
        vonBertalanffyGrowthFunction clone = new vonBertalanffyGrowthFunction();
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
                case PARAM_Linf:
                    Linf    = ((Double) value).doubleValue();
                    break;
                case PARAM_K:
                    K    = ((Double) value).doubleValue();
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
     *      z0 - initial weight in terms of protein
     * @return - the function value (z[dt]) as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double dt  = lvars[i++];
        double z0  = lvars[i++];
        double r = (Linf-z0)*Math.exp(K*dt);//growth rate
        if (stdvRt>0) r += rng.computeNormalVariate()*stdvRt/Math.sqrt(dt);//TODO: use cv rather than sigRate?
        Double z = z0+r*dt;
        return z;
    }
}
