/*
 * Table3DViewer_JPanel.java
 *
 * Created on December 6, 2005, 5:42 PM
 */

package wts.roms.gui;

import wts.roms.*;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author  William Stockhausen
 */
public class Table3DViewer_JPanel extends javax.swing.JPanel {
    
    Table2DViewer_JPanel t2dv;
    TableModelData3D tm3d;
    
    /** Creates new form Table3DViewer_JPanel */
    public Table3DViewer_JPanel() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        bgSliceType = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jrbXY = new javax.swing.JRadioButton();
        jrbXZ = new javax.swing.JRadioButton();
        jrbYZ = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jspSlice = new javax.swing.JSpinner();
        jPanel3 = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("2d slice direction"));
        jPanel2.setPreferredSize(new java.awt.Dimension(200, 30));
        jPanel2.setRequestFocusEnabled(false);
        bgSliceType.add(jrbXY);
        jrbXY.setSelected(true);
        jrbXY.setText("XY");
        jrbXY.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbXY.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jrbXY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbXYActionPerformed(evt);
            }
        });

        bgSliceType.add(jrbXZ);
        jrbXZ.setText("XZ");
        jrbXZ.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbXZ.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jrbXZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbXZActionPerformed(evt);
            }
        });

        bgSliceType.add(jrbYZ);
        jrbYZ.setText("YZ");
        jrbYZ.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbYZ.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jrbYZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbYZActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jrbXY)
                .add(24, 24, 24)
                .add(jrbXZ)
                .add(27, 27, 27)
                .add(jrbYZ)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jrbXY)
                    .add(jrbXZ)
                    .add(jrbYZ))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1.add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Slice index"));
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 70));
        jspSlice.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspSliceStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jspSlice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jspSlice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanel1.add(jPanel4, java.awt.BorderLayout.EAST);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel3.setLayout(new java.awt.BorderLayout());

        t2dv = new Table2DViewer_JPanel();
        jPanel3.add(t2dv,java.awt.BorderLayout.CENTER);
        add(jPanel3, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents

    private void jrbXYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbXYActionPerformed
        setSliceType();
    }//GEN-LAST:event_jrbXYActionPerformed

    private void jrbYZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbYZActionPerformed
        setSliceType();
    }//GEN-LAST:event_jrbYZActionPerformed

    private void jrbXZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbXZActionPerformed
        setSliceType();
    }//GEN-LAST:event_jrbXZActionPerformed

    private void jspSliceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspSliceStateChanged
// TODO add your handling code here:
        tm3d.setSliceIndex(((Integer)jspSlice.getValue()).intValue());
        t2dv.validate();
        t2dv.repaint();
    }//GEN-LAST:event_jspSliceStateChanged
    
    private void setSliceType() {
        if (jrbXY.isSelected()) {
            tm3d.setSliceType(tm3d.SLICETYPE_XY);
        } else if (jrbXZ.isSelected()) {
            tm3d.setSliceType(tm3d.SLICETYPE_XZ);
        } else if (jrbYZ.isSelected()) {
            tm3d.setSliceType(tm3d.SLICETYPE_YZ);
        } 
        setSpinnerModel();
    }
    
    private void setSpinnerModel() {
        int mn = tm3d.getMinSlice();
        int mx = tm3d.getMaxSlice();
        SpinnerNumberModel spmod;
        spmod = new SpinnerNumberModel(new Integer(mn),
                                       new Integer(mn),
                                       new Integer(mx),
                                       new Integer(1));
        jspSlice.setModel(spmod);
        tm3d.setSliceIndex(mn);
        t2dv.validate();
        t2dv.repaint();
    }
    
    void setTableModel(AbstractTableModel tm) {
        tm3d = (TableModelData3D) tm;
        setSliceType(); //sets slice type and spinner model
        t2dv.setTableModel(tm3d);
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgSliceType;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jrbXY;
    private javax.swing.JRadioButton jrbXZ;
    private javax.swing.JRadioButton jrbYZ;
    private javax.swing.JSpinner jspSlice;
    // End of variables declaration//GEN-END:variables
    
}
