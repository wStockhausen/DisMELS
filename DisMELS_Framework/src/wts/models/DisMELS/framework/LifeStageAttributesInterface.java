/*
 * LifeStageAttributesInterface.java
 *
 * Created on January 19, 2006, 11:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.framework;

import java.util.ArrayList;

/**
 * This class provides the interface that all life stage attribute classes
 * must implement.
 * 
 * @author William Stockhausen
 */
public interface LifeStageAttributesInterface extends LifeStageDataInterface {
    
    public static final String PROP_typeName   = "Life stage type name";
    public static final String PROP_id         = "ID";
    public static final String PROP_parentID   = "Parent ID";
    public static final String PROP_origID     = "Original ID";
    public static final String PROP_startTime  = "Start time (s)";
    public static final String PROP_time       = "Time (s)";
    public static final String PROP_horizType  = "Horiz. position type";
    public static final String PROP_vertType   = "Vert. position type";
    public static final String PROP_horizPos1  = "Horiz. position 1";
    public static final String PROP_horizPos2  = "Horiz. position 2";
    public static final String PROP_vertPos    = "Vert. position";
    public static final String PROP_gridCellID = "Grid Cell ID";
    public static final String PROP_track      = "track";
    public static final String PROP_active     = "Active status";
    public static final String PROP_alive      = "Alive status";
    public static final String PROP_attached   = "Attached status";
    public static final String PROP_age        = "Age (d)";
    public static final String PROP_ageInStage = "Age in stage (d)";
    public static final String PROP_number     = "Number of individuals";
    
    /**
     *  Creates an instance of a subclass.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     */
    public LifeStageAttributesInterface createInstance(final String[] strv);
    
    /**
     *  Sets values of a subclass from a String vector.
     *
     *@param strv - array of values (as Strings). 
     */
    public void setValues(final String[] strv);
    
    /**
     * Returns the life history stage atributes as an Object array.
     *
     *@return - Object[] of life history attributes.  Will be in same
     *          order as the keys returned by getKeys();
     */
    public Object[] getAttributes();
                
    /**
     * Returns the comma-delimited string corresponding to the attributes
     * to be used as a header for a csv file.  
     * This should be overriden by subclasses that add additional attributes, 
     * possibly calling super.getCSVHeaderGetShortNames() to get an initial header string 
     * to which additional field names could be appended.
     * Use getCSV() to get the string of actual attribute values.
     *
     *@return - String of CSV header names (short style)
     */
    public String getCSVHeaderShortNames();
                
    public Class[] getClasses();

    public String[] getShortNames();

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
