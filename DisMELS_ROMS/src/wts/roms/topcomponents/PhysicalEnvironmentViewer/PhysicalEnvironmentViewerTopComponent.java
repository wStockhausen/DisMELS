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
import javax.swing.SpinnerNumberModel;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
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
import wts.roms.gis.MapDataGradientHorizontal;
import wts.roms.gis.MapDataScalar;
import wts.roms.gis.MapDataVector;
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
    "CTL_PhysicalEnvironmentViewerTopComponent=Physical environment viewer",
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
    

    private boolean doEvents = true;//flag indicates whether to pay attention to events

    private NetcdfReader netcdfReader = null;
    private CalendarIF calendar       = null;
    private ModelGrid3D grid3D        = null;
    private PhysicalEnvironment pe    = null;
    private Interpolator3D i3d        = null;

    private MapDataScalar smd         = null;
    private ColorBarStyle scalarStyle = null;
    private MapLayer scalarLayer      = null;

    private MapDataVector vmd       = null;
    private VectorStyle vectorStyle = null;
    private MapLayer vectorLayer    = null;

    private MapDataGradientHorizontal hgmd = null;
    private VectorStyle     horizGradStyle = null;
    private MapLayer        horizGradLayer = null;

    /** singleton instance of GlobalInfo */
    private GlobalInfo globalInfo;
    
    /** the singleton instance of the MapViewerTopComponent */
    private MapViewerTopComponent tcMapViewer = null;
    
    /** instance content reflecting Loader and Saver capabilities */
    private InstanceContent content;
    
    private int otIndex = 1;

    /** flag to perform operations when componentOpened() is called */
    private boolean doOnOpen = true;
    
    public PhysicalEnvironmentViewerTopComponent() {
        initComponents();
        initComponents1();
        setName(Bundle.CTL_PhysicalEnvironmentViewerTopComponent());
        setToolTipText(Bundle.HINT_PhysicalEnvironmentViewerTopComponent());

        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect csvSaver, csvLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
    }

    private void initComponents1() {
        doOnOpen = true;
        globalInfo = GlobalInfo.getInstance();
        globalInfo.addPropertyChangeListener(this);
        
        jfbDataset.addFileFilter("nc", "netCDF file");
        sfCustomizer.setVisible(false);//set invisible, initially
        vsCustomizer.setVisible(false);
        hgCustomizer.setVisible(false);
        
        jfbDataset.setEnabled(false);
        jbUpdate.setEnabled(false);
        jbPrev.setEnabled(false);
        jbNext.setEnabled(false);
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
        vsCustomizer = new wts.GIS.styling.VectorStyleCustomizer();
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
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jpNavigationLayout.setVerticalGroup(
            jpNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpNavigationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jbUpdate)
                .addComponent(jbPrev, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jbNext, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jtfTime, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
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

        jspScalarK.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(0), null, Integer.valueOf(1)));
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
                .addContainerGap(260, Short.MAX_VALUE))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addComponent(jcbScalarVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 624, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(26, 26, 26)
                    .addComponent(jpScalarDepthSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(539, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(75, 75, 75)
                    .addComponent(jchkUpdateStyleForSF)
                    .addContainerGap(546, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(48, 48, 48)
                    .addComponent(jchkShowSF)
                    .addContainerGap(573, Short.MAX_VALUE)))
            .addGroup(jpScalarFieldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jpScalarFieldLayout.createSequentialGroup()
                    .addGap(115, 115, 115)
                    .addComponent(jpScalarRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(484, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpScalarField.TabConstraints.tabTitle"), jpScalarField); // NOI18N

        jpVectorField.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpVectorField.border.title"))); // NOI18N

        jpVectorDepthSelector.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jpVectorDepthSelector.border.title"))); // NOI18N

        bgVectorDepthButtons.add(jrbVectorK);
        org.openide.awt.Mnemonics.setLocalizedText(jrbVectorK, org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbVectorK.text")); // NOI18N
        jrbVectorK.setToolTipText(org.openide.util.NbBundle.getMessage(PhysicalEnvironmentViewerTopComponent.class, "PhysicalEnvironmentViewerTopComponent.jrbVectorK.toolTipText")); // NOI18N

        jspVectorK.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(0), null, Integer.valueOf(1)));
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
                    .addComponent(vsCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 445, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(vsCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jspHGradK.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(0), null, Integer.valueOf(1)));
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
                .addGap(0, 265, Short.MAX_VALUE))
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
                .addContainerGap(778, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jfbDataset, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 806, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(82, 82, 82)
                    .addComponent(jpOceanTime, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(696, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(160, 160, 160)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 681, Short.MAX_VALUE)
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
     * Loads the 3D model info from the canonical dataset and creates the grid3D
     * object.
     */
    private void load3DInfo(){
        logger.info("Reading 3D model info");
        ProgressRunnable<Integer> r = new ProgressRunnable<Integer>(){
            @Override
            public Integer run(ProgressHandle handle) {
                try {
                    //read grid
                    grid3D = new ModelGrid3D(globalInfo.getGridFile()); 
                    //read canonical dataset from netcdf file
                    netcdfReader = new NetcdfReader(globalInfo.getCanonicalFile());
                    calendar = netcdfReader.getCalendar();
                    grid3D.readConstantFields(netcdfReader);
                    jfbDataset.setEnabled(true);
                    validate();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
                return new Integer(0);
            }

        };
        ProgressHandle h = ProgressHandleFactory.createHandle("Reading ROMS 3D model info...");
        ProgressUtils.showProgressDialogAndRunLater(r, h, false);
    }

    /**
     * Loads the ROMS model dataset specified by jfbDatset.getFilename().
     */
    private void loadDataset(){
        logger.info("loading dataset");
        ProgressRunnable<Integer> r = new ProgressRunnable<Integer>(){
            @Override
            public Integer run(ProgressHandle handle) {
                try {
                    //read dataset from netcdf file
                    netcdfReader = new NetcdfReader(jfbDataset.getFilename());
                    pe = new PhysicalEnvironment(0, netcdfReader, grid3D);
                    i3d = new Interpolator3D(pe);
                    setOceanTime(0);
                    setVariables();
                    jbUpdate.setEnabled(true);
                    jbPrev.setEnabled(true);
                    jbNext.setEnabled(true);
                    repaint();
                } catch (IOException ex) {
                    logger.severe(ex.getMessage());
                }
                return new Integer(0);
            }

        };
        ProgressHandle h = ProgressHandleFactory.createHandle("Reading ROMS model dataset...");
        ProgressUtils.showProgressDialogAndRunLater(r, h, false);
    }
    
    /**
     * Updates the oceantime presented by the jtfTime and jtfTime1 text boxes.
     * This DOES NOT update the map viewer date (because it does not update the
     * map layers).
     * 
     * @param evt 
     */
    private void jspTimeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jspTimeStateChanged
        //update the oceantime shown in the text fields 
        //use update() to update map to this time
        int i = ((Integer) jspTime.getValue()).intValue() - 1;
        double ot = netcdfReader.getOceanTime(i);
        calendar.setTimeOffset((long) ot);
        jtfTime.setText(calendar.getDate().getDateTimeString());
        jtfTime1.setText(""+(new Double(ot).longValue()));
    }//GEN-LAST:event_jspTimeStateChanged

    private void jcbScalarVarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbScalarVarItemStateChanged
        //do nothing
    }//GEN-LAST:event_jcbScalarVarItemStateChanged

    private void jcbScalarVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbScalarVarActionPerformed
        if (doEvents) {
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
            logger.info(scalarStyle.toString());
            sfCustomizer.setObject(scalarStyle);
            sfCustomizer.setVisible(true);
            logger.info(scalarStyle.toString());
            jbScalarColorScale.setActionCommand("Hide color scale");
            jbScalarColorScale.setText("Hide color scale");
            validate();
        } else {
            jbScalarColorScale.setActionCommand("Edit color scale");
            jbScalarColorScale.setText("Edit color scale");
            sfCustomizer.setVisible(false);
            validate();
        }
    }//GEN-LAST:event_jbScalarColorScaleActionPerformed

    private void jcbVectorXItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbVectorXItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbVectorXItemStateChanged

    private void jcbVectorXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbVectorXActionPerformed
        if (doEvents) {
            try {
                boolean hasK = false;//assume no vertical dimension for selected field
                Object obj = jcbVectorX.getSelectedItem();
                if (obj instanceof String) {
                    String strFld = (String) obj;//name of selected model field
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
//                if (obj instanceof String) {
//                    String strFld = (String) obj;//name of selected model field
//                    Variable v = netcdfReader.findVariable(strFld);
//                    List dims = v.getDimensionsAll();
//                    ListIterator it = dims.listIterator();
//                    logger.info("Vector X field name: " + strFld);
//                    Dimension vertDim = null;
//                    while (it.hasNext()) {
//                        Dimension d = (Dimension) it.next();
//                        String str = d.getName();
//                        logger.info("\tDimension name: " + str);
//                        if (str.startsWith("s")) {
//                            hasK = true;
//                            vertDim = d;
//                        }
//                    }
//                    this.jpVectorDepthSelector.setEnabled(hasK);
//                    this.jpVectorDepthSelector.setVisible(hasK);
//                    if (hasK) {
//                        int vertType = ModelTypes.getInstance().getVertPosType(vertDim.getName());
//                        SpinnerNumberModel nm = (SpinnerNumberModel) jspVectorK.getModel();
//                        nm.setStepSize(Integer.valueOf(1));
//                        nm.setMaximum(Integer.valueOf(grid3D.getN()));//max index of cells or layers
//                        if (vertType == ModelTypes.VERT_POSTYPE_W) {
//                            nm.setMinimum(Integer.valueOf(0));//layers start at 0
//                            nm.setValue(Integer.valueOf(0));
//                        } else {
//                            nm.setMinimum(Integer.valueOf(1));//layers start at 0
//                            nm.setValue(Integer.valueOf(1));
//                        }
//                    }
//                }
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
            try {
                boolean hasK = false;//assume no vertical dimension for selected field
                Object obj = jcbVectorY.getSelectedItem();
                if (obj instanceof String) {
                    String strFld = (String) obj;//name of selected model field
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
//                if (obj instanceof String) {
//                    String strFld = (String) obj;//name of selected model field
//                    Variable v = netcdfReader.findVariable(strFld);
//                    List dims = v.getDimensionsAll();
//                    ListIterator it = dims.listIterator();
//                    logger.info("Vector Y field name: " + strFld);
//                    Dimension vertDim = null;
//                    while (it.hasNext()) {
//                        Dimension d = (Dimension) it.next();
//                        String str = d.getName();
//                        logger.info("\tDimension name: " + str);
//                        if (str.startsWith("s")) {
//                            hasK = true;
//                            vertDim = d;
//                        }
//                    }
//                    this.jpVectorDepthSelector.setEnabled(hasK);
//                    this.jpVectorDepthSelector.setVisible(hasK);
//                    if (hasK) {
//                        int vertType = ModelTypes.getInstance().getVertPosType(vertDim.getName());
//                        SpinnerNumberModel nm = (SpinnerNumberModel) jspVectorK.getModel();
//                        nm.setStepSize(Integer.valueOf(1));
//                        nm.setMaximum(Integer.valueOf(grid3D.getN()));//max index of cells or layers
//                        if (vertType == ModelTypes.VERT_POSTYPE_W) {
//                            nm.setMinimum(Integer.valueOf(0));//layers start at 0
//                            nm.setValue(Integer.valueOf(0));
//                        } else {
//                            nm.setMinimum(Integer.valueOf(1));//layers start at 0
//                            nm.setValue(Integer.valueOf(1));
//                        }
//                    }
//                }
            } catch (Exception ex) {
                logger.severe(ex.getMessage());
            }
        }
    }//GEN-LAST:event_jcbVectorYActionPerformed

    private void jbEditVectorScalingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEditVectorScalingActionPerformed
        if (evt.getActionCommand().equals("Edit vector scaling")&&(vectorLayer!=null)) {
            vectorStyle = (VectorStyle) vectorLayer.getStyle();
            logger.info(vectorStyle.toString());
            vsCustomizer.setObject(vectorStyle);
            vsCustomizer.setVisible(true);
            logger.info(vectorStyle.toString());
            jbEditVectorScaling.setActionCommand("Hide vector scaling");
            jbEditVectorScaling.setText("Hide vector scaling");
            validate();
            return;
        }
        if (evt.getActionCommand().equals("Hide vector scaling")||(vectorLayer==null)) {
            jbEditVectorScaling.setActionCommand("Edit vector scaling");
            jbEditVectorScaling.setText("Edit vector scaling");
            vsCustomizer.setVisible(false);
            validate();
        }
    }//GEN-LAST:event_jbEditVectorScalingActionPerformed

    private void jcbHGradFieldItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbHGradFieldItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbHGradFieldItemStateChanged

    private void jcbHGradFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbHGradFieldActionPerformed
        if (doEvents) {
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
    
    /**
     * Updates oceantime to jth (0-based) time segment in the current netcdf file.
     * The jspTime spinner is set to j+1, the jtfTime, jtfTime1 text boxes are set to 
     * the corresponding values (date string and long), and the map viewer 
     * time display is updated.
     * 
     * @param j 
     */
    private void setOceanTime(int j) {
        int mx = netcdfReader.getNumTimeSteps();
        double oceantime = netcdfReader.getOceanTime(j);
        calendar.setTimeOffset((long)oceantime);
        SpinnerNumberModel snm = new SpinnerNumberModel(j+1,1,mx,1);
        jspTime.setModel(snm);
        jtfTime.setText(calendar.getDate().getDateTimeString());
        jtfTime1.setText(""+(new Double(oceantime)).longValue());
        if (pe!=null) tcMapViewer.setOceanTime(pe.getOceanTime());
    }
    
    /**
     * Sets the potential fields lists for the scalar and 
     * the x- and y-components of th vector field based on the
     * fields from the netcdf file.
     */
    private void setVariables() {
        doEvents = false;
        String selScl = (String) jcbScalarVar.getSelectedItem();
        String selX   = (String) jcbVectorX.getSelectedItem();
        String selY   = (String) jcbVectorY.getSelectedItem();
        String selHG  = (String) jcbHGradField.getSelectedItem();
        logger.info("setVariables(): previously selected variables = '"+selScl+"', '"+selX+"', '"+selY+"', '"+selHG+"'.");
        TreeSet<String> fields = new TreeSet<>(pe.getFieldNames());
        jcbScalarVar.removeAllItems();
        jcbVectorX.removeAllItems();
        jcbVectorY.removeAllItems();
        jcbHGradField.removeAllItems();
        Iterator<String> vars = fields.iterator();
        while (vars.hasNext()) {
            String str = vars.next();
            jcbScalarVar.addItem(str);
            jcbVectorX.addItem(str);
            jcbVectorY.addItem(str);
            jcbHGradField.addItem(str);
        }
        fields = new TreeSet<>(pe.getGrid().getFieldNames());
        vars = fields.iterator();
        while (vars.hasNext()) {
            String str = vars.next();
            jcbScalarVar.addItem(str);
            jcbVectorX.addItem(str);
            jcbVectorY.addItem(str);
            jcbHGradField.addItem(str);
        }
        jcbScalarVar.setSelectedIndex(-1);//set to "no item"
        jcbVectorX.setSelectedIndex(-1);//set to "no item"
        jcbVectorY.setSelectedIndex(-1);//set to "no item"
        jcbHGradField.setSelectedIndex(-1);//set to "no item"
        doEvents = true;
        if ((selScl!=null)&&(!selScl.equals(""))){
            jcbScalarVar.setSelectedItem(selScl);
        }
        if ((selX!=null)&&(!selX.equals(""))){
            jcbVectorX.setSelectedItem(selX);
        }
        if ((selY!=null)&&(!selY.equals(""))){
            jcbVectorY.setSelectedItem(selY);
        }
        if ((selHG!=null)&&(!selHG.equals(""))){
            jcbHGradField.setSelectedItem(selHG);
        }
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
        pe = new PhysicalEnvironment(0,netcdfReader,grid3D);
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
        pe = new PhysicalEnvironment(nt-1,netcdfReader,grid3D);
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
            if (scalarLayer!=null) tcMapViewer.removeGISLayer(scalarLayer);
        }
        if (jchkShowVF.isSelected()) {
            updateVectorMapLayer();
        } else {
            if (vectorLayer!=null) tcMapViewer.removeGISLayer(vectorLayer);
        }
        if (jchkShowHGrad.isSelected()) {
            updateHGradMapLayer();
        } else {
            if (horizGradLayer!=null) tcMapViewer.removeGISLayer(horizGradLayer);
        }
        tcMapViewer.repaint();
    }

    private void updateHGradMapLayer() {
        try {
            logger.info("Updating horiz. gradient layer");
            String strHG = (String)jcbHGradField.getSelectedItem();
            CalendarIF cal = netcdfReader.getCalendar();
            cal.setTimeOffset((long)pe.getOceanTime());
            hgmd = new MapDataGradientHorizontal(strHG,i3d,cal.getDate());
            logger.info("created hgmd");
            //remove last map layer
            if (horizGradLayer!=null) tcMapViewer.removeGISLayer(horizGradLayer);
            //create new vector style, if required
            boolean wasNull = false;
            if (horizGradStyle==null){
                logger.info("Creating a new horizontal gradient style");
                FeatureType ft = hgmd.getFeatureType();
                horizGradStyle = new VectorStyle(ft,"Geometry","magnitude","angle");
                hgCustomizer.setObject(horizGradStyle);
                hgCustomizer.repaint();
                wasNull = true;
            }
            hgmd.setStyle(horizGradStyle);//must set this before creating feature collection
            logger.info("set style on hgmd");
            //create feature collection
            FeatureCollection fc;
            if (jpHGradDepthSelector.isVisible()) {
                //ModelData is 3D
                if (jrbHGradK.isSelected()) {
                    int k = ((Integer)jspHGradK.getValue()).intValue();
                    fc = hgmd.createFeatureCollection(k);
                } else {
                    double d = Double.parseDouble(jtfHGradDepth.getText());
                    fc = hgmd.createFeatureCollection(-d);
                }
            } else {
                //ModelData is 2D
                fc = hgmd.createFeatureCollection();
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
            logger.info("Updating vector layer");
            String strX = (String)jcbVectorX.getSelectedItem();
            String strY = (String)jcbVectorY.getSelectedItem();
            CalendarIF cal = netcdfReader.getCalendar();
            cal.setTimeOffset((long)pe.getOceanTime());
            NumberFormat frmt = NumberFormat.getNumberInstance();
            frmt.setMinimumFractionDigits(0);
            frmt.setMaximumFractionDigits(3);
            vmd = new MapDataVector(strX,strY,i3d,cal.getDate());
            logger.info("created vmd");
            //remove last vector map layer
            if (vectorLayer!=null) tcMapViewer.removeGISLayer(vectorLayer);
            //create new vector style, if required
            boolean wasNull = false;
            if (vectorStyle==null){
                logger.info("Creating a new vector style");
                FeatureType ft = vmd.getFeatureType();
                vectorStyle = new VectorStyle(ft,"Geometry","speed","angle");
                vsCustomizer.setObject(vectorStyle);
                vsCustomizer.repaint();
                wasNull = true;
            } else {
                if (vectorStyle.mustUpdateStyle()){
                    logger.info("Updating existing vector style");
                    vectorStyle.updateStyle();
                }
            }
            vmd.setStyle(vectorStyle);//must set this before creating feature collection
            logger.info("set style on vmd");
            //create feature collection
            FeatureCollection fc;
            if (jpVectorDepthSelector.isVisible()) {
                //ModelData is 3D
                if (jrbVectorK.isSelected()) {
                    int k = ((Integer)jspVectorK.getValue()).intValue();
                    logger.info("creating vector feature collection for vertical grid "+k);
                    fc = vmd.createFeatureCollection(k);
                } else {
                    double d = Double.parseDouble(jtfVectorDepth.getText());
                    logger.info("creating vector feature collection for depth "+d);
                    fc = vmd.createFeatureCollection(-d);
                }
            } else {
                //ModelData is 2D
                logger.info("creating 2D vector feature collection");
                fc = vmd.createFeatureCollection();
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
                tcMapViewer.addGISLayer(vectorLayer);//paint on top
            } else {
                logger.severe("Could not create vectorLayer");
            }
        } catch (IOException ex) {
            logger.severe(ex.getMessage());
        } catch (Exception ex) {
            logger.severe("Error updating vector layer");
            logger.severe(ex.getMessage());
        }
    }

    private void updateScalarMapLayer() {
        try {
            //remove last scalar data map layer
            if (scalarLayer!=null) {
//                scalarStyle = (ColorBarStyle) scalarLayer.getStyle();
                tcMapViewer.removeGISLayer(scalarLayer);
                scalarLayer = null;
            }
            String strFld = (String)jcbScalarVar.getSelectedItem();
            CalendarIF cal = netcdfReader.getCalendar();
            cal.setTimeOffset((long)pe.getOceanTime());
            smd = new MapDataScalar(strFld,i3d,cal.getDate());
            NumberFormat frmt = NumberFormat.getNumberInstance();
            frmt.setMinimumFractionDigits(0);
            frmt.setMaximumFractionDigits(3);
            FeatureCollection fc = null;
            if (jpScalarDepthSelector.isVisible()) {
                //ModelData is 3D
                if (jrbScalarK.isSelected()) {
                    int k = ((Integer)jspScalarK.getValue()).intValue();
                    logger.info("creating scalar feature collection for vertical layer "+k);
                    fc = smd.createFeatureCollection(k);
                } else {
                    double d = Double.parseDouble(jtfScalarDepth.getText());
                    logger.info("creating scalar feature collection for depth "+d);
                    fc = smd.createFeatureCollection(-d);
                }
            } else {
                //ModelData is 2D
                logger.info("creating 2D scalar feature collection");
                fc = smd.createFeatureCollection();
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
                tcMapViewer.addGISLayerAtBase(scalarLayer);
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
    private wts.GIS.styling.VectorStyleCustomizer vsCustomizer;
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
            File cdF;
            if (!globalInfo.getCanonicalFile().equals(GlobalInfo.PROP_NotSet)){
                cdF = new File(globalInfo.getCanonicalFile());
                setCurrentDirectory(cdF);
            } else if (!globalInfo.getGridFile().equals(GlobalInfo.PROP_NotSet)){
                cdF = new File(globalInfo.getGridFile());
                setCurrentDirectory(cdF);
            }

            if (!globalInfo.getCanonicalFile().equals(GlobalInfo.PROP_NotSet)){
                load3DInfo();
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
        doOnOpen = false;
        
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
        val = p.getProperty("ROMS_Dataset");     if (val!=null) jfbDataset.setFilename(val);
        val = p.getProperty("ocean_time_index"); if (val!=null) otIndex = Integer.parseInt(val);
        val = p.getProperty("ScalarFieldName");  if (val!=null) {jcbScalarVar.addItem(val); jcbScalarVar.setSelectedItem(val);}
        val = p.getProperty("VectorXFieldName"); if (val!=null) {jcbVectorX.addItem(val); jcbVectorX.setSelectedItem(val);}
        val = p.getProperty("VectorYFieldName"); if (val!=null) {jcbVectorY.addItem(val); jcbVectorY.setSelectedItem(val);}
        val = p.getProperty("HorizGradFieldName"); if (val!=null) {jcbHGradField.addItem(val); jcbHGradField.setSelectedItem(val);}
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
        if (evt.getPropertyName().equals(GlobalInfo.PROP_CanonicalFile)){
            jfbDataset.setEnabled(false);
            jbUpdate.setEnabled(false);
            jbPrev.setEnabled(false);
            jbNext.setEnabled(false);
            if (!globalInfo.getCanonicalFile().equals(GlobalInfo.PROP_NotSet)){
                if (isOpened()){
                    logger.info("ROMS Canonical file changed--loading 3D info");
                    File f = new File(globalInfo.getCanonicalFile());
                    setCurrentDirectory(f);
                    load3DInfo();
                    jfbDataset.setEnabled(true);
                } else {
                    doOnOpen = true;
                }
            }
        } else if (evt.getPropertyName().equals(GlobalInfo.PROP_GridFile)){
            jfbDataset.setEnabled(false);
            jbUpdate.setEnabled(false);
            jbPrev.setEnabled(false);
            jbNext.setEnabled(false);
            if (!globalInfo.getCanonicalFile().equals(GlobalInfo.PROP_NotSet)){
                if (!isOpened()) doOnOpen = true;//don't change directory
            } else if (!globalInfo.getGridFile().equals(GlobalInfo.PROP_NotSet)){
                if (isOpened()){
                    File f = new File(globalInfo.getGridFile());
                    setCurrentDirectory(f);
                } else {
                    doOnOpen = true;
                }
            }
        }
    }
}
