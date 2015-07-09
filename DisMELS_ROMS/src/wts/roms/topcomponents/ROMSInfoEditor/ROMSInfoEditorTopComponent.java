/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.ROMSInfoEditor;

import com.wtstockhausen.utils.FileFilterImpl;
import com.wtstockhausen.utils.ReverseListModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Savable;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.WindowManager;
import wts.roms.actions.Resetable;
import wts.roms.gis.CSCreator;
import wts.roms.model.CriticalGrid2DVariablesInfo;
import wts.roms.model.CriticalModelVariablesInfo;
import wts.roms.model.GlobalInfo;
import wts.roms.model.OptionalModelVariablesInfo;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//wts.roms.topcomponents.ROMSInfoEditor//ROMSInfoEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = "ROMSInfoEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "wts.roms.topcomponents.ROMSInfoEditor.ROMSInfoEditorTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ROMSInfoEditorAction",
preferredID = "ROMSInfoEditorTopComponent")
@Messages({
    "CTL_ROMSInfoEditorAction=ROMS Info Editor",
    "CTL_ROMSInfoEditorTopComponent=ROMS Info Editor",
    "HINT_ROMSInfoEditorTopComponent=The ROMS Info Editor"
})
public final class ROMSInfoEditorTopComponent extends TopComponent implements PropertyChangeListener {
    
    /** singleton object */
    private static ROMSInfoEditorTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "ROMSInfoEditorTopComponent";
    /** logger for operating messages */
    private static final Logger logger = Logger.getLogger(ROMSInfoEditorTopComponent.class.getName());

    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized ROMSInfoEditorTopComponent getDefault(){
        if (instance==null){
            instance = new ROMSInfoEditorTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized ROMSInfoEditorTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof ROMSInfoEditorTopComponent) {
            return (ROMSInfoEditorTopComponent) win;
        }
        Logger.getLogger(ROMSInfoEditorTopComponent.class.getName()).warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }
    
    /** GlobalInfo singleton */
    private GlobalInfo globalInfo = null;
    
    /** ReverseListModel for recent grid files */
    private ReverseListModel grdLM = null;
    
    /** ReverseListModel for recent canonical datasets */
    private ReverseListModel canLM = null;
    
    /** flag indicating TopComponent is initializing and should not respond to events */
    private boolean initializing;
    
    /** JFileChooser for selecting grid and canonical datasets */
    private final JFileChooser jfcNC = new JFileChooser();   
    /** JFileChooser for selecting ROMS properties file */
    private final JFileChooser jfcGI = new JFileChooser();
    
    /** instance content reflecting csvLoader and csvSaver capabilities */
    private InstanceContent content;
    /** instance of the private class for loading the ROMS info properties file */
    private InfoSaver infoSaver;
    /** instance of the private class for loading the ROMS info properties file */
    private InfoLoader infoLoader;
    /** instance of the private class for reseting all ROMS info properties */
    private Resetter resetter;
    
    public ROMSInfoEditorTopComponent() {
        logger.info("++starting ROMSInfoEditorTopComponent()");
        initializing = true;
        globalInfo = GlobalInfo.getInstance();
        
        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect infoSaver, infoLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
        
        initComponents();
        initComponents1();
        
        setName(Bundle.CTL_ROMSInfoEditorTopComponent());
        setToolTipText(Bundle.HINT_ROMSInfoEditorTopComponent());
        
        logger.info("++finished ROMSInfoEditorTopComponent()");
    }

    /**
     * This method is called from the constructor to set up additional components.
     */
    private void initComponents1(){
        logger.info("++++Starting initComponents1()");
        //set up file choosers
        final FileFilter ffNC = new FileFilterImpl("nc","netCDF files");
        jfcNC.addChoosableFileFilter(ffNC);
        jfcNC.setFileFilter(ffNC);
        jfcNC.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (!globalInfo.getCanonicalFile().equals(GlobalInfo.PROP_NotSet)){
            jfcNC.setCurrentDirectory(new File(globalInfo.getCanonicalFile()));
        } else if (!globalInfo.getGridFile().equals(GlobalInfo.PROP_NotSet)){
            jfcNC.setCurrentDirectory(new File(globalInfo.getGridFile()));
        } else {
            jfcNC.setCurrentDirectory(new File(globalInfo.getWorkingDir()));
        }
        
        
        final FileFilter ffGI = new FileFilterImpl("properties","ROMS properties file");
        jfcGI.addChoosableFileFilter(ffGI);
        jfcGI.setFileFilter(ffGI);
        jfcGI.setFileSelectionMode(JFileChooser.FILES_ONLY);
        jfcGI.setDialogTitle("Open ROMS.roperties file");
        
        //set ROMS grid functionality
        grdLM = new ReverseListModel();
        grdLM.setSize(4);
        
        //set canonical ROMS file functionality
        canLM = new ReverseListModel();
        canLM.setSize(4);
        
        //Set map projections functionality
        jcbMapRegions.removeAllItems();
        jcbMapRegions.addItem(CSCreator.REGION_ALASKA);
        jcbMapRegions.addItem(CSCreator.REGION_ROK);

        infoSaver  = new InfoSaver();
        infoLoader = new InfoLoader();
        resetter = new Resetter();
        
        logger.info("++++Ending initComponents1().");        
    }
    @Override
    public void componentOpened() {
        logger.info("+++++starting componentOpened()");
        jtfROMSRefDate.setText(globalInfo.getRefDateString());
        jtfROMSRefDate.setToolTipText(globalInfo.getRefDateString());
        
        jcbGridFiles.setSelectedItem(globalInfo.getGridFile());        
        jcbCanonicalFiles.setSelectedItem(globalInfo.getCanonicalFile());
        
        jcbMapRegions.setSelectedItem(globalInfo.getMapRegion());
        
        //set critical grid variables information
        CriticalGrid2DVariablesInfo cvig = globalInfo.getCriticalGrid2DVariablesInfo();
        grid2DCVIsCustomizer.setObject(cvig);
        
        //set critical model variables information
        CriticalModelVariablesInfo cvim = globalInfo.getCriticalModelVariablesInfo();
        criticalMVsCustomizer.setObject(cvim);
        
        //set critical model variables information
        OptionalModelVariablesInfo ovim = globalInfo.getOptionalModelVariablesInfo();
        oviCustomizer.setObject(ovim);
        
        validate();
        
        globalInfo.addPropertyChangeListener(grid2DCVIsCustomizer);
        globalInfo.addPropertyChangeListener(criticalMVsCustomizer);
        globalInfo.addPropertyChangeListener(oviCustomizer);
        globalInfo.addPropertyChangeListener(this);
        
        initializing = false;//initialization stage is finished
        enableLoadAction(true);
        enableSaveAction(false);
        enableResetAction(true);
        logger.info("+++++finished componentOpened()");
    }

    @Override
    public void componentClosed() {
        logger.info("+++starting compnonentClosed()");
        enableLoadAction(false);
        enableSaveAction(false);
        enableResetAction(false);
        
        globalInfo.removePropertyChangeListener(grid2DCVIsCustomizer);
        globalInfo.removePropertyChangeListener(criticalMVsCustomizer);
        globalInfo.removePropertyChangeListener(oviCustomizer);
        globalInfo.removePropertyChangeListener(this);
        logger.info("+++finished compnonentClosed()");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jtfROMSRefDate = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jbSelGridFile = new javax.swing.JButton();
        jcbGridFiles = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        jbSelCanonicalFile = new javax.swing.JButton();
        jcbCanonicalFiles = new javax.swing.JComboBox();
        jPanel12 = new javax.swing.JPanel();
        jcbMapRegions = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        grid2DCVIsCustomizer = new wts.roms.model.CriticalGrid2DVariablesInfoCustomizer();
        jPanel4 = new javax.swing.JPanel();
        criticalMVsCustomizer = new wts.roms.model.CriticalModelVariablesInfoCustomizer();
        jPanel5 = new javax.swing.JPanel();
        oviCustomizer = new wts.roms.model.OptionalModelVariablesInfoCustomizer();

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jPanel13.border.title"))); // NOI18N

        jtfROMSRefDate.setText(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jtfROMSRefDate.text")); // NOI18N
        jtfROMSRefDate.setToolTipText(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jtfROMSRefDate.toolTipText")); // NOI18N
        jtfROMSRefDate.setMinimumSize(new java.awt.Dimension(400, 22));
        jtfROMSRefDate.setPreferredSize(new java.awt.Dimension(400, 22));
        jtfROMSRefDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jtfROMSRefDateMousePressed(evt);
            }
        });
        jtfROMSRefDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfROMSRefDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfROMSRefDate, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jtfROMSRefDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jPanel2.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jbSelGridFile, org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jbSelGridFile.text")); // NOI18N
        jbSelGridFile.setToolTipText(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jbSelGridFile.toolTipText")); // NOI18N
        jbSelGridFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSelGridFileActionPerformed(evt);
            }
        });

        jcbGridFiles.setEditable(true);
        jcbGridFiles.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbGridFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbGridFilesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbGridFiles, 0, 388, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbSelGridFile))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jbSelGridFile)
                .addComponent(jcbGridFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jPanel11.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jbSelCanonicalFile, org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jbSelCanonicalFile.text")); // NOI18N
        jbSelCanonicalFile.setToolTipText(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jbSelCanonicalFile.toolTipText")); // NOI18N
        jbSelCanonicalFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSelCanonicalFileActionPerformed(evt);
            }
        });

        jcbCanonicalFiles.setEditable(true);
        jcbCanonicalFiles.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbCanonicalFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbCanonicalFilesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbCanonicalFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbSelCanonicalFile))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jbSelCanonicalFile)
                .addComponent(jcbCanonicalFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jPanel12.border.title"))); // NOI18N

        jcbMapRegions.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Alaska", "Korea" }));
        jcbMapRegions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMapRegionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jcbMapRegions, 0, 455, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcbMapRegions, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(172, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(grid2DCVIsCustomizer, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(grid2DCVIsCustomizer, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(criticalMVsCustomizer, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(criticalMVsCustomizer, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jPanel5.setName(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jPanel5.name")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(oviCustomizer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(oviCustomizer, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(ROMSInfoEditorTopComponent.class, "ROMSInfoEditorTopComponent.jTabbedPane1.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void jtfROMSRefDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtfROMSRefDateActionPerformed
        if (!initializing){
            logger.info("++++starting jtfROMSRefDateActionPerformed()");
            String oldRef = globalInfo.getRefDateString();
            globalInfo.setRefDateString(jtfROMSRefDate.getText());
            jtfROMSRefDate.setToolTipText(globalInfo.getRefDateString());
            enableSaveAction(true);
            logger.info("++++finished ROMSInfoEditor.jtfROMSRefDateActionPerformed()\n");
        }
    }//GEN-LAST:event_jtfROMSRefDateActionPerformed

    private void jtfROMSRefDateMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtfROMSRefDateMousePressed
        jtfROMSRefDate.selectAll();
    }//GEN-LAST:event_jtfROMSRefDateMousePressed

    private void jbSelGridFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSelGridFileActionPerformed
        logger.info("++++starting jbSelGridFileActionPerformed()");
        File f = new File(globalInfo.getGridFile());
        jfcNC.setCurrentDirectory(f);
        jfcNC.setDialogTitle("Select ROMS grid:");
        jfcNC.setApproveButtonText("Select");
        int res = jfcNC.showOpenDialog(this);
        if (res==jfcNC.APPROVE_OPTION) {
            f = jfcNC.getSelectedFile();
            jcbGridFiles.setSelectedItem(f.getPath());//this the path in the GlobalInfo instance
            jcbGridFiles.setToolTipText(f.getPath());
        }
        logger.info("++++ending jbSelGridFileActionPerformed()\n");
    }//GEN-LAST:event_jbSelGridFileActionPerformed

    private void jbSelCanonicalFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSelCanonicalFileActionPerformed
        logger.info("++++starting jbSelCanonicalFileActionPerformed()");
        File f = new File(globalInfo.getCanonicalFile());
        jfcNC.setCurrentDirectory(f);
        jfcNC.setDialogTitle("Select ROMS model canonical output file:");
        jfcNC.setApproveButtonText("Select");
        int res = jfcNC.showOpenDialog(this);
        if (res==jfcNC.APPROVE_OPTION) {
            f = jfcNC.getSelectedFile();
            jcbCanonicalFiles.setSelectedItem(f.getPath());//this sets the path in the GlobalInfo  instance
            jcbCanonicalFiles.setToolTipText(f.getPath());
        }
        logger.info("+++++finished jbSelCanonicalFileActionPerformed()\n");
    }//GEN-LAST:event_jbSelCanonicalFileActionPerformed

    private void jcbGridFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbGridFilesActionPerformed
        if (!initializing){
            logger.info("++++starting jcbGridFilesActionPerformed()");
            String grdFN = (String) jcbGridFiles.getSelectedItem();
            jcbGridFiles.setToolTipText(grdFN);
            globalInfo.setGridFile(grdFN);
            grdLM.addElement(grdFN);
            logger.info("++++ending jcbGridFilesActionPerformed(): \n\tROMS grid file set to "+grdFN+"\n");
        }
    }//GEN-LAST:event_jcbGridFilesActionPerformed

    private void jcbCanonicalFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbCanonicalFilesActionPerformed
        if (!initializing){
            logger.info("++++starting jcbCanonicalFilesActionPerformed()");
            String canFN = (String) jcbCanonicalFiles.getSelectedItem();
            jcbCanonicalFiles.setToolTipText(canFN);
            globalInfo.setCanonicalFile(canFN);
            canLM.addElement(canFN);
            logger.info("++++ending jcbCanonicalFilesActionPerformed(): \n\tROMS canonical file set to "+canFN+"\n");
        }
    }//GEN-LAST:event_jcbCanonicalFilesActionPerformed

    private void jcbMapRegionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMapRegionsActionPerformed
        if (!initializing){
            logger.info("++++starting jcbMapRegionsActionPerformed()");
            String val = (String) jcbMapRegions.getSelectedItem();
            if (val!=null){
                globalInfo.setMapRegion(val);//update globalInfo
                logger.info("map region updated to '"+globalInfo.getMapRegion()+"'");
            }
            logger.info("++++ending jcbMapRegionsActionPerformed()\n");
        }
    }//GEN-LAST:event_jcbMapRegionsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private wts.roms.model.CriticalModelVariablesInfoCustomizer criticalMVsCustomizer;
    private wts.roms.model.CriticalGrid2DVariablesInfoCustomizer grid2DCVIsCustomizer;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbSelCanonicalFile;
    private javax.swing.JButton jbSelGridFile;
    private javax.swing.JComboBox jcbCanonicalFiles;
    private javax.swing.JComboBox jcbGridFiles;
    private javax.swing.JComboBox jcbMapRegions;
    private javax.swing.JTextField jtfROMSRefDate;
    private wts.roms.model.OptionalModelVariablesInfoCustomizer oviCustomizer;
    // End of variables declaration//GEN-END:variables
     
    /**
     * Write editor properties to file:
     *  1. recent grid files
     *  2. recent canonical files
     * @param p 
     */
    void writeProperties(java.util.Properties p) {
        logger.info("+++++starting writeProperties()");
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        
        //save recent ROMS grid file names
        if ((grdLM!=null)&&(grdLM.getSize()>0)){
            for (int i=0;i<4;i++) {
                String str = (String) grdLM.getElementAt(i);
                if (str!=null) p.setProperty("RecentROMSGridFiles."+i,str);
            }
        }
        
        //save recent ROMS canonical file names
        if ((canLM!=null)&&(canLM.getSize()>0)){
            for (int i=0;i<4;i++) {
                String str = (String) canLM.getElementAt(i);
                if (str!=null) p.setProperty("RecentROMSCanonicalFiles."+i,str);
            }
        }
        logger.info("+++++ending writeProperties()\n");
    }

    /**
     * This method is called after instantiation but before the component is opened,
     * so initializing = true.
     * 
     * @param p 
     */
    void readProperties(java.util.Properties p) {
        logger.info("++++started readProperties()");
        String version = p.getProperty("version");
        
        jcbGridFiles.removeAllItems();
        for (int i=3;i>-1;i--) {
            String str  = p.getProperty("RecentROMSGridFiles."+i);
            if (str!=null) {
                grdLM.addElement(str);
                jcbGridFiles.addItem(str);
            }
        }        
        jcbCanonicalFiles.removeAllItems();
        for (int i=3;i>-1;i--) {
            String str  = p.getProperty("RecentROMSCanonicalFiles."+i);
            if (str!=null) {
                canLM.addElement(str);
                jcbCanonicalFiles.addItem(str);
            }
        }        
        logger.info("++++finished readProperties()");
    }
    
    /**
     * Enables ability to load ROMS info from properties file
     * 
     * @param canLoad 
     */
    public void enableLoadAction(boolean canLoad){
        if (canLoad){
            content.add(infoLoader);//add a LoadCookie to the instance content
            logger.info("----Load enabled!!");
        } else {
            content.remove(infoLoader);//remove a loadCookie from the instance content 
            logger.info("----Load disabled!!");
        }
    }
    
    /**
     * Enables ability to save ROMS info to properties file..
     * 
     * @param canSave 
     */
    public void enableSaveAction(boolean canSave){
        if (canSave){
            content.add(infoSaver);//add the SaveCookie to the instance content
            logger.info("----Save enabled!!");
        } else {
            content.remove(infoSaver);//remove the SaveCookie from the instance content 
            logger.info("----Save disabled!!");
        }
    }
    
    /**
     * Enables ability to reset ROMS info to default.
     * 
     * @param canReset 
     */
    public void enableResetAction(boolean canReset){
        if (canReset){
            content.add(resetter);//add the resettere to the instance content
            logger.info("----Reset enabled!!");
        } else {
            content.remove(resetter);//remove the resetter from the instance content 
            logger.info("----Reset disabled!!");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.info("--starting propertyChange(): "+evt.getPropertyName());
        switch (evt.getPropertyName()) {
            case GlobalInfo.PROP_CanonicalFile:
                enableSaveAction(true);
                break;
            case GlobalInfo.PROP_GridFile:
                enableSaveAction(true);
                break;
            case GlobalInfo.PROP_MapRegion:
                enableSaveAction(true);
                break;
            case GlobalInfo.PROP_RefDate:
                enableSaveAction(true);
                break;
            case GlobalInfo.PROP_Grid2DCVI_RESET:
                enableSaveAction(true);
                break;
            case GlobalInfo.PROP_ModelCVI_RESET:
                enableSaveAction(true);
                break;
            case GlobalInfo.PROP_ModelOVI_ADDED:
                enableSaveAction(true);
                break;
            case GlobalInfo.PROP_ModelOVI_REMOVED:
                enableSaveAction(true);
                break;
            case GlobalInfo.PROP_ModelOVI_RENAMED:
                enableSaveAction(true);
                break;
            case GlobalInfo.PROP_ModelOVI_RESET:
                enableSaveAction(true);
                break;
        }
        logger.info("--finished propertyChange(): "+evt.getPropertyName());
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement open functionality for ROMS properties file
     */
    private class InfoLoader implements Openable {

        @Override
        public void open() {
            logger.info("--starting InfoLoader.open()");
            jfcGI.setCurrentDirectory(new File(globalInfo.getWorkingDir()));
            int res = jfcGI.showOpenDialog(ROMSInfoEditorTopComponent.this);
            if (res!=JFileChooser.APPROVE_OPTION) return;
            try {
                File f = jfcGI.getSelectedFile();
                globalInfo.readProperties(f);
                componentOpened();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            logger.info("--finished InfoLoader.open()");
        }
    }

    //------------------------------------------------------------------------//
    /**
     * Private class to implement save functionality for the ROMS properties file.
     * The properties are saved to a file in the working directory.
     */
    private class InfoSaver implements Savable  {

        @Override
        public void save() throws IOException {
            logger.info("--starting InfoSaver.save()");
            String fn = globalInfo.getWorkingDir()+File.separator+GlobalInfo.propsFN;
            globalInfo.writeProperties(fn);
            logger.info("--finished InfoSaver.save()");
        }
    }
    
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement a "reset" capability for the ROMS info
     */
    private class Resetter implements Resetable {

        @Override
        public void reset() {
            logger.info("--starting Resetter.reset()");
            globalInfo.reset();
            componentOpened();
            logger.info("--finished Resetter.reset()");
        }        
    }
}
