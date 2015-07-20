/*
 * Interpolator2D.java
 *
 * Created on December 22, 2005, 2:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.model;

import java.util.logging.Logger;

/**
 *
 * @author William Stockhausen
 */
public class Interpolator2D {
    
    /** flag to turn on debugging info */
    public static boolean debug = false;
    /** flag to interpolate values */
    public static int INTERP_VAL   = 0;
    /** flag to interpolate slopes */
    public static int INTERP_SLOPE = 1;

    GlobalInfo globalInfo;
    
    ModelData md;
    ModelData mask;
    
    int[]  shp;
    double v, xPos, yPos;
    int    interpType;
    int    i1,i2,j1,j2,Lm,Mm;
    int    m11,m21,m12,m22;
    double p1,p2,q1,q2;
    double s111,s211,s121,s221;
    double cff1,cff2;
    
//    int Ir,Iu,Jr,Jv;
//    int Irn,Jrn;
    
//    boolean halo;

    /** logger for class */
    private static final Logger logger = Logger.getLogger(Interpolator2D.class.getName());
    
    /** 
     * Creates a new instance of Interpolator2D 
     */
    protected Interpolator2D() {
        globalInfo = GlobalInfo.getInstance();
    }
    
    /**
     * Returns the model grid (via the GlobalInfo).
     * 
     * @return -- ModelGrid2D object
     */
    public ModelGrid2D getGrid(){
        return globalInfo.getGrid();
    }
    
    protected void initializeParameters() {
//        halo=false;
//        Ir=Iu=Irn=i1=i2=Lm=-1;
//        Jr=Jv=Jrn=j1=j2=Mm=-1;
        i1=i2=Lm=-1;
        j1=j2=Mm=-1;
        p1=p2=q1=q2=0.0;
        m11=m21=m12=m22=1;//set mask values to 1
        cff1=cff2=0.0;
        s111=s211=s121=s221=(double) 1;
    }
    
//    protected boolean isHalo() throws ArrayIndexOutOfBoundsException {
//        if (mask==null) return false;//no halo
//        //otherwise test for halo
//        halo = false;
//        Irn = (int) Math.round(xPos);
//        Jrn = (int) Math.round(yPos);
//        //missing EW_PERIODIC
//        //missing NS_PERIODIC
//
//        if (mask.getValue(Irn,Jrn)<0.5) {
//            halo = true;
//        } else if ((Ir<Irn)&&(mask.getValue(Irn-1,Jrn)<0.5)) {
//            halo = true;
//        } else if ((Ir==Irn)&&(mask.getValue(Irn+1,Jrn)<0.5)) {
//            halo = true;
//        } else if ((Jr<Jrn)&&(mask.getValue(Irn,Jrn-1)<0.5)) {
//            halo = true;
//        } else if ((Jr==Jrn)&&(mask.getValue(Irn,Jrn+1)<0.5)) {
//            halo = true;
//        } else if ((Ir<Irn)&&(Jr<Jrn)&&(mask.getValue(Irn-1,Jrn-1)<0.5)) {
//            halo = true;
//        } else if ((Ir==Irn)&&(Jr<Jrn)&&(mask.getValue(Irn+1,Jrn-1)<0.5)) {
//            halo = true;
//        } else if ((Ir<Irn)&&(Jr==Jrn)&&(mask.getValue(Irn-1,Jrn+1)<0.5)) {
//            halo = true;
//        } else if ((Ir==Irn)&&(Jr==Jrn)&&(mask.getValue(Irn+1,Jrn+1)<0.5)) {
//            halo = true;
//        }
//        return halo;
//    }
    
    /**
     * Interpolates the value of a (2D) model field (with appropriate mask) to the given position.
     * @param pos        -- position vector [x,y,z] in grid units (0<x<L,0<y<M,0<z<N)
     * @param modelField -- name of model field to interpolate
     * 
     * @return value of field at given position or NaN
     */
    public double interpolateValue(double[] pos, 
                                     String modelField) throws ArrayIndexOutOfBoundsException {
        String maskField = GlobalInfo.getInstance().getMaskForField(modelField);
        return interpolateValue(pos,modelField,maskField, INTERP_VAL);
    }

    /**
     * Interpolates a 2D model field (with appropriate mask) to the given position.
     * @param pos -- position vector [x,y] in grid units (0<x<L,0<y<M,0<z<N)
     * @param modelField -- name of model field to interpolate
     * @param interpType -- flag to interpolate values directly (=INTERP_VAL) or 
     *                      to interpolate "slopes" (=INTERP_SLOPE)
     * @return value of field at given position or NaN
     */
    public double interpolateValue(double[] pos, 
                                     String modelField, 
                                     int interpType) throws ArrayIndexOutOfBoundsException {
        String maskField = GlobalInfo.getInstance().getMaskForField(modelField);
        return interpolateValue(pos, modelField, maskField, interpType);
    }
    /**
     * Interpolates a 2D model field (with appropriate mask) to the given position.
     * @param pos -- position vector [x,y] in grid units (0<x<L,0<y<M,0<z<N)
     * @param modelField -- name of model field to interpolate
     * @param maskField -- name of mask field for interpolation
     * @param interpType -- flag to interpolate values directly (=INTERP_VAL) or 
     *                      to interpolate "slopes" (=INTERP_SLOPE)
     * @return value of field at given position or NaN
     */
    public double interpolateValue(double[] pos, 
                                     String modelField, 
                                     String maskField,
                                     int interpType) throws ArrayIndexOutOfBoundsException {
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        ModelData mdp = grid.getGridField(modelField);
        MaskData  mkp = grid.getGridMask(maskField);
                    
        //interpolate field
        double val = interpolateValue2D(pos,mdp,mkp,interpType);
        return val;
    }
    
    /**
     * Interpolates a 2D model field (with appropriate mask) to the given position.
     * @param pos position vector [x,y] in grid units (0<x<L,0<y<M)
     * @param mdp model field to interpolate
     * @param maskp mask for field
     * @param interpType
     * @return value of field at given position
     */
    public double interpolateValue2D(double[] pos, 
                                     ModelData mdp, 
                                     ModelData maskp,
                                     int interpType) throws ArrayIndexOutOfBoundsException {
        //set input variables
        md   = mdp;
        mask = maskp;
        shp  = md.getShape();
        
        xPos = pos[0];
        yPos = pos[1];
        this.interpType = interpType;
        
        //initialize values
        initializeParameters();
        //get max ROMS indices along xi and eta directions
        Lm = md.getLm();
        Mm = md.getMm();
        //interpolate value using appropriate grid
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_RHO) 
            {interpOnRhoCells();} else
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_U)   
            {interpOnUCells();} else
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V)   
            {interpOnVCells();} 
            
        if (debug) debug();
        
        //"release" inputs to prevent memory leaks
        md   = null;
        mask = null;
        shp  = null;
        return v;
    }

    private void interpOnRhoCells() throws ArrayIndexOutOfBoundsException {
        int Ir = (int) Math.floor(xPos);
        int Jr = (int) Math.floor(yPos);

        i1 = Math.min(Math.max(Ir  ,0),Lm+1);
        i2 = Math.min(Math.max(Ir+1,1),Lm+1);
        j1 = Math.min(Math.max(Jr  ,0),Mm+1);
        j2 = Math.min(Math.max(Jr+1,1),Mm+1);

        p2 = ((double)(i2-i1))*(xPos-(double) i1);
        q2 = ((double)(j2-j1))*(yPos-(double) j1);
        p1 = ((double) 1) - p2;
        q1 = ((double) 1) - q2;

        if (mask!=null) {
            m11 = (int) Math.round(mask.getValue(i1,j1));
            m21 = (int) Math.round(mask.getValue(i2,j1));
            m12 = (int) Math.round(mask.getValue(i1,j2));
            m22 = (int) Math.round(mask.getValue(i2,j2));
        }

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

        if (Double.isNaN(v)) {
            logger.info("Interpolator2D: interpolated rho-cell NaN value\n"
                              +"\tmask was "+m12+", "+m22+"\n"
                              +"\t         "+m11+", "+m22+"\n"
                              +"\tvalues were "+v12+", "+v22+"\n"
                              +"\t            "+v11+", "+v21);
        }
    }

    /**
     * New halo formulation.
     * @throws ArrayIndexOutOfBoundsException
     */
    private void interpOnUCells() throws ArrayIndexOutOfBoundsException {
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        int Iu = (int) Math.floor(xPos+0.5);
        int Ju = (int) Math.floor(yPos);

        i1 = Math.min(Math.max(Iu  ,1),Lm+1);
        i2 = Math.min(Math.max(Iu+1,1),Lm+1);
        j1 = Math.min(Math.max(Ju  ,0),Mm+1);
        j2 = Math.min(Math.max(Ju+1,0),Mm+1);

        p2 = ((double)(i2-i1))*(xPos-(double)i1+0.5);
        q2 = ((double)(j2-j1))*(yPos-(double)j1);
        p1 = ((double) 1) - p2;
        q1 = ((double) 1) - q2;

        if (interpType==INTERP_SLOPE) {
            //TODO: check for correct java indices!!
            s111 = 0.5*(grid.getPM(i1-1,j1)+grid.getPM(i1,j1));
            s211 = 0.5*(grid.getPM(i2-1,j1)+grid.getPM(i2,j1));
            s121 = 0.5*(grid.getPM(i1-1,j2)+grid.getPM(i1,j2));
            s221 = 0.5*(grid.getPM(i2-1,j2)+grid.getPM(i2,j2));
        }

        if (mask!=null) {
            m11 = (int) Math.round(mask.getValue(i1,j1));
            m21 = (int) Math.round(mask.getValue(i2,j1));
            m12 = (int) Math.round(mask.getValue(i1,j2));
            m22 = (int) Math.round(mask.getValue(i2,j2));
        }

        double v11 = md.getValue(i1,j1); v11 = Double.isNaN(v11) ? 0.0 : v11;
        double v21 = md.getValue(i2,j1); v21 = Double.isNaN(v21) ? 0.0 : v21;
        double v12 = md.getValue(i1,j2); v12 = Double.isNaN(v12) ? 0.0 : v12;
        double v22 = md.getValue(i2,j2); v22 = Double.isNaN(v22) ? 0.0 : v22;

        if ((m11+m21+m12+m22)<4) {//at least one of the corners is masked
            if (q2<=0.5) {//interpolating in "lower" half of grid cell
                v = p1*s111*m11*v11+p2*s211*m21*v21;//linearly interpolate (v=0 on mask)
            } else {      //interpolating in "upper" half of grid cell
                v = p1*s121*m12*v12+p2*s221*m22*v22;//linearly interpolate (v=0 on mask)
            }
        } else {
            v = p1*q1*s111*v11+
                p2*q1*s211*v21+
                p1*q2*s121*v12+
                p2*q2*s221*v22;
        }

        if (Double.isNaN(v)) {
            logger.info("Interpolator2D: interpolated U-cell NaN value\n"
                              +"\tmask was "+m12+", "+m22+"\n"
                              +"\t         "+m11+", "+m22+"\n"
                              +"\tvalues were "+v12+", "+v22+"\n"
                              +"\t            "+v11+", "+v21);
        }
    }

    private void interpOnVCells() throws ArrayIndexOutOfBoundsException {
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        int Iv = (int) Math.floor(xPos);
        int Jv = (int) Math.floor(yPos+0.5);
        
        i1 = Math.min(Math.max(Iv  ,0),Lm+1);
        i2 = Math.min(Math.max(Iv+1,0),Lm+1);
        j1 = Math.min(Math.max(Jv  ,1),Mm+1);
        j2 = Math.min(Math.max(Jv+1,1),Mm+1);

        p2 = ((double)(i2-i1))*(xPos-(double)i1);
        q2 = ((double)(j2-j1))*(yPos-(double)j1+0.5);
        p1 = ((double) 1) - p2;
        q1 = ((double) 1) - q2;

        if (interpType==INTERP_SLOPE) {
            //TODO: check for correct java indices!!
            s111 = 0.5*(grid.getPN(i1,j1-1)+grid.getPN(i1,j1));
            s211 = 0.5*(grid.getPN(i2,j1-1)+grid.getPN(i2,j1));
            s121 = 0.5*(grid.getPN(i1,j2-1)+grid.getPN(i1,j2));
            s221 = 0.5*(grid.getPN(i2,j2-1)+grid.getPN(i2,j2));
        }

        if (mask!=null) {
            m11 = (int) Math.round(mask.getValue(i1,j1));
            m21 = (int) Math.round(mask.getValue(i2,j1));
            m12 = (int) Math.round(mask.getValue(i1,j2));
            m22 = (int) Math.round(mask.getValue(i2,j2));
        }

        double v11 = md.getValue(i1,j1); v11 = Double.isNaN(v11) ? 0.0 : v11;
        double v21 = md.getValue(i2,j1); v21 = Double.isNaN(v21) ? 0.0 : v21;
        double v12 = md.getValue(i1,j2); v12 = Double.isNaN(v12) ? 0.0 : v12;
        double v22 = md.getValue(i2,j2); v22 = Double.isNaN(v22) ? 0.0 : v22;

        if ((m11+m21+m12+m22)<4) {//at least one of the corners is masked
            if (i2<=0.5) {//interpolating in "left" half of grid cell
                v = q1*s111*m11*v11+q2*s121*m12*v12;//linearly interpolate (v=0 on mask)
            } else {      //interpolating in "right" half of grid cell
                v = q1*s211*m21*v21+q2*s221*m22*v22;//linearly interpolate (v=0 on mask)
            }
        } else {
            v = p1*q1*s111*md.getValue(i1,j1)+
                p2*q1*s211*md.getValue(i2,j1)+
                p1*q2*s121*md.getValue(i1,j2)+
                p2*q2*s221*md.getValue(i2,j2);
        }

        if (Double.isNaN(v)) {
            logger.info("Interpolator2D: interpolated V-cell NaN value\n"
                              +"\tmask was "+m12+", "+m22+"\n"
                              +"\t         "+m11+", "+m22+"\n"
                              +"\tvalues were "+v12+", "+v22+"\n"
                              +"\t            "+v11+", "+v21);
        }
    }

    /**
     * Calculate the bathymetric depth (h>0) corresponding to 
     * the grid position vector pos.
     *
     * @param pos -- position vector in grid units (0<I<L,0<J<M)
     * @return interpolated bathymetric depth.
     */
    public double interpolateBathymetricDepth(double[] pos) throws ArrayIndexOutOfBoundsException {
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        v = interpolateValue2D(pos,grid.h,null,INTERP_VAL);
        if (Double.isNaN(v)) {
            throw new java.lang.UnknownError("Found a NaN in interpolateBathymetricDepth "+pos[0]+", "+pos[1]+", "+pos[2]);
        }
        return v;
    }
    
    /**
     * Calculate the latitude corresponding to the grid position vector pos.
     * 
     * @param pos -- position vector in grid units (0<I<L,0<J<M)
     * @return interpolated latitude (or NaN if model grid not spherical).
     */
    public double interpolateLat(double[] pos) throws ArrayIndexOutOfBoundsException {
        v = Double.NaN;
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        if (grid.isSpherical()) v = interpolateValue2D(pos,grid.lat_rho,null,INTERP_VAL);
        if (Double.isNaN(v)) {
            throw new java.lang.UnknownError("Found a NaN in interpolateLat "+pos[0]+", "+pos[1]+", "+pos[2]);
        }
        return v;
    }
    
    /**
     * Calculate the longitude corresponding to the grid position vector pos.
     * 
     * @return interpolated longitude relative to NAD83 (range -180,180)
     *      (or NaN if model grid not spherical).
     *
     * @param pos -- position vector in grid units (0<I<L,0<J<M)
     */
    public double interpolateLon(double[] pos) throws ArrayIndexOutOfBoundsException {
        v = Double.NaN;
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        if (grid.isSpherical()) v = interpolateValue2D(pos,grid.lon_rho,null,INTERP_VAL);
        if (v>180) v=v-360;
        if (Double.isNaN(v)) {
            throw new java.lang.UnknownError("Found a NaN in interpolateLon "+pos[0]+", "+pos[1]+", "+pos[2]);
        }
        return v;
    }
    
    /**
     * Calculate the X position corresponding to the grid position vector pos.
     * @return interpolated X value.
     *
     * @param pos -- position vector in grid units (0<I<L,0<J<M)
     */
    public double interpolateX(double[] pos) throws ArrayIndexOutOfBoundsException {
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        v = interpolateValue2D(pos,grid.x_rho,null,INTERP_VAL);
        return v;
    }
    
    /**
     * Calculate the Y position corresponding to the grid position vector pos.
     * @return interpolated longitude (or -999 if model grid not spherical).
     *
     * @param pos -- position vector in grid units (0<I<L,0<J<M)
     */
    public double interpolateY(double[] pos) throws ArrayIndexOutOfBoundsException {
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        v = interpolateValue2D(pos,grid.y_rho,null,INTERP_VAL);
        return v;
    }
    
    /**
     * Prints debug info to System.out
     */
    public void debug() {
        String cc = ",";
        String tt = "\t";
        logger.info("<<<<<<<<<<<<<<<<<<Interpolator2D>>>>>>>>>>>>>>>>>");
        logger.info("Name = "+md.getName());
        logger.info("Position is\t"+xPos+tt+yPos);
        logger.info("interpType =\t"+interpType);
        logger.info("i1,i2 =\t"+i1+tt+i2);
        logger.info("j1,j2 =\t"+j1+tt+j2);
        logger.info("p1,p2 =\t"+p1+tt+p2);
        logger.info("q1,q2 =\t"+q1+tt+q2);
        logger.info("s111,s211 =\t"+s111+tt+s211);
        logger.info("s121,s221 =\t"+s121+tt+s221);
        logger.info("v111,v211 =\t"+md.getValue(i1,j1)+tt
                                          +md.getValue(i2,j1));
        logger.info("v121,v221 =\t"+md.getValue(i1,j2)+tt
                                          +md.getValue(i2,j2));
        if (mask!=null){
            logger.info("m111,m211 =\t"+mask.getValue(i1,j1)+tt
                                              +mask.getValue(i2,j1));
            logger.info("m121,m221 =\t"+mask.getValue(i1,j2)+tt
                                              +mask.getValue(i2,j2));
            logger.info("cff1,cff2 =\t"+cff1+tt+cff2);
        }
        logger.info("v = "+v);
        logger.info("<<<<<<<<<<<<<<<<<<Interpolator2D>>>>>>>>>>>>>>>>>");
   }
    
    /**
     * Calculate the scale factor to transform a physical
     * horizontal u velocity into one relative to grid units.
     *
     * @return uScale such that u(grid units) = u(physical units)/uScale.
     * @param pos position vector in grid units (0<x<L,0<y<M,0<z<N)
     */
    public double calcUscale(double[] pos) throws ArrayIndexOutOfBoundsException {
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        int Iu = (int) Math.floor(pos[0]+0.5);
        int Ju = (int) Math.floor(pos[1]);

        int i1 = Math.min(Math.max(Iu  ,1),Lm+1);
        int i2 = Math.min(Math.max(Iu+1,1),Lm+1);
        int j1 = Math.min(Math.max(Ju  ,0),Mm+1);
        int j2 = Math.min(Math.max(Ju+1,0),Mm+1);

        double p2 = ((double)(i2-i1))*(pos[0]-(double)i1+0.5);
        double q2 = ((double)(j2-j1))*(pos[1]-(double)j1);
        double p1 = ((double) 1) - p2;
        double q1 = ((double) 1) - q2;

        //interpType==INTERP_SLOPE
        double s111 = 0.5*(grid.getPM(i1-1,j1)+grid.getPM(i1,j1));
        double s211 = 0.5*(grid.getPM(i2-1,j1)+grid.getPM(i2,j1));
        double s121 = 0.5*(grid.getPM(i1-1,j2)+grid.getPM(i1,j2));
        double s221 = 0.5*(grid.getPM(i2-1,j2)+grid.getPM(i2,j2));

        //the scaling value is
        double s = p1*q1*s111+
                   p2*q1*s211+
                   p1*q2*s121+
                   p2*q2*s221;

        if (Double.isNaN(s)) {
            logger.info("Interpolator2D: interpolated U-scale NaN value\n"
                              +"\tmask was "+m12+", "+m22+"\n"
                              +"\t         "+m11+", "+m22);
        }
        return 1.0/s;//take inverse so u_grid = u_phys/scale
    }
    
    /**
     * Calculate the scale factor to transform a physical
     * horizontal v velocity into one relative to grid units.
     * 
     * @return vScale such that v(grid units) = v(physical units)/vScale.
     * @param pos position vector in grid units (0<x<L,0<y<M,0<z<N)
     */
    public double calcVscale(double[] pos) throws ArrayIndexOutOfBoundsException {
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        int Iv = (int) Math.floor(pos[0]);
        int Jv = (int) Math.floor(pos[1]+0.5);
        
        int i1 = Math.min(Math.max(Iv  ,0),Lm+1);
        int i2 = Math.min(Math.max(Iv+1,0),Lm+1);
        int j1 = Math.min(Math.max(Jv  ,1),Mm+1);
        int j2 = Math.min(Math.max(Jv+1,1),Mm+1);

        double p2 = ((double)(i2-i1))*(pos[0]-(double)i1);
        double q2 = ((double)(j2-j1))*(pos[1]-(double)j1+0.5);
        double p1 = ((double) 1) - p2;
        double q1 = ((double) 1) - q2;

        //interpType==INTERP_SLOPE
        double s111 = 0.5*(grid.getPN(i1,j1-1)+grid.getPN(i1,j1));
        double s211 = 0.5*(grid.getPN(i2,j1-1)+grid.getPN(i2,j1));
        double s121 = 0.5*(grid.getPN(i1,j2-1)+grid.getPN(i1,j2));
        double s221 = 0.5*(grid.getPN(i2,j2-1)+grid.getPN(i2,j2));

        double s = p1*q1*s111+
                    p2*q1*s211+
                    p1*q2*s121+
                    p2*q2*s221;

        if (Double.isNaN(s)) {
            logger.info("Interpolator2D: interpolated V-cell NaN value\n"
                              +"\tmask was "+m12+", "+m22+"\n"
                              +"\t         "+m11+", "+m22);
        }
        return 1.0/s;//take inverse so v_grid = v_phys/scale
    }
    
    /**
     * Calculates the horizontal gradient of a model field and returns the results
     * in physical units (i.e., [field units]/m), not grid units.
     * 
     * @param pos
     * @param modelField
     * @param interpType
     * @return 
     */
    public double[] calcHorizGradient(double[] pos, String modelField, int interpType){
        String maskField = GlobalInfo.getInstance().getMaskForField(modelField);
        return calcHorizGradient(pos,modelField,maskField,interpType);
    }
    
    /**
     * Calculates the horizontal gradient of a model field and returns the results
     * in physical units (i.e., [field units]/m), not grid units.
     * 
     * @param pos
     * @param modelField
     * @param maskField
     * @param interpType
     * @return 
     */
    public double[] calcHorizGradient(double[] pos, String modelField, String maskField, int interpType){
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
        ModelData mdp = grid.getGridField(modelField);
        MaskData  mkp = grid.getGridMask(maskField);
                    
        return calcHorizGradient(pos, mdp, mkp, interpType);
    }
    
    /**
     * Calculates the horizontal gradient of a model field and returns the results
     * in physical units (i.e., [field units]/m), not grid units.
     * 
     * @param pos
     * @param modelField
     * @param maskField
     * @param interpType
     * @return 
     */
    public double[] calcHorizGradient(double[] pos, ModelData modelField, ModelData maskField, int interpType){
        ModelGrid2D grid = GlobalInfo.getInstance().getGrid();
//        double m00 = interpolateValue2D(new double[]{pos[0],    pos[1]},     modelField, maskField, interpType);
        double mp0 = interpolateValue2D(new double[]{pos[0]+0.1,pos[1]},     modelField, maskField, interpType);
        double mm0 = interpolateValue2D(new double[]{pos[0]-0.1,pos[1]},     modelField, maskField, interpType);
        double m0p = interpolateValue2D(new double[]{pos[0],    pos[1]+0.1}, modelField, maskField, interpType);
        double m0m = interpolateValue2D(new double[]{pos[0],    pos[1]-0.1}, modelField, maskField, interpType);
        
        double pm = interpolateValue2D(pos,grid.pm,null,Interpolator2D.INTERP_VAL);
        double pn = interpolateValue2D(pos,grid.pn,null,Interpolator2D.INTERP_VAL);
        double dmdx = 5*(mp0-mm0)*pm;// = 0.5*[(mp0-m00)/(0.1*1/pm)+(m00-mm0)/(0.1*1/pm));
        double dmde = 5*(m0p-m0m)*pn;// = 0.5*[(m0p-m00)/(0.1*1/pn)+(m00-m0m)/(0.1*1/pn));
        return new double[]{dmdx,dmde};
    }
}
