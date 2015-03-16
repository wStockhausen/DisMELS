/*
 * Viewer_OceanTime.java
 *
 * Created on December 6, 2005, 11:35 AM
 */

package wts.roms.gui;

/**
 *
 * @author  William Stockhausen
 */
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import wts.models.utilities.CalendarIF;
import wts.models.utilities.CalendarJulian;
import wts.models.utilities.DateTime;
import wts.roms.model.Array;
import wts.roms.model.NetcdfReader;

@SuppressWarnings("static-access")
public class Viewer_OceanTime extends javax.swing.JFrame {
    
    JFileChooser jFC;
    NetcdfReader netcdfReader;
    Array oceanTime;
    CalendarIF julCal;
    DateTimeEditorDialog refDateEditor = null;
    
    /** Creates new form ModelDataViewer */
    public Viewer_OceanTime() {
        initComponents();
        jFC = new JFileChooser();
        jFC.setCurrentDirectory(new java.io.File("D:/MyDocuments/Projects/ROMS/ModelData"));
        julCal = new CalendarJulian();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jcbDataset = new javax.swing.JComboBox();
        jbDataset = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jspTime = new javax.swing.JSpinner();
        jtfTime = new javax.swing.JTextField();
        dtCustomizer = new wts.models.utilities.DateTimeCustomizer();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jmiExit = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jmiRefDate = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ROMS Ocean Time Viewer");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Model dataset"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jcbDataset.setEditable(true);
        jPanel1.add(jcbDataset, java.awt.BorderLayout.CENTER);

        jbDataset.setText("Select");
        jbDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDataset(evt);
            }
        });
        jPanel1.add(jbDataset, java.awt.BorderLayout.EAST);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ocean time"));

        jspTime.setToolTipText("index into netCDF dataset");
        jspTime.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspTimeStateChanged(evt);
            }
        });

        jLabel1.setText("Index");

        jLabel2.setText("ROMS time (sec)");

        jLabel3.setText("Date/time");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jspTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                    .add(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel1)
                        .add(27, 27, 27)))
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jtfTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 150, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel3)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, dtCustomizer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jLabel3)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jspTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jtfTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(dtCustomizer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jMenu2.setText("File");

        jmiExit.setText("Exit");
        jmiExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiExitActionPerformed(evt);
            }
        });
        jMenu2.add(jmiExit);

        jMenuBar1.add(jMenu2);

        jMenu1.setText("Tools");

        jmiRefDate.setText("Change ROMS reference date");
        jmiRefDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRefDateActionPerformed(evt);
            }
        });
        jMenu1.add(jmiRefDate);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jspTimeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspTimeStateChanged
// TODO add your handling code here:
        int i = ((Integer) jspTime.getValue()).intValue()-1;
        long t = (long) oceanTime.getDouble(oceanTime.getIndex().set0(i));
        jtfTime.setText(""+t);
        julCal.setTimeOffset(t);
        setDateTime();
    }//GEN-LAST:event_jspTimeStateChanged

    private void selectDataset(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDataset
// TODO add your handling code here:
        int res = jFC.showOpenDialog(this);
        if (res==JFileChooser.APPROVE_OPTION) {
            String dsn = jFC.getSelectedFile().getPath();
            jcbDataset.addItem(dsn);
            jcbDataset.setSelectedItem(dsn);
            try {
                netcdfReader = new NetcdfReader(dsn);
                julCal = netcdfReader.getCalendar();
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(null,"Not a netCDF file",
                                         "File selection",
                                         JOptionPane.ERROR_MESSAGE);
                return;
            }
            setOceanTime();
        }
    }//GEN-LAST:event_selectDataset


    private void jmiRefDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRefDateActionPerformed
        if (this.refDateEditor==null) {
            DateTime refDate = this.julCal.getRefDate();
            refDateEditor = new DateTimeEditorDialog(this,refDate,true);
        }
        int res = refDateEditor.showDialog();
        if (res==refDateEditor.ACCEPT_CHANGES) {
            DateTime refDate = refDateEditor.getObject();
            this.julCal.setRefDate(refDate);
            setDateTime();
        }
    }//GEN-LAST:event_jmiRefDateActionPerformed

    private void jmiExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiExitActionPerformed
        this.setVisible(false);
        dispose();
    }//GEN-LAST:event_jmiExitActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Viewer_OceanTime().setVisible(true);
            }
        });
    }
    
    private void setOceanTime() {
        oceanTime = netcdfReader.getOceanTime();
        int mx = oceanTime.getShape()[0];
        SpinnerNumberModel snm = new SpinnerNumberModel(new Integer(1),
                                                        new Integer(1),
                                                        new Integer(mx),
                                                        new Integer(1));
        jspTime.setModel(snm);
        long t = (long)oceanTime.getDouble(oceanTime.getIndex().set0(0));
        jtfTime.setText(""+t);
        julCal.setTimeOffset(t);
        setDateTime();
    }

    private void setDateTime() {
        DateTime td = julCal.getDate();
        dtCustomizer.setObject(td);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private wts.models.utilities.DateTimeCustomizer dtCustomizer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton jbDataset;
    private javax.swing.JComboBox jcbDataset;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiRefDate;
    private javax.swing.JSpinner jspTime;
    private javax.swing.JTextField jtfTime;
    // End of variables declaration//GEN-END:variables
    
}
