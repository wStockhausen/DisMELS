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
    String conn = null;
    /** the netcdf datset object */
    NetcdfDataset ds = null;
    /** number of cells in x direction */
    int nx;
    /** number of cells in y direction */
    int ny;
    /** cell size (m) */
    double csz; 
    /** x-coordinates lower left corner */
    double xll;
    /** y-coordinates lower left corner */
    double yll;
    /** x coordinates of raster (m) as netcdf Variable */
    Variable xps = null;
    /** y coordinates of raster (m) as netcdf Variable */
    Variable yps = null;
    /** habitat suitability values as netcdf Variable */
    Variable hsm = null;
    
    /** flag indicating active connection */
    boolean isConnected = false;
    
    Index idx;
    
    private static final Logger logger = Logger.getLogger(HSM_NetCDF.class.getName());
    
    public HSM_NetCDF(){
        
    }
    
    @Override
    public String getConnectionString() {
        return(conn);
    }

    @Override
    public boolean setConnectionString(String conn) throws IOException {
        try{
            if (ds!=null) {ds.close(); ds=null; isConnected=false;}//close old connection
            ds = NetcdfDataset.openDataset(conn);
            nx  = ds.findDimension("x").getLength();
            ny  = ds.findDimension("y").getLength();
            xll = ds.findVariable("xll").readScalarDouble();
            yll = ds.findVariable("yll").readScalarDouble();
            csz = ds.findVariable("cellsize").readScalarDouble();
            xps = ds.findVariable("xps");
            yps = ds.findVariable("yps");
            hsm = ds.findVariable("hsm");
            isConnected = true;
            return isConnected;
        } catch (IOException ex) {
            logger.severe("\n\tCould not set connection string \n\t\t'"+conn+"'");
            throw(ex);
        }
    }
    
    public int getNX(){return nx;}
    public int getNY(){return ny;}
    public double getXLL(){return xll;}
    public double getYLL(){return yll;}
    public double getCellSize(){return csz;}

    /**
     * Checks if instance is connected to an HSMap.
     * 
     * @return true if connected
     */
    @Override
    public boolean isConnected(){return isConnected;}
    
    /**
     * Calculate value of the HSM at position 'pos', given in
     * geographic (lon, lat) coordinates.
     * 
     * @param objPosLL - double[] with location as {lon, lat} as Object
     * 
     * @return Object (Double) reflecting value(s) of HSM
     * 
     * @throws IOException, Exception
     * @throws ucar.ma2.InvalidRangeException
     */
    @Override
    public Object calcValue(Object objPosLL) throws IOException, InvalidRangeException {
        System.out.println("\tStarting HSMap_NetCdF.calcValue(objPosLL)");
        double val = -1.0;
        double[] posXY = null;
        if (objPosLL instanceof double[]){
            double[] posLL = (double[]) objPosLL;
            System.out.println("\t\tposLL[] = {"+posLL[0]+", "+posLL[1]+"}");
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
        System.out.println("\t\tposXY[] = {"+posXY[0]+", "+posXY[1]+"}");
       
         //convert to decimal array indices
        double xPos = (posXY[0]-xll)/csz;
        double yPos = (posXY[1]-yll)/csz;
        System.out.println("\t\txPos, yPos = {"+xPos+", "+yPos+"}");
        
        //convert to nearest-neighbor integer array indices
        int Ir = (int) Math.round(xPos)+1;
        int Jr = (int) Math.round(yPos)+1;
        System.out.println("\t\tIr, Jr = {"+Ir+", "+Jr+"}");
        
        int i1 = Math.min(Math.max(Ir  ,1),nx);
        int j1 = Math.min(Math.max(Jr  ,1),ny);
        System.out.println("\t\ti1, j1 = {"+i1+", "+j1+"}");
        
        //extract associated Alaska Albers coordinates
        int[] shpxy = new int[]{1};
        int[] orgx  = new int[]{i1-1};
        System.out.println("xps.read(i1,1) has size "+xps.read(orgx,shpxy).getSize());
        double xpsv = xps.read(orgx,shpxy).getDouble(0);
        int[] orgy = new int[]{j1-1};
        System.out.println("yps.read(j1,1) has size "+yps.read(orgy,shpxy).getSize());
        double ypsv = yps.read(orgy,shpxy).getDouble(0);
        
        System.out.println("\t\txpsv, ypsv  = {"+xpsv+", "+ypsv+"}");
        System.out.println("\t\toffsets x,y = {"+(posXY[0]-xpsv)+", "+(posXY[1]-ypsv)+"}");
        
        int[] shp = new int[]{1,1};
        int[] org = new int[]{j1-1,i1-1};
        System.out.println("shp = "+shp[0]+", "+shp[1]);
        System.out.println("org = "+org[0]+", "+org[1]);
        ucar.ma2.Array arr = hsm.read(org,shp);
        System.out.println("hsm.read(org,shp) has size "+arr.getSize());

        val = arr.getDouble(0);
        System.out.println("\t\t\tval = "+val);

        if (Double.isNaN(val)) {
            logger.info("HSMap_NetCDF: interpolated NaN value\n"
                              +"\tpos[]      = "+posXY[0]+", "+posXY[1]+"\n"
                              +"\txPos, yPos = "+xPos+", "+yPos+"\n"
                              +"\ti1, j1     = "+i1+", "+j1);
        }
        System.out.println("\tFinished HSMap_NetCdF.calcValue(objPosLL)");
        return val;
    }

    @Override
    public Object interpolateValue(Object objPosLL) {
        double[] val = new double[]{-1.0,0.0,0.0};
        return val;
    }    
}
