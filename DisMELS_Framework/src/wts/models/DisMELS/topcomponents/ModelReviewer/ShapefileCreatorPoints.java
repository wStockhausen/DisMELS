/*
 * ShapefileCreatorPoints.java
 *
 * Created on July 11, 2006, 10:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.models.DisMELS.topcomponents.ModelReviewer;

import com.vividsolutions.jts.geom.Geometry;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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
public class ShapefileCreatorPoints {
    
    private ShapefileCreator sc = null;
    private ArrayList av  = new ArrayList(); //attributes array list
    private ArrayList<Geometry> gv  = new ArrayList<Geometry>(); //geometries array list
    
    /** Creates a new instance of ShapefileCreatorPoints */
    public ShapefileCreatorPoints() {
    }
    
    public void createShapefile(String filename,ArrayList<Feature> fv) 
            throws MalformedURLException, IllegalAttributeException, SchemaException, IOException {
        File file = new File(filename);
        createShapefile(file,fv);
    }
    
    public void createShapefile(File file,ArrayList<Feature> fv) 
            throws MalformedURLException, IllegalAttributeException, SchemaException, IOException {
        URL url = file.toURI().toURL();
        sc = new ShapefileCreator();
        av.clear(); //attributes array list
        gv.clear(); //geometries array list
        Iterator<Feature> fi = fv.iterator();
        Feature f = fi.next();
        Geometry g = f.getDefaultGeometry();
        LifeStageAttributesInterface attr = (LifeStageAttributesInterface) f.getAttribute(1);
        Class[] classes = attr.getClasses();
        String[] names = attr.getShortNames();
        gv.add(g);
        av.add(attr.getArrayList());
        while (fi.hasNext()) {
            f = fi.next();
            g = f.getDefaultGeometry();
            attr = (LifeStageAttributesInterface) f.getAttribute(1);
            gv.add(g);
            av.add(attr.getArrayList());
        }
        sc.setShapefileURL(url);
        sc.setShapeType(ShapeType.POINTZ);
        sc.setAttributeNames(names);
        sc.setAttributeClasses(classes);
        sc.setAttributesArray(av);
        sc.setGeometriesArray(gv);
        sc.createShapefile();
    }
    
    public void addToShapefile(ArrayList<Feature> fv) 
            throws MalformedURLException, IllegalAttributeException, SchemaException, IOException {
        av.clear(); //attributes array list
        gv.clear(); //geometries array list
        Iterator<Feature> fi = fv.iterator();
        Feature f = fi.next();
        Geometry g = f.getDefaultGeometry();
        LifeStageAttributesInterface attr = (LifeStageAttributesInterface) f.getAttribute(1);
        gv.add(g);
        av.add(attr.getArrayList());
        while (fi.hasNext()) {
            f = fi.next();
            g = f.getDefaultGeometry();
            attr = (LifeStageAttributesInterface) f.getAttribute(1);
            gv.add(g);
            av.add(attr.getArrayList());
        }
        sc.setAttributesArray(av);
        sc.setGeometriesArray(gv);
        sc.addToShapefile();
    }
    
    public void closeShapefile() {
        sc = null;
    }
}
