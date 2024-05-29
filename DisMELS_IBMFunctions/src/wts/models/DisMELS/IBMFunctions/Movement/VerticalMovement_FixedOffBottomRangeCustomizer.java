/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.Movement;

import wts.models.DisMELS.framework.IBMFunctions.IBMParameterDouble;

/**
 *
 * @author William.Stockhausen
 */
public class VerticalMovement_FixedOffBottomRangeCustomizer extends javax.swing.JPanel implements java.beans.Customizer {
    
    private VerticalMovement_FixedOffBottomRange obj = null;;

    /**
     * Creates new customizer DielVerticalMigration_FixedOffBottomRangesCustomizer
     */
    public VerticalMovement_FixedOffBottomRangeCustomizer() {
        initComponents();
    }
    
    @Override
    public void setObject(Object bean) {
        if (bean instanceof VerticalMovement_FixedOffBottomRange){
            obj = (VerticalMovement_FixedOffBottomRange) bean;
            setParameters();
        }
    }

    private void setParameters() {
        jtfMaxDepth.setText(((IBMParameterDouble)obj.getParameter(VerticalMovement_FixedOffBottomRange.PARAM_maxDepth)).getValueAsString());
        jtfMinDistanceOffBottom.setText(((IBMParameterDouble)obj.getParameter(VerticalMovement_FixedOffBottomRange.PARAM_minDistOffBottom)).getValueAsString());
        jtfMaxDistOffBottom.setText(((IBMParameterDouble)obj.getParameter(VerticalMovement_FixedOffBottomRange.PARAM_maxDistOffBottom)).getValueAsString());
        jtfRPW.setText(((IBMParameterDouble)obj.getParameter(VerticalMovement_FixedOffBottomRange.PARAM_rpw)).getValueAsString());
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtfRPW = new javax.swing.JTextField();
        jtfMaxDepth = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfMinDistanceOffBottom = new javax.swing.JTextField();
        jtfMaxDistOffBottom = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(305, 122));
        setMinimumSize(new java.awt.Dimension(305, 122));
        setPreferredSize(new java.awt.Dimension(305, 122));

        jLabel1.setText("random walk parameter (m^2/s)");
        jLabel1.setToolTipText("");

        jtfRPW.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfRPW.setText("0.0");
        jtfRPW.setToolTipText("diffusion rate");
        jtfRPW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfRPWActionPerformed(evt);
            }
        });

        jtfMaxDepth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfMaxDepth.setText("1000");
        jtfMaxDepth.setToolTipText("maximum overall depth for individual");
        jtfMaxDepth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMaxDepthActionPerformed(evt);
            }
        });

        jLabel2.setText("max depth (m)");

        jtfMinDistanceOffBottom.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfMinDistanceOffBottom.setText("0");
        jtfMinDistanceOffBottom.setToolTipText("min preferred distance above bottom (m)");
        jtfMinDistanceOffBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMinDistanceOffBottomActionPerformed(evt);
            }
        });

        jtfMaxDistOffBottom.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfMaxDistOffBottom.setText("10000");
        jtfMaxDistOffBottom.setToolTipText("max preferred distance above bottom (m) ");
        jtfMaxDistOffBottom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMaxDistOffBottomActionPerformed(evt);
            }
        });

        jLabel3.setText("min distance off bottom (m)");
        jLabel3.setToolTipText("");

        jLabel4.setText("max distance off bottom (m)");
        jLabel4.setToolTipText("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtfRPW, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtfMaxDepth, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtfMinDistanceOffBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtfMaxDistOffBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfMaxDepth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfMinDistanceOffBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfMaxDistOffBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfRPW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(1, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfRPWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfRPWActionPerformed
        String str = jtfRPW.getText();
        Double val = Double.parseDouble(str);
        obj.setParameterValue(VerticalMovement_FixedOffBottomRange.PARAM_rpw, val);
    }//GEN-LAST:event_jtfRPWActionPerformed

    private void jtfMaxDepthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMaxDepthActionPerformed
        String str = jtfMaxDepth.getText();
        Double val = Double.parseDouble(str);
        obj.setParameterValue(VerticalMovement_FixedOffBottomRange.PARAM_maxDepth, val);
    }//GEN-LAST:event_jtfMaxDepthActionPerformed

    private void jtfMinDistanceOffBottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMinDistanceOffBottomActionPerformed
        String str = jtfMinDistanceOffBottom.getText();
        Double val = Double.parseDouble(str);
        obj.setParameterValue(VerticalMovement_FixedOffBottomRange.PARAM_minDistOffBottom, val);
    }//GEN-LAST:event_jtfMinDistanceOffBottomActionPerformed

    private void jtfMaxDistOffBottomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMaxDistOffBottomActionPerformed
        String str = jtfMaxDistOffBottom.getText();
        Double val = Double.parseDouble(str);
        obj.setParameterValue(VerticalMovement_FixedOffBottomRange.PARAM_maxDistOffBottom, val);
    }//GEN-LAST:event_jtfMaxDistOffBottomActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jtfMaxDepth;
    private javax.swing.JTextField jtfMaxDistOffBottom;
    private javax.swing.JTextField jtfMinDistanceOffBottom;
    private javax.swing.JTextField jtfRPW;
    // End of variables declaration//GEN-END:variables
}
