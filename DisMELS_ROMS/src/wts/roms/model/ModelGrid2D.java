/*
 * ModelGrid2D.java
 *
 * Created on November 29, 2005, 3:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *TODO: create/import variable Hz
 */
package wts.roms.model;

import com.wtstockhausen.utils.MathFunctions;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import ucar.ma2.ArrayChar;
import ucar.ma2.Index;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 *
 * @author William Stockhausen
 */
public class ModelGrid2D {
    
    /** map of all ModelData fields */
    protected final HashMap<String,ModelData> mdMap = new HashMap<>(40);
//    /** map of all MaskData fields */
//    protected final HashMap<String,MaskData> mkMap = new HashMap<>(7);
    
    
    NetcdfReader nr;
    boolean spherical;
    /*
     *Grid lengths in xi, eta directions
     */
    double xl,el;
    /* 
     *Max indices in xi, eta directions
     */
    int L,M;
    /*
     *Grid fields
     */
    ModelData h, pm, pn, dmde, dndx, angle;
    ModelData x_rho, x_psi, x_u, x_v;
    ModelData y_rho, y_psi, y_u, y_v;
    ModelData lat_rho, lat_psi, lat_u, lat_v;
    ModelData lon_rho, lon_psi, lon_u, lon_v;    

    /** mask fields */
    MaskData mask_rho, mask_psi, mask_u, mask_v;
    
    /**
     * Create model grid object based on file.
     * @param fn - filename for ROMS grid.
     */
    ModelGrid2D(String fn) {
        initialize();
        if (isGrid(fn)) {
            setDataset(fn);
        }
    }
    
    /**
     * Create model grid object based on netcdf dataset.
     * 
     * @param nds - the netcdf dataset representing the ROMS grid
     */
    ModelGrid2D(ucar.nc2.dataset.NetcdfDataset nds) {
        initialize();
        if (isGrid(nds)) {
            setDataset(nds);
        }
    }
    
    private void initialize() {
        nr = null;
        spherical = false;
        xl=el=0.0;
        this.L=this.M=-1;
        h=pm=pn=dmde=dndx=angle=null;
        mask_rho=mask_psi=mask_u=mask_v=null;
        x_rho=x_psi=x_u=x_v=null;
        y_rho=y_psi=y_u=y_v=null;
        lat_rho=lat_psi=lat_u=lat_v=null;
        lon_rho=lon_psi=lon_u=lon_v=null;         
        createFieldMaps();
    }
    
    /**
     * Creates the initial maps of internal names to model fields.
     */
    private void createFieldMaps(){
        //create mdMap
        CriticalGrid2DVariablesInfo cvis = GlobalInfo.getInstance().getCriticalGrid2DVariablesInfo();
        Iterator<String> keys = cvis.getNames().iterator();
        while (keys.hasNext()){
            String key = keys.next();
            CriticalVariableInfo cvi = cvis.getVariableInfo(key);
            if (cvi.isSpatialField()) mdMap.put(key,null);//additional field
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
     * Returns a Set of the internal field names available.
     * 
     * @return Set<String>
     */
    public Set<String> getFieldNames() {
        return mdMap.keySet();
    }
    
    /**
     * Tests if the file identified by fn is a ROMS grid file.
     * @param fn - path/filename to test
     * @return 
     */
    public static boolean isGrid(String fn) {
        boolean b = false;
        try {
            NetcdfDataset dsp = NetcdfDataset.openDataset(fn);
            b = isGrid(dsp);
        } catch (IOException ex) {
//            ex.printStackTrace();
        }
        return b;
    }
    
    /**
     * Tests if the input NetCDF dataset is a ROMS grid.
     * @param nds - path/filename to test
     * @return 
     */
    public static boolean isGrid(ucar.nc2.dataset.NetcdfDataset nds) {
        if (nds==null) return false;
        boolean b = (nds.findVariable(GlobalInfo.getInstance().getCriticalGrid2DVariablesInfo().getNameInROMSDataset("h"))!=null);
        return b;
    }
    
    /**
     * Reads the (double) value of the scalar identified by 'name'.
     * 
     * @param name
     * @return
     * @throws IOException 
     */
    private double readScalarDouble(String name) throws IOException {
        double x = 0;
        Variable v = nr.findVariable(name);
        if (v!=null) x = v.readScalarDouble(); else throw new IOException();
        return x;
    }
    
    /**
     * Extracts all required variables from the ROMS grid file associated with
     * the NetCdF reader instance, as determined by the ROMS GlobalInfo's 
     * CriticalGrid2DVariablesInfo instance.
     * 
     * @throws IOException 
     */
    private void extractVariables() throws IOException {
        CriticalGrid2DVariablesInfo cvis = GlobalInfo.getInstance().getCriticalGrid2DVariablesInfo();
        Variable sph = nr.findVariable(cvis.getNameInROMSDataset("spherical"));
        if (sph!=null) {
            ArrayChar ac = (ArrayChar) sph.read();
            Index ix = ac.getIndex();
            char c = ac.getChar(0);
//            char c = ac.getChar(ix.set(0));
            String str = String.valueOf(c);
            spherical = str.equalsIgnoreCase("T");
        } else {
            spherical = false;
        }
        
        xl = readScalarDouble(cvis.getNameInROMSDataset("xl"));
        el = readScalarDouble(cvis.getNameInROMSDataset("el"));

        h = nr.getModelData(cvis.getNameInROMSDataset("h"),"h");
        pm = nr.getModelData(cvis.getNameInROMSDataset("pm"),"pm");
        pn = nr.getModelData(cvis.getNameInROMSDataset("pn"),"pn");
        dmde = nr.getModelData(cvis.getNameInROMSDataset("dmde"),"dmde");
        dndx = nr.getModelData(cvis.getNameInROMSDataset("dndx"),"dndx");
        angle = nr.getModelData(cvis.getNameInROMSDataset("angle"),"angle");
        
        int[] shp = h.getShape();
        L = shp[1]-1; //max index in xi direction
        M = shp[0]-1; //max index in eta direction
        
        mask_rho = nr.getMaskData(cvis.getNameInROMSDataset("mask_rho"),"mask_rho");
        mask_psi = nr.getMaskData(cvis.getNameInROMSDataset("mask_psi"),"mask_psi");
        mask_u   = nr.getMaskData(cvis.getNameInROMSDataset("mask_u"),"mask_u");
        mask_v   = nr.getMaskData(cvis.getNameInROMSDataset("mask_v"),"mask_v");
        
        x_rho = nr.getModelData(cvis.getNameInROMSDataset("x_rho"),"x_rho");
        x_psi = nr.getModelData(cvis.getNameInROMSDataset("x_psi"),"x_psi");
        x_u   = nr.getModelData(cvis.getNameInROMSDataset("x_u"),"x_u");
        x_v   = nr.getModelData(cvis.getNameInROMSDataset("x_v"),"x_v");
        y_rho = nr.getModelData(cvis.getNameInROMSDataset("y_rho"),"y_rho");
        y_psi = nr.getModelData(cvis.getNameInROMSDataset("y_psi"),"y_psi");
        y_u   = nr.getModelData(cvis.getNameInROMSDataset("y_u"),"y_u");
        y_v   = nr.getModelData(cvis.getNameInROMSDataset("y_v"),"y_v");
        
        lat_rho = nr.getModelData(cvis.getNameInROMSDataset("lat_rho"),"lat_rho");
        lat_psi = nr.getModelData(cvis.getNameInROMSDataset("lat_psi"),"lat_psi");
        lat_u   = nr.getModelData(cvis.getNameInROMSDataset("lat_u"),"lat_u");
        lat_v   = nr.getModelData(cvis.getNameInROMSDataset("lat_v"),"lat_v");            
        lon_rho = nr.getModelData(cvis.getNameInROMSDataset("lon_rho"),"lon_rho");
        lon_psi = nr.getModelData(cvis.getNameInROMSDataset("lon_psi"),"lon_psi");
        lon_u   = nr.getModelData(cvis.getNameInROMSDataset("lon_u"),"lon_u");
        lon_v   = nr.getModelData(cvis.getNameInROMSDataset("lon_v"),"lon_v");            
        if (lat_rho!=null) spherical=true;
        
        Iterator<String> keys = mdMap.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            mdMap.put(key,getGridField1(key));
        }
    }
    
    /**
     * Set the ROMS grid using a file name.
     * 
     * @param fn 
     */
    public void setDataset(String fn) {
        try {
            if (isGrid(fn)) {
                nr = new NetcdfReader(fn);
                extractVariables();
            } else {
                System.out.println("Grid.setDataset(fn): File not valid ROMS grid.");
                System.out.println(fn);
                System.out.println("");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Set the ROMS grid using a netcdf dataset object. 
     * 
     * @param nds 
     */
    public void setDataset(ucar.nc2.dataset.NetcdfDataset nds) {
        try {
            if (isGrid(nds)) {
                nr = new NetcdfReader(nds);
                extractVariables();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Returns a grid field based on the internal name (not the ROMS alias) provided.
     * @param name
     * @return - the field as a ModelData object.
     */
    private ModelData getGridField1(String name) {
        if (name.equalsIgnoreCase("angle"))    {return angle;}    else 
        if (name.equalsIgnoreCase("dmde"))     {return dmde;}     else 
        if (name.equalsIgnoreCase("dndx"))     {return dndx;}     else 
        if (name.equalsIgnoreCase("h"))        {return h;}        else 
        if (name.equalsIgnoreCase("lat_psi"))  {return lat_psi;}  else 
        if (name.equalsIgnoreCase("lat_rho"))  {return lat_rho;}  else
        if (name.equalsIgnoreCase("lat_u"))    {return lat_u;}    else 
        if (name.equalsIgnoreCase("lat_v"))    {return lat_v;}    else 
        if (name.equalsIgnoreCase("lon_psi"))  {return lon_psi;}  else
        if (name.equalsIgnoreCase("lon_rho"))  {return lon_rho;}  else
        if (name.equalsIgnoreCase("lon_u"))    {return lon_u;}    else
        if (name.equalsIgnoreCase("lon_v"))    {return lon_v;}    else
        if (name.equalsIgnoreCase("mask_psi")) {return mask_psi;} else 
        if (name.equalsIgnoreCase("mask_rho")) {return mask_rho;} else 
        if (name.equalsIgnoreCase("mask_u"))   {return mask_u;}   else 
        if (name.equalsIgnoreCase("mask_v"))   {return mask_v;}   else 
        if (name.equalsIgnoreCase("pm"))       {return pm;}       else 
        if (name.equalsIgnoreCase("pn"))       {return pn;}       else 
        if (name.equalsIgnoreCase("x_psi"))    {return x_psi;}    else 
        if (name.equalsIgnoreCase("x_rho"))    {return x_rho;}    else 
        if (name.equalsIgnoreCase("x_u"))      {return x_u;}      else 
        if (name.equalsIgnoreCase("x_v"))      {return x_v;}      else 
        if (name.equalsIgnoreCase("y_psi"))    {return y_psi;}    else 
        if (name.equalsIgnoreCase("y_rho"))    {return y_rho;}    else 
        if (name.equalsIgnoreCase("y_u"))      {return y_u;}      else 
        if (name.equalsIgnoreCase("y_v"))      {return y_v;} 
        return null;
    }
    
    /**
     * Returns a grid field based on the internal name (not the ROMS alias) provided.
     * 
     * @param name
     * @return - the field as a ModelData object.
     */
    public ModelData getGridField(String name) {
        return mdMap.get(name);
    }
    
//    /**
//     * Returns a grid mask field based on the internal name (not the ROMS alias) provided.
//     * 
//     * @param name
//     * @return - the mask as a MaskData object (or null).
//     */
//    public MaskData getGridMask(String name) {
//        MaskData md = mkMap.get(name);
//        return md;
//    }
//    
    public double getH(int xi, int eta) 
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return h.getValue(xi,eta);
    }
    
    public double getPM(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return pm.getValue(xi,eta);
    }
    
    public double getPN(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return pn.getValue(xi,eta);
    }
    
    public double getDMDE(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return dmde.getValue(xi,eta);
    }
    
    public double getDNDX(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return dndx.getValue(xi,eta);
    }
    
    public double getAngle(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return angle.getValue(xi,eta);
    }
    
    public double getX_RHO(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return x_rho.getValue(xi,eta);
    }
    
    public double getX_PSI(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return x_psi.getValue(xi,eta);
    }
    
    public double getX_U(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return x_u.getValue(xi,eta);
    }
    
    public double getX_V(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return x_v.getValue(xi,eta);
    }
    
    public double getY_RHO(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return y_rho.getValue(xi,eta);
    }
    
    public double getY_PSI(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return y_psi.getValue(xi,eta);
    }
    
    public double getY_U(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return y_u.getValue(xi,eta);
    }
    
    public double getY_V(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return y_v.getValue(xi,eta);
    }
    
    public double getLat_RHO(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return lat_rho.getValue(xi,eta);
    }
    
    public double getLat_PSI(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return lat_psi.getValue(xi,eta);
    }
    
    public double getLat_U(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return lat_u.getValue(xi,eta);
    }
    
    public double getLat_V(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return lat_v.getValue(xi,eta);
    }
    
    /**
     *  Returns grid lon at xi,eta.
     *  @param xi  - grid coordinate value
     *  @param eta - grid coordinate value
     *
     *  @return lon ROMS grid longitude (range 0-360). (Note: NOT standard NAD83)
     * @throws java.lang.ArrayIndexOutOfBoundsException 
     */
    public double getLon_RHO(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return lon_rho.getValue(xi,eta);
    }
    
    /**
     *  Returns grid lon at xi,eta.
     *  @param xi  - grid coordinate value
     *  @param eta - grid coordinate value
     *
     *  @return lon ROMS grid longitude (range 0-360). (Note: NOT standard NAD83)
     * @throws java.lang.ArrayIndexOutOfBoundsException 
     */
    public double getLon_PSI(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return lon_psi.getValue(xi,eta);
    }
    
    /**
     *  Returns grid lon at xi,eta.
     *  @param xi  - grid coordinate value
     *  @param eta - grid coordinate value
     *
     *  @return lon ROMS grid longitude (range 0-360). (Note: NOT standard NAD83)
     * @throws java.lang.ArrayIndexOutOfBoundsException 
     */
    public double getLon_U(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return lon_u.getValue(xi,eta);
    }
    
    /**
     *  Returns grid lon at xi,eta.
     *  @param xi  - grid coordinate value
     *  @param eta - grid coordinate value
     *
     *  @return lon ROMS grid longitude (range 0-360). (Note: NOT standard NAD83)
     * @throws java.lang.ArrayIndexOutOfBoundsException 
     */
    public double getLon_V(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return lon_v.getValue(xi,eta);
    }
    
    public int getMask_RHO(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return (int) mask_rho.getValue(xi,eta);
    }
    
    public int getMask_PSI(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return (int) mask_psi.getValue(xi,eta);
    }
    
    public int getMask_U(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return (int) mask_u.getValue(xi,eta);
    }
    
    public int getMask_V(int xi, int eta)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        return (int) mask_v.getValue(xi,eta);
    }
    
    public boolean isSpherical() {
        return spherical;
    }

    /**
     * Gets max ROMS index in xi direction.
     * @return - the value
     */
    public int getL() {
        return L;
    }

    /**
     * Gets max ROMS index-1 in xi direction (max xi index internal to grid).
     * @return - the value
     */
    public int getLm() {
        return L-1;
    }

    /**
     * Gets max ROMS index in eta direction.
     * @return - the value
     */
    public int getM() {
        return M;
    }

    /**
     * Gets max ROMS index-1 in eta direction (max eta index internal to grid).
     * @return - the value
     */
    public int getMm() {
        return M-1;
    }
    
    /**
     *  Computes the grid xi,eta coordinates corresponding to the input
     *  lat, lon coordinates.  The input lat/lon coordinates should be
     *  in NAD83 (Greenwich PM, lon range -180 to 180).
     * @param lat 
     * @param lon 
     * @return double[] - grid {I,J} corresponding to lat,lon
     * @throws java.lang.ArrayIndexOutOfBoundsException 
     */
    public double[] computeIJfromLL(double lat, double lon)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        if (!spherical) return new double[] {-1.0,-1.0};
        //transform range to [0,360)
        double[] pos = new double[] {MathFunctions.mod(lon+360.0,360.0),lat};
        double[] posIJ = Grid2DUtilities.computeHorizontalIndices(pos,
                                                                    lon_rho,
                                                                    lat_rho,
                                                                    angle,
                                                                    spherical); 
        return posIJ;
    }
    
    /**
     *  Computes the grid xi,eta coordinates corresponding to the input
     *  x,y coordinates.
     * @param x 
     * @param y 
     * @return double[] - grid {I,J} corresponding to x,y
     * @throws java.lang.ArrayIndexOutOfBoundsException 
     */
    public double[] computeIJfromXY(double x, double y)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        //if (spherical) return new double[] {-1.0,-1.0};
        double[] pos = new double[] {x,y};
        double[] posIJ = Grid2DUtilities.computeHorizontalIndices(pos,
                                                            x_rho,
                                                            y_rho,
                                                            angle,
                                                            spherical); 
        return posIJ;
    }
    
    /**
     *  Returns the physical location of the vertices of the xi,eta cell in the
     * psi grid.
     * @param xi - xi index of cell
     * @param eta - eta index of cell 
     * @param spherical - flag to return lat,lon (true) or x,y (false)
     * @return double[4][2] - array of vertices
     */
    public double[][] getVerticesPSIgrid(int xi, int eta, boolean spherical) {
        double[][] verts = new double[4][2];
        if (spherical) {
            verts[0][0] = lon_rho.getValue(xi-1, eta-1);
            verts[0][1] = lat_rho.getValue(xi-1, eta-1);
            verts[1][0] = lon_rho.getValue(xi, eta-1);
            verts[1][1] = lat_rho.getValue(xi, eta-1);
            verts[2][0] = lon_rho.getValue(xi, eta);
            verts[2][1] = lat_rho.getValue(xi, eta);
            verts[3][0] = lon_rho.getValue(xi-1, eta);
            verts[3][1] = lat_rho.getValue(xi-1, eta);
        } else {
            verts[0][0] = x_rho.getValue(xi-1, eta-1);
            verts[0][1] = y_rho.getValue(xi-1, eta-1);
            verts[1][0] = x_rho.getValue(xi, eta-1);
            verts[1][1] = y_rho.getValue(xi, eta-1);
            verts[2][0] = x_rho.getValue(xi, eta);
            verts[2][1] = y_rho.getValue(xi, eta);
            verts[3][0] = x_rho.getValue(xi-1, eta);
            verts[3][1] = y_rho.getValue(xi-1, eta);
        }
        return verts;
    }
    
    /**
     *  Returns the physical location of the vertices of the xi,eta cell in the
     * rho grid.
     * @param xi - xi index of cell
     * @param eta - eta index of cell 
     * @param spherical - flag to return lat,lon (true) or x,y (false)
     * @return double[4][2] - array of vertices
     */
    public double[][] getVerticesRHOgrid(int xi, int eta, boolean spherical) {
        double[][] verts = new double[4][2];
        if (spherical) {
            verts[0][0] = lon_psi.getValue(xi, eta);
            verts[0][1] = lat_psi.getValue(xi, eta);
            verts[1][0] = lon_psi.getValue(xi+1, eta);
            verts[1][1] = lat_psi.getValue(xi+1, eta);
            verts[2][0] = lon_psi.getValue(xi+1, eta+1);
            verts[2][1] = lat_psi.getValue(xi+1, eta+1);
            verts[3][0] = lon_psi.getValue(xi, eta+1);
            verts[3][1] = lat_psi.getValue(xi, eta+1);
        } else {
            verts[0][0] = x_psi.getValue(xi, eta);
            verts[0][1] = y_psi.getValue(xi, eta);
            verts[1][0] = x_psi.getValue(xi+1, eta);
            verts[1][1] = y_psi.getValue(xi+1, eta);
            verts[2][0] = x_psi.getValue(xi+1, eta+1);
            verts[2][1] = y_psi.getValue(xi+1, eta+1);
            verts[3][0] = x_psi.getValue(xi, eta+1);
            verts[3][1] = y_psi.getValue(xi, eta+1);
        }
        return verts;
    }
    
    /**
     *  Returns the physical location of the vertices of the xi,eta cell in the
     * v grid.
     * @param xi - xi index of cell
     * @param eta - eta index of cell 
     * @param spherical - flag to return lat,lon (true) or x,y (false)
     * @return double[4][2] - array of vertices
     */
    public double[][] getVerticesUgrid(int xi, int eta, boolean spherical) {
        double[][] verts = new double[4][2];
        if (spherical) {
            verts[0][0] = lon_v.getValue(xi-1, eta);
            verts[0][1] = lat_v.getValue(xi-1, eta);
            verts[1][0] = lon_v.getValue(xi, eta);
            verts[1][1] = lat_v.getValue(xi, eta);
            verts[2][0] = lon_v.getValue(xi, eta+1);
            verts[2][1] = lat_v.getValue(xi, eta+1);
            verts[3][0] = lon_v.getValue(xi-1, eta+1);
            verts[3][1] = lat_v.getValue(xi-1, eta+1);
        } else {
            verts[0][0] = x_v.getValue(xi-1, eta);
            verts[0][1] = y_v.getValue(xi-1, eta);
            verts[1][0] = x_v.getValue(xi, eta);
            verts[1][1] = y_v.getValue(xi, eta);
            verts[2][0] = x_v.getValue(xi, eta+1);
            verts[2][1] = y_v.getValue(xi, eta+1);
            verts[3][0] = x_v.getValue(xi-1, eta+1);
            verts[3][1] = y_v.getValue(xi-1, eta+1);
        }
        return verts;
    }
    
    /**
     *  Returns the physical location of the vertices of the xi,eta cell in the
     * v grid.
     * @param xi - xi index of cell
     * @param eta - eta index of cell 
     * @param spherical - flag to return lat,lon (true) or x,y (false)
     * @return double[4][2] - array of vertices
     */
    public double[][] getVerticesVgrid(int xi, int eta, boolean spherical) {
        double[][] verts = new double[4][2];
        if (spherical) {
            verts[0][0] = lon_u.getValue(xi, eta-1);
            verts[0][1] = lat_u.getValue(xi, eta-1);
            verts[1][0] = lon_u.getValue(xi+1, eta-1);
            verts[1][1] = lat_u.getValue(xi+1, eta-1);
            verts[2][0] = lon_u.getValue(xi+1, eta);
            verts[2][1] = lat_u.getValue(xi+1, eta);
            verts[3][0] = lon_u.getValue(xi, eta);
            verts[3][1] = lat_u.getValue(xi, eta);
        } else {
            verts[0][0] = x_u.getValue(xi, eta-1);
            verts[0][1] = y_u.getValue(xi, eta-1);
            verts[1][0] = x_u.getValue(xi+1, eta-1);
            verts[1][1] = y_u.getValue(xi+1, eta-1);
            verts[2][0] = x_u.getValue(xi+1, eta);
            verts[2][1] = y_u.getValue(xi+1, eta);
            verts[3][0] = x_u.getValue(xi, eta);
            verts[3][1] = y_u.getValue(xi, eta);
        }
        return verts;
    }
    public boolean isOnLand(double[] pos){
        int Ir = (int) Math.floor(pos[0])+1;
        int Jr = (int) Math.floor(pos[1])+1;

        int i1 = Math.min(Math.max(Ir,1),L);
        int j1 = Math.min(Math.max(Jr,1),M);
        return (mask_psi.getValue(i1,j1)<0.5);
    }
}
