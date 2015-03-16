/*
 * LHS_Type.java
 *
 * Created on April 3, 2006, 3:47 PM
 */

package wts.models.DisMELS.framework;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import wts.models.DisMELS.framework.LHS_Classes.LHS_ClassInfo;

/**
 * @author William Stockhausen
 */
public class LHS_Type extends Object implements Serializable {
    
    public static final String VALUE_NOT_SET        = "--not set--";
    
    public static final String PROP_lhsName         = "LHS name";
    public static final String PROP_nextLHSName     = "next LHS name";
    public static final String PROP_spawnedLHSName  = "spawned LHS name";
    public static final String PROP_lhsClass        = "LHS class";
    public static final String PROP_attributesClass = "attributes class";
    public static final String PROP_parametersClass = "parameters class";
    public static final String PROP_pointFTClass    = "point feature type class";
    public static final String PROP_nextLHSClass    = "next LHS class";
    public static final String PROP_spawnedLHSClass = "spawned LHS class";
    public static final String PROP_color           = "display color";
    
    /** name of LH stage */ 
    private String lhsName         = "";
    /** name of 'next' LH stage */ 
    private String nextLHSName     = "";
    /** name of spawned LH stage */ 
    private String spawnedLHSName  = "";
    /** class name of LH stage */ 
    private String lhsClass        = "";
    /** class name of LH stage attributes */ 
    private String attributesClass = "";
    /** class name of LH stage parameters */ 
    private String parametersClass = "";
    /** class name of LH stage point feature type */ 
    private String pointFTClass    = "";
    /** class name of 'next' LH stage */ 
    private String nextLHSClass    = "";
    /** class name of spawned LH stage */ 
    private String spawnedLHSClass = "";
    /** color used to identify individuals of this LH stage */ 
    private Color color            = java.awt.Color.GRAY;
    
    transient private PropertyChangeSupport propertySupport;
    
    public LHS_Type() {
        propertySupport = new PropertyChangeSupport(this);
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color c) {
        Color oldValue = color;
        color = c;
        propertySupport.firePropertyChange(PROP_color, oldValue, color);
    }
    
    /**
     * Gets name identifying this life history stage type.
     * 
     * @return - the name
     */
    public String getLHSName() {
        return lhsName;
    }
    
    /**
     * Sets the name identifying this life history stage.
     * 
     * @param value - the new name
     */
    public void setLHSName(String value) {
        String oldValue = lhsName;
        lhsName = value;
        propertySupport.firePropertyChange(PROP_lhsName, oldValue, lhsName);
    }
     
    /**
     * Gets name identifying the next life history stage.
     * 
     * @return - the name
     */
    public String getNextLHSName() {
        return  nextLHSName;
    }
    
    /**
     * Sets the name identifying the next life history stage.
     * 
     * @param value - the new name
     */
    public void setNextLHSName(String value) {
        String oldValue =  nextLHSName;
        nextLHSName = value;
        propertySupport.firePropertyChange(PROP_nextLHSName, oldValue,  nextLHSName);
    }
    
    /**
     * Gets name identifying the spawned life history stage.
     * 
     * @return - the name
     */
    public String getSpawnedLHSName() {
        return  spawnedLHSName;
    }
    
    /**
     * Sets the name identifying the spawned life history stage.
     * 
     * @param value - the new name
     */
    public void setSpawnedLHSName(String value) {
        String oldValue =  spawnedLHSName;
        spawnedLHSName = value;
        propertySupport.firePropertyChange(PROP_spawnedLHSName, oldValue,  spawnedLHSName);
    }
    
    /**
     * Gets the name of the class for this life history stage.
     * 
     * @return - the class name
     */
    public String getLHSClass() {
        return lhsClass;
    }
    
    /**
     * Sets the class for this life history stage by class name.
     * 
     * @param value - the new class name
     */
    public void setLHSClass(String value) {
        String oldValue = lhsClass;
        lhsClass = value;
        propertySupport.firePropertyChange(PROP_lhsClass, oldValue, lhsClass);
        
        //TODO: the following information should come from LHS_Classes
        GlobalInfo globalInfo = GlobalInfo.getInstance();
        LHS_ClassInfo lhsClassInfo = globalInfo.getLHSClassesInfo().getClassInfo(lhsClass);
        setAttributesClass(lhsClassInfo.attributesClass);
        setParametersClass(lhsClassInfo.parametersClass);
        setPointFeatureTypeClass(lhsClassInfo.pointFTClass);
    }
    
    /**
     * Gets the name of the attributes class for this life history stage.
     * 
     * @return - the class name
     */
    public String getAttributesClass() {
        return attributesClass;
    }
    
    /**
     * Sets the attributes class for this life history stage by class name.
     * 
     * @param value - the new class name
     */
    public void setAttributesClass(String value) {
        String oldValue = attributesClass;
        attributesClass = value;
        propertySupport.firePropertyChange(PROP_attributesClass, oldValue, attributesClass);
    }
    
    /**
     * Gets the name of the parameters class for this life history stage.
     * 
     * @return - the class name
     */
    public String getParametersClass() {
        return parametersClass;
    }
    
    /**
     * Sets the parameters class for this life history stage by class name.
     * 
     * @param value - the new class name
     */
    public void setParametersClass(String value) {
        String oldValue = parametersClass;
        parametersClass = value;
        propertySupport.firePropertyChange(PROP_parametersClass, oldValue, parametersClass);
    }
    
    /**
     * Gets the name of the next life stage class for this life history stage.
     * 
     * @return - the class name
     */
    public String getNextLHSClass() {
        return nextLHSClass;
    }
    
    /**
     * Sets the next life stage class for this life history stage by class name.
     * 
     * @param value - the new class name
     */
    public void setNextLHSClass(String value) {
        String oldValue = nextLHSClass;
        nextLHSClass = value;
        propertySupport.firePropertyChange(PROP_nextLHSClass, oldValue, nextLHSClass);
    }
    
    /**
     * Gets the name of the spawned life history class for this life history stage.
     * 
     * @return - the class name
     */
    public String getSpawnedLHSClass() {
        return spawnedLHSClass;
    }
    
    /**
     * Sets the spawned life stage class for this life history stage by class name.
     * 
     * @param value - the new class name
     */
    public void setSpawnedLHSClass(String value) {
        String oldValue = spawnedLHSClass;
        spawnedLHSClass = value;
        propertySupport.firePropertyChange(PROP_spawnedLHSClass, oldValue, spawnedLHSClass);
    }
    
    /**
     * Gets the name of the point feature type class for this life history stage.
     * 
     * @return - the class name
     */
    public String getPointFeatureTypeClass() {
        return pointFTClass;
    }
    
    /**
     * Sets the point feature type class for this life history stage by class name.
     * 
     * @param value - the new class name
     */
    public void setPointFeatureTypeClass(String value) {
        String oldValue = pointFTClass;
        pointFTClass = value;
        propertySupport.firePropertyChange(PROP_pointFTClass, oldValue, pointFTClass);
    }
    
    @Override
    public String toString() {
        return lhsName;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
}
