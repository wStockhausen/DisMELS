/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.HSMs;

import java.io.IOException;
import java.util.logging.Logger;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

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

    /**
     * Checks if instance is connected to an HSMap.
     * 
     * @return true if connected
     */
    @Override
    public boolean isConnected(){return isConnected;}
    
    /**
     * Calculate value of the HSM at position 'pos'.
     * 
     * @param pos
     * @return Object reflecting value(s) of HSM
     * 
     * @throws IOException, Exception
     * @throws ucar.ma2.InvalidRangeException
     */
    @Override
    public Object calcValue(double[] pos) throws IOException, InvalidRangeException {
        System.out.println("\tStarting HSMap_NetCdF.calcValue(pos)");
        double val = -1.0;
        double xPos = (pos[0]-xll)/csz;
        double yPos = (pos[1]-yll)/csz;
        System.out.println("\t\tpos[] = {"+pos[0]+", "+pos[1]+"}");
        
        int Ir = (int) Math.round(xPos)+1;
        int Jr = (int) Math.round(yPos)+1;
        System.out.println("\t\tIr, Jr = {"+Ir+", "+Jr+"}");
        
        int i1 = Math.min(Math.max(Ir  ,1),nx);
        int j1 = Math.min(Math.max(Jr  ,1),ny);
        System.out.println("\t\ti1, j1 = {"+i1+", "+j1+"}");
        
        int[] shpxy = new int[]{1};
        int[] orgx  = new int[]{i1-1};
        System.out.println("xps.read(i1,1) has size "+xps.read(orgx,shpxy).getSize());
        double xpsv = xps.read(orgx,shpxy).getDouble(0);
        int[] orgy = new int[]{j1-1};
        System.out.println("yps.read(j1,1) has size "+yps.read(orgy,shpxy).getSize());
        double ypsv = yps.read(orgy,shpxy).getDouble(0);
        
        System.out.println("\t\txpsv, ypsv  = {"+xpsv+", "+ypsv+"}");
        System.out.println("\t\toffsets x,y = {"+(pos[0]-xpsv)+", "+(pos[1]-ypsv)+"}");
        
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
                              +"\tpos[]      = "+pos[0]+", "+pos[1]+"\n"
                              +"\txPos, yPos = "+xPos+", "+yPos+"\n"
                              +"\ti1, j1     = "+i1+", "+j1);
        }
        System.out.println("\tFinished HSMap_NetCdF.calcValue(pos)");
        return val;
    }

    /**
     * Calculate value and gradient of the HSM at position 'pos'.
     * 
     * @param pos
     * @param xtra if xtra is NOT null, the value of the HSM is linearly interpolated,
     * otherwise it is taken from the nearest neighbor.
     * 
     * @return Object (Double) reflecting value of HSM
     */
    @Override
    public Object calcValue(double[] pos, Object xtra) {
        double[] val = new double[]{-1.0,0.0,0.0};
//        try {
//            double xPos = (pos[0]-xll)/csz;
//            double yPos = (pos[1]-yll)/csz;
//            int Ir = (int) Math.floor(xPos)+1;
//            int Jr = (int) Math.floor(yPos)+1;
//            
//            int i1 = Math.min(Math.max(Ir  ,1),nx);
//            int i2 = Math.min(Math.max(Ir+1,2),nx);
//            int j1 = Math.min(Math.max(Jr  ,1),ny);
//            int j2 = Math.min(Math.max(Jr+1,2),ny);
//
//            double p2 = ((double)(i2-i1))*(xPos-(double) i1);
//            double q2 = ((double)(j2-j1))*(yPos-(double) j1);
//            double p1 = ((double) 1) - p2;
//            double q1 = ((double) 1) - q2;
//
//            int[] shp = new int[]{y+1,x+1};
//            int[] origin = new int[]{y-1,x-1};
//            ucar.ma2.Array a = hsm.read(origin,shp);
//            
//            double v11 = md.getValue(i1,j1); v11 = Double.isNaN(v11) ? 0.0 : v11;
//            double v21 = md.getValue(i2,j1); v21 = Double.isNaN(v21) ? 0.0 : v21;
//            double v12 = md.getValue(i1,j2); v12 = Double.isNaN(v12) ? 0.0 : v12;
//            double v22 = md.getValue(i2,j2); v22 = Double.isNaN(v22) ? 0.0 : v22;
//
//            cff1 =  p1*q1*m11*v11+
//                    p2*q1*m21*v21+
//                    p1*q2*m12*v12+
//                    p2*q2*m22*v22;
//
//            cff2 =  p1*q1*m11+
//                    p2*q1*m21+
//                    p1*q2*m12+
//                    p2*q2*m22;
//
//            if (cff2>0.0) {
//                v = cff1/cff2;
//            } else {
//                v = 0.0;
//            }
//
//            if (Double.isNaN(val)) {
//                logger.info("HSM_NetCDF: interpolated NaN value\n"
//                                  +"\tvalues were "+v12+", "+v22+"\n"
//                                  +"\t            "+v11+", "+v21);
//            }
//        } catch (IOException ex){
//            Exceptions.printStackTrace(ex);
//        } catch (InvalidRangeException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        return val;
    }    
}
