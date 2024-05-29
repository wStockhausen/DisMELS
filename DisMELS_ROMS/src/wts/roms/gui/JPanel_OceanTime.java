/*
 * JPanel_OceanTime.java
 *
 * Created on June 12, 2006, 3:43 PM
 */

package wts.roms.gui;

import wts.models.utilities.CalendarJulian;
import wts.models.utilities.DateTime;

/**
 *
 * @author  William Stockhausen
 */
public class JPanel_OceanTime extends javax.swing.JPanel {
    
    private CalendarJulian julCal;
    
    /** Creates new form JPanel_OceanTime */
    public JPanel_OceanTime() {
        initComponents();
        julCal = new CalendarJulian();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jpDayNight = new javax.swing.JPanel();
        lblDateTime = new javax.swing.JLabel();

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        setMaximumSize(new java.awt.Dimension(2147483647, 20));
        jpDayNight.setBackground(new java.awt.Color(0, 0, 0));
        jpDayNight.setMaximumSize(new java.awt.Dimension(20, 20));
        jpDayNight.setMinimumSize(new java.awt.Dimension(20, 20));
        jpDayNight.setPreferredSize(new java.awt.Dimension(20, 20));
        org.jdesktop.layout.GroupLayout jpDayNightLayout = new org.jdesktop.layout.GroupLayout(jpDayNight);
        jpDayNight.setLayout(jpDayNightLayout);
        jpDayNightLayout.setHorizontalGroup(
            jpDayNightLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );
        jpDayNightLayout.setVerticalGroup(
            jpDayNightLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 20, Short.MAX_VALUE)
        );
        add(jpDayNight);

        lblDateTime.setText("0000-00-00 00:00:00 GMT");
        add(lblDateTime);

    }// </editor-fold>//GEN-END:initComponents
    
    public void setOceanTime(long time) {
        julCal.setTimeOffset(time);
        setText();
    }
    
    public void setOceanTime(double time) {
        setOceanTime((long) time);
    }
    
    public DateTime getDateTime() {
        return julCal.getDate();
    }
    
    public void setDateTime(DateTime dt) {
        julCal.setDate(dt);
        setText();
    }
    
    private void setText() {
        String dts = julCal.getDateTimeString();
        lblDateTime.setText("  "+dts);
    }
    
    public DateTime getRefDate() {
        return julCal.getRefDate();
    }
    
    public void setRefDate(DateTime dt) {
        julCal.setRefDate(dt);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jpDayNight;
    private javax.swing.JLabel lblDateTime;
    // End of variables declaration//GEN-END:variables

    
}
