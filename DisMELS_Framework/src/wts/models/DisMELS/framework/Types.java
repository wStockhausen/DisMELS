/*
 * Types.java
 *
 * Created on January 19, 2006, 10:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

/**
 *
 * @author William Stockhausen
 */
public class Types {
    
    /* specifies horizontal coordinates are cell-type (I,J) */
    public static final int HORIZ_IJ = 0;
    /* specifies horizontal coordinates are xy-type (physical units) */
    public static final int HORIZ_XY = 1;
    /* specifies horizontal coordinates are lon, lat coordinates */
    public static final int HORIZ_LL = 2;
    /* specifies vertical coordinate is cell-type (K=0-N levels) */
    public static final int VERT_K  = 0;
    /* specifies vertical coordinate is z-type (depth is negative, in physical units) */
    public static final int VERT_Z  = 1;
    /* specifies vertical coordinate is depth-type (meters below surface, in physical units) */
    public static final int VERT_H  = 2; 
    /* specifies vertical coordinate is distance above the bottom, in physical uinits */
    public static final int VERT_DH = 3;
    
    /** Creates a new instance of Types */
    private Types() {
    }
    
}
