/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ColorBarStyleEditor.java
 *
 * Created on Apr 6, 2009, 5:41:58 AM
 */

package wts.GIS.gui;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.factory.FactoryConfigurationError;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.SchemaException;
import org.geotools.filter.IllegalFilterException;
import wts.GIS.styling.ColorBarStyle;

/**
 *
 * @author wstockhausen
 */
public class ColorBarStyleEditor extends javax.swing.JDialog {

    public static final int ACCEPT_CHANGES = 0;
    public static final int CANCEL_CHANGES = 1;

    int result;

    private ColorBarStyle obj;

    /** Creates new form ColorBarStyleEditor */
    public ColorBarStyleEditor(java.awt.Frame parent, ColorBarStyle oldObj, boolean modal) {
        super(parent, modal);
        initComponents();
        setObject(oldObj);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    /** For testing! */
    public ColorBarStyleEditor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jbOK = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        cbsCustomizer = new wts.GIS.styling.ColorBarStyleCustomizer();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOK.setText("OK");
        jbOK.setMaximumSize(new java.awt.Dimension(73, 25));
        jbOK.setMinimumSize(new java.awt.Dimension(73, 25));
        jbOK.setPreferredSize(new java.awt.Dimension(73, 25));
        jbOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOKActionPerformed(evt);
            }
        });
        jPanel1.add(jbOK);

        jbCancel.setText("Cancel");
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jbCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbsCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbsCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOKActionPerformed
        if (this.getModalityType()!=ModalityType.MODELESS) {
            result = ACCEPT_CHANGES;
            setVisible(false);
            dispose();
        }
    }//GEN-LAST:event_jbOKActionPerformed

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        if (this.getModalityType()!=ModalityType.MODELESS) {
            //user pressed "Cancel" button
            result = CANCEL_CHANGES;
            setVisible(false);
            dispose();
        } else {
            //user pressed "apply" button to update style
            result = ACCEPT_CHANGES;
            try {
                obj.updateStyle();
            } catch (IllegalFilterException ex) {
                Logger.getLogger(ColorBarStyleEditor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jbCancelActionPerformed

    public ColorBarStyle getObject() {
        return obj;
    }

    public void setObject(ColorBarStyle oldObj) {
        if (this.getModalityType()==ModalityType.MODELESS) {
            //modify passed in object directly
            obj = oldObj;
            jbOK.setVisible(false);
            jbCancel.setText("Apply");
            jbCancel.setToolTipText("Press to apply changes to style");

        } else {
            //modify clone of object passed in
            obj = (ColorBarStyle)oldObj.clone();
            //must call 'updateStyle' on cloned object to finish the style
        }
        cbsCustomizer.setObject(obj);
    }

    /**
     * Makes the dialog visible, then returns the result (ACCEPT_ or CANCEL_CHANGES)
     * from the user's interaction.  If ACCEPT_CHANGES, the user can access the
     * updated object via @METHOD getLarvalParameters().
     *
     * @return - ACCEPT_CHANGES or CANCEL_CHANGES.
     */
    public int showDialog() {
        this.setVisible(true);
        return result;
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AttributeType[] attTypes = null;
                    FeatureType ft = FeatureTypeFactory.newFeatureType(attTypes, "the FeatureType");
                    ColorBarStyle cbs = new ColorBarStyle(ft);
                    cbs.setMin(0);
                    cbs.setMax(1);
                    cbs.setColorRamp("jet");
                    cbs.setNumberOfColors(25);
                    ColorBarStyleEditor dialog = new ColorBarStyleEditor(new javax.swing.JFrame(), true);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setObject(cbs);
                    if (dialog.showDialog()==dialog.ACCEPT_CHANGES) {
                        System.out.println("Accepted changes!!");
                    } else {
                        System.out.println("Canceled changes!!");
                    }
                    System.exit(0);
                } catch (FactoryConfigurationError ex) {
                    Logger.getLogger(ColorBarStyleEditor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SchemaException ex) {
                    Logger.getLogger(ColorBarStyleEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private wts.GIS.styling.ColorBarStyleCustomizer cbsCustomizer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOK;
    // End of variables declaration//GEN-END:variables

}
