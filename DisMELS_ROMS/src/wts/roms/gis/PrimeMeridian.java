/*
 * PrimeMeridian.java
 *
 * Created on June 20, 2006, 10:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gis;

import com.wtstockhausen.utils.MathFunctions;

/**
 *
 * @author William Stockhausen
 */
public class PrimeMeridian {
    
    public static final int PM_GREENWICH = 0;
    public static final int PM_IDL       = 1;
    
    /* Indicates value of the prime meridian in the GIS system.
     * The PRIME_MERIDIAN's default value is PM_GREENWICH */
    private static int PRIME_MERIDIAN = PM_GREENWICH;
    /* IDL=1 if PRIME_MERIDIAN is the IDL, 0 otherwise */
    private static int IDL = MathFunctions.isTrue(PRIME_MERIDIAN==PM_IDL);
    
    /** Creates a new instance of PrimeMeridian */
    protected PrimeMeridian() {
    }

    /*
     *  Sets the prime meridian used for displaying ROMS grid data in
     *  a GIS setting.
     */
    public static void setPrimeMeridian(int i) {
        PRIME_MERIDIAN = i;
        IDL = MathFunctions.isTrue(PRIME_MERIDIAN==PM_IDL);
    }

    /*
     *  Sets the prime meridian used for displaying ROMS grid data in
     *  a GIS setting.
     */
    public static int getPrimeMeridian() {
        return PRIME_MERIDIAN;
    }

    /*
     *  Adjusts input lon from GIS coordinates for GIS prime meridian and 
     *  range ([-180,180]) to ROMS coordinates with Greenwich as prime meridian 
     *  and range ([0,360]).
     *
     *  @param lon - lon relative to GIS coordinate system
     *  @return    - lon relative to ROMS coordinate system
     */
    public static double adjustToROMSlon(double lon) {
        lon = MathFunctions.mod(lon-180*IDL+360,360.0);
        return lon;
    }

    /*
     *  Adjusts input lon from ROMS coordinates for ROMS prime meridian (Greenwich) 
     *  and range ([0,360]) to GIS coordinate system with prime meridian as
     *  inidcated by setPrimeMeridian(int) with range ([-180,180])
     *
     *  @param lon - lon relative to ROMS coordinate system
     *  @return    - lon relative to GIS coordinate system
     */
    public static double adjustToGISlon(double lon) {
        lon = MathFunctions.mod(lon+180*IDL+360,360.0);
        if (lon>180) lon = lon-360; // lon in [-180,180]
        return lon;
    }
    
}
