/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.ModelGridViewer;

import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.filter.IllegalFilterException;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
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
import wts.GIS.styling.ColorBarStyleCustomizer;
import wts.roms.gis.MapDataScalar2D;
import wts.roms.model.GlobalInfo;
import wts.roms.model.ModelData;
import wts.roms.topcomponents.MapViewer.MapViewerTopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//wts.roms.topcomponents.ModelGridViewer//ModelGridViewer//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ModelGridViewerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "wts.roms.topcomponents.ModelGridViewer.ModelGridViewerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ModelGridViewerAction",
        preferredID = "ModelGridViewerTopComponent"
)
@Messages({
    "CTL_ModelGridViewerAction=ModelGridViewer",
    "CTL_ModelGridViewerTopComponent=ModelGridViewer Window",
    "HINT_ModelGridViewerTopComponent=This is a ModelGridViewer window"
})
public final class ModelGridViewerTopComponent extends TopComponent implements PropertyChangeListener {
    
    /** singleton object */
    private static ModelGridViewerTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "ModelGridViewerTopComponent";
    /** */
    private static final Logger logger = Logger.getLogger(PREFERRED_ID);

    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized ModelGridViewerTopComponent getDefault(){
        if (instance==null){
            instance = new ModelGridViewerTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized ModelGridViewerTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof ModelGridViewerTopComponent) {
            return (ModelGridViewerTopComponent) win;
        }
        logger.warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }
    
    /** indicates whether to pay attention to events */
    private boolean doEvents = true;

    /** name of scalar field to map */
    private String sclrFld = null;
    /** feature collection for the scalar map layer */
    private FeatureCollection scalarFC = null;
    /**style for the scalar map layer */
    private ColorBarStyle scalarStyle = null;
    /** the scalar map layer */
    private MapLayer scalarLayer = null;

    /** file chooser for output shapefiles */
    JFileChooser jFC = null;
    
    /** singleton instance of GlobalInfo */
    private GlobalInfo romsGI;
    /** ROMS grid file */
    private String gridFile = GlobalInfo.PROP_NotSet;
    
    /** the singleton instance of the MapViewerTopComponent */
    private MapViewerTopComponent tcMapViewer = null;
    
    /** instance content reflecting Loader and Saver capabilities */
    private final InstanceContent content;
    

    public ModelGridViewerTopComponent() {
        logger.info("INSTANTIATING ModelGridViewerTopComponent");
        initComponents();
        initComponents1();
        setName(Bundle.CTL_ModelGridViewerTopComponent());
        setToolTipText(Bundle.HINT_ModelGridViewerTopComponent());

        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect csvSaver, csvLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
        logger.info("Finished INSTANTIATING ModelGridViewerTopComponent");
    }

    private void initComponents1() {
        logger.info("Starting initCompnents1");
        romsGI = GlobalInfo.getInstance();
    
        sfCustomizer.setVisible(false);//set invisible, initially
        logger.info("Finished initCompnents1");
    }
    
    /**
     * Sets the potential fields lists for the scalar and 
     * the x- and y-components of th vector field based on the
     * fields from the netcdf file.
     */
    private void setVariables() {
        logger.info("setVariables()");
        doEvents = false;
        logger.info("setVariables(): previously selected variable = '"+sclrFld+"'.");
        jcbScalarVar.removeAllItems();
        jcbScalarVar.setSelectedIndex(-1);//set to "no item"
        if (romsGI.getGrid()!=null){
            TreeSet<String> fields = new TreeSet<>(romsGI.getGrid().getFieldNames());
            Iterator<String> vars = fields.iterator();
            while (vars.hasNext()) {
                String str = vars.next();
                jcbScalarVar.addItem(str);
            }
            jcbScalarVar.setSelectedIndex(-1);
            sclrFld = null;
//            doEvents = true;
//            if ((sclrFld!=null)&&(!sclrFld.equalsIgnoreCase(""))){
//                jcbScalarVar.setSelectedItem(sclrFld);
//            }
            jcbScalarVar.repaint();
        } else {
            logger.warning("SETTING VARIABLES but grid is NULL!!");
        }
        doEvents = true;//make sure doEvents is true (@TODO: SHOULD it be?)
    }
    
    /**
     * Updates the GUI variables to be consistent with the current 
     * ROMS grid file (via GlobalInfo.getGridFile()), depending on what the new file is.
     *  1. if the new file is the same as the old file, then nothing is done
     *  2. if the file is different, then the scalarLayer is removed from the MapViewer
     *      a. if the file is "--not set--", the scalar variables selector (jcbScalarVar)
     *          is cleared and the style customizer is hidden
     *      b. otherwise setVairables() is called to update the scalar variables selector
     * This is called in response to a PropertyChangeEvent.
     */
    private void updateGridInfo(){
        logger.info("Starting setGridFile(). old file = '"+gridFile+"'");
        if (romsGI.getGridFile().equals(gridFile)){
            //do nothing--no real change
            logger.info("Did nothing: old = new");
        } else {
            //grid file has changed
            gridFile = romsGI.getGridFile();//update attribute
            logger.info("new file = '"+gridFile+"'");
            doEvents = false;
            //remove scalar layer from map
            if (scalarLayer!=null){
                 tcMapViewer.removeGISLayer(scalarLayer);
                 sclrFld = null;
                 scalarFC = null;
                 scalarLayer = null;
            }
            if (gridFile.equals(GlobalInfo.PROP_NotSet)){
                //grid file not set to valid file yet
                logger.info("Grid file not set yet, so removing vars");
                jcbScalarVar.removeAllItems();
                jcbScalarVar.setSelectedIndex(-1);
                sfCustomizer.setVisible(false);
                repaint();
            } else {
                //assume grid file is valid, so
                setVariables();
            }
        }
    }
    
    /**
     *  1. calls updateGridInfo() to check for changes to the ROMS grid file while the component
      was closed.
  2.opens the MapViewer and adds the scalarLayer (if it exists) to the map
  3. identifies itself as a PropertyChangeListener on the ROMS GloballInfo instance.
     */
    @Override
    public void componentOpened() {
        logger.info("starting componentOpened(): roms gid file = "+romsGI.getGridFile());
        Cursor c = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        doEvents = false;
        updateGridInfo();
        doEvents = false;
        
        //make sure MapViewerTopComponent is open
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                tcMapViewer = MapViewerTopComponent.findInstance();
                if (!tcMapViewer.isOpened()) tcMapViewer.open();
                tcMapViewer.addPartner();
                if (scalarLayer!=null) tcMapViewer.addGISLayerAtBase(scalarLayer);
                tcMapViewer.requestActive();
                requestActive();
            }
        });
        
        setCursor(c);
        doEvents = true;
        romsGI.addPropertyChangeListener(this);
        sfCustomizer.addPropertyChangeListener(this);
        logger.info("done componentOpened");
    }

    /**
     *  1. stops listening to PopertyChangeEvents from the ROMS GlobalInfo instance
     *  2. removes the scalar layer from the MapViewer top component (TODO: should this occur?)
     *  3. removes itself as a partner of the MapViewer top component
     */
    @Override
    public void componentClosed() {
        logger.info("starting componentClosed()");
        sfCustomizer.removePropertyChangeListener(this);
        romsGI.removePropertyChangeListener(this);
        if (scalarLayer!=null) tcMapViewer.removeGISLayer(scalarLayer);
        tcMapViewer.removePartner();
        if (tcMapViewer.canClose()) tcMapViewer.close();
        logger.info("finished componentClosed()");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jcbScalarVar = new javax.swing.JComboBox();
        jpScalarRange = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jtfScalarMin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jtfScalarMax = new javax.swing.JTextField();
        jbScalarColorScale = new javax.swing.JButton();
        sfCustomizer = new wts.GIS.styling.ColorBarStyleCustomizer();
        jbRefreshMap = new javax.swing.JButton();

        jcbScalarVar.setToolTipText(org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jcbScalarVar.toolTipText")); // NOI18N
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

        jpScalarRange.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jpScalarRange.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jLabel3.text")); // NOI18N

        jtfScalarMin.setEditable(false);
        jtfScalarMin.setText(org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jtfScalarMin.text")); // NOI18N
        jtfScalarMin.setToolTipText(org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jtfScalarMin.toolTipText")); // NOI18N
        jtfScalarMin.setEnabled(false);
        jtfScalarMin.setPreferredSize(new java.awt.Dimension(75, 22));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jLabel4.text")); // NOI18N

        jtfScalarMax.setEditable(false);
        jtfScalarMax.setText(org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jtfScalarMax.text")); // NOI18N
        jtfScalarMax.setToolTipText(org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jtfScalarMax.toolTipText")); // NOI18N
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

        org.openide.awt.Mnemonics.setLocalizedText(jbScalarColorScale, org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jbScalarColorScale.text")); // NOI18N
        jbScalarColorScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbScalarColorScaleActionPerformed(evt);
            }
        });

        sfCustomizer.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                sfCustomizerPropertyChange(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jbRefreshMap, org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jbRefreshMap.text")); // NOI18N
        jbRefreshMap.setActionCommand(org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jbRefreshMap.actionCommand")); // NOI18N
        jbRefreshMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRefreshMapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbScalarVar, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpScalarRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jbScalarColorScale)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbRefreshMap))
                    .addComponent(sfCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jcbScalarVar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpScalarRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbScalarColorScale)
                    .addComponent(jbRefreshMap))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sfCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jcbScalarVarItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbScalarVarItemStateChanged
        logger.info("jcbScalarVarItemStateChanged()");
    }//GEN-LAST:event_jcbScalarVarItemStateChanged

    private void jcbScalarVarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbScalarVarActionPerformed
        logger.info("jcbScalarVarActionPerformed()");
        if (doEvents) {
            try {
                Object obj = jcbScalarVar.getSelectedItem();
                if (obj instanceof String) {
                    String strFldNew = (String) obj;//name of selected model field
                    if (!strFldNew.equals(sclrFld)) {
                        sclrFld = strFldNew;
                        createScalarMapLayer();
                    }
                }
            } catch (Exception ex) {
                logger.severe(ex.getMessage());
            }
        }
    }//GEN-LAST:event_jcbScalarVarActionPerformed

    private void jbScalarColorScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbScalarColorScaleActionPerformed
        logger.info("jbScalarColorScaleActionPerformed: "+evt.toString());
        if (evt.getActionCommand().equals("Edit color scale")&&(scalarLayer!=null)) {
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

    private void sfCustomizerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_sfCustomizerPropertyChange
        logger.info("starting sfCustomizerPropertyChange: "+evt.toString());
        if ((scalarStyle!=null)&&scalarStyle.mustUpdateStyle()) {
            try {
                logger.info("updating scalar style for user changes "+scalarStyle.toString());
                scalarStyle.updateStyle();
                tcMapViewer.repaint();
            } catch (IllegalFilterException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }//GEN-LAST:event_sfCustomizerPropertyChange

    private void jbRefreshMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRefreshMapActionPerformed
        logger.info("starting jbRefreshMapActionPerformed");
        logger.info("ModelGridViewer: "+ ((PropertyChangeListener)this).toString());
        logger.info("scalarStyle: "+scalarStyle.toString());
        tcMapViewer.refreshMap();//<-removes scalarLayer, but grid layers still exist and transformation from mouse ccords to grid and bathymetry still works 
        if (scalarLayer!=null) {
            try {
                scalarStyle.updateStyle();
                scalarLayer = new DefaultMapLayer(scalarFC,scalarStyle,sclrFld);
                tcMapViewer.addGISLayerAtBase(scalarLayer);
            } catch (IllegalFilterException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        tcMapViewer.repaint();
        logger.info("finished jbRefreshMapActionPerformed()");
    }//GEN-LAST:event_jbRefreshMapActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbRefreshMap;
    private javax.swing.JButton jbScalarColorScale;
    private javax.swing.JComboBox jcbScalarVar;
    private javax.swing.JPanel jpScalarRange;
    private javax.swing.JTextField jtfScalarMax;
    private javax.swing.JTextField jtfScalarMin;
    private wts.GIS.styling.ColorBarStyleCustomizer sfCustomizer;
    // End of variables declaration//GEN-END:variables

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
     * Reacts to the following PropertyChangeEvents:
     *  1. GlobalInfo.PROP_GridFile
      calls updateGridInfo() to update the scalar variables selector and scalar layer
  2. ColorBarStyle.PROP_ELEMENT
     *      
     * @param pce 
     */
    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        logger.info("PropertyChangeEvent caught: "+pce.toString());
        if (pce.getPropertyName().equals(GlobalInfo.PROP_GridFile)){
            doEvents = false;
            updateGridInfo();
            doEvents = true;
        } else
        if (pce.getPropertyName().equals(ColorBarStyle.PROP_ELEMENT)){
            logger.info("ColorBarStyler PropertyCChange: layer style!!");
            try {
                scalarStyle.updateStyle();
            } catch (IllegalFilterException ex) {
                Exceptions.printStackTrace(ex);
            }
            tcMapViewer.resetMap();
            if (scalarFC!=null) scalarLayer = new DefaultMapLayer(scalarFC,scalarStyle,sclrFld);
            if (scalarLayer!=null) {
                tcMapViewer.addGISLayerAtBase(scalarLayer);
            } else {
                logger.severe("Could not create scalarLayer.");
            }
            logger.info("Changed layer style?!");
        } else 
        if (pce.getPropertyName().equals(ColorBarStyleCustomizer.PROP_StyleChanged)){
            logger.info("sfCustomizer PropertyCChange: layer style!!");
            try {
                if (scalarStyle.mustUpdateStyle()) scalarStyle.updateStyle();
            } catch (IllegalFilterException ex) {
                Exceptions.printStackTrace(ex);
            }
            tcMapViewer.resetMap();
            if (scalarFC!=null) scalarLayer = new DefaultMapLayer(scalarFC,scalarStyle,sclrFld);
            if (scalarLayer!=null) {
                tcMapViewer.addGISLayerAtBase(scalarLayer);
            } else {
                logger.severe("Could not create scalarLayer.");
            }
           logger.info("Changed layer style?!");
        }    
    }

    private void createScalarMapLayer() {
        logger.info("Starting createScalarMapLayer()");
        Cursor c = this.getCursor();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (instance==null){
            logger.warning("instance is NULL!!");
        }
        Runnable r = new Runnable(){
            @Override
            public void run() {
                try {
                    //remove last scalar data map layer
                    if (scalarLayer!=null) {
                        tcMapViewer.removeGISLayer(scalarLayer);
                        scalarFC    = null;
                        scalarLayer = null;
                    }
                    ModelData md = romsGI.getGrid().getGridField(sclrFld);
                    MapDataScalar2D smd = new MapDataScalar2D(md);
                    NumberFormat frmt = NumberFormat.getNumberInstance();
                    frmt.setMinimumFractionDigits(0);
                    frmt.setMaximumFractionDigits(3);

                    logger.info("creating 2D scalar feature collection");
                    scalarFC =  smd.createFeatureCollection();

                    jtfScalarMin.setText(frmt.format(smd.getMin()));
                    jtfScalarMax.setText(frmt.format(smd.getMax()));
                    //create new layer
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
                        logger.info("new scalarStyle = "+scalarStyle.toString());
                        if (sfCustomizer!=null) sfCustomizer.setObject(scalarStyle);//update the colorbar scale editors

                    scalarLayer = new DefaultMapLayer(scalarFC,scalarStyle,sclrFld);
                    if (scalarLayer!=null) {
                        tcMapViewer.addGISLayerAtBase(scalarLayer);
                    } else {
                        logger.severe("Could not create scalarLayer.");
                    }
                    tcMapViewer.repaint();
                } catch (Exception ex) {
                    logger.severe(ex.getMessage());
                }
            }
        };
        ProgressUtils.showProgressDialogAndRun(r, "Creating scalar map layer...");
        this.setCursor(c);
        logger.info("Finished createScalarMapLayer()");
    }
}
