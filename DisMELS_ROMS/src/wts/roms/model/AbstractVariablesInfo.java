/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Abstract class representing a collection of AbstractVariableInfo instances.
 * 
 * Individual AVIs should be extracted using the "internal" DisMELS name assigned to
 * the variable, NOT the name in the ROMS dataset (the latter may be different in
 * different ROMS datasets).
 * 
 * @author William.Stockhausen
 */
public abstract class AbstractVariablesInfo implements PropertyChangeListener {
    /**String for PropertyChange indicating removal of ALL AbstractVariableInfo instances from collection. */
    public static final String PROP_RESET = "wts.roms.model.AbstractVariablesInfo.RESET";

    /**String for PropertyChanges indicating AbstractVariableInfo instance added to collection */
    public static final String PROP_VARIABLE_ADDED = "wts.roms.model.AbstractVariablesInfo.VARIABLE_ADDED";
    /**String for PropertyChanges indicating AbstractVariableInfo instance removed from collection */
    public static final String PROP_VARIABLE_REMOVED = "wts.roms.model.AbstractVariablesInfo.VARIABLE_REMOVED";
    /**String for PropertyChanges indicating AbstractVariableInfo instance renamed in collection */
    public static final String PROP_VARIABLE_RENAMED = "wts.roms.model.AbstractVariablesInfo.VARIABLE_RENAMED";

    /** flag to throw PropertyChange events */
    protected boolean throwPCEs = true;
    
    /**Collection of VariableInfo instances */
    protected final Map<String,AbstractVariableInfo> mapAVI; 
    
    /** support for throwing property changes */
    transient protected PropertyChangeSupport propertySupport;
    
    /** logger */
    private static final Logger logger = Logger.getLogger(AbstractVariablesInfo.class.getName());
    
    /**
     * Constructor for "n" AVIs.
     * 
     * @param n - the initial number ofAVIs to encapsulate
     */
    public AbstractVariablesInfo(int n) {
        mapAVI = new HashMap<>(n);
    }

    /**
     * Method called to clear all VariableInfo instances from collection.
     */
    protected abstract void reset();

    /**
     * Add a VariableInfo instance class to the collection.
     * 
     * The "internal" name (from avi.getName())must be used as the key to retrieve the
     * associated VariableInfo object (not the name in the ROMS dataset, which 
     * can be retrieved using avi.getNameInROMSDataset()).
     * 
     * @param avi 
     */
    protected void addVariable(AbstractVariableInfo avi){
        String name = avi.getName();
        avi.addPropertyChangeListener(this);
        mapAVI.put(name, avi);
        if (throwPCEs) propertySupport.firePropertyChange(PROP_VARIABLE_ADDED,null,avi);
    }

    /** 
     * Abstract method to add a VariableInfo subclass instance to the collection, assigning it
     * the (internal) name and description provided.
     * 
     * @param name - "internal" name used in DisMELS to refer to this variable
     * @param desc - a description of the variable
     */
    protected abstract void addVariable(String name, String desc);

    /**
     * Returns the description associated with the "internal" variable name.
     *
     * The "internal" name is the one used in DisMELS, not necessarily the name of
     * the variable in the ROMS dataset. If the name was not found, a warning 
     * message is displayed and returns a null.
     *
     * @param name - the "internal" name used in DisMELS
     * 
     * @return - the description of Variable, as a String
     */
    public String getDescription(String name) {
        String str = null;
        AbstractVariableInfo avi = mapAVI.get(name);
        if (avi != null) {
            str = avi.getDescription();
        } else {
//            JOptionPane.showMessageDialog(
//                    null,
//                    "No variable associated with internal name '"+name+"'.",
//                    "AbstractVariablesInfo.GetDescription: Error!",
//                    JOptionPane.ERROR_MESSAGE);
//            logger.warning("No variable associated with internal name '"+name+"'.");
        }
        return str;
    }

    /**
     * Returns the name used in the ROMS dataset associated with the 
     * "internal" variable name.
     * 
     * The "internal" name is the one used in DisMELS, not necessarily the name of
     * the variable in the ROMS dataset. If the name was not found, a warning 
     * message is displayed and returns a null.
     *
     * @param name - the "internal" name used in DisMELS
     * 
     * @return - the name used in the ROMS dataset
     */
    public String getNameInROMSDataset(String name) {
        String varname = null;
        AbstractVariableInfo avi = mapAVI.get(name);
        if (avi != null) {
            varname = avi.getNameInROMSDataset();
        } else {
//            JOptionPane.showMessageDialog(
//                    null,
//                    "No variable associated with internal name '"+name+"'.",
//                    "AbstractVariablesInfo.getNameInROMSDataset: Error!",
//                    JOptionPane.ERROR_MESSAGE);
//            logger.warning("No variable associated with internal name '"+name+"'.");
        }
        return varname;
    }

    /**
     * Returns the names of variables.
     *
     * @return - the names as a Set<String>
     */
    public Set<String> getNames() {
        Set<String> keySet = mapAVI.keySet();
        if (!keySet.isEmpty()) keySet = new TreeSet<>(keySet);
        return keySet;
    }

    /**
     * Returns the variable information associated with an "internal" variable name.
     * 
     * The "internal" name is the one used in DisMELS, not necessarily the name of
     * the variable in the ROMS dataset. If the name was not found, a warning 
     * message is displayed and returns a null.
     *
     * @param name - the variable's "internal" name
     * 
     * @return - the associated AbstractVariableInfo object
     */
    public AbstractVariableInfo getVariableInfo(String name){
        AbstractVariableInfo avi = mapAVI.get(name);
//        if (avi == null) {
//            JOptionPane.showMessageDialog(
//                    null,
//                    "No variable associated with internal name '"+name+"'.",
//                    "AbstractVariablesInfo.getVariableInfo: Error!",
//                    JOptionPane.ERROR_MESSAGE);
//            logger.warning("No variable associated with internal name '"+name+"'.");
//        }
        return avi;
    }

    /**
     * Removes the variable info associated with the given name.
     * @param name
     * @return 
     */
    public AbstractVariableInfo removeVariable(String name){
        AbstractVariableInfo avi = mapAVI.remove(name);
        if (avi!=null) avi.removePropertyChangeListener(this);
        if (throwPCEs) propertySupport.firePropertyChange(PROP_VARIABLE_REMOVED,avi,null);
        return avi;
    }


    /**
     * Resets all VariableInfo objects to "unchecked".
     */
    public void resetCheckedStatus() {
        Iterator<String> keys = mapAVI.keySet().iterator();
        while (keys.hasNext()) {
            AbstractVariableInfo mvi = mapAVI.get(keys.next());
            mvi.setChecked(false);
        }
    }
    
    /**
     * Adds a PropertyChangeListener instance to list of objects to be notified of
     * PropertyChanges.
     * @param listener - object to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener instance from list of objects to be notified of
     * PropertyChanges.
     * @param listener - object to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    @Override
    public abstract void propertyChange(PropertyChangeEvent evt);

    /**
     * Read in stored properties.
     * TODO: probably don't want to do this-->write to a file in the WorkingDirectory.
     *
     * @param p
     */
    public abstract void readProperties(Properties p);

    /**
     * Writes property values.
     *
     * @param p
     */
    public abstract void writeProperties(Properties p);
    
    /**
     * Writes property values.
     * 
     * @param p 
     */
    protected void writeProperties(Properties p, Class cls, String version){
        String clazz = cls.getName();
        p.put(clazz+"_version", version); 
        Set<String> names = mapAVI.keySet();
        p.put(clazz+"_"+"vars", Integer.toString(names.size()));
        int i = 0;
        for (String name: names){
            String str = clazz+"_var"+i;
            AbstractVariableInfo avi = mapAVI.get(name);
            avi.writeProperties(p,str,version);
            i++;
        }       
    }
}
