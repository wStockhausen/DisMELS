/*
 * MathFunctions.java
 *
 * Created on March 6, 2006, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.utilities;

import org.apache.commons.math3.special.Erf;

/**
 *  This class provides static math functions that may be missing from
 *  java.lang.Math.
 *
 * @author William Stockhausen
 */
public class MathFunctions {
    
    public static double SQRT_TWO   = Math.sqrt(2.0);
    public static double SQRT_TWOPI = Math.sqrt(2.0*Math.PI);
    
    /** Creates a new instance of MathFunctions */
    public MathFunctions() {
    }
    
    public static double mod(double x, double y) {
        double z = x-y*Math.floor(x/y);
        return z;
    }
    
    public static int mod(int x, int y) {
        int z = x-y*(x/y);
        return z;
    }
    
    public static long mod(long x, long y) {
        long z = x-y*(x/y);
        return z;
    }

    /**
     * Returns the value of the inverse CDF for a N(0,1) distribution.
     * Based on an algorithm given by
     *  http://home.online.no/~pjacklam/notes/invnorm/#The_algorithm
     * @param p
     * @return 
     */
    public static double invNormalCDF(double p){
        //based on http://home.online.no/~pjacklam/notes/invnorm/#The_algorithm
        //The algorithm below assumes p is the input and x is the output.
        double x = Double.NEGATIVE_INFINITY;

        //Coefficients in rational approximations.
        double[] a = new double[6];
        a[1] = -3.969683028665376e+01;
        a[2] =  2.209460984245205e+02;
        a[3] = -2.759285104469687e+02;
        a[4] =  1.383577518672690e+02;
        a[5] = -3.066479806614716e+01;
        a[6] =  2.506628277459239e+00;

        double[] b = new double[5];
        b[1] = -5.447609879822406e+01;
        b[2] =  1.615858368580409e+02;
        b[3] = -1.556989798598866e+02;
        b[4] =  6.680131188771972e+01;
        b[5] = -1.328068155288572e+01;

        double[] c = new double[6];
        c[1] = -7.784894002430293e-03;
        c[2] = -3.223964580411365e-01;
        c[3] = -2.400758277161838e+00;
        c[4] = -2.549732539343734e+00;
        c[5] =  4.374664141464968e+00;
        c[6] =  2.938163982698783e+00;

        double[] d = new double[4];
        d[1] =  7.784695709041462e-03;
        d[2] =  3.224671290700398e-01;
        d[3] =  2.445134137142996e+00;
        d[4] =  3.754408661907416e+00;

        //Define break-points.
        double p_low = - 0.02425;
        double p_high = 1 - p_low;

        double q; double r;
        if ((0 < p)&&(p<p_low)){
            //Rational approximation for lower region.
           q = Math.sqrt(-2*Math.log(p));
           x = (((((c[1]*q+c[2])*q+c[3])*q+c[4])*q+c[5])*q+c[6]) /
                 ((((d[1]*q+d[2])*q+d[3])*q+d[4])*q+1);
        } else if ((p_low<=p)&&(p<=p_high)){
            //Rational approximation for central region.
           q = p - 0.5;
           r = q*q;
           x = (((((a[1]*r+a[2])*r+a[3])*r+a[4])*r+a[5])*r+a[6])*q /
                (((((b[1]*r+b[2])*r+b[3])*r+b[4])*r+b[5])*r+1);
        } else if ((p_high<p)&&(p<1)){
           //Rational approximation for upper region.
           q = Math.sqrt(-2*Math.log(1-p));
           x = -(((((c[1]*q+c[2])*q+c[3])*q+c[4])*q+c[5])*q+c[6]) /
                  ((((d[1]*q+d[2])*q+d[3])*q+d[4])*q+1);
        }
        
        //   The relative error of the approximation has
        //   absolute value less than 1.15 × 10−9.  One iteration of
        //   Halley’s rational method (third order) gives full machine precision.
        if ((0 < p)&&(p < 1)){
           double e = 0.5 * erfc(-x/SQRT_TWO) - p ;
           double u = e * SQRT_TWOPI * Math.exp((x*x)/2);
           x = x - u/(1 + x*u/2);
       }

        return x;
    }
    
    /**
     * Returns the value of the error function integral{-inf,x} exp(-y^2/2)dy.
     * Based the erf() function in the Apache Commons Math library (http://commons.apache.org/math).
     * 
     * @param x
     * @return 
     */
    public static double erf(double x){
        return Erf.erf(x);
    }
    
    /**
     * Returns the value of the complementary error function 1-erf(x).
     * Based the erfc() function in the Apache Commons Math library (http://commons.apache.org/math).
     * 
     * @param x
     * @return 
     */
    public static double erfc(double x){
        return Erf.erfc(x);
    }
}
