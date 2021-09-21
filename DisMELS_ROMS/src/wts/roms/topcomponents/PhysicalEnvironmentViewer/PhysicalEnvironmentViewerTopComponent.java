/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.PhysicalEnvironmentViewer;

import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.filter.IllegalFilterException;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.progress.ProgressRunnable;
import org.netbeans.api.progress.ProgressUtils;
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
import wts.GIS.styling.ColorBarStyle;
import wts.GIS.styling.VectorStyle;
import wts.models.utilities.CalendarIF;
import wts.roms.gis.MapDataGradientHorizontal2D;
import wts.roms.gis.MapDataScalar2D;
import wts.roms.gis.MapDataGradientHorizontal3D;
import wts.roms.gis.MapDataScalar3D;
import wts.roms.gis.MapDataInterfaceScalarBase;
import wts.roms.gis.MapDataInterfaceVectorBase;
import wts.roms.gis.MapDataVector2D;
import wts.roms.gis.MapDataVector3D;
import wts.roms.model.*;
import wts.roms.topcomponents.MapViewer.MapViewerTopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//wts.roms.topcomponents.PhysicalEnvironmentViewer//PhysicalEnvironmentViewer//EN",
autostore = false)
@TopComponent.Description(preferredID = "PhysicalEnvironmentViewerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "wts.roms.topcomponents.PhysicalEnvironmentViewer.PhysicalEnvironmentViewerTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_PhysicalEnvironmentViewerAction",
preferredID = "PhysicalEnvironmentViewerTopComponent")
@Messages({
    "CTL_PhysicalEnvironmentViewerAction=Physical environment viewer",
    "CTL_PhysicalEnvironmentViewerTopComponent=Physical Environment Viewer",
    "HINT_PhysicalEnvironmentViewerTopComponent=This is the Physical Environment Viewer window"
})
public final class PhysicalEnvironmentViewerTopComponent extends TopComponent implements PropertyChangeListener {
    
    /** singleton object */
    private static PhysicalEnvironmentViewerTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "PhysicalEnvironmentViewerTopComponent";
    /** */
    private static final Logger logger = Logger.getLogger(PREFERRED_ID);

    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized PhysicalEnvironmentViewerTopComponent getDefault(){
        if (instance==null){
            instance = new PhysicalEnvironmentViewerTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized PhysicalEnvironmentViewerTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof PhysicalEnvironmentViewerTopComponent) {
            return (PhysicalEnvironmentViewerTopComponent) win;
        }
        logger.warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }
    

    /** indicates whether to pay attention to events */
    private boolean doEvents = true;//flag indicates whether to pay attention to events

    /** singleton instance of GlobalInfo */
    private GlobalInfo romsGI;

    private NetcdfReader netcdfReader = null;
    private CalendarIF calendar       = null;
    private PhysicalEnvironment pe    = null;
    private Interpolator3D i3d        = null;

    /** name of scalar field to map */
    private String scalarFld = null;
    /** feature collection for the scalar map layer */
    private FeatureCollection scalarFC = null;
    /**style for the scalar map layer */
    private ColorBarStyle scalarStyle = null;
    /** the scalar map layer */
    private MapLayer scalarLayer = null;

    /** name of vector field x component to map */
    private String vectorFldX = null;
    /** name of vector field y component to map */
    private String vectorFldY = null;
    /** feature collection for the scalar map layer */
    private FeatureCollection vectorFC = null;
    /**style for the scalar map layer */
    private VectorStyle vectorStyle = null;
    /** the vector map layer */
    private MapLayer vectorLayer    = null;

    /** name of scalar field to map */
    private String horizGradFld = null;
    /** feature collection for the scalar map layer */
    private FeatureCollection horizGradFC = null;
    /**style for the horizontal gradient map layer */
    private VectorStyle     horizGradStyle = null;
    /** the horizontal gradient map layer */
    private MapLayer        horizGradLayer = null;
    
    /** the singleton instance of the MapViewerTopComponent */
    private MapViewerTopComponent tcMapViewer = null;
    
    /** instance content reflecting Loader and Saver capabilities */
    private InstanceContent content;
    
    private int otIndex = 1;

    /** flag to perform operations when componentOpened() is called */
    private boolean doOnOpen = true;
    
    public PhysicalEnvironmentViewerTopComponent() {
        logger.info("Start INSTANTIATING PhysEnvViewerTopComponent");
        initComponents();
        initComponents1();
        setName(Bundle.CTL_PhysicalEnvironmentViewerTopComponent());
        setToolTipText(Bundle.HINT_PhysicalEnvironmentViewerTopComponent());

        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect csvSaver, csvLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
        logger.info("Finished INSTANTIATING PhysEnvViewerTopComponent");
    }

    private void initComponents1() {
        logger.info("Starting initCompnents1");
        romsGI = GlobalInfo.getInstance();
        
        jfbDataset.addFileFilter("nc", "netCDF file");
        sfCustomizer.setVisible(false);//set invisible, initially
        vfCustomizer.setVisible(false);
        hgCustomizer.setVisible(false);
        
        jfbDataset.setEnabled(false);
        jbUpdate.setEnabled(false);
        jbPrev.setEnabled(false);
        jbNext.setEnabled(false);
        logger.info("Finished initCompnents1");
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgScalarDepthButtons = new javax.swing.ButtonGroup();
        bgVectorDepthButtons = new javax.swing.ButtonGroup();
        jfbDataset = new com.wtstockhausen.beans.swing.JFilenameBean();
        jpNavigation = new javax.swing.JPanel();
        jbNext = new javax.swing.JButton();
        jbPrev = new javax.swing.JButton();
        jbUpdate = new javax.swing.JButton();
        jbResetAll = new javax.swing.JButton();
        jpOceanTime = new javax.swing.JPanel();
        jspTime = new javax.swing.JSpinner();
        jtfTime = new javax.swing.JTextField();
        jtfTime1 = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpScalarField = new javax.swing.JPanel();
        jcbScalarVar = new javax.swing.JComboBox();
        jpScalarDepthSelector = new javax.swing.JPanel();
        jrbScalarK = new javax.swing.JRadioButton();
        jspScalarK = new javax.swing.JSpinner();
        jrbScalarDepth = new javax.swing.JRadioButton();
        jtfScalarDepth = new javax.swing.JTextField();
        jchkUpdateStyleForSF = new javax.swing.JCheckBox();
        jchkShowSF = new javax.swing.JCheckBox();
        jpScalarRange = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jtfScalarMin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfScalarMax = new javax.swing.JTextField();
        jbScalarColorScale = new javax.swing.JButton();
        sfCustomizer = new wts.GIS.styling.ColorBarStyleCustomizer();
        jpVectorField = new javax.swing.JPanel();
        jpVectorDepthSelector = new javax.swing.JPanel();
        jrbVectorK = new javax.swing.JRadioButton();
        jspVectorK = new javax.swing.JSpinner();
        jrbVectorDepth = new javax.swing.JRadioButton();
        jtfVectorDepth = new javax.swing.JTextField();
        jjpVectorRange = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jtfMinVel = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jtfMaxVel = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jchkUpdateStyleForVF = new javax.swing.JCheckBox();
        jchkShowVF = new javax.swing.JCheckBox();
        jcbVectorX = new javax.swing.JComboBox();
        jcbVectorY = new javax.swing.JComboBox();
        jbEditVectorScaling = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        vfCustomizer = new wts.GIS.styling.VectorStyleCustomizer();
        jpHGrad = new javax.swing.JPanel();
        jpHGradDepthSelector = new javax.swing.JPanel();
        jrbHGradK = new javax.swing.JRadioButton();
        jspHGradK = new javax.swing.JSpinner();
        jrbHGradDepth = new javax.swing.JRadioButton();
        jtfHGradDepth = new javax.swing.JTextField();
        jpHGradRange = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jtfHGradMinMag = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jtfHGradMaxMag = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jchkUpdateStyleForHGrad = new javax.swing.JCheckBox();
        jchkShowHGrad = new javax.swing.JCheckBox();
        jcbHGradField = new javax.swing.JComboBox();
        jbEditHGradScaling = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        hgCustomizer = new wts.GIS.styling.VectorStyleCustomizer();

        jfbDataset.setLabel(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jfbDataset.label")); // NOI18N
        jfbDataset.setMinimumSize(new java.awt.Dimension(112, 33));
        jfbDataset.setPreferredSize(new java.awt.Dimension(430, 33));
        jfbDataset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfbDatasetActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jbNext, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbNext.text")); // NOI18N
        jbNext.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbNext.toolTipText")); // NOI18N
        jbNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbNext.setMaximumSize(new java.awt.Dimension(75, 25));
        jbNext.setMinimumSize(new java.awt.Dimension(75, 25));
        jbNext.setPreferredSize(new java.awt.Dimension(75, 25));
        jbNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbNextActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jbPrev, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbPrev.text")); // NOI18N
        jbPrev.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbPrev.toolTipText")); // NOI18N
        jbPrev.setActionCommand(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbPrev.actionCommand")); // NOI18N
        jbPrev.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbPrev.setMaximumSize(new java.awt.Dimension(75, 25));
        jbPrev.setMinimumSize(new java.awt.Dimension(75, 25));
        jbPrev.setPreferredSize(new java.awt.Dimension(75, 25));
        jbPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPrevActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jbUpdate, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbUpdate.text")); // NOI18N
        jbUpdate.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbUpdate.toolTipText")); // NOI18N
        jbUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbUpdateActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jbResetAll, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbResetAll.text")); // NOI18N
        jbResetAll.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbResetAll.toolTipText")); // NOI18N
        jbResetAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbResetAllActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpNavigationLayout = new javax.swing.GroupLayout(jpNavigation);
        jpNavigation.setLayout(jpNavigationLayout);
        jpNavigationLayout.setHorizontalGroup(
            jpNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpNavigationLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jbUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jbPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addComponent(jbResetAll)
                .addContainerGap())
        );
        jpNavigationLayout.setVerticalGroup(
            jpNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jbUpdate)
                .addComponent(jbPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jbNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jbResetAll))
        );

        jpOceanTime.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpOceanTime.border.title"))); // NOI18N

        jspTime.setModel(new javax.swing.SpinnerNumberModel());
        jspTime.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jspTime.toolTipText")); // NOI18N
        jspTime.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jspTimeStateChanged(evt);
            }
        });

        jtfTime.setEditable(false);
        jtfTime.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfTime.toolTipText")); // NOI18N

        jtfTime1.setEditable(false);
        jtfTime1.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfTime1.toolTipText")); // NOI18N

        javax.swing.GroupLayout jpOceanTimeLayout = new javax.swing.GroupLayout(jpOceanTime);
        jpOceanTime.setLayout(jpOceanTimeLayout);
        jpOceanTimeLayout.setHorizontalGroup(
            jpOceanTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpOceanTimeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jspTime, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfTime, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jpOceanTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpOceanTimeLayout.createSequentialGroup()
                    .addContainerGap(70, Short.MAX_VALUE)
                    .addComponent(jtfTime1, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        jpOceanTimeLayout.setVerticalGroup(
            jpOceanTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpOceanTimeLayout.createSequentialGroup()
                .addGroup(jpOceanTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jspTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtfTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jpOceanTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpOceanTimeLayout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(jtfTime1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(4, Short.MAX_VALUE)))
        );

        jpScalarField.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpScalarField.border.title"))); // NOI18N

        jcbScalarVar.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jcbScalarVar.toolTipText")); // NOI18N
        jcbScalarVar.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbScalarVarItemStateChanged(evt);
            }
        });
        jcbScalarVar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbScalarVarActionPerformed(evt);
            }
        });

        jpScalarDepthSelector.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpScalarDepthSelector.border.title"))); // NOI18N

        bgScalarDepthButtons.add(jrbScalarK);
        jrbScalarK.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jrbScalarK, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbScalarK.text")); // NOI18N
        jrbScalarK.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbScalarK.toolTipText")); // NOI18N

        jspScalarK.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        jspScalarK.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jspScalarK.toolTipText")); // NOI18N

        bgScalarDepthButtons.add(jrbScalarDepth);
        org.openide.awt.Mnemonics.setLocalizedText(jrbScalarDepth, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbScalarDepth.text")); // NOI18N
        jrbScalarDepth.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbScalarDepth.toolTipText")); // NOI18N

        jtfScalarDepth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfScalarDepth.setText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfScalarDepth.text")); // NOI18N
        jtfScalarDepth.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfScalarDepth.toolTipText")); // NOI18N

        javax.swing.GroupLayout jpScalarDepthSelectorLayout = new javax.swing.GroupLayout(jpScalarDepthSelector);
        jpScalarDepthSelector.setLayout(jpScalarDepthSelectorLayout);
        jpScalarDepthSelectorLayout.setHorizontalGroup(
            jpScalarDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpScalarDepthSelectorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpScalarDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbScalarK)
                    .addComponent(jrbScalarDepth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpScalarDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfScalarDepth, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jspScalarK, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpScalarDepthSelectorLayout.setVerticalGroup(
            jpScalarDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpScalarDepthSelectorLayout.createSequentialGroup()
                .addGroup(jpScalarDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbScalarK)
                    .addComponent(jspScalarK, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpScalarDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfScalarDepth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jrbScalarDepth)))
        );

        jchkUpdateStyleForSF.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jchkUpdateStyleForSF, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkUpdateStyleForSF.text")); // NOI18N
        jchkUpdateStyleForSF.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkUpdateStyleForSF.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jchkShowSF, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkShowSF.text")); // NOI18N
        jchkShowSF.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkShowSF.toolTipText")); // NOI18N

        jpScalarRange.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpScalarRange.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jLabel3.text")); // NOI18N

        jtfScalarMin.setText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfScalarMin.text")); // NOI18N
        jtfScalarMin.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfScalarMin.toolTipText")); // NOI18N
        jtfScalarMin.setEnabled(false);
        jtfScalarMin.setPreferredSize(new java.awt.Dimension(75, 22));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jLabel4.text")); // NOI18N

        jtfScalarMax.setText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfScalarMax.text")); // NOI18N
        jtfScalarMax.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfScalarMax.toolTipText")); // NOI18N
        jtfScalarMax.setEnabled(false);
        jtfScalarMax.setPreferredSize(new java.awt.Dimension(75, 22));

        javax.swing.GroupLayout jpScalarRangeLayout = new javax.swing.GroupLayout(jpScalarRange);
        jpScalarRange.setLayout(jpScalarRangeLayout);
        jpScalarRangeLayout.setHorizontalGroup(
            jpScalarRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpScalarRangeLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfScalarMin, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfScalarMax, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jpScalarRangeLayout.setVerticalGroup(
            jpScalarRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpScalarRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(jtfScalarMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel4)
                .addComponent(jtfScalarMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jbScalarColorScale, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbScalarColorScale.text")); // NOI18N
        jbScalarColorScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbScalarColorScaleActionPerformed(evt);
            }
        });

        sfCustomizer.setEnabled(false);

        javax.swing.GroupLayout jpScalarFieldLayout = new javax.swing.GroupLayout(jpScalarField);
        jpScalarField.setLayout(jpScalarFieldLayout);
        jpScalarFieldLayout.setHorizontalGroup(
            jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpScalarFieldLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbScalarColorScale)
                    .addComponent(sfCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jcbScalarVar, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(38, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jpScalarDepthSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(203, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(320, 320, 320)
                    .addComponent(jchkUpdateStyleForSF, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(40, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(319, 319, 319)
                    .addComponent(jchkShowSF, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(81, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jpScalarRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(29, Short.MAX_VALUE)))
        );
        jpScalarFieldLayout.setVerticalGroup(
            jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpScalarFieldLayout.createSequentialGroup()
                .addGap(167, 167, 167)
                .addComponent(jbScalarColorScale)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sfCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(252, Short.MAX_VALUE))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addComponent(jcbScalarVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 604, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(jpScalarDepthSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(524, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(75, 75, 75)
                    .addComponent(jchkUpdateStyleForSF)
                    .addContainerGap(533, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(48, 48, 48)
                    .addComponent(jchkShowSF)
                    .addContainerGap(560, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(115, 115, 115)
                    .addComponent(jpScalarRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(470, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpScalarField.TabConstraints.tabTitle"), jpScalarField); // NOI18N

        jpVectorField.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpVectorField.border.title"))); // NOI18N

        jpVectorDepthSelector.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpVectorDepthSelector.border.title"))); // NOI18N

        bgVectorDepthButtons.add(jrbVectorK);
        org.openide.awt.Mnemonics.setLocalizedText(jrbVectorK, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbVectorK.text")); // NOI18N
        jrbVectorK.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbVectorK.toolTipText")); // NOI18N

        jspVectorK.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        jspVectorK.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jspVectorK.toolTipText")); // NOI18N

        bgVectorDepthButtons.add(jrbVectorDepth);
        org.openide.awt.Mnemonics.setLocalizedText(jrbVectorDepth, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbVectorDepth.text")); // NOI18N
        jrbVectorDepth.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbVectorDepth.toolTipText")); // NOI18N

        jtfVectorDepth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfVectorDepth.setText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfVectorDepth.text")); // NOI18N
        jtfVectorDepth.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfVectorDepth.toolTipText")); // NOI18N

        javax.swing.GroupLayout jpVectorDepthSelectorLayout = new javax.swing.GroupLayout(jpVectorDepthSelector);
        jpVectorDepthSelector.setLayout(jpVectorDepthSelectorLayout);
        jpVectorDepthSelectorLayout.setHorizontalGroup(
            jpVectorDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpVectorDepthSelectorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpVectorDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbVectorK)
                    .addComponent(jrbVectorDepth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpVectorDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfVectorDepth)
                    .addComponent(jspVectorK, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpVectorDepthSelectorLayout.setVerticalGroup(
            jpVectorDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpVectorDepthSelectorLayout.createSequentialGroup()
                .addGroup(jpVectorDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbVectorK)
                    .addComponent(jspVectorK, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpVectorDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfVectorDepth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jrbVectorDepth)))
        );

        jjpVectorRange.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jjpVectorRange.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jLabel1.text")); // NOI18N

        jtfMinVel.setText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfMinVel.text")); // NOI18N
        jtfMinVel.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfMinVel.toolTipText")); // NOI18N
        jtfMinVel.setEnabled(false);
        jtfMinVel.setPreferredSize(new java.awt.Dimension(75, 22));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jLabel2.text")); // NOI18N

        jtfMaxVel.setText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfMaxVel.text")); // NOI18N
        jtfMaxVel.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfMaxVel.toolTipText")); // NOI18N
        jtfMaxVel.setEnabled(false);
        jtfMaxVel.setPreferredSize(new java.awt.Dimension(75, 22));

        javax.swing.GroupLayout jjpVectorRangeLayout = new javax.swing.GroupLayout(jjpVectorRange);
        jjpVectorRange.setLayout(jjpVectorRangeLayout);
        jjpVectorRangeLayout.setHorizontalGroup(
            jjpVectorRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jjpVectorRangeLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfMinVel, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfMaxVel, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jjpVectorRangeLayout.setVerticalGroup(
            jjpVectorRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jjpVectorRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(jtfMaxVel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jjpVectorRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(jtfMinVel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jchkUpdateStyleForVF.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jchkUpdateStyleForVF, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkUpdateStyleForVF.text")); // NOI18N
        jchkUpdateStyleForVF.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkUpdateStyleForVF.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jchkShowVF, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkShowVF.text")); // NOI18N
        jchkShowVF.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkShowVF.toolTipText")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jchkUpdateStyleForVF, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jchkShowVF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jchkShowVF)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jchkUpdateStyleForVF))
        );

        jcbVectorX.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jcbVectorX.toolTipText")); // NOI18N
        jcbVectorX.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbVectorXItemStateChanged(evt);
            }
        });
        jcbVectorX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbVectorXActionPerformed(evt);
            }
        });

        jcbVectorY.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jcbVectorY.toolTipText")); // NOI18N
        jcbVectorY.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbVectorYItemStateChanged(evt);
            }
        });
        jcbVectorY.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbVectorYActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jbEditVectorScaling, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbEditVectorScaling.text")); // NOI18N
        jbEditVectorScaling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEditVectorScalingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 495, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(vfCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(vfCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jpVectorFieldLayout = new javax.swing.GroupLayout(jpVectorField);
        jpVectorField.setLayout(jpVectorFieldLayout);
        jpVectorFieldLayout.setHorizontalGroup(
            jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpVectorFieldLayout.createSequentialGroup()
                .addGroup(jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpVectorFieldLayout.createSequentialGroup()
                        .addComponent(jcbVectorX, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jcbVectorY, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jjpVectorRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpVectorFieldLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jbEditVectorScaling)))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpVectorFieldLayout.createSequentialGroup()
                    .addComponent(jpVectorDepthSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 203, Short.MAX_VALUE)))
            .addGroup(jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpVectorFieldLayout.createSequentialGroup()
                    .addGap(314, 314, 314)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(40, Short.MAX_VALUE)))
            .addGroup(jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpVectorFieldLayout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jpVectorFieldLayout.setVerticalGroup(
            jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpVectorFieldLayout.createSequentialGroup()
                .addGroup(jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcbVectorX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcbVectorY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(99, 99, 99)
                .addComponent(jjpVectorRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbEditVectorScaling)
                .addContainerGap(472, Short.MAX_VALUE))
            .addGroup(jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpVectorFieldLayout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(jpVectorDepthSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(557, Short.MAX_VALUE)))
            .addGroup(jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpVectorFieldLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(570, Short.MAX_VALUE)))
            .addGroup(jpVectorFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpVectorFieldLayout.createSequentialGroup()
                    .addGap(214, 214, 214)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpVectorField.TabConstraints.tabTitle"), jpVectorField); // NOI18N

        jpHGrad.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpHGrad.border.title"))); // NOI18N

        jpHGradDepthSelector.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpHGradDepthSelector.border.title"))); // NOI18N

        bgVectorDepthButtons.add(jrbHGradK);
        jrbHGradK.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jrbHGradK, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbHGradK.text")); // NOI18N
        jrbHGradK.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbHGradK.toolTipText")); // NOI18N

        jspHGradK.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));
        jspHGradK.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jspHGradK.toolTipText")); // NOI18N

        bgVectorDepthButtons.add(jrbHGradDepth);
        org.openide.awt.Mnemonics.setLocalizedText(jrbHGradDepth, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbHGradDepth.text")); // NOI18N
        jrbHGradDepth.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbHGradDepth.toolTipText")); // NOI18N

        jtfHGradDepth.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfHGradDepth.setText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfHGradDepth.text")); // NOI18N
        jtfHGradDepth.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfHGradDepth.toolTipText")); // NOI18N

        javax.swing.GroupLayout jpHGradDepthSelectorLayout = new javax.swing.GroupLayout(jpHGradDepthSelector);
        jpHGradDepthSelector.setLayout(jpHGradDepthSelectorLayout);
        jpHGradDepthSelectorLayout.setHorizontalGroup(
            jpHGradDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHGradDepthSelectorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpHGradDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbHGradK)
                    .addComponent(jrbHGradDepth))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpHGradDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jtfHGradDepth)
                    .addComponent(jspHGradK, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE))
                .addContainerGap())
        );
        jpHGradDepthSelectorLayout.setVerticalGroup(
            jpHGradDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHGradDepthSelectorLayout.createSequentialGroup()
                .addGroup(jpHGradDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jrbHGradK)
                    .addComponent(jspHGradK, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpHGradDepthSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfHGradDepth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jrbHGradDepth)))
        );

        jpHGradRange.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpHGradRange.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jLabel5.text")); // NOI18N

        jtfHGradMinMag.setText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfHGradMinMag.text")); // NOI18N
        jtfHGradMinMag.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfHGradMinMag.toolTipText")); // NOI18N
        jtfHGradMinMag.setEnabled(false);
        jtfHGradMinMag.setPreferredSize(new java.awt.Dimension(75, 22));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jLabel6.text")); // NOI18N

        jtfHGradMaxMag.setText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfHGradMaxMag.text")); // NOI18N
        jtfHGradMaxMag.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jtfHGradMaxMag.toolTipText")); // NOI18N
        jtfHGradMaxMag.setEnabled(false);
        jtfHGradMaxMag.setPreferredSize(new java.awt.Dimension(75, 22));

        javax.swing.GroupLayout jpHGradRangeLayout = new javax.swing.GroupLayout(jpHGradRange);
        jpHGradRange.setLayout(jpHGradRangeLayout);
        jpHGradRangeLayout.setHorizontalGroup(
            jpHGradRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHGradRangeLayout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfHGradMinMag, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtfHGradMaxMag, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jpHGradRangeLayout.setVerticalGroup(
            jpHGradRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHGradRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel6)
                .addComponent(jtfHGradMaxMag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jpHGradRangeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(jtfHGradMinMag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jchkUpdateStyleForHGrad.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jchkUpdateStyleForHGrad, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkUpdateStyleForHGrad.text")); // NOI18N
        jchkUpdateStyleForHGrad.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkUpdateStyleForHGrad.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jchkShowHGrad, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkShowHGrad.text")); // NOI18N
        jchkShowHGrad.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jchkShowHGrad.toolTipText")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jchkUpdateStyleForHGrad, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jchkShowHGrad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jchkShowHGrad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jchkUpdateStyleForHGrad))
        );

        jcbHGradField.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jcbHGradField.toolTipText")); // NOI18N
        jcbHGradField.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbHGradFieldItemStateChanged(evt);
            }
        });
        jcbHGradField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbHGradFieldActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jbEditHGradScaling, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jbEditHGradScaling.text")); // NOI18N
        jbEditHGradScaling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEditHGradScalingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 495, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(hgCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(hgCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout jpHGradLayout = new javax.swing.GroupLayout(jpHGrad);
        jpHGrad.setLayout(jpHGradLayout);
        jpHGradLayout.setHorizontalGroup(
            jpHGradLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHGradLayout.createSequentialGroup()
                .addGroup(jpHGradLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpHGradRange, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jpHGradLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jbEditHGradScaling))
                    .addComponent(jcbHGradField, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(jpHGradLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpHGradLayout.createSequentialGroup()
                    .addComponent(jpHGradDepthSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 203, Short.MAX_VALUE)))
            .addGroup(jpHGradLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpHGradLayout.createSequentialGroup()
                    .addGap(314, 314, 314)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(40, Short.MAX_VALUE)))
            .addGroup(jpHGradLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpHGradLayout.createSequentialGroup()
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jpHGradLayout.setVerticalGroup(
            jpHGradLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpHGradLayout.createSequentialGroup()
                .addComponent(jcbHGradField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(99, 99, 99)
                .addComponent(jpHGradRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbEditHGradScaling)
                .addContainerGap(472, Short.MAX_VALUE))
            .addGroup(jpHGradLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpHGradLayout.createSequentialGroup()
                    .addGap(34, 34, 34)
                    .addComponent(jpHGradDepthSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(557, Short.MAX_VALUE)))
            .addGroup(jpHGradLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpHGradLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(570, Short.MAX_VALUE)))
            .addGroup(jpHGradLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpHGradLayout.createSequentialGroup()
                    .addGap(214, 214, 214)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpHGrad.TabConstraints.tabTitle"), jpHGrad); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jpNavigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 54, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jfbDataset, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 38, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jpOceanTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 33, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addComponent(jTabbedPane1)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jpNavigation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(767, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jfbDataset, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 799, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(82, 82, 82)
                    .addComponent(jpOceanTime, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(689, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(160, 160, 160)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 681, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jTabbedPane1.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void jfbDatasetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfbDatasetActionPerformed
        if (doEvents) {
            logger.info("Starting jfbDatasetActionPerformed()");
            Cursor c = getCursor();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            loadDataset();
            setCursor(c);
        }
    }//GEN-LAST:event_jfbDatasetActionPerformed

    /**
     * Loads the ROMS model dataset specified by jfbDatset.getFilename().
     */
    private void loadDataset(){
        logger.info("started loadDataset()");
        ProgressRunnable<Integer> r = new ProgressRunnable<Integer>(){
            @Override
            public Integer run(ProgressHandle handle) {
                logger.info("loadDataset(): starting run()");
                try {
                    //read dataset from netcdf file
                    netcdfReader = new NetcdfReader(jfbDataset.getFilename());
                    pe = new PhysicalEnvironment(0, netcdfReader);
                    i3d = new Interpolator3D(pe);
                    setOceanTime(0);
                    setVariables();
                    jbUpdate.setEnabled(true);
                    jbPrev.setEnabled(true);
                    jbNext.setEnabled(true);
                    repaint();
                    logger.info("loadDataset(): finished try successfully");
                } catch (IOException ex) {
                    logger.info("loadDataset(): finished try UNsuccessfully with IOException");
                    logger.severe(ex.getMessage());
                } catch (Exception ex){
                    logger.info("loadDataset(): finished try UNsuccessfully with Exception");
                    logger.severe(ex.toString());
                }
                logger.info("loadDataset(): finished run()");
                return 0;
            }

        };
        ProgressHandle h = ProgressHandleFactory.createHandle("Reading ROMS model dataset...");
        ProgressUtils.showProgressDialogAndRunLater(r, h, false);
        logger.info("finished loadDataset()");
    }
    
    /**
     * Updates the oceantime presented by the jtfTime and jtfTime1 text boxes.
     * This DOES NOT update the map viewer date (because it does not update the
     * map layers).
     * 
     * @param evt 
     */
    private void jspTimeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspTimeStateChanged
        if (doEvents){
            //update the oceantime shown in the text fields 
            //use update() to update map to this time
            int i = ((Integer) jspTime.getValue()) - 1;
            double ot = netcdfReader.getOceanTime(i);
            if (calendar==null) calendar = romsGI.getCalendar();
            calendar.setTimeOffset((long) ot);
            jtfTime.setText(calendar.getDate().getDateTimeString());
            jtfTime1.setText(""+(new Double(ot).longValue()));
        }
    }//GEN-LAST:event_jspTimeStateChanged

    private void jcbScalarVarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbScalarVarItemStateChanged
        //do nothing
    }//GEN-LAST:event_jcbScalarVarItemStateChanged

    private void jcbScalarVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbScalarVarActionPerformed
        if (doEvents) {
            ModelGrid3D grid3D = romsGI.getGrid3D();
            if ((grid3D==null)||!grid3D.hasVerticalGridInfo()) return;
            try {
                boolean hasK = false;//assume no vertical dimension for selected field
                Object obj = jcbScalarVar.getSelectedItem();
                if (obj instanceof String) {
                    String strFld = (String) obj;//name of selected model field
                    ModelData md = pe.getField(strFld);
                    if (md!=null){
                        hasK = md.getVertPosType()!=ModelTypes.VERT_POSTYPE_NONE;
                    } else {
                        md = grid3D.getGridField(strFld);
                        hasK = false;
                    }
                    this.jpScalarDepthSelector.setEnabled(hasK);
                    this.jpScalarDepthSelector.setVisible(hasK);
                    if (hasK) {
                        int vertType = md.getVertPosType();
                        SpinnerNumberModel nm = (SpinnerNumberModel) jspScalarK.getModel();
                        nm.setStepSize(1);
                        nm.setMaximum(grid3D.getN());//max index of cells or layers
                        if (vertType == ModelTypes.VERT_POSTYPE_W) {
                            nm.setMinimum(0);//layers start at 0
                            nm.setValue(0);
                        } else {
                            nm.setMinimum(1);//layers start at 0
                            nm.setValue(1);
                        }
                    }
                }
            } catch (Exception ex) {
                logger.severe(ex.getMessage());
            }
        }
    }//GEN-LAST:event_jcbScalarVarActionPerformed

    private void jbNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbNextActionPerformed
        Cursor c = this.getCursor();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            next();
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
        this.setCursor(c);
    }//GEN-LAST:event_jbNextActionPerformed

    private void jbPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPrevActionPerformed
        Cursor c = this.getCursor();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            previous();
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        }
        this.setCursor(c);
    }//GEN-LAST:event_jbPrevActionPerformed

    private void jbUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbUpdateActionPerformed
        Cursor c = this.getCursor();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        updateMapLayer();
        this.setCursor(c);//set to original cursor
    }//GEN-LAST:event_jbUpdateActionPerformed

    private void jbScalarColorScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbScalarColorScaleActionPerformed
        if (evt.getActionCommand().equals("Edit color scale")&&(scalarLayer!=null)) {
            logger.info(scalarStyle.toString());
            scalarStyle = (ColorBarStyle) scalarLayer.getStyle();
            sfCustomizer.setObject(scalarStyle);
            sfCustomizer.setVisible(true);
            jbScalarColorScale.setActionCommand("Hide color scale");
            jbScalarColorScale.setText("Hide color scale");
            validate();
        } else {
            jbScalarColorScale.setActionCommand("Edit color scale");
            jbScalarColorScale.setText("Edit color scale");
            sfCustomizer.setVisible(false);
        }
        if ((scalarStyle!=null)&&scalarStyle.mustUpdateStyle()) {
            try {
                logger.info("updating scalar style for user changes");
                scalarStyle.updateStyle();
                tcMapViewer.repaint();
                logger.info("updating scalar style for user changes");
            } catch (IllegalFilterException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }//GEN-LAST:event_jbScalarColorScaleActionPerformed

    private void jcbVectorXItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbVectorXItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbVectorXItemStateChanged

    private void jcbVectorXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbVectorXActionPerformed
        if (doEvents) {
            logger.info("starting jcbVectorXActionPerformed(): "+evt.toString() );
            ModelGrid3D grid3D = romsGI.getGrid3D();
            if ((grid3D==null)||!grid3D.hasVerticalGridInfo()) return;
            try {
                boolean hasK = false;//assume no vertical dimension for selected field
                Object obj = jcbVectorX.getSelectedItem();
                if (obj instanceof String) {
                    String strFld = (String) obj;//name of selected model field
                    logger.info("jcbVectorXActionPerformed() for "+strFld);
                    ModelData md = pe.getField(strFld);
                    if (md!=null){
                        hasK = md.getVertPosType()!=ModelTypes.VERT_POSTYPE_NONE;
                    } else {
                        md = grid3D.getGridField(strFld);
                        hasK = false;
                    }
                    this.jpVectorDepthSelector.setEnabled(hasK);
                    this.jpVectorDepthSelector.setVisible(hasK);
                    if (hasK) {
                        int vertType = md.getVertPosType();
                        SpinnerNumberModel nm = (SpinnerNumberModel) jspVectorK.getModel();
                        nm.setStepSize(1);
                        nm.setMaximum(grid3D.getN());//max index of cells or layers
                        if (vertType == ModelTypes.VERT_POSTYPE_W) {
                            nm.setMinimum(0);//layers start at 0
                            nm.setValue(0);
                        } else {
                            nm.setMinimum(1);//layers start at 0
                            nm.setValue(1);
                        }
                    }
                }
                logger.info("jcbVectorXActionPerformed(): finished try");
            } catch (Exception ex) {
                logger.severe(ex.getMessage());
            }
        }
    }//GEN-LAST:event_jcbVectorXActionPerformed

    private void jcbVectorYItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbVectorYItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbVectorYItemStateChanged

    private void jcbVectorYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbVectorYActionPerformed
        if (doEvents) {
            logger.info("starting jcbVectorYActionPerformed(): "+evt.toString() );
            ModelGrid3D grid3D = romsGI.getGrid3D();
            if ((grid3D==null)||!grid3D.hasVerticalGridInfo()) return;
            try {
                boolean hasK = false;//assume no vertical dimension for selected field
                Object obj = jcbVectorY.getSelectedItem();
                if (obj instanceof String) {
                    String strFld = (String) obj;//name of selected model field
                    logger.info("jcbVectorYActionPerformed() for "+strFld);
                    ModelData md = pe.getField(strFld);
                    if (md!=null){
                        hasK = md.getVertPosType()!=ModelTypes.VERT_POSTYPE_NONE;
                    } else {
                        md = grid3D.getGridField(strFld);
                        hasK = false;
                    }
                    this.jpVectorDepthSelector.setEnabled(hasK);
                    this.jpVectorDepthSelector.setVisible(hasK);
                    if (hasK) {
                        int vertType = md.getVertPosType();
                        SpinnerNumberModel nm = (SpinnerNumberModel) jspVectorK.getModel();
                        nm.setStepSize(Integer.valueOf(1));
                        nm.setMaximum(Integer.valueOf(grid3D.getN()));//max index of cells or layers
                        if (vertType == ModelTypes.VERT_POSTYPE_W) {
                            nm.setMinimum(Integer.valueOf(0));//layers start at 0
                            nm.setValue(Integer.valueOf(0));
                        } else {
                            nm.setMinimum(Integer.valueOf(1));//layers start at 0
                            nm.setValue(Integer.valueOf(1));
                        }
                    }
                }
                logger.info("jcbVectorYActionPerformed(): finished try");
            } catch (Exception ex) {
                logger.severe(ex.getMessage());
            }
        }
    }//GEN-LAST:event_jcbVectorYActionPerformed

    private void jbEditVectorScalingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEditVectorScalingActionPerformed
        logger.info("strting jbEditVectorScalingActionPerformed(): "+evt.toString());
        if (evt.getActionCommand().equals("Edit vector scaling")&&(vectorLayer!=null)) {
            logger.info("Editing vector scaling");
            vectorStyle = (VectorStyle) vectorLayer.getStyle();
            logger.info(vectorStyle.toString());
            vfCustomizer.setObject(vectorStyle);
            vfCustomizer.setVisible(true);
            logger.info(vectorStyle.toString());
            jbEditVectorScaling.setActionCommand("Hide vector scaling");
            jbEditVectorScaling.setText("Hide vector scaling");
            validate();
            return;
        }
        if (evt.getActionCommand().equals("Hide vector scaling")||(vectorLayer==null)) {
            logger.info("revealing vector scaling");
            jbEditVectorScaling.setActionCommand("Edit vector scaling");
            jbEditVectorScaling.setText("Edit vector scaling");
            vfCustomizer.setVisible(false);
            validate();
        }
    }//GEN-LAST:event_jbEditVectorScalingActionPerformed

    private void jcbHGradFieldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbHGradFieldItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbHGradFieldItemStateChanged

    private void jcbHGradFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbHGradFieldActionPerformed
        if (doEvents) {
            ModelGrid3D grid3D = romsGI.getGrid3D();
            if ((grid3D==null)||!grid3D.hasVerticalGridInfo()) return;
            try {
                boolean hasK = false;//assume no vertical dimension for selected field
                Object obj = jcbHGradField.getSelectedItem();
                if (obj instanceof String) {
                    String strFld = (String) obj;//name of selected model field
                    ModelData md = pe.getField(strFld);
                    if (md!=null){
                        hasK = md.getVertPosType()!=ModelTypes.VERT_POSTYPE_NONE;
                    } else {
                        md = grid3D.getGridField(strFld);
                        hasK = false;
                    }
                    this.jpHGradDepthSelector.setEnabled(hasK);
                    this.jpHGradDepthSelector.setVisible(hasK);
                    if (hasK) {
                        int vertType = md.getVertPosType();
                        SpinnerNumberModel nm = (SpinnerNumberModel) jspHGradK.getModel();
                        nm.setStepSize(Integer.valueOf(1));
                        nm.setMaximum(Integer.valueOf(grid3D.getN()));//max index of cells or layers
                        if (vertType == ModelTypes.VERT_POSTYPE_W) {
                            nm.setMinimum(Integer.valueOf(0));//layers start at 0
                            nm.setValue(Integer.valueOf(0));
                        } else {
                            nm.setMinimum(Integer.valueOf(1));//layers start at 0
                            nm.setValue(Integer.valueOf(1));
                        }
                    }
                }
            } catch (Exception ex) {
                logger.severe(ex.getMessage());
            }
        }

    }//GEN-LAST:event_jcbHGradFieldActionPerformed

    private void jbEditHGradScalingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEditHGradScalingActionPerformed
        if (evt.getActionCommand().equals("Edit scaling")&&(horizGradLayer!=null)) {
            vectorStyle = (VectorStyle) horizGradLayer.getStyle();
            logger.info(vectorStyle.toString());
            hgCustomizer.setObject(vectorStyle);
            hgCustomizer.setVisible(true);
            logger.info(vectorStyle.toString());
            jbEditHGradScaling.setActionCommand("Hide scaling");
            jbEditHGradScaling.setText("Hide scaling");
            validate();
            return;
        }
        if (evt.getActionCommand().equals("Hide scaling")||(horizGradLayer==null)) {
            jbEditHGradScaling.setActionCommand("Edit scaling");
            jbEditHGradScaling.setText("Edit scaling");
            hgCustomizer.setVisible(false);
            validate();
        }
    }//GEN-LAST:event_jbEditHGradScalingActionPerformed

    private void jbResetAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbResetAllActionPerformed
        resetAll();
    }//GEN-LAST:event_jbResetAllActionPerformed
    
    private void resetVariables(){
        logger.info("Resetting variables to default");
        jbUpdate.setEnabled(false);
        jbPrev.setEnabled(false);
        jbNext.setEnabled(false);
        jcbScalarVar.removeAllItems();
        jcbVectorX.removeAllItems();
        jcbVectorY.removeAllItems();
        jcbHGradField.removeAllItems();
        jcbScalarVar.setSelectedIndex(-1);//set to "no item"
        jcbVectorX.setSelectedIndex(-1);//set to "no item"
        jcbVectorY.setSelectedIndex(-1);//set to "no item"
        jcbHGradField.setSelectedIndex(-1);//set to "no item"
        scalarFld = null;
        vectorFldX = null;
        vectorFldY = null;
        horizGradFld = null;
        jbUpdate.setEnabled(false);
        jbPrev.setEnabled(false);
        jbNext.setEnabled(false);
        logger.info("Finished resetting variables to default");        
    }
    
    private void resetAll(){
        logger.info("Resetting state to default");
        doEvents = false;
        resetVariables();
        netcdfReader = null;
        pe = null;
        i3d = null;
        jspTime.setModel(new javax.swing.SpinnerNumberModel());
        jtfTime.setText("");
        jtfTime1.setText("");
        jfbDataset.setFilename("");
        //reset style and map viewer controls
        jchkShowSF.setSelected(false); jchkUpdateStyleForSF.setSelected(false);
        if (scalarLayer!=null) tcMapViewer.removeGISLayer(scalarLayer);
        jchkShowVF.setSelected(false); jchkUpdateStyleForVF.setSelected(false);
        if (vectorLayer!=null) tcMapViewer.removeGISLayer(vectorLayer);
        jchkShowHGrad.setSelected(false); jchkUpdateStyleForHGrad.setSelected(false);
        if (horizGradLayer!=null) tcMapViewer.removeGISLayer(horizGradLayer);
        tcMapViewer.repaint();
        doEvents = true;
        logger.info("Finished resetting state to default");        
    }
    
    /**
     * Updates oceantime to jth (0-based) time segment in the current netcdf file.
     * The jspTime spinner is set to j+1, the jtfTime, jtfTime1 text boxes are set to 
     * the corresponding values (date string and long), and the map viewer 
     * time display is updated.
     * 
     * @param j 
     */
    private void setOceanTime(int j) {
        logger.info("starting setOceanTime("+j+")");
        int mx = netcdfReader.getNumTimeSteps();
        logger.info("starting setOceanTime("+j+"): mx = "+mx);
        double oceantime = netcdfReader.getOceanTime(j);
        if (calendar==null) calendar = romsGI.getCalendar();
        calendar.setTimeOffset((long)oceantime);
        SpinnerNumberModel snm = new SpinnerNumberModel(j+1,1,mx,1);
        jspTime.setModel(snm);
        jtfTime.setText(calendar.getDate().getDateTimeString());
        jtfTime1.setText(""+(new Double(oceantime)).longValue());
        if (pe!=null) tcMapViewer.setOceanTime(pe.getOceanTime());
        logger.info("finished setOceanTime("+j+")");
    }
    
    /**
     * Sets the potential fields lists for the scalar and 
     * the x- and y-components of th vector field based on the
     * fields from the netcdf file.
     */
    private void setVariables() {
        logger.info("starting setVariables()");
        doEvents = false;
        logger.info("setVariables(): previously selected variables = '"+scalarFld+"', '"+vectorFldX+"', '"+vectorFldY+"', '"+horizGradFld+"'.");
        resetVariables();
        if (pe!=null){
            logger.info("setVariables(): reading fields from pe");
            TreeSet<String> fields = new TreeSet<>(pe.getFieldNames());
            Iterator<String> vars = fields.iterator();
            while (vars.hasNext()) {
                String str = vars.next();
                logger.info("setVariables(): adding var "+str);
                jcbScalarVar.addItem(str);
                jcbVectorX.addItem(str);
                jcbVectorY.addItem(str);
                jcbHGradField.addItem(str);
            }
            doEvents = true;
            if ((scalarFld!=null)&&(!scalarFld.equals(""))){
                jcbScalarVar.setSelectedItem(scalarFld);
            }
            if ((vectorFldX!=null)&&(!vectorFldX.equals(""))){
                jcbVectorX.setSelectedItem(vectorFldX);
            }
            if ((vectorFldY!=null)&&(!vectorFldY.equals(""))){
                jcbVectorY.setSelectedItem(vectorFldY);
            }
            if ((horizGradFld!=null)&&(!horizGradFld.equals(""))){
                jcbHGradField.setSelectedItem(horizGradFld);
            }
        }
        logger.info("finished setVariables()");
    }

    private void next() throws IOException{
        if (pe.hasNext()) {
            try {
                pe = pe.next();
                i3d.setPhysicalEnvironment(pe);
                Integer t = (Integer) jspTime.getNextValue();
                if (t != null)  {
                    jspTime.setValue(t);
                    setOceanTime(t-1);//t is 1-based, oceantime is 0-based
                }
                updateMapLayer();
            } catch (ArrayIndexOutOfBoundsException ex) {
                logger.severe(ex.getMessage());
            } catch (IOException ex) {
                logger.severe(ex.getMessage());
            }
        } else {
            logger.info("Trying next netcdf file");
            try {
                tryNext();
                updateMapLayer();
            } catch (IOException ex) {
                logger.severe(ex.getMessage());
                throw new IOException("could not find a subsequent netcdf file",ex);
            }
        }
    }

    /**
     * Method is called internally when reading the "next" PhysicalEnvironment
     * from the netCDF file fails (because there aren't any more time steps in
     * the file).  The method increments the netCDF file name and attempts to
     * get the first PhysicalEnvironment from the new file.
     *
     * @throws java.io.IOException
     */
    protected void tryNext() throws IOException {
        String file_ROMSDataset = netcdfReader.getFilename();
        int n = file_ROMSDataset.length();
        //logger.info("file_ROMSDataset = "+file_ROMSDataset);
        String idx = file_ROMSDataset.substring(n-7,n-3);
        //logger.info("idx = "+idx);
        int ndx = Integer.parseInt(idx);
        char[] idxc = String.valueOf(ndx+1).toCharArray();
        //logger.info("new index = "+String.valueOf(idxc));
        char[] dfnc = file_ROMSDataset.toCharArray();
        for (int i=idxc.length;i>0;i--) {
            dfnc[n-(4+(idxc.length-i))] = idxc[i-1];
        }
        file_ROMSDataset = String.valueOf(dfnc);
        logger.info("Switching to new netCDF file: "+file_ROMSDataset);
        netcdfReader.setNetcdfDataset(file_ROMSDataset);
        pe = new PhysicalEnvironment(0,netcdfReader);
        i3d.setPhysicalEnvironment(pe);
        doEvents = false;
        jfbDataset.setFilename(file_ROMSDataset);
        setOceanTime(0);
        //do not have to call setVariables() here
        repaint();
        doEvents = true;
    }

    private void previous() throws IOException{
        if (pe.hasPrevious()) {
            try {
                pe = pe.previous();
                i3d.setPhysicalEnvironment(pe);
                Integer t = (Integer) jspTime.getPreviousValue();
                if (t != null)  {
                    jspTime.setValue(t);
                    setOceanTime(t-1);//t is 1-based, oceantime is 0-based
                }
                updateMapLayer();
            } catch (ArrayIndexOutOfBoundsException ex) {
                logger.severe(ex.getMessage());
            } catch (IOException ex) {
                logger.severe(ex.getMessage());
            }
        } else {
            logger.info("Trying previous netcdf file");
            try {
                tryPrevious();
                updateMapLayer();
            } catch (IOException ex) {
                logger.severe(ex.getMessage());
                throw new IOException("could not find a subsequent netcdf file",ex);
            }
        }
    }

    /**
     * Method is called internally when reading the "previous" PhysicalEnvironment
     * from the netCDF file fails (because there aren't any more time steps in
     * the file).  The method decrements the netCDF file name and attempts to
     * get the last PhysicalEnvironment from the new file.
     *
     * @throws java.io.IOException
     */
    protected void tryPrevious() throws IOException {
        String file_ROMSDataset = netcdfReader.getFilename();
        int n = file_ROMSDataset.length();
        //logger.info("file_ROMSDataset = "+file_ROMSDataset);
        String idx = file_ROMSDataset.substring(n-7,n-3);
        //logger.info("idx = "+idx);
        int ndx = Integer.parseInt(idx);
        char[] idxc = String.valueOf(ndx-1).toCharArray();
        //logger.info("new index = "+String.valueOf(idxc));
        char[] dfnc = file_ROMSDataset.toCharArray();
        for (int i=idxc.length;i>0;i--) {
            dfnc[n-(4+(idxc.length-i))] = idxc[i-1];
        }
        file_ROMSDataset = String.valueOf(dfnc);
        logger.info("Switching to new netCDF file: "+file_ROMSDataset);
        netcdfReader.setNetcdfDataset(file_ROMSDataset);
        int nt = netcdfReader.getNumTimeSteps();
        pe = new PhysicalEnvironment(nt-1,netcdfReader);
        i3d.setPhysicalEnvironment(pe);
        doEvents = false;
        jfbDataset.setFilename(file_ROMSDataset);
        setOceanTime(nt-1);
        repaint();
        doEvents = true;
    }

    private void updateMapLayer(){
        if (jchkShowSF.isSelected()) {
            updateScalarMapLayer();
        } else {
            if (scalarLayer!=null) {
                logger.info("updateMapLayer(): removing scalar layer from map"+scalarLayer.getTitle());
                tcMapViewer.removeGISLayer(scalarLayer);
                logger.info("updateMapLayer(): removed scalar layer from map"+scalarLayer.getTitle());
            }
        }
        if (jchkShowVF.isSelected()) {
            updateVectorMapLayer();
        } else {
            if (vectorLayer!=null) {
                logger.info("updateMapLayer(): removing vector layer from map"+vectorLayer.getTitle());
                tcMapViewer.removeGISLayer(vectorLayer);
                logger.info("updateMapLayer(): removed vector layer from map"+vectorLayer.getTitle());
            }
        }
        if (jchkShowHGrad.isSelected()) {
            updateHGradMapLayer();
        } else {
            if (horizGradLayer!=null) {
                logger.info("updateMapLayer(): removing HGrad layer from map"+horizGradLayer.getTitle());
                tcMapViewer.removeGISLayer(horizGradLayer);
                logger.info("updateMapLayer(): removed HGrad layer from map"+horizGradLayer.getTitle());
            }
        }
        tcMapViewer.repaint();
    }

    private void updateHGradMapLayer() {
        try {
            logger.info("Updating horiz. gradient layer");
            String strHG = (String)jcbHGradField.getSelectedItem();
            CalendarIF cal = netcdfReader.getCalendar();
            cal.setTimeOffset((long)pe.getOceanTime());
            ModelData md = pe.getField(strHG);
            MapDataInterfaceVectorBase hgmd;
            //remove last map layer
            if (horizGradLayer!=null) tcMapViewer.removeGISLayer(horizGradLayer);
            //create new vector style, if required
            boolean wasNull = false;
            //create feature collection
            FeatureCollection fc;
            if (jpHGradDepthSelector.isVisible()) {
                //ModelData is 3D
                hgmd = new MapDataGradientHorizontal3D(md,i3d,cal.getDate());
                logger.info("created hgmd3D");
                if (horizGradStyle==null){
                    logger.info("--Creating a new horizontal gradient style");
                    FeatureType ft = hgmd.getFeatureType();
                    horizGradStyle = new VectorStyle(ft,"Geometry","magnitude","angle");
                    hgCustomizer.setObject(horizGradStyle);
                    hgCustomizer.repaint();
                    wasNull = true;
                }
                hgmd.setStyle(horizGradStyle);//must set this before creating feature collection
                logger.info("--set style on hgmd3D");
                if (jrbHGradK.isSelected()) {
                    int k = ((Integer)jspHGradK.getValue());
                    fc = ((MapDataGradientHorizontal3D)hgmd).createFeatureCollection(k);
                    logger.info("--created feature collection on layer "+k);
                } else {
                    double d = Double.parseDouble(jtfHGradDepth.getText());
                    fc = ((MapDataGradientHorizontal3D)hgmd).createFeatureCollection(-d);
                    logger.info("--created feature collection at depth "+d);
                }
            } else {
                //ModelData is 2D
                hgmd = new MapDataGradientHorizontal2D(md,cal.getDate());
                logger.info("created hgmd2D");
                if (horizGradStyle==null){
                    logger.info("--Creating a new horizontal gradient style");
                    FeatureType ft = hgmd.getFeatureType();
                    horizGradStyle = new VectorStyle(ft,"Geometry","magnitude","angle");
                    hgCustomizer.setObject(horizGradStyle);
                    hgCustomizer.repaint();
                    wasNull = true;
                }
                hgmd.setStyle(horizGradStyle);//must set this before creating feature collection
                logger.info("--set style on hgmd2D");
                fc = ((MapDataGradientHorizontal2D)hgmd).createFeatureCollection();
                logger.info("--created feature collection");
            }
            double mx = hgmd.getMax();
            NumberFormat frmt = NumberFormat.getNumberInstance();
            frmt.setMinimumFractionDigits(0);
            frmt.setMaximumFractionDigits(10);
            jtfHGradMinMag.setText(frmt.format(hgmd.getMin()));
            jtfHGradMaxMag.setText(frmt.format(hgmd.getMax()));
            if (wasNull||jchkUpdateStyleForHGrad.isSelected()){
                horizGradStyle.setMin(hgmd.getMin());//must be done AFTER feature collection is created
                horizGradStyle.setMax(hgmd.getMax());
            }
            horizGradStyle.updateStyle();
            //create new layer
            horizGradLayer = new DefaultMapLayer(fc,horizGradStyle,hgmd.getName());
            if (horizGradLayer!=null) {
                tcMapViewer.addGISLayer(horizGradLayer);//paint on top
            } else {
                logger.severe("Could not create horizGradLayer");
            }
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        } catch (Exception ex) {
            logger.severe("Error updating horizGrad layer");
            logger.severe(ex.getMessage());
        }
    }

    private void updateVectorMapLayer() {
        try {
            String strX = (String)jcbVectorX.getSelectedItem();
            String strY = (String)jcbVectorY.getSelectedItem();
            logger.info("starting updateVectorMapLayer() for ["+strX+", "+strY+"]");
            CalendarIF cal = netcdfReader.getCalendar();
            cal.setTimeOffset((long)pe.getOceanTime());
            NumberFormat frmt = NumberFormat.getNumberInstance();
            frmt.setMinimumFractionDigits(0);
            frmt.setMaximumFractionDigits(3);
            
            MapDataInterfaceVectorBase vmd;
            //remove last vector map layer
            if (vectorLayer!=null) {
                logger.info("Removing vector layer to map: "+vectorLayer.getTitle());
                tcMapViewer.removeGISLayer(vectorLayer);
                logger.info("Removed vector layer to map: "+vectorLayer.getTitle());
                vectorLayer=null;
            }
            //create new vector style, if required
            boolean wasNull = false;
            //create feature collection
            FeatureCollection fc;
            if (jpVectorDepthSelector.isVisible()) {
                //ModelData is 3D
                vmd = new MapDataVector3D(strX,strY,i3d,cal.getDate());
                logger.info("created 3D vmd");
                if (vectorStyle==null){
                    logger.info("Creating a new vector style");
                    FeatureType ft = vmd.getFeatureType();
                    vectorStyle = new VectorStyle(ft,"Geometry","speed","angle");
                    vfCustomizer.setObject(vectorStyle);
                    vfCustomizer.repaint();
                    wasNull = true;
                } else {
                    if (vectorStyle.mustUpdateStyle()){
                        logger.info("Updating existing vector style");
                        vectorStyle.updateStyle();
                    }
                }
                vmd.setStyle(vectorStyle);//must set this before creating feature collection
                logger.info("set style on vmd");
                if (jrbVectorK.isSelected()) {
                    int k = ((Integer)jspVectorK.getValue()).intValue();
                    logger.info("creating vector feature collection for vertical grid "+k);
                    fc = ((MapDataVector3D)vmd).createFeatureCollection(k);
                } else {
                    double d = Double.parseDouble(jtfVectorDepth.getText());
                    logger.info("creating vector feature collection for depth "+d);
                    fc = ((MapDataVector3D)vmd).createFeatureCollection(-d);
                }
                if (fc==null) logger.severe("updateVectorMapLayer(): Could not create feature collection from MapDataVector3D");
            } else {
                //ModelData is 2D
                vmd = new MapDataVector2D(strX,strY,cal.getDate());
                logger.info("created 2D vmd");
                if (vectorStyle==null){
                    logger.info("Creating a new vector style");
                    FeatureType ft = vmd.getFeatureType();
                    vectorStyle = new VectorStyle(ft,"Geometry","speed","angle");
                    vfCustomizer.setObject(vectorStyle);
                    vfCustomizer.repaint();
                    wasNull = true;
                } else {
                    if (vectorStyle.mustUpdateStyle()){
                        logger.info("Updating existing vector style");
                        vectorStyle.updateStyle();
                    }
                }
                vmd.setStyle(vectorStyle);//must set this before creating feature collection
                logger.info("set style on vmd");
                logger.info("creating 2D vector feature collection");
                fc = ((MapDataVector2D)vmd).createFeatureCollection();
                if (fc==null) logger.severe("updateVectorMapLayer(): Could not create feature collection from MapDataVector2D");
            }
            jtfMinVel.setText(frmt.format(vmd.getMin()));
            jtfMaxVel.setText(frmt.format(vmd.getMax()));
            if (wasNull||jchkUpdateStyleForVF.isSelected()){
                vectorStyle.setMin(vmd.getMin());//must be done AFTER feature collection is created
                vectorStyle.setMax(vmd.getMax());
            }
            vectorStyle.updateStyle();
            //create new layer
            vectorLayer = new DefaultMapLayer(fc,vectorStyle,vmd.getName());
            if (vectorLayer!=null) {
                logger.info("Adding vector layer to map: "+vectorLayer.getTitle());
                tcMapViewer.addGISLayer(vectorLayer);//paint on top
                logger.info("Added vector layer to map: "+vectorLayer.getTitle());
            } else {
                logger.severe("updateVectorMapLayer(): vectorLayer = null");
            }
        } catch (IOException ex) {
            logger.severe("updateVectorMapLayer(): Error updating vector layer: could not read file!");
            logger.severe(ex.getMessage());
        } catch (Exception ex) {
            logger.severe("updateVectorMapLayer(): Error updating vector layer: unspecified");
            logger.severe(ex.toString());
            logger.severe(ex.getMessage());
        }
    }

    private void updateScalarMapLayer() {
        try {
            //remove last scalar data map layer
            if (scalarLayer!=null) {
                logger.info("Removing scalar layer to map: "+scalarLayer.getTitle());
                tcMapViewer.removeGISLayer(scalarLayer);
                logger.info("Removed scalar layer to map: "+scalarLayer.getTitle());
                scalarLayer = null;
            }
            String strFld = (String)jcbScalarVar.getSelectedItem();
            CalendarIF cal = netcdfReader.getCalendar();
            cal.setTimeOffset((long)pe.getOceanTime());
            NumberFormat frmt = NumberFormat.getNumberInstance();
            frmt.setMinimumFractionDigits(0);
            frmt.setMaximumFractionDigits(3);
            FeatureCollection fc = null;
            MapDataInterfaceScalarBase smd;
            
            ModelData md = i3d.getPhysicalEnvironment().getField(strFld);
            if (jpScalarDepthSelector.isVisible()) {
                smd = new MapDataScalar3D(md,i3d,cal.getDate());
                //ModelData is 3D
                if (jrbScalarK.isSelected()) {
                    int k = ((Integer)jspScalarK.getValue()).intValue();
                    logger.info("creating scalar feature collection for vertical layer "+k);
                    fc = ((MapDataScalar3D)smd).createFeatureCollection(k);
                } else {
                    double d = Double.parseDouble(jtfScalarDepth.getText());
                    logger.info("creating scalar feature collection for depth "+d);
                    fc = ((MapDataScalar3D)smd).createFeatureCollection(-d);
                }
            } else {
                //ModelData is 2D
                logger.info("creating 2D scalar feature collection");
                smd = new MapDataScalar2D(md,cal.getDate());
                fc = ((MapDataScalar2D)smd).createFeatureCollection();
            }
            jtfScalarMin.setText(frmt.format(smd.getMin()));
            jtfScalarMax.setText(frmt.format(smd.getMax()));
            //create new layer
            if (jchkUpdateStyleForSF.isSelected()||scalarStyle==null){
                logger.info("Creating new scalar style!");
                FeatureType ft = smd.getFeatureType();
                ColorBarStyle newStyle = new ColorBarStyle(ft,"Geometry","Value");
                if (scalarStyle!=null) {
                    newStyle.setColorRamp(scalarStyle.getColorRamp());
                    newStyle.setNumberOfColors(scalarStyle.getNumberOfColors());
                }
                newStyle.setMin(smd.getMin());
                newStyle.setMax(smd.getMax());
                newStyle.updateStyle();
                scalarStyle = newStyle;
                if (sfCustomizer!=null) sfCustomizer.setObject(scalarStyle);//update the colorbar scale editors
            } else {
                logger.info("Using old scalar style!");
                if (scalarStyle.mustUpdateStyle()) {
                    logger.info("updating scalar style for user changes");
                    scalarStyle.updateStyle();
                }
            }

            scalarLayer = new DefaultMapLayer(fc,scalarStyle,smd.getName());
            if (scalarLayer!=null) {
                logger.info("Adding scalar layer to map: "+scalarLayer.getTitle());
                tcMapViewer.addGISLayerAtBase(scalarLayer);
                logger.info("Added scalar layer to map: "+scalarLayer.getTitle());
            } else {
                logger.severe("Could not create scalarLayer.");
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgScalarDepthButtons;
    private javax.swing.ButtonGroup bgVectorDepthButtons;
    private wts.GIS.styling.VectorStyleCustomizer hgCustomizer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbEditHGradScaling;
    private javax.swing.JButton jbEditVectorScaling;
    private javax.swing.JButton jbNext;
    private javax.swing.JButton jbPrev;
    private javax.swing.JButton jbResetAll;
    private javax.swing.JButton jbScalarColorScale;
    private javax.swing.JButton jbUpdate;
    private javax.swing.JComboBox jcbHGradField;
    private javax.swing.JComboBox jcbScalarVar;
    private javax.swing.JComboBox jcbVectorX;
    private javax.swing.JComboBox jcbVectorY;
    private javax.swing.JCheckBox jchkShowHGrad;
    private javax.swing.JCheckBox jchkShowSF;
    private javax.swing.JCheckBox jchkShowVF;
    private javax.swing.JCheckBox jchkUpdateStyleForHGrad;
    private javax.swing.JCheckBox jchkUpdateStyleForSF;
    private javax.swing.JCheckBox jchkUpdateStyleForVF;
    private com.wtstockhausen.beans.swing.JFilenameBean jfbDataset;
    private javax.swing.JPanel jjpVectorRange;
    private javax.swing.JPanel jpHGrad;
    private javax.swing.JPanel jpHGradDepthSelector;
    private javax.swing.JPanel jpHGradRange;
    private javax.swing.JPanel jpNavigation;
    private javax.swing.JPanel jpOceanTime;
    private javax.swing.JPanel jpScalarDepthSelector;
    private javax.swing.JPanel jpScalarField;
    private javax.swing.JPanel jpScalarRange;
    private javax.swing.JPanel jpVectorDepthSelector;
    private javax.swing.JPanel jpVectorField;
    private javax.swing.JRadioButton jrbHGradDepth;
    private javax.swing.JRadioButton jrbHGradK;
    private javax.swing.JRadioButton jrbScalarDepth;
    private javax.swing.JRadioButton jrbScalarK;
    private javax.swing.JRadioButton jrbVectorDepth;
    private javax.swing.JRadioButton jrbVectorK;
    private javax.swing.JSpinner jspHGradK;
    private javax.swing.JSpinner jspScalarK;
    private javax.swing.JSpinner jspTime;
    private javax.swing.JSpinner jspVectorK;
    private javax.swing.JTextField jtfHGradDepth;
    private javax.swing.JTextField jtfHGradMaxMag;
    private javax.swing.JTextField jtfHGradMinMag;
    private javax.swing.JTextField jtfMaxVel;
    private javax.swing.JTextField jtfMinVel;
    private javax.swing.JTextField jtfScalarDepth;
    private javax.swing.JTextField jtfScalarMax;
    private javax.swing.JTextField jtfScalarMin;
    private javax.swing.JTextField jtfTime;
    private javax.swing.JTextField jtfTime1;
    private javax.swing.JTextField jtfVectorDepth;
    private wts.GIS.styling.ColorBarStyleCustomizer sfCustomizer;
    private wts.GIS.styling.VectorStyleCustomizer vfCustomizer;
    // End of variables declaration//GEN-END:variables

    /**
     * Method called when this TopCOmponent's window is opened in the application.
     * This could happen multiple times before the application is closed.
     */
    @Override
    public void componentOpened() {
        logger.info("starting componentOpened");
        doEvents = false;
        //make sure MapViewerTopComponent is open
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                tcMapViewer = MapViewerTopComponent.findInstance();
                if (!tcMapViewer.isOpened()) tcMapViewer.open();
                tcMapViewer.addPartner();
                if (scalarLayer!=null) tcMapViewer.addGISLayerAtBase(scalarLayer);
                if (vectorLayer!=null) tcMapViewer.addGISLayer(vectorLayer);
                if (horizGradLayer!=null) tcMapViewer.addGISLayer(horizGradLayer);
                tcMapViewer.requestActive();
                requestActive();
            }
        });
        
        if (doOnOpen){
            WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
                @Override
                public void run() {
                    jbUpdate.setEnabled(false);
                    jbPrev.setEnabled(false);
                    jbNext.setEnabled(false);
                    File cdF;
                    if (!romsGI.getCanonicalFile().equals(GlobalInfo.PROP_NotSet)){
                        cdF = new File(romsGI.getCanonicalFile());
                        setCurrentDirectory(cdF);
                    } else if (!romsGI.getGridFile().equals(GlobalInfo.PROP_NotSet)){
                        cdF = new File(romsGI.getGridFile());
                        setCurrentDirectory(cdF);
                    }

                    if (!romsGI.getCanonicalFile().equals(GlobalInfo.PROP_NotSet)){
                        String f = jfbDataset.getFilename();
                        if ((f!=null)&&(!f.isEmpty())) {
                            loadDataset();
                            cdF = new File(jfbDataset.getFilename());
                            setCurrentDirectory(cdF);
                            jbUpdate.setEnabled(true);
                            jbPrev.setEnabled(true);
                            jbNext.setEnabled(true);
                        }
                    }
                }
            });
        }
        doOnOpen = false;
        
        romsGI.addPropertyChangeListener(this);
        sfCustomizer.addPropertyChangeListener(this);
        vfCustomizer.addPropertyChangeListener(this);
        hgCustomizer.addPropertyChangeListener(this);
        doEvents = true;
        logger.info("done componentOpened");
    }

    /**
     * Method called when this TopCOmponent's window is closed in the application.
     * This could happen multiple times before the application is closed.
     */
    @Override
    public void componentClosed() {
        logger.info("starting componentClosed()");
        sfCustomizer.removePropertyChangeListener(this);
        vfCustomizer.removePropertyChangeListener(this);
        hgCustomizer.removePropertyChangeListener(this);
        romsGI.removePropertyChangeListener(this);
        if (scalarLayer!=null) tcMapViewer.removeGISLayer(scalarLayer);
        if (vectorLayer!=null) tcMapViewer.removeGISLayer(vectorLayer);
        if (horizGradLayer!=null) tcMapViewer.removeGISLayer(horizGradLayer);
        tcMapViewer.removePartner();
        if (tcMapViewer.canClose()) tcMapViewer.close();
        logger.info("finished componentClosed()");
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        logger.info("Writing properties");
        String str;
        p.setProperty("version", "1.0");
        str = romsGI.getGridFile();
        if (str!=null) p.setProperty("ROMS_GridFile", str);
        str = jfbDataset.getFilename();
        if (str!=null) p.setProperty("ROMS_Dataset", str);
        str = jspTime.getValue().toString();
        if (str!=null) p.setProperty("ocean_time_index", str);
        str = (String)jcbScalarVar.getSelectedItem();
        if (str!=null) p.setProperty("ScalarFieldName",str);
        str = (String)jcbVectorX.getSelectedItem();
        if (str!=null) p.setProperty("VectorXFieldName",str);
        str = (String)jcbVectorY.getSelectedItem();
        if (str!=null) p.setProperty("VectorYFieldName",str);
        str = (String)jcbHGradField.getSelectedItem();
        if (str!=null) p.setProperty("HorizGradFieldName",str);
    }

    void readProperties(java.util.Properties p) {
        logger.info("Reading properties");
        String version = p.getProperty("version");
        String val;
        doEvents = false;
        val = p.getProperty("ROMS_GridFile");    
        if ((val!=null)&&!val.equals(GlobalInfo.PROP_NotSet)) {
            val = p.getProperty("ROMS_Dataset");       if (val!=null) jfbDataset.setFilename(val);
            val = p.getProperty("ocean_time_index");   if (val!=null) otIndex = Integer.parseInt(val);
            val = p.getProperty("ScalarFieldName");    if (val!=null) {jcbScalarVar.addItem(val);  jcbScalarVar.setSelectedItem(val); scalarFld   =val;}
            val = p.getProperty("VectorXFieldName");   if (val!=null) {jcbVectorX.addItem(val);    jcbVectorX.setSelectedItem(val);   vectorFldX  =val;}
            val = p.getProperty("VectorYFieldName");   if (val!=null) {jcbVectorY.addItem(val);    jcbVectorY.setSelectedItem(val);   vectorFldY  =val;}
            val = p.getProperty("HorizGradFieldName"); if (val!=null) {jcbHGradField.addItem(val); jcbHGradField.setSelectedItem(val);horizGradFld=val;}
        }
        doEvents = true;
        logger.info("Done reading properties");
    }

    /**
     * Sets the current directory for file lookups (jfcFiles and jfbDataset)
     * to that of the input file.
     * 
     * @param f - the file from which to set the current directory
     */
    public void setCurrentDirectory(File f){
        jfbDataset.setCurrentDirectory(f);
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        logger.info("PropertyChangeEvent: "+evt.toString());
        if (evt.getPropertyName().equals(GlobalInfo.PROP_CanonicalFile)){
            jfbDataset.setEnabled(false);
            jbUpdate.setEnabled(false);
            jbPrev.setEnabled(false);
            jbNext.setEnabled(false);
            if (romsGI.getCanonicalFile().equals(GlobalInfo.PROP_NotSet)){
            } else {
                if (isOpened()){
                    logger.info("ROMS Canonical file changed--loading 3D info");
                    ModelGrid3D mg = romsGI.getGrid3D();
                    if (!mg.hasVerticalGridInfo()) {
                        doOnOpen = true;
                    } else {
                        File f = new File(romsGI.getCanonicalFile());
                        setCurrentDirectory(f);
                        jfbDataset.setEnabled(true);
                    }
                }
            }
        }
    }
}
