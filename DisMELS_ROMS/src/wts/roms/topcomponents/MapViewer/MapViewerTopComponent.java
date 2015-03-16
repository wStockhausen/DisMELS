/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.MapViewer;

import com.vividsolutions.jts.geom.*;
import com.wtstockhausen.utils.FileFilterImpl;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.*;
import org.netbeans.api.progress.ProgressUtils;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import wts.roms.gis.AlbersNAD83;
import wts.roms.model.GlobalInfo;
import wts.roms.model.ModelGrid2D;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//wts.roms.topcomponents.MapViewer//MapViewer//EN",
autostore = false)
@TopComponent.Description(preferredID = "MapViewerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "wts.roms.topcomponents.MapViewer.MapViewerTopComponent")
//@ActionReference(path = "Menu/Window" /*
// * , position = 333
// */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_MapViewerAction",
preferredID = "MapViewerTopComponent")
@Messages({
    "CTL_MapViewerAction=Map Viewer",
    "CTL_MapViewerTopComponent=Map viewer",
    "HINT_MapViewerTopComponent=Map viewer"
})
public final class MapViewerTopComponent extends TopComponent implements PropertyChangeListener {
    
    /** singleton object */
    private static MapViewerTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "MapViewerTopComponent";
    
    /** logger */
    static private Logger logger = Logger.getLogger(MapViewerTopComponent.class.getName()); 

    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized MapViewerTopComponent getDefault(){
        if (instance==null){
            instance = new MapViewerTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized MapViewerTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof MapViewerTopComponent) {
            return (MapViewerTopComponent) win;
        }
        Logger.getLogger(MapViewerTopComponent.class.getName()).warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }
    
    /** GlobalInfo singleton */
    private GlobalInfo globalInfo;
    
    /** file chooser for GIS layers */
    private JFileChooser jfcLayer = new JFileChooser();
    /** file chooser for GIS layers */
    private JFileChooser jfcImage = new JFileChooser();
    
    /** Geotools 2.0 style builder for shapefile layers */
    private StyleBuilder sb = new StyleBuilder();
    
    /** map of layers by title */
    private TreeMap<String,MapLayer> mapLayers = new TreeMap<>();
    
    /** JMenu for removable GIS layers */
    private JMenu jmuGISLayers = null;
    
    /** flag to postpone operations if false */
    private boolean doOnOpen = true;
    
    /** 
     * Counter for associated TopComponent "partners".
     * The MapViewer instance can close when partners = 0.
     * "Partner" TopComponents should call instance.addParther() after opening
     * the MapViewer instance to indicate their partnership.
     * Partner TopComponents should call instance.removeParther() and 
     * check instance.canClose() prior to closing the MapViewer instance 
     * to see if other partners exist.
     */
    private int partners = 0;

    public MapViewerTopComponent() {
        logger.info("------Start instantiating MapViewer");
        initComponents();
        initComponents1();
        setName(Bundle.CTL_MapViewerTopComponent());
        setToolTipText(Bundle.HINT_MapViewerTopComponent());
        
        //add the actionMap and 'this' to the global lookup
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this)));
        logger.info("------Done instantiating MapViewer");
    }
    
    /** 
     * Increment counter for associated TopComponent "partners".
     * The MapViewer instance can close when partners = 0.
     * "Partner" TopComponents should call instance.addParther() after opening
     * the MapViewer instance to indicate their partnership.
     * Partner TopComponents should call instance.removeParther() and 
     * check instance.canClose() prior to closing the MapViewer instance 
     * to see if other partners exist.
     */
    public void addPartner(){
        partners++;
    }
    
    /** 
     * Decrement counter for associated TopComponent "partners".
     * The MapViewer instance can close when partners = 0.
     * "Partner" TopComponents should call instance.addParther() after opening
     * the MapViewer instance to indicate their partnership.
     * Partner TopComponents should call instance.removeParther() and 
     * check instance.canClose() prior to closing the MapViewer instance 
     * to see if other partners exist.
     */
    public void removePartner(){
        partners--;
    }

    /**
     * Indicates when the MapViewer instance can close. This is true when all 
     * partners have been removed.  TopComponents which have added themselves as partners
     * should remove themselves as partners prior to checking this.
     * 
     * @return - boolean indicating whether the instance should be allowed to close. 
     */
    @Override
    public boolean canClose(){
        logger.info("canClose(). partners = "+partners);
        return (partners<=0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpMap = new javax.swing.JPanel();
        mapGUI = new wts.roms.gis.MapGUI_JPanel();

        setLayout(new java.awt.BorderLayout());

        jpMap.setLayout(new java.awt.BorderLayout());
        jpMap.add(mapGUI, java.awt.BorderLayout.CENTER);

        add(jpMap, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jpMap;
    private wts.roms.gis.MapGUI_JPanel mapGUI;
    // End of variables declaration//GEN-END:variables

    /**
     * Method called when this TopComponent's window is opened in the application.
     * This could happen multiple times before the application is closed.
     */
    @Override
    public void componentOpened() {
        logger.info("starting MapViewer.compnonentOpened()");
        if (doOnOpen) loadGridFile();
    }

    /**
     * Method called when this TopComponent's window is closed in the application.
     * This could happen multiple times before the application is closed.
     */
    @Override
    public void componentClosed() {
        logger.info("starting MapViewer.compnonentClosed()");
        logger.info("finished MapViewer.compnonentClosed()");
    }
    
    /**
     * Gets the ModelGrid2D associated with the map
     * @return 
     */
    public ModelGrid2D getModelGrid(){
        return mapGUI.getGridMapData().getGrid2D();
    }

    /**
     * Load the grid file.
     */
    private void loadGridFile() {
        Runnable r = new Runnable(){
            @Override
            public void run() {
                String grdFN = globalInfo.getGridFile();
                Cursor c = getCursor();
                try {
                    if ((!grdFN.equals("<not set>"))&&(wts.roms.model.ModelGrid2D.isGrid(grdFN))) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                        mapGUI.setGridFileName(grdFN);
                        mapGUI.validate();
                        mapGUI.repaint();
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(
                                null,
                                grdFN,
                                "Error loading ROMS grid file: this is not a grid file:",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                } catch (java.lang.Exception ex) {
                    javax.swing.JOptionPane.showMessageDialog(
                            null,
                            grdFN,
                            "Error loading grid file",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                    Exceptions.printStackTrace(ex);
                }
                setCursor(c);
                doOnOpen = false;
            }
        };
        ProgressUtils.showProgressDialogAndRun(r, "Reading ROMS grid info...");
    }

    private void initComponents1() {
        doOnOpen = true;//flag to load grid in componentOpened
        globalInfo = GlobalInfo.getInstance();
        globalInfo.addPropertyChangeListener(this);
        String wdFN = globalInfo.getGridFile();
        File wdF = new File(wdFN);

        FileFilter fileFilter = new FileFilterImpl("shp","Shape files");
        jfcLayer.addChoosableFileFilter(fileFilter);
        jfcLayer.setFileFilter(fileFilter);
        jfcLayer.setCurrentDirectory(wdF);
        jfcLayer.setDialogTitle("Open an ESRI shapefile");

        FileFilter fileFilter1 = new FileFilterImpl(new String[]{"jpg","gif","png"},"Image files");
        jfcImage.addChoosableFileFilter(fileFilter1);
        jfcImage.setFileFilter(fileFilter1);
        jfcImage.setCurrentDirectory(wdF);
        jfcImage.setDialogTitle("Save map as image");

    }
    
    /**
     * Adds listener on geo mouse motion events.
     * 
     * @param listener 
     */
    public void addGeoMouseMotionListener(MouseMotionListener listener){
        mapGUI.addMouseMotionListener(listener);
    }
    
    /**
     * Removes listener on geo mouse motion events.
     * 
     * @param listener 
     */
    public void removeGeoMouseMotionListener(MouseMotionListener listener){
        mapGUI.removeMouseMotionListener(listener);
    }
    
    /**
     * Add listener on geo mouse events (e.g. press, release).
     * 
     * @param listener 
     */
    public void addGeoMouseListener(MouseListener listener){
        mapGUI.addMouseListener(listener);
    }
    
    /**
     * Removes listener on geo mouse events (e.g. press, release).
     * 
     * @param listener 
     */
    public void removeGeoMouseListener(MouseListener listener){
        mapGUI.removeMouseListener(listener);
    }

    public void setRemoveGISLayersMenu(JMenu jmu){
        logger.info("Setting RemoveGISLayersMenu");
        jmuGISLayers = jmu;
    }
    
    /** 
     * Adds a GIS layer based on a shapefile the user is prompted for.
     */
    public void addGISLayer(){
        int res = jfcLayer.showOpenDialog(this);
        if (res==JFileChooser.APPROVE_OPTION) {
            File f = jfcLayer.getSelectedFile();
            createShapefileLayer(f);
        }
    }
    
    /**
     * Adds the input layer to the map.
     * 
     * @param layer 
     */
    public void addGISLayer(MapLayer layer){
        mapLayers.put(layer.getTitle(),layer);
        mapGUI.addLayer(layer);
    }
    
    /**
     * Adds the input layer to the base of the map.
     * 
     * @param layer 
     */
    public void addGISLayerAtBase(MapLayer layer){
        mapLayers.put(layer.getTitle(),layer);
        mapGUI.addLayerAtBottom(layer);
    }
    
    /**
     * Removes the layer from the map.
     * 
     * @param layer - name of layer to remove
     */
    public void removeGISLayer(String layer){
        MapLayer mapLayer = mapLayers.remove(layer);
        mapGUI.removeLayer(mapLayer);
    }
    
    /**
     * Removes the layer from the map.
     * 
     * @param layer - the layer to remove
     */
    public void removeGISLayer(MapLayer layer){
        mapLayers.remove(layer.getTitle());
        mapGUI.removeLayer(layer);
    }
    
    /**
     * Adds a layer to the map using the input shapefile f.
     * 
     * @param f - the shapefile
     */
    public void createShapefileLayer(File f) {
        try {
            URL url = f.toURI().toURL();
            ShapefileDataStore store = new ShapefileDataStore(url);
            String name = store.getTypeNames()[0];
            FeatureSource source = store.getFeatureSource(name);

            Style style;
            Class geometryClass = source.getSchema().getDefaultGeometry().getType();

            if (LineString.class.isAssignableFrom(geometryClass)
                    || MultiLineString.class.isAssignableFrom(geometryClass)) {
                style = sb.createStyle(sb.createLineSymbolizer(Color.BLUE,2));
                style.setName("line name");
                style.setTitle("line title");
            } else if (Point.class.isAssignableFrom(geometryClass)
                    || MultiPoint.class.isAssignableFrom(geometryClass)) {
                style = sb.createStyle(sb.createPointSymbolizer());
                style.setName("point name");
                style.setTitle("point title");
            } else {
                style = sb.createStyle(sb.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 1));
                style.setName("polygon name");
                style.setTitle("polygon title");
           }

            final MapLayer layer = new DefaultMapLayer(source,style,url.getPath());
            mapGUI.addLayer(layer);
            mapLayers.put(layer.getTitle(),layer);
            logger.info("Creating JMenuItem to remove "+layer.getTitle());
            JMenuItem jmi = new JMenuItem(layer.getTitle());
            jmi.setText(layer.getTitle());
            jmi.setActionCommand(layer.getTitle());
            jmi.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    logger.info("jmi ActionPerformed for "+evt.getActionCommand());
                    MapLayer layer = mapLayers.remove(evt.getActionCommand());
                    mapGUI.removeLayer(layer);
                    jmuGISLayers.remove((JMenuItem)evt.getSource());
                }
            });
            jmi.setEnabled(true);
            jmi.setVisible(true);
            if (jmuGISLayers!=null){
                logger.info("Adding JMenuItem to RemoveGISLayers menu");
                jmuGISLayers.add(jmi);//add item to menu associated with the action
                jmuGISLayers.setEnabled(true);
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
     * Sets the ROMS ocean time as a double.
     * 
     * @param t 
     */
    public void setOceanTime(double t){
        mapGUI.setOceanTime(t);
    }
    
    public void setMouseDragZooms(boolean b){
        this.mapGUI.setMouseDragZooms(b);
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
    
    //TODO: need to be able to load grid after changes to grid file name

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GlobalInfo.PROP_GridFile)){
            logger.info("PropertyChange--grid file");
            if (isOpened()) loadGridFile(); else doOnOpen = true;
        }
    }
    
    /**
     * Returns the bathymetric depth corresponding to the given location.
     * @param pt - location (in lon/lat) as Point
     * @return - the interpolated bathymetric depth (m)
     */
    public double interpolateBathymetricDepth(double lon, double lat){
        double[] trPt = AlbersNAD83.transformPtoG(new double[]{lon,lat});
        double bd = Math.abs(mapGUI.interpolateBathymetricDepth(trPt[0],trPt[1]));
        return bd;
    }
    
    /**
     * Saves map as an image file.  The user is prompted for a file using a JFileChooser.
     */
    public void saveMapAsImage() throws IOException{
        int res = jfcImage.showSaveDialog(mapGUI);
        if (res==JFileChooser.APPROVE_OPTION){
            File f = jfcImage.getSelectedFile();
            mapGUI.saveMapAsImage(f);
        }
    }
    
    /**
     * Saves map as an image file.
     * 
     * @param fn - filename to save image to
     */
    public void saveMapAsImage(String fn) throws IOException{
        mapGUI.saveMapAsImage(fn);
    }
}
