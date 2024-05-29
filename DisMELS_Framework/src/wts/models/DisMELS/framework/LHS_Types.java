/*
 * LHS_Types.java
 *
 * Created on March 20, 2012
 */

package wts.models.DisMELS.framework;

import java.beans.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * The class definition for the group of defined LHS types (i.e., sub-stages).
 * This group is a singleton object (no need for more than 1 instance), which 
 * can be obtained from the static method LHS_Types.getInstance();
 * 
 * The instance listens for changes to the working directory via the GlobalInfo 
 * singleton and updates itself based on the LHS_Types.xml file found in the new
 * directory. 
 * 
 * The instance fires PropertyChanges to listeners when a new LHS_Type is added, 
 * when an existing LHS_Type is renamed or removed, and when an xml file is read.
 * 
 * @author William Stockhausen
 */
public class LHS_Types extends Object 
                       implements Serializable, 
                                  PropertyChangeListener,
                                  ExceptionListener {
    
    /** the name of the xml file to be read/written */
    private static final String xmlFN = "LHS_Types.xml";
    
    /** Identifier for PropertyChanges signaling that LHS types have changed */
    public static final String PROP_types          = "types";
    /** Identifier for PropertyChanges signaling that an LHS type has been added */
    public static final String PROP_addType        = "add type";
    /** Identifier for PropertyChanges signaling that an LHS type has been removed */
    public static final String PROP_removeType     = "remove type";
    /** Identifier for PropertyChanges signaling that an LHS type's name has changed */
    public static final String PROP_typeNameChange = "type name change";
    
    /** the singleton instance */
    private static LHS_Types inst = null;
    
    /** a logger for messages */
    private static Logger logger = Logger.getLogger(LHS_Types.class.getName());
    
    /**
     * Returns the singleton instance of this class.
     * 
     * @return 
     */
    public static LHS_Types getInstance(){
        if (inst==null) inst = new LHS_Types();
        return inst;
    }
    
//    /** */
    /** the map of LHS type names to LHS_Types */
    private Map<String,LHS_Type> mapTypes = new LinkedHashMap<>(16);
    
    /** the global info singleton */
    transient private GlobalInfo globalInfo = null;
    
    /** this provides support for PropertyChanges */
    transient private PropertyChangeSupport propertySupport;

    private LHS_Types() {
        logger.info("Instantiating singleton");
        globalInfo = GlobalInfo.getInstance();
        propertySupport = new PropertyChangeSupport(this);
        readXML();
    }
    
    /**
     * Saves the LHS_Types information to an xml file.
     */
    public final void writeXML() {
        try {
            String wdFN = globalInfo.getWorkingDir();
            logger.info("LHS_Types:Saving to xml "+wdFN+xmlFN);
            File f = new File(wdFN+xmlFN);
            FileOutputStream fos     = new FileOutputStream(f);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            XMLEncoder xe = new XMLEncoder(bos);
            xe.setExceptionListener(this);
            xe.writeObject(mapTypes);
            logger.info("SaveAs -- Done");
            xe.close();
        } catch (FileNotFoundException exc) {
            logger.info(exc.toString());
        }       
    }
    
    /**
     * Reads the LHS_Types information from an xml file.
     */
    public final void readXML() {
//        Set<String> kys1 = null;
        Map<String,LHS_Type> map1 = null;
        try {
            String wdFN = globalInfo.getWorkingDir();
            logger.info("LHS_Types.readXML():loading xml file '"+wdFN+xmlFN+"'");
            File f = new File(wdFN+xmlFN);
            FileInputStream fis     = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            XMLDecoder xd = new XMLDecoder(bis);
//            xd.setOwner(this);
            xd.setExceptionListener(this);
            Object obj = xd.readObject();
            try {
                map1 = (LinkedHashMap<String,LHS_Type>) obj;
            } catch (ClassCastException ex){
                map1 = (HashMap<String,LHS_Type>) obj;                
            }
            logger.info("done loading xml");
            xd.close();
        } catch (FileNotFoundException exc) {
            logger.info("-----LHS_Types.readXML(): "+exc.toString());
            map1 = new LinkedHashMap<>(16);//file not found, so reset map
        } catch (ArrayIndexOutOfBoundsException exc){
            logger.info("-----LHS_Types.readXML(): "+exc.toString());
            if (map1==null) map1 = new LinkedHashMap<>(16);
        }
        setTypes(map1);
    }
    
    /**
     * Resets the LHS_Types to an empty map.
     */
    public void reset(){
        Map<String,LHS_Type> map1 = new LinkedHashMap<>(16);
        setTypes(map1);
    }
    
    private void setTypes(Map<String,LHS_Type> newMap) {
        Map<String,LHS_Type> oldMap = mapTypes;
        mapTypes = newMap;
        LHS_Type type;
        for (String key: oldMap.keySet()) {
            type = oldMap.get(key);
            if (type!=null) type.removePropertyChangeListener(this);
        }
        for (String key: newMap.keySet()) {
            type = mapTypes.get(key);
            if (type!=null) {
                type.addPropertyChangeListener(this);
            } else {
                mapTypes.remove(key);//remove null object & key from map
            }
        }
        propertySupport.firePropertyChange(PROP_types, null, null);
    }
    
    /**
     * Gets the set of keys (life stage type names) for the map of life stage types.
     * 
     * @return 
     */
    public Set<String> getKeys() {
        return mapTypes.keySet();
    }
    
    /**
     * Add a life stage type to the map of types. The key for this life stage
     * is taken from the type's LHS name.
     * 
     * @param type 
     */
    public void addType(LHS_Type type) {
        type.addPropertyChangeListener(this);
        mapTypes.put(type.getLHSName(),type);
        propertySupport.firePropertyChange(PROP_addType,null,type);
    }
    
    /**
     * Gets the LHS_Type object associated with the given key (type name)
     * from the map of LHS_Types.
     * 
     * @param key - the type name
     * 
     * @return 
     */
    public LHS_Type getType(String key) {
        return mapTypes.get(key);
    }
    
    /**
     * Removes the LHS_Type associated with the key (type name) from the 
     * map of LHS_Types.
     * 
     * @param key 
     */
    public void removeType(String key) {
        LHS_Type type = mapTypes.get(key);
        removeType(type);
    }
    /**
     * Removes the given LHS_Type from the map of LHS_Types.
     * 
     * @param type 
     */
    public void removeType(LHS_Type type) {
        type.removePropertyChangeListener(this);
        mapTypes.remove(type.getLHSName());//remove key & object
        propertySupport.firePropertyChange(PROP_removeType,type,null);
    }
    
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * This method listens for name changes in existing individual LHS types,
     * as well as to the GlobalInfo singleton for changes in the working directory.
     * 
     * @param evt - PropertChangeEvent from one of the LHS_Type instances in map.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(LHS_Type.PROP_lhsName)) {
            String oldKey = (String) evt.getOldValue();
            String newKey = (String) evt.getNewValue();
            LHS_Type type = mapTypes.remove(oldKey);
            mapTypes.put(newKey,type);
            //fire a property change event indicating a change in the typeName
            //of one of the LHS_Type instances.
            propertySupport.firePropertyChange(PROP_typeNameChange,oldKey,newKey);
        } else if (evt.getPropertyName().equals(GlobalInfo.PROP_WorkingDirFN)) {
            logger.info("WorkingDirectory change detected.");
            readXML();
        }
    }

    @Override
    public void exceptionThrown(Exception e) {
        logger.info(e.toString());
    }

}
