/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.LHSTypesEditor;

import java.beans.ExceptionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.logging.Logger;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Savable;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import wts.models.DisMELS.actions.Resetable;
import wts.models.DisMELS.framework.*;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//wts.models.DisMELS.topcomponents//LHSTypesEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = "LHSTypesEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "wts.models.DisMELS.topcomponents.LHSTypesEditorTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_LHSTypesEditorAction",
preferredID = "LHSTypesEditorTopComponent")
@Messages({
    "CTL_LHSTypesEditorAction=LHSTypesEditor",
    "CTL_LHSTypesEditorTopComponent=LHS Types Editor",
    "HINT_LHSTypesEditorTopComponent=The LHS Types Editor"
})
public final class LHSTypesEditorTopComponent extends TopComponent implements PropertyChangeListener {
    
    /** singleton object */
    private static LHSTypesEditorTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "LHSTypesEditorTopComponent";
    
    /** GlobalInfo singleton */
    private GlobalInfo globalInfo = null;
    
    /** the LHS_Types object holding the lhs types definitions */
    private LHS_Types types;
    
    /** instance content reflecting FileLoader, FileSaver, and Resetter capabilities */
    private InstanceContent content;
    /** instance of the private class for saving the life stage parameters to csv or xml */
    private FileSaver fileSaver;
    /** instance of the private class for loading the life stage parameters from csv or xml */
    private FileLoader fileLoader;
    /** instance of the private class for reseting all parameter values */
    private Resetter resetter;

    private static final Logger logger = Logger.getLogger(LHSTypesEditorTopComponent.class.getName());
    
    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized LHSTypesEditorTopComponent getDefault(){
        if (instance==null){
            instance = new LHSTypesEditorTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized LHSTypesEditorTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof LHSTypesEditorTopComponent) {
            return (LHSTypesEditorTopComponent) win;
        }
        logger.warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }

    public LHSTypesEditorTopComponent() {
        initComponents();
        setName(Bundle.CTL_LHSTypesEditorTopComponent());
        setToolTipText(Bundle.HINT_LHSTypesEditorTopComponent());
        initComponents1();
        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect csvSaver, csvLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
        fileLoader = new FileLoader();
        content.add(fileLoader);
        fileSaver = new FileSaver();
        content.add(fileSaver);
        resetter = new Resetter();
        content.add(resetter);
    }
    
    private void initComponents1(){
        globalInfo = GlobalInfo.getInstance();
        globalInfo.addPropertyChangeListener(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jpCustomizer = new javax.swing.JPanel();
        typesCustomizer = new wts.models.DisMELS.gui.LHS_TypesCustomizer();
        jPanel2 = new javax.swing.JPanel();
        jbSave = new javax.swing.JButton();
        jbRestore = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jpCustomizer.setLayout(new java.awt.BorderLayout());
        jpCustomizer.add(typesCustomizer, java.awt.BorderLayout.CENTER);

        jPanel1.add(jpCustomizer, java.awt.BorderLayout.CENTER);

        add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        org.openide.awt.Mnemonics.setLocalizedText(jbSave, org.openide.util.NbBundle.getMessage(LHSTypesEditorTopComponent.class, "LHSTypesEditorTopComponent.jbSave.text")); // NOI18N
        jbSave.setToolTipText(org.openide.util.NbBundle.getMessage(LHSTypesEditorTopComponent.class, "LHSTypesEditorTopComponent.jbSave.toolTipText")); // NOI18N
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });
        jPanel2.add(jbSave);

        org.openide.awt.Mnemonics.setLocalizedText(jbRestore, org.openide.util.NbBundle.getMessage(LHSTypesEditorTopComponent.class, "LHSTypesEditorTopComponent.jbRestore.text")); // NOI18N
        jbRestore.setToolTipText(org.openide.util.NbBundle.getMessage(LHSTypesEditorTopComponent.class, "LHSTypesEditorTopComponent.jbRestore.toolTipText")); // NOI18N
        jbRestore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRestoreActionPerformed(evt);
            }
        });
        jPanel2.add(jbRestore);

        add(jPanel2, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
        saveValues();
    }//GEN-LAST:event_jbSaveActionPerformed

    private void jbRestoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRestoreActionPerformed
        restoreValues();
    }//GEN-LAST:event_jbRestoreActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbRestore;
    private javax.swing.JButton jbSave;
    private javax.swing.JPanel jpCustomizer;
    private wts.models.DisMELS.gui.LHS_TypesCustomizer typesCustomizer;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        logger.info("----------LHSTypesEditor.componentOpened(): start");
        types = LHS_Types.getInstance();
        types.addPropertyChangeListener(this);
        setLHS_Types();
        logger.info("----------LHSTypesEditor.componentOpened(): end");
    }

    @Override
    public void componentClosed() {
    }

    private void setLHS_Types(){
        if (types.getKeys().isEmpty()) {
            LHS_Type type = new LHS_Type();
            type.setLHSName("<no types defined>");
            types.addType(type);
        }
        typesCustomizer.setObject(types);
        validate();
        repaint();
        
    }
    
    private void restoreValues() {
        logger.info("Restoring values");
        types.readXML();    //read current version of xml file
//        LHS_Factory.reset();//reset LHS_Factory maps        
//        if (types.getKeys().isEmpty()) {
//            LHS_Type type = new LHS_Type();
//            type.setLHSName("<no types defined>");
//            types.addType(type);
//        }
//        typesCustomizer.setObject(types);
//        validate();
//        repaint();
        setLHS_Types();
    }
    
    private void saveValues() {
        types.writeXML();
//        LHS_Factory.reset();//TODO: why reset?
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

    /**
     * This method reacts to a property change event signaling a change in 
     * the working directory.  It resets the types customizer based on the 'new'
     * LHS_Types object.
     * 
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GlobalInfo.PROP_WorkingDirFN)){
            //At this point, 
            // 1. LHS_Types instance has read the LHS_Types.xml file in the new dir and
            // 2. LHS_Factory.reset() has been called
            //so all that needs to be done here is refresh the customizer by setting types again
            logger.info("propertyChange(): detected GlobalInfo.PROP_WorkingDirFN");
            typesCustomizer.setObject(types);
            validate();
            repaint();
        } else 
        if (evt.getPropertyName().equals(LHS_Types.PROP_types)){
            logger.info("propertyChange(): detected LHS_Types.PROP_types");
            setLHS_Types();
        }
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement open functionality for LHS_Types.
     */
    private class FileLoader implements Openable {

        @Override
        public void open() {
            restoreValues();
        }
    }

    //------------------------------------------------------------------------//
    /**
     * Private class to implement save functionality for LHS_Types.
     */
    private class FileSaver implements Savable, ExceptionListener {

        @Override
        public void save() throws IOException {
            saveValues();
        }

        @Override
        public void exceptionThrown(Exception e) {
            Exceptions.printStackTrace(e);
        }
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement a "reset" capability for LHS_Types
     */
    private class Resetter implements Resetable {

        @Override
        public void reset() {
            types.reset();
        }        
    }
}
