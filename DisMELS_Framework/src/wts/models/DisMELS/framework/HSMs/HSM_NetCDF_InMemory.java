/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.HSMs;

import java.io.IOException;
import java.util.logging.Logger;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.dataset.NetcdfDataset;
import wts.roms.gis.AlbersNAD83;

/**
 *
 * @author William Stockhausen
 */
public class HSM_NetCDF_InMemory implements HSM_Interface {

    /** string giving connection info (filename, url) to netcdf file */
    protected String conn = "";
    /** number of cells in x direction */
    protected  int nx;
    /** number of cells in y direction */
    protected int ny;
    /** x coordinates of the centers of the HSM raster cells (m), as a ucar.ma2.ArrayDouble.D1 */
    protected ucar.ma2.ArrayDouble.D1 x = null;
    /** y coordinates of the centers of the HSM raster cells (m), as a ucar.ma2.ArrayDouble.D1 */
    protected ucar.ma2.ArrayDouble.D1 y = null;
    /** habitat suitability values as a ucar.ma2.ArrayFloat.D2 */
    protected ucar.ma2.ArrayFloat.D2 hsm = null;
    
    /** flag indicating active connection */
    protected boolean isConnected = false;
    
        /** cell size (m) */
    protected double csz; 
    /** x-coordinate for origin of the hsm (center of upper left corner raster cell; i=0) */
    protected double xul;
    /** y-coordinate for origin of the hsm (center of upper left corner raster cell; j=0) */
    protected double yul;

    /** flag to print debugging information */
    public static boolean debug = false;
    
    /** class logger */
    private static final Logger logger = Logger.getLogger(HSM_NetCDF_InMemory.class.getName());
    
    public HSM_NetCDF_InMemory(){
        
    }
    
    public String getConnectionString() {
        return(conn);
    }

    public boolean setConnectionString(String conn) throws IOException, InvalidRangeException {
        if (conn.equals(this.conn)) {return isConnected;}
        if (conn.isEmpty()||conn.equalsIgnoreCase("")||conn.equalsIgnoreCase("<none>")){
            logger.info("No connection string specified.");
            isConnected=false;
            return isConnected;
        }
        try{
            //if we got thhis far, try to open the connection string and 
            //read data for HSM
            NetcdfDataset ds          = NetcdfDataset.openDataset(conn);
            ucar.nc2.Variable x_var   = ds.findVariable("x");
            ucar.nc2.Variable y_var   = ds.findVariable("y");
            ucar.nc2.Variable hsm_var = ds.findVariable("hsm");
            
            nx  = ds.findDimension("x").getLength();
            ny  = ds.findDimension("y").getLength();
            x   = (ucar.ma2.ArrayDouble.D1) x_var.read();
            y   = (ucar.ma2.ArrayDouble.D1) y_var.read();
            hsm = (ucar.ma2.ArrayFloat.D2) hsm_var.read();
            
            
            xul = x.get(0);    //x-coordinate for origin of hsm (center of upper left corner raster cell)
            yul = y.get(0);    //y-coordinate for origin of hsm (center of upper left corner raster cell)
            csz = x.get(1)-xul;//cell size, in m
            
            ds.close();
            
            this.conn = conn;
            isConnected = true;
            return isConnected;
        } catch (IOException ex) {
            logger.severe("HSM_NetCDF_InMemory.setConnectionString(String conn): Invalid connection string \n\t\t'"+conn+"'");
            this.conn = null;
            isConnected = false;
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
    public double calcValue(double[] objPosLL) throws IOException, InvalidRangeException {
        if (debug) System.out.println("\tStarting HSMap_NetCdF_InMemory.calcValue(objPosLL)");
        double val = 0.0;
        double[] posXY = null;
        double[] posLL = (double[]) objPosLL;
        if (debug) System.out.println("\t\tposLL[] = {"+posLL[0]+", "+posLL[1]+"}");
        //convert to Albers projected coordinate system
        posXY = AlbersNAD83.transformGtoP(posLL);
       
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
        double xv = x.get(i1);
        double yv = y.get(j1);
        
        if (debug) {
            System.out.println("\t\txv, yv  = {"+xv+", "+yv+"}");
            System.out.println("\t\toffsets x,y = {"+(posXY[0]-xv)+", "+(posXY[1]-yv)+"}");
        }
        
        val = hsm.get(j1,i1);

        if (Double.isNaN(val)) {
            logger.info("HSMap_NetCDF: interpolated NaN value\n"
                              +"\tpos[]      = "+posXY[0]+", "+posXY[1]+"\n"
                              +"\txPos, yPos = "+xPos+", "+yPos+"\n"
                              +"\ti1, j1     = "+i1+", "+j1);
        }
        if (debug) {
            System.out.println("\t\t\tval = "+val);
            System.out.println("\tFinished HSMap_NetCdF_InMemory.calcValue(objPosLL)");
        }
        return val;
    }

    @Override
    public double interpolateValue(double[] objPosLL) {
        double val = 0.0;
        return val;
    }    
}
