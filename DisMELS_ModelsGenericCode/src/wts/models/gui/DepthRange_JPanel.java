/*
 * DepthRange_JPanel.java
 *
 * Created on January 6, 2006, 10:30 AM
 */

package wts.models.gui;

import javax.swing.border.TitledBorder;
/**
 * @author William Stockhausen
 */
public class DepthRange_JPanel extends javax.swing.JPanel {
    //Static fields
    public static final String PROP_hasPreferredRange = "hasPrefferedRange";
    public static final String PROP_minDepth = "minDepth";
    public static final String PROP_maxDepth = "maxDepth";
    public static final String PROP_willAttach = "willAttach";

    //new Bean fields
    private String title = "";
    private boolean hasPreferredRange = false;
    private double minDepth = 0.0;
    private double maxDepth = 0.0;
    private boolean willAttach = false;

    /**
     * Utility field used by bound properties.
     */
    private java.beans.PropertyChangeSupport propertyChangeSupport =  
            new java.beans.PropertyChangeSupport(this);
    
    /**
     * Creates new customizer DepthRange_JPanel
     */
    public DepthRange_JPanel() {
        initComponents();
        setTitle(title);
        setHasPreferredRange(hasPreferredRange);
        setMinDepth(minDepth);
        setMaxDepth(maxDepth);
        setWillAttach(willAttach);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpTitle = new javax.swing.JPanel();
        jcbHasPreferredRange = new javax.swing.JCheckBox();
        jpRange = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtfMaxDepth = new javax.swing.JTextField();
        jtfMinDepth = new javax.swing.JTextField();
        jcbWillAttach = new javax.swing.JCheckBox();

        setLayout(new java.awt.BorderLayout());

        jpTitle.setBorder(javax.swing.BorderFactory.createTitledBorder("Daytime"));

        jcbHasPreferredRange.setText("Has a preferred depth range");
        jcbHasPreferredRange.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jcbHasPreferredRange.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jcbHasPreferredRange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbHasPreferredRangeActionPerformed(evt);
            }
        });

        jLabel1.setText("Min depth (m; >0)");

        jLabel2.setText("Max depth (m; >0)");

        jtfMaxDepth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMaxDepthActionPerformed(evt);
            }
        });

        jtfMinDepth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMinDepthActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jpRangeLayout = new org.jdesktop.layout.GroupLayout(jpRange);
        jpRange.setLayout(jpRangeLayout);
        jpRangeLayout.setHorizontalGroup(
            jpRangeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpRangeLayout.createSequentialGroup()
                .add(jpRangeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jtfMaxDepth)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jtfMinDepth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpRangeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jLabel1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpRangeLayout.setVerticalGroup(
            jpRangeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpRangeLayout.createSequentialGroup()
                .add(jpRangeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jtfMinDepth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpRangeLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jtfMaxDepth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );

        jcbWillAttach.setText("Will attach to bottom");
        jcbWillAttach.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbWillAttachActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jpTitleLayout = new org.jdesktop.layout.GroupLayout(jpTitle);
        jpTitle.setLayout(jpTitleLayout);
        jpTitleLayout.setHorizontalGroup(
            jpTitleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpTitleLayout.createSequentialGroup()
                .addContainerGap()
                .add(jpTitleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jcbWillAttach)
                    .add(jcbHasPreferredRange, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 183, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jpRange, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(106, 106, 106))
        );
        jpTitleLayout.setVerticalGroup(
            jpTitleLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpTitleLayout.createSequentialGroup()
                .add(5, 5, 5)
                .add(jcbHasPreferredRange)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jpRange, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jcbWillAttach)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jpTitle, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jtfMaxDepthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMaxDepthActionPerformed
        setMaxDepth(Double.parseDouble(jtfMaxDepth.getText()));
    }//GEN-LAST:event_jtfMaxDepthActionPerformed

    private void jtfMinDepthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMinDepthActionPerformed
        setMinDepth(Double.parseDouble(jtfMinDepth.getText()));
    }//GEN-LAST:event_jtfMinDepthActionPerformed

    private void jcbHasPreferredRangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbHasPreferredRangeActionPerformed
        setHasPreferredRange(jcbHasPreferredRange.isSelected());
    }//GEN-LAST:event_jcbHasPreferredRangeActionPerformed

    private void jcbWillAttachActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbWillAttachActionPerformed
        setWillAttach(jcbWillAttach.isSelected());
    }//GEN-LAST:event_jcbWillAttachActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JCheckBox jcbHasPreferredRange;
    private javax.swing.JCheckBox jcbWillAttach;
    private javax.swing.JPanel jpRange;
    private javax.swing.JPanel jpTitle;
    private javax.swing.JTextField jtfMaxDepth;
    private javax.swing.JTextField jtfMinDepth;
    // End of variables declaration//GEN-END:variables

    /**
     * Getter for property title.
     * @return Value of property title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Setter for property title.
     * @param title New value of property title.
     */
    public void setTitle(String title) {
        this.title = title;
        TitledBorder tb = (TitledBorder) jpTitle.getBorder();
        tb.setTitle(title);
    }

    /**
     * Adds a PropertyChangeListener to the listener list.
     * @param l The listener to add.
     */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /**
     * Removes a PropertyChangeListener from the listener list.
     * @param l The listener to remove.
     */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    /**
     * Getter for property hasPreferredRange.
     * @return Value of property hasPreferredRange.
     */
    public boolean getHasPreferredRange() {
        return this.hasPreferredRange;
    }

    /**
     * Setter for property hasPreferredRange.
     * @param hasPreferredRange New value of property hasPreferredRange.
     */
    public void setHasPreferredRange(boolean newVal) {
        boolean oldVal = hasPreferredRange;
        hasPreferredRange = newVal;
        jcbHasPreferredRange.setSelected(hasPreferredRange);
        jtfMinDepth.setEnabled(hasPreferredRange);
        jtfMaxDepth.setEnabled(hasPreferredRange);
        propertyChangeSupport.firePropertyChange (PROP_hasPreferredRange, oldVal, newVal);
    }

    /**
     * Getter for property newVal.
     * @return Value of property newVal.
     */
    public double getMinDepth() {
        return this.minDepth;
    }

    /**
     * Setter for property minDepth.
     * @param newVal New value of property minDepth.
     */
    public void setMinDepth(double newVal) {
        double oldVal = this.minDepth;
        this.minDepth = newVal;
        jtfMinDepth.setText(String.valueOf(newVal));
        propertyChangeSupport.firePropertyChange (PROP_minDepth, oldVal, newVal);
    }

    /**
     * Getter for property newVal.
     * @return Value of property newVal.
     */
    public double getMaxDepth() {
        return this.maxDepth;
    }

    /**
     * Setter for property maxDepth.
     * @param newVal New value of property maxDepth.
     */
    public void setMaxDepth(double newVal) {
        double oldVal = this.maxDepth;
        this.maxDepth = newVal;
        jtfMaxDepth.setText(String.valueOf(newVal));
        propertyChangeSupport.firePropertyChange (PROP_maxDepth, oldVal, newVal);
    }

    /**
     * Setter for property willAttach.
     * @param newVal New value of property willAttach.
     */
    public void setWillAttach(boolean newVal) {
        boolean oldVal = willAttach;
        willAttach = newVal;
        jcbWillAttach.setSelected(willAttach);
        propertyChangeSupport.firePropertyChange (PROP_willAttach, oldVal, newVal);
    }
    
}
