/*
 * Interpolator3D.java
 *
 * Created on December 2, 2005, 11:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 *
 * @author William Stockhausen
 */
public class Interpolator3D extends Interpolator2D {
    
    public static boolean debug = false;
    public static boolean interpolateLikeROMS = false;
    
    PhysicalEnvironment pe;
    double zPos;
    int    K,k1,k2,N;
    double r1,r2;
    int    khm,khp;
    double s112,s122,s212,s222;
    /** logger for class */
    private static final Logger logger = Logger.getLogger(Interpolator3D.class.getName());
    
    /**
     * Creates a new instance of Interpolator3D
     *
     */
    public Interpolator3D() {
        super();
        logger.info("Starting Interpolator3D()");
        this.pe    = null;
        if (interpolateLikeROMS) logger.info("\n\nInterploator3D:  Warning: likeOldROMS is true!!\n\n");
        logger.info("Finished Interpolator3D()");
    }

    /**
     * Creates a new instance of Interpolator3D
     *
     * @param pe--PhysicalEnvironment instance to use for interpolations
     */
    public Interpolator3D(PhysicalEnvironment pe) {
        super();
        logger.info("Starting Interpolator3D(pe)");
        this.pe    = pe;
        if (interpolateLikeROMS) logger.info("\n\nInterploator3D:  Warning: likeOldROMS is true!!\n\n");
        logger.info("Finished Interpolator3D(pe)");
    }
    
    /**
     * Returns the model grid (via the GlobalInfo).
     * 
     * @return -- ModelGrid3D object
     */
    @Override
    public ModelGrid3D getGrid(){
        return globalInfo.getGrid3D();
    }

    /**
     * Gets the PhysicalEnvironment object for interpolations.
     *
     * @return PhysicalEnvironment object
     */
    public PhysicalEnvironment getPhysicalEnvironment() {
        return pe;
    }
    
    /**
     * Sets the PhysicalEnvironment object for interpolations.
     *
     * @param PhysicalEnvironment object
     */
    public void setPhysicalEnvironment(PhysicalEnvironment pe) {
        this.pe = pe;
        if (interpolateLikeROMS) logger.info("\n\nInterploator3D:  Warning: likeOldROMS is true!!\n\n");
    }
    
    @Override
    protected void initializeParameters() {
        super.initializeParameters();
        K=k1=k2=N=-1;
        khm=khp= -1;
        r1=r2=0.0;
        s112=s122=s212=s222=(double) 1;
    }
    
    /**
     * Interpolates the value of a (3D) model field (with appropriate mask) to the given position.
     * @param pos        -- position vector [x,y,z] in grid units (0<x<L,0<y<M,0<z<N)
     * @param modelField -- name of model field to interpolate
     * 
     * @return value of field at given position or NaN
     */
    @Override
    public double interpolateValue(double[] pos, 
                                     String modelField) throws ArrayIndexOutOfBoundsException {
        String maskField = GlobalInfo.getInstance().getMaskForField(modelField);
        return interpolateValue(pos,modelField,maskField, INTERP_VAL);
    }
    
    /**
     * Interpolates a 3D model field (with appropriate mask) to the given position.
     * @param -- pos position vector [x,y,z] in grid units (0<x<L,0<y<M,0<z<N)
     * @param modelField -- name of model field to interpolate
     * @param interpType -- flag to interpolate values directly (=INTERP_VAL) or 
     *                      to interpolate "slopes" (=INTERP_SLOPE)
     * @return value of field at given position or NaN
     */
    @Override
    public double interpolateValue(double[] pos, 
                                     String modelField, 
                                     int interpType) throws ArrayIndexOutOfBoundsException {
        String maskField = GlobalInfo.getInstance().getMaskForField(modelField);
        return interpolateValue(pos,modelField,maskField, interpType);
    }
    
    /**
     * Interpolates a 3D model field (with appropriate mask) to the given position.
     * @param -- pos position vector [x,y,z] in grid units (0<x<L,0<y<M,0<z<N)
     * @param modelField -- name of model field to interpolate
     * @param modelField -- name of mask field for interpolation
     * @param interpType -- flag to interpolate values directly (=INTERP_VAL) or 
     *                      to interpolate "slopes" (=INTERP_SLOPE)
     * @return value of field at given position or NaN
     */
    @Override
    public double interpolateValue(double[] pos, 
                                     String modelField, 
                                     String maskField,
                                     int interpType) throws ArrayIndexOutOfBoundsException {
        if (pe==null) {
            throw new UnsupportedOperationException("Interpolator3D.interpolateValue(double[],String,String,int) cannot be used because pe is null!!");
        }
        //set interpolation field
        if (pos.length<3) {
            //use the function in Interpolator2D
            v = super.interpolateValue(pos, modelField, maskField, interpType);
            return v;
        }
        ModelData mdp = pe.getField(modelField);
        if (mdp.getRank()<3){
            //try 2d interpolator again
            v = super.interpolateValue(pos, modelField, maskField, interpType);
            return v;
        }
        
        MaskData  mkp = globalInfo.getGrid2D().getGridMask(maskField);
                  
        //interpolate field
        v = interpolateValue3D(pos,mdp,mkp,interpType);
        return v;
    }
        
    /**
     * Interpolates a 2D or 3D model field (with appropriate mask) to the given position.
     * If "pos" is a 2D vector, the overriden method in Interpolator2D is called
     * for the interpolation.
     * @param pos position vector [x,y,z] in grid units (0<x<L,0<y<M,0<z<N)
     * @param mdp model field to interpolate
     * @param maskp mask for field
     * @param interpType flag to interpolate values directly (=INTERP_VAL) or 
     * to interpolate "slopes" (=INTERP_SLOPE)
     * @return value of field at given position
     */
    public double interpolateValue3D(double[] pos, 
                                     ModelData mdp, 
                                     ModelData maskp,
                                     int interpType) throws ArrayIndexOutOfBoundsException {
        if (pos.length<3) {
            //use the function in Interpolator2D
            v = interpolateValue2D(pos, mdp, maskp, interpType);
            return v;
        }
        //set input variables
        md = mdp;
        mask = maskp;
        shp = md.getShape();
        
        xPos = pos[0];
        yPos = pos[1];
        zPos = pos[2];
        this.interpType = interpType;
        
        //initialize values
        initializeParameters();
        //get max ROMS index in s direction
        N = md.getN(); 
        //compute indices for vertical levels and vertical interp. factors
        if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_RHO) {
            K  = (int) Math.floor(zPos+0.5);
            k1 = Math.min(Math.max(K  ,1),N);
            k2 = Math.min(Math.max(K+1,1),N);
            r2 = ((double)(k2-k1))*(zPos+0.5-((double) k1));
        } else if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_W) {
            K  = (int) Math.floor(zPos);
            k1 = Math.min(Math.max(K  ,0),N);
            k2 = Math.min(Math.max(K+1,0),N);
            r2 = ((double)(k2-k1))*(zPos-((double) k1));
        }
        r1 = ((double) 1)-r2;
//        if (debug && halo) {
//            if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_RHO) {
//                logger.info("Vert pos type = RHO");
//            } else  if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_W) {
//                logger.info("Vert pos type = W");
//            } else {
//                logger.info("ERROR!! Unrecognized vert pos type "+md.getVertPosType());
//                logger.info("ModelData name = "+md.getName());
//                System.exit(1);
//            }
//            logger.info("K,k1,k2 = "+K+","+k1+","+k2);
//        }
        
        //get max ROMS indices along xi and eta directions
        Lm = md.getLm();
        Mm = md.getMm();
        //interpolate value using appropriate grid
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_RHO) 
            {interpOnRhoColumns(globalInfo.getGrid3D());} else
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_U)   
            {interpOnUColumns(globalInfo.getGrid3D());} else
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V)   
            {interpOnVColumns(globalInfo.getGrid3D());} 
            
//        if (debug && halo) debug();
        
        //set these to null to prevent memory leakage
        md   = null;
        mask = null;
        shp  = null;
        
        return v;
    }

    private void interpOnRhoColumns(ModelGrid3D grid) throws ArrayIndexOutOfBoundsException {
        if (debug) logger.info("Interpolating on Rho columns");
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

        int khm1 = Math.min(Math.max(k1  ,1),N);  //check for correct java indices!!
        int khp1 = Math.min(Math.max(k1+1,1),N);  //check for correct java indices!!
        int khm2 = Math.min(Math.max(k2  ,1),N);  //check for correct java indices!!
        int khp2 = Math.min(Math.max(k2+1,1),N);  //check for correct java indices!!
        ModelData Hz = pe.getField("Hz");
        if (interpType==INTERP_SLOPE) {
            //TODO: check for correct java indices!!
            s111 = 2.0*grid.getPM(i1,j1)*grid.getPN(i1,j1)/
                    (Hz.getValue(i1,j1,khm1)+Hz.getValue(i1,j1,khp1));
            s211 = 2.0*grid.getPM(i2,j1)*grid.getPN(i2,j1)/
                    (Hz.getValue(i2,j1,khm1)+Hz.getValue(i2,j1,khp1));
            s121 = 2.0*grid.getPM(i1,j2)*grid.getPN(i1,j2)/
                    (Hz.getValue(i1,j2,khm1)+Hz.getValue(i1,j2,khp1));
            s221 = 2.0*grid.getPM(i2,j2)*grid.getPN(i2,j2)/
                    (Hz.getValue(i2,j2,khm1)+Hz.getValue(i2,j2,khp1));
            s112 = 2.0*grid.getPM(i1,j1)*grid.getPN(i1,j1)/
                    (Hz.getValue(i1,j1,khm2)+Hz.getValue(i1,j1,khp2));
            s212 = 2.0*grid.getPM(i2,j1)*grid.getPN(i2,j1)/
                    (Hz.getValue(i2,j1,khm2)+Hz.getValue(i2,j1,khp2));
            s122 = 2.0*grid.getPM(i1,j2)*grid.getPN(i1,j2)/
                    (Hz.getValue(i1,j2,khm2)+Hz.getValue(i1,j2,khp2));
            s222 = 2.0*grid.getPM(i2,j2)*grid.getPN(i2,j2)/
                    (Hz.getValue(i2,j2,khm2)+Hz.getValue(i2,j2,khp2));
        }

        if (mask!=null) {
            m11 = (int) Math.round(mask.getValue(i1,j1));
            m21 = (int) Math.round(mask.getValue(i2,j1));
            m12 = (int) Math.round(mask.getValue(i1,j2));
            m22 = (int) Math.round(mask.getValue(i2,j2));
        }

        double v111 = md.getValue(i1,j1,k1); v111 = Double.isNaN(v111) ? 0.0 : v111;
        double v211 = md.getValue(i2,j1,k1); v211 = Double.isNaN(v211) ? 0.0 : v211;
        double v121 = md.getValue(i1,j2,k1); v121 = Double.isNaN(v121) ? 0.0 : v121;
        double v221 = md.getValue(i2,j2,k1); v221 = Double.isNaN(v221) ? 0.0 : v221;
        double v112 = md.getValue(i1,j1,k2); v112 = Double.isNaN(v112) ? 0.0 : v112;
        double v212 = md.getValue(i2,j1,k2); v212 = Double.isNaN(v212) ? 0.0 : v212;
        double v122 = md.getValue(i1,j2,k2); v122 = Double.isNaN(v122) ? 0.0 : v122;
        double v222 = md.getValue(i2,j2,k2); v222 = Double.isNaN(v222) ? 0.0 : v222;

        cff1 =  p1*q1*r1*m11*s111*v111+
                p2*q1*r1*m21*s211*v211+
                p1*q2*r1*m12*s121*v121+
                p2*q2*r1*m22*s221*v221+
                p1*q1*r2*m11*s112*v112+
                p2*q1*r2*m21*s212*v212+
                p1*q2*r2*m12*s122*v122+
                p2*q2*r2*m22*s222*v222;

        cff2 =  p1*q1*r1*m11+
                p2*q1*r1*m21+
                p1*q2*r1*m12+
                p2*q2*r1*m22+
                p1*q1*r2*m11+
                p2*q1*r2*m21+
                p1*q2*r2*m12+
                p2*q2*r2*m22;

        if (cff2>0.0) {
            v = cff1/cff2;
        } else {
            v = 0.0;
        }

        if (Double.isNaN(v)) {
            logger.info("Interpolator3D: interpolated rho-cell NaN value for field "+md.getName()+"\n"
                              +"\tp1 = "+p1+", q1 = "+q1+", r1 = "+r1+"\n"
                              +"\tp2 = "+p2+", q2 = "+q2+", r2 = "+r2+"\n"
                              +"\tmask was   "+m12+", "+m22+"\n"
                              +"\t           "+m11+", "+m22+"\n"
                              +"\tPM         "+grid.getPM(i1,j2)+", "+grid.getPM(i2,j2)+"\n"
                              +"\t           "+grid.getPM(i1,j1)+", "+grid.getPM(i2,j1)+"\n"
                              +"\tPN         "+grid.getPN(i1,j2)+", "+grid.getPN(i2,j2)+"\n"
                              +"\t           "+grid.getPN(i1,j1)+", "+grid.getPN(i2,j1)+"\n"
                              +"\tupper Hz   "+Hz.getValue(i1,j2,khp2)+", "+Hz.getValue(i1,j2,khp2)+"\n"
                              +"\t           "+Hz.getValue(i1,j1,khp2)+", "+Hz.getValue(i2,j2,khp2)+"\n"
                              +"\tmiddle Hz  "+Hz.getValue(i1,j2,khp1)+", "+Hz.getValue(i1,j2,khp1)+"\n"
                              +"\t           "+Hz.getValue(i1,j1,khp1)+", "+Hz.getValue(i2,j2,khp1)+"\n"
                              +"\tupper Hz   "+Hz.getValue(i1,j2,khm1)+", "+Hz.getValue(i1,j2,khm1)+"\n"
                              +"\t           "+Hz.getValue(i1,j1,khm1)+", "+Hz.getValue(i2,j2,khm1)+"\n"
                              +"\tupper wgts "+s121+", "+s221+"\n"
                              +"\t           "+s111+", "+s211+"\n"
                              +"\tlower wgts "+s122+", "+s222+"\n"
                              +"\t           "+s112+", "+s212+"\n"
                              +"\tupper vals "+v121+", "+v221+"\n"
                              +"\t           "+v111+", "+v211+"\n"
                              +"\tlower vals "+v122+", "+v222+"\n"
                              +"\t           "+v112+", "+v212);
            throw new java.lang.UnknownError("calculated NaN using "+xPos+", "+yPos+", "+zPos);
        }
    }
    
    private void interpOnUColumns(ModelGrid3D grid) throws ArrayIndexOutOfBoundsException {
        if (debug) logger.info("Interpolating on U columns");
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
            s112 = s111;
            s212 = s211;//ROMS code has s212=s112, but that doesn't seem right!
            s122 = s121;
            s222 = s221;
        }

        if (mask!=null) {
            m11 = (int) Math.round(mask.getValue(i1,j1));
            m21 = (int) Math.round(mask.getValue(i2,j1));
            m12 = (int) Math.round(mask.getValue(i1,j2));
            m22 = (int) Math.round(mask.getValue(i2,j2));
        }

        double v111 = md.getValue(i1,j1,k1); v111 = Double.isNaN(v111) ? 0.0 : v111;
        double v211 = md.getValue(i2,j1,k1); v211 = Double.isNaN(v211) ? 0.0 : v211;
        double v121 = md.getValue(i1,j2,k1); v121 = Double.isNaN(v121) ? 0.0 : v121;
        double v221 = md.getValue(i2,j2,k1); v221 = Double.isNaN(v221) ? 0.0 : v221;
        double v112 = md.getValue(i1,j1,k2); v112 = Double.isNaN(v112) ? 0.0 : v112;
        double v212 = md.getValue(i2,j1,k2); v212 = Double.isNaN(v212) ? 0.0 : v212;
        double v122 = md.getValue(i1,j2,k2); v122 = Double.isNaN(v122) ? 0.0 : v122;
        double v222 = md.getValue(i2,j2,k2); v222 = Double.isNaN(v222) ? 0.0 : v222;


        if ((m11+m21+m12+m22)<4) {//at least one of the corners is masked
            if (q2<=0.5) {//interpolating in "lower" half of horiz. grid cell
                v = p1*r1*s111*m11*v111+p2*r1*s211*m21*v211+
                    p1*r2*s112*m11*v112+p2*r2*s212*m21*v212;//linearly interpolate (v=0 on mask)
            } else {      //interpolating in "upper" half of horiz. grid cell
                v = p1*r1*s121*m12*v121+p2*r1*s221*m22*v221+
                    p1*r2*s122*m12*v122+p2*r2*s222*m22*v222;//linearly interpolate (v=0 on mask)
            }
        } else {
            v = p1*q1*r1*s111*v111+
                p2*q1*r1*s211*v211+
                p1*q2*r1*s121*v121+
                p2*q2*r1*s221*v221+
                p1*q1*r2*s112*v112+
                p2*q1*r2*s212*v212+
                p1*q2*r2*s122*v122+
                p2*q2*r2*s222*v222;
        }
        if (Double.isNaN(v)) {
            logger.info("Interpolator3D: interpolated U-cell NaN value for field "+md.getName()+"\n"
                              +"\tp1 = "+p1+", q1 = "+q1+", r1 = "+r1+"\n"
                              +"\tp2 = "+p2+", q2 = "+q2+", r2 = "+r2+"\n"
                              +"\tmask was "+m12+", "+m22+"\n"
                              +"\t         "+m11+", "+m22+"\n"
                              +"\tupper wgts "+s121+", "+s221+"\n"
                              +"\t           "+s111+", "+s211+"\n"
                              +"\tlower wgts "+s122+", "+s222+"\n"
                              +"\t           "+s112+", "+s212+"\n"
                              +"\tupper vals "+v121+", "+v221+"\n"
                              +"\t           "+v111+", "+v211+"\n"
                              +"\tlower vals "+v122+", "+v222+"\n"
                              +"\t           "+v112+", "+v212);
            throw new java.lang.UnknownError("calculated NaN using "+xPos+", "+yPos+", "+zPos);
        }
    }

    private void interpOnVColumns(ModelGrid3D grid) throws ArrayIndexOutOfBoundsException {
        if (debug) logger.info("Interpolating on V columns");
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
            s112 = s111;
            s212 = s211;//ROMS code has s212=s112, but that doesn't seem right!
            s122 = s121;
            s222 = s221;
        }

        if (mask!=null) {
            m11 = (int) Math.round(mask.getValue(i1,j1));
            m21 = (int) Math.round(mask.getValue(i2,j1));
            m12 = (int) Math.round(mask.getValue(i1,j2));
            m22 = (int) Math.round(mask.getValue(i2,j2));
        }

        double v111 = md.getValue(i1,j1,k1); v111 = Double.isNaN(v111) ? 0.0 : v111;
        double v211 = md.getValue(i2,j1,k1); v211 = Double.isNaN(v211) ? 0.0 : v211;
        double v121 = md.getValue(i1,j2,k1); v121 = Double.isNaN(v121) ? 0.0 : v121;
        double v221 = md.getValue(i2,j2,k1); v221 = Double.isNaN(v221) ? 0.0 : v221;
        double v112 = md.getValue(i1,j1,k2); v112 = Double.isNaN(v112) ? 0.0 : v112;
        double v212 = md.getValue(i2,j1,k2); v212 = Double.isNaN(v212) ? 0.0 : v212;
        double v122 = md.getValue(i1,j2,k2); v122 = Double.isNaN(v122) ? 0.0 : v122;
        double v222 = md.getValue(i2,j2,k2); v222 = Double.isNaN(v222) ? 0.0 : v222;

        if ((m11+m21+m12+m22)<4) {//at least one of the corners is masked
            if (i2<=0.5) {//interpolating in "left" half of horiz. grid cell
                v = q1*r1*s111*m11*v111+q2*r1*s121*m12*v121+
                    q1*r2*s112*m11*v112+q2*r1*s122*m12*v122;//linearly interpolate (v=0 on mask)
            } else {      //interpolating in "right" half of ohriz. grid cell
                v = q1*r1*s211*m21*v211+q2*r1*s221*m22*v221+
                    q1*r2*s212*m21*v212+q2*r2*s222*m22*v222;//linearly interpolate (v=0 on mask)
            }
        } else {
            v = p1*q1*r1*s111*v111+
                p2*q1*r1*s211*v211+
                p1*q2*r1*s121*v121+
                p2*q2*r1*s221*v221+
                p1*q1*r2*s112*v112+
                p2*q1*r2*s212*v212+
                p1*q2*r2*s122*v122+
                p2*q2*r2*s222*v222;
        }

        if (Double.isNaN(v)) {
            logger.info("Interpolator3D: interpolated V-cell NaN value for field "+md.getName()+"\n"
                              +"\tp1 = "+p1+", q1 = "+q1+", r1 = "+r1+"\n"
                              +"\tp2 = "+p2+", q2 = "+q2+", r2 = "+r2+"\n"
                              +"\tmask was "+m12+", "+m22+"\n"
                              +"\t         "+m11+", "+m22+"\n"
                              +"\tupper wgts "+s121+", "+s221+"\n"
                              +"\t           "+s111+", "+s211+"\n"
                              +"\tlower wgts "+s122+", "+s222+"\n"
                              +"\t           "+s112+", "+s212+"\n"
                              +"\tupper vals "+v121+", "+v221+"\n"
                              +"\t           "+v111+", "+v211+"\n"
                              +"\tlower vals "+v122+", "+v222+"\n"
                              +"\t           "+v112+", "+v212);
            throw new java.lang.UnknownError("calculated NaN using "+xPos+", "+yPos+", "+zPos);
        }
    }
    
    /**
     * Computes the stretched vertical grid coordinate K (0<K<N) corresponding
     * to the input depth (z<0, typically) and horizontal grid position (I,J).
     * This calculation interpolates the bathymetric depth and SSH to I,J 
     * and then computes layer depths based on the interpolated values.
     *
     *@return double--calculated grid depth (0<K<N).
     *@param I--
     *@param J--
     *@param z--depth in m (z<0, typically).
     *@return K--vertical grid corrdinate (0<=K<=N; if z<-Bathymetric Depth or z>Sea Surface Height, K = NaN)
     */
    public double calcKfromZ(double I, double J, double z) throws ArrayIndexOutOfBoundsException {
        ModelGrid3D grid = globalInfo.getGrid3D();
        double K = -1;
        int N = grid.getN();
        double[] pos = new double[] {I,J};
        double bd = interpolateBathymetricDepth(pos);
        if (z<-bd) return 0.0;//originally returned 0, then returned NaN, then Double.NEGATIVE_INFINITY, now K=0 (bottom)
        double zt = interpolateValue3D(pos,pe.getField("zeta"),null,INTERP_VAL);
        if (z>zt) return (double) N;//originally returned 0, then returned NaN, then Double.POSITIVE_INFINITY, now K=N (surface)
        double[] zw = grid.computeLayerZs(bd,zt);
        int k = 1;
        while ((zw[k]<=z)&&(k<N)) {
            k++;
        }
        K = Math.max(0,(k-1)+(z-zw[k-1])/(zw[k]-zw[k-1]));
        return K;
    }
    
    /**
     * Computes the depth (z<0, typically) corresponding to the input stretched
     * vertical grid coordinate K (0<=K<=N) and horizontal grid position (I,J).
     * This calculation interpolates the bathymetric depth and SSH to I,J 
     * and then computes layer depths based on the interpolated values.
     *
     *@return z--calculated depth (z<0, typically).
     *@param I--
     *@param J--
     *@param K--vertical grid position.
     */
    public double calcZfromK(double I, double J, double K) throws ArrayIndexOutOfBoundsException {
        ModelGrid3D grid = globalInfo.getGrid3D();
        double z = 0;
        double[] pos = new double[] {I,J};
        double bd = interpolateBathymetricDepth(pos);
        double zt = interpolateValue3D(pos,pe.getField("zeta"),null,INTERP_VAL);
        double[] zw = grid.computeLayerZs(bd,zt);
        int N = grid.getN();
        if (K<N){
            int k = (int) Math.floor(K);
            if (k<0) {
                logger.info("calcZfromK: k<0");
                k =0;
            }
            z = zw[k]+(K-k)*(zw[k+1]-zw[k]);
        } else {
            z = zw[N];
        }
        return z;
    }
    
    /**
     * Calculate the scale factor to transform a physical
     * vertical velocity into one relative to grid units.
     * The layer depth is interpolated at the specified position to
     * scale the physical vertical velocity to grid units.
     *
     * @return wScale such that w(grid units) = w(physical units)/wScale.
     * @param pos-- double[] with {xi,eta,K) where xi, eta, K are in grid coordinates
     * @param delz--double = W*dT in physical units (m)
     */
    public double calcWscale(double[] pos, double delz) throws ArrayIndexOutOfBoundsException {
        double K0 = pos[2];
        double z0 = calcZfromK(pos[0],pos[1],pos[2]);
        double z1 = z0+delz;
        double K1 = calcKfromZ(pos[0],pos[1],z1);
        v  = delz/(K1-K0);
        if (debug||Double.isNaN(v)) {
            logger.info("calcWscale: "+v);
            logger.info("\tpos = "+pos[0]+", "+pos[1]+", "+pos[2]+", "+delz);
            logger.info("\tz1: "+z1+", z0: "+z0+", K1= "+K1+" K0 = "+K0);
        }
        return v;
    }
    
    /**
     * Calculate the physical depth corresponding to the input grid position.
     *
     * @return--physical depth corresponding to vertical grid position.
     * @param pos--position vector in grid units (0<I<L,0<J<M,0<K<N)
     */
    public double interpolateDepth(double[] pos) throws ArrayIndexOutOfBoundsException {
        //initialize values
        initializeParameters();
        
        ModelGrid3D grid = globalInfo.getGrid3D();
        N = grid.getN();
        K = (int) Math.floor(pos[2]);
        k2 = Math.min(Math.max(K+1,0),N);
        k1 = k2+1;
        
        //Interpolate on rho-columns
        int Ir = (int) Math.floor(pos[0]);
        int Jr = (int) Math.floor(pos[1]);
        i1 = Math.min(Math.max(Ir  ,0),grid.getLm()+1);
        i2 = Math.min(Math.max(Ir+1,0),grid.getLm()+1);
        j1 = Math.min(Math.max(Jr  ,0),grid.getMm()+1);
        j2 = Math.min(Math.max(Jr+1,0),grid.getMm()+1);
        
        p2 = (i2-i1)*(pos[0]-i1);
        q2 = (j2-j1)*(pos[1]-j1);
        p1 = 1-p2;
        q1 = 1-q2;
        
        //integrate the vertical layer depths
        double frc = k2-pos[2];
        ModelData Hz = pe.getField("Hz");
        double s11 = frc*Hz.getValue(i1,j1,k2);
        double s21 = frc*Hz.getValue(i2,j1,k2);
        double s12 = frc*Hz.getValue(i1,j2,k2);
        double s22 = frc*Hz.getValue(i2,j2,k2);
        for (int k=k1;k<=N;k++) {
            s11 += Hz.getValue(i1,j1,k);
            s21 += Hz.getValue(i2,j1,k);
            s12 += Hz.getValue(i1,j2,k);
            s22 += Hz.getValue(i2,j2,k);
        }
        //change sign on total depths and add SSHs
        ModelData zeta = pe.getField("zeta");
        s11 = -s11+zeta.getValue(i1, j1);
        s21 = -s21+zeta.getValue(i2, j1);
        s12 = -s12+zeta.getValue(i1, j2);
        s22 = -s22+zeta.getValue(i2, j2);
        
        v = p1*q1*s11+
            p2*q1*s21+
            p1*q2*s12+
            p2*q2*s22;
        
        return v;
    }
    
    /**
     * Prints debug info to System.out
     */
    @Override
    public void debug() {
        String cc = ",";
        DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance();
        DecimalFormat ef1 = (DecimalFormat) NumberFormat.getInstance();
        df1.applyPattern("###0.0000000000");//fixed decimal format (f15.10)
        ef1.applyPattern("0.00000000E00");//scientific notation (e15.8)
        logger.info("<<<<<<<<<<<<<<<<<<Interpolator3D>>>>>>>>>>>>>>>>>");
        logger.info("Name = "+md.getName());
        logger.info("Position is "+xPos+","+yPos+","+zPos);
        logger.info("interpType = "+interpType);
        try {
                logger.info("i1,i2 = "+i1+cc+i2);
                logger.info("j1,j2 = "+j1+cc+j2);
                logger.info("K,k1,k2  = "+K+cc+k1+cc+k2);
                logger.info("p1,p2 = "+df1.format(p1)+cc+df1.format(p2));
                logger.info("q1,q2 = "+df1.format(q1)+cc+df1.format(q2));
                logger.info("r1,r2 = "+df1.format(r1)+cc+df1.format(r2));
                logger.info("s111,s211 = "+ef1.format(s111)+cc+ef1.format(s211));
                logger.info("s121,s221 = "+ef1.format(s121)+cc+ef1.format(s221));
                logger.info("s112,s212 = "+ef1.format(s112)+cc+ef1.format(s212));
                logger.info("s122,s222 = "+ef1.format(s122)+cc+ef1.format(s222));
                logger.info("m11,m21 ="+df1.format(mask.getValue(i1,j1))+cc+df1.format(mask.getValue(i2,j1)));
                logger.info("m12,m22 ="+df1.format(mask.getValue(i1,j2))+cc+df1.format(mask.getValue(i2,j2)));
                logger.info("v122,v222 ="+df1.format(md.getValue(i1,j2,k2))+cc+df1.format(md.getValue(i2,j2,k2)));
                logger.info("v112,v212 ="+df1.format(md.getValue(i1,j1,k2))+cc+df1.format(md.getValue(i2,j1,k2)));
                logger.info("v121,v221 ="+df1.format(md.getValue(i1,j2,k1))+cc+df1.format(md.getValue(i2,j2,k1)));
                logger.info("v111,v211 ="+df1.format(md.getValue(i1,j1,k1))+cc+df1.format(md.getValue(i2,j1,k1)));
        } catch (Exception ex) {
            logger.info("Interpolator3D.debug() error in indices");
            ex.printStackTrace();
        }
        logger.info("cff1,cff2 = "+df1.format(cff1)+cc+df1.format(cff2));
        logger.info("value     = "+df1.format(v));
        logger.info("<<<<<<<<<<<<<<<<<<Interpolator3D>>>>>>>>>>>>>>>>>");
   }

    public double interpolateTemperature(double[] pos) throws ArrayIndexOutOfBoundsException {
        ModelGrid3D grid = globalInfo.getGrid3D();
        double t = interpolateValue3D(pos,pe.getField("temp"),grid.mask_rho,INTERP_VAL);
        return t;
    }

    public double interpolateSalinity(double[] pos) throws ArrayIndexOutOfBoundsException {
        ModelGrid3D grid = globalInfo.getGrid3D();
        double s = interpolateValue3D(pos,pe.getField("salt"),grid.mask_rho,INTERP_VAL);
        return s;
    }

    public double interpolateSSH(double[] pos) throws ArrayIndexOutOfBoundsException {
        ModelGrid3D grid = globalInfo.getGrid3D();
        double s = interpolateValue2D(pos,pe.getField("zeta"),grid.mask_rho,INTERP_VAL);
        return s;
    }

    /**
     * Returns true if position (in grid coordinates) is within the given tolerance
     * of the edge of the model grid (regarded as [1:Lm, 1:Mm]).
     * 
     * @param pos         - position in grid coordinates to test
     * @param tolGridEdge - max distance in grid coordinates that results in true
     * @return            - true or false
     */
    public boolean isAtGridEdge(double[] pos, double tolGridEdge) {
        ModelGrid3D grid = globalInfo.getGrid3D();
        return ((pos[0]<tolGridEdge)||((grid.getLm()-tolGridEdge)<pos[0])||(pos[1]<tolGridEdge)||((grid.getMm()-tolGridEdge)<pos[1]));
    }

    /**
     * Returns a grid cell id string for an input position, accounting for
     * location relative to the grid edge.
     * 
     * @param pos         - position in grid coordinates to test
     * @param tolGridEdge - max distance in grid coordinates that results in true
     * @return            - grid cell id string
     */
    public String getGridCellID(double[] pos, double tolGridEdge) {
        ModelGrid3D grid = globalInfo.getGrid3D();
        
        String gx = ""+Math.round(pos[0]);
        if ((pos[0]<tolGridEdge)) gx = "-0";
        else if ((grid.getLm()-tolGridEdge)<pos[0]) gx = "+"+Math.round(pos[0]);
        
        String gy = ""+Math.round(pos[1]);
        if ((pos[1]<tolGridEdge)) gy = "0-"; else
        if ((grid.getMm()-tolGridEdge)<pos[1]) gy = Math.round(pos[1])+"+";
        
        String s = gx + "_" + gy;
        return s;
    }
}
