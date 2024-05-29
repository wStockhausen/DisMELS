/*
 * DateTimeTableModel.java
 * 
 * Created on Jul 11, 2007, 5:10:05 AM
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gui;

import java.util.Vector;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author wstockhausen
 */
public class DateTimeFileTableModel  extends AbstractTableModel {
        
    public static final int dirCol  = 8;
    public static final int fileCol = 7;
    public static final int otCol  = 0;
    public static final int yrCol  = 1;
    public static final int monCol = 2;
    public static final int domCol = 3;
    public static final int hrCol  = 4;
    public static final int minCol = 5;
    public static final int secCol = 6;
    
    public static boolean debug = false;
    
    protected String[] columnNames = new String[] {"Ocean Time",
                                                   "Year","Month","Day",
                                                   "Hour","Min","Sec",
                                                    "File name",
                                                    "Directory"};
    protected Vector<DateTimeFileRowModel> rows;
    

    public DateTimeFileTableModel() {
        rows = new Vector<DateTimeFileRowModel>();
    }

    public void addRow(DateTimeFileRowModel row) {
        if (debug) System.out.println("start NameValeTableModel.addRow()");
        rows.add(row);
        if (debug) {
            if (row==null) {
                System.out.println("NameValeTableModel.addRow(): value =null");
            } else {
                System.out.println("NameValeTableModel.addRow(): value ="+row.toString());
            } 
        }
        fireTableRowsInserted(rows.size()-1, rows.size()-1);
        if (debug) System.out.println("end NameValeTableModel.addRow()");
    }
    
    public void insertRow(DateTimeFileRowModel row,int rowIndex) {
        if (debug) System.out.println("start NameValeTableModel.insertRow()");
        rows.add(rowIndex,row);
        fireTableRowsInserted(rowIndex, rowIndex);
        if (debug) System.out.println("end NameValeTableModel.insertRow()");
    }
    
    public void deleteRow(int rowIndex) {
        try {
            if (debug) System.out.println("start NameValeTableModel.deleteRow()");
            rows.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
            if (debug) System.out.println("end NameValeTableModel.deleteRow()");
        } catch (ArrayIndexOutOfBoundsException exc) {
            //do nothing
        }
    }
    
    public boolean isCellEditable(int rowIndex, int colIndex) {
        return false;
    }
    
    public Class getColumnClass(int col) {
        if (col==dirCol) return String.class;
        if (col==fileCol) return String.class;
        if (col==otCol) return Long.class;
        if (col==yrCol) return Integer.class;
        if (col==monCol) return Integer.class;
        if (col==domCol) return Integer.class;
        if (col==hrCol) return Integer.class;
        if (col==minCol) return Integer.class;
        if (col==secCol) return Integer.class;
        return null;
    }
    
    public int getColumnCount() {
        return 9;
    }

    public String getColumnName(int colIndex) {
        if ((colIndex>=0)&&(colIndex<9)) return columnNames[colIndex];
        return null;
    }
    
    public int getRowCount() {
        return rows.size();
    }

    public Object getRow(int rowIndex) {
        return rows.get(rowIndex);
    }
     
    public Object getValueAt(int rowIndex, int colIndex) {
        try {
            return rows.get(rowIndex).get(colIndex);
        } catch (ArrayIndexOutOfBoundsException exc) {
            //do nothing
        }
        return null;
    }
     
    public void setRow(DateTimeFileRowModel row, int rowIndex) {
        if (debug) System.out.println("start NameValueTableModel.setValueAt("+rowIndex+")");
        rows.set(rowIndex,row);
        fireTableRowsUpdated(rowIndex, rowIndex);
        if (debug) System.out.println("end NameValueTableModel.setRow("+rowIndex+")");
    }
     
    public void setValueAt(Object value, int rowIndex, int colIndex) {
        //do nothing for now
    }
    
    public Vector<DateTimeFileRowModel> getRows() {
        return (Vector<DateTimeFileRowModel>) rows.clone();
    }
    
    public void reset() {
        if (debug) System.out.println("start NameValueTableModel.reset()");
        rows = new Vector<DateTimeFileRowModel>();
        fireTableStructureChanged();
        if (debug) System.out.println("end NameValueTableModel.reset()");
    }
}
