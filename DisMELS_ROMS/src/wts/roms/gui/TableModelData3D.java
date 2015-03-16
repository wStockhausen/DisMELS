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
public class TableModelData3D extends javax.swing.table.AbstractTableModel {
    public static int SLICETYPE_XY = 0;
    public static int SLICETYPE_XZ = 1;
    public static int SLICETYPE_YZ = 2;
    
    ModelData md = null;
    int slice = 0;
    int slicetype = SLICETYPE_XZ;
    int sliceIndices = 1;
    int rowIndices   = 2;
    int colIndices   = 3;
    
    private int tIndex   = -1;
    private int sIndex   = -1;
    private int etaIndex = -1;
    private int xiIndex  = -1;
    
    /** Creates a new instance of TableModelData3D */
    public TableModelData3D() {
    }

    public TableModelData3D(ModelData md) {
        this.md = md;
        setIndexes();
    }
    
    public int getRowCount() {
        int[] shp = md.getShape();
        return shp[rowIndices];
    }

    public int getColumnCount() {
        int[] shp = md.getShape();
        return shp[colIndices]+1;
    }

    public String getColumnName(int cI) {
        String nm = "";
        if (cI==0) {
            nm = getLabel();
        } else {
            if (slicetype==SLICETYPE_XY) {
                int xip = 0;
                //rho points and v points start at ROMS xi index 0, not 1
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_RHO) xip = 1;
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V)   xip = 1;
                nm = String.valueOf(cI-xip);
            } else if (slicetype==SLICETYPE_XZ) {
                int xip = 0;
                //rho points and v points start at ROMS xi index 0, not 1
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_RHO) xip = 1;
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V)   xip = 1;
                nm = String.valueOf(cI-xip);
                
            } else if (slicetype==SLICETYPE_YZ) {
                int etap = 0;
                //rho points and u points start at ROMS eta index 0, not 1
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_RHO) etap = 1;
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_U)   etap = 1;
                nm = String.valueOf(cI-etap);
            }
        }
        return nm;
    }
    
    private String getLabel() {
        String str = "";
        if (slicetype==SLICETYPE_XY) str = "eta/xi";
        if (slicetype==SLICETYPE_XZ) str = "s/xi";
        if (slicetype==SLICETYPE_YZ) str = "s/eta";
        return str;
    }
    
    public int getMaxEta() {
        int s = md.getShape()[etaIndex]-1;
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_PSI) s++;
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V) s++;
        return s;
    }
    
    public int getMaxS() {
        int s = md.getShape()[sIndex]-1;
        if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_RHO) s++;
        return s;
    }
    
    public int getMaxXi() {
        int s = md.getShape()[xiIndex]-1;
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_PSI) s++;
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_U) s++;
        return s;
    }
    
    public int getMinEta() {
        int s = 0;
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_PSI) s++;
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V) s++;
        return s;
    }
    
    public int getMinS() {
        int s = 0;
        if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_RHO) s++;
        return s;
    }
    
    public int getMinXi() {
        int s = 0;
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_PSI) s++;
        if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_U) s++;
        return s;
    }
    
    public int getMaxSlice() {
        int i = 0;
        if (slicetype==SLICETYPE_XY) i = getMaxS();
        if (slicetype==SLICETYPE_XZ) i = getMaxEta();
        if (slicetype==SLICETYPE_YZ) i = getMaxXi();
        return i;
    }
    
    public int getMinSlice() {
        int i = 0;
        if (slicetype==SLICETYPE_XY) i = getMinS();
        if (slicetype==SLICETYPE_XZ) i = getMinEta();
        if (slicetype==SLICETYPE_YZ) i = getMinXi();
        return i;
    }
    
    public Object getValueAt(int rI, int cI) {
        Object o = null;
        if (slicetype==SLICETYPE_XY) {
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
                o = new Double(md.getValue(cI-xip,rI+etap,slice));
            }
        } else if (slicetype==SLICETYPE_XZ) {
            //display s value in first column
            if (cI==0) {
                int sp = 0;
                //rho points start at ROMS s index 1, not 0
                if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_RHO) sp = 1;
                o = new Integer(rI+sp);
            } else {
                int xip = 0;
                int sp = 0;
                //rho points and v points start at ROMS xi index 0, not 1
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_RHO) xip = 1;
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V)   xip = 1;
                //rho points start at ROMS s index 1, not 0
                if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_RHO) sp = 1;
                o = new Double(md.getValue(cI-xip,slice,rI+sp));
            }
        } else if (slicetype==SLICETYPE_YZ) {
            //display s value in first column
            if (cI==0) {
                int sp = 0;
                //rho points start at ROMS s index 1, not 0
                if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_RHO) sp = 1;
                o = new Integer(rI+sp);
            } else {
                int etap = 0;
                int sp = 0;
                //rho points and u points start at ROMS eta index 0, not 1
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_RHO) etap = 1;
                if (md.getHorzPosType()==ModelTypes.HORZ_POSTYPE_V)   etap = 1;
                //rho points start at ROMS s index 1, not 0
                if (md.getVertPosType()==ModelTypes.VERT_POSTYPE_RHO) sp = 1;
                o = new Double(md.getValue(slice,cI-etap,rI+sp));
            }
        }
        return o;
    }
    
    private void setIndexes() {
        int[] i = md.getDimIndices();
        tIndex   = i[0];
        sIndex   = i[1];
        etaIndex = i[2];
        xiIndex  = i[3];
    }
    
    public void setSliceIndex(int slice) {
        this.slice = slice;
    }
    
    public void setSliceType(int slicetype) {
        this.slicetype = slicetype;
        if (slicetype==SLICETYPE_XY) {
            sliceIndices = sIndex;
            rowIndices   = etaIndex;
            colIndices   = xiIndex;
        } else if (slicetype==SLICETYPE_XZ) {
            sliceIndices = etaIndex;
            rowIndices   = sIndex;
            colIndices   = xiIndex;
        } else if (slicetype==SLICETYPE_YZ) {
            sliceIndices = xiIndex;
            rowIndices   = sIndex;
            colIndices   = etaIndex;
        }
    }
    
    public void setModelData(ModelData md) {
        this.md = md;
        setIndexes();
    }
}
