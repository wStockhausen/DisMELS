/*
 * DataTableModel.java
 *
 * Created on February 28, 2003, 10:05 AM
 */

package com.wtstockhausen.table;

/**
 *
 * @author  William Stockhausen
 */

import java.beans.PropertyChangeSupport;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class DataTableModel extends DefaultTableModel {
    
    public static int TYPE_STRING  = 0;
    public static int TYPE_BOOLEAN = 1;
    public static int TYPE_INTEGER = 2;
    public static int TYPE_FLOAT   = 4;
    public static int TYPE_DOUBLE  = 8;
    
    protected Class[] columnClasses;
    
    /** Holds value of property editable. */
    private boolean tableEditable=false;
    
    /** Utility field used by bound properties. */
    private PropertyChangeSupport propertyChangeSupport =  new PropertyChangeSupport(this);
    
    /** Creates a new instance of DataTableModel */
    public DataTableModel() {
        super();
    }
    
    public DataTableModel(Object[][] table, String[] columnNames, 
                             Class[] columnClasses) {
        super(table,columnNames);
        this.columnClasses = columnClasses;
    }
    
    public Class getColumnClass(int i) {
        return columnClasses[i];
    }
    
    public Class getColumnClasses(int i) {
        return columnClasses[i];
    }
    
    public Class[] getColumnClasses() {
        return columnClasses;
    }
    
    public String[] getColumnNames() {
        int nc = super.getColumnCount();
        String[] columnNames = new String[nc];
        for (int i=0;i<nc;i++) {
            columnNames[i] = super.getColumnName(i);
        }
        return columnNames;
    }
    
    public ArrayList getDataArray(){
        ArrayList al = new ArrayList();
        Vector    dv = super.getDataVector();
        int sz = dv.size();
        int nr = super.getRowCount();
        int nc = super.getColumnCount();
        for (int i=0;i<nr;i++){
            ArrayList alr = new ArrayList();
            Vector dvr = (Vector) dv.get(i);
            for (int j=0;j<nc;j++){
                alr.add(dvr.get(j));
            }
            al.add(alr);
        }
        return al;
    }
    public Vector getDataVector(){
        return super.getDataVector();
    }
    
    public boolean isCellEditable(int row, int col) {
        if (tableEditable) {
            return true;
        }
        return false;
    }
    
    /** Getter for property editable.
     * @return Value of property editable.
     */
    public boolean isTableEditable() {
        return this.tableEditable;
    }
    
    /** Setter for property array columnClasses.
     * @param headerRowCount Array of new values for property columnClasses.
     */
    public void setColumnClasses(Class[] classes) {
        Class[] oldClasses = this.columnClasses;
        this.columnClasses = classes;
        propertyChangeSupport.firePropertyChange("columnClasses", 
                                                 oldClasses, 
                                                 this.columnClasses);
    }
    
    /** Setter for property editable.
     * @param editable New value of property editable.
     */
    public void setTableEditable(boolean editable) {
        boolean oldEditable = this.tableEditable;
        this.tableEditable = editable;
        propertyChangeSupport.firePropertyChange("tableEditable", 
                                                 new Boolean(oldEditable), 
                                                 new Boolean(tableEditable));
    }
    
    public void setValueAt(Object obj, int row, int col) {
        if (!tableEditable) return;
        if (obj.getClass()==columnClasses[col]) {
            super.setValueAt(obj,row,col);
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
    
}
