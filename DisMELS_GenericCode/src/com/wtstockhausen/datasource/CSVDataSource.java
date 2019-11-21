/*
 * CSVDataSource.java
 *
 * Created on February 28, 2003, 10:05 AM
 */

package com.wtstockhausen.datasource;

/**
 *
 * @author  William Stockhausen
 */

import com.wtstockhausen.table.DataTableModel;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.table.TableModel;

public class CSVDataSource {
    
    public static final int TYPE_NONE    = 255;
    public static final int TYPE_STRING  = 0;
    public static final int TYPE_DOUBLE  = 1;
    public static final int TYPE_FLOAT   = 2;
    public static final int TYPE_LONG = 4;
    public static final int TYPE_INTEGER = 8;
    public static final int TYPE_BOOLEAN = 16;
    
    protected int nRows = 0;
    protected int nCols = 0;
    protected int headerRowCount = 0;
    protected String[] columnNames;
    protected int[] columnTypes;
    protected String[] header;
    protected File file;
    protected Object[][] table;
    
    /** Utility field used by bound properties. */
    private PropertyChangeSupport propertyChangeSupport =  new PropertyChangeSupport(this);
    
    /** Creates a new instance of CSVTableSource */
    public CSVDataSource() {
    }
    
    public CSVDataSource(File f) {
        setFile(f);
    }
    
    public int findColumn(String str) {
        for (int i=0;i<columnNames.length;i++) {
            if (columnNames[i].equalsIgnoreCase(str)) {
                return i;
            }
        }
        return -1;
    }
    
    public Class getColumnClasses(int i) {
        if ((columnTypes!=null)&&(i>=0)&&(i<columnTypes.length)){
            int j = columnTypes[i];
            try {
                if (j==TYPE_BOOLEAN) {
                    return Class.forName("java.lang.Boolean");
                } else if (j==TYPE_INTEGER) {
                        return Class.forName("java.lang.Integer");
                } else if (j==TYPE_LONG) {
                        return Class.forName("java.lang.Long");
                } else if (j==TYPE_FLOAT) {
                    return Class.forName("java.lang.Float");
                } else if (j==TYPE_DOUBLE) {
                    return Class.forName("java.lang.Double");
                } else if (j==TYPE_STRING) {
                    return Class.forName("java.lang.String");
                }
            } catch (ClassNotFoundException exc) {
                System.out.println(exc.toString());
            }
        }
        return null;
    }
    
    public Class[] getColumnClasses() {
        Class[] columnClasses = new Class[nCols];
        for (int i=0;i<nCols;i++) {
            columnClasses[i] = getColumnClasses(i);
        }
        return columnClasses;
    }
    
    public int getColumnCount() {
        return nCols;
    }
    
    public String getColumnName(int i) {
        return columnNames[i];
    }
    
    public File getFile() {
        return file;
    }
    
    /** Getter for property headerRowCount.
     * @return Value of property headerRowCount.
     */
    public int getHeaderRowCount() {
        return this.headerRowCount;
    }
    
    public int getRowCount() {
        return nRows;
    }
    
    public TableModel getTableModel() {
        if (table !=null) {
            DataTableModel dtm = new DataTableModel(table,
                                                    columnNames,
                                                    getColumnClasses());
            return dtm;
        }
        return new DataTableModel();
    }
    
    public Object getValueAt(int row, int col) {
        return table[row][col];
    }
    
    private int objectType(Object obj) {
        int oType = -1;
        if (obj instanceof Boolean) {
            oType = TYPE_BOOLEAN;
        } else if (obj instanceof Integer) {
            oType = TYPE_INTEGER;
        } else if (obj instanceof Long) {
            oType = TYPE_LONG;
        } else if (obj instanceof Float) {
            oType = TYPE_FLOAT;
        } else if (obj instanceof Double) {
            oType = TYPE_DOUBLE;
        } else if (obj instanceof String) {
            oType = TYPE_STRING;
        }
        return oType;
    }
    
    public void readCSV() {
        readCSV(null);
    }
    
    
    public void readCSV(int[] newColumnTypes) {
        try {
            FileReader        fr   = new FileReader(file);
            BufferedReader    br   = new BufferedReader(fr);
            ParseCSV pCSV = new ParseCSV(br);
            
            //read header and column titles
            readIntro(br,pCSV);
            
            // loop through remaining to get nCols, nRows
            // and columnTypes
            nCols = 0;
            nRows = 0;
            String[] list;
            while ((list = pCSV.decodeLine()) != null) {
                nRows = nRows+1;
                nCols = Math.max(nCols,list.length);
            }
            System.out.println("nRows = "+nRows);
            System.out.println("nCols = "+nCols);
            br.close();
            
            //now that we've got the table size, let's read it again
            //to get the column types
            fr   = new FileReader(file);
            br   = new BufferedReader(fr);
            pCSV = new ParseCSV(br);
            
            readIntro(br,pCSV);
            int ct      = -1;
            columnTypes = new int[nCols];
            for (int j=0;j<nCols;j++) columnTypes[j] = TYPE_NONE; //initalize column types to dummy
            
            for (int j=0;j<nRows;j++){
                list = pCSV.decodeLine();
                for (int i=0;i<list.length;i++) {
                    try {
                        Double d = Double.valueOf(list[i]);
                        ct = TYPE_DOUBLE;
                        columnTypes[i] = Math.min(columnTypes[i],ct);
                    } catch (java.lang.NumberFormatException exc) {
                        if (list[i].equalsIgnoreCase("") && (columnTypes[i]!=TYPE_STRING)) {
                            //don't change column type
                        } else {
                            columnTypes[i] = TYPE_STRING;
                        }
                    }
                }
            }
            for (int i=0;i<nCols;i++) {
                if (columnTypes[i]==TYPE_NONE) columnTypes[i]=TYPE_STRING;
            }
            br.close();
            
            //if necessary, override the discerned column types with the input types
            if (newColumnTypes!=null) {
                for (int j=0;j<Math.min(nCols,newColumnTypes.length);j++) 
                    columnTypes[j] = newColumnTypes[j]; 
            }

            //now that we've got the column types, let's read it one last time!
            fr   = new FileReader(file);
            br   = new BufferedReader(fr);
            pCSV = new ParseCSV(br);
            
            readIntro(br,pCSV);
            table       = new Object[nRows][nCols];
            for (int j=0;j<nRows;j++){
                list = pCSV.decodeLine();
                for (int i=0;i<list.length;i++) {
                    try {
                        if (columnTypes[i]==TYPE_BOOLEAN) {
                            table[j][i] = Boolean.valueOf(list[i]);
                        } else if (columnTypes[i]==TYPE_DOUBLE) {
                            table[j][i] = Double.valueOf(list[i]);
                        } else if (columnTypes[i]==TYPE_FLOAT) {
                            table[j][i] = Float.valueOf(list[i]);
                        } else if (columnTypes[i]==TYPE_INTEGER) {
                            table[j][i] = Integer.valueOf(list[i]);
                        } else if (columnTypes[i]==TYPE_LONG) {
                            table[j][i] = Long.valueOf(list[i]);
                        } else if (columnTypes[i]==TYPE_STRING) {
                            table[j][i] = list[i];
                        } else {
                            table[j][i] = list[i];
                        }
                    } catch (java.lang.NumberFormatException exc) {
                        if (list[i].equalsIgnoreCase("")) {
                            table[j][i] = list[i];
                        }
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException exc) {
            System.out.println(exc.toString());
        } catch (IOException exc) {
            System.out.println(exc.toString());
        }
        for (int i=0;i<nCols;i++) {
            System.out.println("columnTypes["+i+"] = "+columnTypes[i]);
        }
    }
    
    protected void readIntro(BufferedReader br, ParseCSV pCSV) throws IOException {
        // read the header lines
        System.out.println("Reading header");
        header = new String[headerRowCount];
        for (int i=0;i<headerRowCount;i++){
            header[i] = br.readLine();
            System.out.println(header[i]);
        }

        // read the column names
        System.out.println("Reading column names");
        columnNames = pCSV.decodeLine();
        for (int i=0;i<columnNames.length;i++){
            System.out.println(columnNames[i]);
        }
    }
    
    public void setFile(File f) {
        File oldFile = this.file;
        this.file = f;
        propertyChangeSupport.firePropertyChange("file", 
                                                 oldFile, 
                                                 file);
     }
    
    /** Setter for property headerRowCount.
     * @param headerRowCount New value of property headerRowCount.
     */
    public void setHeaderRowCount(int headerRowCount) {
        int oldHeaderRowCount = this.headerRowCount;
        this.headerRowCount = headerRowCount;
        propertyChangeSupport.firePropertyChange("headerRowCount", 
                                                 new Integer(oldHeaderRowCount), 
                                                 new Integer(headerRowCount));
    }
    
    public void setTable(int nRows, int nCols, int[] columnTypes, Object[][] table) {
        Object[][] oldTable = this.table;
        int oldRows = nRows;
        int oldCols = nCols;
        this.table = table;
        this.nRows = nRows;
        this.nCols = nCols;
        this.columnTypes = columnTypes;
        propertyChangeSupport.firePropertyChange("number of rows", 
                                                 oldRows, 
                                                 nRows);
        propertyChangeSupport.firePropertyChange("number of columns", 
                                                 oldCols, 
                                                 nCols);
        propertyChangeSupport.firePropertyChange("table", 
                                                 oldTable, 
                                                 table);
    }
    
    public void setValueAt(Object obj, int row, int col) {
        if (table==null) return;
        if ((row >=0)&&(row<nRows)&&(col>=0)&&(col<nCols)) {
            if (objectType(obj)==columnTypes[col]) {
                ArrayList oldVal = new ArrayList();
                oldVal.add(new Integer(row));
                oldVal.add(new Integer(col));
                oldVal.add(table[row][col]);
                ArrayList newVal = new ArrayList();
                table[row][col] = obj;
                newVal.add(new Integer(row));
                newVal.add(new Integer(col));
                newVal.add(table[row][col]);
                propertyChangeSupport.firePropertyChange("table value", 
                                                         oldVal, 
                                                         newVal);
            }
        }
    }
    
    public void writeCSV() {
        try {
            String str;
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);
            for (int i=0;i<header.length;i++) {
                pw.println(header[i]);
            }
            str = columnNames[0];
            for (int i=1;i<columnNames.length;i++) {
                str = str+","+columnNames[i];
            }
            pw.println(str);
            for (int r=0;r<nRows;r++){
                str = stringRep(getValueAt(r,0));
                for (int c=0;c<nCols;c++) {
                    str = str+","+stringRep(getValueAt(r,0));
                }
                pw.println(str);
            }
            pw.close();
        } catch (IOException ex) {
            System.out.println("\n\rException writing csv file");
            System.out.println(ex.toString());
        }
    }
    
    private String stringRep(Object obj) {
        String str = "";
        if (obj instanceof Boolean) {
            str = ((Boolean) obj).toString();
        } else if (obj instanceof Integer) {
            str = ((Integer) obj).toString();
        } else if (obj instanceof Float) {
            str = ((Float) obj).toString();
        } else if (obj instanceof Double) {
            str = ((Double) obj).toString();
        } else if (obj instanceof String) {
            str = ((String) obj).toString();
        } else {
            str = obj.toString();
        }
        
        return str;
    }
    
    /** Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }
    
    /** Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }
    
}
