/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.HSMs;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    /** flag to print debugging information */
    public static boolean debug = false;
   
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
                    if (fileName.equalsIgnoreCase("")||fileName.equalsIgnoreCase("<none>")){
                        //do nothing--no file set
                    } else {
                        try {
                            res = hsm.setConnectionString(fileName);
                        } catch (IOException ex) {
                            res = false;
                            String title = "Error in HSMFunction_NetCDF.setParameterValue(param,value)";
                            String msg = "netCDF file \n\t'"+fileName+"'\nnot found!";
                            System.out.println(title+"\n"+msg);
                            javax.swing.JOptionPane.showMessageDialog(null, msg, title, javax.swing.JOptionPane.ERROR_MESSAGE);
                        } catch (InvalidRangeException ex) {
                            System.out.println(ex.toString());
                        }
                    }
                    break;
            }
            return res;
        }
        return res;
    }

    /**
     * Calculates the value of the function at a location.
     * 
     * @param posLL double[], with position given as {lon,lat}
     * @return Double with HSM value
     */
    @Override
    public Double calculate(Object posLL) {
        if (debug) System.out.println("\tStarting HSMFunction_NetCDF.calculate(pos)");
        if (!(posLL instanceof double[])){   
            String msg = "Error in HSM_Function_NetCDF.calculate(vars).\n"+
                         "vars must be an ArrrayList or a double[], but got\n"+
                         "vars.class = "+posLL.getClass().getName()+".";
            throw(new java.lang.Error(msg));
        }
        
        double res = 0.0;
        try {
            res = (Double)hsm.calcValue((double[])posLL);
        } catch (IOException | InvalidRangeException ex) {
            Logger.getLogger(HSMFunction_NetCDF.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (debug) System.out.println("\tFinished HSMFunction_NetCDF.calculate(vars)");
        return res;
    }
}
