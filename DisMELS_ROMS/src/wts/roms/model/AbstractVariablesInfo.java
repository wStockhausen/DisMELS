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
 *
 * @author William.Stockhausen
 */
public abstract class AbstractVariablesInfo implements PropertyChangeListener {
    public static final String PROP_RESET = "RESET";

    public static final String PROP_VARIABLE_ADDED = "VARIABLE_ADDED";
    public static final String PROP_VARIABLE_REMOVED = "VARIABLE_REMOVED";
    public static final String PROP_VARIABLE_RENAMED = "VARIABLE_RENAMED";

    
    /** */
    protected final Map<String,AbstractVariableInfo> mapAVI; 
    
    /** support for throwing property changes */
    transient protected PropertyChangeSupport propertySupport;
    
    public AbstractVariablesInfo(int n) {
        mapAVI = new HashMap<>(n);
    }

    protected void addVariable(AbstractVariableInfo mvi){
        String name = mvi.getName();
        mvi.addPropertyChangeListener(this);
        mapAVI.put(name, mvi);
        propertySupport.firePropertyChange(PROP_VARIABLE_ADDED,null,mvi);
    }

    protected abstract void addVariable(String name, String desc);

    /**
     * Returns the description associated with the variable name.
     *
     * @return - the name used in the ROMS dataset
     */
    public String getDescription(String name) {
        return mapAVI.get(name).getDescription();
    }

    /**
     * Returns the ROMS name (alias) associated with the variable name,
     * null if the name was not found.
     *
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
        propertySupport.firePropertyChange(PROP_VARIABLE_REMOVED,mvi,null);
        return mvi;
    }


    /**
     * Resets the set of model variable info objects to "unchecked".
     */
    public void reset() {
        Iterator<String> keys = mapAVI.keySet().iterator();
        while (keys.hasNext()) {
            AbstractVariableInfo mvi = mapAVI.get(keys.next());
            mvi.setChecked(false);
        }
        propertySupport.firePropertyChange(OptionalModelVariablesInfo.PROP_RESET, null, null);
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
