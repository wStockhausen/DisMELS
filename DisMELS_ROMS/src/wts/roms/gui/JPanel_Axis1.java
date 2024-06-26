/*
 * JPanel_Axis1.java
 *
 * Created on December 8, 2005, 6:32 PM
 */

package wts.roms.gui;

import wts.roms.*;
import wts.roms.ui.events.StateChangedSupport;
import java.awt.Component;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author  William Stockhausen
 */
public class JPanel_Axis1 extends javax.swing.JPanel {
    
    private StateChangedSupport scSupport;
    
    /**
     * Creates new form JPanel_Axis1
     */
    public JPanel_Axis1() {
        scSupport = new StateChangedSupport(this);
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jpLabels = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jpSettings = new javax.swing.JPanel();
        jtfName = new javax.swing.JTextField();
        jspStart = new javax.swing.JSpinner();
        jspEnd = new javax.swing.JSpinner();
        jpXYLabels = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jpXY = new javax.swing.JPanel();
        jrbX = new javax.swing.JRadioButton();
        jrbY = new javax.swing.JRadioButton();

        setMinimumSize(new java.awt.Dimension(300, 20));
        setPreferredSize(new java.awt.Dimension(500, 40));
        jpLabels.setOpaque(false);
        jpLabels.setPreferredSize(new java.awt.Dimension(10, 20));
        jLabel1.setLabelFor(jtfName);
        jLabel1.setText("Name");

        jLabel4.setLabelFor(jspStart);
        jLabel4.setText("Start");

        jLabel5.setLabelFor(jspEnd);
        jLabel5.setText("End");

        org.jdesktop.layout.GroupLayout jpLabelsLayout = new org.jdesktop.layout.GroupLayout(jpLabels);
        jpLabels.setLayout(jpLabelsLayout);
        jpLabelsLayout.setHorizontalGroup(
            jpLabelsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jpLabelsLayout.createSequentialGroup()
                .add(48, 48, 48)
                .add(jLabel1)
                .add(126, 126, 126)
                .add(jLabel4)
                .add(93, 93, 93)
                .add(jLabel5)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jpLabelsLayout.setVerticalGroup(
            jpLabelsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jpLabelsLayout.createSequentialGroup()
                .add(jpLabelsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(jLabel4)
                    .add(jLabel1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpSettings.setPreferredSize(new java.awt.Dimension(10, 20));
        jtfName.setEditable(false);
        jtfName.setText("Dim Name");
        jtfName.setEnabled(false);

        jspStart.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspStartStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jpSettingsLayout = new org.jdesktop.layout.GroupLayout(jpSettings);
        jpSettings.setLayout(jpSettingsLayout);
        jpSettingsLayout.setHorizontalGroup(
            jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jpSettingsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jtfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(15, 15, 15)
                .add(jspStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(16, 16, 16)
                .add(jspEnd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 103, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jpSettingsLayout.setVerticalGroup(
            jpSettingsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.BASELINE, jtfName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(org.jdesktop.layout.GroupLayout.BASELINE, jspStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(org.jdesktop.layout.GroupLayout.BASELINE, jspEnd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        jLabel2.setLabelFor(jrbX);
        jLabel2.setText("X");

        jLabel3.setLabelFor(jrbY);
        jLabel3.setText("Y");

        org.jdesktop.layout.GroupLayout jpXYLabelsLayout = new org.jdesktop.layout.GroupLayout(jpXYLabels);
        jpXYLabels.setLayout(jpXYLabelsLayout);
        jpXYLabelsLayout.setHorizontalGroup(
            jpXYLabelsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jpXYLabelsLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(22, 22, 22)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpXYLabelsLayout.setVerticalGroup(
            jpXYLabelsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jpXYLabelsLayout.createSequentialGroup()
                .add(jpXYLabelsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jLabel3))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jrbX.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbX.setMargin(new java.awt.Insets(0, 0, 0, 0));

        jrbY.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jrbY.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout jpXYLayout = new org.jdesktop.layout.GroupLayout(jpXY);
        jpXY.setLayout(jpXYLayout);
        jpXYLayout.setHorizontalGroup(
            jpXYLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jpXYLayout.createSequentialGroup()
                .addContainerGap()
                .add(jrbX)
                .add(17, 17, 17)
                .add(jrbY)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpXYLayout.setVerticalGroup(
            jpXYLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.BASELINE, jrbX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(org.jdesktop.layout.GroupLayout.BASELINE, jrbY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jpSettings, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
                        .add(16, 16, 16))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                        .add(jpLabels, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 386, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, Short.MAX_VALUE)))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jpXYLabels)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jpXY)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jpXYLabels, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jpLabels, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jpSettings, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .add(jpXY, 0, 21, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jspStartStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspStartStateChanged
        scSupport.fireStateChangedEvent(jspStart);
    }//GEN-LAST:event_jspStartStateChanged
    
    public void enableSingleSpinner(boolean b) {
        jspEnd.setEnabled(b);
    }
    
    public String getAxisName() {
        return jtfName.getText();
    }
    
    public void setAxisName(String name) {
        jtfName.setText(name);
    }
    
    public boolean isSelectedX() {
        return jrbX.isSelected();
    }
    
    public void setSelectedX(boolean b) {
        jrbX.setSelected(b);
    }
    
    public boolean isSelectedY() {
        return jrbY.isSelected();
    }
    
    public void setSelectedY(boolean b) {
        jrbY.setSelected(b);
    }
    
    public int[] getRange() {
        int[] r = new int[2];
        r[0] = ((Integer) jspStart.getValue()).intValue();
        r[1] = ((Integer) jspEnd.getValue()).intValue();
        return r;
    }
    
    public void setSpinnerModel(SpinnerNumberModel sm) {
        SpinnerNumberModel sm1 = new SpinnerNumberModel((Integer)sm.getMaximum(),
                                                        (Integer)sm.getMinimum(),
                                                        (Integer)sm.getMaximum(),
                                                        (Integer)sm.getStepSize());
        jspStart.setModel(sm);
        jspEnd.setModel(sm1);
    }
    
    public void removeLabels() {
        this.remove(jpLabels);
        this.revalidate();
        this.repaint();
    }
    
    public void removeXY() {
        this.remove(jpXY);
    }

    public void removeXYLabels() {
        this.remove(jpXYLabels);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jpLabels;
    private javax.swing.JPanel jpSettings;
    private javax.swing.JPanel jpXY;
    private javax.swing.JPanel jpXYLabels;
    private javax.swing.JRadioButton jrbX;
    private javax.swing.JRadioButton jrbY;
    private javax.swing.JSpinner jspEnd;
    private javax.swing.JSpinner jspStart;
    private javax.swing.JTextField jtfName;
    // End of variables declaration//GEN-END:variables
    
}
