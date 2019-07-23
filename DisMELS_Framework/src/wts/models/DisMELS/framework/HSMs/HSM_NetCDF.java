/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.HSMs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import wts.roms.gis.AlbersNAD83;

/**
 *
 * @author William Stockhausen
 */
public class HSM_NetCDF implements HSM_Interface {

    /** string giving connection info (filename, url) to netcdf file */
    protected String conn = null;
    /** the netcdf datset object */
    protected NetcdfDataset ds = null;
    /** number of cells in x direction */
   protected  int nx;
    /** number of cells in y direction */
    protected int ny;
    /** x coordinates of the centers of the HSM raster cells (m), as a ucar.nc2.Variable */
    protected Variable x = null;
    /** y coordinates of the centers of the HSM raster cells (m), as a ucar.nc2.Variable */
    protected Variable y = null;
    /** habitat suitability values as netcdf Variable */
    protected Variable hsm = null;
    
    /** flag indicating active connection */
    protected boolean isConnected = false;
    
        /** cell size (m) */
    protected double csz; 
    /** x-coordinate for origin of the hsm (center of upper left corner raster cell; i=0) */
    protected double xul;
    /** y-coordinate for origin of the hsm (center of upper left corner raster cell; j=0) */
    protected double yul;

    protected Index idx;
    
    /** flag to print debugging information */
    public static boolean debug = false;
    
    private static final Logger logger = Logger.getLogger(HSM_NetCDF.class.getName());
    
    public HSM_NetCDF(){
        
    }
    
    public String getConnectionString() {
        return(conn);
    }

    public boolean setConnectionString(String conn) throws IOException, InvalidRangeException {
        try{
            if (ds!=null) {ds.close(); ds=null; isConnected=false;}//close old connection
            ds  = NetcdfDataset.openDataset(conn);
            nx  = ds.findDimension("x").getLength();
            ny  = ds.findDimension("y").getLength();
            x   = ds.findVariable("x");
            y   = ds.findVariable("y");
            hsm = ds.findVariable("hsm");
            
            ucar.ma2.Array xa = x.read(new int[]{0}, new int[]{2});
            ucar.ma2.Array ya = y.read(new int[]{0}, new int[]{1});
            
            xul = xa.getDouble(0); //x-coordinate for origin of hsm (center of upper left corner raster cell)
            yul = ya.getDouble(0); //y-coordinate for origin of hsm (center of upper left corner raster cell)
            csz = xa.getDouble(1)-xul;
            
            isConnected = true;
            return isConnected;
        } catch (IOException ex) {
            logger.severe("\n\tCould not set connection string \n\t\t'"+conn+"'");
            throw(ex);
        }
    }
    
    public int getNX(){return nx;}
    public int getNY(){return ny;}
    public double getXUL(){return xul;}
    public double getYUL(){return yul;}
    public double getCellSize(){return csz;}

    /**
     * Checks if instance is connected to an HSMap.
     * 
     * @return true if connected
     */
    public boolean isConnected(){return isConnected;}
    
    /**
     * Calculate value of the HSM at position 'pos', given in
     * geographic (lon, lat) coordinates.
     * 
     * @param objPosLL - double[] with location as {lon, lat} OR ArrayList with double[] as {x,y}
     * 
     * @return Double (as Object) value of the HSM at the location
     * 
     * @throws IOException, Exception
     * @throws ucar.ma2.InvalidRangeException
     */
    @Override
    public Object calcValue(Object objPosLL) throws IOException, InvalidRangeException {
        if (debug) System.out.println("\tStarting HSMap_NetCdF.calcValue(objPosLL)");
        double val = 0.0;
        double[] posXY = null;
        if (objPosLL instanceof double[]){
            double[] posLL = (double[]) objPosLL;
            if (debug) System.out.println("\t\tposLL[] = {"+posLL[0]+", "+posLL[1]+"}");
            //convert to Albers projected coordinate system
            posXY = AlbersNAD83.transformGtoP(posLL);
        } else if (objPosLL instanceof ArrayList){
            ArrayList arrList = (ArrayList) objPosLL;
            //get Albers projected coordinates
            posXY = (double[])arrList.get(0);
        } else {
            String msg = "Error: Expected a double[] or ArrayList but got "+objPosLL.getClass().getName();
            throw(new Error(msg));
        }
        if (debug) System.out.println("\t\tposXY[] = {"+posXY[0]+", "+posXY[1]+"}");
       
         //convert to decimal array indices
        double xPos = (posXY[0]-xul)/csz;
        double yPos = (yul-posXY[1])/csz;
        if (debug) System.out.println("\t\txPos, yPos = {"+xPos+", "+yPos+"}");
        
        //convert to nearest-neighbor integer array indices
        int Ir = (int) Math.round(xPos);
        int Jr = (int) Math.round(yPos);
        if (debug) System.out.println("\t\tIr, Jr = {"+Ir+", "+Jr+"}");
        
        //Check if position is outside the grid and return 0 if true
        if ((Ir<0)||(Ir>(nx-1))||(Jr<0)||(Jr>(ny-1))) {
            if (debug) System.out.println("\t\t--Position is outside grid!!--");
            return val;//returning 0.0
        }
        
        //location is inside the grid, so constinue
        int i1 = Math.min(Math.max(Ir  ,0),nx-1);
        int j1 = Math.min(Math.max(Jr  ,0),ny-1);
        if (debug) System.out.println("\t\ti1, j1 = {"+i1+", "+j1+"}");
        
        //extract associated Alaska Albers coordinates
        int[] shpxy = new int[]{1};
        int[] orgx  = new int[]{i1};
        if (debug) System.out.println("xps.read(i1,1) has size "+x.read(orgx,shpxy).getSize());
        double xv = x.read(orgx,shpxy).getDouble(0);
        int[] orgy = new int[]{j1};
        if (debug) System.out.println("yps.read(j1,1) has size "+y.read(orgy,shpxy).getSize());
        double yv = y.read(orgy,shpxy).getDouble(0);
        
        if (debug) System.out.println("\t\txv, yv  = {"+xv+", "+yv+"}");
        if (debug) System.out.println("\t\toffsets x,y = {"+(posXY[0]-xv)+", "+(posXY[1]-yv)+"}");
        
        int[] shp = new int[]{1,1};
        int[] org = new int[]{j1,i1};
        if (debug) System.out.println("shp = "+shp[0]+", "+shp[1]);
        if (debug) System.out.println("org = "+org[0]+", "+org[1]);
        ucar.ma2.Array arr = hsm.read(org,shp);
        if (debug) System.out.println("hsm.read(org,shp) has size "+arr.getSize());

        val = arr.getDouble(0);
        if (debug) System.out.println("\t\t\tval = "+val);

        if (Double.isNaN(val)) {
            logger.info("HSMap_NetCDF: interpolated NaN value\n"
                              +"\tpos[]      = "+posXY[0]+", "+posXY[1]+"\n"
                              +"\txPos, yPos = "+xPos+", "+yPos+"\n"
                              +"\ti1, j1     = "+i1+", "+j1);
        }
        if (debug) System.out.println("\tFinished HSMap_NetCdF.calcValue(objPosLL)");
        return val;
    }

    @Override
    public Object interpolateValue(Object objPosLL) {
        double[] val = new double[]{-1.0,0.0,0.0};
        return val;
    }    
}
