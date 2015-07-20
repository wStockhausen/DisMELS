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
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.filter.IllegalFilterException;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
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
    


    private boolean doEvents = true;//flag indicates whether to pay attention to events

    private String strFld = null;
    private ColorBarStyle scalarStyle = null;
    private MapLayer scalarLayer      = null;

    /** file chooser for output shapefiles */
    JFileChooser jFC = null;
    
    /** singleton instance of GlobalInfo */
    private GlobalInfo romsGI;
    
    /** the singleton instance of the MapViewerTopComponent */
    private MapViewerTopComponent tcMapViewer = null;
    
    /** instance content reflecting Loader and Saver capabilities */
    private final InstanceContent content;
    
    /** flag to perform operations when componentOpened() is called */
    private boolean doOnOpen = true;
    

    public ModelGridViewerTopComponent() {
        initComponents();
        initComponents1();
        setName(Bundle.CTL_ModelGridViewerTopComponent());
        setToolTipText(Bundle.HINT_ModelGridViewerTopComponent());

        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect csvSaver, csvLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
    }

    private void initComponents1() {
        doOnOpen = true;
        romsGI = GlobalInfo.getInstance();
        romsGI.addPropertyChangeListener(this);
        
        sfCustomizer.setVisible(false);//set invisible, initially
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

        jtfScalarMin.setText(org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jtfScalarMin.text")); // NOI18N
        jtfScalarMin.setToolTipText(org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jtfScalarMin.toolTipText")); // NOI18N
        jtfScalarMin.setEnabled(false);
        jtfScalarMin.setPreferredSize(new java.awt.Dimension(75, 22));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(ModelGridViewerTopComponent.class, "ModelGridViewerTopComponent.jLabel4.text")); // NOI18N

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

        sfCustomizer.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbScalarVar, javax.swing.GroupLayout.PREFERRED_SIZE, 447, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpScalarRange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbScalarColorScale)
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
                .addComponent(jbScalarColorScale)
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
                    if (!strFldNew.equals(strFld)) {
                        strFld = strFldNew;
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
        if (scalarStyle.mustUpdateStyle()) {
            try {
                logger.info("updating scalar style for user changes");
                scalarStyle.updateStyle();
                tcMapViewer.repaint();
            } catch (IllegalFilterException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }//GEN-LAST:event_jbScalarColorScaleActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbScalarColorScale;
    private javax.swing.JComboBox jcbScalarVar;
    private javax.swing.JPanel jpScalarRange;
    private javax.swing.JTextField jtfScalarMax;
    private javax.swing.JTextField jtfScalarMin;
    private wts.GIS.styling.ColorBarStyleCustomizer sfCustomizer;
    // End of variables declaration//GEN-END:variables
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
                tcMapViewer.requestActive();
                requestActive();
            }
        });
        doOnOpen = false;
        
        doEvents = true;
        logger.info("done componentOpened");
    }

    @Override
    public void componentClosed() {
        logger.info("starting componentClosed()");
        if (scalarLayer!=null) tcMapViewer.removeGISLayer(scalarLayer);
        tcMapViewer.removePartner();
        if (tcMapViewer.canClose()) tcMapViewer.close();
        logger.info("finished componentClosed()");
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
    public void propertyChange(PropertyChangeEvent pce) {
        logger.info("PropertyChangeEvent caught: "+pce.toString());
    }

    private void createScalarMapLayer() {
        Cursor c = this.getCursor();
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            //remove last scalar data map layer
            if (scalarLayer!=null) {
//                scalarStyle = (ColorBarStyle) scalarLayer.getStyle();
                tcMapViewer.removeGISLayer(scalarLayer);
                scalarLayer = null;
            }
            ModelData md = romsGI.getGrid().getGridField(strFld);
            MapDataScalar2D smd = new MapDataScalar2D(md);
            NumberFormat frmt = NumberFormat.getNumberInstance();
            frmt.setMinimumFractionDigits(0);
            frmt.setMaximumFractionDigits(3);
            
            logger.info("creating 2D scalar feature collection");
            FeatureCollection fc =  smd.createFeatureCollection();
            
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
                if (sfCustomizer!=null) sfCustomizer.setObject(scalarStyle);//update the colorbar scale editors

            scalarLayer = new DefaultMapLayer(fc,scalarStyle,smd.getName());
            if (scalarLayer!=null) {
                tcMapViewer.addGISLayerAtBase(scalarLayer);
            } else {
                logger.severe("Could not create scalarLayer.");
            }
            tcMapViewer.repaint();
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        }
        this.setCursor(c);
    }
}
