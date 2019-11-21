/*
 * DateTimeCustomizer.java
 *
 * Created on December 9, 2008
 */

package wts.models.utilities;

/**
 *
 * @author  William Stockhausen
 */
public class DateTimeCustomizer extends javax.swing.JPanel 
                                implements java.beans.Customizer {
    //Static fields
    public static final String PROP_editable = "editable";
    public static final String PROP_enabled  = "enabled";
    
    private DateTime dt;
    
    private boolean editable = true;
    private boolean enabled  = true;
    
    /** Creates new form DateTimeCustomizer */
    public DateTimeCustomizer() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtfYear = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jtfMonth = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfDay = new javax.swing.JTextField();
        jtfHour = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jtfMin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfSec = new javax.swing.JTextField();

        jtfYear.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfYear.setText("0000");
        jtfYear.setToolTipText("year");
        jtfYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfYearActionPerformed(evt);
            }
        });

        jLabel1.setText("/");
        jLabel1.setMaximumSize(new java.awt.Dimension(2, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(2, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(2, 14));
        jLabel1.setRequestFocusEnabled(false);

        jtfMonth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfMonth.setText("00");
        jtfMonth.setToolTipText("month (1-12)");
        jtfMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMonthActionPerformed(evt);
            }
        });

        jLabel2.setText("/");
        jLabel2.setMaximumSize(new java.awt.Dimension(2, 14));
        jLabel2.setMinimumSize(new java.awt.Dimension(2, 14));
        jLabel2.setPreferredSize(new java.awt.Dimension(2, 14));
        jLabel2.setRequestFocusEnabled(false);

        jtfDay.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfDay.setText("00");
        jtfDay.setToolTipText("day");
        jtfDay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDayActionPerformed(evt);
            }
        });

        jtfHour.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfHour.setText("00");
        jtfHour.setToolTipText("hour (1-23)");
        jtfHour.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfHourActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText(":");

        jtfMin.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfMin.setText("00");
        jtfMin.setToolTipText("minutes");
        jtfMin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfMinActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText(":");

        jtfSec.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfSec.setText("00");
        jtfSec.setToolTipText("seconds");
        jtfSec.setPreferredSize(new java.awt.Dimension(26, 22));
        jtfSec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfSecActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jtfYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jtfMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jtfDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(17, 17, 17)
                .add(jtfHour, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jtfMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jtfSec, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
            .add(jtfYear, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jtfMonth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jtfDay, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jtfHour, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jLabel3)
            .add(jtfMin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jLabel4)
            .add(jtfSec, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfYearActionPerformed
        dt.setYear(Integer.parseInt(jtfYear.getText()));
    }//GEN-LAST:event_jtfYearActionPerformed

    private void jtfMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMonthActionPerformed
        dt.setMonth(Integer.parseInt(jtfMonth.getText()));
    }//GEN-LAST:event_jtfMonthActionPerformed

    private void jtfDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDayActionPerformed
        dt.setDOM(Integer.parseInt(jtfDay.getText()));
    }//GEN-LAST:event_jtfDayActionPerformed

    private void jtfHourActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfHourActionPerformed
        dt.setHour(Integer.parseInt(jtfHour.getText()));
    }//GEN-LAST:event_jtfHourActionPerformed

    private void jtfMinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfMinActionPerformed
        dt.setMinute(Integer.parseInt(jtfMin.getText()));
    }//GEN-LAST:event_jtfMinActionPerformed

    private void jtfSecActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfSecActionPerformed
        dt.setSecond(Integer.parseInt(jtfSec.getText()));
    }//GEN-LAST:event_jtfSecActionPerformed
    
    public boolean getEditable() {
        return editable;
    }
    
    public void setEditable(boolean b) {
        boolean oldVal = editable;
        editable = b;
        jtfYear.setEditable(b);
        jtfMonth.setEditable(b);
        jtfDay.setEditable(b);
        jtfHour.setEditable(b);
        jtfMin.setEditable(b);
        jtfSec.setEditable(b);
        firePropertyChange(PROP_editable,oldVal,editable);
    }
    
    public boolean getEnabled() {
        return enabled;
    }
    
    @Override
    public void setEnabled(boolean b) {
        boolean oldVal = enabled;
        enabled = b;
        jtfYear.setEnabled(b);
        jtfMonth.setEnabled(b);
        jtfDay.setEnabled(b);
        jtfHour.setEnabled(b);
        jtfMin.setEnabled(b);
        jtfSec.setEnabled(b);
        firePropertyChange(PROP_enabled,oldVal,enabled);
    }
            
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField jtfDay;
    private javax.swing.JTextField jtfHour;
    private javax.swing.JTextField jtfMin;
    private javax.swing.JTextField jtfMonth;
    private javax.swing.JTextField jtfSec;
    private javax.swing.JTextField jtfYear;
    // End of variables declaration//GEN-END:variables

    public void setObject(Object bean) {
        if (bean instanceof DateTime) {
            dt = (DateTime) bean;
            jtfYear.setText(Integer.toString(dt.year));
            jtfMonth.setText(Integer.toString(dt.month));
            jtfDay.setText(Integer.toString(dt.dom));
            jtfHour.setText(Integer.toString(dt.hour));
            jtfMin.setText(Integer.toString(dt.min));
            jtfSec.setText(Integer.toString(dt.sec));
        }
    }
    
}
