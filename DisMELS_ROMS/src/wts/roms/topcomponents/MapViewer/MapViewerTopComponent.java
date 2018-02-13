/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.roms.topcomponents.MapViewer;

import com.vividsolutions.jts.geom.*;
import com.wtstockhausen.utils.FileFilterImpl;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.*;
import org.netbeans.api.settings.ConvertAsProperties;
import org.opengis.referencing.operation.TransformException;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import wts.GIS.shapefile.ShapefileCreator;
import wts.roms.gis.ModelGrid2DMapData;
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
    "CTL_MapViewerTopComponent=Map Viewer",
    "HINT_MapViewerTopComponent=Map Viewer"
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
    private GlobalInfo romsGI;
    
    /** file chooser for GIS layers */
    private final JFileChooser jfcLayer = new JFileChooser();
    /** file chooser for output images */
    private final JFileChooser jfcImage = new JFileChooser();
    /** file chooser for output shapefiles */
    private final JFileChooser jfcShape = new JFileChooser();
    
    /** Geotools 2.0 style builder for shapefile layers */
    private final StyleBuilder sb = new StyleBuilder();
    
    /** map of layers by title */
    private final TreeMap<String,MapLayer> mapLayers = new TreeMap<>();
    
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
     * 
     * Calls loadGridFile() if doOnOpen is true.
     */
    @Override
    public void componentOpened() {
        logger.info("Starting componentOpened()");
//        if (doOnOpen) loadGridFile();
        if (doOnOpen) mapGUI.setGrid();
        logger.info("Finished componentOpened()");
    }

    /**
     * Method called when this TopComponent's window is closed in the application.
     * This could happen multiple times before the application is closed.
     */
    @Override
    public void componentClosed() {
        logger.info("Starting compnonentClosed()");
        logger.info("Finished compnonentClosed()");
    }
    
    /**
     * Gets the  ModelGrid2DMapData associated with the map
     * @return 
     */
    public ModelGrid2DMapData getGrid2DMapData(){
        return mapGUI.getGridMapData();
    }
    
    /**
     * Gets the ModelGrid2D associated with the map
     * @return 
     */
    public ModelGrid2D getModelGrid(){
        return romsGI.getGrid2D();
    }

    private void initComponents1() {
        doOnOpen = true;//flag to load grid in componentOpened
        romsGI = GlobalInfo.getInstance();
        romsGI.addPropertyChangeListener(this);
        String wdFN = romsGI.getGridFile();
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

        FileFilter fileFilter2 = new FileFilterImpl("shp","Shape files");
        jfcShape.addChoosableFileFilter(fileFilter2);
        jfcShape.setFileFilter(fileFilter1);
        jfcShape.setCurrentDirectory(wdF);
        jfcShape.setDialogTitle("Save as ESRI shapefile");
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
    
    /**
     * Calls refreshMap() method on the MapGUI_JPanel object (mapGUI) to refresh
     * the map to the current context. This re-creates the MapPane and Legend objects
     * in MapGUI_JPanel, and adds the current grid layers to the context.
     */
    public void refreshMap(){
        logger.info("Starting refreshMap()");
        mapGUI.refreshMap();
        logger.info("Finished refreshMap()");
    }
    
    /**
     * Calls resetMap() method on the MapGUI_JPanel object (mapGUI) to reset
     * the map to the current context. This re-creates the MapPane and Legend objects
     * in MapGUI_JPanel.
     */
    public void resetMap(){
        logger.info("Starting resetMap()");
//        mapGUI.resetMap();
        remove(jpMap);
        initComponents();
        mapGUI.setGrid();
        logger.info("Finished resetMap()");
    }

    /**
     * Sets the menu item allowing removal of manual removal of
     * GIS layers from the map.
     * 
     * @param jmu - the JMenu object to set (assign as jmuGISLayers)
     */
    public void setRemoveGISLayersMenu(JMenu jmu){
        logger.info("Setting RemoveGISLayersMenu");
        jmuGISLayers = jmu;
        logger.info("Done setting RemoveGISLayersMenu");
    }
    
    /** 
     * Adds a GIS layer to the map, based on a shapefile the user is prompted for.
     */
    public void addGISLayer(){
        int res = jfcLayer.showOpenDialog(this);
        if (res==JFileChooser.APPROVE_OPTION) {
            File f = jfcLayer.getSelectedFile();
            createLayerFromShapefile(f);
        }
    }
    
    /**
     * Adds the input layer to the map.
     * 
     * @param layer 
     */
    public void addGISLayer(MapLayer layer){
        logger.info("addGISLayer(MapLayer): Adding GIS layer: "+layer.getTitle());
        Set<String> ks = mapLayers.keySet();
        logger.info("--mapLayers before add:");
        for (String k : ks) logger.info("----"+k);
        mapLayers.put(layer.getTitle(),layer);
        mapGUI.addLayerAtTop(layer);
        mapGUI.repaint();
        logger.info("--mapLayers after add:");
        ks = mapLayers.keySet();
        for (String k : ks) logger.info("----"+k);
        logger.info("addGISLayer(MapLayer): Added GIS layer: "+layer.getTitle());
    }
    
    /**
     * Adds the input layer to the base of the map.
     * 
     * @param layer 
     */
    public void addGISLayerAtBase(MapLayer layer){
        logger.info("addGISLayerAtBase(MapLayer): Adding GIS layer: "+layer.getTitle());
        Set<String> ks = mapLayers.keySet();
        logger.info("--mapLayers before add:");
        for (String k : ks) logger.info("----"+k);
        mapLayers.put(layer.getTitle(),layer);
        mapGUI.addLayerAtBottom(layer);
        mapGUI.repaint();
        logger.info("--mapLayers after add:");
        ks = mapLayers.keySet();
        for (String k : ks) logger.info("----"+k);
        logger.info("addGISLayerAtBase(MapLayer): Added GIS layer: "+layer.getTitle());
    }
    
    /**
     * Removes the layer from the map.
     * 
     * @param layer - name of layer to remove
     */
    public void removeGISLayer(String layer){
        logger.info("removeGISLayer(String layer): Removing GIS layer: "+layer);
        MapLayer mapLayer = mapLayers.remove(layer);
        mapGUI.removeLayer(mapLayer);
        mapGUI.repaint();
        logger.info("removeGISLayer(String layer): Removed GIS layer: "+layer);
    }
    
    /**
     * Removes the layer from the map.
     * 
     * @param layer - the layer to remove
     */
    public void removeGISLayer(MapLayer layer){
        logger.info("removeGISLayer(MapLayer layer): Removing GIS layer: "+layer.getTitle());
        mapLayers.remove(layer.getTitle());
        mapGUI.removeLayer(layer);
        mapGUI.repaint();
        logger.info("removeGISLayer(MapLayer layer): Removed GIS layer: "+layer.getTitle());
    }
    
    /**
     * Adds a layer to the map using the input shapefile f.
     * 
     * @param f - the shapefile
     */
    public void createLayerFromShapefile(File f) {
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
            logger.severe(ex.getLocalizedMessage());
        } catch (java.io.IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    f.getAbsolutePath(),
                    "I/O error creating GIS layer: expected a shapefile.",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            logger.severe(ex.getLocalizedMessage());
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
    
    /**
     * Reacts to the following PropertyChangeEvents:
     *  1. GlobalInfo.PROP_GridFIle
     *      a. if the component is opened, calls loadGridFile() to show the new grid on the map
     *      b. otherwise, sets a flag to call loadGridFile() when it does open
     *  Note: this removes all GIS layers from the map and ONLY adds the new grid and mask layers
     * 
     * @param evt 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GlobalInfo.PROP_GridFile)){
            logger.info("PropertyChange--grid file is : '"+romsGI.getGridFile()+"'");
            //load the grid file (if component is opened) or flag it to load on opening
            if (isOpened()) resetMap(); else doOnOpen = true;
//            if (!isOpened()) doOnOpen = true;
        }
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
    
    /**
     * Create a polyline shapefile from the model grid.
     */
    public void createLineShapefile(){
        jfcShape.setDialogTitle("Select output shapefile for grid lines");
        int res = jfcShape.showSaveDialog(this);
        if (res==JFileChooser.APPROVE_OPTION) {
            try {
                File fshp = jfcShape.getSelectedFile();                                                      
                URL url = fshp.toURI().toURL();
                FeatureCollection fc = mapGUI.getGridMapData().getGridLines();
                ShapefileCreator sc = new ShapefileCreator();
                sc.setShapefileURL(url);
                sc.createShapefile(fc);
            } catch (MalformedURLException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (SchemaException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (IllegalAttributeException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (TransformException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void createMaskShapefile(){
        jfcShape.setDialogTitle("Select output shapefile for land mask");
        int res = jfcShape.showSaveDialog(this);
        if (res==jfcShape.APPROVE_OPTION) {
            try {
                File fshp = jfcShape.getSelectedFile();
                URL url = fshp.toURI().toURL();
                FeatureCollection fc = mapGUI.getGridMapData().getMask();
                ShapefileCreator sc = new ShapefileCreator();
                sc.setShapefileURL(url);
                sc.createShapefile(fc);
            } catch (MalformedURLException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (SchemaException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (IllegalAttributeException ex) {
                logger.log(Level.SEVERE, null, ex);
            } catch (TransformException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }                                                      
    
    public void createShapefileFromLayer(String layerName){
         MapLayer layer = mapLayers.get(layerName);
         if (layer!=null){
            jfcShape.setDialogTitle("Select output shapefile for layer");
            int res = jfcShape.showSaveDialog(this);
            if (res==jfcShape.APPROVE_OPTION) {
                try {
                    File fshp = jfcShape.getSelectedFile();
                    URL url = fshp.toURI().toURL();
                    FeatureCollection fc = layer.getFeatureSource().getFeatures();
                    ShapefileCreator sc = new ShapefileCreator();
                    sc.setShapefileURL(url);
                    sc.createShapefile(fc);
                } catch (MalformedURLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
         }
    }                                                      
}
