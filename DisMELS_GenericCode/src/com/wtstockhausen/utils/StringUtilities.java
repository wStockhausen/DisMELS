/*
 * StringUtilities.java
 *
 * Created on January 14, 2004, 5:18 PM
 */

package com.wtstockhausen.utils;

/** This class provides generic utilities for string manipulation.
 *
 * @author William Stockhausen
 */
public class StringUtilities {
    
    /** Creates a new instance of StringUtilities */
    protected StringUtilities() {
    }
    
    /** Compresses a string by removing all spaces & tabs
     * @param str - string to compress
     * @return string with all spaces & tabs removed
     */    
    public static String compress(String str) {
        String strp = compress(str,"\t");
        return compress(strp," ");
    }
    
    /** Compresses a string by removing instances of a regular expression
     * @param str - string to compress
     * @param regex - regular expression to remove on compression
     * @return string with instances of the regex removed
     */    
    public static String compress(String str, String regex) {
        String[] strv = str.split(regex);
        String sc = "";
        for (int i=0;i<strv.length;i++) sc = sc.concat(strv[i]);
        return sc;
    }
    
    /** Checks a string for instances of a regular expression
     * @param str - string to test
     * @param regex - regular expression to remove on compression
     * @return boolean: true if regex is in string, false otherwise
     */    
    public static boolean contains(String str, String regex) {
        boolean res = false;
        if (str.indexOf(regex) > -1) res = true;
        return res;
    }
}
