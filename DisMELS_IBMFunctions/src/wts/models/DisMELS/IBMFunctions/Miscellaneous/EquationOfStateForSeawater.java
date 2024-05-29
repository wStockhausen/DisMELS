/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Miscellaneous;

import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;

/**
 * This class provides an implementation of the JPOTS equation of state for seawater,
 * as described in Gill, (1982).
 * Type: 
 *      oceanographic
 * Parameters (by key): none
 * Variables:
 *      vars - double[] - consisting of
 *      T - temperature (deg C)
 *      S - salinity (PSU)
 *      D - depth (m)
 * Value:
 *      density of seawater (kg/m^3) at T,S,D
 * Calculation:
 *      As in Gill, 1982. Input depth is converted to pressure in bar using 
 *      the approximate formula P (bar) = 10*D.
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class EquationOfStateForSeawater extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "oceanographic";
    /** user-friendly function name */
    public static final String DEFAULT_name = "JPOTS equation of state for seawater";
    /** function description */
    public static final String DEFAULT_descr = "JPOTS equation of state for seawater (Gill, 1982)";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of the JPOTS equation of state for seawater,"+
            "\n\t* as described in Gill, (1982)."+
            "\n\t* Type: "+
            "\n\t*      oceanographic"+
            "\n\t* Parameters (by key): none"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[] - consisting of"+
            "\n\t*      T - temperature (deg C)"+
            "\n\t*      S - salinity (PSU)"+
            "\n\t*      D - depth (m)"+
            "\n\t* Value:"+
            "\n\t*      density of seawater (kg/m^3) at T,S,D"+
            "\n\t* Calculation:"+
            "\n\t*      As in Gill, 1982. Input depth is converted to pressure in bar using "+
            "\n\t*      the approximate formula P (bar) = 10*D."+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";

    /** number of parameters */
    public static final int numParams = 0;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    public EquationOfStateForSeawater(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
    }
    
    @Override
    public Object clone() {
        EquationOfStateForSeawater clone = new EquationOfStateForSeawater();
        clone.setFunctionType(getFunctionType());
        clone.setFunctionName(getFunctionName());
        clone.setDescription(getDescription());
        clone.setFullDescription(getFullDescription());
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
        return false;
    }

    /**
     * 
     * @param vars - double[] consisting of
     *      T - temperature (deg C)
     *      S - salinity (PSU)
     *      D - depth (m)
     * 
     * @return rho - density of seawater (kg/m^3) at T,S,D
     */
    @Override
    public Double calculate(Object vars) {
        double[] dvars = (double[]) vars;
        double T = dvars[0];//temperature (deg C)
        double S = dvars[1];//salinity (PSU)
        double P = dvars[2]/10.0;//should be pressure in bars, ~=0.1*(depth in m) [1 m ~ 1 dbar]
        double T2 = T*T;
        double T3 = T*T2;
        double T4 = T*T3;
        double T5 = T*T4;
        double pT = 999.842594 + (6.793952E-2)*T - (9.095290E-3)*T2 + (1.001685E-4)*T3
                               - (1.120083E-6)*T4 + (6.536332E-9)*T5;//ok
        double pTS = pT + S*(0.824493 - (4.0899E-3)*T + (7.6438E-5)*T2 - (8.2467E-7)*T3 + (5.3875E-9)*T4)
                        + Math.pow(S,1.5)*((-5.72466E-3) + (1.0227E-4)*T - (1.6546E-6)*T2)
                        + S*S*(4.8314E-4);//ok
        
        double Kt = 19652.21 + 148.4206*T - 2.327105*T2 + (1.360477E-2)*T3 - (5.155288E-5)*T4;
        
        double Kts = Kt + S*(54.6746 - 0.603459*T + (1.09987E-2)*T2 - (6.1670E-5)*T3)
                        + Math.pow(S,1.5)*(7.944E-2 + (1.6483E-2)*T - (5.3009E-4)*T2);
        
        double Ktsp = Kts + P*(3.239908 + (1.43713E-3)*T + (1.16092E-4)*T2 - (5.77905E-7)*T3)
                          + P*S*(2.2838E-3 - (1.0981E-5)*T - (1.6078E-6)*T2)
                          + P*Math.pow(S,1.5)*(1.91075E-4)
                          + P*P*(8.50935E-5 - (6.12293E-6)*T + (5.2787E-8)*T2)
                          + P*P*S*(-9.9348E-7 + (2.0816E-8)*T + (9.1697E-10)*T2);
        
        double rho = pTS/(1.0D-P/Ktsp);
        return rho;
    }
    
}
