/*
 * PhysicalEnvironment.java
 *
 * Created on November 30, 2005, 4:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import ucar.ma2.Index;

/**
 *
 * @author William Stockhausen
 */
public class PhysicalEnvironment {
    
    /* time interpolation sensitivity */
    public final static double eps = 1.0e-6;
    /* flag to update layer depths like ROMS offline float tracking */
    public static boolean updateLayerDepthsLikeROMS = false;
    
    NetcdfReader nR;
    ModelGrid3D grid3D;
    
    int iTime = -1;
    double ocean_time;
    
    
    /** map of all ModelData fields */
    protected final HashMap<String,ModelData> mdMap = new HashMap<>(20);
    
    /** Class logger */
    private static final Logger logger = Logger.getLogger(PhysicalEnvironment.class.getName());
    
    /** Creates a new instance of PhysicalEnvironment */
    public PhysicalEnvironment() {
        logger.info("starting PhysicalEnvironment()");
        createFieldMap();
        logger.info("finished PhysicalEnvironment()");
    }
    
    /**
     * Instantiates a PhysicalEnvironment object.
     * 
     * @param nR
     * @param modGrid3D
     * @throws java.io.IOException 
     */
    public PhysicalEnvironment(NetcdfReader nR, ModelGrid3D modGrid3D) 
                               throws java.io.IOException{
        logger.info("starting PhysicalEnvironment(nR,modGrid3D)");
        createFieldMap();
        this.iTime = 0;
        this.nR = nR;
        grid3D = modGrid3D;
        readTimeDependentFields();
        logger.info("finished PhysicalEnvironment(nR,modGrid3D)");
    }
    
    /**
     * Instantiates a PhysicalEnvironment object.
     * 
     * @param iTime
     * @param nR
     * @param modGrid3D
     * @throws java.lang.ArrayIndexOutOfBoundsException
     * @throws java.io.IOException 
     */
    public PhysicalEnvironment(int iTime, NetcdfReader nR, ModelGrid3D modGrid3D) 
                               throws java.lang.ArrayIndexOutOfBoundsException,
                                       java.io.IOException {
        logger.info("starting PhysicalEnvironment(iTime,nR,modGrid3D)");
        createFieldMap();
        this.iTime = iTime;
        this.nR = nR;
        grid3D = modGrid3D;
        readTimeDependentFields();
        logger.info("finished PhysicalEnvironment(iTime,nR,modGrid3D)");
    }
    
    /**
     * Creates the initial map of internal names to model fields.
     */
    private void createFieldMap(){
        GlobalInfo gi = GlobalInfo.getInstance();
        CriticalModelVariablesInfo cvis = gi.getCriticalModelVariablesInfo();
        Iterator<String> keys = cvis.getNames().iterator();
        while (keys.hasNext()){
            String key = keys.next();
            CriticalVariableInfo cvi = cvis.getVariableInfo(key);
            if (cvi.isSpatialField()) mdMap.put(key,null);//add field
        }
        mdMap.put("Hz",null);//computed field
        mdMap.put("w",null); //computed field
        OptionalModelVariablesInfo mvis = gi.getOptionalModelVariablesInfo();
        keys = mvis.getNames().iterator();
        while (keys.hasNext()){
            String key = keys.next();
            OptionalVariableInfo mvi = mvis.getVariableInfo(key);
            if (mvi.isSpatialField()) mdMap.put(key,null);//additional field
        }        
    }
    
    /**
     * Adds name to map of fields to be extracted from the ROMS netcdf dataset.
     * 
     * @param name
     * @return 
     */
    public boolean addFieldName(String name){
        boolean b = mdMap.containsKey(name);
        if (b) mdMap.put(name,null);
        return !b;
    }

    /*
     *Compute vertical cell sizes at all horizontal rho points
     */
    public void computeHz() {
        logger.info("starting computeHz()");
        double[] zw;
        int N,M,L;
        N = grid3D.getN();
        M = grid3D.getM();
        L = grid3D.getL();
        Array Hza = Array.factory(double.class,new int[] {N,M+1,L+1});
        Hza.setIndexName(0,"s_rho");
        Hza.setIndexName(1,"eta_rho");
        Hza.setIndexName(2,"xi_rho");
        Index ima = Hza.getIndex();
        ModelData zeta = getField("zeta");
        for (int eta=0;eta<=M;eta++) {
            for (int xi=0;xi<=L;xi++) {
                zw = computeLayerZs(grid3D.h.getValue(xi,eta),
                                     zeta.getValue(xi,eta));
                for (int s=1;s<=N;s++) {
                    Hza.setDouble(ima.set(s-1,eta,xi),zw[s]-zw[s-1]);
                }
            }
        }
        ModelData Hz = new ModelData(ocean_time,Hza,"Hz");
        Hz.setDimIndices(-1,0,1,2);
        mdMap.put("Hz",Hz);
        logger.info("finished computeHz()");
    }
    
    /**
     * Computes the layer depths corresponding to a bathymetric depth bd and
     * surface height zeta.  Delegates to ModelGrid3D.computeLayerZs(bd,zeta).
     *
     *@returns double[] with layer z's, starting from the bottom (z<0).
     *@param bd--bathymetric depth in m (bd>0)
     *@param zeta--sea surface height (m)
     */
    public double[] computeLayerZs(double bd, double zeta) {
        return grid3D.computeLayerZs(bd,zeta);
    }
    
    /**
     * Computes the vertical velocity field W by scaling the omega field.
     */
    public void computeW() {
        logger.info("starting computeW()");
        int L,M,N;
        L = grid3D.getL();
        M = grid3D.getM();
        N = grid3D.getN();
        //want indices to run from 0 to L (or M or N)
        Array Wa = Array.factory(double.class,new int[] {N+1,M+1,L+1});
        Wa.setIndexName(0,"s_w");
        Wa.setIndexName(1,"eta_rho");
        Wa.setIndexName(2,"xi_rho");
        Index ima = Wa.getIndex();
        double W;
        ModelData omega = getField("omega");
        for (int s=0;s<=N;s++) {
            for (int eta=0;eta<=M;eta++) {
                for (int xi=0;xi<=L;xi++) {
                    W = omega.getValue(xi,eta,s)/
                            (grid3D.pm.getValue(xi,eta)*grid3D.pn.getValue(xi,eta));
                    Wa.setDouble(ima.set(s,eta,xi),W);
                }
            }
        }
        ModelData w = new ModelData(ocean_time,Wa,"w");
        w.setDimIndices(-1,0,1,2);
        mdMap.put("w",w);
        logger.info("finished computeW()");
    }
    
    /**
     * Test whether its possible to get a next
     * instance of PhysicalEnvironment with the current
     * NetcdfReader object.
     */
    public boolean hasNext() {
        boolean res = false;
        if (iTime<0) return res; //instance not associated with model dataset
        try {
            nR.getOceanTime(iTime+1);
            res = true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            //do nothing!
        }
        return res;
    }
    
    /**
     * Returns the next PhysicalEnvironment instance
     * associated with the current NetcdfReader object;
     */
    public PhysicalEnvironment next()  
                               throws java.lang.ArrayIndexOutOfBoundsException, 
                                      java.io.IOException {
        PhysicalEnvironment pe = new PhysicalEnvironment();
        pe.iTime = iTime+1;
        pe.nR    = nR;
        pe.grid3D = grid3D;
        pe.readTimeDependentFields();
        return pe;
    }
    
    /**
     * Test whether its possible to get a previous
     * instance of PhysicalEnvironment with the current
     * NetcdfReader object.
     */
    public boolean hasPrevious() {
        boolean res = false;
        if (iTime<0) return res; //instance not associated with model dataset
        try {
            nR.getOceanTime(iTime-1);
            res = true;
        } catch (ArrayIndexOutOfBoundsException ex) {
            //do nothing!
        }
        return res;
    }
    
    /**
     * Returns the previous PhysicalEnvironment instance
     * associated with the current NetcdfReader object;
     */
    public PhysicalEnvironment previous()  
                               throws java.lang.ArrayIndexOutOfBoundsException, 
                                      java.io.IOException {
        PhysicalEnvironment pe = new PhysicalEnvironment();
        pe.iTime = iTime-1;
        pe.nR    = nR;
        pe.grid3D = grid3D;
        pe.readTimeDependentFields();
        return pe;
    }
    
    /**
     * Gets the ocean_time associated with this PhysicalEnvironment instance.
     * 
     * @return 
     */
    public double getOceanTime() {
        return ocean_time;
    }
    
    /**
     * Interpolate time-dependent fields between two PhysicalEnvironment instances.  
     * Note: the resulting instance is not connected to a NetcdfReader instance 
     * and thus the next() method cannot be used on it.
     * Note: if updateHzLikeROMS is true, then the SSH field (zeta) is copied
     * from pe0, not interpolated using both pe's.
     * @param t
     * @param pe0
     * @param pe1
     * 
     * @return 
     */
    public static PhysicalEnvironment interpolate(double t,
                                                  PhysicalEnvironment pe0, 
                                                  PhysicalEnvironment pe1) {
        PhysicalEnvironment ipe = null;
        if (Math.abs(t-pe0.ocean_time)<eps*Math.abs(pe1.ocean_time-pe0.ocean_time)) {
            System.out.println("ipe = pe0");
            ipe = pe0;
        } else if (Math.abs(t-pe0.ocean_time)<eps*Math.abs(pe1.ocean_time-pe0.ocean_time)) {
            System.out.println("ipe = pe1");
            ipe = pe1;
        } else {
            System.out.println("Interpolating pe");
            ipe = new PhysicalEnvironment();
            ipe.iTime = -1;
            ipe.nR    = null;
            ipe.grid3D = pe1.grid3D;

            ipe.ocean_time = t;

            for (String fld: pe0.getFieldNames()){
                if (!(fld.equals("w")||fld.equals("Hz"))){
                    ModelData md0 = pe0.getField(fld);
                    ModelData md1 = pe1.getField(fld);
                    if ((md0!=null)&&(md1!=null)){
                        if (md0.isTimeDependent()){
                            ModelData mde = ModelData.timeInterpolate(t, md0, md1);
                            ipe.mdMap.put(fld,mde);
                        } else {
                            ipe.mdMap.put(fld,md0);
                        }
                    } else {
                        ipe.mdMap.put(fld,null);
                    }
                }
            }
            if (updateLayerDepthsLikeROMS) {
                //use SSH from 1st pe (otherwise it's updated)
                System.out.println("SSH is not updated (like ROMS)");
                ModelData zeta = pe0.getField("zeta");
                ipe.mdMap.put("zeta",zeta);
            }
            ipe.computeHz();
            ipe.computeW();
        }        
        return ipe;
    }
        
    private void readTimeDependentFields() 
                               throws java.lang.ArrayIndexOutOfBoundsException,
                                      java.io.IOException {
        logger.info("Starting readTimeDependentFields()");
        ocean_time = nR.getOceanTime(iTime);
        Iterator<String> it = mdMap.keySet().iterator();
        GlobalInfo gi = GlobalInfo.getInstance();
        CriticalModelVariablesInfo cvi = gi.getCriticalModelVariablesInfo();
        OptionalModelVariablesInfo ovi = gi.getOptionalModelVariablesInfo();
        while (it.hasNext()){
            String fld = it.next();
            if (!(fld.equals("w")||fld.equals("Hz"))){
                logger.info("readTimeDependentFields: updating "+fld);
                String var = cvi.getNameInROMSDataset(fld);
                if (var!=null) {
                    mdMap.put(fld, nR.getModelData(iTime, var,fld));
                } else {
                    var = ovi.getNameInROMSDataset(fld);
                    if (var!=null) {
                        mdMap.put(fld, nR.getModelData(iTime, var,fld));
                    } else {
                        JOptionPane.showMessageDialog(null, 
                                                      "Problem in PhysicalEnvironment.readTimeDependentFields().", 
                                                      "Could not read ROMS variable "+var+" for DisMELS field "+fld, 
                                                      JOptionPane.ERROR_MESSAGE);
                    }
                } 
            }
        }
        computeHz();
        computeW();//computes w field from omega.  DO NOT READ IN w from ROMS as it is NOT equivalent!!!! 
        System.gc();//call garbage collection
        logger.info("Finished readTimeDependentFields()");
    }
    
    /**
     * Returns a Set of the internal field names available.
     * 
     * @return Set<String>
     */
    public Set<String> getFieldNames() {
        return mdMap.keySet();
    }

    /**
     * Gets model field based on internal name, not the ROMS alias.
     * 
     * @param fld - the internal name for the field
     * @return 
     */
    public ModelData getField(String fld) {
        ModelData md = mdMap.get(fld);//check model fields
        if (md==null) md = grid3D.getGridField(fld);//check grid fields
        return md;
    }
    
     /**
     * Gets the value of a model field at grid coordinates xi,eta,k 
     * based on internal name, not the ROMS alias.
     * 
     * @param fld - the internal name for the field
     * @param xi  - grid coordinate
     * @param eta - grid coordinate
     * @param k   - grid coordinate
     * @return 
     */
   public double getFieldValue(String fld,int xi, int eta, int k) {
        ModelData md = mdMap.get(fld);
        int r = md.getRank();
        if (r==3) return md.getValue(xi, eta, k); else
        if (r==2) return md.getValue(xi, eta);
        return 0;
    }
}
