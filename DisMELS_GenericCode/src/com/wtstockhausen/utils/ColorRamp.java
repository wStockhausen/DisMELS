/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wtstockhausen.utils;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author wstockhausen
 */
public class ColorRamp {
    public static boolean debug = false;
    /**system color ramp: jet */
    public static String COLORRAMP_JET = "jet";
    /**system color ramp: heat */
    public static String COLORRAMP_HEAT = "heat";

    private static Color[] jet  = {Color.blue, Color.cyan, Color.yellow, Color.red};
    private static Color[] heat = {Color.blue, Color.red};
    private static Map<String,Color[]> userRamps = Collections.synchronizedMap(new HashMap<String,Color[]>());

    /**
     * Add a user-defined color ramp to those previously defined.
     * @param name - String with name of ramp
     * @param ramp - Color[] description of ramp
     */
     public static void addUserRamp(String name, Color[] ramp) {
         userRamps.put(name, ramp);
     }

    /**
     * Returns names of defined ramps as a Set.
     * @return - set of names of defined color ramps
     */
     public static Set<String> getRampNames() {
         TreeSet<String> ramps = new TreeSet<String>();
         ramps.add("heat");
         ramps.add("jet");
         Iterator<String> it = userRamps.keySet().iterator();
         while (it.hasNext()) ramps.add(it.next());
         return ramps;
     }

    /**
     * Returns user-defined ramps as a Map.
     * @return - Map of user-defined color ramps
     */
     public static Map<String,Color[]> getUserRamps() {
         return userRamps;
     }

    /*
     * Returns a java.awt.Color instance interpolated from the jet color scheme
     *  (blue,cyan,yellow,red) corresponding to 0<=x<=1.
     */
    public static Color jet(double x){
        if (debug) System.out.print("jet");
        return createColor(x,jet);
    }

    /*
     * Returns a java.awt.Color instance interpolated from the heat color scheme
     *  (blue,red) corresponding to 0<=x<=1.
     */
    public static Color heat(double x){
        if (debug) System.out.print("heat");
        return createColor(x,heat);
    }

    /*
     * Returns a java.awt.Color instance corresponding to 0<=x<=1
     *  interpolated from the color ramp identified by "ramp".
     * @param x    - position of desired color along ramp (0<=x<=1)
     * @param ramp - name of color ramp to use (String)
     * @return     - java.awt.Color
                     or null (if no ramp matching the String is defined)
     */
    public static Color createColor(double x,String ramp) {
        //try user-defined ramps first (so users can replace default ramps)
        Color[] clrs = userRamps.get(ramp);
        if (clrs!=null) {
            return createColor(x,clrs);
        }
        //try default ramps next
        if (ramp.equalsIgnoreCase("heat")) return heat(x);
        if (ramp.equalsIgnoreCase("jet")) return jet(x);
        //no matches to "ramp", so return null
        return null;
    }
    
    /*
     * Returns a java.awt.Color instance interpolated from the color scheme
     *  embodied in clrs corresponding to 0<=x<=1.
     */
    public static Color createColor(double x,Color[] clrs) {
        if (x<0) x=0;
        if (x>1) x=1;
        int r,g,b;
        x = (clrs.length-1)*x;//stretch x to index into clrs (runs 0 to clrs.length-1)
        int idx = Math.min(clrs.length-2,(int) Math.floor(x));//make sure idx+1<=clrs.length-1
        if (debug) System.out.print("["+idx+"]");
        r = (int) (clrs[idx].getRed()  +(x-idx)*(clrs[idx+1].getRed()  -clrs[idx].getRed()));
        g = (int) (clrs[idx].getGreen()+(x-idx)*(clrs[idx+1].getGreen()-clrs[idx].getGreen()));
        b = (int) (clrs[idx].getBlue() +(x-idx)*(clrs[idx+1].getBlue() -clrs[idx].getBlue()));
        if (debug) System.out.print("("+x/(clrs.length-1)+") = {"+r+","+g+","+b+"}\n");
        Color clr = new Color(r, g, b);
        return clr;
    }
}
