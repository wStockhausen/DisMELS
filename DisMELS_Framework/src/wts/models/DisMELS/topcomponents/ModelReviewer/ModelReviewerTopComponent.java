/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.topcomponents.ModelReviewer;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.wtstockhausen.utils.FileFilterImpl;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.*;
import org.geotools.filter.*;
import org.geotools.map.DefaultMapLayer;
import org.geotools.map.MapLayer;
import org.geotools.styling.*;
import org.netbeans.api.actions.Openable;
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
import wts.models.DisMELS.events.AnimationEvent;
import wts.models.DisMELS.events.AnimationEventListener;
import wts.models.DisMELS.events.MediaToolBarEvent;
import wts.models.DisMELS.events.MediaToolBarEventListener;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.LHS_Factory;
import wts.models.DisMELS.framework.LifeStageAttributesInterface;
import wts.models.utilities.MathFunctions;
import wts.roms.topcomponents.MapViewer.MapViewerTopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//wts.models.DisMELS.topcomponents.ModelReviewer//ModelReviewer//EN",
autostore = false)
@TopComponent.Description(preferredID = "ModelReviewerTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "wts.models.DisMELS.topcomponents.ModelReviewer.ModelReviewerTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ModelReviewerAction",
preferredID = "ModelReviewerTopComponent")
@Messages({
    "CTL_ModelReviewerAction=ModelReviewer",
    "CTL_ModelReviewerTopComponent=IBM Results Reviewer",
    "HINT_ModelReviewerTopComponent=This is the IBM Results Reviewer window"
})
public final class ModelReviewerTopComponent extends TopComponent 
                                            implements AnimationEventListener, MediaToolBarEventListener, PropertyChangeListener {
    
    /** singleton object */
    private static ModelReviewerTopComponent instance = null;
    /** the preferred id should be same as above*/
    public static final String PREFERRED_ID = "ModelReviewerTopComponent";

    /** logger for operating messages */
    private static final Logger logger = Logger.getLogger(ModelReviewerTopComponent.class.getName());

    /**
     * Gets the default instance.  DO NOT USE DIRECTLY: reserved for settings files only.
     * To obtain the singleton instance, use {@link #findInstance}.
     * @return 
     */
    public static synchronized ModelReviewerTopComponent getDefault(){
        if (instance==null){
            instance = new ModelReviewerTopComponent();
        }
        return instance;
    }
    
    /**
     * Returns the singleton instance.  Never use {@link #getDefault() } directly.
     * 
     * @return 
     */
    public static synchronized ModelReviewerTopComponent findInstance(){
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win==null){
            return getDefault();
        } 
        if (win instanceof ModelReviewerTopComponent) {
            return (ModelReviewerTopComponent) win;
        }
        Logger.getLogger(ModelReviewerTopComponent.class.getName()).warning("There seem to be multiple components with the PREFERRED_ID "+
                "'"+PREFERRED_ID+"'.  This is a potential source of errors and an unexpected result.");
        return getDefault();
    }
    
    /** GlobalInfo singleton */
    private GlobalInfo globalInfo = null;

    private String fnCSV = "";
    private JFileChooser jfcCSV;
    private JFileChooser shpJFC = new JFileChooser();
    
    private StyleBuilder sb = new StyleBuilder();
    private  FilterFactory ff = FilterFactory.createFilterFactory();
    
    private long skip = 0;
    private long stride = 250000;
    private File ptsF;
//    private File sgsF;
    /** the shapefile creator for the points layer */
    private final ShapefileCreatorPoints scp;
    /** the shapefile creator for the track segments layer */
//    private final ShapefileCreatorSegments scs;
    /** the points layer */
    private MapLayer ptsLayer = null;
    /** the track segments layer */
//    private MapLayer sgsLayer = null;    
    
    private TreeSet<Double> times = new TreeSet<>();
    private Double[] vTimes = null;
    private int iTime = 0;
    private Timer timer;
    
    /** flag to save animation during model run to a series of files */
    private boolean saveAnimation = false;
    /** counter for animation files */
    private int animCtr = 0;
    /** filename base for animation files */
    private String animBase = "Anim_";
    
    /** the singleton instance of the MapViewerTopComponent */
    private MapViewerTopComponent tcMapViewer = null;

    /** instance content reflecting csvLoader capabilities */
    private InstanceContent content;
    
    /** instance of the private class for loading the initial attributes from csv */
    private CSVLoader csvLoader;
    
    public ModelReviewerTopComponent() {
        
        initComponents();
        initComponents1();
        setName(Bundle.CTL_ModelReviewerTopComponent());
        setToolTipText(Bundle.HINT_ModelReviewerTopComponent());
        
        //add the actionMap, the instance content (wrapped in an AbstractLookup) and 'this' to the global lookup
        content = new InstanceContent();//will reflect csvLoader capabilities
        associateLookup(new ProxyLookup(Lookups.fixed(getActionMap(),this),new AbstractLookup(content)));
        
        //the shapefile creators
        scp = new ShapefileCreatorPoints();
//        scs = new ShapefileCreatorSegments();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mtb = new wts.models.DisMELS.gui.MediaToolBar();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mtb, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mtb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private wts.models.DisMELS.gui.MediaToolBar mtb;
    // End of variables declaration//GEN-END:variables
    
    /**
     * Private method called during instantiation to set up class variables.
     */
    private void initComponents1() {
        globalInfo = GlobalInfo.getInstance();
        globalInfo.addPropertyChangeListener(this);
        String wdFN = globalInfo.getWorkingDir();
        File wdF = new File(wdFN);

        csvLoader = new CSVLoader();

        //set up file choosers
        FileFilter filter = new FileFilterImpl("csv","csv files");
        jfcCSV = new JFileChooser();
        jfcCSV.addChoosableFileFilter(filter);
        jfcCSV.setFileFilter(filter);
        jfcCSV.setCurrentDirectory(wdF);
        
        this.validate();
    }
    
    @Override
    public void componentOpened() {
        //make sure MapViewerTopComponent is open
        WindowManager.getDefault().invokeWhenUIReady(new Runnable() {
            @Override
            public void run() {
                tcMapViewer = MapViewerTopComponent.findInstance();
                tcMapViewer.open();
                tcMapViewer.addPartner();
                tcMapViewer.requestActive();
                requestActive();
            }
        });
        enableLoadAction(true);
        mtb.addMediaToolBarEventListener(this);
        logger.info("finished compnonentOpened()");
    }

    @Override
    public void componentClosed() {
        logger.info("starting compnonentClosed()");
        mtb.removeMediaToolBarEventListener(this);
        if (ptsLayer!=null) tcMapViewer.removeGISLayer(ptsLayer);
//        if (sgsLayer!=null) tcMapViewer.removeGISLayer(sgsLayer);
        tcMapViewer.removePartner();
        if (tcMapViewer.canClose()) tcMapViewer.close();
        logger.info("finished compnonentClosed()");
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
     * Loads the results file
     */
    public void loadAttributesFile() {
        try {
            boolean create = true;
            List<LifeStageAttributesInterface> lhs = LHS_Factory.createAttributesFromCSV(fnCSV,skip,stride,create);
            int ctr = 0;
            while (lhs.size()>0) {
                System.out.println("Loading attributes file: counter = "+(ctr++));
                Iterator<LifeStageAttributesInterface> it = lhs.iterator();
                ArrayList<Feature> fv = new ArrayList<>();
                while (it.hasNext()) {
                    fv.add(LHS_Factory.createPointFeature(it.next()));
                }
                if (create) {
                    createShapefiles(fv);
                    create = false;
                } else {
                    addToShapefiles(fv);
                }
                lhs = LHS_Factory.createAttributesFromCSV(fnCSV,skip,stride,create);
            }
            scp.closeShapefile();//close the shapefile
//            scs.closeShapefile();//close the shapefile
            loadPointsLayer(ptsF);//ptsF assigned in createShapefiles(...);
//            loadSegsLayer(sgsF);  //sgsF assigned in createShapefiles(...);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InstantiationException | IllegalAccessException | IllegalAttributeException | SchemaException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private void loadPointsLayer(File file) {
        try {
            URL url = file.toURI().toURL();
            ShapefileDataStore store = new ShapefileDataStore(url);
            String name = store.getTypeNames()[0];
            FeatureSource source = store.getFeatureSource(name);
            FeatureType ft = source.getSchema();
            Class geometryClass = ft.getDefaultGeometry().getType();
            
            FeatureCollection fc = source.getFeatures();
            FeatureIterator fit = fc.features();
            int t = store.getSchema().find("time");
            times.clear();
            Feature f;
            Double td;
            while (fit.hasNext()) {
                f = fit.next();
                td = (Double) f.getAttribute(t);
                times.add(td);
            }
            Iterator<Double> id = times.iterator();
            System.out.println("Unique time values: ");
            int ctr = 0;
            while (id.hasNext()) {
                System.out.println("\t"+(ctr++)+": "+id.next().toString());
            }
            vTimes = new Double[times.size()];
            vTimes = times.toArray(vTimes);
            mtb.setProgressBarParams(0,times.size()-1);
            mtb.setString(null);
            mtb.setStringPainted(true);

            if (Point.class.isAssignableFrom(geometryClass)
                    || MultiPoint.class.isAssignableFrom(geometryClass)) {
                Style style = createPointsStyle(ft,vTimes[0],vTimes[vTimes.length-1]);
                style.setName("point results");
                style.setTitle("point results");
            
                if (ptsLayer!=null) tcMapViewer.removeGISLayer(ptsLayer);
                ptsLayer = new DefaultMapLayer(source,style,"Track positions");
                tcMapViewer.addGISLayer(ptsLayer);
                System.out.println("Done with points layer");
            } else {
            }
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        } catch (java.io.IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
//    private void loadSegsLayer(File f) {
//        try {
//            URL url = f.toURI().toURL();
//            ShapefileDataStore store = new ShapefileDataStore(url);
//            String name = store.getTypeNames()[0];
//            FeatureSource source = store.getFeatureSource(name);
//            FeatureType ft = source.getSchema();
//            Class geometryClass = ft.getDefaultGeometry().getType();
//
//            if (LineString.class.isAssignableFrom(geometryClass)
//                    || MultiLineString.class.isAssignableFrom(geometryClass)) {
//                Style style = createSegsStyle(ft,vTimes[0],vTimes[vTimes.length-1]);
//                style.setName("segment results");
//                style.setTitle("segment results");
//            
//                if (sgsLayer!=null) tcMapViewer.removeGISLayer(sgsLayer);
//                sgsLayer = new DefaultMapLayer(source,style,"Track segments");
//                tcMapViewer.addGISLayer(sgsLayer);
//            } else {
//           }
//        } catch (MalformedURLException ex) {
//            Exceptions.printStackTrace(ex);
//        } catch (java.io.IOException ex) {
//            Exceptions.printStackTrace(ex);
//        }
//    }

    private void createShapefiles(ArrayList<Feature> fv) 
            throws MalformedURLException, IllegalAttributeException, SchemaException, IOException {
        scp.closeShapefile();
//        scs.closeShapefile();
        String ptsShp = fnCSV;
        String sgsShp = fnCSV;
        ptsShp = ptsShp.replaceAll(".csv", "Points.shp");
        sgsShp = sgsShp.replaceAll(".csv", "Segments.shp");
        System.out.println("points shapefile = "+ptsShp);
        System.out.println("segs shapefile = "+sgsShp);
        ptsF = new File(ptsShp);
//        sgsF = new File(sgsShp);
        scp.createShapefile(ptsF,fv);
//        scs.createShapefile(sgsF,fv);
    }
    
    private void addToShapefiles(ArrayList<Feature> fv) 
            throws MalformedURLException, IllegalAttributeException, SchemaException, IOException {
        scp.addToShapefile(fv);
//        scs.addToShapefile(fv);
    }
    
    private Style createPointsStyle(FeatureType ft, double tmin, double tmax) {
        Style s = sb.createStyle();
        try {
            BetweenFilter fbs; //filter on "time" to get required start positions
            BetweenFilter fbe; //filter on "time" to get required end positions
            CompareFilter fc; //filter on "type" to get LHS type
            LogicFilter   f;  //combined filter: fb AND fc
            String[] keys = LHS_Factory.getTypeNames();
            for (int i=0;i<keys.length;i++) {
                Color clr = LHS_Factory.getLHScolor(keys[i]); //color signifying LHS type
                //create filter on LHS types
                fc = ff.createCompareFilter(CompareFilter.COMPARE_EQUALS);
                fc.addLeftValue(ff.createAttributeExpression(ft,"typeName"));
                fc.addRightValue(ff.createLiteralExpression(keys[i]));
                //create FeatureTypeStyle for end point 
                //create filter on time for start positons  
                fbs = ff.createBetweenFilter();
                fbs.addLeftValue(ff.createLiteralExpression(tmin-1));
                fbs.addMiddleValue(ff.createAttributeExpression(ft,"time"));
                fbs.addRightValue(ff.createLiteralExpression(tmin+1));
                //create combined filters
                f = ff.createLogicFilter(fc,fbs,LogicFilter.LOGIC_AND);
                Mark mrk1 = sb.createMark(StyleBuilder.MARK_CIRCLE,clr);
                Graphic gr1 = sb.createGraphic(null,mrk1,null,1,8,0);
                PointSymbolizer ps = sb.createPointSymbolizer(gr1);
                Rule r1 = sb.createRule(ps);
                r1.setFilter(f);
                r1.setName(keys[i]+" first position");
                FeatureTypeStyle fts1 = sb.createFeatureTypeStyle(ft.getTypeName(),r1);
                fts1.setName(keys[i]+" first position");
                s.addFeatureTypeStyle(fts1);
                //create FeatureTypeStyle for end point 
                //create filter on time for end (current) positions
                fbe = ff.createBetweenFilter();
                fbe.addLeftValue(ff.createLiteralExpression(tmax-1));
                fbe.addMiddleValue(ff.createAttributeExpression(ft,"time"));
                fbe.addRightValue(ff.createLiteralExpression(tmax+1));
                //create combined filters
                f = ff.createLogicFilter(fc,fbe,LogicFilter.LOGIC_AND);
    //            nf1 = fs.getFeatures(f).size();
    //            System.out.println("Found "+nf1+" "+kyes[i]+" features at "+tmax);
                mrk1 = sb.createMark(sb.MARK_TRIANGLE,clr);
                gr1 = sb.createGraphic(null,mrk1,null,1,8,0);
                ps = sb.createPointSymbolizer(gr1);
                r1 = sb.createRule(ps);
                r1.setFilter(f);
                r1.setName("current position");
                fts1 = sb.createFeatureTypeStyle(ft.getTypeName(),r1);
                fts1.setName("current position");
                s.addFeatureTypeStyle(fts1);
            }
        } catch (IllegalFilterException ex) {
            Exceptions.printStackTrace(ex);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
        return s;
    }

    private Style createSegsStyle(FeatureType ft, double tmin, double tmax) {
        Style s = sb.createStyle();
        try {
            BetweenFilter fb; //filter on "time" to get required segs
            CompareFilter fc; //filter on "typeName" to get LHS type
            LogicFilter   f;  //combined filter: fb AND fc
            String[] keys = LHS_Factory.getTypeNames();
            for (String key: keys) {
                Color clr = LHS_Factory.getLHScolor(key); //color signifying LHS type
                fb = ff.createBetweenFilter();
                fb.addLeftValue(ff.createLiteralExpression(tmin-1));
                fb.addMiddleValue(ff.createAttributeExpression(ft,"time"));
                fb.addRightValue(ff.createLiteralExpression(tmax+1));
                fc = ff.createCompareFilter(CompareFilter.COMPARE_EQUALS);
                fc.addLeftValue(ff.createAttributeExpression(ft,"typeName"));
                fc.addRightValue(ff.createLiteralExpression(key));
                f = ff.createLogicFilter(fc,fb,LogicFilter.LOGIC_AND);
                LineSymbolizer ps = sb.createLineSymbolizer(clr,2);
                Rule r = sb.createRule(ps);
                r.setFilter(f);
                r.setName(key+" rule");
                FeatureTypeStyle fts = sb.createFeatureTypeStyle(ft.getTypeName(),r);
                fts.setName(key+" style");
                s.addFeatureTypeStyle(fts);
            }
        } catch (IllegalFilterException ex) {
            Exceptions.printStackTrace(ex);
        }
        return s;
    }
    
    private void drawMap() {
        mtb.setValue(iTime);
        Double tmx = vTimes[iTime];
        Double tmn0 = vTimes[Math.max(0,iTime-4)];
        Double tmn1 = vTimes[Math.max(0,iTime-1)];//this was 5
        tcMapViewer.setOceanTime(tmx);
        if (ptsLayer!=null) {
            FeatureType ftp = ptsLayer.getFeatureSource().getSchema();
            Style ps = createPointsStyle(ftp,tmn1,tmx);
            ps.setName("Points style");
            ptsLayer.setStyle(ps);
        }
//        logger.info("drawing map from "+tmn0+" to "+tmx);
//        if (sgsLayer!=null) {
//            if (iTime<5){
//                logger.info("Hiding track segments");
//                sgsLayer.setVisible(false);
//            } else {
//                logger.info("Displaying track segsments");
//                FeatureType ftp = sgsLayer.getFeatureSource().getSchema();
//                Style ss = createSegsStyle(ftp,tmn0,tmx);
//                ss.setName("Segments style");
//                sgsLayer.setStyle(ss);
//            }
//        }
        if (saveAnimation) updateGraphics(null);
    }
    
    @Override
    public void play(MediaToolBarEvent ev) {
        logger.info("Playing!");
        timer = new javax.swing.Timer(500,new ActionListener() {
            public void actionPerformed(ActionEvent evt){
                mtb.setValue(iTime++);
                if (iTime>=vTimes.length) {
                    timer.stop();
                } else {
                    drawMap();
                }
            }
        });
        timer.start();
    }

    @Override
    public void stop(MediaToolBarEvent ev) {
        logger.info("Stop!");
        timer.stop();
    }

    @Override
    public void rewind(MediaToolBarEvent ev) {
        logger.info("Rewinding!");
        iTime = 0;
        drawMap();
    }

    @Override
    public void stepBackward(MediaToolBarEvent ev) {
        logger.info("Stepping backward from "+iTime);
        if (iTime>0) iTime--;
        drawMap();
    }

    @Override
    public void stepForward(MediaToolBarEvent ev) {
        logger.info("Stepping forward from "+iTime);
        if (iTime<(vTimes.length-1)) iTime++;
        drawMap();
    }

    @Override
    public void fastForward(MediaToolBarEvent ev) {
        logger.info("Fastfowarding!");
        iTime = vTimes.length-1;
        drawMap();
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
            logger.info("Saving animation image to "+animFN);
            animCtr++;
            tcMapViewer.saveMapAsImage(animFN);
        } catch (IOException ex) {
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
            logger.info("----Load enabled!!");
        } else {
            content.remove(csvLoader);//remove an Openable implementation (CSVLoader) from the instance content 
            logger.info("----Load disabled!!");
        }
    }

    /**
     * Method reacts to PropertyChanges fired by: 
     *      the GlobalInfo instance for changes to the working directory
     * 
     * TODO: do we need this?
     * 
     * @param evt - the PropertyChange event 
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GlobalInfo.PROP_WorkingDirFN)){
            logger.info("Detected PropertyChange!");
            String wdFN = globalInfo.getWorkingDir();
            File wdF = new File(wdFN);
            jfcCSV.setCurrentDirectory(wdF);
        }
    }
    
    //------------------------------------------------------------------------//
    /**
     * Private class to implement open functionality for the results file.
     */
    private class CSVLoader implements Openable {

        @Override
        public void open() {
            logger.info("CSVLoader.open()");
            jfcCSV.setDialogTitle("Select csv file to load DisMELS results from:");
            int res = jfcCSV.showOpenDialog(instance);
            if (res==JFileChooser.APPROVE_OPTION){
                File openFile = jfcCSV.getSelectedFile();
                fnCSV = openFile.getPath();
                loadAttributesFile();
            }
        }
    }
}
