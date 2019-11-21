/*
 * DataTableModel.java
 *
 * Created on February 28, 2003, 10:05 AM
 */

package wts.models.DisMELS.gui;

/**
 *
 * @author  William Stockhausen
 */

import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import org.geotools.feature.*;
import wts.models.DisMELS.framework.LHSPointFeatureType;
import wts.models.DisMELS.framework.LifeStageAttributesInterface;

public class LHSPointFeaturesTableModel extends AbstractTableModel implements CollectionListener {
    
    /** Integer flag specifying column class is a String */
    public static final int TYPE_STRING  = 0;
    /** Integer flag specifying column class is a Boolean */
    public static final int TYPE_BOOLEAN = 1;
    /** Integer flag specifying column class is a Integer */
    public static final int TYPE_INTEGER = 2;
    /** Integer flag specifying column class is a Float */
    public static final int TYPE_FLOAT   = 4;
    /** Integer flag specifying column class is a Double */
    public static final int TYPE_DOUBLE  = 8;
    
    /** Integer flag indicating no last edit */
    public static final int EDIT_NONE = -1;
    /** Integer flag indicating last edit consisted of adding features */
    public static final int EDIT_ADD = 0;
    /** Integer flag indicating last edit consisted of removing features */
    public static final int EDIT_REMOVE = 1;
    
    /** the data source for the table */
    private ArrayList<Feature> fc    = null;
    
    /** flag indicating last type of edit (for undo capability) */
    private int lastEditType = EDIT_NONE;
    /** collection of last-edited features for undo capability */
    private FeatureCollection fcLastEdits = null;
    
    private final int colCount;
    private final ArrayList<String> keys;
    private final ArrayList<Class>  colClasses;
    private final ArrayList<String> colNames;
    
    /** Holds value of property editable. */
    private boolean tableEditable=true;
    
    /** Utility field used by bound properties. */
    private PropertyChangeSupport propertyChangeSupport =  new PropertyChangeSupport(this);
    
    /** flag to print debugging info */
    public static boolean debug = false;
    /** logger for class */
    private static final Logger logger = Logger.getLogger(LHSPointFeaturesTableModel.class.getName());
    
    /** 
     * Creates a new instance of DataTableModel 
     */
    public LHSPointFeaturesTableModel(LHSPointFeatureType ft, FeatureCollection featColl) throws IOException {
        super();
        LifeStageAttributesInterface atts = ft.getAttributes();
        colCount   = atts.getClasses().length;
        keys       = new ArrayList<>(colCount);
        keys.add("typeName");//typeName is NOT included in LifeStageAttributesInterface.getKeys()
        keys.addAll(Arrays.asList(atts.getKeys()));
        colClasses = new ArrayList<>(colCount);
        colClasses.addAll(Arrays.asList(atts.getClasses()));
        colNames   = new ArrayList<>(colCount);
        colNames.addAll(Arrays.asList(atts.getShortNames()));
        fc = new ArrayList<>(featColl.getCount());
        fc.addAll(featColl);
        fcLastEdits = FeatureCollections.newCollection();
    }
    
    /**
     * Returns the type of the last edit.
     * 
     * @return 
     */
    public int getLastEditType(){
        return lastEditType;
    }
    
    /**
     * Returns the last edited features as a FeatureCollection.
     * 
     * @return 
     */
    public FeatureCollection getLastEditedFeatures(){
        return fcLastEdits;
    }
    
    /**
     * Undo the last edit and returns a FeatureCollection of the affected features.
     * If the last edit type is of interest, the user should call getLastEditType() 
     * BEFORE calling this method to find out what type of edit was performed 
     * and will be reversed.
     * 
     * @return 
     */
    public FeatureCollection undoLastEdit(){
        if (lastEditType==EDIT_NONE){
            //don't undo anything, fcLast is empty
        } else if (lastEditType==EDIT_ADD){
            //remove the added features
            removeAll(fcLastEdits);
        } else if (lastEditType==EDIT_REMOVE){
            //add the removed features
            addAll(fcLastEdits);
        }
        return fcLastEdits;
    }

    /**
     * Adds a feature to table.
     * Fires a TableRowsInserted event.
     * 
     * Listeners can obtain the added Feature by calling getLastEditedFeatures()
     * on the event source to obtain a FeatureCollection containing the added Feature.
     * 
     * @param f 
     */
    public void add(Feature f){
        if (debug) logger.info("feature added");
        int z = fc.size();
        fc.add(f);
        fcLastEdits.clear();//clear possible feature edits
        fcLastEdits.add(f);
        lastEditType = EDIT_ADD;
        fireTableRowsInserted(z+1, z+1);
    }
    
    /**
     * Adds the collection of features to the table.
     * Fires a TableDataChanged event.
     * 
     * Listeners can obtain the added Features by calling getLastEditedFeatures()
     * on the event source to obtain a FeatureCollection containing the added Features.
     * 
     * @param featColl 
     */
    public void addAll(FeatureCollection featColl){
        if (debug) logger.info("collection added");
        fc.addAll(featColl);
        fcLastEdits.clear();
        fcLastEdits.addAll(featColl);
        lastEditType = EDIT_ADD;
        fireTableDataChanged();
    }
    
    /**
     * Removes the row corresponding to the Feature from the table.
     * Fires a TableRowsDeleted event.
     * 
     * Listeners can obtain the removed Feature by calling getLastEditedFeatures()
     * on the event source to obtain a FeatureCollection containing the removed Feature.
     * 
     * @param f 
     */
    public void remove(Feature f){
        if (debug) logger.info("feature removed");
        int row = fc.indexOf(f);
        fc.remove(f);
        fcLastEdits.clear();
        fcLastEdits.add(f);
        lastEditType = EDIT_REMOVE;
        fireTableRowsDeleted(row,row);
    }
    
    /**
     * Removes the collection of features from the table.
     * Fires a TableDataChanged event.
     * 
     * Listeners can obtain the removed Features by calling getLastEditedFeatures()
     * on the event source to obtain a FeatureCollection containing the removed Feature.
     * 
     * @param featColl - the collection of features to be removed
     */
    public void removeAll(FeatureCollection featColl){
        if (debug) logger.info("collection removed");
        fc.removeAll(featColl);
        fcLastEdits.clear();
        fcLastEdits.addAll(featColl);
        lastEditType = EDIT_REMOVE;
        fireTableDataChanged();
    }
    
    /**
     * Removes the row from the DataTable instance. 
     * Also removes the corresponding Feature from the original FeatureCollection.
     * Fires a TableRowsDeleted event. 
     * 
     * Listeners can obtain the removed Feature by calling getLastEditedFeatures()
     * on the event source to obtain a FeatureCollection containing the removed Feature.
     * 
     * @param row - the row corresponding to the Feature to remove
     * @return    - the removed Feature
     */
    public Feature removeRow(int row){
        if (debug) logger.info("feature added");
        Feature f = fc.remove(row);
        fcLastEdits.clear();
        fcLastEdits.add(f);
        lastEditType = EDIT_REMOVE;
        fireTableRowsDeleted(row, row);
        return f;
    }
    
    /**
     * Removes the rows from the DataTable instance. 
     * Also removes the corresponding Features from the original FeatureCollection.
     * Fires a TableDataChanged event.
     * 
     * Listeners can obtain the removed Feature by calling getLastEditedFeatures()
     * on the event source to obtain a FeatureCollection containing the removed Feature.
     * 
     * @param rows - the row indices corresponding to the Features to remove
     * @return  - the removed features
     */
    public FeatureCollection removeRows(int[] rows){
        if (debug) logger.info("Removing multiple rows");
        fcLastEdits.clear();
        for (int row : rows){
            Feature f = fc.get(row);
            fcLastEdits.add(f);
        }
        fc.removeAll(fcLastEdits);
        lastEditType = EDIT_REMOVE;
        fireTableDataChanged();//TODO: need to fire something else here to return removed feature to listeners?
        return fcLastEdits;
    }
    
    /**
     * Returns the row index associated with the given Feature.
     * 
     * @param f
     * @return - the row index corresponding to the Feature 
     */
    public int getRowIndex(Feature f){
        return fc.indexOf(f);
    }
    
    /**
     * Gets the key assigned to the column
     * 
     * @param col - column index
     * @return    - the data key
     */
    public String getColumnKey(int col){
        return keys.get(col);
    }
    
    /** 
     * Getter for property editable.
     * @return Value of property editable.
     */
    public boolean isTableEditable() {
        return tableEditable;
    }
    
    /** Setter for property editable.
     * @param editable New value of property editable.
     */
    public void setTableEditable(boolean editable) {
        boolean oldEditable = this.tableEditable;
        this.tableEditable = editable;
        propertyChangeSupport.firePropertyChange("tableEditable", 
                                                 oldEditable,
                                                 tableEditable);
    }
    
    /**
     * Sets the value at the indicated row and column.
     * 
     * @param obj
     * @param row
     * @param col 
     */
    @Override
    public void setValueAt(Object obj, int row, int col) {
        if (isCellEditable(row,col)){
            Feature f = fc.get(row);
            LifeStageAttributesInterface atts = (LifeStageAttributesInterface) f.getAttribute(1);
            if (debug) logger.info("SetValue for row "+atts.getCSV());
            atts.setValue(keys.get(col), obj);
            fireTableCellUpdated(row, col);
        }
    }
    
    /** Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    /** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
    @Override
    public Class getColumnClass(int i) {
        return (colClasses!=null) ? colClasses.get(i) : Object.class;
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colNames.get(columnIndex);
    }

    @Override
    public int getRowCount() {
        return  (fc!=null) ?  fc.size() : 0;
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return (col>0)&&tableEditable;
    }

    /**
     * Gets the value at the given row, column indices.
     * @param rowIndex
     * @param columnIndex
     * @return 
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Feature f = fc.get(rowIndex);
        LifeStageAttributesInterface atts = (LifeStageAttributesInterface)f.getAttribute(1);
        return (columnIndex==0) ? atts.getTypeName() : atts.getValue(keys.get(columnIndex));
    }
    
    /**
     * Method responds to events on the original feature collection.
     * 
     * @param ce 
     */
    @Override
    public synchronized void collectionChanged(CollectionEvent ce) {
        if (ce.getEventType()==CollectionEvent.FEATURES_ADDED){
            if (debug) logger.info("features added collection event");
            for (Feature f: ce.getFeatures()) {fc.add(f);}
            fireTableDataChanged();
        } else if (ce.getEventType()==CollectionEvent.FEATURES_REMOVED){
            if (debug) logger.info("features removed collection event");
            for (Feature f: ce.getFeatures()) {fc.remove(f);}
            fireTableDataChanged();
        } else if (ce.getEventType()==CollectionEvent.FEATURES_CHANGED){
            //no need to fire a change here
            if (debug) logger.info("features changed collection event");
        }
    }
    
}
