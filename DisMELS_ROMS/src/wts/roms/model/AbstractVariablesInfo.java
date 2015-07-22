/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

/**
 * Abstract class representing a collection of AbstractVariableInfo instances.
 * 
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
     * @param mvi 
     */
    protected void addVariable(AbstractVariableInfo mvi){
        String name = mvi.getName();
        mvi.addPropertyChangeListener(this);
        mapAVI.put(name, mvi);
        if (throwPCEs) propertySupport.firePropertyChange(PROP_VARIABLE_ADDED,null,mvi);
    }

    /** 
     * Abstract class to add a VariableInfo instance to the collection, assigning it
     * the (internal) name and description provided.
     * @param name
     * @param desc 
     */
    protected abstract void addVariable(String name, String desc);

    /**
     * Returns the description associated with the variable name.
     *
     * @param name
     * @return - the name used in the ROMS dataset
     */
    public String getDescription(String name) {
        return mapAVI.get(name).getDescription();
    }

    /**
     * Returns the ROMS name (alias) associated with the variable name,
     * null if the name was not found.
     *
     * @param name
     * @return - the name used in the ROMS dataset
     */
    public String getNameInROMSDataset(String name) {
        String varname = null;
        AbstractVariableInfo mvi = mapAVI.get(name);
        if (mvi != null) {
            varname = mvi.getNameInROMSDataset();
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
     * Returns the variable information associated with a variable name.
     *
     * @param name = the variable's name
     * @return -
     */
    public AbstractVariableInfo getVariableInfo(String name){
        return mapAVI.get(name);
    }

    /**
     * Removes the variable info associated with the given name.
     * @param name
     * @return 
     */
    public AbstractVariableInfo removeVariable(String name){
        AbstractVariableInfo mvi = mapAVI.remove(name);
        if (mvi!=null) mvi.removePropertyChangeListener(this);
        if (throwPCEs) propertySupport.firePropertyChange(PROP_VARIABLE_REMOVED,mvi,null);
        return mvi;
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
