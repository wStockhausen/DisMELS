/*
 * LagrangianParticle.java
 *
 * Created on December 16, 2005, 5:23 PM
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
public class LagrangianParticle implements Cloneable {
    
    public static boolean debug = false;
    
    /**
     * Max index of time steps required for integration
     */
    public static int NFT = 4;
    /**
     * Max index of values in track matrix (track[NFV+1][NFT+1])
     */
    public static int NFV = 8;
    
    public static int IXGRD = 0;
    public static int IYGRD = 1;
    public static int IZGRD = 2;
    public static int IXRHS = 3;
    public static int IYRHS = 4;
    public static int IZRHS = 5;
    public static int IU    = 6;
    public static int IV    = 7;
    public static int IW    = 8;
    
    private static LagrangianParticleTracker lpt;
    
    private double[][] trackData;
    int np1,n,nm1,nm2,nm3,nsv;
    
    /** logger for class */
    private static final Logger logger = Logger.getLogger(LagrangianParticle.class.getName());
    
    /** Creates a new instance of LagrangianParticle */
    public LagrangianParticle() {
        trackData = new double[NFV+1][NFT+1];
        resetIndices();        
    }
    
    public Object clone() {
        LagrangianParticle clone = null;
        try {
            clone = (LagrangianParticle) super.clone();
            clone.trackData = new double[NFV+1][NFT+1];
            for (int i=0;i<(NFV+1);i++) {
                for (int j=0;j<(NFT+1);j++) {
                    clone.trackData[i][j] = trackData[i][j];
                }
            }
            if (debug) {
                logger.info("Cloned track:");
                lpt.printTrackArray(clone.trackData);
            }
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return clone;
    }
    
    /**
     * Sets LagrangianParticleTracker instance used to subsequently
     * integrate positions of all LangrangianParticle instances.
     * @param lpt LagrangianParticleTracker instance used to integrate position.
     */
    public static void setTracker(LagrangianParticleTracker lpt) {
        LagrangianParticle.lpt = lpt;
    }
    
    /**
     * Calculates the corrector step in the 4th-order Milne predictor-corrector
     * integration scheme.
     * @param dt --time step for integration
     */
    public void doCorrectorStep() throws ArrayIndexOutOfBoundsException {
        lpt.doCorrectorStep(this);
        nsv = nm3;
        nm3 = nm2;
        nm2 = nm1;
        nm1 = n;
        n   = np1;
        np1 = nsv;
        if (debug) {
            String cc = ",";
            String str = ""+trackData[IXGRD][n]+cc
                           +trackData[IYGRD][n]+cc
                           +trackData[IZGRD][n]+cc
                           +trackData[IXRHS][n]+cc
                           +trackData[IYRHS][n]+cc
                           +trackData[IZRHS][n]+cc
                           +trackData[IU][n]+cc
                           +trackData[IV][n]+cc
                           +trackData[IW][n];
            logger.info("Current particle position is");
            logger.info("\t"+str);
                           
        }
    }
    
    /**
     * Calculates the predictor step in the 4th-order Milne predictor-corrector
     * integration scheme.
     * @param dt --time step for integration.
     */
    public void doPredictorStep() throws ArrayIndexOutOfBoundsException {
        lpt.doPredictorStep(this);
    }
    
    /**
     * Gets predicted (np1) position vector as grid I,J,K values (0<I<L;0<J<M;0<K<N).
     * @return --double[3] vector with particle I,J,K as elements.
     */
    public double[] getPredictedIJK() {
        return new double[] {trackData[IXGRD][np1],
                             trackData[IYGRD][np1],
                             trackData[IZGRD][np1]};
    }
    
    /**
     * Gets current (n) position vector as grid I,J,K values (0<I<L;0<J<M;0<K<N).
     * @return --double[3] vector with particle I,J,K as elements.
     */
    public double[] getIJK() {
        return new double[] {trackData[IXGRD][n],
                             trackData[IYGRD][n],
                             trackData[IZGRD][n]};
    }
    
    /**
     * Gets current IJK position as a String ("I,J,K").
     * @return --String representation of IJK position.
     */
    public String getIJKstring() {
        String str = "";
        for (int i=0;i<2;i++) {
            str = str+trackData[i][n]+",";
        }
        str = str+trackData[2][n];
        return str;
    }
    
    /**
     * Gets the track data matrix (as double[NFV][NFT])
     * @return --double[NFV][NFT].
     */
    public double[][] getTrackData() {
        return trackData;
    }
    
    /**
     * Resets the indices mapping the track data to the 
     * integration time steps n-3,n-2,n-1,n,n+1.
     */
    public void resetIndices() {
        //initial tracker indices
        np1 = 1;
        n   = 0;
        nm1 = 4;
        nm2 = 3;
        nm3 = 2;
    }
    
    /**
     * Performs the predictor-corrector integration. (The integration can also be
     * performed by calling doPredictorStep() followed by a call to doCorrectorStep()
     * with the appropriate parameter values).
     * @param dt --integration time step.
     */
    public void step() throws ArrayIndexOutOfBoundsException {
        doPredictorStep();
        doCorrectorStep();
    }
    
    /**
     * Sets the initial position of the particle in IJK coordinates.  This resets
     * the integration sequence as if the particle was just released.
     * @param i --location along xi axis (0<I<L).
     * @param j --location along eta axis (0<J<M).
     * @param k --location along s axis (0<K<N).
     */
    public void setIJK(double i, double j, double k) {
        resetIndices();
        for (int m=0;m<(NFT+1);m++) {
            trackData[IXGRD][m] = i;
            trackData[IYGRD][m] = j;
            trackData[IZGRD][m] = k;
            trackData[IXRHS][m] = 0.0;
            trackData[IYRHS][m] = 0.0;
            trackData[IZRHS][m] = 0.0;
            trackData[IU][m] = 0.0;
            trackData[IV][m] = 0.0;
            trackData[IW][m] = 0.0;
        }
        lpt.initialize(this);
    }
    
    public void setTimeStep(double dt) {
        lpt.setTimeStep(dt);
    }
    
    /**
     * Convenience method to set particle's intrinsic u,v,w.
     * @param u --particle's intrinsic velocity in (m/s) xi direction.
     * @param v --particle's intrinsic velocity (m/s) in eta direction.
     * @param w --particle's intrinsic vertical velocity (m/s).
     * @param m --index at which to set W: for LagrangianParticle lp,
     *              use lp.getN() if setting before doPredictorStep()
     *              use lp.getNP1() if setting before doCorrectorStep()
     */
    public void setUVW(double u, double v, double w, int m) {
        setU(u,m);
        setV(v,m);
        setW(w,m);
     }
    
    /**
     * Sets the particle's intrinsic velocity in the xi direction 
     * at integration time step n+1.  The input velocity should be in m/s, 
     * but is then converted to an equivalent grid velocity.
     *
     * @param u --particle's intrinsic velocity in (m/s) xi direction.
     * @param m --index at which to set W: for LagrangianParticle lp,
     *              use lp.getN() if setting before doPredictorStep()
     *              use lp.getNP1() if setting before doCorrectorStep()
     */
    public void setU(double u, int m) {
        if (u!=0) {
            double[] pos = new double[] {trackData[IXGRD][m],
                                         trackData[IYGRD][m]};
            double uScale = lpt.calcUscale(pos);
            if (Math.abs(uScale)>0) {
                trackData[IU][m] = u/uScale;
            } else {
                trackData[IU][m] = 0;
            }
            if (debug) {
                logger.info("in LagrangianParticle.setU(u,m)");
                logger.info("u, uScale, tu = "+u+", "+uScale+", "+trackData[IU][m]);
            }
        } else {
            trackData[IU][m] = 0;
        }
    }
    
    /**
     * Sets the particle's intrinsic velocity in the eta direction 
     * at integration time step n+1.  The input velocity should be in m/s, 
     * but is then converted to an equivalent grid velocity.
     *
     * @param v --particle's intrinsic velocity in (m/s) eta direction.
     * @param m --index at which to set W: for LagrangianParticle lp,
     *              use lp.getN() if setting before doPredictorStep()
     *              use lp.getNP1() if setting before doCorrectorStep()
     */
    public void setV(double v,int m) {
        if (v!=0) {
            double[] pos = new double[] {trackData[IXGRD][m],
                                         trackData[IYGRD][m]};
            double vScale = lpt.calcVscale(pos);
            if (Math.abs(vScale)>0) {
                trackData[IV][m] = v/vScale;
            } else {
                trackData[IV][m] = 0;
            }
            if (debug) {
                logger.info("in LagrangianParticle.setV(v,m)");
                logger.info("v, vScale, tv = "+v+", "+vScale+", "+trackData[IV][m]);
            }
        } else {
            trackData[IV][m] = 0;
        }
    }
    
    /**
     * Sets the particle's intrinsic velocity in the vertical direction 
     * at integration time step m.  The input velocity should be in m/s, 
     * but is then converted to an equivalent grid velocity.
     *
     * @param w --particle's intrinsic vertical velocity (m/s).
     * @param m --index at which to set W: for LagrangianParticle lp,
     *              use lp.getN() if setting before doPredictorStep()
     *              use lp.getNP1() if setting before doCorrectorStep()
     */
    public void setW(double w, int m) {
        if (w!=0) {
            double[] pos = new double[] {trackData[IXGRD][m],
                                         trackData[IYGRD][m],
                                         trackData[IZGRD][m]};
            double wScale = lpt.calcWscale(pos,w);
            if (Math.abs(wScale)>0) {
                trackData[IW][m] = w/wScale;
            } else {
                trackData[IW][m] = 0;
            }
            if (debug) {
                logger.info("in LagrangianParticle.setW(w,m)");
                logger.info("w, wScale, tw = "+w+", "+wScale+", "+trackData[IW][m]);
            }
        } else {
            trackData[IW][m] = 0;
        }
    }

    /**
     *  Returns the index of trackdata corresponding to
     *  the "current" values.
     *  @return -- int
     */
    public int getN() {
        return n;
    }

    /**
     *  Returns the index of trackdata corresponding to
     *  the "next" or predicted values.
     *  @return -- int
     */
    public int getNP1() {
        return np1;
    }
    
}
