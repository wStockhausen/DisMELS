/*
 * PhysicalEnvironment2D.java
 *
 * Created on November 7, 2019.
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
public class PhysicalEnvironment2D {
    
    /* time interpolation sensitivity */
    public final static double eps = 1.0e-6;
    
    NetcdfReader nR;
    
    int iTime = -1;
    double ocean_time;    
    
    /** map of all ModelData fields */
    protected final HashMap<String,ModelData> mdMap = new HashMap<>(20);
    
    /** Class logger */
    private static final Logger logger = Logger.getLogger(PhysicalEnvironment2D.class.getName());
    
    /** Creates a new instance of PhysicalEnvironment */
    public PhysicalEnvironment2D() {
        logger.info("starting PhysicalEnvironment2D()");
        createFieldMaps();
        logger.info("finished PhysicalEnvironment2D()");
    }
    
    /**
     * Instantiates a PhysicalEnvironment2D object.
     * 
     * @param nR
     * @throws java.io.IOException 
     */
    public PhysicalEnvironment2D(NetcdfReader nR) 
                               throws java.io.IOException{
        logger.info("starting PhysicalEnvironment(nR)");
        createFieldMaps();
        this.iTime = 0;
        this.nR = nR;
        readTimeDependentFields();
        logger.info("finished PhysicalEnvironment(nR)");
    }
    
    /**
     * Instantiates a PhysicalEnvironment2D object.
     * 
     * @param iTime
     * @param nR
     * @throws java.lang.ArrayIndexOutOfBoundsException
     * @throws java.io.IOException 
     */
    public PhysicalEnvironment2D(int iTime, NetcdfReader nR) 
                                 throws java.lang.ArrayIndexOutOfBoundsException,
                                        java.io.IOException {
        logger.info("starting PhysicalEnvironment(iTime,nR,modGrid3D)");
        createFieldMaps();
        this.iTime = iTime;
        this.nR = nR;
        readTimeDependentFields();
        logger.info("finished PhysicalEnvironment(iTime,nR,modGrid3D)");
    }
    
    /**
     * Creates the initial map of internal names to model fields.
     */
    private void createFieldMaps(){
        GlobalInfo gi = GlobalInfo.getInstance();
        CriticalModelVariablesInfo cvis = gi.getCriticalModelVariablesInfo();
        Iterator<String> keys = cvis.getNames().iterator();
        while (keys.hasNext()){
            String key = keys.next();
            CriticalVariableInfo cvi = cvis.getVariableInfo(key);
            if (cvi.isSpatialField()) {
                mdMap.put(key,null);
            }//add field
        }
        OptionalModelVariablesInfo mvis = gi.getOptionalModelVariablesInfo();
        keys = mvis.getNames().iterator();
        while (keys.hasNext()){
            String key = keys.next();
            OptionalVariableInfo mvi = mvis.getVariableInfo(key);
            if (mvi.isSpatialField()) {
                mdMap.put(key,null);
            }//additional field
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
     * Returns the next PhysicalEnvironment2D instance
     * associated with the current NetcdfReader object;
     */
    public PhysicalEnvironment2D next()  
                               throws java.lang.ArrayIndexOutOfBoundsException, 
                                      java.io.IOException {
        PhysicalEnvironment2D pe = new PhysicalEnvironment2D();
        pe.iTime = iTime+1;
        pe.nR    = nR;
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
     * Returns the previous PhysicalEnvironment2D instance
     * associated with the current NetcdfReader object;
     */
    public PhysicalEnvironment2D previous()  
                               throws java.lang.ArrayIndexOutOfBoundsException, 
                                      java.io.IOException {
        PhysicalEnvironment2D pe = new PhysicalEnvironment2D();
        pe.iTime = iTime-1;
        pe.nR    = nR;
        pe.readTimeDependentFields();
        return pe;
    }
    
    /**
     * Gets the ocean_time associated with this PhysicalEnvironment2D instance.
     * 
     * @return 
     */
    public double getOceanTime() {
        return ocean_time;
    }
    
    /**
     * Interpolate time-dependent fields between two PhysicalEnvironment2D instances.  
     * Note: the resulting instance is not connected to a NetcdfReader instance 
     * and thus the next() method cannot be used on it.
     * @param t
     * @param pe0
     * @param pe1
     * 
     * @return 
     */
    public static PhysicalEnvironment2D interpolate(double t,
                                                    PhysicalEnvironment2D pe0, 
                                                    PhysicalEnvironment2D pe1) {
        PhysicalEnvironment2D ipe = null;
        if (Math.abs(t-pe0.ocean_time)<eps*Math.abs(pe1.ocean_time-pe0.ocean_time)) {
            System.out.println("ipe = pe0");
            ipe = pe0;
        } else if (Math.abs(t-pe0.ocean_time)<eps*Math.abs(pe1.ocean_time-pe0.ocean_time)) {
            System.out.println("ipe = pe1");
            ipe = pe1;
        } else {
            System.out.println("Interpolating pe");
            ipe = new PhysicalEnvironment2D();
            ipe.iTime = -1;
            ipe.nR    = null;

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
                                                      "Problem in PhysicalEnvironment2D.readTimeDependentFields().", 
                                                      "Could not read ROMS variable "+var+" for DisMELS field "+fld, 
                                                      JOptionPane.ERROR_MESSAGE);
                    }
                } 
            }
        }
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
        if (md==null) md = GlobalInfo.getInstance().getGrid3D().getGridField(fld);//check grid fields
        return md;
    }
    
     /**
     * Gets the value of a model field at grid coordinates xi,eta,k 
     * based on internal name, not the ROMS alias.
     * 
     * @param fld - the internal name for the field
     * @param xi  - grid coordinate
     * @param eta - grid coordinate
     * @return 
     */
   public double getFieldValue(String fld,int xi, int eta) {
        ModelData md = mdMap.get(fld);
        return md.getValue(xi, eta);
    }
}
