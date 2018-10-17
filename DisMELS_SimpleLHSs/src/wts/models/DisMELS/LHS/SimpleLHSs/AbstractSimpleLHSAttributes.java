/*
 * AbstractSimpleLHSAttributes.java
 */

package wts.models.DisMELS.LHS.SimpleLHSs;

import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.LifeStageAttributesInterface;
import wts.models.DisMELS.framework.Types;

/**
 * This abstract class provides a base class that is extended in the 
 * Simple...LHSAttributes classes to provide concrete classes 
 * representing the attributes for the "simple" DisMELS life stages.
 */
public abstract class AbstractSimpleLHSAttributes 
                        implements LifeStageAttributesInterface {
    
    /** new boolean attribute indicating whether or not individual is "attached" to bottom */
    public final static String PROP_attached   = "Attached";
    /** new double attribute reflecting individual size (in cm, nominally) */
    public final static String PROP_size       = "Size (cm)";
    /** new double attribute reflecting in situ temperature */
    public final static String PROP_temp       = "Temperature";
    /** new double attribute reflecting in situ salinity */
    public final static String PROP_salinity   = "Salinity";
        
    /** output format for dates ("yyyy-MM-dd HH:mm:ss") */
    protected static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /** output format for decimal numbers ("#.#####") */
    protected static final DecimalFormat decFormat = new DecimalFormat("#.#####");
    
    /* String array with property names */
    protected static final String[] keys=  new String[]{PROP_id,
                                                        PROP_parentID,
                                                        PROP_origID,
                                                        PROP_horizType,
                                                        PROP_vertType,
                                                        PROP_active,
                                                        PROP_alive,
                                                        PROP_attached,
                                                        PROP_startTime,
                                                        PROP_time,
                                                        PROP_age,
                                                        PROP_ageInStage,
                                                        PROP_size,
                                                        PROP_number,
                                                        PROP_horizPos1,
                                                        PROP_horizPos2,
                                                        PROP_vertPos,
                                                        PROP_bathym,
                                                        PROP_temp,
                                                        PROP_salinity,
                                                        PROP_gridCellID};
    /* String array with field names for shapefiles */
    protected static final String[] shortNames =  new String[]{"typeName",
                                                                "id",
                                                                "parentID",
                                                                "origID",
                                                                "horizType",
                                                                "vertType",
                                                                "active",
                                                                "alive",
                                                                "attached",
                                                                "startTime",
                                                                "time",
                                                                "age",
                                                                "ageInStage",
                                                                "size",
                                                                "number",
                                                                "horizPos1",
                                                                "horizPos2",
                                                                "vertPos",
                                                                "bathym",
                                                                "temp",
                                                                "salinity",
                                                                "gridCellID"};
    /* class list corresponding to shortNames for shapefile input/output */
    protected static final Class[] classes = new Class[]{String.class,
                                                        Long.class,
                                                        Long.class,
                                                        Long.class,
                                                        Integer.class,
                                                        Integer.class,
                                                        Boolean.class,
                                                        Boolean.class,
                                                        Boolean.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        Double.class,
                                                        String.class};
    /* LHS type name assigned to instance*/
    protected String typeName;
    /* attributes map */
    protected HashMap<String,Object> map;
    
    /**
     * Utility field used by bound properties.
     */
    protected PropertyChangeSupport propertySupport;
    
    /** 
     * Assigns the LHS type name to the constructed subclass instance.
     * Subclasses should call this constructor with a valid LHS type name from
     * all constructors to set the type name.
     * 
     *@param typeName - the LHS type name as a String.
     */
    protected AbstractSimpleLHSAttributes(String typeName) {
       this.typeName = typeName;
        createMap();
        propertySupport =  new PropertyChangeSupport(this);
    }
    
    //Abstract methods should be overriden by inheriting classes
    
    /**
     * Sets attribute values to those of input String[].
     * @param strv - String[] of attribute values.
     */
    @Override
    public void setValues(final String[] strv) {
        int j = 1;
        try {
            setValue(PROP_id,       Long.valueOf(strv[j++]));
            setValue(PROP_parentID, Long.valueOf(strv[j++]));
            setValue(PROP_origID,   Long.valueOf(strv[j++]));
            setValue(PROP_horizType,Integer.valueOf(strv[j++]));
            setValue(PROP_vertType, Integer.valueOf(strv[j++]));
            setValue(PROP_active,   Boolean.valueOf(strv[j++]));
            setValue(PROP_alive,    Boolean.valueOf(strv[j++]));
            setValue(PROP_attached, Boolean.valueOf(strv[j++]));
            String startTimeStr = strv[j++];
            try {
                double v = Double.valueOf(startTimeStr);
                setValue(PROP_startTime,v);
            } catch (java.lang.NumberFormatException ex) {
                try {
                    Date dt  = dateFormat.parse(startTimeStr);
                    double v = (double)(dt.getTime()-GlobalInfo.getInstance().getRefDate().getTime())/1000L;
                    setValue(PROP_startTime,v);
                } catch (ParseException ex1) {
                    Logger.getLogger(AbstractSimpleLHSAttributes.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new java.lang.NumberFormatException();
                }
            }
            String timeStr = strv[j++];
            try {
                double v = Double.valueOf(timeStr);
                setValue(PROP_time,v);
            } catch (java.lang.NumberFormatException ex) {
                try {
                    Date dt  = dateFormat.parse(timeStr);
                    double v = (double)(dt.getTime()-GlobalInfo.getInstance().getRefDate().getTime())/1000L;
                    setValue(PROP_time,v);
                } catch (ParseException ex1) {
                    Logger.getLogger(AbstractSimpleLHSAttributes.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new java.lang.NumberFormatException();
                }
            }
            setValue(PROP_age,      Double.valueOf(strv[j++]));
            setValue(PROP_ageInStage,Double.valueOf(strv[j++]));
            setValue(PROP_size,     Double.valueOf(strv[j++]));
            setValue(PROP_number,   Double.valueOf(strv[j++]));
            setValue(PROP_horizPos1,Double.valueOf(strv[j++]));
            setValue(PROP_horizPos2,Double.valueOf(strv[j++]));
            setValue(PROP_vertPos,  Double.valueOf(strv[j++]));
            setValue(PROP_bathym,   Double.valueOf(strv[j++]));
            setValue(PROP_temp,     Double.valueOf(strv[j++]));
            setValue(PROP_salinity, Double.valueOf(strv[j++]));
            setValue(PROP_gridCellID, strv[j++]);
        } catch (java.lang.IndexOutOfBoundsException ex) {
            //@TODO: should throw an exception here that identifies the problem
            if (j!=21) {
                String str = "Missing attribute value for "+keys[j-2]+".\n"+
                             "Prior values are ";
                for (int i=0;i<(j-2);i++) str = str+strv[i]+" ";
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        str,
                        "Error setting attribute values:",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                throw ex;
            }
        } catch (java.lang.NumberFormatException ex) {
            String str = "Bad attribute value for "+keys[j-2]+".\n"+
                         "Value was '"+strv[j-1]+"'.\n"+
                         "Entry was '";
            try {
                for (int i=0;i<19;i++) {
                    if ((strv[i]!=null)&&(!strv[i].isEmpty())) {
                        str = str+strv[i]+", ";
                    } else {
                        str = str+"<missing_value>, ";
                    }
                }
                if ((strv[19]!=null)&&(!strv[19].isEmpty())) {
                    str = str+strv[19]+"'.";
                } else {
                    str = str+"<missing_value>'.";
                }
            }  catch (java.lang.IndexOutOfBoundsException ex1) {
                //do nothing
            }
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    str,
                    "Error setting attribute values:",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            throw ex;
        }
    }
    
    /**
     *  Creates an instance of a subclass.
     *
     *@param strv - array of values (as Strings) used to create the new instance. 
     */
    @Override
    public abstract LifeStageAttributesInterface createInstance(final String[] strv);
    
    /**
     * This creates the basic attributes map. Subclasses should override this 
     * method as appropriate, possibly calling super.createMap() to create
     * this map as super.map.  Subclass-specific attributes could then be mapped
     * in a subclass-specific map.
     */
    protected void createMap() {
        map = new HashMap<String,Object>(keys.length+2,1);
        for (int i=0;i<keys.length;i++) {
            map.put(keys[i],null);
        }
        map.put(PROP_id,        new Long(-1));
        map.put(PROP_parentID,  new Long(-1));
        map.put(PROP_origID,    new Long(-1));
        map.put(PROP_horizType, new Integer(0));
        map.put(PROP_vertType,  new Integer(0));
        map.put(PROP_active,    false);
        map.put(PROP_alive,     true);
        map.put(PROP_attached,  false);
        map.put(PROP_startTime, new Double(0));
        map.put(PROP_time,      new Double(0));
        map.put(PROP_age,       new Double(0));
        map.put(PROP_ageInStage,new Double(0));
        map.put(PROP_size,      new Double(0));
        map.put(PROP_number,    new Double(1));
        map.put(PROP_horizPos1, new Double(0));
        map.put(PROP_horizPos2, new Double(0));
        map.put(PROP_vertPos,   new Double(0));
        map.put(PROP_bathym,    new Double(0));
        map.put(PROP_temp,      new Double(-1));
        map.put(PROP_salinity,  new Double(-1));
        map.put(PROP_gridCellID,"");
    }

    /**
     * Gets an array of the classes associated with the attribute values in the 
     * implementing Attributes class.
     * 
     * The order of classes should be the same as the order of values returned
     * by getAttributes(). Must be overridden by implementing classes to provide the array.
     * 
     * @return - an array of Class objects
     */
    @Override
    public Class[] getClasses() {
        return classes;
    }

    /**
     * Gets an array of Strings with the short name associated with each attribute.
     * 
     * The order of classes should be the same as the order of values returned
     * by getAttributes(). Must be overridden by implementing classes.
     * 
     * @return - an array of Strings
     */
    @Override
    public String[] getShortNames() {
        return shortNames;
    }

    /**
     * Gets the type name and attribute values as an ArrayList.
     * 
     * The type name (as a String) should be the first element of the ArrayList. 
     * The order of values should then be the same as the order of values returned
     * by getAttributes(). Must be overridden by implementing classes to provide the array.
     * 
     * @return - the array list.
     */
    @Override
    public ArrayList getArrayList() {
        ArrayList a = new ArrayList();
        a.add(typeName);
        for (int i=0;i<keys.length;i++) {
            a.add(getValue(keys[i]));
        }
        return a;
    }
    
    /**
     * Gets the value of the "active" attribute.
     * 
     * @return - boolean indicating whether associated LHS is active or not
     */
    @Override
    public boolean isActive() {
        return getValue(PROP_active,b);
    }
    
    /**
     * Sets "active" value.
     * 
     * @param b - boolean indicating whether associated LHS is active or not
     */
    @Override
    public void setActive(boolean b) {
        setValue(PROP_active,b);
    }

    /**
     * Gets the value of the "alive" attribute.
     * 
     * @return - boolean indicating whether associated LHS is alive or not
     */
    @Override
    public boolean isAlive() {
        return getValue(PROP_alive,b);
    }

    /**
     * Sets "alive" value.
     * 
     * @param b - boolean indicating whether associated LHS is alive or not
     */
    @Override
    public void setAlive(boolean b) {
        setValue(PROP_alive,b);
    }

    /**
     * Gets the ID for the LHS.
     * 
     * @return - the ID, as a long
     */
    @Override
    public long getID() {
        return getValue(PROP_id,l);
    }

    /**
     * Gets the start time for the LHS.
     * 
     * @return - start time (in seconds)
     */
    @Override
    public double getStartTime() {
        return getValue(PROP_startTime,d);
    }

    /**
     * Sets start time for LHS.
     * 
     * @param t - new start time (in seconds)
     */
    @Override
    public void setStartTime(double t) {
        setValue(PROP_startTime,t);
    }
    
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//      The following are implemented to extend LifeStageAttributesInterface
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     *  This method should be overridden by extending classes.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    /**
     *  Returns attribute values as an Object array.
     *  Subclasses should order the values in the array
     *  in the same order as in the keys array.
     */
    @Override
    public Object[] getAttributes() {
        Object[] atts = new Object[keys.length];
        for (int i=0;i<keys.length;i++) {
            atts[i] = map.get(keys[i]);
        }
        return atts;
    }
    
    /**
     * Returns a CSV string representation of the attribute values.
     * This method should be overriden by subclasses that add additional attributes, 
     * possibly calling super.getCSV() to get an initial csv string to which 
     * additional field values could be appended.
     * 
     *@return - CSV string attribute values
     */
    @Override
    public String getCSV() {
        String str = typeName+cc
                    +this.getValue(PROP_id,l).toString()+cc
                    +this.getValue(PROP_parentID,l).toString()+cc
                    +this.getValue(PROP_origID,l).toString()+cc
                    +this.getValue(PROP_horizType,i).toString()+cc
                    +this.getValue(PROP_vertType,i).toString()+cc
                    +this.getValue(PROP_active,b).toString()+cc
                    +this.getValue(PROP_alive,b).toString()+cc
                    +this.getValue(PROP_attached,b).toString()+cc
                    +this.getValue(PROP_startTime,d).toString()+cc
                    +this.getValue(PROP_time,d).toString()+cc
                    +decFormat.format(this.getValue(PROP_age,d))+cc
                    +decFormat.format(this.getValue(PROP_ageInStage,d))+cc
                    +decFormat.format(this.getValue(PROP_size,d))+cc
                    +decFormat.format(this.getValue(PROP_number,d))+cc
                    +decFormat.format(this.getValue(PROP_horizPos1,d))+cc
                    +decFormat.format(this.getValue(PROP_horizPos2,d))+cc
                    +decFormat.format(this.getValue(PROP_vertPos,d))+cc
                    +decFormat.format(this.getValue(PROP_bathym,d))+cc
                    +decFormat.format(this.getValue(PROP_temp,d))+cc
                    +decFormat.format(this.getValue(PROP_salinity,d))+cc
                    +this.getValue(PROP_gridCellID,s);
        return str;
    }
                
    /**
     * Returns the comma-delimited string corresponding to the attributes
     * to be used as a header for a csv file.  
     * 
     * This should be overriden by subclasses that add additional attributes, 
     * possibly calling super.getCSVHeader() to get an initial header string 
     * to which additional field names could be appended.
     * Use getCSV() to get the string of actual attribute values.
     *
     *@return - String of CSV header names
     */
    @Override
    public String getCSVHeader() {
        String str = "LHS type name";
        int n = keys.length;
        for (int i=0;i<n;i++) {
            str = str+cc+keys[i];
        }
        return str;
    }
                
    /**
     * Returns the comma-delimited string corresponding to the attributes
     * to be used as a header for a csv file.  
     * 
     * This should be overriden by subclasses that add additional attributes, 
     * possibly calling super.getCSVHeaderGetShortNames() to get an initial header string 
     * to which additional field names could be appended.
     * Use getCSV() to get the string of actual attribute values.
     *
     *@return - String of CSV header names (short style)
     */
    @Override
    public String getCSVHeaderShortNames() {
        String str = shortNames[0];//this is "typeName"
        int n = shortNames.length;
        for (int i=1;i<n;i++) {
            str = str+cc+shortNames[i];
        }
        return str;
    }
    
    /**
     * Gets the LHS type name assigned to the instance.
     *
     * @return - the LHS type name as a String.
     */
    @Override
    public String getTypeName() {return typeName;}
                
    /**
     * Returns the position (x,y,z) or (lon,lat,z) as a 3-element double[].  If the
     * horizType is Lat/Lon (Types.HORIZ_LL) the returned coordinates are
     * relative to NAD83 (Greenwich PM, range -180 to 180).
     * 
     * @return - double[] {x,y,z} or {lon,lat,z} or {I,J,K}
     */
    @Override
    public double[] getGeometry() {
        int vertType=0;
        double x=0,y=0,z=0;
        vertType = getValue(PROP_vertType,vertType);
        x = getValue(PROP_horizPos1,x);
        y = getValue(PROP_horizPos2,y);
        z = getValue(PROP_vertPos,z);
        if (vertType==Types.VERT_H) z = -z;
        return new double[]{x,y,z};
    }
                
    /**
     * Sets the horizontal position from a double[].  
     * 
     * The coordinates should be NAD83, if lon/lat.
     * 
     * @param pt - {x,y} or {lon,lat}. 
     */
    @Override
    public void setGeometry(double[] pt) {
        map.put(PROP_horizPos1, pt[0]);
        map.put(PROP_horizPos2, pt[1]);
    }

    
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
//      The following are implemented to extend LifeStageDataInterface
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    /**
     * Gets the parameter keys.
     * 
     * This method is abstract to force dispatch of method to overriding method
     * in subclasses.
     * 
     * @return - keys as String array.
     */
    @Override
    public String[] getKeys() {
        return keys;
    }
    
    @Override
    public Boolean getValue(String key, Boolean b) {
        Boolean v = null;
        try {
            v = (Boolean) map.get(key);
        } catch (ClassCastException exc) {}
        return v;
    }

    @Override
    public Double getValue(String key, Double d) {
        Double v = null;
        try {
            v = (Double) map.get(key);
        } catch (ClassCastException exc) {}
        return v;
    }

    @Override
    public Integer getValue(String key, Integer i) {
        Integer v = null;
        try {
            v = (Integer) map.get(key);
        } catch (ClassCastException exc) {}
        return v;
    }

    @Override
    public Long getValue(String key, Long l) {
        Long v = null;
        try {
            v = (Long) map.get(key);
        } catch (ClassCastException exc) {}
        return v;
    }

    @Override
    public String getValue(String key, String s) {
        String v = null;
        try {
            v = (String) map.get(key);
        } catch (ClassCastException exc) {}
        return v;
    }

    @Override
    public boolean getValue(String key, boolean b) throws ClassCastException {
        boolean v = ((Boolean) map.get(key)).booleanValue();
        return v;
    }

    @Override
    public double getValue(String key, double d) throws ClassCastException {
        double v = ((Double) map.get(key)).doubleValue();
        return v;
    }

    @Override
    public int getValue(String key, int i) throws ClassCastException {
        int v = ((Integer) map.get(key)).intValue();
        return v;
    }

    @Override
    public long getValue(String key, long l) throws ClassCastException {
        long v = ((Long) map.get(key)).longValue();
        return v;
    }

    @Override
    public Object getValue(String key) {
        return map.get(key);
    }
    
    /**
     * Abstract method to set attribute value identified by the key.
     * This method is abstract to allow subclasses that want to use property
     * change support to dispatch to their method from a method call on an
     * AbstractLHSAttribures instance.
     * @param key   - key identifying attribute to be set
     * @param value - value to set
     */
    @Override
    public void setValue(String key, Object value) {
        if (map.containsKey(key)) {
            Object old = map.get(key);
            Object val = map.put(key,value);
            propertySupport.firePropertyChange(key,old,val);
        }
    }
    
    @Override
    public void setValue(String key, double value) {
        setValue(key,new Double(value));
    }
    
    @Override
    public void setValue(String key, float value) {
        setValue(key,new Float(value));
    }
    
    @Override
    public void setValue(String key, int value) {
        setValue(key,new Integer(value));
    }
    
    @Override
    public void setValue(String key, long value) {
        setValue(key,new Long(value));
    }
    
    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertySupport.removePropertyChangeListener(l);
    }
}
