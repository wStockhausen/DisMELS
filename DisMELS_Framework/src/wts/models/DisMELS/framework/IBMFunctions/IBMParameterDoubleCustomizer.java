/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.IBMFunctions;

import java.beans.Customizer;

/**
 *
 * @author William.Stockhausen
 */
public class IBMParameterDoubleCustomizer extends javax.swing.JPanel 
                                              implements Customizer {

    /** property flag indicating change in value */
    public static final String VALUE_CHANGED = "value changed";
    
    /** local copy of model parameter */
    private IBMParameterDouble ip = null;
    
    /**
     * Creates new form IBMParameterDoubleCustomizer
     */
    public IBMParameterDoubleCustomizer() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel = new javax.swing.JLabel();
        jtfValue = new javax.swing.JTextField();

        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        setMaximumSize(new java.awt.Dimension(32767, 22));
        setMinimumSize(new java.awt.Dimension(400, 27));

        jLabel.setText(org.openide.util.NbBundle.getMessage(IBMParameterDoubleCustomizer.class, "IBMParameterDoubleCustomizer.jLabel.text")); // NOI18N

        jtfValue.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfValue.setText(org.openide.util.NbBundle.getMessage(IBMParameterDoubleCustomizer.class, "IBMParameterDoubleCustomizer.jtfValue.text")); // NOI18N
        jtfValue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfValueActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jtfValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfValueActionPerformed
        System.out.println("ModelParameterDoubleCustomizer PropertyChange: Value");
        Double oldVal = ip.getValue();
        ip.parseValue(jtfValue.getText());
        firePropertyChange(VALUE_CHANGED, oldVal, ip.getValue());
    }//GEN-LAST:event_jtfValueActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JLabel jLabel;
    protected javax.swing.JTextField jtfValue;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setObject(Object bean) {
        if (bean.getClass().equals(IBMParameterDouble.class)) {
            ip = (IBMParameterDouble) bean;
            jLabel.setText(ip.description);
            jLabel.setToolTipText(ip.name);
            jtfValue.setText(ip.getValueAsString());
//            jtfValue.setToolTipText(ip.description);
        }
    }
}
