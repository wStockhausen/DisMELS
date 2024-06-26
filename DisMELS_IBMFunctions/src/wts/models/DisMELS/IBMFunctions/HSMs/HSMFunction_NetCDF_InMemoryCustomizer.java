/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.HSMs;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.IBMParameterString;

/**
 *
 * @author william.stockhausen
 */
public class HSMFunction_NetCDF_InMemoryCustomizer extends javax.swing.JPanel implements java.beans.Customizer {

    private HSMFunction_NetCDF_InMemory obj = null;
    private JFileChooser jfc = new JFileChooser();
    
    /**
     * Creates new form HSMFunction_NetCDF_InMemoryCustomizer
     */
    public HSMFunction_NetCDF_InMemoryCustomizer() {
        initComponents();
        FileFilter ff = new FileNameExtensionFilter("netCDF files", "nc");
        jfc.setFileFilter(ff);
        jfc.setDialogTitle("path to a Habitat Suitability Map in netCDF format");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jtfFilename = new javax.swing.JTextField();
        jbFindFile = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(HSMFunction_NetCDF_InMemoryCustomizer.class, "HSMFunction_NetCDF_InMemoryCustomizer.jPanel1.border.title"))); // NOI18N

        jtfFilename.setText(org.openide.util.NbBundle.getMessage(HSMFunction_NetCDF_InMemoryCustomizer.class, "HSMFunction_NetCDF_InMemoryCustomizer.jtfFilename.text")); // NOI18N
        jtfFilename.setToolTipText(org.openide.util.NbBundle.getMessage(HSMFunction_NetCDF_InMemoryCustomizer.class, "HSMFunction_NetCDF_InMemoryCustomizer.jtfFilename.toolTipText")); // NOI18N
        jtfFilename.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfFilenameActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jbFindFile, org.openide.util.NbBundle.getMessage(HSMFunction_NetCDF_InMemoryCustomizer.class, "HSMFunction_NetCDF_InMemoryCustomizer.jbFindFile.text")); // NOI18N
        jbFindFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFindFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jtfFilename, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbFindFile))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jtfFilename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jbFindFile))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jtfFilenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfFilenameActionPerformed
       String fn = jtfFilename.getText();
       File f = new File(fn);
       if (f.exists()) {
           boolean r = obj.setParameterValue(HSMFunction_NetCDF_InMemory.PARAM_fileName, fn);
           if (r) {
               jfc.setSelectedFile(f);
           } else {
               revertFile(fn);
           }
       } else {
           revertFile(fn);
       }
    }//GEN-LAST:event_jtfFilenameActionPerformed

    private void revertFile(String fn){
        JOptionPane.showMessageDialog(jPanel1, "Problem with file '"+fn+"'.","Customizer: File doesn't exist or is not an HSM file.",JOptionPane.ERROR_MESSAGE);
        String fnp = jfc.getSelectedFile().getPath();
        File f = new File(fnp);
        if (f.exists()) {
           boolean r = obj.setParameterValue(HSMFunction_NetCDF_InMemory.PARAM_fileName, fnp);
           if (r) {//revert to previous file
            jtfFilename.setEnabled(false);//turn off event processing
            jtfFilename.setText(fnp);
            jtfFilename.setEnabled(true);//turn on event processing
           }
        } else {
            jtfFilename.setEnabled(false);//turn off event processing
            jtfFilename.setText("");
            jtfFilename.setEnabled(true);//turn on event processing
        }
    }
    
    private void jbFindFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFindFileActionPerformed
        if (jfc.showOpenDialog(jPanel1)==JFileChooser.APPROVE_OPTION){
           String fn = jfc.getSelectedFile().getPath();
           boolean r = obj.setParameterValue(HSMFunction_NetCDF_InMemory.PARAM_fileName, fn);
           if (r) {
                jtfFilename.setEnabled(false);//turn off event processing
                jtfFilename.setText(fn);
                jtfFilename.setEnabled(true);//turn on event processing
           } else {
               revertFile(fn);
           }
        }
    }//GEN-LAST:event_jbFindFileActionPerformed

    @Override
    public void setObject(Object bean) {
        if (bean instanceof HSMFunction_NetCDF_InMemory){
            obj = (HSMFunction_NetCDF_InMemory) bean;
            setParameters();
        }
    }

    private void setParameters() {
        String fn = ((IBMParameterString)obj.getParameter(HSMFunction_NetCDF_InMemory.PARAM_fileName)).getValueAsString();
        jtfFilename.setText(fn);
        File f = new File(fn);
        if (f.exists()){
            jfc.setSelectedFile(f);
        } else {
            File d = new File(GlobalInfo.getInstance().getWorkingDir());
            if (d.exists()) jfc.setCurrentDirectory(d);
        }
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbFindFile;
    private javax.swing.JTextField jtfFilename;
    // End of variables declaration//GEN-END:variables
}
