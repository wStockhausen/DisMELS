/*
 * SpawningParametersCustomizer.java
 *
 * Created on May 21, 2008, 11:12 AM
 */

package wts.models.DisMELS.LHS.BenthicAdult;

/**
 *
 * @author  wstockhausen
 */
public class SpawningParametersCustomizer extends javax.swing.JPanel {

    //Static fields
    public static final String PROP_batchPeriod  = "batchPeriod";
    public static final String PROP_batchSpawner = "batchSpawner";
    public static final String PROP_duration     = "duration";
    public static final String PROP_fecundity    = "fecundity";
    public static final String PROP_firstDay     = "firstDaySpawning";
    public static final String PROP_randomizedSpawning = "randomizedSpawning";
    public static final String PROP_recoveryPeriod     = "recoveryPeriod";

    //new Bean fields
    private double batchPeriod   = 0.0;
    private boolean batchSpawner = false;
    private double duration      = 0.0;
    private double fecundity     = 0.0;
    private double firstDay      = 0.0;
    private boolean randomizedSpawning = false;
    private double recoveryPeriod      = 0.0;

    /**
     * Utility field used by bound properties.
     */
    private java.beans.PropertyChangeSupport propertyChangeSupport =
            new java.beans.PropertyChangeSupport(this);

    /** Creates new form SpawningParametersCustomizer */
    public SpawningParametersCustomizer() {
        initComponents();
//        setFirstDay(firstDay);
//        setDuration(duration);
//        setFecundity(fecundity);
//        setRandomizedSpawning(randomizedSpawning);
//        setBatchSpawner(batchSpawner);
//        setRecoveryPeriod(recoveryPeriod);
//        setBatchPeriod(batchPeriod);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jtfFirstDay = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jtfDuration = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jtfFecundity = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jcbRandomizedSpawning = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jcbBatchSpawner = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jtfRecoveryPeriod = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfBatchPeriod = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Spawning parameters")); // NOI18N
        setName("Form"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Season")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jtfFirstDay.setText("0"); // NOI18N
        jtfFirstDay.setToolTipText("Day of year on which spawning commences"); // NOI18N
        jtfFirstDay.setMinimumSize(new java.awt.Dimension(20, 22));
        jtfFirstDay.setName("jtfFirstDay"); // NOI18N
        jtfFirstDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfFirstDayActionPerformed(evt);
            }
        });

        jLabel1.setText("First day"); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfDuration.setText("365"); // NOI18N
        jtfDuration.setToolTipText("length of spawning season (days)"); // NOI18N
        jtfDuration.setName("jtfDuration"); // NOI18N
        jtfDuration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDurationActionPerformed(evt);
            }
        });

        jLabel2.setText("Duration (d)"); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfFirstDay, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(78, 78, 78)
                .addComponent(jtfDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(50, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfFirstDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jtfDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setName("jPanel2"); // NOI18N

        jtfFecundity.setText("1"); // NOI18N
        jtfFecundity.setMinimumSize(new java.awt.Dimension(20, 22));
        jtfFecundity.setName("jtfFecundity"); // NOI18N
        jtfFecundity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfFecundityActionPerformed(evt);
            }
        });

        jLabel3.setText("Fecundity"); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jcbRandomizedSpawning.setText("Randomize spawning?"); // NOI18N
        jcbRandomizedSpawning.setName("jcbRandomizedSpawning"); // NOI18N
        jcbRandomizedSpawning.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbRandomizedSpawningItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfFecundity, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(62, 62, 62)
                .addComponent(jcbRandomizedSpawning)
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jcbRandomizedSpawning)
                .addComponent(jLabel3)
                .addComponent(jtfFecundity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Batch spawning")); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jcbBatchSpawner.setText("Batch spawner?"); // NOI18N
        jcbBatchSpawner.setName("jcbBatchSpawner"); // NOI18N
        jcbBatchSpawner.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbBatchSpawnerItemStateChanged(evt);
            }
        });

        jPanel4.setName("jPanel4"); // NOI18N

        jtfRecoveryPeriod.setText("0"); // NOI18N
        jtfRecoveryPeriod.setName("jtfRecoveryPeriod"); // NOI18N
        jtfRecoveryPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfRecoveryPeriodActionPerformed(evt);
            }
        });

        jLabel4.setText("Recovery period (d)"); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jtfBatchPeriod.setText("0"); // NOI18N
        jtfBatchPeriod.setName("jtfBatchPeriod"); // NOI18N
        jtfBatchPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfBatchPeriodActionPerformed(evt);
            }
        });

        jLabel5.setText("Batch period (d)"); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jtfRecoveryPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jtfBatchPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jtfRecoveryPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel4)
                .addComponent(jtfBatchPeriod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel5))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jcbBatchSpawner)
                        .addContainerGap(250, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jcbBatchSpawner)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfFirstDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfFirstDayActionPerformed
        double oldVal = firstDay;
        double newVal = Double.valueOf(jtfFirstDay.getText());
        propertyChangeSupport.firePropertyChange (PROP_firstDay, oldVal, newVal);
    }//GEN-LAST:event_jtfFirstDayActionPerformed

    private void jtfDurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDurationActionPerformed
        double oldVal = duration;
        double newVal = Double.valueOf(jtfDuration.getText());
        propertyChangeSupport.firePropertyChange (PROP_duration, oldVal, newVal);
    }//GEN-LAST:event_jtfDurationActionPerformed

    private void jtfFecundityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfFecundityActionPerformed
        double oldVal = duration;
        double newVal = Double.valueOf(jtfFecundity.getText());
        propertyChangeSupport.firePropertyChange (PROP_fecundity, oldVal, newVal);
    }//GEN-LAST:event_jtfFecundityActionPerformed

    private void jcbRandomizedSpawningItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbRandomizedSpawningItemStateChanged
        boolean oldVal = randomizedSpawning;
        boolean newVal = jcbRandomizedSpawning.isSelected();
        propertyChangeSupport.firePropertyChange (PROP_randomizedSpawning, oldVal, newVal);
    }//GEN-LAST:event_jcbRandomizedSpawningItemStateChanged

    private void jcbBatchSpawnerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbBatchSpawnerItemStateChanged
        boolean oldVal = batchSpawner;
        boolean newVal = jcbBatchSpawner.isSelected();
        propertyChangeSupport.firePropertyChange (PROP_batchSpawner, oldVal, newVal);
    }//GEN-LAST:event_jcbBatchSpawnerItemStateChanged

    private void jtfRecoveryPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfRecoveryPeriodActionPerformed
        double oldVal = duration;
        double newVal = Double.valueOf(jtfRecoveryPeriod.getText());
        propertyChangeSupport.firePropertyChange (PROP_recoveryPeriod, oldVal, newVal);
    }//GEN-LAST:event_jtfRecoveryPeriodActionPerformed

    private void jtfBatchPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfBatchPeriodActionPerformed
        double oldVal = duration;
        double newVal = Double.valueOf(jtfBatchPeriod.getText());
        propertyChangeSupport.firePropertyChange (PROP_batchPeriod, oldVal, newVal);
    }//GEN-LAST:event_jtfBatchPeriodActionPerformed

    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    @Override
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    @Override
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JCheckBox jcbBatchSpawner;
    private javax.swing.JCheckBox jcbRandomizedSpawning;
    private javax.swing.JTextField jtfBatchPeriod;
    private javax.swing.JTextField jtfDuration;
    private javax.swing.JTextField jtfFecundity;
    private javax.swing.JTextField jtfFirstDay;
    private javax.swing.JTextField jtfRecoveryPeriod;
    // End of variables declaration//GEN-END:variables

    public double getBatchPeriod() {
        return batchPeriod;
    }

    public void setBatchPeriod(double newVal) {
        jtfBatchPeriod.setText(String.valueOf(newVal));
    }

    public boolean isBatchSpawner() {
        return batchSpawner;
    }

    public void setBatchSpawner(boolean newVal) {
        jcbBatchSpawner.setSelected(newVal);
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double newVal) {
        jtfDuration.setText(String.valueOf(newVal));
    }

    public double getFecundity() {
        return fecundity;
    }

    public void setFecundity(double newVal) {
        jtfFecundity.setText(String.valueOf(newVal));
    }

    public double getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(double newVal) {
        jtfFirstDay.setText(String.valueOf(newVal));
    }

    public boolean isRandomizedSpawning() {
        return randomizedSpawning;
    }

    public void setRandomizedSpawning(boolean newVal) {
        jcbRandomizedSpawning.setSelected(newVal);
    }

    public double getRecoveryPeriod() {
        return recoveryPeriod;
    }

    public void setRecoveryPeriod(double newVal) {
        jtfRecoveryPeriod.setText(String.valueOf(newVal));
    }

}
