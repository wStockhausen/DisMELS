/*
 * TableModelData2D.java
 *
 * Created on December 6, 2005, 2:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gui;

import wts.roms.*;
import wts.roms.model.ModelData;
import wts.roms.model.ModelTypes;

/**
 *
 * @author William Stockhausen
 */
public class TableModelData2D extends javax.swing.table.AbstractTableModel {
    ModelData md = null;
    /** Creates a new instance of TableModelData2D */
    public TableModelData2D() {
    }

    public TableModelData2D(ModelData md) {
        this.md = md;
    }
    
    public int getRowCount() {
        int[] shp = md.getShape();
        return shp[0];
    }

    public int getColumnCount() {
        int[] shp = md.getShape();
        return shp[1]+1;
    }

    public String getColumnName(int cI) {
        String nm = "";
        if (cI==0) {
            nm = "eta/xi";
        } else {
            int xip = 0;
            //rho points and v points start at ROMS xi index 0, not 1
            if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_RHO) xip = 1;
            if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V)   xip = 1;
            nm = String.valueOf(cI-xip);
        }
        return nm;
    }
    
    public Object getValueAt(int rI, int cI) {
        Object o = null;
        //display eta value in first column
        if (cI==0) {
            int etap = 0;
            //v points and psi points start at ROMS eta index 1, not 0
            if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V) etap = 1;
            if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_PSI) etap = 1;
            o = new Integer(rI+etap);
        } else {
            int xip = 0;
            int etap = 0;
            //rho points and v points start at ROMS xi index 0, not 1
            if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_RHO) xip = 1;
            if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V)   xip = 1;
            //v points and psi points start at ROMS eta index 1, not 0
            if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V) etap = 1;
            if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_PSI) etap = 1;
            o = new Double(md.getValue(cI-xip,rI+etap));
        }
        return o;
    }
    
    public void setModelData(ModelData md) {
        this.md = md;
    }
}
