/*
 * ReleaseParametersArray.java
 *
 * Created on January 23, 2006, 2:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.topcomponents.LHSAttributesEditor;

/**
 *
 * @author William Stockhausen
 */
public class ReleaseParametersArray implements Cloneable {
    
    private double deltaX = 25000; //presumed meters
    private double deltaY = 25000; //presumed meters
    private double deltaZ = 100;  //presumed meters
    private double deltaT = 0;
    private double minZ   = 0;
    private double maxZ   = 500;
    private boolean createVerticalGrid = false;
    private boolean createTemporalGrid = false;
    private int numberReleaseTimes = 1;
    
    /**
     * Creates a new instance of ReleaseParametersArray
     */
    public ReleaseParametersArray() {
    }
    
    @Override
    public Object clone() {
        ReleaseParametersArray clone = null;
        try {
            clone = (ReleaseParametersArray) super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return clone;
    }
    
    public boolean getCreateTemporalGrid() {
        return createTemporalGrid;
    }
    
    public void setCreateTemporalGrid(boolean b) {
        createTemporalGrid = b;
    }
    
    public boolean getCreateVerticalGrid() {
        return createVerticalGrid;
    }
    
    public void setCreateVerticalGrid(boolean b) {
        createVerticalGrid = b;
    }
    
    public double getDeltaT() {
        return deltaT;
    }
    
    public void setDeltaT(double val) {
        deltaT = val;
    }
    
    public double getDeltaX() {
        return deltaX;
    }
    
    public void setDeltaX(double val) {
        deltaX = val;
    }
    
    public double getDeltaY() {
        return deltaY;
    }
    
    public void setDeltaY(double val) {
        deltaY = val;
    }
    
    public double getDeltaZ() {
        return deltaZ;
    }
    
    public void setDeltaZ(double val) {
        deltaZ = val;
    }
    
    public double getMinZ() {
        return minZ;
    }
    
    public void setMinZ(double val) {
        minZ = val;
    }
    
    public double getMaxZ() {
        return maxZ;
    }
    
    public void setMaxZ(double val) {
        maxZ = val;
    }
    
    public int getNumberReleaseTimes() {
        return numberReleaseTimes;
    }
    
    public void setNumberReleaseTimes(int val) {
        numberReleaseTimes = val;
    }
}
