/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.OceanTimesIdentifier;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import wts.models.utilities.CalendarIF;
import wts.models.utilities.CalendarJulian;
import wts.models.utilities.DateTime;
import wts.roms.model.Array;
import wts.roms.model.GlobalInfo;
import wts.roms.model.NetcdfReader;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
    dtd = "-//wts.roms.topcomponents.OceanTimesIdentifier//OceanTimesIdentifier//EN",
autostore = false)
@TopComponent.Description(
    preferredID = "OceanTimesIdentifierTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "wts.roms.topcomponents.OceanTimesIdentifier.OceanTimesIdentifierTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_OceanTimesIdentifierAction",
preferredID = "OceanTimesIdentifierTopComponent")
@Messages({
    "CTL_OceanTimesIdentifierAction=Ocean Times Identifier",
    "CTL_OceanTimesIdentifierTopComponent=Ocean Times Identifier",
    "HINT_OceanTimesIdentifierTopComponent=This is an Ocean Times Identifier window"
})
public final class OceanTimesIdentifierTopComponent extends TopComponent {
    
    JFileChooser jFC;
    NetcdfReader netcdfReader = null;
    Array oceanTime = null;
    CalendarIF julCal;
    
    public OceanTimesIdentifierTopComponent() {
        initComponents();
        setName(Bundle.CTL_OceanTimesIdentifierTopComponent());
        setToolTipText(Bundle.HINT_OceanTimesIdentifierTopComponent());
        jFC = new JFileChooser();
        julCal = new CalendarJulian();
        dtCustomizer.setEditable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(OceanTimesIdentifierTopComponent.class, "OceanTimesIdentifierTopComponent.jPanel1.border.title"))); // NOI18N
        jPanel1.setLayout(new java.awt.BorderLayout());

        jcbDataset.setEditable(true);
        jPanel1.add(jcbDataset, java.awt.BorderLayout.CENTER);

        org.openide.awt.Mnemonics.setLocalizedText(jbDataset, org.openide.util.NbBundle.getMessage(OceanTimesIdentifierTopComponent.class, "OceanTimesIdentifierTopComponent.jbDataset.text")); // NOI18N
        jbDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDatasetselectDataset(evt);
            }
        });
        jPanel1.add(jbDataset, java.awt.BorderLayout.EAST);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(OceanTimesIdentifierTopComponent.class, "OceanTimesIdentifierTopComponent.jPanel3.border.title"))); // NOI18N

        jspTime.setToolTipText(org.openide.util.NbBundle.getMessage(OceanTimesIdentifierTopComponent.class, "OceanTimesIdentifierTopComponent.jspTime.toolTipText")); // NOI18N
        jspTime.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspTimeStateChanged(evt);
            }
        });

        jtfTime.setEditable(false);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(OceanTimesIdentifierTopComponent.class, "OceanTimesIdentifierTopComponent.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(OceanTimesIdentifierTopComponent.class, "OceanTimesIdentifierTopComponent.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(OceanTimesIdentifierTopComponent.class, "OceanTimesIdentifierTopComponent.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jspTime, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jtfTime, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dtCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jspTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jtfTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dtCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 129, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbDatasetselectDataset(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDatasetselectDataset
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
    }//GEN-LAST:event_jbDatasetselectDataset

    private void jspTimeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspTimeStateChanged
        if (oceanTime!=null){
            int i = ((Integer) jspTime.getValue()).intValue()-1;
            long t = (long) oceanTime.getDouble(oceanTime.getIndex().set0(i));
            jtfTime.setText(""+t);
            julCal.setTimeOffset(t);
            setDateTime();
        }
    }//GEN-LAST:event_jspTimeStateChanged
    
    private void setOceanTime() {
        if (netcdfReader!=null){
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbDataset;
    private javax.swing.JComboBox jcbDataset;
    private javax.swing.JSpinner jspTime;
    private javax.swing.JTextField jtfTime;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        p.setProperty("current.directory",jFC.getCurrentDirectory().getPath());
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        String currdir = p.getProperty("current.directory");
        if (currdir==null) {
            jFC.setCurrentDirectory(new java.io.File(GlobalInfo.PROP_CanonicalFile));
        } else {
            jFC.setCurrentDirectory(new File(currdir));
        }
    }
}
