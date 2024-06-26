/*
 * MapGUI_JPanel.java
 *
 * Created on December 30, 2005, 1:58 PM
 */

package wts.roms.gis;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.UIManager;
import org.geotools.cs.CoordinateSystem;
import org.geotools.ct.MathTransform;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.gui.swing.Legend;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.j2d.GeoMouseEvent;
import org.geotools.renderer.j2d.RenderedLayer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.netbeans.api.progress.ProgressUtils;
import org.opengis.referencing.operation.TransformException;
import org.openide.util.Exceptions;
import wts.models.utilities.DateTime;
import wts.roms.gui.JPanel_OceanTime;
import wts.roms.model.GlobalInfo;
import wts.roms.model.ModelGrid2D;

/**
 *
 * @author  William Stockhausen
 */
public class MapGUI_JPanel extends javax.swing.JPanel implements PropertyChangeListener {
    
    private final GlobalInfo romsGI;
    
    private ModelGrid2DMapData gridMapData;

    private MapContext context;
    private EditingMapPane mapPane;
//    private GeoMouseEvent geoMousePressed;
    private CoordinateSystem csMapView;
//    private CoordinateSystem csCoordDisplay;
    private MathTransform mtMapToDisplay;

    private MapLayer gridLayer=null;
    private MapLayer maskLayer=null;
    
    private Legend legend;
    private Dimension2D dim;
    
    private static final Logger logger = Logger.getLogger(MapGUI_JPanel.class.getName());
    
//    private JPanel_OceanTime oceanTimeJP;
    
    /** Constructor for class */
    public MapGUI_JPanel() {
        romsGI = GlobalInfo.getInstance();
        initComponents();
        initComponents1();
//        romsGI.addPropertyChangeListener(this);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jpStatus = new javax.swing.JPanel();
        jtfLon = new javax.swing.JTextField();
        jtfLat = new javax.swing.JTextField();
        jtfDepth = new javax.swing.JTextField();
        jtfStatus = new javax.swing.JTextField();
        splitPane = new javax.swing.JSplitPane();
        jpLegend = new javax.swing.JPanel();
        jpDrawing = new javax.swing.JPanel();
        jpMapPanel = new javax.swing.JPanel();
        oceanTimeJP = new wts.roms.gui.JPanel_OceanTime();

        setLayout(new java.awt.BorderLayout());

        jpStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpStatus.setMinimumSize(new java.awt.Dimension(100, 25));
        jpStatus.setPreferredSize(new java.awt.Dimension(100, 25));
        jpStatus.setRequestFocusEnabled(false);
        jtfLon.setEditable(false);
        jtfLon.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jtfLon.setToolTipText("longitude");

        jtfLat.setEditable(false);
        jtfLat.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jtfLat.setToolTipText("latitude");

        jtfDepth.setEditable(false);
        jtfDepth.setToolTipText("depth (m)");

        jtfStatus.setEditable(false);
        jtfStatus.setText("Status");

        org.jdesktop.layout.GroupLayout jpStatusLayout = new org.jdesktop.layout.GroupLayout(jpStatus);
        jpStatus.setLayout(jpStatusLayout);
        jpStatusLayout.setHorizontalGroup(
            jpStatusLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpStatusLayout.createSequentialGroup()
                .add(jtfStatus, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                .add(33, 33, 33)
                .add(jtfLon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 166, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jtfLat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 166, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jtfDepth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 166, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        jpStatusLayout.setVerticalGroup(
            jpStatusLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jpStatusLayout.createSequentialGroup()
                .add(jpStatusLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jtfDepth, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jtfLat, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jtfLon, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jtfStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        add(jpStatus, java.awt.BorderLayout.SOUTH);

        org.jdesktop.layout.GroupLayout jpLegendLayout = new org.jdesktop.layout.GroupLayout(jpLegend);
        jpLegend.setLayout(jpLegendLayout);
        jpLegendLayout.setHorizontalGroup(
            jpLegendLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 100, Short.MAX_VALUE)
        );
        jpLegendLayout.setVerticalGroup(
            jpLegendLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 284, Short.MAX_VALUE)
        );
        splitPane.setLeftComponent(jpLegend);

        jpDrawing.setLayout(new java.awt.BorderLayout());

        jpDrawing.setBackground(new java.awt.Color(204, 204, 204));
        jpMapPanel.setLayout(new java.awt.BorderLayout());

        jpMapPanel.setMinimumSize(new java.awt.Dimension(100, 100));
        jpMapPanel.setPreferredSize(new java.awt.Dimension(100, 100));
        jpDrawing.add(jpMapPanel, java.awt.BorderLayout.CENTER);

        jpDrawing.add(oceanTimeJP, java.awt.BorderLayout.NORTH);

        splitPane.setRightComponent(jpDrawing);

        add(splitPane, java.awt.BorderLayout.CENTER);

    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Re-creates the map pane (mapPane):. 
     * Removes all components from the map panel, then 
     * 1. recreates mapPane
     * 2. adds it to the map panel as a scroll pane
     * 3. hooks up mouse motion listeners
     * 4. sets drag to zoom
     * 5. disables input methods on mapPane (methods enabled when grid is set)
     * 
     * Does not create a legend nor set the context.
     */
    private void resetMapPane(){
        //Create map coordinate system
        PrimeMeridian.setPrimeMeridian(PrimeMeridian.PM_GREENWICH);
        try {
            csMapView = AlbersNAD83.getAlbers();
//            csCoordDisplay = AlbersNAD83.getNAD83();
            mtMapToDisplay = AlbersNAD83.getPtoGtransform();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //remove all existing components from panel
        jpMapPanel.removeAll();
        //Create a StyledMapPane and add it to the JPanel with a scroll pane
        mapPane = new EditingMapPane(csMapView);
        jpMapPanel.add(mapPane.createScrollPane());
        //hook up mouse listeners
        mapPane.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                geoMouseMoved(evt);
            }
        });

        //Set Add/Zoom to Zoom to start
        setMouseDragZooms(true);
        //disable input methods until grid file is set
        mapPane.enableInputMethods(false);
    }
    
    /**
     * Re-creates the Legend object used in the lefthand split pane of the mapPane.
     * Adds the context to the legend on creation.
     */
    private void resetLegend(){
////        java.awt.Component c = splitPane.getLeftComponent();
////        if (c!=null) splitPane.remove(c);
//        legend = new Legend(context,"Layers");
////        splitPane.setLeftComponent(legend);
//        jpLegend.removeAll();
//        jpLegend.add(legend);
    }
    
    /**
     * Creates:
     *  1. the map view coordinate system
     *  2. the Physical-to-Geographic math transformation from map to display (Albers to LatLon)
     *  3. the minimum pixel size
     *  4. the map context
     *  5. the map pane (using resetMapPane)
     *  6. the map legend (using resetLegend)
     */
    private void initComponents1() {
        //make sure Legend has icons it needs
        Icon open = UIManager.getIcon("Tree.openIcon");
        if (open==null) {
            Icon icon= new javax.swing.ImageIcon(getClass().getResource("/wts/roms/gui/resources/icons/Open16.gif"));
            UIManager.put("Tree.openIcon", icon);
        }
        
        dim = new Dimension2D(){
            private double w = 1.0e-5;//old value 1.0e-5
            private double h = 1.0e-5;
            @Override
            public double getWidth() {return w;}

            @Override
            public double getHeight() {return h;}

            @Override
            public void setSize(double width, double height) {
                w = width; h = height;
            }
        };
                
        //Create map context to hold map layers
        context = new DefaultMapContext();
        //create the mapPane to hold the map
        resetMapPane();
        //create the legend 
        resetLegend();
    }
   
    /**
     * Adds layer to "top" of existing layers so that it is painted AFTER all
     * other layers.
     * @param layer
     */
    public void addLayer(MapLayer layer) {
        logger.info("--Adding map layer (at top): "+layer.getTitle());
        Cursor c = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        context.addLayer(layer);
        if (mapPane.getMapContext()==null) setContext();//only set context if not already set
        setCursor(c);
        logger.info("--Added map layer (at top): "+layer.getTitle());
    }

    /**
     * Adds layer at index idx of existing layers so that it is the idx-th layer
     * painted.
     * @param idx - 0-based order at which added layer should be painted
     * @param layer
     */
    public void addLayer(int idx, MapLayer layer) {
        logger.info("--Adding layer at "+idx+": "+layer.getTitle());
        Cursor c = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        context.addLayer(idx,layer);
        if (mapPane.getMapContext()==null) setContext();//only set context if not already set
        setContext();
        setCursor(c);
        logger.info("--Added layer at "+idx+": "+layer.getTitle());
    }

    /**
     * Adds layer to "bottom" of existing layers so that it is painted BEFORE all
     * other layers.
     * @param layer
     */
    public void addLayerAtBottom(MapLayer layer) {
        logger.info("--Adding layer at bottom: "+layer.getTitle());
        Cursor c = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        context.addLayer(0, layer);//add as 1st element in context, as this gets painted first
        for (int i=0;i<context.getLayerCount();i++) {
            logger.info("----map layer "+i+" is '"+context.getLayer(i).getTitle()+"'");
        }
        if (mapPane.getMapContext()==null) setContext();
        setCursor(c);
        logger.info("--Added layer at bottom: "+layer.getTitle());
    }

    /**
     * Adds layer to "top" of existing layers so that it is painted AFTER all
     * other layers.
     * @param layer
     */
    public void addLayerAtTop(MapLayer layer) {
        logger.info("--Adding layer at top: "+layer.getTitle());
        Cursor c = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        int top = context.getLayerCount();
        context.addLayer(top, layer);//add as last element in context, as this gets painted last
        for (int i=0;i<context.getLayerCount();i++) {
            logger.info("----map layer "+i+" is '"+context.getLayer(i).getTitle()+"'");
        }
        if (mapPane.getMapContext()==null) setContext();//only set context if not already set
        setCursor(c);
        logger.info("--Added layer at top: "+layer.getTitle());
    }

    @Override
    public void addMouseListener(MouseListener listener) {
        mapPane.addMouseListener(listener);
    }
    
    @Override
    public void addMouseMotionListener(MouseMotionListener listener) {
        mapPane.addMouseMotionListener(listener);
    }
    
    public ModelGrid2DMapData getGridMapData() {
        return gridMapData;
    }
    
    public Image getMapAsImage() {
        Cursor c = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        int width      = jpDrawing.getWidth();
        int height     = jpDrawing.getHeight();
        Image img      = jpDrawing.createImage(width,height);
        Graphics grImg = img.getGraphics();
        jpDrawing.paintComponents(grImg);
//        jpDrawing.paintAll(grImg);
        setCursor(c);
        return img;
    }
    
    public BufferedImage getMapAsBufferedImage() {
        Cursor c = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        int width      = jpDrawing.getWidth();
        int height     = jpDrawing.getHeight();
        BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D grBI = bi.createGraphics();
        jpDrawing.paintComponents(grBI);
        setCursor(c);
        return bi;
    }
    
    public void saveMapAsImage(File f) throws IOException {
        String fName = f.getName();
        String ext = fName.substring(fName.length()-3);//get extension
        BufferedImage bi = getMapAsBufferedImage();
        ImageIO.write(bi,ext,f);
    }
    
    public void saveMapAsImage(String fName) throws IOException {
        String ext = fName.substring(fName.length()-3);//get extension
        BufferedImage bi = getMapAsBufferedImage();
        File f = new File(fName);
        ImageIO.write(bi,ext,f);
    }
    
    private void geoMouseMoved(java.awt.event.MouseEvent evt) {
        if ((gridLayer!=null)&&(evt instanceof GeoMouseEvent)) {
            GeoMouseEvent gme = null;
            gme = (GeoMouseEvent) evt;
            Point2D dest = gme.getMapCoordinate(null);
            try {
                //transform from map coordinates to display coordinates
                double[] srcPt = new double[]{dest.getX(),dest.getY()};
                double[] dstPt = new double[2];
                mtMapToDisplay.transform(srcPt,0,dstPt,0,1);
                jtfLat.setText(String.valueOf(dstPt[1]));
                jtfLon.setText(String.valueOf(dstPt[0]));
                double z = 
                        gridMapData.interpolateBathymetricDepth(dstPt[0],dstPt[1]);
                jtfDepth.setText(String.valueOf(z));
            } catch(ArrayIndexOutOfBoundsException exc) {
                //do nothing
            } catch (org.opengis.referencing.operation.TransformException ex) {
                ex.printStackTrace();
            } catch (java.lang.NullPointerException exc){
                //invalid mouse coordinates
            }
        }
    }
    
    /**
     * Sets the current MapContext object (context) on the MapPane (mapPane).
     */
    private void setContext(){
        logger.info("Starting setContext()");
        try {
            mapPane.setMapContext(context);
            RenderedLayer[] rls = mapPane.getRenderer().getLayers();
            for (RenderedLayer rl: rls){
                logger.info("rendered layer: "+rl.getName(Locale.US));
                rl.setPreferredPixelSize(dim);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        logger.info("Finished setContext()");
    }
    
    /**
     * Calls resetMapPane() and resetLegend() to recreate MapPane and the Legend objects, 
     * then calls setContext() to set the MapContect (context) on the MapPane and Legend.
     * 
     * Adds existing gridLayer and maskLayer to the context.
     */
    public void refreshMap(){
        logger.info("Starting refreshMap()");
//        context.clearLayerList();
        Runnable r = new Runnable(){
            @Override
            public void run() {
                //re-create mapPane 
                resetMapPane();
                MapLayer[] layers = context.getLayers();
                for (MapLayer layer: layers) logger.info(layer.getTitle()+": "+layer.toString());
                //Create map context to hold map layers
                context = new DefaultMapContext();
                //set context on mapPane
                setContext();
                //re-create legend, hooking it up to context 
                resetLegend();
                //add grid and mask layers
                if (gridLayer!=null) context.addLayer(gridLayer);
                if (maskLayer!=null) context.addLayer(maskLayer);
                validate();
                repaint();
            }
        };
        ProgressUtils.showProgressDialogAndRun(r, "Refreshing map...");
        logger.info("Finished refreshMap()");
    }
    
    /**
     * Reset the map.
     * 
     * Calls resetMapPane() and resetLegend() to recreate MapPane and the Legend objects, 
     * then calls setContext() to set the empty MapContect (context) on the MapPane and Legend.
     * 
     * Does NOT add existing gridLayer and maskLayer to the context 
     */
    public void resetMap(){
        logger.info("Starting resetMap()");
//        context.clearLayerList();
        Runnable r = new Runnable(){
            @Override
            public void run() {
                //re-create mapPane 
                resetMapPane();
                //Create map context to hold map layers
                context = new DefaultMapContext();
                //set context on mapPane
                setContext();
                //re-create legend, hooking it up to context 
                resetLegend();
                //add grid and mask layers
                validate();
                repaint();
            }
        };
        ProgressUtils.showProgressDialogAndRun(r, "Resetting map...");
        logger.info("Finished resetMap()");
    }
 
    /**
     * Maps the current GlobalInfo grid
     *  1. clears the MapContext (context) of all layers
     *  2. creates the gridLayer and maskLayer layers
     *  3. adds the layers to context
     *  4. sets the context (via setContext()) on the MapPane
     *  5. enables input methods on the MapPane
     */
    final public void setGrid() {
        logger.info("Starting setGrid()");
        Cursor c = getCursor();
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Runnable r = new Runnable(){
            @Override
            public void run() {
                resetMap();
                ModelGrid2D mg = romsGI.getGrid2D();
                if (mg!=null){
                    try {
                        //create the grid and mask MapLayers
                        FeatureCollection fc;
                        Style style;
                        StyleBuilder sb = new StyleBuilder();

                        //Make the grid fc
                        gridMapData = new ModelGrid2DMapData();
                        fc = gridMapData.getGridLines(5);
                        logger.info("line bounds: "+fc.getBounds().toString());
            //            System.out.println("grid coordinate system: "+
            //                    fc.getFeatureType().getDefaultGeometry().getCoordinateSystem().getName());
                        style = sb.createStyle(sb.createLineSymbolizer(Color.RED,1.0));
                        gridLayer = new DefaultMapLayer(fc,style,"model grid");
                        context.addLayer(gridLayer);

                        //and the mask fc
                        fc = gridMapData.getMask();
                        logger.info("polygon bounds: "+fc.getBounds().toString());
                        style = sb.createStyle(sb.createPolygonSymbolizer(Color.GRAY));
                        maskLayer = new DefaultMapLayer(fc,style,"land mask");
                        context.addLayer(maskLayer);

                        setContext();
                        validate();
                        repaint();
                        //enable input methods on mapPane
                        mapPane.enableInputMethods(true);
                    } catch (IllegalAttributeException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (SchemaException ex) {
                        Exceptions.printStackTrace(ex);
                    } catch (Exception ex) {
                        Exceptions.printStackTrace(ex);
                    }
                } else {
                    //enable input methods on mapPane
                    mapPane.enableInputMethods(false);
                }
            }
        };
        ProgressUtils.showProgressDialogAndRun(r, "Setting ROMS grid layers...");
        setCursor(c);
        logger.info("Finished setGrid()");
    }
    
    public boolean getMouseDragZooms() {
        return mapPane.getMouseDragZooms();
    }
    
    public void setMouseDragZooms(boolean b) {
        mapPane.setMouseDragZooms(b);
    }
 
    public void setStatusString(String s) {
        jtfStatus.setText(s);
    }
    
    public JPanel_OceanTime getOceanTimeJP() {
        return oceanTimeJP;
    }
    
    public void setOceanTime(long time) {
        oceanTimeJP.setOceanTime(time);
    }
    
    public void setOceanTime(double time) {
        oceanTimeJP.setOceanTime(time);
    }
    
    public void setOceanTime(DateTime dt) {
        oceanTimeJP.setDateTime(dt);
    }
    
    public void setOceanTimeVisible(boolean b) {
        oceanTimeJP.setVisible(b);
    }
    
    public DateTime getRefDate() {
        return oceanTimeJP.getRefDate();
    }
    public void setRefDate(DateTime dt) {
        oceanTimeJP.setRefDate(dt);
    }
    
    public double[] transformFromMapCStoDisplayCS(double[] mapPt) throws TransformException {
        double[] dspPt = new double[mapPt.length];
        mtMapToDisplay.transform(mapPt,0,dspPt,0,1);
        return dspPt;
    }

    public void removeLayer(MapLayer layer) {
        context.removeLayer(layer);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jpDrawing;
    private javax.swing.JPanel jpLegend;
    private javax.swing.JPanel jpMapPanel;
    private javax.swing.JPanel jpStatus;
    private javax.swing.JTextField jtfDepth;
    private javax.swing.JTextField jtfLat;
    private javax.swing.JTextField jtfLon;
    private javax.swing.JTextField jtfStatus;
    private wts.roms.gui.JPanel_OceanTime oceanTimeJP;
    private javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        logger.info("PropertyChange: "+pce.toString());
        if (pce.getSource().equals(romsGI)){
            if (pce.getPropertyName().equals(GlobalInfo.PROP_GridFile)){
//                setGrid(); <-want this to come through MapViewerTopComponent
            }
        }
    }
    
}
