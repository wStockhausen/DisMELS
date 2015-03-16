/*
 * JPanel_ModelData.java
 *
 * Created on December 8, 2005, 9:21 PM
 */

package wts.roms.gui;

import wts.roms.*;

/**
 *
 * @author  wstockhausen
 */
public class JPanel_ModelData extends javax.swing.JPanel {
    
    /**
     * Creates new form JPanel_ModelData
     */
    public JPanel_ModelData() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jpVariable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaVariable = new javax.swing.JTextArea();
        jpAxes = new javax.swing.JPanel();
        jpTable = new javax.swing.JPanel();

        jpVariable.setBorder(javax.swing.BorderFactory.createTitledBorder("Model variable"));
        jtaVariable.setColumns(20);
        jtaVariable.setRows(5);
        jScrollPane1.setViewportView(jtaVariable);

        org.jdesktop.layout.GroupLayout jpVariableLayout = new org.jdesktop.layout.GroupLayout(jpVariable);
        jpVariable.setLayout(jpVariableLayout);
        jpVariableLayout.setHorizontalGroup(
            jpVariableLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jpVariableLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap())
        );
        jpVariableLayout.setVerticalGroup(
            jpVariableLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, jpVariableLayout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpAxes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jpAxes.setBorder(javax.swing.BorderFactory.createTitledBorder("Axes/dimensions"));

        jpTable.setLayout(new java.awt.BorderLayout());

        jpTable.setBorder(javax.swing.BorderFactory.createTitledBorder("Data"));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpVariable)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jpAxes, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .add(jpTable, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                .add(jpVariable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpAxes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 15, Short.MAX_VALUE)
                .add(jpTable, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jpAxes;
    private javax.swing.JPanel jpTable;
    private javax.swing.JPanel jpVariable;
    private javax.swing.JTextArea jtaVariable;
    // End of variables declaration//GEN-END:variables
    
}