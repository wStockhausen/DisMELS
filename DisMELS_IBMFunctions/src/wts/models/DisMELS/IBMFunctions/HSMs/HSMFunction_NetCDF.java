/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.HSMs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import ucar.ma2.InvalidRangeException;
import wts.models.DisMELS.framework.HSMs.HSM_NetCDF;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameter;

/**
 * This class provides an implementation of an HSM function.
 * <pre>
 * Type: 
 *      habitat suitability function
 * Parameters (by key):
 *      fileName - String - file name
 * Variables:
 *      vars - null
 * Value:
 *      Double - the hsm value at the provided location
 * </pre>
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class HSMFunction_NetCDF extends AbstractIBMFunction {
    
    /** function classification */
    public static final String DEFAULT_type = "HSM";
    /** user-friendly function name */
    public static final String DEFAULT_name = "HSM function for netCDF file";
    /** function description */
    public static final String DEFAULT_descr = "returns HSM value at specified position";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a function to read HSM values from a netCDF file."+
            "\n\t* Type: "+
            "\n\t*      HSM"+
            "\n\t* Parameters (by key):"+
            "\n\t*      fileName - String - file name"+
            "\n\t* Variables:"+
            "\n\t*      vars - any Object or null"+
            "\n\t* Value:"+
            "\n\t*      Double - the hsm value at the provided location"+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";

    /** number of settable parameters */
    public static final int numParams = 1;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set the fileName parameter */
    public static final String PARAM_fileName = "the netCDF file name";
    
    /** value of the fileName parameter */
    protected String fileName = "";
    
    /** instance of HSM_NetCDF class to read file*/
    protected HSM_NetCDF hsm = null;
   
    /** constructor for class */
    public HSMFunction_NetCDF(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_fileName;  addParameter(key,String.class, key);
    }
    
    @Override
    public HSMFunction_NetCDF clone(){
        HSMFunction_NetCDF clone = new HSMFunction_NetCDF();
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
        boolean res = false;
        if (super.setParameterValue(param, value)){
            switch (param) {
                case PARAM_fileName:
                    fileName = (String) value;
                    if (hsm==null) hsm = new HSM_NetCDF();
                    try {
                        res = hsm.setConnectionString(fileName);
                    } catch (IOException ex) {
                        res = false;
                        String title = "Error: netCDF file not found";
                        String msg = "netCDF file \n\t'"+fileName+"'\nnot found!";
                        javax.swing.JOptionPane.showMessageDialog(null, msg, title, javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                    break;
            }
            return res;
        }
        return res;
    }

    /**
     * Calculates the value of the function.
     * 
     * @param vars double[]
     * <pre>
     * The first two elements of <code>vars</code> must be the 2-d position in 
     * the coordinate system the hsm uses.
     * 
     * If the length of <code>vars</code> is &gt 2, the return value will include
     * gradient information as well the local value of the HSM.
     * </pre>
     * @return     double[] with HSM value and (possibly) horizontal gradient information.
     */
    @Override
    public double[] calculate(Object vars) {
        double[] res = null;
        ArrayList al = (ArrayList) vars;
        double[] pos = (double[])al.get(0);
        if (al.size()==1){
            res = new double[1];
            try {
                res[0] = (Double)hsm.calcValue(pos);
            } catch (IOException | InvalidRangeException ex) {
                Logger.getLogger(HSMFunction_NetCDF.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            res = (double[]) hsm.calcValue(pos,null);
        }
        return res;
    }
}
