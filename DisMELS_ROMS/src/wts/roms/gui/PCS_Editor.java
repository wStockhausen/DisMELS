/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PCS_Editor.java
 *
 * Created on Mar 26, 2010, 9:59:13 AM
 */

package wts.roms.gui;

import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.ct.CannotCreateTransformException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import wts.roms.gis.AbstractPCS;
import wts.roms.gis.newAlbersNAD83;

/**
 *
 * @author wstockhausen
 */
public class PCS_Editor extends javax.swing.JDialog {

    public static final int ACCEPT_CHANGES = 0;
    public static final int CANCEL_CHANGES = 1;

    private int result = CANCEL_CHANGES;
    private AbstractPCS pcs = null;

    /** Creates new form PCS_Editor */
    public PCS_Editor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        try {
            pcs = new newAlbersNAD83();
            initComponents();
            setPCS(pcs);
        } catch (FactoryException ex) {
            Logger.getLogger(PCS_Editor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CannotCreateTransformException ex) {
            Logger.getLogger(PCS_Editor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(PCS_Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** Creates new form PCS_Editor */
    public PCS_Editor(java.awt.Frame parent, AbstractPCS oldPCS, boolean modal) {
        super(parent, modal);
        initComponents();
        setPCS(oldPCS);
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

    void setPCS(AbstractPCS oldPCS) {
        pcs = oldPCS;
        pcsCustomizer.setPCS(pcs);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pcsCustomizer = new wts.roms.gis.PCS_Customizer();
        jPanel1 = new javax.swing.JPanel();
        jbCancel = new javax.swing.JButton();
        jbAccept = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pcsCustomizer.setName("pcsCustomizer"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setName("jPanel1"); // NOI18N

        jbCancel.setText("Cancel");
        jbCancel.setName("jbCancel"); // NOI18N
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });

        jbAccept.setText("Accept");
        jbAccept.setName("jbAccept"); // NOI18N
        jbAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAcceptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(247, Short.MAX_VALUE)
                .addComponent(jbAccept)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCancel))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jbCancel)
                .addComponent(jbAccept))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pcsCustomizer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pcsCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        result = CANCEL_CHANGES;
        setVisible(false);
        dispose();
        this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_jbCancelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        System.out.println("Form window closing!");
        setVisible(false);
        dispose();
    }//GEN-LAST:event_formWindowClosing

    private void jbAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAcceptActionPerformed
        result = ACCEPT_CHANGES;
        setVisible(false);
        dispose();
        this.getToolkit().getSystemEventQueue().postEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_jbAcceptActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PCS_Editor dialog = new PCS_Editor(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.out.println("windowClosing in main");
                        System.exit(0);
                    }
                });
                int res = dialog.showDialog();
                System.out.println("Finsihed with dialog.");
                System.out.println("result = "+res);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbAccept;
    private javax.swing.JButton jbCancel;
    private wts.roms.gis.PCS_Customizer pcsCustomizer;
    // End of variables declaration//GEN-END:variables

}
