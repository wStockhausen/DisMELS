/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.ModelTime;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.utilities.CalendarIF;
import wts.models.utilities.ModelCalendar;
import wts.roms.model.NetcdfReader;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//wts.models.DisMELS.topcomponents.ModelTime//ModelTime//EN",
autostore = false)
@TopComponent.Description(preferredID = "ModelTimeTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "wts.models.DisMELS.topcomponents.ModelTime.ModelTimeTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ModelTimeAction",
preferredID = "ModelTimeTopComponent")
@Messages({
    "CTL_ModelTimeAction=Model time",
    "CTL_ModelTimeTopComponent=Model time",
    "HINT_ModelTimeTopComponent=Converts between oceantime (s) and a date"
})
public final class ModelTimeTopComponent extends TopComponent implements PropertyChangeListener {
    
    /** GlobalInfo singleton */
//    private GlobalInfo globalInfo = null;
    
    /** flag to do actions associated with changing values */
    private boolean doActions = true;
    

    public ModelTimeTopComponent() {
        initComponents();
        setName(Bundle.CTL_ModelTimeTopComponent());
        setToolTipText(Bundle.HINT_ModelTimeTopComponent());
//        initComponents1();
    }
    
//    private void initComponents1(){
//        globalInfo = GlobalInfo.getInstance();
//        if (ModelCalendar.getCalendar()==null){
//            try {
//                NetcdfReader netcdfReader = new NetcdfReader(globalInfo.getCanonicalFile());
//                CalendarIF cal = netcdfReader.getCalendar();
//                ModelCalendar.setCalendar(cal);
//            } catch (IOException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfROMSTime = new javax.swing.JTextField();
        jtfDate = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ModelTimeTopComponent.class, "ModelTimeTopComponent.jPanel3.border.title"))); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(418, 82));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ModelTimeTopComponent.class, "ModelTimeTopComponent.jLabel1.text")); // NOI18N

        jtfROMSTime.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfROMSTime.setText(org.openide.util.NbBundle.getMessage(ModelTimeTopComponent.class, "ModelTimeTopComponent.jtfROMSTime.text")); // NOI18N
        jtfROMSTime.setToolTipText(org.openide.util.NbBundle.getMessage(ModelTimeTopComponent.class, "ModelTimeTopComponent.jtfROMSTime.toolTipText")); // NOI18N
        jtfROMSTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfROMSTimeActionPerformed(evt);
            }
        });

        jtfDate.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfDate.setToolTipText(org.openide.util.NbBundle.getMessage(ModelTimeTopComponent.class, "ModelTimeTopComponent.jtfDate.toolTipText")); // NOI18N
        jtfDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfDateActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(ModelTimeTopComponent.class, "ModelTimeTopComponent.jLabel9.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jtfDate, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfROMSTime, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jtfROMSTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jPanel3);

        add(jScrollPane1, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jtfROMSTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfROMSTimeActionPerformed
        //Set model start time
        try {
            if (doActions) {
                System.out.println("Setting start time " + jtfROMSTime.getText());
                double n = Double.parseDouble(jtfROMSTime.getText());
                wts.roms.model.GlobalInfo giROMS = wts.roms.model.GlobalInfo.getInstance();
                giROMS.getCalendar().setTimeOffset((long) n);
                doActions = false;
                jtfDate.setText(giROMS.getCalendar().getDateTimeString());
                doActions = true;
            }
        } catch (NumberFormatException ex) {
            NotifyDescriptor nd = new NotifyDescriptor.Message("Invalid start time (not a number)!");
            DialogDisplayer.getDefault().notify(nd);
            doActions = true;
        }
    }//GEN-LAST:event_jtfROMSTimeActionPerformed

    private void jtfDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfDateActionPerformed
        try {
            if (doActions) {
                doActions = false;
                System.out.println("Setting start date " + jtfDate.getText());
                String[] strp = jtfDate.getText().split(" ");//split date/time
                String[] strd = strp[0].split("-");//split date part
                String[] strt = strp[1].split(":");//split time part
                int yr = Integer.parseInt(strd[0]);
                int mo = Integer.parseInt(strd[1]);
                int dy = Integer.parseInt(strd[2]);
                int hr = Integer.parseInt(strt[0]);
                int mi = Integer.parseInt(strt[1]);
                int sc = Integer.parseInt(strt[2]);
                ModelCalendar.getCalendar().setDate(yr, mo, dy, hr, mi, sc);
                double v = ModelCalendar.getCalendar().getTimeOffset();
                jtfROMSTime.setText(Long.toString((long) v));
                doActions = true;
            }
        } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
            NotifyDescriptor nd = new NotifyDescriptor.Message("Invalid date format!");
            DialogDisplayer.getDefault().notify(nd);
            doActions = true;
        }
    }//GEN-LAST:event_jtfDateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jtfDate;
    private javax.swing.JTextField jtfROMSTime;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
