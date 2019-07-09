/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.HSMs;

import java.io.IOException;
import java.util.logging.Logger;
import org.openide.util.Exceptions;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Group;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import wts.roms.model.Array;

/**
 *
 * @author William Stockhausen
 */
public class HSMap_NetCDF implements HSMapInterface {

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
    /** hsm */
    Variable hsm = null;
    
    Index idx;
    
    private static final Logger logger = Logger.getLogger(HSMap_NetCDF.class.getName());
    
    public HSMap_NetCDF(){
        
    }
    
    @Override
    public String getConnectionString() {
        return(conn);
    }

    @Override
    public boolean setConnectionString(String conn) {
        try{
            ds = NetcdfDataset.openDataset(conn);
            nx  = ds.findDimension("x").getLength();
            ny  = ds.findDimension("y").getLength();
            xll = ds.findVariable("xll").readScalarDouble();
            yll = ds.findVariable("yll").readScalarDouble();
            csz = ds.findVariable("cellsize").readScalarDouble();
            hsm = ds.findVariable("hsm");
            return true;
        } catch (IOException ex) {
            //TOO: do something here!
        }
        return false;
    }

    /**
     * Calculate value of the HSM at position 'pos' (extracts nearest neighbor).
     * 
     * @param pos
     * @return Object reflecting value(s) of HSM
     */
    @Override
    public Object calcValue(double[] pos) {
        double val = -1.0;
        try {
            double xPos = (pos[0]-xll)/csz;
            double yPos = (pos[1]-yll)/csz;
            int Ir = (int) Math.floor(xPos)+1;
            int Jr = (int) Math.floor(yPos)+1;
            
            int i1 = Math.min(Math.max(Ir  ,1),nx);
            int i2 = Math.min(Math.max(Ir+1,2),nx);
            int j1 = Math.min(Math.max(Jr  ,1),ny);
            int j2 = Math.min(Math.max(Jr+1,2),ny);

            int[] shp = new int[]{j1,i1};
            ucar.ma2.Array arr = hsm.read(shp,shp);
            
            val = arr.getDouble(1);
            
            if (Double.isNaN(val)) {
                logger.info("HSMap_NetCDF: interpolated NaN value\n"
                                  +"\tpos[]      = "+pos[0]+", "+pos[1]+"\n"
                                  +"\txPos, yPos = "+xPos+", "+yPos+"\n"
                                  +"\ti1, j1     = "+i1+", "+j1);
            }
        } catch (IOException ex){
            Exceptions.printStackTrace(ex);
        } catch (InvalidRangeException ex) {
            Exceptions.printStackTrace(ex);
        }
        return val;
    }

    /**
     * Calculate value of the HSM at position 'pos' by interpolation.
     * 
     * @param pos
     * @param xtra
     * 
     * @return Object (Double) reflecting value of HSM
     */
    @Override
    public Object calcValue(double[] pos, Object xtra) {
        double val = -1.0;
        try {
            double xPos = (pos[0]-xll)/csz;
            double yPos = (pos[1]-yll)/csz;
            int Ir = (int) Math.floor(xPos)+1;
            int Jr = (int) Math.floor(yPos)+1;
            
            int i1 = Math.min(Math.max(Ir  ,1),nx);
            int i2 = Math.min(Math.max(Ir+1,2),nx);
            int j1 = Math.min(Math.max(Jr  ,1),ny);
            int j2 = Math.min(Math.max(Jr+1,2),ny);

            double p2 = ((double)(i2-i1))*(xPos-(double) i1);
            double q2 = ((double)(j2-j1))*(yPos-(double) j1);
            double p1 = ((double) 1) - p2;
            double q1 = ((double) 1) - q2;

            int[] shp = new int[]{y+1,x+1};
            int[] origin = new int[]{y-1,x-1};
            ucar.ma2.Array a = hsm.read(origin,shp);
            
            double v11 = md.getValue(i1,j1); v11 = Double.isNaN(v11) ? 0.0 : v11;
            double v21 = md.getValue(i2,j1); v21 = Double.isNaN(v21) ? 0.0 : v21;
            double v12 = md.getValue(i1,j2); v12 = Double.isNaN(v12) ? 0.0 : v12;
            double v22 = md.getValue(i2,j2); v22 = Double.isNaN(v22) ? 0.0 : v22;

            cff1 =  p1*q1*m11*v11+
                    p2*q1*m21*v21+
                    p1*q2*m12*v12+
                    p2*q2*m22*v22;

            cff2 =  p1*q1*m11+
                    p2*q1*m21+
                    p1*q2*m12+
                    p2*q2*m22;

            if (cff2>0.0) {
                v = cff1/cff2;
            } else {
                v = 0.0;
            }

            if (Double.isNaN(val)) {
                logger.info("HSMap_NetCDF: interpolated NaN value\n"
                                  +"\tvalues were "+v12+", "+v22+"\n"
                                  +"\t            "+v11+", "+v21);
            }
        } catch (IOException ex){
            Exceptions.printStackTrace(ex);
        } catch (InvalidRangeException ex) {
            Exceptions.printStackTrace(ex);
        }
        return val;
    }    
}
