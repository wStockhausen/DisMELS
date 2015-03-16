/*
 * SimpleBenthicJuvenileLHSAttributesCustomizer.java
 *
 * Created on January 12, 2006, 4:20 PM
 */

package wts.models.DisMELS.LHS.BenthicJuvenile;

import wts.models.DisMELS.framework.*;
import wts.models.DisMELS.gui.AttributesCustomizer;

/**
 * @author William Stockhausen
 */
public class SimpleBenthicJuvenileLHSAttributesCustomizer extends AttributesCustomizer {

    private boolean showHorizPos = true;
    private boolean showVertPos = false;
    
    private SimpleBenthicJuvenileLHSAttributes attributes = null;
//                                    new SimpleBenthicJuvenileLHSAttributes("");
    
    /**
     * Creates new customizer SimpleBenthicJuvenileLHSAttributesCustomizer
     */
    public SimpleBenthicJuvenileLHSAttributesCustomizer() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jpTime = new javax.swing.JPanel();
        jtfTime = new javax.swing.JTextField();
        jpHoriz = new javax.swing.JPanel();
        jcbHorizType = new javax.swing.JComboBox();
        jtfX = new javax.swing.JTextField();
        jtfY = new javax.swing.JTextField();
        lblX = new javax.swing.JLabel();
        lblY = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jpVert = new javax.swing.JPanel();
        jtfZ = new javax.swing.JTextField();
        jcbVertType = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        lblZ = new javax.swing.JLabel();
        jpOther = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jtfAge = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfAgeInStage = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtfSize = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtfNumber = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jpTime.setBorder(javax.swing.BorderFactory.createTitledBorder("Start time"));

        jtfTime.setText("0.0");
        jtfTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfTimeActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jpTimeLayout = new org.jdesktop.layout.GroupLayout(jpTime);
        jpTime.setLayout(jpTimeLayout);
        jpTimeLayout.setHorizontalGroup(
            jpTimeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpTimeLayout.createSequentialGroup()
                .addContainerGap()
                .add(jtfTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(181, Short.MAX_VALUE))
        );
        jpTimeLayout.setVerticalGroup(
            jpTimeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpTimeLayout.createSequentialGroup()
                .add(jtfTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpHoriz.setBorder(javax.swing.BorderFactory.createTitledBorder("Horizontal position"));

        jcbHorizType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Grid IJ", "Grid XY", "Lat/Lon" }));
        jcbHorizType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbHorizTypeActionPerformed(evt);
            }
        });

        jtfX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfXActionPerformed(evt);
            }
        });

        jtfY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfYActionPerformed(evt);
            }
        });

        lblX.setText("Lon");
        lblX.setMaximumSize(new java.awt.Dimension(30, 14));
        lblX.setPreferredSize(new java.awt.Dimension(30, 14));

        lblY.setText("Lat");

        jLabel3.setText("Type");

        org.jdesktop.layout.GroupLayout jpHorizLayout = new org.jdesktop.layout.GroupLayout(jpHoriz);
        jpHoriz.setLayout(jpHorizLayout);
        jpHorizLayout.setHorizontalGroup(
            jpHorizLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpHorizLayout.createSequentialGroup()
                .addContainerGap()
                .add(jpHorizLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jtfX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpHorizLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jtfY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblY))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpHorizLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jcbHorizType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 145, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jpHorizLayout.setVerticalGroup(
            jpHorizLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpHorizLayout.createSequentialGroup()
                .add(jpHorizLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblY)
                    .add(jLabel3)
                    .add(lblX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpHorizLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jcbHorizType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jtfY, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jtfX, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpVert.setBorder(javax.swing.BorderFactory.createTitledBorder("Vertical position"));

        jtfZ.setText("0.");
        jtfZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfZActionPerformed(evt);
            }
        });

        jcbVertType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Grid K", "Z (< 0)", "Depth (>0)", "Dist. above bottom" }));
        jcbVertType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbVertTypeActionPerformed(evt);
            }
        });

        jLabel1.setText("Type");

        lblZ.setText("Depth");

        org.jdesktop.layout.GroupLayout jpVertLayout = new org.jdesktop.layout.GroupLayout(jpVert);
        jpVert.setLayout(jpVertLayout);
        jpVertLayout.setHorizontalGroup(
            jpVertLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jpVertLayout.createSequentialGroup()
                .addContainerGap()
                .add(jpVertLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jtfZ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblZ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(55, 55, 55)
                .add(jpVertLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jcbVertType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        jpVertLayout.setVerticalGroup(
            jpVertLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpVertLayout.createSequentialGroup()
                .add(jpVertLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(lblZ))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpVertLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jcbVertType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jtfZ, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpOther.setBorder(javax.swing.BorderFactory.createTitledBorder("Other attributes"));

        jLabel2.setText("age");

        jtfAge.setText("0.0");
        jtfAge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfAgeActionPerformed(evt);
            }
        });

        jLabel4.setText("age in stage");

        jtfAgeInStage.setText("0.0");
        jtfAgeInStage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfAgeInStageActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jtfAge, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .add(40, 40, 40)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jtfAgeInStage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfAgeInStage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jtfAge, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("size");

        jtfSize.setText("0.0");
        jtfSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfSizeActionPerformed(evt);
            }
        });

        jLabel6.setText("number");

        jtfNumber.setText("0.0");
        jtfNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfNumberActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jtfSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel5))
                .add(40, 40, 40)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel6)
                    .add(jtfNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(jLabel5))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfNumber, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jtfSize, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jpOtherLayout = new org.jdesktop.layout.GroupLayout(jpOther);
        jpOther.setLayout(jpOtherLayout);
        jpOtherLayout.setHorizontalGroup(
            jpOtherLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpOtherLayout.createSequentialGroup()
                .addContainerGap()
                .add(jpOtherLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jpOtherLayout.setVerticalGroup(
            jpOtherLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpOtherLayout.createSequentialGroup()
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpTime, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jpHoriz, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jpVert, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jpOther, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jpTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpHoriz, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpVert, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpOther, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jtfNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfNumberActionPerformed
// TODO add your handling code here:
        Double n = new Double(jtfNumber.getText());
        attributes.setValue(attributes.PROP_number,n);
    }//GEN-LAST:event_jtfNumberActionPerformed

    private void jtfSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfSizeActionPerformed
        Double n = new Double(jtfSize.getText());
        attributes.setValue(attributes.PROP_size,n);
    }//GEN-LAST:event_jtfSizeActionPerformed

    private void jtfAgeInStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfAgeInStageActionPerformed
        Double n = new Double(jtfAgeInStage.getText());
        attributes.setValue(attributes.PROP_ageInStage,n);
    }//GEN-LAST:event_jtfAgeInStageActionPerformed

    private void jtfAgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfAgeActionPerformed
        Double n = new Double(jtfAge.getText());
        attributes.setValue(attributes.PROP_age,n);
    }//GEN-LAST:event_jtfAgeActionPerformed

    private void jtfZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfZActionPerformed
        Double n = new Double(jtfZ.getText());
        attributes.setValue(attributes.PROP_vertPos,n);
    }//GEN-LAST:event_jtfZActionPerformed

    private void jtfYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfYActionPerformed
        Double n = new Double(jtfY.getText());
        attributes.setValue(attributes.PROP_horizPos2,n);
    }//GEN-LAST:event_jtfYActionPerformed

    private void jtfXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfXActionPerformed
        Double n = new Double(jtfX.getText());
        attributes.setValue(attributes.PROP_horizPos1,n);
    }//GEN-LAST:event_jtfXActionPerformed

    private void jcbHorizTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbHorizTypeActionPerformed
        int idx = jcbHorizType.getSelectedIndex();
        attributes.setValue(attributes.PROP_horizType,new Integer(idx));
        switch (idx) {
            case 0:
                lblX.setText("I");
                lblY.setText("J");
                break;
            case 1:
                lblX.setText("X");
                lblY.setText("Y");
                break;
            case 2:
                lblX.setText("Lon");
                lblY.setText("Lat");
                break;
        }       
    }//GEN-LAST:event_jcbHorizTypeActionPerformed

    private void jtfTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfTimeActionPerformed
        Double n = new Double(jtfTime.getText());
        attributes.setValue(attributes.PROP_startTime,n);
    }//GEN-LAST:event_jtfTimeActionPerformed

    private void jcbVertTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbVertTypeActionPerformed
        int idx = jcbVertType.getSelectedIndex();
        attributes.setValue(attributes.PROP_vertType,new Integer(idx));
        switch (idx) {
            case Types.VERT_K:
                lblZ.setText("K");
                break;
            case Types.VERT_Z:
                lblZ.setText("Z");
                break;
            case Types.VERT_H:
                lblZ.setText("Depth");
                break;
            case Types.VERT_DH:
                lblZ.setText("Distance off bottom");
                break;
        }
    }//GEN-LAST:event_jcbVertTypeActionPerformed

    public void setObject(Object bean) {
        if (bean instanceof SimpleBenthicJuvenileLHSAttributes) {
            setAttributes((SimpleBenthicJuvenileLHSAttributes) bean);
        }
    }
    
    @Override
    public SimpleBenthicJuvenileLHSAttributes getAttributes() {
        return attributes;
    }
    
    @Override
    public void setAttributes(LifeStageAttributesInterface newAtts) {
        if (newAtts instanceof SimpleBenthicJuvenileLHSAttributes) {
            attributes = (SimpleBenthicJuvenileLHSAttributes) newAtts;
            Boolean b = null;
            Double d = null;
            Integer i = null;
            jcbHorizType.setSelectedIndex(attributes.getValue(attributes.PROP_horizType,i).intValue());
            jcbVertType.setSelectedIndex(attributes.getValue(attributes.PROP_vertType,i).intValue());
            jtfTime.setText(attributes.getValue(attributes.PROP_startTime,d).toString());
            jtfAge.setText(attributes.getValue(attributes.PROP_age,d).toString());
            jtfAgeInStage.setText(attributes.getValue(attributes.PROP_ageInStage,d).toString());
            jtfSize.setText(attributes.getValue(attributes.PROP_size,d).toString());
            jtfNumber.setText(attributes.getValue(attributes.PROP_number,d).toString());
            jtfX.setText(attributes.getValue(attributes.PROP_horizPos1,d).toString());
            jtfY.setText(attributes.getValue(attributes.PROP_horizPos2,d).toString());
            jtfZ.setText(attributes.getValue(attributes.PROP_vertPos,d).toString());
        }
    }
    
    public void showHorizPos(boolean b) {
        jpHoriz.setVisible(b);
        revalidate();
    }
    
    public void showVertPos(boolean b) {
        jpVert.setVisible(b);
        revalidate();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JComboBox jcbHorizType;
    private javax.swing.JComboBox jcbVertType;
    private javax.swing.JPanel jpHoriz;
    private javax.swing.JPanel jpOther;
    private javax.swing.JPanel jpTime;
    private javax.swing.JPanel jpVert;
    private javax.swing.JTextField jtfAge;
    private javax.swing.JTextField jtfAgeInStage;
    private javax.swing.JTextField jtfNumber;
    private javax.swing.JTextField jtfSize;
    private javax.swing.JTextField jtfTime;
    private javax.swing.JTextField jtfX;
    private javax.swing.JTextField jtfY;
    private javax.swing.JTextField jtfZ;
    private javax.swing.JLabel lblX;
    private javax.swing.JLabel lblY;
    private javax.swing.JLabel lblZ;
    // End of variables declaration//GEN-END:variables
    
}
