package wts.models.DisMELS.IBMFunctions.Growth;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * This class provides an implementation of the Wisconsin Bioenergetics Model
 * as an individual growth function.
 * Type: 
 *      Individual growth function
 * Parameters (by key):
        pVal - Double - "realized fraction of max consumption"
        
        aC  - Double - "linear coefficient of weight-dependent max consumption"
        bC  - Double - "exponent coefficient of weight-dependent max consumption"
        cmT - Double - "max temperature at which consumption occurs"
        coT - Double - "temperature at which consumption is maximized"
        c1c - Double - "consumption coefficient"
        
        ACT  - Double - "respiration activity multiplier"
        aR  - Double - "linear coefficient of weight-dependent respiration"
        bR  - Double - "exponent coefficient of weight-dependent respiration"
        rmT  - Double - "max temperature at which respiration occurs"
        roT  - Double - "temperature at which respiration is maximized"
        c1r  - Double - "respiration coefficient"
        
        FA   - Double - "fraction of consumption lost to egestion"
        SDA  - Double - "fraction of assimilated energy lost to SDA"
        UA   - Double - ???
        
        sigRt  - Double - "std. dev. in linear growth rate"
 * Variables:
 *      vars - double[]{dt,w0,T}.
 *      dt - double - time interval   ([time])
 *      w0 - double - weight at time t0 ([weight])
 *      T  - double - temperature (deg C)
 * Value:
 *      w(dt) - Double - weight at time t+dt
 * Calculation:
 *     ;
 * 
 * @author William.Stockhausen
 *  Citation:
 * Hanson, P.C., T.B. Johnson, D.E. Schindler, and J. F. Kitchell. 1997. 
 * Fish Bioenergetics 3.0. University of Wisconsin Sea Grant Institute, Madison, WI.
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMGrowthFunctionInterface.class),
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class WisconsinBioenergeticsModelGrowthFunction extends AbstractIBMFunction implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Individual growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Constant linear growth";
    /** function description */
    public static final String DEFAULT_descr = "Wisconsin Bioenergetics Model";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of the Wisconsin Bioenergetics Model"+
            "\n\t* as an individual growth function."+
            "\n\t* Type: "+
            "\n\t*      Individual growth function"+
            "\n\t* Parameters (by key):"+
            "\n\t       pVal - Double - realized fraction of max consumption"+
            "\n\t"+
            "\n\t       aC  - Double - linear coefficient of weight-dependent max consumption"+
            "\n\t       bC  - Double - exponent coefficient of weight-dependent max consumption"+
            "\n\t       cmT - Double - max temperature at which consumption occurs"+
            "\n\t       coT - Double - temperature at which consumption is maximized"+
            "\n\t       c1c - Double - consumption coefficient"+
            "\n\t       "+
            "\n\t       ACT  - Double - respiration activity multiplier"+
            "\n\t       aR  - Double - linear coefficient of weight-dependent respiration"+
            "\n\t       bR  - Double - exponent coefficient of weight-dependent respiration"+
            "\n\t       rmT  - Double - max temperature at which respiration occurs"+
            "\n\t       roT  - Double - temperature at which respiration is maximized"+
            "\n\t       c1r  - Double - respiration coefficient"+
            "\n\t        "+
            "\n\t       FA   - Double - fraction of consumption lost to egestion"+
            "\n\t       SDA  - Double - fraction of assimilated energy lost to SDA"+
            "\n\t       UA   - Double - ???"+
            "\n\t"+
            "\n\t       sigRt  - Double - std. dev. in linear growth rate"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[]{dt,w0,T}."+
            "\n\t*      dt - double - time interval   ([time])"+
            "\n\t*      w0 - double - weight at time t0 ([weight])"+
            "\n\t*      T  - double - temperature (deg C)"+
            "\n\t* Value:"+
            "\n\t*      w(dt) - Double - weight at time t+dt"+
            "\n\t* Calculation:"+
            "\n\t*     ;"+
            "\n\t* "+
            "\n\t* @author William.Stockhausen"+
            "\n\t*  Citation:"+
            "\n\t* Hanson, P.C., T.B. Johnson, D.E. Schindler, and J. F. Kitchell. 1997. "+
            "\n\t* Fish Bioenergetics 3.0. University of Wisconsin Sea Grant Institute, Madison, WI."+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 16;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set pVal parameter */
    public static final String PARAM_pVal = "pVal";
    /** key to set aC parameter */
    public static final String PARAM_aC = "aC";
    /** key to set bC parameter */
    public static final String PARAM_bC = "bC";
    /** key to set cmT parameter */
    public static final String PARAM_cmT = "cmT";
    /** key to set coT parameter */
    public static final String PARAM_coT = "coT";
    /** key to set c1c parameter */
    public static final String PARAM_c1c = "c1c";
    /** key to set aR parameter */
    public static final String PARAM_ACT = "ACT";
    /** key to set aR parameter */
    public static final String PARAM_aR = "aR";
    /** key to set bR parameter */
    public static final String PARAM_bR = "bR";
    /** key to set rmT parameter */
    public static final String PARAM_rmT = "rmT";
    /** key to set roT parameter */
    public static final String PARAM_roT = "roT";
    /** key to set c1r parameter */
    public static final String PARAM_c1r = "c1r";
    /** key to set FA parameter */
    public static final String PARAM_FA = "FA";
    /** key to set SDA parameter */
    public static final String PARAM_SDA = "SDA";
    /** key to set UA parameter */
    public static final String PARAM_UA = "UA";
    /** key to set sigRate parameter */
    public static final String PARAM_sigRt = "std. dev.";
    
    /** value of pVal parameter */
    private double pVal = 0;
    
    /** value of aC parameter */
    private double aC = 0;
    /** value of bC parameter */
    private double bC = 0;
    /** value of cmT parameter */
    private double cmT = 0;
    /** value of coT parameter */
    private double coT = 0;
    /** value of c1c parameter */
    private double c1c = 0;
    
    /** value of aR parameter */
    private double ACT = 0;
    /** value of aR parameter */
    private double aR = 0;
    /** value of bR parameter */
    private double bR = 0;
    /** value of rmT parameter */
    private double rmT = 0;
    /** value of roT parameter */
    private double roT = 0;
    /** value of c1r parameter */
    private double c1r = 0;
    
    /** value of FA parameter */
    private double FA = 0;
    /** value of SDA parameter */
    private double SDA = 0;
    /** value of UA parameter */
    private double UA = 0;
    
    /** value of sigRate parameter */
    private double sigRt = 0;
    
    /** constructor for class */
    public WisconsinBioenergeticsModelGrowthFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_pVal;addParameter(key,Double.class,"realized fraction of max consumption");
        
        key = PARAM_aC; addParameter(key,Double.class,"linear coefficient of weight-dependent max consumption");
        key = PARAM_bC; addParameter(key,Double.class,"exponent coefficient of weight-dependent max consumption");
        key = PARAM_cmT;addParameter(key,Double.class,"max temperature at which consumption occurs");
        key = PARAM_coT;addParameter(key,Double.class,"temperature at which consumption is maximized");
        key = PARAM_c1c;addParameter(key,Double.class,"consumption coefficient");
        
        key = PARAM_ACT;addParameter(key,Double.class,"respiration activity multiplier");
        key = PARAM_aR; addParameter(key,Double.class,"linear coefficient of weight-dependent respiration");
        key = PARAM_bR; addParameter(key,Double.class,"exponent coefficient of weight-dependent respiration");
        key = PARAM_rmT;addParameter(key,Double.class,"max temperature at which respiration occurs");
        key = PARAM_roT;addParameter(key,Double.class,"temperature at which respiration is maximized");
        key = PARAM_c1r;addParameter(key,Double.class,"respiration coefficient");
        
        key = PARAM_FA; addParameter(key,Double.class,"fraction of consumption lost to egestion");
        key = PARAM_SDA;addParameter(key,Double.class,"fraction of assimilated energy lost to SDA");
        key = PARAM_UA; addParameter(key,Double.class,"std. dev. in linear growth rate");
        
        key = PARAM_sigRt;addParameter(key,Double.class,"std. dev. in linear growth rate");
    }
    
    @Override
    public WisconsinBioenergeticsModelGrowthFunction clone(){
        WisconsinBioenergeticsModelGrowthFunction clone = new WisconsinBioenergeticsModelGrowthFunction();
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
                case PARAM_pVal:
                    pVal = ((Double) value).doubleValue();
                    break;
                case PARAM_aC:
                    aC = ((Double) value).doubleValue();
                    break;
                case PARAM_bC:
                    bC = ((Double) value).doubleValue();
                    break;
                case PARAM_cmT:
                    cmT = ((Double) value).doubleValue();
                    break;
                case PARAM_coT:
                    coT = ((Double) value).doubleValue();
                    break;
                case PARAM_c1c:
                    c1c = ((Double) value).doubleValue();
                    break;
                case PARAM_ACT:
                    ACT = ((Double) value).doubleValue();
                    break;
                case PARAM_aR:
                    aR = ((Double) value).doubleValue();
                    break;
                case PARAM_bR:
                    bC = ((Double) value).doubleValue();
                    break;
                case PARAM_rmT:
                    rmT = ((Double) value).doubleValue();
                    break;
                case PARAM_roT:
                    roT = ((Double) value).doubleValue();
                    break;
                case PARAM_c1r:
                    c1r = ((Double) value).doubleValue();
                    break;
                case PARAM_FA:
                    FA = ((Double) value).doubleValue();
                    break;
                case PARAM_SDA:
                    SDA = ((Double) value).doubleValue();
                    break;
                case PARAM_UA:
                    UA = ((Double) value).doubleValue();
                    break;
                case PARAM_sigRt:
                    sigRt = ((Double) value).doubleValue();
                    break;
            }
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter params 
     * and the input variable.
     * 
     * @param vars - the inputs variables, {dt,w0,T}, as a double[].
     * @return     - the function value (w[dt]) as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double dt = lvars[i++];
        double w0 = lvars[i++];
        double T   = lvars[i++];
        double maxC = aC*Math.pow(w0,bC-1.0);//max consumption
        double c = maxC*pVal*calcF(T,cmT,coT,c1c);//realized weight-specific consumtion
        double maxR = aR*Math.pow(w0,bR-1.0);       //reference-level respiration
        double r = maxR*ACT*calcF(T,rmT,roT,c1r);
        double f = FA*c;     //weight-specific egestion
        double s = SDA*(c-f);//weight-specific loss due to specific dynamic ocean (TODO:is this correct?!)
        double m = r+s;       //weight-specific metabolic loss rate
        double e = UA*(c-f); //weight-specific excretion
        double w = f+e;       //weight-specific waste rate
        double g = c-(m+w);   //weight-specific total growth rate 
        if (sigRt>0) g += rng.computeNormalVariate()*sigRt; 
        Double res = new Double(w0*Math.exp(g*dt));
        return res;
    }

    private double calcF(double T, double Tm, double T0, double a){
        double v = (Tm-T)/(Tm-T0);
        double x = a*(Tm-T0)*(Tm-T0)/(Tm-T0+2);
        double f = Math.pow(v,x)*Math.exp(x-(1-v));
        return f;
    }
}
