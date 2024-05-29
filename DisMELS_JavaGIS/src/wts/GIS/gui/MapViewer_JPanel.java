/*
 * MapViewer_JPanel.java
 *
 * Created on March 20, 2004, 7:17 PM
 */

package wts.GIS.gui;

/**
 *
 * @author  William Stockhausen
 */

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.wtstockhausen.utils.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import org.geotools.cs.CoordinateSystem;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.gui.swing.Legend;
import org.geotools.gui.swing.StatusBar;
import org.geotools.gui.swing.StyledMapPane;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;
import org.geotools.renderer.j2d.RenderedMapScale;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

public class MapViewer_JPanel extends javax.swing.JPanel {
    
    private Legend legend;
    private MapContext context;
    private JProgressBar progressBar;
    private StatusBar statusBar;
    private StyledMapPane mapPane;
    private RenderedMapScale mapScale;
    private Timer timer = null;

    /** Creates new form MapViewer_JPanel */
    public MapViewer_JPanel() throws Exception {
        initComponents();
        context = new DefaultMapContext();
        mapPane = new StyledMapPane();
        mapScale = new RenderedMapScale();
        mapPane.setMapContext(context);
        mapPane.setPaintingWhileAdjusting(false);
        mapPane.getRenderer().addLayer(mapScale);
        
        context.setTitle("Map Layers");
        legend = new Legend(context,"Layers");
        
        // create the status bar and the progress bar
        statusBar = new StatusBar(mapPane);
        progressBar = new JProgressBar(0,100);
        progressBar.setStringPainted(true);
        progressBar.setString("");
        
        splitPane.setLeftComponent(legend);
        splitPane.setRightComponent(mapPane.createScrollPane());
        
        JPanel jpSB = new JPanel();
        jpSB.setLayout(new java.awt.BorderLayout());
        
        jpSB.add(statusBar,BorderLayout.NORTH);
        jpSB.add(progressBar,BorderLayout.SOUTH);
        
        this.add(jpSB,BorderLayout.SOUTH);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        splitPane = new javax.swing.JSplitPane();

        setLayout(new java.awt.BorderLayout());

        add(splitPane, java.awt.BorderLayout.CENTER);

    }//GEN-END:initComponents
    
    private class LoadLayerTask {
        
        private boolean done=false;
        private boolean canceled = false;
        private String statMessage;
        private URL url;
        private String name;
        
        private LoadLayerTask(URL inUrl, String inName){
            url = inUrl;
            name = inName;
        }
        
        private void go() throws Exception {
            final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    done=false;
                    canceled=false;
                    statMessage="loading...";
                    return new ShapefileLoaderTask(url,name);
                }
            };
            worker.start();
        }
        
        private void stop() {
            canceled = true;
            statMessage = null;
        }
        
        private boolean isDone() {
            return done;
        }
        
        private String getMessage() {
            return statMessage;
        }
        
        private class ShapefileLoaderTask {
            private ShapefileLoaderTask(URL url,String name) {
                try {
                    final DataStore store = new ShapefileDataStore(url);
                    final FeatureSource fs = store.getFeatureSource(store.getTypeNames()[0]);

                    // Create the style
                    final StyleBuilder builder = new StyleBuilder();
                    final Style style;
                    GeometryAttributeType gat = fs.getSchema().getDefaultGeometry();
                    Class geometryClass = gat.getType();
                    CoordinateReferenceSystem crs = gat.getCoordinateSystem();
                    if (crs!=null) 
                        System.out.println("input data:\n"+crs.toWKT());
                    if (context!=null) 
                        System.out.println("context:\n"+context.getCoordinateReferenceSystem().toWKT());

                    if (LineString.class.isAssignableFrom(geometryClass)
                            || MultiLineString.class.isAssignableFrom(geometryClass)) {
                        style = builder.createStyle(builder.createLineSymbolizer());
                        style.setName("line name");
                        style.setTitle("line title");
                    } else if (Point.class.isAssignableFrom(geometryClass)
                            || MultiPoint.class.isAssignableFrom(geometryClass)) {
                        style = builder.createStyle(builder.createPointSymbolizer());
                        style.setName("point name");
                        style.setTitle("point title");
                    } else {
                        style = builder.createStyle(builder.createPolygonSymbolizer(Color.LIGHT_GRAY, Color.BLACK, 1));
                        style.setName("polygon name");
                        style.setTitle("polygon title");
                    }

                    final MapLayer layer = new DefaultMapLayer(fs, style, name);

                    context.addLayer(layer);

                    if (context.getLayerCount() == 1) {
                        mapPane.reset();
                    }    
                    done=true;
                } catch (Exception ex) {
                    System.out.println("Exception caught loading shapefile:");
                    System.out.println(ex.toString());
                }
            }
        }    
    }
    
    public void addShapefileLayer(final URL url, String name)
        throws Exception {
        // Load the file
        if (url == null) {
            throw new FileNotFoundException("Resource not found");
        }

        progressBar.setIndeterminate(true);
        final LoadLayerTask task = new LoadLayerTask(url,name);
        timer = new Timer(1000,new ActionListener() {
          public void actionPerformed(ActionEvent evt){
              progressBar.setString("reading shapefile");
              if (task.isDone()) {
                  timer.stop();
                  progressBar.setIndeterminate(false);
                  progressBar.setValue(progressBar.getMinimum());
                  progressBar.setString("");
              }
          }
        });
        task.go();
        timer.start();
    }

    public void removeLayer() {
        MapLayer currentLayer = legend.getSelectedLayer();

        if (currentLayer != null) {
            context.removeLayer(currentLayer);
        }
    }

    public void setLayerVisible() {
        MapLayer currentLayer = legend.getSelectedLayer();

        if (currentLayer != null) {
            currentLayer.setVisible(!currentLayer.isVisible());
        }
    }

    public void setMapScaleVisible() {
        mapScale.setVisible(!mapScale.isVisible());
    }
    
    public void moveLayerUp() {
        MapLayer currentLayer = legend.getSelectedLayer();

        if (currentLayer != null) {
            int position = context.indexOf(currentLayer);

            if (position > 0) {
                context.moveLayer(position, position - 1);
            }
        }
    }

    public void moveLayerDown() {
        MapLayer currentLayer = legend.getSelectedLayer();

        if (currentLayer != null) {
            int position = context.indexOf(currentLayer);

            if (position < (context.getLayerCount() - 1)) {
                context.moveLayer(position, position + 1);
            }
        }
    }
    
    public JProgressBar getProgressBar() {
        return progressBar;
    }
    
    public MapLayer getSelectedLayer(){
        return legend.getSelectedLayer();
    }
    
    public void setCoordinateSystem(CoordinateSystem cs) throws TransformException {
        mapPane.setCoordinateSystem(cs);
        mapPane.reset();
     }
    
    public void setCRS(CoordinateReferenceSystem crs) throws TransformException {
        CoordinateSystem cs = CoordinateSystem.fromGeoAPI(crs);
        mapPane.setCoordinateSystem(cs);
        mapPane.reset();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane splitPane;
    // End of variables declaration//GEN-END:variables
    
}
