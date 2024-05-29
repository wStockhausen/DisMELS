/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.GIS.styling;

import java.awt.Color;
import java.beans.Customizer;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author William.Stockhausen
 */
public class VectorStyleCustomizer extends javax.swing.JPanel implements Customizer {

    /**property indicating bean settings have been modified in some form */
    public static final String PROP_StyleChanged = "wts.GIS.styling. VectorStyleCustomizer: StyleChanged";
    
    /** the bean instance to be customized */
    private VectorStyle obj;
    /** flag to enable "input" event processing */
    private boolean enableEvents;
    
    /** class logger */
    private static final Logger logger = Logger.getLogger(VectorStyleCustomizer.class.getName());
    
    /**
     * Creates new form VectorStyleCustomizer
     */
    public VectorStyleCustomizer() {
        enableEvents = false;
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

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfStdScale = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jtfExpScl = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtfMapLength = new javax.swing.JTextField();
        jtfStdWidth = new javax.swing.JTextField();
        jchkFixedLength = new javax.swing.JCheckBox();
        jchkFixedWidth = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jtfHeadSize = new javax.swing.JTextField();
        jchkFixedHeadSize = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jtfHeadAngle = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jtfStride = new javax.swing.JTextField();
        jpSingleColor = new javax.swing.JPanel();
        jpFixedColor = new javax.swing.JPanel();
        jchkFixedColor = new javax.swing.JCheckBox();
        cbsCustomizer = new wts.GIS.styling.ColorBarStyleCustomizer();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jPanel2.border.title"))); // NOI18N

        jLabel1.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel1.toolTipText")); // NOI18N

        jtfStdScale.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfStdScale.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfStdScale.text")); // NOI18N
        jtfStdScale.setMinimumSize(new java.awt.Dimension(24, 22));
        jtfStdScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfStdScaleActionPerformed(evt);
            }
        });

        jLabel5.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel5.text")); // NOI18N

        jtfExpScl.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfExpScl.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfExpScl.text")); // NOI18N
        jtfExpScl.setMinimumSize(new java.awt.Dimension(24, 22));
        jtfExpScl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfExpSclActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfStdScale, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfExpScl, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jtfStdScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jtfExpScl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jPanel3.border.title"))); // NOI18N

        jLabel2.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel2.text")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel3.text")); // NOI18N

        jtfMapLength.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfMapLength.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfMapLength.text")); // NOI18N
        jtfMapLength.setToolTipText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfMapLength.toolTipText")); // NOI18N
        jtfMapLength.setPreferredSize(new java.awt.Dimension(100, 22));
        jtfMapLength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMapLengthActionPerformed(evt);
            }
        });

        jtfStdWidth.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfStdWidth.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfStdWidth.text")); // NOI18N
        jtfStdWidth.setToolTipText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfStdWidth.toolTipText")); // NOI18N
        jtfStdWidth.setPreferredSize(new java.awt.Dimension(100, 22));
        jtfStdWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfStdWidthActionPerformed(evt);
            }
        });

        jchkFixedLength.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jchkFixedLength.text")); // NOI18N
        jchkFixedLength.setToolTipText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jchkFixedLength.toolTipText")); // NOI18N
        jchkFixedLength.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkFixedLengthActionPerformed(evt);
            }
        });

        jchkFixedWidth.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jchkFixedWidth.text")); // NOI18N
        jchkFixedWidth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkFixedWidthActionPerformed(evt);
            }
        });

        jLabel4.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel4.text")); // NOI18N
        jLabel4.setToolTipText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel4.toolTipText")); // NOI18N

        jtfHeadSize.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfHeadSize.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfHeadSize.text")); // NOI18N
        jtfHeadSize.setToolTipText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfHeadSize.toolTipText")); // NOI18N
        jtfHeadSize.setPreferredSize(new java.awt.Dimension(100, 22));
        jtfHeadSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfHeadSizeActionPerformed(evt);
            }
        });

        jchkFixedHeadSize.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jchkFixedHeadSize.text")); // NOI18N
        jchkFixedHeadSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkFixedHeadSizeActionPerformed(evt);
            }
        });

        jLabel6.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel6.text")); // NOI18N
        jLabel6.setToolTipText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel6.toolTipText")); // NOI18N

        jtfHeadAngle.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfHeadAngle.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfHeadAngle.text")); // NOI18N
        jtfHeadAngle.setToolTipText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfHeadAngle.toolTipText")); // NOI18N
        jtfHeadAngle.setPreferredSize(new java.awt.Dimension(100, 22));
        jtfHeadAngle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfHeadAngleActionPerformed(evt);
            }
        });

        jLabel7.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jLabel7.text")); // NOI18N

        jtfStride.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfStride.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfStride.text")); // NOI18N
        jtfStride.setToolTipText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jtfStride.toolTipText")); // NOI18N
        jtfStride.setPreferredSize(new java.awt.Dimension(50, 22));
        jtfStride.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfStrideActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jtfHeadSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jtfMapLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfStdWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfHeadAngle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jchkFixedWidth)
                    .addComponent(jchkFixedHeadSize)
                    .addComponent(jchkFixedLength)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtfStride, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jchkFixedLength)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtfMapLength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtfStdWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jchkFixedWidth))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jtfHeadSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jchkFixedHeadSize))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtfHeadAngle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jtfStride, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        jpSingleColor.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jpSingleColor.border.title"))); // NOI18N

        jpFixedColor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpFixedColor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jpFixedColorMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jpFixedColorLayout = new javax.swing.GroupLayout(jpFixedColor);
        jpFixedColor.setLayout(jpFixedColorLayout);
        jpFixedColorLayout.setHorizontalGroup(
            jpFixedColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );
        jpFixedColorLayout.setVerticalGroup(
            jpFixedColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 24, Short.MAX_VALUE)
        );

        jchkFixedColor.setText(org.openide.util.NbBundle.getMessage(VectorStyleCustomizer.class, "VectorStyleCustomizer.jchkFixedColor.text")); // NOI18N
        jchkFixedColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkFixedColorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpSingleColorLayout = new javax.swing.GroupLayout(jpSingleColor);
        jpSingleColor.setLayout(jpSingleColorLayout);
        jpSingleColorLayout.setHorizontalGroup(
            jpSingleColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSingleColorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jchkFixedColor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jpFixedColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(248, 248, 248))
        );
        jpSingleColorLayout.setVerticalGroup(
            jpSingleColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jpFixedColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jchkFixedColor)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpSingleColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbsCustomizer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(205, 205, 205)
                .addComponent(jpSingleColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbsCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 385, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(62, 62, 62)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(243, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfStdScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfStdScaleActionPerformed
        if (enableEvents) {
            double val = Double.parseDouble(jtfStdScale.getText());
            obj.setStandardScale(val);
            firePropertyChange(PROP_StyleChanged, null, val);
        }
    }//GEN-LAST:event_jtfStdScaleActionPerformed

    private void jtfExpSclActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfExpSclActionPerformed
        if (enableEvents) {
            obj.setExponentScale(Double.parseDouble(jtfExpScl.getText()));
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jtfExpSclActionPerformed

    private void jtfMapLengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMapLengthActionPerformed
        if (enableEvents) {
            obj.setMapLength(Double.parseDouble(jtfMapLength.getText()));
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jtfMapLengthActionPerformed

    private void jtfStdWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfStdWidthActionPerformed
        if (enableEvents) {
            obj.setStandardWidth(Double.parseDouble(jtfStdWidth.getText()));
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jtfStdWidthActionPerformed

    private void jchkFixedLengthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkFixedLengthActionPerformed
        if (enableEvents) {
            obj.setUseFixedLength(jchkFixedLength.isSelected());
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jchkFixedLengthActionPerformed

    private void jchkFixedWidthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkFixedWidthActionPerformed
        if (enableEvents) {
            obj.setUseFixedWidth(jchkFixedWidth.isSelected());
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jchkFixedWidthActionPerformed

    private void jtfHeadSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfHeadSizeActionPerformed
        if (enableEvents) {
            obj.setArrowScale(Double.parseDouble(jtfHeadSize.getText()));
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jtfHeadSizeActionPerformed

    private void jchkFixedHeadSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkFixedHeadSizeActionPerformed
        if (enableEvents) {
            obj.setUseFixedHeadSize(jchkFixedHeadSize.isSelected());
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jchkFixedHeadSizeActionPerformed

    private void jtfHeadAngleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfHeadAngleActionPerformed
        if (enableEvents) {
            obj.setArrowAngle(Double.parseDouble(jtfHeadAngle.getText()));
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jtfHeadAngleActionPerformed

    private void jpFixedColorMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jpFixedColorMousePressed
        if (enableEvents) {
            Color clr = JColorChooser.showDialog(this, "Choose vector color", jpFixedColor.getBackground());
            if (clr != null) {
                jpFixedColor.setBackground(clr);
                obj.setFixedColor(clr);
            }
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jpFixedColorMousePressed

    private void jchkFixedColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkFixedColorActionPerformed
        if (enableEvents){
            jpFixedColor.setVisible(jchkFixedColor.isSelected());
            cbsCustomizer.setVisible(!jchkFixedColor.isSelected());
            obj.setUseFixedColor(jchkFixedColor.isSelected());
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jchkFixedColorActionPerformed

    private void jtfStrideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfStrideActionPerformed
        if (enableEvents){
            double d = Double.parseDouble(jtfStride.getText());
            if (d>0) {
                obj.setStride(d);
            } else {
                NotifyDescriptor nd = new NotifyDescriptor.Message("The stride cannot be <=0. \nResetting to previous value.",NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);
                jtfStride.setText(Double.toString(obj.getStride()));
            }
            firePropertyChange(PROP_StyleChanged, null, null);
        }
    }//GEN-LAST:event_jtfStrideActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected wts.GIS.styling.ColorBarStyleCustomizer cbsCustomizer;
    protected javax.swing.JLabel jLabel1;
    protected javax.swing.JLabel jLabel2;
    protected javax.swing.JLabel jLabel3;
    protected javax.swing.JLabel jLabel4;
    protected javax.swing.JLabel jLabel5;
    protected javax.swing.JLabel jLabel6;
    protected javax.swing.JLabel jLabel7;
    protected javax.swing.JPanel jPanel2;
    protected javax.swing.JPanel jPanel3;
    protected javax.swing.JCheckBox jchkFixedColor;
    protected javax.swing.JCheckBox jchkFixedHeadSize;
    protected javax.swing.JCheckBox jchkFixedLength;
    protected javax.swing.JCheckBox jchkFixedWidth;
    protected javax.swing.JPanel jpFixedColor;
    protected javax.swing.JPanel jpSingleColor;
    protected javax.swing.JTextField jtfExpScl;
    protected javax.swing.JTextField jtfHeadAngle;
    protected javax.swing.JTextField jtfHeadSize;
    protected javax.swing.JTextField jtfMapLength;
    protected javax.swing.JTextField jtfStdScale;
    protected javax.swing.JTextField jtfStdWidth;
    protected javax.swing.JTextField jtfStride;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setObject(Object bean) {
        if (bean instanceof VectorStyle){
            logger.info("Starting setObject(): "+bean.toString());
            obj = (VectorStyle) bean;
            enableEvents = false;
            //set values
            jchkFixedColor.setSelected(obj.getUseFixedColor());
            jchkFixedHeadSize.setSelected(obj.getUseFixedHeadSize());
            jchkFixedLength.setSelected(obj.getUseFixedLength());
            jchkFixedWidth.setSelected(obj.getUseFixedWidth());
            
            jpFixedColor.setBackground(obj.getFixedColor());
            jpFixedColor.setVisible(obj.getUseFixedColor());
            cbsCustomizer.setObject(obj.getColorBarStyle());
            cbsCustomizer.setVisible(!obj.getUseFixedColor());

            jtfExpScl.setText(Double.toString(obj.getExponentScale()));
            jtfHeadAngle.setText(Double.toString(obj.getArrowheadAngle()));
            jtfHeadSize.setText(Double.toString(obj.getArrowScale()));
            jtfMapLength.setText(Double.toString(obj.getMapLength()));
            jtfStdScale.setText(Double.toString(obj.getStandardScale()));
            jtfStdWidth.setText(Double.toString(obj.getStandardWidth()));
            jtfStride.setText(Double.toString(obj.getStride()));
            repaint();
            enableEvents = true;
            logger.info("Finished setObject(): "+bean.toString());
        }
    }
}
