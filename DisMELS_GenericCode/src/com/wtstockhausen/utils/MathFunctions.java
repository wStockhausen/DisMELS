/*
 * MathFunctions.java
 *
 * Created on March 6, 2006, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.wtstockhausen.utils;

/**
 *  This class provides static math functions that may be missing from
 *  java.lang.Math.
 *
 * @author William Stockhausen
 */
public class MathFunctions {
    
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
    
    public static int isTrue(boolean b) {
        if (b) return 1;
        return 0;
    }
}
