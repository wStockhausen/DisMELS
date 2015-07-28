/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.LHSAttributesEditor;

import com.vividsolutions.jts.geom.*;
import com.wtstockhausen.utils.FileFilterImpl;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.beans.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.*;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.GeometryFilter;
import org.geotools.filter.IllegalFilterException;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.renderer.j2d.GeoMouseEvent;
import org.geotools.styling.*;
import org.netbeans.api.actions.Openable;
import org.netbeans.api.actions.Savable;
import org.netbeans.api.progress.ProgressUtils;
import org.netbeans.api.settings.ConvertAsProperties;
import org.opengis.referencing.operation.TransformException;
import org.openide.awt.ActionID;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import wts.models.DisMELS.actions.SaveAsable;
import wts.models.DisMELS.framework.*;
import wts.models.DisMELS.gui.AttributesCustomizer;
import wts.models.DisMELS.gui.LHSPointFeaturesTableModel;
import wts.roms.gis.AlbersNAD83;
import wts.roms.topcomponents.MapViewer.MapViewerTopComponent;

/**
 * Top component which displays the LHS initial attributes editor.
 * 
 * @author  William Stockhausen
 */
@ConvertAsProperties(dtd = "-//wts.models.DisMELS.topcomponents.LHSAttributesEditor//LHSAttributesEditor//EN",
autostore = false)
@TopComponent.Description(preferredID = "LHSAttributesEditorTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "wts.models.DisMELS.topcomponents.LHSAttributesEditor.LHSAttributesEditorTopComponent")
//@ActionReference(path = "Menu/Window" /*
// * , position = 333
// */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_LHSAttributesEditorAction",
preferredID = "LHSAttributesEditorTopComponent")
@Messages({
    "CTL_LHSAttributesEditorAction=LHSAttributesEditor",
    "CTL_LHSAttributesEditorTopComponent=Initial Attributes Editor",
    "HINT_LHSAttributesEditorTopComponent=Initial Attributes Editor window"
})
public final class LHSAttributesEditorTopComponent extends TopComponent implements PropertyChangeListener {
    
    /** singleton object */
    private static LHSAttributesEditorTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "LHSAttributesEditorTopComponent";
    /** */
    public static final String PROP_RELASEGRID = "relaese grid";
    /** */
    public static final String PROP_LHS = "life stage";
    /** */
    public static final String PROP_ATTRIBUTES = "attributes";
    
    /** flag to turn on debug info */
    public static boolean debug = true;
    /** logger to print debug info */
    private static final Logger logger = Logger.getLogger(LHSAttributesEditorTopComponent.class.getName());

    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized LHSAttributesEditorTopComponent getDefault(){
        if (instance==null){
            instance = new LHSAttributesEditorTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized LHSAttributesEditorTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof LHSAttributesEditorTopComponent) {
            return (LHSAttributesEditorTopComponent) win;
        }
        Logger.getLogger(LHSAttributesEditorTopComponent.class.getName()).warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }
    
    /** GlobalInfo singleton */
    private GlobalInfo globalInfo = null;
    
    /** output file name */
    private String fnCSV = "";
    /** file chooser for image files */
    private final JFileChooser imgJFC = new JFileChooser();
    /** file chooser for output csv files */
    private final JFileChooser jfcCSV = new JFileChooser();    
    /** file chooser for GIS layers */
    private final JFileChooser jfcLayer = new JFileChooser();

    /** release parameters array object */
    private final ReleaseParametersArray rpa = new ReleaseParametersArray();

    /** */
    private Feature feature;
    /** The feature type for the current LHS*/
    private LHSPointFeatureType ftLHS = null;
    /** feature collection containing the last points added to the map */
    private final FeatureCollection fcLastAddedPoints = FeatureCollections.newCollection();
    /** feature collection containing the all points added to the map */
    private FeatureCollection fcStartPoints;
    
    /** map layer showing initial locations */
    private MapLayer startPointsLayer = null;

    /** a Geotools 2.0 FilterFactory */
    private final FilterFactory filterFactory = FilterFactory.createFilterFactory();
    /** a Geotools 2.0 StyleBuilder object */
    private final StyleBuilder sb = new StyleBuilder();
    
    /** flag for mouse clicks/drag to zoom on mapGUI */
    private boolean mouseZooms = true;
    /** flag for mouse clicks/drag to add individuals to mapGUI */
    private boolean mouseAddsIndividuals = false;
    /** flag for mouse clicks/drag to add individuals to polygon in mapGUI */
    private boolean mouseAddsIndividualsToPolygon = false;
    /** flag indicating mouse is being dragged */
    private boolean isDragging = false;
    
    /** adapter for mouse motion events */
    MouseMotionAdapter mouseMotionAdapter = null;
    /** adapter for mouse events (press, release) */
    MouseAdapter mouseAdapter = null;
    /** instance variable for GeoMouseEvent */
    private GeoMouseEvent geoMousePressed;
    
    /** the selected map layer */
    private MapLayer selectedLayer    = null;
    /** collection of the selectable map layers */
    private final Map<String,MapLayer> selectableLayers = new TreeMap<>();
    /** JMenu for removing selectable GIS layers */
    JMenu jmuRemoveSelectableGISLayers = null;
    /** JMenu for selecting selectable GIS layer */
    JMenu jmuSelectedGISLayer = null;
    /** ButtonGroup for selected map layer */
    ButtonGroup bgSelectableGISLayers = new ButtonGroup();
    
    /** instance content reflecting csvLoader and csvSaver capabilities */
    private final InstanceContent content;

    /** the "key" (i.e., type name) for the selected LHS */
    private String lhsKey = null;
    /** an instance of LifeStageAttributesInterface for the selected LHS */
    private LifeStageAttributesInterface lhsAttributes = null;
    /** a convenience map of LifeStageAttributesInterface instances by type name (as key) for easy lookup */
    private Map<String,LifeStageAttributesInterface> attributesMap;
    /** the customizer associated with the current LHS */
    private AttributesCustomizer customizer = null;
    
    
    /** the singleton instance of the MapViewerTopComponent */
    private MapViewerTopComponent tcMapViewer = null;
    /** the singleton instance of the ROMS info */
    private wts.roms.model.GlobalInfo romsGI = null;
    
    /** instance of the private class for saving the initial attributes to csv */
    private CSVSaver csvSaver;
    /** instance of the private class for loading the initial attributes from csv */
    private CSVLoader csvLoader;
    
    public LHSAttributesEditorTopComponent() {
        if (debug) logger.info("------Start instantiating LHSAttributesEditor");
        initComponents();
        initComponents1();
        setName(Bundle.CTL_LHSAttributesEditorTopComponent());
        setToolTipText(Bundle.HINT_LHSAttributesEditorTopComponent());
        
        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect csvSaver, csvLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
        enableSaveAction(false);
        enableLoadAction(true);
        if (debug) logger.info("------Done instantiating LHSAttributesEditor");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jpAttributes = new javax.swing.JPanel();
        jpReleaseGrid = new javax.swing.JPanel();
        rpaCustomizer = new wts.models.DisMELS.topcomponents.LHSAttributesEditor.ReleaseParametersArrayCustomizer();
        jPanel1 = new javax.swing.JPanel();
        jpFeaturesTable = new wts.models.DisMELS.gui.JPanel_LHSPointFeaturesDataTable();
        jPanel4 = new javax.swing.JPanel();
        lhsSelector = new wts.models.DisMELS.gui.LHSSelector_JPanel();

        setLayout(new java.awt.BorderLayout());

        jpAttributes.setLayout(new java.awt.BorderLayout());
        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(LHSAttributesEditorTopComponent.class, "LHSAttributesEditorTopComponent.jpAttributes.TabConstraints.tabTitle"), null, jpAttributes, org.openide.util.NbBundle.getMessage(LHSAttributesEditorTopComponent.class, "LHSAttributesEditorTopComponent.jpAttributes.TabConstraints.tabToolTip")); // NOI18N

        javax.swing.GroupLayout jpReleaseGridLayout = new javax.swing.GroupLayout(jpReleaseGrid);
        jpReleaseGrid.setLayout(jpReleaseGridLayout);
        jpReleaseGridLayout.setHorizontalGroup(
            jpReleaseGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpReleaseGridLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpaCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(384, Short.MAX_VALUE))
        );
        jpReleaseGridLayout.setVerticalGroup(
            jpReleaseGridLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpReleaseGridLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rpaCustomizer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(334, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(LHSAttributesEditorTopComponent.class, "LHSAttributesEditorTopComponent.jpReleaseGrid.TabConstraints.tabTitle"), null, jpReleaseGrid, org.openide.util.NbBundle.getMessage(LHSAttributesEditorTopComponent.class, "LHSAttributesEditorTopComponent.jpReleaseGrid.TabConstraints.tabToolTip")); // NOI18N

        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.add(jpFeaturesTable, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(LHSAttributesEditorTopComponent.class, "LHSAttributesEditorTopComponent.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        lhsSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lhsSelectorActionPerformed(evt);
            }
        });
        jPanel4.add(lhsSelector, java.awt.BorderLayout.CENTER);

        add(jPanel4, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void lhsSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lhsSelectorActionPerformed
        try {
            setAttributes();
        } catch ( InstantiationException | IllegalAccessException | IntrospectionException  | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }//GEN-LAST:event_lhsSelectorActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JPanel jpAttributes;
    private wts.models.DisMELS.gui.JPanel_LHSPointFeaturesDataTable jpFeaturesTable;
    private javax.swing.JPanel jpReleaseGrid;
    private wts.models.DisMELS.gui.LHSSelector_JPanel lhsSelector;
    private wts.models.DisMELS.topcomponents.LHSAttributesEditor.ReleaseParametersArrayCustomizer rpaCustomizer;
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        if (debug) logger.info("starting LHSAttributesEditor.comphonentOpened()");
        romsGI.removePropertyChangeListener(this);
        globalInfo.addPropertyChangeListener(this);
        LHS_Factory.addPropertyChangeListener(this);
        //make sure MapViewerTopComponent is open
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                tcMapViewer = MapViewerTopComponent.findInstance();
                if (!tcMapViewer.isOpened()) tcMapViewer.open();
                tcMapViewer.addPartner();
                if (startPointsLayer!=null) tcMapViewer.addGISLayer(startPointsLayer);
                tcMapViewer.addGeoMouseMotionListener( mouseMotionAdapter);
                tcMapViewer.addGeoMouseListener(mouseAdapter);
                tcMapViewer.requestActive();
                requestActive();
            }
        });
        try {
            setAttributes();
        } catch (InstantiationException | IllegalAccessException | IntrospectionException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        //create feature collection to map individuals
        createStartPointsMapLayer();
        if (debug) logger.info("finished componentOpened()");
    }

    @Override
    public void componentClosed() {
        if (debug) logger.info("starting componentClosed()");
        if (startPointsLayer!=null) tcMapViewer.removeGISLayer(startPointsLayer);
        if (!mouseZooms) setMouseZooms();
        tcMapViewer.removeGeoMouseMotionListener( mouseMotionAdapter);
        tcMapViewer.removeGeoMouseListener(mouseAdapter);
        tcMapViewer.removePartner();
        if (tcMapViewer.canClose()) tcMapViewer.close();
        romsGI.addPropertyChangeListener(this);
        LHS_Factory.removePropertyChangeListener(this);
        globalInfo.removePropertyChangeListener(this);
        if (debug) logger.info("finished componentClosed()");
    }

    private void initComponents1() {
        if (debug) logger.info("starting initComponents1()");
        globalInfo = GlobalInfo.getInstance();
        romsGI = wts.roms.model.GlobalInfo.getInstance();
        String wdFN = globalInfo.getWorkingDir();
        File wdF = new File(wdFN);

        csvSaver = new CSVSaver();
        csvLoader = new CSVLoader();

        FileFilter fileFilter1 = new FileFilterImpl("csv","CSV files");
        jfcCSV.addChoosableFileFilter(fileFilter1);
        jfcCSV.setCurrentDirectory(wdF);

        String[] formats = ImageIO.getWriterFormatNames();
        FileFilter fileFilter2 = new FileFilterImpl(formats,"Image files");
        imgJFC.addChoosableFileFilter(fileFilter2);
        imgJFC.setCurrentDirectory(wdF);
        imgJFC.setDialogTitle("Save map to image file");

        FileFilter fileFilter3 = new FileFilterImpl("shp","Shape files");
        jfcLayer.addChoosableFileFilter(fileFilter3);
        jfcLayer.setFileFilter(fileFilter3);
        jfcLayer.setCurrentDirectory(wdF);
        jfcLayer.setDialogTitle("Open an ESRI polygon shapefile");
        
        lhsSelector.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent evt) {
               lhsSelectorActionPerformed(evt);
           }
        });
        
        //set up the mouse event adapters
        mouseMotionAdapter = new java.awt.event.MouseMotionAdapter() {
                                    @Override
                                    public void mouseDragged(java.awt.event.MouseEvent evt) {
                                        geoMouseDragged(evt);
                                    }
                                };
        mouseAdapter = new java.awt.event.MouseAdapter() {
                            @Override
                            public void mousePressed(java.awt.event.MouseEvent evt) {
                                geoMousePressed(evt);
                            }
                            @Override
                            public void mouseReleased(java.awt.event.MouseEvent evt) {
                                geoMouseReleased(evt);
                            }
                        };
        
        //set up the attributes map
        attributesMap = new TreeMap<>();
        
        //set up the release parameters customizer
        rpaCustomizer.setObject(rpa);
        
        //set up an empty feature collection for individuals
        fcStartPoints = FeatureCollections.newCollection();
        
        if (debug) logger.info("finished initComponents1()");
    }
    
    /**
     * Retrieves the customizer for the lhs attributes using introspection.
     * 
     * @param object - instance of LifeStageAttributesInterface
     * @return - the associated customizer
     * @throws IntrospectionException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    private AttributesCustomizer getAttributesCustomizer(LifeStageAttributesInterface object) throws IntrospectionException, 
                                                        InstantiationException, 
                                                        IllegalAccessException {
        if (debug) logger.info("---Starting LHSAttributesEditor.getCustomizer()");
        BeanInfo bi = Introspector.getBeanInfo(object.getClass());
        BeanDescriptor bd = bi.getBeanDescriptor();
        Class cClass = bd.getCustomizerClass();
        customizer = (AttributesCustomizer) cClass.newInstance();
        if (debug) logger.info("---Finished LHSAttributesEditor.getCustomizer()");
        return customizer;
    }

    /**
     * Sets the attributes in the customizer for the selected lhs type (i.e., sub-stage).
     * 
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IntrospectionException 
     */
    private void setAttributes() throws InstantiationException, IllegalAccessException, IntrospectionException, IOException {
        if (debug) logger.info("---Starting setAttributes()");
        lhsKey = lhsSelector.getSelectedName();
        ftLHS = LHS_Factory.createPointFeatureType(lhsKey);
        lhsAttributes = attributesMap.get(lhsKey);
        if (lhsAttributes==null) {
            if (debug) logger.info("Creating new LHS attributes for "+lhsKey);
            lhsAttributes = LHS_Factory.createAttributes(lhsKey);
            lhsAttributes.setValue(LifeStageAttributesInterface.PROP_horizType,Types.HORIZ_LL);
            attributesMap.put(lhsKey,lhsAttributes);
        }
        ftLHS.setAttributes(lhsAttributes);
        if (customizer!=null) jpAttributes.remove(customizer);
        customizer = getAttributesCustomizer(lhsAttributes);
        customizer.setAttributes(lhsAttributes);
        jpAttributes.add(customizer, java.awt.BorderLayout.CENTER);
        customizer.showHorizPos(false);
        customizer.showVertPos(true);
        jpAttributes.validate();
        jpAttributes.repaint();
//        if (!jpFeaturesTable.hasTableModel()) {
            jpFeaturesTable.createTableModel(ftLHS, fcStartPoints);
            jpFeaturesTable.validate();
//        }
        if (debug) logger.info("---finished setAttributes()\n");
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
     * Method reacts to PropertyChanges fired by the GlobalInfo instance
     * 
     * TODO: do we need this?
     * 
     * @param evt - the PropertyChange event 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GlobalInfo.PROP_WorkingDirFN)){
            String wdFN = globalInfo.getWorkingDir();
            File wdF = new File(wdFN);
            fnCSV = "";
            jfcCSV.setCurrentDirectory(wdF);
            imgJFC.setCurrentDirectory(wdF);
            lhsSelector.setEnabled(false);//turns off ActionEvents for lhs selection
            lhsSelector.updateTypeNames();
            fcStartPoints.clear();
            fcLastAddedPoints.clear();
//            jpFeaturesTable.removeAll();
            attributesMap.clear();
            try {
                setAttributes();
            } catch (InstantiationException | IllegalAccessException | IntrospectionException  | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            tcMapViewer.removeGISLayer(startPointsLayer);
            createStartPointsMapLayer();
            lhsSelector.setEnabled(true);//turns on ActionEvents for lhs selection
        } else if (evt.getPropertyName().equals(LHS_Factory.PROP_RESET)){
            lhsSelector.setEnabled(false);//turns off ActionEvents for lhs selection
            lhsSelector.updateTypeNames();
            fcStartPoints.clear();
            fcLastAddedPoints.clear();
//            jpFeaturesTable.removeAll();
            attributesMap.clear();
            try {
                setAttributes();
            } catch (InstantiationException | IllegalAccessException | IntrospectionException  | IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            tcMapViewer.removeGISLayer(startPointsLayer);
            createStartPointsMapLayer();
            lhsSelector.setEnabled(true);//turns on ActionEvents for lhs selection
        }
    }
    
    /**
     * Gets the selected map layer from among the selectable layers.
     * TODO: implement layer selection
     * 
     * @return - the selected layer
     */
    public MapLayer getSelectedLayer(){
        return selectedLayer;
    }
    
    /**
     * Sets the selected map layer based on the layer name.
     * 
     * @param layer - the name of the layer to be the selected layer 
     */
    public void setSelectedLayer(String layer){
        MapLayer mapLayer = selectableLayers.get(layer);
        if (mapLayer!=null) selectedLayer = mapLayer;
    }

    /**
     * Read the initial attributes file csvFN.
     * 
     * @throws IOException 
     */
    private void loadAttributesFile() throws IOException {
        InitialAttributesReader ipr = new InitialAttributesReader();
        try {
            FeatureCollection fc = ipr.readAttributesFile(fnCSV);
            fcStartPoints.addAll(fc);
            jpFeaturesTable.addAll(fc);
            fcLastAddedPoints.clear();
            fcLastAddedPoints.addAll(fc);
        } catch (InstantiationException | IllegalAccessException | java.lang.NullPointerException ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    fnCSV,
                    "Error loading initial attributes file",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
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
     * Creates a map layer of points representing the start points 
     * of tracks of "live" individuals.
     * 
     * @param fcEndPoints - the feature collection of start points for live individuals
     */
    public void createStartPointsMapLayer() {
        logger.info("started createStartPointsMapLayer(fcStartPoints)");
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                if (startPointsLayer !=null) tcMapViewer.removeGISLayer(startPointsLayer);
                Style style =  createPointsStyle(StyleBuilder.MARK_CIRCLE);
                style.setName("IBM start position style");
                style.setTitle("IBM start position style");
                startPointsLayer = new DefaultMapLayer(fcStartPoints,style,"Start locations");
                tcMapViewer.addGISLayer(startPointsLayer);
            }
        });
        logger.info("finished createStartPointsMapLayer(fcStartPoints)");
    }

    /**
     * Is true if mouse function on map is set to zoom on left-clicks/drags.
     * 
     * @return - true/false 
     */
    public boolean getMouseZooms(){
        return mouseZooms;
    }
    
    /**
     * Sets mouse function on map to zooming.
     */
    void setMouseZooms(){
        logger.fine("Mouse set to zoom");
        mouseZooms = true;
        mouseAddsIndividuals=false;
        mouseAddsIndividualsToPolygon=false;
        tcMapViewer.setMouseDragZooms(true);
    }

    /**
     * Is true if mouse function on map is set to add individuals on left-clicks/drags.
     * 
     * @return - true/false 
     */
    public boolean getMouseAddsIndividuals(){
        return mouseAddsIndividuals;
    }
    
    /**
     * Sets mouse function on map to adding individuals.
     */
    void setMouseAddsIndividuals(){
        logger.fine("Mouse set to add individuals");
        mouseZooms = false;
        mouseAddsIndividuals=true;
        mouseAddsIndividualsToPolygon=false;
        tcMapViewer.setMouseDragZooms(false);
    }

    /**
     * Is true if mouse function on map is set to add individuals on left-clicks 
     * to selected polygon layer.
     * 
     * @return - true/false 
     */
    public boolean getMouseAddsIndividualsToPolygon(){
        return mouseAddsIndividualsToPolygon;
    }
    
    /**
     * Sets mouse function on map to adding individuals to selected polygon layer.
     */
    public void setMouseAddsIndividualsToPolygon(){
        mouseZooms = false;
        mouseAddsIndividuals=false;
        mouseAddsIndividualsToPolygon=true;
        tcMapViewer.setMouseDragZooms(false);
    }
    
    /**
     * 
     * @param evt 
     */
    public void geoMouseDragged(java.awt.event.MouseEvent evt) {
        if (mouseAddsIndividuals&&
                (evt instanceof GeoMouseEvent)&&
                (evt.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK)) {
            isDragging = true;
        }
    }

    /**
     * 
     * @param evt 
     */
    public void geoMousePressed(java.awt.event.MouseEvent evt) {
        logger.fine("geoMousePressed");
        geoMousePressed = null;
        if (!mouseZooms&&
                (evt instanceof GeoMouseEvent)&&(evt.getButton()==MouseEvent.BUTTON1)) {
            geoMousePressed = (GeoMouseEvent) evt;
        }
    }

    /**
     * Responds to "release" mouse events.
     * 
     * @param evt 
     */
    public void geoMouseReleased(java.awt.event.MouseEvent evt) {
        logger.fine("geoMouseReleased");
        if ((mouseZooms)||
                (evt.getButton()!=MouseEvent.BUTTON1)) return;
        if (evt instanceof GeoMouseEvent) {
            GeoMouseEvent gme = (GeoMouseEvent) evt;
            if (!isDragging) {
                try {
                    Point2D strt = geoMousePressed.getMapCoordinate(null);
                    addIndividuals(mouseAddsIndividuals,strt);
                    return;
                } catch (IllegalAttributeException | TransformException | IntrospectionException |InstantiationException |IllegalAccessException  | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            //otherwise isDragging==true so
            Point2D strt = geoMousePressed.getPixelCoordinate(null);
            Point2D dest = gme.getPixelCoordinate(null);
            double d = strt.distance(dest);
            strt = geoMousePressed.getMapCoordinate(strt);
            //if onscreen drag distance is small, assume a "click" was meant,
            //so just add a single point at the point of the original press.
            if (d<3) {
                try {
                    if (mouseAddsIndividuals||mouseAddsIndividualsToPolygon)
                        addIndividuals(mouseAddsIndividuals,strt);
                    return;
                } catch (IllegalAttributeException | TransformException | IntrospectionException |InstantiationException |IllegalAccessException  | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (debug) logger.fine("Drag started: "+String.valueOf(strt.getX())+","+
                                                    String.valueOf(strt.getY()));
            dest = gme.getMapCoordinate(dest);
            if (debug) logger.fine("Drag ended: "+String.valueOf(dest.getX())+","+
                                                  String.valueOf(dest.getY()));
            addIndividuals(strt,dest);
        }
        isDragging = false;
    }

    public void addIndividuals(final boolean addIndividual, final java.awt.geom.Point2D pt)throws 
                        IllegalAttributeException, TransformException, IntrospectionException, InstantiationException, IllegalAccessException, IOException{
        if (debug) logger.info("Adding individuals!!");
        fcLastAddedPoints.clear();
        if (ftLHS==null) setAttributes();
        FeatureCollection fc = null;
        if (addIndividual) {
            fc = addPoint(pt);
            fcStartPoints.addAll(fc);
            fcLastAddedPoints.addAll(fc);
            jpFeaturesTable.addAll(fc);
            enableSaveAction(true);
        } else {
            Runnable r = new Runnable(){
                @Override
                public void run() {
                    try {
                        Cursor c = getCursor();
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        validate();
                        repaint();
                        if (ftLHS==null) setAttributes();
                        FeatureCollection fc = addPointsToPolygon(pt);
                        fcStartPoints.addAll(fc);
                        fcLastAddedPoints.addAll(fc);
                        jpFeaturesTable.addAll(fc);
                        enableSaveAction(true);
                        setCursor(c);
                    } catch (IllegalAttributeException | TransformException | InstantiationException | IllegalAccessException | IntrospectionException | IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            };
            ProgressUtils.showProgressDialogAndRun(r, "Creating individuals...");
        }
    }

    public void addIndividuals(final java.awt.geom.Point2D pt1, final java.awt.geom.Point2D pt2){
        Runnable r = new Runnable(){
            @Override
            public void run() {
                try {
                    Cursor c = getCursor();
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    validate();
                    repaint();
                    if (ftLHS==null) setAttributes();
                    FeatureCollection fc = addPointsInGrid(pt1,pt2);
                    fcStartPoints.addAll(fc);
                    fcLastAddedPoints.addAll(fc);
                    jpFeaturesTable.addAll(fc);
                    enableSaveAction(true);
                    setCursor(c);
                } catch (IllegalAttributeException | TransformException | InstantiationException | IllegalAccessException | IntrospectionException | IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        };
        ProgressUtils.showProgressDialogAndRun(r, "Creating individuals...");
    }
    
    /**
     * Adds individuals based on the horizontal location given by the point.  Multiple individuals
     * can be added if a vertical and/or temporal grid is specified, so a FeatureCollection is returned.
     * 
     * @param pt - the horizontal location of the individuals to be added.
     * @throws IllegalAttributeException
     * @throws TransformException
     * @throws IntrospectionException 
     */
    private FeatureCollection addPoint(java.awt.geom.Point2D pt)
        throws IllegalAttributeException, TransformException, IntrospectionException {
        //Create the feature and add itm to the feature collection
//        if (debug) logger.info("Adding point");
        FeatureCollection fc = FeatureCollections.newCollection();
        double[] latlon = AlbersNAD83.transformPtoG(new double[]{pt.getX(),pt.getY()});
        double[] IJ = romsGI.getGrid2D().computeIJfromLL(latlon[1],latlon[0]);
        //do not create points on land!
        if (romsGI.getGrid2D().isOnLand(IJ)) return fc;
        
        try {
            ftLHS.setGeometryFromMap(pt.getX(),pt.getY());
            LifeStageAttributesInterface aLHSt = (LifeStageAttributesInterface) lhsAttributes.clone();
            ftLHS.setAttributes(aLHSt);
            int nt = 1;
            double dt = 0;
            //TODO: need to get rpa from LHSAttributesEditor
            if (rpa.getCreateTemporalGrid()) {
                nt = rpa.getNumberReleaseTimes();
                dt = rpa.getDeltaT();//this is in days
            }
            int nz = 1;
            double dz = 0;
            double bd = 0;
            if (rpa.getCreateVerticalGrid()) {
                dz = rpa.getDeltaZ();
                bd = romsGI.getInterpolator().interpolateBathymetricDepth(IJ);
//                bd = tcMapViewer.interpolateBathymetricDepth(pt.getX(),pt.getY());
                nz = (int)(bd/dz)+1;
                aLHSt.setValue(LifeStageAttributesInterface.PROP_vertType,Types.VERT_H);
            }
            double startTime = ((java.lang.Double) aLHSt.getValue(LifeStageAttributesInterface.PROP_startTime)).doubleValue();
            double st,zt;
            for (int it=1;it<=nt;it++) {
                if (startTime>0){
                    st = startTime+(it-1)*86400*dt;//start times are in ROMS ocean_time (i.e., seconds)
                } else {
                    st = -(it-1)*dt;//start times are in (negative) days relative to MODEL RUNNER start time
                }
                aLHSt.setValue(LifeStageAttributesInterface.PROP_startTime,st);
                for (int iz=0;iz<nz;iz++) {
                    if (rpa.getCreateVerticalGrid()) {
                        zt = iz*dz;
                        aLHSt.setValue(LifeStageAttributesInterface.PROP_vertPos,zt);
                    }
                    feature = ftLHS.createFeature();
                    fc.add(feature);
//                    fcStartPoints.add(feature);
//                    fcLastAddedPoints.add(feature);
//                    jpFeaturesTable.add(feature);
                }
            }
            ftLHS.setAttributes(lhsAttributes);
        } catch (CloneNotSupportedException ex) {
            Exceptions.printStackTrace(ex);
        }    
        return fc;
    }
    
    /**
     * Adds points (individuals) to rectangular grid defined by opposite end points.
     * 
     * @param pt1 - one end point
     * @param pt2 - the opposite end point
     * @return - a FeatureCollection of individuals
     * @throws IllegalAttributeException
     * @throws TransformException
     * @throws IntrospectionException 
     */
    private FeatureCollection addPointsInGrid(java.awt.geom.Point2D pt1,java.awt.geom.Point2D pt2)
            throws IllegalAttributeException, TransformException, IntrospectionException {
        //Now create the features and add them to the feature collection
//        if (debug) logger.info("Adding many points");
        FeatureCollection fc = FeatureCollections.newCollection();
        fcLastAddedPoints.clear();
        Point2D pt;
        double dx = rpa.getDeltaX();
        double dy = rpa.getDeltaY();
        if (debug) logger.info("Adding points in grid at spacing "+dx+" x "+dy);
        int nx = (int)Math.abs((pt2.getX()-pt1.getX())/dx)+1;
        int ny = (int)Math.abs((pt2.getY()-pt1.getY())/dy)+1;
        double sx = Math.signum(pt2.getX()-pt1.getX());
        double sy = Math.signum(pt2.getY()-pt1.getY());
        double x,y;
        for (int i=0;i<nx;i++) {
            x = pt1.getX()+i*sx*dx;
            for (int j=0;j<ny;j++) {
                y = pt1.getY()+j*sy*dy;
                pt = new Point2D.Double(x,y);
                fc.addAll(addPoint(pt));
            }
        }
        return fc;
    }

    /**
     * Adds points (individuals) to the polygon features from the selectable polygon
     * layer that enclose the input Point2D location.
     * 
     * @param pt1 - the location that selected polygons must enclose
     * @return - FeatureCollection of individuals 
     * @throws IllegalAttributeException
     * @throws TransformException
     * @throws IntrospectionException 
     */
    private FeatureCollection addPointsToPolygon(java.awt.geom.Point2D pt1)
            throws IllegalAttributeException, TransformException, IntrospectionException {
        FeatureCollection fc = FeatureCollections.newCollection();
        try {
            //create a Point corresponding to Point2D location
            GeometryFactory gfac = new GeometryFactory();
            Coordinate coords = new Coordinate(pt1.getX(),pt1.getY());
            Point point = gfac.createPoint(coords);
            //get the feature source for the selectable layer
            FeatureSource fs = getSelectedLayer().getFeatureSource();
            //create a filter to find features that enclose the clicked point
            GeometryFilter gf =
                    filterFactory.createGeometryFilter(GeometryFilter.GEOMETRY_CONTAINS);
            gf.addLeftGeometry(filterFactory.createAttributeExpression(
                    fs.getSchema(),fs.getSchema().getDefaultGeometry().getName()));
            gf.addRightGeometry(filterFactory.createLiteralExpression(point));
            FeatureCollection selFC = fs.getFeatures(gf);
            FeatureIterator itSelFC = selFC.features();
            if (itSelFC.hasNext()) {
                //for first feature only, use bounds to define an "array" of
                //potential points to add.  For each point that is contained by
                //the feature, add it to the map as a new initial position.
                Feature f = itSelFC.next();
                Geometry poly = f.getDefaultGeometry();
                Envelope e = f.getBounds();
                double dx = rpa.getDeltaX();
                double dy = rpa.getDeltaY();
                if (debug) logger.info("Adding points to polygon at spacing "+dx+" x "+dy);
                Point tp;
                gf = filterFactory.createGeometryFilter(GeometryFilter.GEOMETRY_CONTAINS);
                gf.addLeftGeometry(filterFactory.createLiteralExpression(
                        f.getDefaultGeometry()));
                for (double x=e.getMinX();x<=e.getMaxX();x=x+dx) {
                    for (double y=e.getMinY();y<=e.getMaxY();y=y+dy) {
                        tp = gfac.createPoint(new Coordinate(x,y));
                        if (tp.within(poly)) {
                            fc.addAll(addPoint(new Point2D.Double(x,y)));
                        }
                    }
                }
            }
        } catch (IllegalFilterException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return fc;
    }
    
    /**
     * Clears all individuals from fcStartPoints.
     */
    public void clearAllIndividuals(){
        if (debug) logger.info("Removing all individuals");
        jpFeaturesTable.removeAll(fcStartPoints);
        fcStartPoints.clear();
    }
    
    /**
     * Removes the last-added individuals from fcStartPoints.
     */
    public void clearLastAddedIndividuals(){
        if (debug) logger.info("Removing last added individuals");
        jpFeaturesTable.removeAll(fcLastAddedPoints);
        fcStartPoints.removeAll(fcLastAddedPoints);
        fcLastAddedPoints.clear();
    }
    
    public void setSelectableGISLayersMenu(JMenu jmu){
        jmuSelectedGISLayer = jmu;
    }
    
    public void setRemoveSelectableGISLayersMenu(JMenu jmu){
        jmuRemoveSelectableGISLayers = jmu;
    }
    
    /** 
     * Adds a GIS layer based on a shapefile the user is prompted for.
     */
    public void addSelectableGISLayer(){
        if (debug) logger.info("Adding GIS Layer");
        int res = jfcLayer.showOpenDialog(this);
        if (res==JFileChooser.APPROVE_OPTION) {
            File f = jfcLayer.getSelectedFile();
            createShapefileLayer(f);
        }
    }
    
    /**
     * Adds a layer to the map using the input polygon shapefile f.
     * 
     * @param f - the shapefile
     */
    public void createShapefileLayer(File f) {
        try {
            URL url = f.toURI().toURL();
            ShapefileDataStore store = new ShapefileDataStore(url);
            String name = store.getTypeNames()[0];
            FeatureSource source = store.getFeatureSource(name);

            GeometryAttributeType gt = source.getSchema().getDefaultGeometry();
            Class geometryClass = gt.getType();
//            CoordinateReferenceSystem crs = (CoordinateReferenceSystem) gt.getCoordinateSystem();
//            if (debug) logger.info("CRS for selectable layer is: "+crs.toWKT());
            
            
            Style style;
            if (Polygon.class.isAssignableFrom(geometryClass)
                    || MultiPolygon.class.isAssignableFrom(geometryClass)) {
                Fill fill = sb.createFill(Color.LIGHT_GRAY,0.5);
                Stroke strk = sb.createStroke(Color.BLACK, 1.0);
                PolygonSymbolizer ps = sb.createPolygonSymbolizer(strk, fill);
                style = sb.createStyle(ps);
//                style = sb.createStyle(sb.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 1));
                style.setName("polygon name");
                style.setTitle("polygon title");
           } else {
                //TODO: add a notify descriptor dialog here
                return;
            }

            final MapLayer layer = new DefaultMapLayer(source,style,url.getPath());
            tcMapViewer.addGISLayer(layer);
            selectedLayer = layer;
            selectableLayers.put(layer.getTitle(),layer);
            final JRadioButtonMenuItem jrb = new JRadioButtonMenuItem(layer.getTitle());
            bgSelectableGISLayers.add(jrb);
            jrb.setSelected(true);
            jrb.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    setSelectedLayer(evt.getActionCommand());
                }
            });
            jmuSelectedGISLayer.add(jrb);//add item to menu associated with the action
            jmuSelectedGISLayer.setEnabled(true);
            
            final JMenuItem jmi = new JMenuItem(layer.getTitle());
            jmi.setText(layer.getTitle());
            jmi.setActionCommand(layer.getTitle());
            jmi.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    String ac = evt.getActionCommand();
                    if (debug) logger.info("jmi ActionPerformed for "+ac);
                    //remmove layer identified by action command from the map
                    MapLayer layer = selectableLayers.remove(ac);
                    tcMapViewer.removeGISLayer(layer);
                    //remove the associated menu items 
                    jmuRemoveSelectableGISLayers.remove(jmi);
                    if (jrb.isSelected()) selectedLayer = null;
                    jmuSelectedGISLayer.remove(jrb);
                }
            });
            jmi.setEnabled(true);
            jmi.setVisible(true);
            if (jmuRemoveSelectableGISLayers!=null){
                if (debug) logger.info("Adding JMenuItem to RemoveSelectableGISLayers menu");
                jmuRemoveSelectableGISLayers.add(jmi);//add item to menu associated with the action
                jmuRemoveSelectableGISLayers.setEnabled(true);
            }
        } catch (MalformedURLException ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    f.getAbsolutePath(),
                    "Error opening GIS layer: problem with file name?",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            Exceptions.printStackTrace(ex);
        } catch (java.io.IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    f.getAbsolutePath(),
                    "I/O error creating GIS layer: expected a shapefile.",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            Exceptions.printStackTrace(ex);
        }
    }
    
    /**
     * Enables ability to load initial attributes from csv file.
     * 
     * @param canLoad 
     */
    public void enableLoadAction(boolean canLoad){
        if (canLoad){
            content.add(csvLoader);//add an Openable implementation (CSVLoader) to the instance content
            if (debug) logger.info("----Load enabled!!");
        } else {
            content.remove(csvLoader);//remove an Openable implementation (CSVLoader) from the instance content 
            if (debug) logger.info("----Load disabled!!");
        }
    }
    
    /**
     * Enables ability to save initial attributes to csv file.
     * 
     * @param canSave 
     */
    public void enableSaveAction(boolean canSave){
        if (canSave){
            content.add(csvSaver);//add a Savable implementation to the instance content
            if (debug) logger.info("----Save enabled!!");
        } else {
            content.remove(csvSaver);//remove a Saveable implementation from the instance content 
            if (debug) logger.info("----Save disabled!!");
        }
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement open functionality for the initial attributes.
     */
    private class CSVLoader implements Openable {

        @Override
        public void open() {
            if (debug) logger.info("CSVLoader.open()");
            jfcCSV.setDialogTitle("Select csv file to load initial attributes from:");
            int res = jfcCSV.showOpenDialog(instance);
            if (res==JFileChooser.APPROVE_OPTION){
                try {
                    File openFile = jfcCSV.getSelectedFile();
                    fnCSV = openFile.getPath();
                    loadAttributesFile();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    //------------------------------------------------------------------------//
    /**
     * Private class to implement save functionality for the initial attributes.
     */
    private class CSVSaver implements Savable, SaveAsable {

        @Override
        public void save() throws IOException {
            if (fnCSV.isEmpty()) {
                saveAs();
            } else {
                File saveTo = new File(fnCSV);
                save(saveTo);
            }
        }

        @Override
        public void saveAs() throws IOException {
            jfcCSV.setDialogTitle("Select csv file to save initial attributes to:");
            int res = jfcCSV.showSaveDialog(instance);
            if (res==JFileChooser.APPROVE_OPTION){
                File saveTo = jfcCSV.getSelectedFile();
                fnCSV = saveTo.getPath();
                save(saveTo);
            } else {
                
            }
        }

        private void save(File f) throws IOException {
            InitialAttributesWriter ipm = new InitialAttributesWriter();
            //TODO: need to get ftLHS here?
            ipm.setFeatureType(ftLHS);
            ipm.writePositionFile(f,fcStartPoints);
            StatusDisplayer.getDefault().setStatusText("Saved initial attributes to file.");
            enableSaveAction(false);
        }
    }
}
