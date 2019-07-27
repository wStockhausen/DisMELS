/*
 * Grid2DUtilities.java
 *
 * Created on December 21, 2005, 6:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.model;

import java.util.logging.Logger;

/**
 * Provides global constants and methods for ROMS model output.
 * All constants and methods are static.
 * @author William Stockhausen
 */
public class Grid2DUtilities {
    /** Flag to turn on ROMS approach to finding cell indices from input lat/lons. */
    public static boolean likeOldROMS = true;
    /** Raduis of earth (m) */
    public static final double earthRadius = 6371315.0;
    /** Conversion factor for degrees to radians (pi/180). */
    public static final double deg2rad = Math.toRadians(1.0);
    /** flag to print debugging info. */
    public static boolean debug = false;
    /** logger for class */
    private static final Logger logger = Logger.getLogger(Grid2DUtilities.class.getName());
    
    /**
     * Creates a new instance of Grid2DUtilities
     */
    private Grid2DUtilities() {
    }
    
    /**
     * Computes the horizontal IJ coordinates equivalent to the
     * input position vector.  This method duplicates the functionality
     * of the ROMS subroutine "hindices" in Utility.F.
     * @param pos --double[2] position vector to convert to IJ.
     *    if LatLon, pos={lon,lat}
     *    if XY,     pos={x,y}.
     * @param xgrd --ModelData field with grid of coordinates corresponding to pos index 0.
     * @param ygrd --ModelData field with grid of coordinates corresponding to pos index 1.
     * @param angler --the ModelData field angler from a ModelGrid2D instance
     * @param spherical --boolean flag indicates input coordinates are spherical (lat/lon), if true.
     * @return --double[2] position vector in IJ (xi,eta) coordinates.
     */
    public static double[] computeHorizontalIndices(double[] pos, 
                                                    ModelData xgrd,
                                                    ModelData ygrd,
                                                    ModelData angler,
                                                    boolean spherical)
                            throws java.lang.ArrayIndexOutOfBoundsException {
        double[] grdPos = new double[] {-1.0,-1.0};
        boolean found;
        int Imin,Imax,i0,Jmin,Jmax,j0;
        
        Imin = 0;
        Jmin = 0;
        Imax = xgrd.getL();
        Jmax = xgrd.getM();
        
        found = try_range(pos,xgrd,ygrd,Imin,Imax,Jmin,Jmax);
        if (found) {
            while (((Imax-Imin)>1)||((Jmax-Jmin)>1)) {
                if ((Imax-Imin)>1) {
                    i0 = (Imin+Imax)/2;
                    found = try_range(pos,xgrd,ygrd,Imin,i0,Jmin,Jmax);
                    if (found) {
                        Imax = i0;
                    } else {
                        Imin = i0;
                    }
                }
                if ((Jmax-Jmin)>1) {
                    j0 = (Jmin+Jmax)/2;
                    found = try_range(pos,xgrd,ygrd,Imin,Imax,Jmin,j0);
                    if (found) {
                        Jmax = j0;
                    } else {
                        Jmin = j0;
                    }
                }
            }
            if (debug) {
                logger.info("<<<<<<<<<<<<<<<<<<<<<Grid2DUtilities>>>>>>>>>>>>>>>>>");
                logger.info("pos:\t"+pos[0]+"\t"+pos[1]);
                logger.info("IJ:\t"+Imin+"\t"+Imax+"\t"+Jmin+"\t"+Jmax);
                logger.info(xgrd.getName()+":");
                logger.info(""+xgrd.getValue(Imin,Jmin)+"\t"+xgrd.getValue(Imax,Jmin));
                logger.info(""+xgrd.getValue(Imin,Jmax)+"\t"+xgrd.getValue(Imax,Jmax));
                logger.info(ygrd.getName()+":");
                logger.info(""+ygrd.getValue(Imin,Jmin)+"\t"+ygrd.getValue(Imax,Jmin));
                logger.info(""+ygrd.getValue(Imin,Jmax)+"\t"+ygrd.getValue(Imax,Jmax));
            }
            grdPos = calcIJold(pos,Imin,Jmin,xgrd,ygrd,angler,spherical);
            if (debug) {
                logger.info("IJ from calcIJold:");
                logger.info("grdPos =\t "+grdPos[0]+"\t"+grdPos[1]);                
//                logger.info("<<<<<<<<<<<<<<<<<<<<<Grid2DUtilities>>>>>>>>>>>>>>>>>");
            }
            grdPos = calcIJnew(pos,Imin,Jmin,xgrd,ygrd);
            if (debug) {
                logger.info("IJ from calcIJnew:");
                logger.info("grdPos =\t "+grdPos[0]+"\t"+grdPos[1]);                
                logger.info("<<<<<<<<<<<<<<<<<<<<<Grid2DUtilities>>>>>>>>>>>>>>>>>");
            }
        } else {
            String str = "\nGrid2DUtilities.computeHorizontalIndices: could not compute indices for {"+
                    pos[0]+","+pos[1]+"}. \nStopped at: "+"IJ:\t"+Imin+"\t"+Imax+"\t"+Jmin+"\t"+Jmax;
            throw new java.lang.ArrayIndexOutOfBoundsException(str);
        }
        return grdPos;
    }

    /**
     * Calculate IJ from input coordinates pos using the original ROMS method. 
     * Note that this does not guarantee a "round trip" in that it is not guaranteed
     * that computing XY->IJ->XY' yields XY = XY' (as one might think).
     * @param pos
     * @param Imin
     * @param Jmin
     * @param xgrd
     * @param ygrd
     * @param angler
     * @param spherical
     * @return 
     */
    private static double[] calcIJold(double[] pos, 
                                        int Imin, int Jmin,
                                        ModelData xgrd,
                                        ModelData ygrd,
                                        ModelData angler,
                                        boolean spherical){
        double[] grdPos = new double[] {-1.0,-1.0};
        //Knowing correct cell, calculate exact indices
        double xfac,xpp,yfac,ypp;
        double diag2,aa2,bb2,phi,ang,dx,dy;
        if (likeOldROMS) {
            if (spherical) {
                yfac = earthRadius*deg2rad;
                xfac = yfac*Math.cos(pos[1]*deg2rad);
                xpp = (pos[0]-xgrd.getValue(Imin,Jmin))*xfac;
                ypp = (pos[1]-ygrd.getValue(Imin,Jmin))*yfac;
            } else {
                xfac = 1.0;
                yfac = 1.0;
                xpp = pos[0]-xgrd.getValue(Imin,Jmin);
                ypp = pos[1]-ygrd.getValue(Imin,Jmin);
            }
        } else {
            xfac = 1.0;
            yfac = 1.0;
            xpp = pos[0]-xgrd.getValue(Imin,Jmin);
            ypp = pos[1]-ygrd.getValue(Imin,Jmin);
        }

        //use law of cosines to get cell parallelogram "shear" angle.
        diag2 = Math.pow(xgrd.getValue(Imin+1,Jmin)-xgrd.getValue(Imin,Jmin+1),2)+
                Math.pow(ygrd.getValue(Imin+1,Jmin)-ygrd.getValue(Imin,Jmin+1),2);
        aa2   = Math.pow(xgrd.getValue(Imin,Jmin)-xgrd.getValue(Imin+1,Jmin),2)+
                Math.pow(ygrd.getValue(Imin,Jmin)-ygrd.getValue(Imin+1,Jmin),2);
        bb2   = Math.pow(xgrd.getValue(Imin,Jmin)-xgrd.getValue(Imin,Jmin+1),2)+
                Math.pow(ygrd.getValue(Imin,Jmin)-ygrd.getValue(Imin,Jmin+1),2);
        phi = Math.asin((diag2-aa2-bb2)/(2*Math.sqrt(aa2*bb2)));

        //transform position into curvilinear coordinates.
        //Assume cell is rectangular, for now.
        ang = angler.getValue(Imin,Jmin);
        dx = xpp*Math.cos(ang)+ypp*Math.sin(ang);
        dy = ypp*Math.cos(ang)-xpp*Math.sin(ang);

        //correct for parallelogram
        dx = dx+dy*Math.tan(phi);
        dy = dy/Math.cos(phi);

        //scale with cell side lengths to translate into grid coordinates
        dx = Math.min(Math.max(0.0,dx/Math.sqrt(aa2)/xfac),1.0);
        dy = Math.min(Math.max(0.0,dy/Math.sqrt(bb2)/yfac),1.0);

        grdPos[0] = Imin+dx;
        grdPos[1] = Jmin+dy;

        return grdPos;
    }
    
    /**
     * Calculate IJ from input coordinates pos using a new method. 
     * Note that this method should guarantee a "round trip" such
     * that computing XY->IJ->XY' yields XY = XY' (as one might think).
     * @param pos
     * @param Imin
     * @param Jmin
     * @param xgrd
     * @param ygrd
     * @return 
     */
    private static double[] calcIJnew(double[] pos, 
                                        int Imin, int Jmin,
                                        ModelData xgrd,
                                        ModelData ygrd){
        double[] grdPos = new double[] {-1.0,-1.0};
        //Knowing correct cell, calculate the grid coordinates such that
        //XY->IJ->XY' yields XY = XY'
        double x,x11,x12,x21,x22;
        double y,y11,y12,y21,y22;
        x = pos[0];
        y = pos[1];
        x11 = xgrd.getValue(Imin  ,Jmin  );
        x21 = xgrd.getValue(Imin+1,Jmin  );
        x12 = xgrd.getValue(Imin  ,Jmin+1);
        x22 = xgrd.getValue(Imin+1,Jmin+1);
        y11 = ygrd.getValue(Imin  ,Jmin  );
        y21 = ygrd.getValue(Imin+1,Jmin  );
        y12 = ygrd.getValue(Imin  ,Jmin+1);
        y22 = ygrd.getValue(Imin+1,Jmin+1);
        
        double di, dj;
        boolean dxchk = (Math.abs(x12-x11)+Math.abs(x22-x21))<Double.MIN_NORMAL;//check if indep of J
        boolean dychk = (Math.abs(y21-y11)+Math.abs(y22-y12))<Double.MIN_NORMAL;//check if indep of I
        if (dxchk & dychk){
            //x = x(I), y = y(J)
            di = (x-x11)/(x21-x11);
            dj = (y-y11)/(y12-y11);
        } else {
            //x = x(I,J), y = y(I,J)
            double ddx, ddy;
            ddx = x11-x21-x12+x22;
            ddy = y11-y21-y12+y22;

            //calculate coefficients of quadratic eqn ax^2+bx+c=0 where x is di
            double a,b,c;
            c = (x-x11)*(y12-y11) - (y-y11)*(x12-x11);
            b = (x-x11)*ddy - (x21-x11)*(y12-y11)  - ((y-y11)*ddx - (y21-y11)*(x12-x11));
            a = -((x21-x11)*ddy - (y21-y11)*ddx);

            if (Math.abs(a)<Double.MIN_NORMAL){
                //equation is degenerate (linear): bx+c=0
                di = -c/b;
                logger.info("Quadratic interpolation is degenerate ");
            } else {
                double rt = Math.sqrt(b*b-4*a*c);
                di = (-b+rt)/(2*a);
        //        logger.info("di positive sign root = "+di);
                if ((di>1.0)||(di<0.0)) {
                    di = (-b-rt)/(2*a);
        //            logger.info("di negative sign root = "+di);
                }
            }
            dj = ((x-x11)-di*(x21-x11))/(di*ddx+(x12-x11));
    //        logger.info("dj = "+dj);
        }
        
        grdPos[0] = di+Imin;
        grdPos[1] = dj+Jmin;
        return grdPos;
   }
     
    public static double[] findRoot() {
        double[] root = new double[]{0,0};
        return root;
    }
    /**
     * Given a gridded domain with matrix coordinates xgrd and ygrd, this
     * function determines whether the point pos = {xo,yo} is inside the
     * box defined by the requested corners Imin,Jmin and Imax,Jmax. This
     * method duplicates the functionality of the ROMS subroutine "try_range"
     * in Utility.F.
     * 
     * At present, this should only be used with rho-grids!
     * 
     * 
     * @return--true if pos is inside, false otherwise.
     * @param pos 
     * @param xgrd 
     * @param ygrd 
     * @param Imin 
     * @param Imax 
     * @param Jmin 
     * @param Jmax 
     * @return 
     */
    public static boolean try_range(double[] pos, ModelData xgrd, ModelData ygrd,
                             int Imin, int Imax, int Jmin, int Jmax) {
        boolean b = false;
        double[] xb,yb;
        int ip,jp,nb,shft;
        //Construct closed polygon corresponding to grid rectangle with 
        //opposite corners at Imin,Jmin and Imax,Jmax.  Here (as opposed to ROMS),
        //the last coordinate must be identical to the first.
        nb = 2*(Jmax-Jmin+Imax-Imin)+1;
        xb = new double[nb];
        yb = new double[nb];
        shft = 0-Imin;
        for (int i=Imin;i<Imax;i++) {
            ip = i+shft;
            xb[ip] = xgrd.getValue(i,Jmin);
            yb[ip] = ygrd.getValue(i,Jmin);
        }
        shft = 0-Jmin+Imax-Imin;
        for (int j=Jmin;j<Jmax;j++) {
            jp = j+shft;
            xb[jp] = xgrd.getValue(Imax,j);
            yb[jp] = ygrd.getValue(Imax,j);
        }
        shft = 0+Jmax-Jmin+2*Imax-Imin;
        for (int i=Imax;i>Imin;i--) {
            ip = shft-i;
            xb[ip] = xgrd.getValue(i,Jmax);
            yb[ip] = ygrd.getValue(i,Jmax);
        }
        shft = 0+2*Jmax-Jmin+2*(Imax-Imin);
        for (int j=Jmax;j>Jmin;j--) {
            jp = shft-j;
            xb[jp] = xgrd.getValue(Imin,j);
            yb[jp] = ygrd.getValue(Imin,j);
        }
        //now close the polygon
        xb[nb-1] = xb[0];
        yb[nb-1] = yb[0];
       
        b = inside(pos,xb,yb);
        return b;
    }
    
    /**
    !================================================== Hernan G. Arango ===
    !  Copyright (c) 2002 ROMS/TOMS Group                                  !
    !========================================== Alexander F. Shchepetkin ===
    !                                                                      !
    !  Given the vectors Xb and Yb of size Nb, defining the coordinates    !
    !  of a closed polygon,  this function find if the point (Xo,Yo) is    !
    !  inside the polygon.  If the point  (Xo,Yo)  falls exactly on the    !
    !  boundary of the polygon, it still considered inside.                !
    !                                                                      !
    !  This algorithm does not rely on the setting of  Xb(Nb)=Xb(1) and    !
    !  Yb(Nb)=Yb(1).  Instead, it assumes that the last closing segment    !
    !  is (Xb(Nb),Yb(Nb)) --> (Xb(1),Yb(1)).                               !
    !                                                                      !
    !  Reference:                                                          !
    !                                                                      !
    !    Reid, C., 1969: A long way from Euclid. Oceanography EMR,         !
    !      page 174.                                                       !
    !                                                                      !
    !  Algorithm:                                                          !
    !                                                                      !
    !  The decision whether the point is  inside or outside the polygon    !
    !  is done by counting the number of crossings from the ray (Xo,Yo)    !
    !  to (Xo,-infinity), hereafter called meridian, by the boundary of    !
    !  the polygon.  In this counting procedure,  a crossing is counted    !
    !  as +2 if the crossing happens from "left to right" or -2 if from    !
    !  "right to left". If the counting adds up to zero, then the point    !
    !  is outside.  Otherwise,  it is either inside or on the boundary.    !
    !                                                                      !
    !  This routine is a modified version of the Reid (1969) algorithm,    !
    !  where all crossings were counted as positive and the decision is    !
    !  made  based on  whether the  number of crossings is even or odd.    !
    !  This new algorithm may produce different results  in cases where    !
    !  Xo accidentally coinsides with one of the (Xb(k),k=1:Nb) points.    !
    !  In this case, the crossing is counted here as +1 or -1 depending    !
    !  of the sign of (Xb(k+1)-Xb(k)).  Crossings  are  not  counted if    !
    !  Xo=Xb(k)=Xb(k+1).  Therefore, if Xo=Xb(k0) and Yo>Yb(k0), and if    !
    !  Xb(k0-1) < Xb(k0) < Xb(k0+1),  the crossing is counted twice but    !
    !  with weight +1 (for segments with k=k0-1 and k=k0). Similarly if    !
    !  Xb(k0-1) > Xb(k0) > Xb(k0+1), the crossing is counted twice with    !
    !  weight -1 each time.  If,  on the other hand,  the meridian only    !
    !  touches the boundary, that is, for example, Xb(k0-1) < Xb(k0)=Xo    !
    !  and Xb(k0+1) < Xb(k0)=Xo, then the crossing is counted as +1 for    !
    !  segment k=k0-1 and -1 for segment k=k0, resulting in no crossing.   !
    !                                                                      !
    !  Note 1: (Explanation of the logical condition)                      !
    !                                                                      !
    !  Suppose  that there exist two points  (x1,y1)=(Xb(k),Yb(k))  and    !
    !  (x2,y2)=(Xb(k+1),Yb(k+1)),  such that,  either (x1 < Xo < x2) or    !
    !  (x1 > Xo > x2).  Therefore, meridian x=Xo intersects the segment    !
    !  (x1,y1) -> (x2,x2) and the ordinate of the point of intersection    !
    !  is:                                                                 !
    !                                                                      !
    !                 y1*(x2-Xo) + y2*(Xo-x1)                              !
    !             y = -----------------------                              !
    !                          x2-x1                                       !
    !                                                                      !
    !  The mathematical statement that point  (Xo,Yo)  either coinsides    !
    !  with the point of intersection or lies to the north (Yo>=y) from    !
    !  it is, therefore, equivalent to the statement:                      !
    !                                                                      !
    !         Yo*(x2-x1) >= y1*(x2-Xo) + y2*(Xo-x1),   if   x2-x1 > 0      !
    !  or                                                                  !
    !         Yo*(x2-x1) <= y1*(x2-Xo) + y2*(Xo-x1),   if   x2-x1 < 0      !
    !                                                                      !
    !  which, after noting that  Yo*(x2-x1) = Yo*(x2-Xo + Xo-x1) may be    !
    !  rewritten as:                                                       !
    !                                                                      !
    !        (Yo-y1)*(x2-Xo) + (Yo-y2)*(Xo-x1) >= 0,   if   x2-x1 > 0      !
    !  or                                                                  !
    !        (Yo-y1)*(x2-Xo) + (Yo-y2)*(Xo-x1) <= 0,   if   x2-x1 < 0      !
    !                                                                      !
    !  and both versions can be merged into  essentially  the condition    !
    !  that (Yo-y1)*(x2-Xo)+(Yo-y2)*(Xo-x1) has the same sign as x2-x1.    !
    !  That is, the product of these two must be positive or zero.         !
    !                                                                      !
    !=======================================================================
     * 
     * @param pos 
     * @param xb 
     * @param yb 
     * @return 
     */
    public static boolean inside(double[] pos, double xb[], double[] yb) {
        boolean b = false;
        
        double dx1,dx2,dxy,xo,yo;
        int crossings,inc,kp,nb,nc,nStep;
        int[] sIndex;
        
        nStep = 128;
        sIndex = new int[128];
        nb = xb.length;
        
        xo = pos[0];
        yo = pos[1];
        crossings = 0;        
        for (int kk=0;kk<nb;kk=kk+nStep) {
            nc = 0;
            for (int k=kk;k<Math.min(kk+nStep,nb-1);k++) {
                if (((xb[k+1]-xo)*(xo-xb[k])>=0.0)&&(xb[k]!=xb[k+1])) {
                    sIndex[nc] = k;
                    nc++; //in java, this must increment after assignment
                }
            }
            for (int i=0;i<nc;i++) {
                kp = sIndex[i];
                double xbp,xbp1,ybp,ybp1;
                xbp = xb[kp];xbp1=xb[kp+1];
                ybp = yb[kp];ybp1=yb[kp+1];
                if (xb[kp]!=xb[kp+1]) {
                    dx1 = xo      -xb[kp];
                    dx2 = xb[kp+1]-xo;
                    dxy = dx2*(yo-yb[kp])-dx1*(yb[kp+1]-yo);
                    inc=0;
                    if ((xb[kp]==xo)&&(yb[kp]==yo)) {
                        crossings = 1;
                        return true;
                    } else if (((dx1==0.0)&&(yo>=yb[kp]))||
                              ((dx2==0.0)&&(yo>=yb[kp+1]))) {
                        inc = 1;
                    } else if ((dx1*dx2>0.0)&&((xb[kp+1]-xb[kp])*dxy>=0.0)) {
                        inc = 2;
                    }
                    if (xb[kp+1]>xb[kp]) {
                        crossings = crossings+inc;
                    } else {
                        crossings = crossings-inc;
                    }
                }
            }
        }
        if (crossings!=0) b = true;
        return b;
    }
}
