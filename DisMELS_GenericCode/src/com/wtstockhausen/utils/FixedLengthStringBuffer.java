/*
 * FixedLengthStringBuffer.java
 *
 * Created on December 6, 2002, 1:43 PM
 */

/**
 *
 * @author  Wiliam Stockhausen
 */

package com.wtstockhausen.utils;

public class FixedLengthStringBuffer {
    
    private java.lang.StringBuffer sb;
    private int iCap;
    private char cBlank;

/** Creates a new instance of FixedLengthStringBuffer */
    public FixedLengthStringBuffer(String str) {
        sb     = new java.lang.StringBuffer(str);
        iCap   = sb.length();
        String strBlank = " ";
        cBlank = strBlank.charAt(0);
    }
    
    public void fillWithChar(char c) {
        for (int i=0;i<iCap;i++){
            sb.setCharAt(i,c);
        }
    }
    
    public void insert(int offset, String str) {
        int iMax = java.lang.Math.min(iCap,str.length()+offset);
        for (int i=offset;i<iMax;i++){
            char c = str.charAt(i-offset);
            sb.setCharAt(i,c);
        }
    }
    
    public int capacity() {
        return iCap;
    }
    
    public void reset(){
        fillWithChar(cBlank);
    }
    
    public String toString() {
        return sb.toString();
    }
}
