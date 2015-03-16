/*
 * ModelGrid3D.java
 *
 * Created on December 19, 2005, 10:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.model;

import java.io.IOException;

/**
 *
 * @author William Stockhausen
 */
public class ModelGrid3D extends ModelGrid2D {
    
    public static final String VAR_hc   = "hc";
    public static final String VAR_sc_w = "sc_w";
    public static final String VAR_Cs_w = "Cs_w";
    
    /** info for critical ROMS 3D grid variables */
    protected final CriticalModelVariablesInfo cmvi; 
                
    /** number of vertical layers */
    protected int N;
    /** critical depth parameter */
    protected double hc;
    /** vertical grid scaling parameters */
    protected ModelData sc_w;
    /** vertical grid scaling parameters */
    protected ModelData Cs_w;
        
   /** Creates a new instance of ModelGrid3D */
    public ModelGrid3D() {
        super();
        cmvi = GlobalInfo.getInstance().getCriticalModelVariablesInfo();
    }
    
    /**
     * Creates a new instance of ModelGrid3D based on the input filename, which
     * should be a ROMS model grid file.  Note that to complete creation of a
     * ModelGrid3D instance, the instance method "readConstantFields" needs to 
     * be called on a NetcdfReader instance that points to a valid ROMS dataset
     * (as distinct from a grid).
     * 
     * @param fn - filename for the ROMS model grid
     */
    public ModelGrid3D(String fn) {
        super(fn);
        cmvi = GlobalInfo.getInstance().getCriticalModelVariablesInfo();
    }
    
    /**
     * Creates a new instance of ModelGrid3D based on the input NetcdfDataset
     * instance, which should be a ROMS model grid file.  
     * Note that to complete creation of a ModelGrid3D instance, the instance 
     * method "readConstantFields" needs to be called on a NetcdfReader 
     * instance that points to a valid ROMS dataset (as distinct from a grid).
     * 
     * @param fn - filename for the ROMS model grid
     */
    public ModelGrid3D(ucar.nc2.dataset.NetcdfDataset nds) {
        super(nds);
        cmvi = GlobalInfo.getInstance().getCriticalModelVariablesInfo();
    }
    
    /**
     * Computes the layer depths corresponding to a bathymetric depth bd and
     * surface height zeta.
     *
     *@param bd--bathymetric depth (>0, m)
     *@param zeta--sea surface height (m)
     *@returns double[] with layer z's, starting from the bottom (z<0).
     */
    public double[] computeLayerZs(double bd, double zeta) {
        double cff_w, cff1_w,hinv,zw0;
        double[] zw = new double[N+1];
        hinv = 1.0/bd;
        zw[0]  = -bd;
        for (int s=1;s<=N;s++) {
            cff_w  = hc*(sc_w.getValue(s)-
                         Cs_w.getValue(s));
            cff1_w = Cs_w.getValue(s);
            zw0    = cff_w+cff1_w*bd;
//            zw[s]  = zw0+zeta*(1.0+zw0*hinv);
            zw[s]  = zw0+(Double.isNaN(zeta) ? 0.0 : zeta) *(1.0+zw0*hinv);
        }
        return zw;
    }
    
    /**
     * 
     * @return -- the number of vertical cells or layers 
     *              (note that the number of surfaces is N+1)
     */
    public int getN() {
        return N;
    }

    /**
     * Reads constant fields for the 3D grid setup from a netcdf reader
     * hooked up to a canonical ROMS dataset.
     * 
     * @param nR
     * @throws IOException 
     */
    public void readConstantFields(NetcdfReader nR) throws IOException {
        try {
            hc    = nR.readScalarDouble(cmvi.getNameInROMSDataset(VAR_hc));
            Cs_w  = nR.getModelData(cmvi.getNameInROMSDataset(VAR_Cs_w),VAR_Cs_w);
            sc_w  = nR.getModelData(cmvi.getNameInROMSDataset(VAR_sc_w),VAR_sc_w);
            
            N = sc_w.getShape()[0]-1;
        } catch (IOException ex) {
            System.out.println("Error reading constant fields");
            throw ex;
        }
    }
    
}
