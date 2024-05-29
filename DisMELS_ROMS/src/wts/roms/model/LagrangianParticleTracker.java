/*
 * LagrangianParticleTracker.java
 *
 * Created on December 16, 2005, 5:23 PM
 *
 * Revisions:
 * 20171018: 1. Revised to accomodate motion without advection by currents.
 */

package wts.roms.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 *
 * @author William Stockhausen
 */
public class LagrangianParticleTracker {
    
    /* turn on debugging output */
    public static boolean debug = false;
    /** 
     * do LPT like ROMS:
     *  if true: use rho mask for u,v interpolation 
     *  else     use u, v masks
     */
    public static boolean maskLikeROMS = true;
    /** w = zero if true so passive, non-diffusive particle stays on isosurface */
    public static boolean noVerticalMotion = false;
    /** 
     * Static flag to set advective u,v,w = zero if true 
     * so NO particles are advected by local currents. This should
     * be used for TESTING ONLY. 
     * 
     * NOTE: to incorporate movement without advection
     * for simulated individuals, noAdvection should be set to true in the
     * LagrangianParticle instance associated with the individuals.
     */
    public static boolean noAdvection = false;
    
    private GlobalInfo globalInfo = GlobalInfo.getInstance();
    
    //get instance of pe from i3d whenever the former is updated
    private Interpolator3D i3d;
    
    /* mask for u interpolation */
    private MaskData umask;
    /* mask for v interpolation */
    private MaskData vmask;
    private final int L,M,N;
    private double dt;
    
    private boolean vertWalk = false;
    private final double cff1p,cff2p;
    private final double cff1c,cff2c,cff3c,cff4c;
    private double val;
    private double[][] track;
    private final double[] pos = new double[3];
    private final int ixgrd,iygrd,izgrd,ixrhs,iyrhs,izrhs,iu,iv,iw;
    private int np1, n, nm1, nm2, nm3;
    /** logger for class */
    private static final Logger logger = Logger.getLogger(LagrangianParticleTracker.class.getName());
    
    /**
     * Creates a new instance of LagrangianParticleTracker
     */
    public LagrangianParticleTracker(Interpolator3D i3dp) {
        i3d = i3dp;
       
        cff1p = 8.0/3.0;
        cff2p = 4.0/3.0;
        cff1c = 9.0/8.0;
        cff2c = 1.0/8.0;
        cff3c = 3.0/8.0;
        cff4c = 6.0/8.0;
           
        ModelGrid3D grid = globalInfo.getGrid3D();
        L = grid.getL();
        M = grid.getM();
        N = grid.getN();
        
        ixgrd = LagrangianParticle.IXGRD;
        iygrd = LagrangianParticle.IYGRD;
        izgrd = LagrangianParticle.IZGRD;
        ixrhs = LagrangianParticle.IXRHS;
        iyrhs = LagrangianParticle.IYRHS;
        izrhs = LagrangianParticle.IZRHS;
        
        iu = LagrangianParticle.IU;
        iv = LagrangianParticle.IV;
        iw = LagrangianParticle.IW;
    }
    
    /**
     * Calculates the u-horizontal velocity scale to convert from physical velocity [m/s]
     * to u-horizontal grid velocity.
     * 
     * @param pos
     * @return 
     */
    public double calcUscale(double[] pos) {
        double s = i3d.calcUscale(pos);
        if (Double.isNaN(s)) {
            throw new java.lang.UnknownError("LPT: U scale calculated NaN using "+pos[0]+", "+pos[1]+", "+pos[2]);
        }
        return s;
    }
    
    /**
     * Calculates the u-horizontal velocity scale to convert from physical velocity [m/s]
     * to u-horizontal grid velocity.
     * 
     * @param pos
     * @return 
     */
    public double calcVscale(double[] pos) {
        double s = i3d.calcVscale(pos);
        if (Double.isNaN(s)) {
            throw new java.lang.UnknownError("LPT: V scale calculated NaN using "+pos[0]+", "+pos[1]+", "+pos[2]);
        }
        return s;
    }
    
    /**
     * Calculates the vertical velocity scale to convert from physical velocity [m/s]
     * to vertical grid velocity.
     * 
     * @param pos - position in grid coordinates (xi,eta,K)
     * @param w   - vertical velocity (in physical units [m/s])
     * 
     * @return wScale - scaling to grid coordinates
     * 
     * Note that wScale will be -/+ infinity if \cr
     *   pos[2]=0 and w<0 or \cr
     *   pos[2]=N and w>0 \cr
     * but that is ok.
     */
    public double calcWscale(double[] pos,double w) {
        double wScale = i3d.calcWscale(pos,w*dt);
        if (Double.isNaN(wScale)) {
            String msg = "Problem in LPT.calcWscale(pos,w): wScale is NaN: \n"+
                          "pos = "+pos[0]+", "+pos[1]+", "+pos[2]+
                          "w, dt, wScale = "+w+", "+dt+", "+wScale;
            logger.warning(msg);
        }
        return wScale;
    }
    
    /**
     * Initialize integration for LagrangianParticle INCLUDING advection by currents
     * (unless static variable noAdvection is true).
     * 
     * @param lp
     * @throws ArrayIndexOutOfBoundsException 
     */
    public void initialize(LagrangianParticle lp) throws ArrayIndexOutOfBoundsException {
        np1 = lp.np1;
        n   = lp.n;
        nm1 = lp.nm1;
        nm2 = lp.nm2;
        nm3 = lp.nm3;
        track = lp.getTrackData();
        //Interpolate "slopes" at corrected locations
        for (int i=0;i<3;i++) pos[i] = track[ixgrd+i][n];
        if (Interpolator3D.debug) logger.info("\tU interpolation:");
        if (noAdvection){
            track[ixrhs][n] = 0.0;
            track[iyrhs][n] = 0.0;
            track[izrhs][n] = 0.0;
        } else {
            track[ixrhs][n] = i3d.interpolateValue3D(pos,
                                                    i3d.pe.getField("u"),
                                                    umask,
                                                    Interpolator3D.INTERP_SLOPE);
            if (Interpolator3D.debug) logger.info("\tV interpolation:");
            track[iyrhs][n] = i3d.interpolateValue3D(pos,
                                                    i3d.pe.getField("v"),
                                                    vmask,
                                                    Interpolator3D.INTERP_SLOPE);
            if (!noVerticalMotion) {
                if (Interpolator3D.debug) logger.info("\tW interpolation:");
                track[izrhs][n] = i3d.interpolateValue3D(pos,
                                                        i3d.pe.getField("w"),
                                                        globalInfo.getGrid3D().mask_rho,
                                                        Interpolator3D.INTERP_SLOPE);
            } else track[izrhs][n] = 0.0;
        }
        if (debug||LagrangianParticle.debug) {
            logger.info("After initialization: ");
            printTrackArray(track);
        }
        track = null;
    }
    
    /**
     * Calculates predictor step of 4th-order Milne time-stepping scheme INCLUDING
     * advection by currents (unless static variable noAdvection is true).
     * 
     * @param lp
     * @throws ArrayIndexOutOfBoundsException 
     */
    public void doPredictorStep(LagrangianParticle lp) throws ArrayIndexOutOfBoundsException {
        ModelGrid3D grid3D = globalInfo.getGrid3D();
        if (maskLikeROMS) {
            umask = grid3D.mask_rho;
            vmask = grid3D.mask_rho;
        } else {
            umask = grid3D.mask_u;
            vmask = grid3D.mask_v;
        }
        np1 = lp.np1;
        n   = lp.n;
        nm1 = lp.nm1;
        nm2 = lp.nm2;
        nm3 = lp.nm3;
        track = lp.getTrackData();
        if (debug||Interpolator3D.debug) {
            logger.info("Before predictor step:");
            printTrackArray(track);
        }
        if (vertWalk) {
            //TODO: fill this in
            logger.info("Error: vertWalk not implemented in LPT!");
            System.exit(1);
        } else {
            //Predictor step: predict particle locations at new time step using 
            //4th-order Milne time-stepping scheme.
            val = track[ixgrd][nm3]+
                                dt*(cff1p*(track[ixrhs][n  ]+track[iu][n  ])-
                                    cff2p*(track[ixrhs][nm1]+track[iu][nm1])+
                                    cff1p*(track[ixrhs][nm2]+track[iu][nm2]));
            track[ixgrd][np1] = Math.max(0,Math.min(val,L));
            val = track[iygrd][nm3]+
                                dt*(cff1p*(track[iyrhs][n  ]+track[iv][n  ])-
                                    cff2p*(track[iyrhs][nm1]+track[iv][nm1])+
                                    cff1p*(track[iyrhs][nm2]+track[iv][nm2]));
            track[iygrd][np1] = Math.max(0,Math.min(val,M));
            if (!noVerticalMotion) {
                val = track[izgrd][nm3]+
                                    dt*(cff1p*(track[izrhs][n  ]+track[iw][n  ])-
                                        cff2p*(track[izrhs][nm1]+track[iw][nm1])+
                                        cff1p*(track[izrhs][nm2]+track[iw][nm2]));
                track[izgrd][np1] = Math.max(0,Math.min(val,N));
            }
        }
         if (debug||Interpolator3D.debug) {
            logger.info("After predictor step: "+dt);
            printTrackArray(track);
        }
        //Predict "slopes" at new timestep
        for (int i=0;i<3;i++) pos[i] = track[ixgrd+i][np1];
        if (Interpolator3D.debug) {
            logger.info("\tU interpolation:");
        }
        if (noAdvection){
            track[ixrhs][n] = 0.0;
            track[iyrhs][n] = 0.0;
            track[izrhs][n] = 0.0;
        } else {
            track[ixrhs][np1] = i3d.interpolateValue3D(pos,
                                                    i3d.pe.getField("u"),
                                                    umask,
                                                    Interpolator3D.INTERP_SLOPE);
            if (Interpolator3D.debug) logger.info("\tV interpolation:");
            track[iyrhs][np1] = i3d.interpolateValue3D(pos,
                                                    i3d.pe.getField("v"),
                                                    vmask,
                                                    Interpolator3D.INTERP_SLOPE);
            if (!noVerticalMotion) {
                if (Interpolator3D.debug) logger.info("\tW interpolation:");
                track[izrhs][np1] = i3d.interpolateValue3D(pos,
                                                        i3d.pe.getField("w"),
                                                        globalInfo.getGrid3D().mask_rho,
                                                        Interpolator3D.INTERP_SLOPE);
            }
        }
        if (debug||Interpolator3D.debug) {
           logger.info("After interpolation: "+dt);
           printTrackArray(track);
            logger.info("End of doPredictorStep(lp)");
        }
        umask = null;
        vmask = null;
        track = null;
   }
    
    /**
     * Calculates corrector step of 4th-order Milne time-stepping scheme INCLUDING
     * advection by currents (unless static variable noAdvection is true).
     * 
     * @param lp
     * @throws ArrayIndexOutOfBoundsException 
     */
    public void doCorrectorStep(LagrangianParticle lp) throws ArrayIndexOutOfBoundsException {
        np1 = lp.np1;
        n   = lp.n;
        nm1 = lp.nm1;
        nm2 = lp.nm2;
        nm3 = lp.nm3;
        track = lp.getTrackData();
        if (vertWalk) {
            //TODO: fill this in
            logger.info("Error: vertWalk not implemented in LPT!");
            System.exit(1);
        } else {
            //Corrector step: predict particle locations at new time step using 
            //4th-order Milne time-stepping scheme.
            val =  cff1c*track[ixgrd][n  ]-
                                 cff2c*track[ixgrd][nm2]+
                                 dt*(cff3c*(track[ixrhs][np1]+track[iu][np1])+
                                     cff4c*(track[ixrhs][n  ]+track[iu][n  ])-
                                     cff3c*(track[ixrhs][nm1]+track[iu][nm1]));
            track[ixgrd][np1] = Math.max(0,Math.min(val,L));
            val =  cff1c*track[iygrd][n  ]-
                                 cff2c*track[iygrd][nm2]+
                                 dt*(cff3c*(track[iyrhs][np1]+track[iv][np1])+
                                     cff4c*(track[iyrhs][n  ]+track[iv][n  ])-
                                     cff3c*(track[iyrhs][nm1]+track[iv][nm1]));
            track[iygrd][np1] = Math.max(0,Math.min(val,M));
            if (!noVerticalMotion) {
                val =  cff1c*track[izgrd][n  ]-
                                     cff2c*track[izgrd][nm2]+
                                     dt*(cff3c*(track[izrhs][np1]+track[iw][np1])+
                                         cff4c*(track[izrhs][n  ]+track[iw][n  ])-
                                         cff3c*(track[izrhs][nm1]+track[iw][nm1]));
                track[izgrd][np1] = Math.max(0,Math.min(val,N));
            }
        }
        //reflect particles at surface or bottom
//        if (track[izgrd][np1]>N) 
//            track[izgrd][np1] = 2*N-track[izgrd][np1];
//        if (track[izgrd][np1]<0) 
//            track[izgrd][np1] = -track[izgrd][np1];

        //Interpolate "slopes" at corrected locations
        for (int i=0;i<3;i++) pos[i] = track[ixgrd+i][np1];
        if (globalInfo.getGrid2D().isOnLand(pos)){
            //set position to edge of previous ocean cell
            double dx = track[ixgrd][np1]-track[ixgrd][n];
            if ((dx>0) && (Math.round(track[ixgrd][n])<Math.round(track[ixgrd][np1]))){
                track[ixgrd][np1] = Math.round(track[ixgrd][n])+0.49;
            } else
            if ((dx<0) && (Math.round(track[ixgrd][np1])<Math.round(track[ixgrd][n]))){
                track[ixgrd][np1] = Math.round(track[ixgrd][n])-0.49;
            }
            double dy = track[iygrd][np1]-track[iygrd][n];
            if ((dy>0) && (Math.round(track[iygrd][n])<Math.round(track[iygrd][np1]))){
                track[iygrd][np1] = Math.round(track[iygrd][n])+0.49;
            } else
            if ((dy<0) && (Math.round(track[iygrd][np1])<Math.round(track[iygrd][n]))){
                track[iygrd][np1] = Math.round(track[iygrd][n])-0.49;
            }
        }
        if (debug||Interpolator3D.debug) {
            logger.info("After corrector step: "+dt);
            printTrackArray(track);
        }
        if (Interpolator3D.debug) logger.info("\tU interpolation:");
        if (noAdvection){
            track[ixrhs][n] = 0.0;
            track[iyrhs][n] = 0.0;
            track[izrhs][n] = 0.0;
        } else {
            track[ixrhs][np1] = i3d.interpolateValue3D(pos,
                                                    i3d.pe.getField("u"),
                                                    umask,
                                                    Interpolator3D.INTERP_SLOPE);
            if (Interpolator3D.debug) logger.info("\tV interpolation:");
            track[iyrhs][np1] = i3d.interpolateValue3D(pos,
                                                    i3d.pe.getField("v"),
                                                    vmask,
                                                    Interpolator3D.INTERP_SLOPE);
            if (!noVerticalMotion && !vertWalk) {
                if (Interpolator3D.debug) logger.info("\tW interpolation:");
                track[izrhs][np1] = i3d.interpolateValue3D(pos,
                                                        i3d.pe.getField("w"),
                                                        globalInfo.getGrid3D().mask_rho,
                                                        Interpolator3D.INTERP_SLOPE);
            }
        }
        if (debug||Interpolator3D.debug) {
            logger.info("After interpolation: "+dt);
            printTrackArray(track);
            logger.info("End doCorrectorStep()");
        }
        track = null;
    }
    
    /**
     * Initialize integration for LagrangianParticle EXCLUDING advection by currents
     * (unless static variable noAdvection is true).
     * 
     * @param lp
     * @throws ArrayIndexOutOfBoundsException 
     */
    public void initializeNoAdvection(LagrangianParticle lp) throws ArrayIndexOutOfBoundsException {
        np1 = lp.np1;
        n   = lp.n;
        nm1 = lp.nm1;
        nm2 = lp.nm2;
        nm3 = lp.nm3;
        track = lp.getTrackData();
        //Interpolate "slopes" at corrected locations
        for (int i=0;i<3;i++) pos[i] = track[ixgrd+i][n];
        if (Interpolator3D.debug) logger.info("\tU interpolation:");
        //no advection, so
        track[ixrhs][n] = 0.0;
        track[iyrhs][n] = 0.0;
        track[izrhs][n] = 0.0;
        if (debug||LagrangianParticle.debug) {
            logger.info("After initialization: ");
            printTrackArray(track);
        }
        track = null;
    }
    
    /**
     * Calculates predictor step of 4th-order Milne time-stepping scheme EXCLUDING
     * advection by currents.
     * 
     * @param lp
     * @throws ArrayIndexOutOfBoundsException 
     */
    public void doPredictorStepNoAdvection(LagrangianParticle lp) throws ArrayIndexOutOfBoundsException {
        ModelGrid3D grid3D = globalInfo.getGrid3D();
        if (maskLikeROMS) {
            umask = grid3D.mask_rho;
            vmask = grid3D.mask_rho;
        } else {
            umask = grid3D.mask_u;
            vmask = grid3D.mask_v;
        }
        np1 = lp.np1;
        n   = lp.n;
        nm1 = lp.nm1;
        nm2 = lp.nm2;
        nm3 = lp.nm3;
        track = lp.getTrackData();
        if (debug||Interpolator3D.debug) {
            logger.info("Before predictor step:");
            printTrackArray(track);
        }
        if (vertWalk) {
            //TODO: fill this in
            logger.info("Error: vertWalk not implemented in LPT!");
            System.exit(1);
        } else {
            //Predictor step: predict particle locations at new time step using 
            //4th-order Milne time-stepping scheme.
            val = track[ixgrd][nm3]+
                                dt*(cff1p*(track[ixrhs][n  ]+track[iu][n  ])-
                                    cff2p*(track[ixrhs][nm1]+track[iu][nm1])+
                                    cff1p*(track[ixrhs][nm2]+track[iu][nm2]));
            track[ixgrd][np1] = Math.max(0,Math.min(val,L));
            val = track[iygrd][nm3]+
                                dt*(cff1p*(track[iyrhs][n  ]+track[iv][n  ])-
                                    cff2p*(track[iyrhs][nm1]+track[iv][nm1])+
                                    cff1p*(track[iyrhs][nm2]+track[iv][nm2]));
            track[iygrd][np1] = Math.max(0,Math.min(val,M));
            if (!noVerticalMotion) {
                val = track[izgrd][nm3]+
                                    dt*(cff1p*(track[izrhs][n  ]+track[iw][n  ])-
                                        cff2p*(track[izrhs][nm1]+track[iw][nm1])+
                                        cff1p*(track[izrhs][nm2]+track[iw][nm2]));
                track[izgrd][np1] = Math.max(0,Math.min(val,N));
            }
        }
         if (debug||Interpolator3D.debug) {
            logger.info("After predictor step: "+dt);
            printTrackArray(track);
        }
        //Predict "slopes" at new timestep
        for (int i=0;i<3;i++) pos[i] = track[ixgrd+i][np1];
        if (Interpolator3D.debug) {
            logger.info("\tU interpolation:");
        }
        track[ixrhs][n] = 0.0;
        track[iyrhs][n] = 0.0;
        track[izrhs][n] = 0.0;
        if (debug||Interpolator3D.debug) {
           logger.info("After interpolation: "+dt);
           printTrackArray(track);
            logger.info("End of doPredictorStep(lp)");
        }
        umask = null;
        vmask = null;
        track = null;
   }
    
    /**
     * Calculates corrector step of 4th-order Milne time-stepping scheme EXCLUDING
     * advection by currents.
     * 
     * @param lp
     * @throws ArrayIndexOutOfBoundsException 
     */
    public void doCorrectorStepNoAdvection(LagrangianParticle lp) throws ArrayIndexOutOfBoundsException {
        np1 = lp.np1;
        n   = lp.n;
        nm1 = lp.nm1;
        nm2 = lp.nm2;
        nm3 = lp.nm3;
        track = lp.getTrackData();
        if (vertWalk) {
            //TODO: fill this in
            logger.info("Error: vertWalk not implemented in LPT!");
            System.exit(1);
        } else {
            //Corrector step: predict particle locations at new time step using 
            //4th-order Milne time-stepping scheme.
            val =  cff1c*track[ixgrd][n  ]-
                                 cff2c*track[ixgrd][nm2]+
                                 dt*(cff3c*(track[ixrhs][np1]+track[iu][np1])+
                                     cff4c*(track[ixrhs][n  ]+track[iu][n  ])-
                                     cff3c*(track[ixrhs][nm1]+track[iu][nm1]));
            track[ixgrd][np1] = Math.max(0,Math.min(val,L));
            val =  cff1c*track[iygrd][n  ]-
                                 cff2c*track[iygrd][nm2]+
                                 dt*(cff3c*(track[iyrhs][np1]+track[iv][np1])+
                                     cff4c*(track[iyrhs][n  ]+track[iv][n  ])-
                                     cff3c*(track[iyrhs][nm1]+track[iv][nm1]));
            track[iygrd][np1] = Math.max(0,Math.min(val,M));
            if (!noVerticalMotion) {
                val =  cff1c*track[izgrd][n  ]-
                                     cff2c*track[izgrd][nm2]+
                                     dt*(cff3c*(track[izrhs][np1]+track[iw][np1])+
                                         cff4c*(track[izrhs][n  ]+track[iw][n  ])-
                                         cff3c*(track[izrhs][nm1]+track[iw][nm1]));
                track[izgrd][np1] = Math.max(0,Math.min(val,N));
            }
        }
        //reflect particles at surface or bottom
//        if (track[izgrd][np1]>N) 
//            track[izgrd][np1] = 2*N-track[izgrd][np1];
//        if (track[izgrd][np1]<0) 
//            track[izgrd][np1] = -track[izgrd][np1];

        //Interpolate "slopes" at corrected locations
        for (int i=0;i<3;i++) pos[i] = track[ixgrd+i][np1];
        if (globalInfo.getGrid2D().isOnLand(pos)){
            //set position to edge of previous ocean cell
            double dx = track[ixgrd][np1]-track[ixgrd][n];
            if ((dx>0) && (Math.round(track[ixgrd][n])<Math.round(track[ixgrd][np1]))){
                track[ixgrd][np1] = Math.round(track[ixgrd][n])+0.49;
            } else
            if ((dx<0) && (Math.round(track[ixgrd][np1])<Math.round(track[ixgrd][n]))){
                track[ixgrd][np1] = Math.round(track[ixgrd][n])-0.49;
            }
            double dy = track[iygrd][np1]-track[iygrd][n];
            if ((dy>0) && (Math.round(track[iygrd][n])<Math.round(track[iygrd][np1]))){
                track[iygrd][np1] = Math.round(track[iygrd][n])+0.49;
            } else
            if ((dy<0) && (Math.round(track[iygrd][np1])<Math.round(track[iygrd][n]))){
                track[iygrd][np1] = Math.round(track[iygrd][n])-0.49;
            }
        }
        if (debug||Interpolator3D.debug) {
            logger.info("After corrector step: "+dt);
            printTrackArray(track);
        }
        if (Interpolator3D.debug) logger.info("\tU interpolation:");
        //no advection, so
        track[ixrhs][n] = 0.0;
        track[iyrhs][n] = 0.0;
        track[izrhs][n] = 0.0;
        if (debug||Interpolator3D.debug) {
            logger.info("After interpolation: "+dt);
            printTrackArray(track);
            logger.info("End doCorrectorStep()");
        }
        track = null;
    }
    
    public boolean isVertWalk() {
        return vertWalk;
    }
    
    public void printTrackArray(double[][] a) {
        String str,cc,tb;
        cc = ",";
        tb = "\t";
        DecimalFormat df1 = (DecimalFormat) NumberFormat.getInstance();
        DecimalFormat ef1 = (DecimalFormat) NumberFormat.getInstance();
        df1.applyPattern("###0.0000000000");//fixed decimal format (f15.10)
        ef1.applyPattern("0.0000000000E00");//scientific notation (e15.10)
        int J = a[0].length;
        int I = a.length;
        int[] m = new int[]{np1,n,nm1,nm2,nm3};
        logger.info("track array:");
        for (int i=0;i<I;i++) {
            str = tb;
            for (int j=0;j<J;j++) {
                str = str+ef1.format(a[i][m[j]])+tb;
            }
            logger.info(str);
        }
    }
    
    public void setInterpolator(Interpolator3D i3dp) {
        i3d = i3dp;
    }

    public double getTimeStep() {
        return dt;
    }
    
    public void setTimeStep(double tstep) {
        dt = tstep;
    }
    
    public void setVertWalk(boolean v) {
        vertWalk = v;
    }
}
