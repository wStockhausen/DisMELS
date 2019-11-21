/*
 * ShapefileCreatorSegments.java
 *
 * Created on July 11, 2006, 4:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.topcomponents.ModelReviewer;

import com.vividsolutions.jts.geom.Coordinate;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.feature.Feature;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import wts.GIS.shapefile.ShapefileCreator;
import wts.models.DisMELS.framework.LifeStageAttributesInterface;

/**
 *
 * @author William Stockhausen
 */
public class ShapefileCreatorSegments {
    
    private class Segment {
        Feature f1;
        Feature f2;
        public Segment(Feature f) {
            f2 = f;
        }
        public void add(Feature f) {
            if (f2!=null) f1=f2;
            f2 = f;
        }
        public Feature getFirst() {
            return f1;
        }
        public Feature getLast() {
            return f2;
        }
    }
    
    private ShapefileCreator sc = null;
    private HashMap<Long,Segment> 
            map = new HashMap<Long,Segment>();
    private ArrayList cv = new ArrayList();
    private ArrayList av = new ArrayList();
    private ArrayList<Coordinate> coords;
    
    /**
     * Creates a new instance of ShapefileCreatorSegments
     */
    public ShapefileCreatorSegments() {
    }
    
    private void addFeature(Feature f) {
        LifeStageAttributesInterface att = (LifeStageAttributesInterface) f.getAttribute(1);
        Long id = new Long(att.getID());
        Segment s = map.get(id);
        if (s==null) {
            s = new Segment(f);
            map.put(id,s);
        } else {
            s.add(f);
            Feature f1 = s.getFirst();
            Coordinate c1 = f1.getDefaultGeometry().getCoordinate();
            Coordinate c2 = f.getDefaultGeometry().getCoordinate();
            coords = new ArrayList<Coordinate>(2);
            coords.add(c1);
            coords.add(c2);
            cv.add(coords);
            av.add(att.getArrayList());
        }
    }
    
    public void createShapefile(String filename, ArrayList<Feature> fv) 
            throws MalformedURLException, IllegalAttributeException, SchemaException, IOException {
        File file = new File(filename);
        createShapefile(file, fv);
    }
    
    public void createShapefile(File file, ArrayList<Feature> fv)  
            throws MalformedURLException, IllegalAttributeException, SchemaException, IOException {
        URL url = file.toURI().toURL();
        sc = new ShapefileCreator();
        Iterator<Feature> fi = fv.iterator();
        map.clear();//clear map to remove previous ids/segments
        av.clear();
        cv.clear();
        Feature f = fi.next();
        LifeStageAttributesInterface attr = (LifeStageAttributesInterface) f.getAttribute(1);
        Class[] classes = attr.getClasses();
        String[] names = attr.getShortNames();
        addFeature(f);
        while (fi.hasNext()) {
            f = fi.next();
            addFeature(f);
        }
        sc.setShapefileURL(url);
        sc.setShapeType(ShapeType.ARCZ);
        sc.setAttributeNames(names);
        sc.setAttributeClasses(classes);
        sc.setAttributesArray(av);
        sc.setCoordinatesArray(cv);
        sc.createShapefile();
    }
    
    public void addToShapefile(ArrayList<Feature> fv)  
            throws MalformedURLException, IllegalAttributeException, SchemaException, IOException {
        //should not clear map here, as we needed to in createShapefile
        av.clear();
        cv.clear();
        Iterator<Feature> fi = fv.iterator();
        Feature f = fi.next();
        addFeature(f);
        while (fi.hasNext()) {
            f = fi.next();
            addFeature(f);
        }
        sc.setAttributesArray(av);
        sc.setCoordinatesArray(cv);
        sc.addToShapefile();
    }
    
    public void closeShapefile() {
        sc = null;
    }
}
