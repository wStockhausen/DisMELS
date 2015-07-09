/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.ModelRunner;

import com.wtstockhausen.utils.FileFilterImpl;
import com.wtstockhausen.utils.ReverseListModel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureType;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.IllegalFilterException;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.*;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Savable;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
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
import wts.models.DisMELS.actions.Resetable;
import wts.models.DisMELS.actions.SaveAsable;
import wts.models.DisMELS.events.AnimationEvent;
import wts.models.DisMELS.events.AnimationEventListener;
import wts.models.DisMELS.framework.*;
import wts.models.DisMELS.gui.ModelControllerBeanCustomizer;
import wts.models.utilities.MathFunctions;
import wts.roms.topcomponents.MapViewer.MapViewerTopComponent;

/**
 * Top component which displays a customizer for the ModelControllerBean and allows
 * the user to initialize and subsequently run a DisMELS model.
 * 
 * @author - William Stockhausen
 */
@ConvertAsProperties(dtd = "-//wts.models.DisMELS.topcomponents.ModelRunner//ModelRunner//EN",
autostore = false)
@TopComponent.Description(preferredID = "ModelRunnerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "wts.models.DisMELS.topcomponents.ModelRunner.ModelRunnerTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ModelRunnerAction",
preferredID = "ModelRunnerTopComponent")
@Messages({
    "CTL_ModelRunnerAction=ModelRunner",
    "CTL_ModelRunnerTopComponent= IBM Editor/Launcher",
    "HINT_ModelRunnerTopComponent=This is the IBM Editor/Launcher window"
})
public final class ModelRunnerTopComponent extends TopComponent implements AnimationEventListener, PropertyChangeListener  {
    
    /** singleton object */
    private static ModelRunnerTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "ModelRunnerTopComponent";
    /** */
    public static final String PROP_ATTRIBUTES = "attributes";

    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized ModelRunnerTopComponent getDefault(){
        if (instance==null){
            instance = new ModelRunnerTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized ModelRunnerTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof ModelRunnerTopComponent) {
            return (ModelRunnerTopComponent) win;
        }
        Logger.getLogger(ModelRunnerTopComponent.class.getName()).warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }
    
    /** GlobalInfo singleton */
    private GlobalInfo globalInfo = null;
    
    /** JFileChooser for Model Controller file */
    private JFileChooser jfcMCB = new JFileChooser();
    private String fnMCB = "";
    private ReverseListModel lmMCB = new ReverseListModel();
    
    /** flag to show animation during model run */
    boolean showAnim = true;
    /** flag to save animation during model run to a series of files */
    private boolean saveAnimation = false;
    /** counter for animation files */
    private int animCtr = 0;
    /** filename base for animation files */
    private String animBase = "Anim_";

    private final transient FeatureCollection fcStartPoints = FeatureCollections.newCollection();
    private final transient FeatureCollection fcEndPoints   = FeatureCollections.newCollection();
    private final transient FeatureCollection fcTracks      = FeatureCollections.newCollection();
    private final transient FeatureCollection fcDeadPoints  = FeatureCollections.newCollection();
    private final transient FeatureCollection fcDeadTracks  = FeatureCollections.newCollection();
    
    private transient MapLayer startPointsLayer = null;
    private transient MapLayer endPointsLayer = null;
    private transient MapLayer tracksLayer = null;
    private transient MapLayer deadPointsLayer = null;
    private transient MapLayer deadTracksLayer = null;

    /** a Geotools 2.0 FilterFactory */
    private FilterFactory filterFactory = FilterFactory.createFilterFactory();
    /** a Geotools 2.0 StyleBuilder object */
    private StyleBuilder sb = new StyleBuilder();
    
    /** the ModelControllerBean instance that actually runs th model */
    private ModelControllerBean mcb = null;
    
    /** the singleton instance of the MapViewerTopComponent */
    private MapViewerTopComponent tcMapViewer = null;

    /** instance content reflecting csvLoader and csvSaver capabilities */
    private InstanceContent content;
    /** instance of the private class for loading the model parameters from xml */
    private XMLSaver xmlSaver;
    /** instance of the private class for loading the model parameters from xml */
    private XMLLoader xmlLoader;
    /** instance of the private class for reseting all parameter values */
    private Resetter resetter;
    
    private Timer timer;
    private ProgressHandle progressHandle;
    
    /** logger for operating messages */
    private static final Logger logger = Logger.getLogger(ModelRunnerTopComponent.class.getName());

    public ModelRunnerTopComponent() {
        logger.info("------Start instantiating ModelRunner");
        initComponents();
        initComponents1();
        setName(Bundle.CTL_ModelRunnerTopComponent());
        setToolTipText(Bundle.HINT_ModelRunnerTopComponent());
        
        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect xmlSaver, xmlLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
//        enableSaveAction(false);
//        enableLoadAction(true);
        resetter = new Resetter();
        content.add(resetter);
        logger.info("------Done instantiating ModelRunner");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mcbCustomizer = new wts.models.DisMELS.gui.ModelControllerBeanCustomizer();
        jPanel2 = new javax.swing.JPanel();
        jchkShowAnim = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jbInitializeModel = new javax.swing.JButton();
        jbRunModel = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());
        add(mcbCustomizer, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jchkShowAnim.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(jchkShowAnim, org.openide.util.NbBundle.getMessage(ModelRunnerTopComponent.class, "ModelRunnerTopComponent.jchkShowAnim.text")); // NOI18N
        jchkShowAnim.setToolTipText(org.openide.util.NbBundle.getMessage(ModelRunnerTopComponent.class, "ModelRunnerTopComponent.jchkShowAnim.toolTipText")); // NOI18N
        jchkShowAnim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jchkShowAnimActionPerformed(evt);
            }
        });
        jPanel2.add(jchkShowAnim, java.awt.BorderLayout.WEST);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        org.openide.awt.Mnemonics.setLocalizedText(jbInitializeModel, org.openide.util.NbBundle.getMessage(ModelRunnerTopComponent.class, "ModelRunnerTopComponent.jbInitializeModel.text")); // NOI18N
        jbInitializeModel.setToolTipText(org.openide.util.NbBundle.getMessage(ModelRunnerTopComponent.class, "ModelRunnerTopComponent.jbInitializeModel.toolTipText")); // NOI18N
        jbInitializeModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbInitializeModelActionPerformed(evt);
            }
        });
        jPanel1.add(jbInitializeModel);

        org.openide.awt.Mnemonics.setLocalizedText(jbRunModel, org.openide.util.NbBundle.getMessage(ModelRunnerTopComponent.class, "ModelRunnerTopComponent.jbRunModel.text")); // NOI18N
        jbRunModel.setToolTipText(org.openide.util.NbBundle.getMessage(ModelRunnerTopComponent.class, "ModelRunnerTopComponent.jbRunModel.toolTipText")); // NOI18N
        jbRunModel.setMaximumSize(new java.awt.Dimension(79, 25));
        jbRunModel.setMinimumSize(new java.awt.Dimension(79, 25));
        jbRunModel.setPreferredSize(new java.awt.Dimension(79, 25));
        jbRunModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRunModelActionPerformed(evt);
            }
        });
        jPanel1.add(jbRunModel);

        jPanel2.add(jPanel1, java.awt.BorderLayout.EAST);

        add(jPanel2, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jbInitializeModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbInitializeModelActionPerformed
        logger.log(Level.INFO, "Initializing model.");
        initializeModel();
//        jbRunModel.setEnabled(true);
//        jbInitializeModel.setEnabled(false);
//        logger.log(Level.INFO, "Done initializing model.");
    }//GEN-LAST:event_jbInitializeModelActionPerformed

    private void jbRunModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRunModelActionPerformed
        logger.log(Level.INFO, "Running model.");
        runModel();
//        jbRunModel.setEnabled(false);
//        jbInitializeModel.setEnabled(true);
//        logger.log(Level.INFO, "Done Running model.");

    }//GEN-LAST:event_jbRunModelActionPerformed

    private void jchkShowAnimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jchkShowAnimActionPerformed
        showAnim = jchkShowAnim.isSelected();
    }//GEN-LAST:event_jchkShowAnimActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbInitializeModel;
    private javax.swing.JButton jbRunModel;
    private javax.swing.JCheckBox jchkShowAnim;
    private wts.models.DisMELS.gui.ModelControllerBeanCustomizer mcbCustomizer;
    // End of variables declaration//GEN-END:variables

    /**
     * Method called when this TopComponent's window is opened in the application.
     * This could happen multiple times before the application is closed.
     */
    @Override
    public void componentOpened() {
        logger.info("starting compnonentOpened()");
        enableSaveAction(true);
        if ((!fnMCB.isEmpty())||(!fnMCB.equals(""))){
            try {
                File f = new File(fnMCB);
                xmlLoader.open(f);
                enableSaveAction(true);
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        //make sure MapViewerTopComponent is open
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                tcMapViewer = MapViewerTopComponent.findInstance();
                if (!tcMapViewer.isOpened()) tcMapViewer.open();
                tcMapViewer.addPartner();
                tcMapViewer.requestActive();
                requestActive();
            }
        });
        enableLoadAction(true);
        logger.info("finished compnonentOpened()");
    }

    @Override
    public void componentClosed() {
        logger.info("starting componentClosed()");
        if (startPointsLayer!=null) tcMapViewer.removeGISLayer(startPointsLayer);
        if (endPointsLayer!=null)   tcMapViewer.removeGISLayer(endPointsLayer);
        if (tracksLayer!=null)      tcMapViewer.removeGISLayer(tracksLayer);
        if (deadPointsLayer!=null)  tcMapViewer.removeGISLayer(deadPointsLayer);
        if (deadTracksLayer!=null)  tcMapViewer.removeGISLayer(deadTracksLayer);
        tcMapViewer.removePartner();
        if (tcMapViewer.canClose()) tcMapViewer.close();
        logger.info("finished componentClosed()");
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        p.setProperty("ModelControllerBeanFilename",fnMCB);
    }

    void readProperties(java.util.Properties p) {
        logger.info("starting readProperties()");
        //get version
        String version = p.getProperty("version");
        
        //get mcb file name, if available
        String s = p.getProperty("ModelControllerBeanFilename");
        if (s!=null) fnMCB = s;
        
        logger.info("finished readProperties()");
    }

    /**
     * Method reacts to PropertyChanges fired by: 
     *      the GlobalInfo instance for changes to the working directory
     *      the ModelControllerBeanCustomizer for any changes in MCB properties
     *      the ModelControllerBean for changes in ocean time during a model run
     * 
     * TODO: do we need this?
     * 
     * @param evt - the PropertyChange event 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case GlobalInfo.PROP_WorkingDirFN:
                String wdFN = globalInfo.getWorkingDir();
                File wdF = new File(wdFN);
                jfcMCB.setCurrentDirectory(wdF);
                break;
            case ModelControllerBeanCustomizer.PROP_CHANGE:
                enableSaveAction(true);
                jbInitializeModel.setEnabled(true);
                jbRunModel.setEnabled(false);
                break;
            case ModelControllerBean.PROP_OCEANTIME:
                tcMapViewer.setOceanTime((double) evt.getNewValue());
                break;
        }
    }
    
    private void initComponents1() {
        globalInfo = GlobalInfo.getInstance();
        globalInfo.addPropertyChangeListener(this);
        String wdFN = globalInfo.getWorkingDir();
        File wdF = new File(wdFN);

        xmlSaver = new XMLSaver();
        xmlLoader = new XMLLoader();

        jfcMCB.addChoosableFileFilter(new FileFilterImpl("xml","MCB files"));
        jfcMCB.setDialogTitle("Select Model Controller file:");
        jfcMCB.setCurrentDirectory(wdF);
        lmMCB.setSize(4);

        mcb = new ModelControllerBean();
        mcb.setRunForward(true);
        mcb.setFile_ROMSDataset("<not set>");
        mcb.setFile_Params("<not set>");
        mcb.setFile_InitialAttributes("<not set>");
        mcb.setFile_Results("<not set>");
        mcbCustomizer.setObject(mcb);
        mcbCustomizer.addPropertyChangeListener(this);
        
        this.validate();
    }
    
    /**
     * Method called to initialize a DisMELS model run.
     * TODO: incorporate flag for map rendering of results.
     */
    private void initializeModel(){
        logger.info("Initializing model");
        jbInitializeModel.setEnabled(false);
        fcStartPoints.clear();
        fcEndPoints.clear();
        fcTracks.clear();
        fcDeadPoints.clear();
        fcDeadTracks.clear();
        System.gc();
        try {
            mcb.setStartPointsFC(fcStartPoints);
            mcb.setEndPointsFC(fcEndPoints);
            mcb.setTracksFC(fcTracks);

            createStartPointsMapLayer(fcStartPoints);
            if (showAnim) {                
                createEndPointsMapLayer(fcEndPoints);
                createTracksMapLayer(fcTracks);
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        if (showAnim||mcb.getShowDeadIndivs()) {
            try {
                mcb.setDeadPointsFC(fcDeadPoints);
                mcb.setDeadTracksFC(fcDeadTracks);
                createDeadPointsMapLayer(fcDeadPoints);
                createDeadTracksMapLayer(fcDeadTracks);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        logger.info(mcb.getFile_ROMSGrid());
        logger.info(mcb.getFile_ROMSDataset());
        logger.info(mcb.getFile_Params());
        logger.info(mcb.getFile_InitialAttributes());
        logger.info(mcb.getFile_Results());
        logger.info("startTime = "+mcb.getStartTime());
        logger.info("nTimes    = "+mcb.getNtEnvironModel());
        logger.info("timeStep  = "+mcb.getTimeStep());
        logger.info("nLPTsteps = "+mcb.getNtBioModel());
        final ModelTaskIF task = mcb.getInitModelTask();

        if (showAnim){
            mcb.addPropertyChangeListener(this);//hook up for changes to ocean time
            if (saveAnimation) {
                logger.info("Adding ModelRunner as AnimationListener");
                mcb.addAnimationEventListener(this);
            } else {
                logger.info("Removing ModelRunner as AnimationListener");
                mcb.removeAnimationEventListener(this);
            }
        }
        
        progressHandle = ProgressHandleFactory.createHandle("Initializing model");        
        timer = new Timer(250,new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                progressHandle.setDisplayName("Initializing model: "+task.getMessage());
                progressHandle.progress(task.getMessage());
                if (task.isDone()) {
                    timer.stop();
                    progressHandle.finish();
                    jbRunModel.setEnabled(true);
                    jbInitializeModel.setEnabled(false);
                    logger.log(Level.INFO, "Done initializing model.");
                }
            }
        });
        progressHandle.start();
        timer.start();
        task.go();
    }
    
    /**
     * Run the model.
     */
    private void runModel(){
        logger.info("Starting runModel()");
        mcb.setGUIMode(showAnim);
        jbRunModel.setEnabled(false);
        jbInitializeModel.setEnabled(false);
        logger.info("Running model");
        final ModelTaskIF task = mcb.getRunModelTask();
        progressHandle = ProgressHandleFactory.createHandle("Running model");        
        timer = new Timer(1000,new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                progressHandle.setDisplayName("Running model: "+task.getMessage());
                progressHandle.progress(task.getMessage(),task.getCurrentValue());
                if (task.isDone()) {
                    timer.stop();
                    progressHandle.finish();
                    jbInitializeModel.setEnabled(true);
                    logger.info("Done running model");
                    mcb.setGUIMode(true);
                }
            }
        });
        progressHandle.start(mcb.getNtEnvironModel()*mcb.getNtBioModel());
        timer.start();
        task.go();
    }
    
    /**
     * Turns on/off whether animation will be saved to a series of files during
     * the model run.
     * 
     * @param b 
     */
    public void saveAnimation(boolean b) {
        logger.info("saveAnimation set to "+b);
        saveAnimation = b;
    }
    
    /**
     * Method saves the current MapViewer image to a gif file in the working directory.
     * 
     * @param ev - the AnimationEvent
     */
    @Override
    public void updateGraphics(AnimationEvent ev) {
        try {
            int h = animCtr/100;
            int t = (animCtr-100*h)/10;
            int o = MathFunctions.mod(animCtr,10);
            String animFN = globalInfo.getWorkingDir()+animBase+h+t+o+".gif";
            logger.info("Saving animataion image to "+animFN);
            animCtr++;
            tcMapViewer.saveMapAsImage(animFN);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    /**
     * Create a style to display points on the map.
     * 
     * @param markType - the type of mark to use on the map
     * @return - the style
     */
    private Style createPointsStyle(String markType) {
        Style s = sb.createStyle();
        try {
            String[] keys = LHS_Factory.getTypeNames();
            for (int i=0;i<keys.length;i++) {
                FeatureType ft = LHS_Factory.createPointFeatureType(keys[i]);
                CompareFilter f = filterFactory.createCompareFilter(CompareFilter.COMPARE_EQUALS);
                f.addLeftValue(filterFactory.createAttributeExpression(ft,"lhsType"));
                f.addRightValue(filterFactory.createLiteralExpression(keys[i]));
                Color clr = LHS_Factory.getLHScolor(keys[i]);
                Mark mrk = sb.createMark(markType,clr);
                Graphic gr1 = sb.createGraphic(null,mrk,null,1,10,0);
                PointSymbolizer ps = sb.createPointSymbolizer(gr1);
                Rule r = sb.createRule(ps);
                r.setFilter(f);
                r.setName(keys[i]+" rule");
                FeatureTypeStyle fts = sb.createFeatureTypeStyle(ft.getTypeName(),r);
                fts.setName(keys[i]+" style");
                s.addFeatureTypeStyle(fts);
            }
        } catch (IllegalAccessException | IllegalFilterException | InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return s;
    }

    /**
     * Creates the style used to display tracks on the map.
     * 
     * @return - the style
     */
    private Style createTracksStyle() {
        Style s = sb.createStyle();
        try {
            String[] keys = LHS_Factory.getTypeNames();
            for (int i=0;i<keys.length;i++) {
                FeatureType ft = LHS_Factory.createTrackFeatureType(keys[i]);
                CompareFilter f = filterFactory.createCompareFilter(CompareFilter.COMPARE_EQUALS);
                f.addLeftValue(filterFactory.createAttributeExpression(ft,"lhsType"));
                f.addRightValue(filterFactory.createLiteralExpression(keys[i]));
                Color clr = LHS_Factory.getLHScolor(keys[i]);
                LineSymbolizer ps = sb.createLineSymbolizer(clr,2);
                Rule r = sb.createRule(ps);
                r.setFilter(f);
                r.setName(keys[i]+" rule");
                FeatureTypeStyle fts = sb.createFeatureTypeStyle(ft.getTypeName(),r);
                fts.setName(keys[i]+" style");
                s.addFeatureTypeStyle(fts);
            }
        } catch (IllegalAccessException | IllegalFilterException | InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return s;
    }
    
    /**
     * Creates a map layer of points representing the start points 
     * of tracks of "live" individuals.
     * 
     * @param fcEndPoints - the feature collection of start points for live individuals
     */
    public void createStartPointsMapLayer(FeatureCollection fcStartPoints) {
        String title = "Start locations";
        tcMapViewer.removeGISLayer(title);
        Style style =  createPointsStyle(StyleBuilder.MARK_CIRCLE);
        style.setName("IBM start position style");
        style.setTitle("IBM start position style");
        startPointsLayer = new DefaultMapLayer(fcStartPoints,style,title);
        tcMapViewer.addGISLayer(startPointsLayer);
    }

    /**
     * Creates a map layer of points representing the end points (current locations) 
     * of tracks of "live" individuals.
     * 
     * @param fcEndPoints - the feature collection of end points (current locations)
     *                      for live individuals.
     */
    public void createEndPointsMapLayer(FeatureCollection fcEndPoints) {
        String title = "Current locations";
        tcMapViewer.removeGISLayer(title);
        Style style = createPointsStyle(StyleBuilder.MARK_TRIANGLE);
        style.setName("IBM current position style");
        style.setTitle("IBM current position style");
        endPointsLayer = new DefaultMapLayer(fcEndPoints,style,title);
        tcMapViewer.addGISLayer(endPointsLayer);
    }

    /**
     * Creates a map layer of points representing "dead" individuals.
     * 
     * @param fcDeadPoints - the feature collection for dead individuals
     */
    public void createDeadPointsMapLayer(FeatureCollection fcDeadPoints) {
        String title = "Dead indivs";
        tcMapViewer.removeGISLayer(title);
        Style style = createPointsStyle(StyleBuilder.MARK_TRIANGLE);
        style.setName("IBM dead position style");
        style.setTitle("IBM dead position style");
        deadPointsLayer = new DefaultMapLayer(fcDeadPoints,style,title);
        tcMapViewer.addGISLayer(deadPointsLayer);
    }

    /**
     * Creates a map layer for tracks of "live" individuals
     * 
     * @param fcTracks - the feature collection for "live" tracks
     */
    public void createTracksMapLayer(FeatureCollection fcTracks) {
        String title = "tracks";
        tcMapViewer.removeGISLayer(title);
        Style style = createTracksStyle();
        style.setName("IBM tracks style");
        style.setTitle("IBM tracks style");
        tracksLayer = new DefaultMapLayer(fcTracks,style,title);
        tcMapViewer.addGISLayer(tracksLayer);
    }

    /**
     * Creates a map layer for tracks of "dead" individuals
     * 
     * @param fcDeadTracks - the feature collection of dead tracks
     */
    public void createDeadTracksMapLayer(FeatureCollection fcDeadTracks) {
        String title = "Dead tracks";
        tcMapViewer.removeGISLayer(title);
        Style style = createTracksStyle();
        style.setName("IBM dead tracks style");
        style.setTitle("IBM dead tracks style");
        deadTracksLayer = new DefaultMapLayer(fcDeadTracks,style,title);
        tcMapViewer.addGISLayer(deadTracksLayer);
    }
    
    /**
     * Enables ability to load initial attributes from csv file.
     * 
     * @param canLoad 
     */
    public void enableLoadAction(boolean canLoad){
        if (canLoad){
            content.add(xmlLoader);//add a LoadCookie to the instance content
            logger.info("----Load enabled!!");
        } else {
            content.remove(xmlLoader);//remove a loadCookie from the instance content 
            logger.info("----Load disabled!!");
        }
    }
    
    /**
     * Enables ability to save initial attributes to csv file.
     * 
     * @param canSave 
     */
    public void enableSaveAction(boolean canSave){
        if (canSave){
            content.add(xmlSaver);//add the SaveCookie to the instance content
            logger.info("----Save enabled!!");
        } else {
            content.remove(xmlSaver);//remove the SaveCookie from the instance content 
            logger.info("----Save disabled!!");
        }
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement open functionality for the initial attributes.
     */
    private class XMLLoader implements Openable, ExceptionListener {

        @Override
        public void open() {
            logger.info("starting XMLLoader.open()");
            jfcMCB.setDialogTitle("Open model controller file (.xml)");
            FileFilter filter = new FileFilterImpl("xml","Model controller file");
            jfcMCB.setFileFilter(filter);
            int res = jfcMCB.showOpenDialog(ModelRunnerTopComponent.this);
            if (res!=JFileChooser.APPROVE_OPTION) return;
            try {
                File f = jfcMCB.getSelectedFile();
                fnMCB = f.getPath();
                open(f);
                jfcMCB.setSelectedFile(f);
            } catch (FileNotFoundException exc) {
                logger.warning(exc.toString());
            }
            logger.info("finished XMLLoader.open()");
        }
        
        private void open(File f) throws FileNotFoundException {
            logger.info("starting XMLLoader.open("+f.getName()+")");
            FileInputStream fis     = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            XMLDecoder xd = new XMLDecoder(bis);
            xd.setOwner(this);
            xd.setExceptionListener(this);
            mcb = null;
            logger.info("loading ModelControllerBean");
            mcb = (ModelControllerBean) xd.readObject();
            logger.info("done loading MCB");
            xd.close();

            mcbCustomizer.setObject(mcb);
            mcbCustomizer.repaint();
            jbInitializeModel.setEnabled(true);
            logger.info("finished XMLLoader.open("+f.getName()+")");
        }

        /**
        * Method catches and prints the stack trace for exceptions, particularly those
        * thrown during reading/writing xml files.
        * 
        * @param e - the exception
        */
        @Override
        public void exceptionThrown(Exception e) {
            Exceptions.printStackTrace(e);
        }
    }

    //------------------------------------------------------------------------//
    /**
     * Private class to implement save functionality for the model parameters.
     */
    private class XMLSaver implements Savable, SaveAsable, ExceptionListener  {

        @Override
        public void save() throws IOException {
            if (fnMCB.isEmpty()) {
                saveAs();
            } else {
                File saveTo = new File(fnMCB);
                save(saveTo);
            }
        }

        @Override
        public void saveAs() throws IOException {
            jfcMCB.setDialogTitle("Save model controller file (as xml)");
            FileFilter filter = new FileFilterImpl("xml","Model controller file");
            jfcMCB.setFileFilter(filter);
            int res = jfcMCB.showSaveDialog(ModelRunnerTopComponent.this);
            if (res!=JFileChooser.APPROVE_OPTION) return;
            File saveTo = jfcMCB.getSelectedFile();
            fnMCB = saveTo.getPath();
            save(saveTo);
        }

        private void save(File f) throws IOException {
            try {
                FileOutputStream fos     = new FileOutputStream(f);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                java.beans.XMLEncoder xe = new java.beans.XMLEncoder(bos);
                xe.setOwner(this);
                xe.setExceptionListener(this);
                logger.info("Saving model controller to xml");
                xe.writeObject(mcb);
                logger.info("SaveAs -- Done");
                //int i = egp.getComponentCount();
                xe.close();
                if (!f.getPath().equals(fnMCB)) {
                    fnMCB = f.getPath();
                    lmMCB.addElement(fnMCB);
                }
            } catch (FileNotFoundException exc) {
                logger.info(exc.toString());
            }
        }
 
        /**
        * Method catches and prints the stack trace for exceptions, particularly those
        * thrown during reading/writing xml files.
        * 
        * @param e - the exception
        */
        @Override
        public void exceptionThrown(Exception e) {
            Exceptions.printStackTrace(e);
        }
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement a "reset" capability for ModelControllerBean
     */
    private class Resetter implements Resetable {

        @Override
        public void reset() {
            mcb = new ModelControllerBean();
            mcb.setRunForward(true);
            mcb.setFile_ROMSDataset("<not set>");
            mcb.setFile_Params("<not set>");
            mcb.setFile_InitialAttributes("<not set>");
            mcb.setFile_Results("<not set>");
            mcbCustomizer.setObject(mcb);
        }        
    }
}
