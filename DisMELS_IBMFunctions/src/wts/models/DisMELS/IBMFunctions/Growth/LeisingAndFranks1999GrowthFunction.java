/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Growth;

import com.wtstockhausen.utils.RandomNumberGenerator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * This class provides an implementation of an individual growth function based
 * on Leising and Franks (1999) growth model.  Note that the formulation here is
 * based on weight-specific rates (dw/dt = g*w) rather than total rates (dw/dt = G)
 * so that the time integration results in w(dt) = w(0)*exp(g*dt).
 * 
 * Type: 
 *      Individual growth function
 * Parameters (by key):
 *      refT    - Double - reference temperature for Q's
 *      refM    - Double - reference metabolism rate
 *      qM      - Double - metabolism rate quotient (Q10 type)
 *      pM      - Double - exponent for weight-dependence of metabolism
 *      maxBeta - Double - max assimilation efficiency
 *      c1      - Double - linear coefficient for assimilation efficiency
 *      c2      - Double - exponential coefficient for assimilation efficiency
 *      minW    - Double - fractional cost of processing assimilated material
 *      qA      - Double - physiological rate quotient (Q10 type)
 *      alpha   - Double - fractional cost of processing assimilated material
 * Variables:
 *      vars: double[]{dt,w0,T,I}.
 *      dt - time interval   ([time])
 *      w0 - weight at time t0
 *      T  - temperature
 *      I  - total ingestion rate
 * Value:
 *      w(dt) - Double - weight at time t+dt
 * Calculation:
 *      w(dt) = w0*exp(g*dt)  g*dt<<1 where
 *      g = (1/w)*dw/dt = (1-alpha)*a - m
 *      m = refM*(qM^[T-refT])*w0^[pM-1]
 *      a = (qA^[T-refT])*b*(I/w0)
 *      b = maxBeta*[1-c1*exp(c2*[w-minW])]
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class LeisingAndFranks1999GrowthFunction extends AbstractIBMFunction implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Individual growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Leising and Franks (1999) growth model";
    /** function description */
    public static final String DEFAULT_descr = "Leising and Franks (1999) growth model";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 11;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set refT parameter */
    public static final String PARAM_refT = "reference temperature";
    /** key to set refM parameter */
    public static final String PARAM_refM = "reference metabolic rate";
    /** key to set qM parameter */
    public static final String PARAM_qM = "temperature-dependent metabolic quotient (Q10)";
    /** key to set pM parameter */
    public static final String PARAM_pM = "weight-dependent metabolic exponent";
    /** key to set qA parameter */
    public static final String PARAM_qA = "temperature-dependent assimilation quotient (Q10)";
    /** key to set maxBeta parameter */
    public static final String PARAM_maxBeta = "max assimilation efficiency";
    /** key to set c1 parameter */
    public static final String PARAM_c1 = "linear coefficient in function for beta";
    /** key to set c2 parameter */
    public static final String PARAM_c2 = "exponential coefficient in function for beta";
    /** key to set minW parameter */
    public static final String PARAM_minW = "minimum weight in life stage";
    /** key to set alpha parameter */
    public static final String PARAM_alpha = "fractional cost of processing assimilated material";
    /** key to set sigRate parameter */
    public static final String PARAM_stdvRate = "std. dev.";
    
    /** value of refT parameter */
    private double refT = 0;
    /** value of refM parameter */
    private double refM = 0;
    /** value of qM parameter */
    private double qM = 0;
    /** value of pM parameter */
    private double pM = 0;
    /** value of qA parameter */
    private double qA = 0;
    /** value of maxBeta parameter */
    private double maxBeta = 0;
    /** value of c1 parameter */
    private double c1 = 0;
    /** value of c2 parameter */
    private double c2 = 0;
    /** value of minW parameter */
    private double minW = 0;
    /** value of alpha parameter */
    private double alpha = 0;
    
    /** value of sigRate parameter */
    private double sigRt = 0;
    
    /** the set of main parameter names */
    protected static final Set<String> setOfParameterNames = new LinkedHashSet<>(16);
    /** the set of subfunction names (empty here) */
    protected static final Set<String> setOfSubfunctionNames = new LinkedHashSet<>(1);
    
    /** constructor for class */
    public LeisingAndFranks1999GrowthFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_refT;     addParameter(key,Double.class, "reference temperature");
        key = PARAM_refM;     addParameter(key,Double.class, "reference metabolic rate");
        key = PARAM_qM;       addParameter(key,Double.class, "temperature-dependent metabolic quotient (Q10)");
        key = PARAM_pM;       addParameter(key,Double.class, "weight-dependent metabolic coefficient");
        key = PARAM_qA;       addParameter(key,Double.class, "temperature-dependent assimilation quotient (Q10)");
        key = PARAM_maxBeta;  addParameter(key,Double.class, "max assimilation efficiency");
        key = PARAM_c1;       addParameter(key,Double.class, "linear coefficient in assimilation efficiency function");
        key = PARAM_c2;       addParameter(key,Double.class, "exponential coefficient in assimilation efficiency function");
        key = PARAM_minW;     addParameter(key,Double.class, "minimum weight in life stage");
        key = PARAM_alpha;    addParameter(key,Double.class, "fractional cost of processing assimilated material");
        key = PARAM_stdvRate; addParameter(key,Double.class, "std. dev. in linear growth rate");
    }
    
    @Override
    public LeisingAndFranks1999GrowthFunction clone(){
        LeisingAndFranks1999GrowthFunction clone = new LeisingAndFranks1999GrowthFunction();
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
        //the following sets the value in the parameter map AND in the local variable
        if (super.setParameterValue(param, value)){
            switch (param) {
                case PARAM_refT:
                    refT = ((Double) value).doubleValue();
                    break;
                case PARAM_refM:
                    refM = ((Double) value).doubleValue();
                    break;
                case PARAM_qM:
                    qM = ((Double) value).doubleValue();
                    break;
                case PARAM_pM:
                    pM = ((Double) value).doubleValue();
                    break;
                case PARAM_qA:
                    qA = ((Double) value).doubleValue();
                    break;
                case PARAM_maxBeta:
                    maxBeta = ((Double) value).doubleValue();
                    break;
                case PARAM_c1:
                    c1 = ((Double) value).doubleValue();
                    break;
                case PARAM_c2:
                    c2 = ((Double) value).doubleValue();
                    break;
                case PARAM_minW:
                    minW = ((Double) value).doubleValue();
                    break;
                case PARAM_alpha:
                    alpha = ((Double) value).doubleValue();
                    break;
                case PARAM_stdvRate:
                    sigRt = ((Double) value).doubleValue();
                    break;
            }
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter values 
     * and the input variable array.
     * 
     * @param vars - the inputs variables as a double[].
     *      dt - time step      [time]
     *      w0 - initial weight [mass]
     *      T  - temperature    [temp]
     *      I  - ingestion ([mass]/[time])
     * @return - the function value (w[dt]) as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double dt = lvars[i++];
        double w0 = lvars[i++];
        double T  = lvars[i++];
        double I  = lvars[i++];
        double m = refM*Math.pow(qM,T-refT)*Math.pow(w0,pM-1);
        double beta = maxBeta*(1.0-c1*Math.exp(-c2*(w0-minW)));
        double a = Math.pow(qA,T-refT)*beta*I;
        double g = (1.0-alpha)*a-m; 
        if (sigRt>0) g += rng.computeNormalVariate()*sigRt; 
        Double res = new Double(w0*Math.exp(g*dt));
        return res;
    }
}
