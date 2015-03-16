/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Miscellaneous;

import com.wtstockhausen.utils.RandomNumberGenerator;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;

/**
 * This class provides an implementation of a random draw from a triangular PDF.
 * Type: 
 *      PDF
 * Parameters (by key):
 *      min  - Double - min of distribution
 *      mode - Double - mode of distribution
 *      max  - Double - max of distribution
 * Variables:
 *      null
 * Value:
 *      Double - draw from Tri(min,mode,max)
 * Calculation:
 *      value   = Tri(min,mode,max) [random draw from a triangular distribution)
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class RandomDrawFromTrianglePDF extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "PDF";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Random draw from triangular PDF";
    /** function description */
    public static final String DEFAULT_descr = "Random draw from triangular PDF";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* Type: PDF"+
            "\n\t* Parameters (by key):"+
            "\n\t*      min  - Double - min of distribution"+
            "\n\t*      mode - Double - mode of distribution"+
            "\n\t*      max  - Double - max of distribution"+
            "\n\t* Variables:"+
            "\n\t*      null"+
            "\n\t* Value:"+
            "\n\t*      Double - draw from Tri(min,mode,max)"+
            "\n\t* Calculation:"+
            "\n\t*      value   = Tri(min,mode,max) [random draw from a triangular distribution)"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 3;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set min parameter */
    public static final String PARAM_min  = "min";
    /** key to set mode parameter */
    public static final String PARAM_mode = "mode";
    /** key to set max parameter */
    public static final String PARAM_max  = "max";
    
    /** value of min parameter */
    private double min = 0;
    /** value of mode parameter */
    private double mode = 0;
    /** value of max parameter */
    private double max = 0;
   
    /** constructor for class */
    public RandomDrawFromTrianglePDF(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_min; addParameter(key,Double.class, "min");
        key = PARAM_mode;addParameter(key,Double.class, "mode");
        key = PARAM_max; addParameter(key,Double.class, "max");
    }
    
    @Override
    public RandomDrawFromTrianglePDF clone(){
        RandomDrawFromTrianglePDF clone = new RandomDrawFromTrianglePDF();
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
                case PARAM_min:
                    min    = ((Double) value).doubleValue();
                    break;
                case PARAM_mode:
                    mode    = ((Double) value).doubleValue();
                    break;
                case PARAM_max:
                    max    = ((Double) value).doubleValue();
                    break;
            }
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter values 
     * and the input variable.
     * 
     * @param vars - null
     * @return     - the random draw as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double rnd;
        //translate distribution so mode = 0
        double min = this.min-this.mode;
        double max = this.max-this.mode;
        double F = rng.computeUniformVariate(0.0D, 1.0D);//cumulative probability associated w/ rnd
        double Fm = -min/(max-min);//cum. prob. at 0
        if (F<=Fm){
            rnd = min-min*(max-min)*F;
        } else {
            double a = 1.0D;
            double b = -2.0D*max;
            double c = min*max-F*max*(max-min);
            rnd = (-b+Math.sqrt(b*b+4.0D*a*c))/(2.0D*a);
        }
        return new Double(rnd+this.mode);//translate back to mode
    }
}
