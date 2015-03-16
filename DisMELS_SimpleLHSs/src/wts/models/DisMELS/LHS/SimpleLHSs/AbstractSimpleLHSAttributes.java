/*
 * GenericLHSAttributes.java
 *
 * Created on January 18, 2006, 6:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.LHS.SimpleLHSs;

import java.beans.PropertyChangeSupport;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import wts.models.DisMELS.framework.AbstractLHSAttributes;
import wts.models.DisMELS.framework.LifeStageAttributesInterface;

/**
 *
 * @author William Stockhausen
 */
public abstract class AbstractSimpleLHSAttributes extends AbstractLHSAttributes {
    
    /**
     * Utility field used by bound properties.
     */
    protected PropertyChangeSupport propertySupport;

    /**
     * Creates a new instance of GenericLHSAttributes
     */
    protected AbstractSimpleLHSAttributes(String typeName) {
        super(typeName);
        createMap();
        propertySupport =  new PropertyChangeSupport(this);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public abstract LifeStageAttributesInterface createInstance(final String[] strv);
    
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
                    double v = (double)(dt.getTime()-refDate.getTime())/1000L;
                    setValue(PROP_startTime,v);
                } catch (ParseException ex1) {
                    Logger.getLogger(AbstractSimpleLHSAttributes.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new java.lang.NumberFormatException();
                }
            }
//            setValue(PROP_startTime,Double.valueOf(strv[j++]));
            String timeStr = strv[j++];
            try {
                double v = Double.valueOf(timeStr);
                setValue(PROP_time,v);
            } catch (java.lang.NumberFormatException ex) {
                try {
                    Date dt  = dateFormat.parse(timeStr);
                    double v = (double)(dt.getTime()-refDate.getTime())/1000L;
                    setValue(PROP_time,v);
                } catch (ParseException ex1) {
                    Logger.getLogger(AbstractSimpleLHSAttributes.class.getName()).log(Level.SEVERE, null, ex1);
                    throw new java.lang.NumberFormatException();
                }
            }
//            setValue(PROP_time,     Double.valueOf(strv[j++]));
            setValue(PROP_age,      Double.valueOf(strv[j++]));
            setValue(PROP_ageInStage,Double.valueOf(strv[j++]));
            setValue(PROP_size,     Double.valueOf(strv[j++]));
            setValue(PROP_number,   Double.valueOf(strv[j++]));
            setValue(PROP_horizPos1,Double.valueOf(strv[j++]));
            setValue(PROP_horizPos2,Double.valueOf(strv[j++]));
            setValue(PROP_vertPos,  Double.valueOf(strv[j++]));
            setValue(PROP_temp,     Double.valueOf(strv[j++]));
            setValue(PROP_salinity, Double.valueOf(strv[j++]));
            setValue(PROP_gridCellID, strv[j++]);
        } catch (java.lang.IndexOutOfBoundsException ex) {
            //@TODO: should throw an exception here that identifies the problem
            if (j!=21) {
                String str = "Missing attribute value for "+AbstractLHSAttributes.keys[j-2]+".\n"+
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
            String str = "Bad attribute value for "+AbstractLHSAttributes.keys[j-2]+".\n"+
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

    @Override
    public void createMap() {
        super.createMap();//no added attributes, so just call superclass method
    }

    /**
     * Gets type name and attribute values as an ArrayList.
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
     * Gets the atribute values as an Object array.
     * @return - the Object array.
     */
    @Override
    public Object[] getAttributes() {
        Object[] atts = new Object[keys.length];
        for (int i=0;i<keys.length;i++) {
            atts[i] = map.get(keys[i]);
        }
        return atts;
    }
    
    @Override
    public String[] getKeys() {
        return keys;
    }
    
    @Override
    public Class[] getClasses() {
        return classes;
    }

    @Override
    public String[] getShortNames() {
        return shortNames;
    }
    
    /**
     * Sets the value for the map object indicated by the key.
     * This overrides the superclass method to provide property change support.
     * 
     * @param key   - String giving key name
     * @param value - Object to be set as value
     */
    @Override
    public void setValue(String key, Object value) {
        if (map.containsKey(key)) {
            Object old = map.get(key);
            Object val = map.put(key,value);
            propertySupport.firePropertyChange(key,old,val);
        }
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
