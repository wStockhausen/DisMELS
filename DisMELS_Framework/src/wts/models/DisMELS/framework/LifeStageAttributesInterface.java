/*
 * LifeStageAttributesInterface.java
 */

package wts.models.DisMELS.framework;

import java.util.ArrayList;

/**
 * This class provides the base interface that all life stage attribute classes
 * must implement.
 * 
 * The standard attributes and associated keys defined here are (in order):
 * <ul>
 *  <li> typeName - "Life stage type name"
 *  <li> id - "ID"
 *  <li> parentID - "Parent ID"
 *  <li> origID - "Original ID"
 *  <li> startTime - "Start tie (s)"
 *  <li> time - "Time (s)"
 *  <li> horizType - "Horiz. position type"
 *  <li> vertType - "Vert. position type"
 *  <li> horizPos1 - "Horiz. position 1"
 *  <li> horizPos2 - "Horiz. position 2"
 *  <li> vertPos - "Vert. position"
 *  <li> bathym - "bathymetric depth"
 *  <li> gridCellID - "Grid Cell ID"
 *  <li> track - "track"
 *  <li> active - "Active status"
 *  <li> alive - "Alive status"
 *  <li> age - "Age (d)"
 *  <li> ageInStage - "Age in stage (d)"
 *  <li> number - "Number of individuals"
 * </ul>
 */
public interface LifeStageAttributesInterface extends LifeStageDataInterface {
    
    /** the number of life stage attributes defined in this interface */
    public static final int PROP_NumAtts = 19;
    
    /** property key for the life stage type name of the individual */
    public static final String PROP_typeName   = "Life stage type name";
    /** property key for the life stage ID of the individual */
    public static final String PROP_id         = "ID";
    /** property key for the life stage ID of the object's parent */
    public static final String PROP_parentID   = "Parent ID";
    /** property key for the life stage ID of the "original" individual */
    public static final String PROP_origID     = "Original ID";
    /** property key for the start time of the individual */
    public static final String PROP_startTime  = "Start time (s)";
    /** property key for the current time */
    public static final String PROP_time       = "Time (s)";
    /** property key for the current horizontal position type */
    public static final String PROP_horizType  = "Horiz. position type";
    /** property key for the current vertical position type */
    public static final String PROP_vertType   = "Vert. position type";
    /** property key for the current value of component 1 of the individual's horizontal position */
    public static final String PROP_horizPos1  = "Horiz. position 1";
    /** property key for the current value of component 2 of the individual's horizontal position */
    public static final String PROP_horizPos2  = "Horiz. position 2";
    /** property key for the current value of the individual's vertical position */
    public static final String PROP_vertPos    = "Vert. position";
    /** property key for the current value of thebathymetric depth at the individual's location */
    public static final String PROP_bathym     = "bathymetric depth";
    /** property key for the ROMS grid cell ID the individual is currently in */
    public static final String PROP_gridCellID = "Grid Cell ID";
    /** property key for the current track of the individual's location */
    public static final String PROP_track      = "track";
    /** property key for the individual's activity status */
    public static final String PROP_active     = "Active status";
    /** property key for the individual's life status */
    public static final String PROP_alive      = "Alive status";
    /** property key for the individual's current age */
    public static final String PROP_age        = "Age (d)";
    /** property key for the individual's age within its current life stage */
    public static final String PROP_ageInStage = "Age in stage (d)";
    /** property key for the number of individuals associated with this life stage "object" */
    public static final String PROP_number     = "Number of individuals";
    
    /**
     *  Creates an instance of a subclass.
     *
     * @param strv - array of values (as Strings) used to create the new instance. 
     * @return an object implementing LifeStageAttributesInterface 
     */
    public LifeStageAttributesInterface createInstance(final String[] strv);
    
    /**
     *  Sets values of a subclass from a String vector.
     *
     *@param strv - array of values (as Strings). 
     */
    public void setValues(final String[] strv);
    
    /**
     * Returns the life history stage attributes as an Object array.
     *
     *@return - Object[] of life history attributes.  Will be in same
     *          order as the keys returned by getKeys();
     */
    public Object[] getAttributes();
                
    /**
     * Returns the comma-delimited string corresponding to the attributes
     * to be used as a header for a csv file.  
     * 
     * This should be overriden by subclasses that add additional attributes, 
     * possibly calling super.getCSVHeaderGetShortNames() to get an initial header string 
     * to which additional field names could be appended.
     * 
     * Use getCSV() to get the string of actual attribute values.
     *
     *@return - String of CSV header names (short style)
     */
    public String getCSVHeaderShortNames();
                
    /**
     * Gets an array of the classes associated with the attribute values in the 
     * implementing Attributes class.
     * 
     * The order of classes should be the same as the order of values returned
     * by getAttributes(). Must be overridden by implementing classes to provide the array.
     * 
     * @return - an array of Class objects
     */
    public Class[] getClasses();

    /**
     * Gets an array of Strings with the short name associated with each attribute.
     * 
     * The order of classes should be the same as the order of values returned
     * by getAttributes(). Must be overridden by implementing classes.
     * 
     * @return - an array of Strings
     */
    public String[] getShortNames();

    /**
     * Gets the type name and attribute values as an ArrayList.
     * 
     * The type name (as a String) should be the first element of the ArrayList. 
     * The order of values should then be the same as the order of values returned
     * by getAttributes(). Must be overridden by implementing classes to provide the array.
     * 
     * @return - the array list.
     */
    public ArrayList getArrayList();
    
    /**
     * Returns the position (x,y,z) or (lon,lat,z) as a 3-element double[].  If the
     * horizType is Lat/Lon (Types.HORIZ_LL) the returned coordinates are
     * relative to NAD83 (Greenwich PM, range -180 to 180).
     * 
     * @return - double[] {x,y,z} or {lon,lat,z} or {I,J,K}
     */
    public double[] getGeometry();
                
    /**
     * Sets the horizontal position from a double[].  The
     * coordinates should be NAD83, if lon/lat.
     * 
     * @param pt - {x,y[,z]} or {lon,lat[,depth]}. [] is optional 
     */
    public void setGeometry(double[] pt);

    /**
     * Returns the "active" status of the individual associated with the attributes instance.
     * 
     * @return 
     */
    public boolean isActive();
    
    /**
     * Sets the "active" status of the individual associated with the attributes instance.
     * 
     * @param b - boolean indicating whether associated LHS is active or not
     */
    public void setActive(boolean b);

    /**
     * Returns the "alive" status of the individual associated with the attributes instance.
     * 
     * @return 
     */
    public boolean isAlive();


    /**
     * Sets the "alive" status.
     * 
     * @param b - boolean indicating whether associated individual is alive or not
     */
    public void setAlive(boolean b);

    /**
     * Returns the ID the individual associated with the attributes instance.
     * 
     * @return 
     */
    public long getID();

    /**
     * Returns the start time (in s from the ROMS model reference) 
     * for the individual associated with the attributes instance.
     * 
     * @return 
     */
    public double getStartTime();

    /**
     * Sets the start time (in s from the ROMS model reference) 
     * for the individual associated with the attributes instance.
     * 
     * @param t - new start time (in seconds)
     */
    public void setStartTime(double t);
}
