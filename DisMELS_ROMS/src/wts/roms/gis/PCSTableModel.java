/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package wts.roms.gis;

import javax.media.jai.ParameterList;
import javax.media.jai.ParameterListDescriptor;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author wstockhausen
 */
public class PCSTableModel extends AbstractTableModel {

    public static boolean debug = false;
    private final static String[] columnNames   = {"parameter","value"};
    private final static Class[] columnClasses  = {String.class,Object.class};

    AbstractPCS pcs = null;

    int np                      = 0;
    String[] names              = null;

    PCSTableModel(AbstractPCS pcs) {
        if (pcs!=null) {
            this.pcs = pcs;
            names = pcs.getParameterNames();
            np    = names.length;
        }
    }

    public int getRowCount() {
        return np;
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (debug) System.out.println("getColumnName: colIndex = "+columnIndex);
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (debug) System.out.println("getColumnClass: colIndex = "+columnIndex);
        return columnClasses[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (debug) System.out.println("isEditable: index = "+rowIndex+", "+columnIndex);
        boolean b = false;
        if (columnIndex==1) b = true;
        return b;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (debug) System.out.println("getValueAt: index = "+rowIndex+", "+columnIndex);
        if (columnIndex==0) {
            return names[rowIndex];
        }
        return pcs.getParameterValue(names[rowIndex]);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (debug) {
            System.out.println("setValueAt: index = "+rowIndex+", "+columnIndex);
            System.out.println("new Value = "+aValue.toString());
            System.out.println("Class  = "+aValue.getClass().getCanonicalName());
        }
        Double val = Double.parseDouble((String)aValue);
        pcs.setParameterValue(names[rowIndex], val);
    }
}
