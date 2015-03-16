/*
 * ShapefileCreator.java
 *
 * Created on March 20, 2004, 1:53 PM
 *
 *  Uses DataStore API.
 *
 *  1. set the url for the shapefile by setShapefileURL(URL)
 *  2
 *      a. call createShapefile(FeatureCollection) OR
 *
 *      b. define the schema & add the data first
 *          1. set the shape type using setShapeType(ShapeType)
 *          2. set the Attribute names using setAttributeNames(String[])
 *          3. set the attribute class definitions using
 *                   setAttribueClasses(Class[])
 *          4. set the attributes data using setAttributesArray(ArrayList)
 *          5. set the coordinates data using setCoordinatesArray(ArrayList)
 *          6. Finally, call createShapefile()
 *          NOTE: steps 1-5 can occur in any order.
 */

package wts.GIS.shapefile;

/**
 *
 * @author  William Stockhausen
 */

import com.vividsolutions.jts.geom.Geometry;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.shp.JTSUtilities;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import wts.GIS.feature.FeatureCollectionCreator;

public class ShapefileCreator {
    
    /* shape type in integer form */
    protected ShapeType shapeType;
    
    /* URL pointing to shapefile location */
    protected URL shapefileURL;
    
    /* ArrayList of ArrayLists of non-geometric feature attributes */
    protected ArrayList shapeAttribs;  
    
    /* Array of the names of the shape attributes */
    protected String[] attNames;
    
    /* Array of the classes of the shape attributes */
    protected Class[] attClasses;
    
    /* ArrayList of vectors of coordinates defining shapes */
    protected ArrayList shapeCoords;
    
    /* ArrayList of geometries defining shapes */
    protected ArrayList<Geometry> shapeGeoms;
    
    /* flag to use shapeCoords instead of shapeGeoms */
    protected boolean useShapeCoords = true;
    
    /** Creates a new instance of ShapefileCreator */
    public ShapefileCreator() {
    }

    private void cleanup() {
        try {
            File shp = new File(shapefileURL.toURI());
            String fn = shp.getName();
            final String base = fn.substring(0,fn.lastIndexOf('.'));
            File td = new File(System.getProperty("java.io.tmpdir"));
            FileFilter ff = new FileFilter(){
                public boolean accept(File f) {
                    boolean accept = false;
                    String fn = f.getName();
                    if ((fn.endsWith("shp"))||(fn.endsWith("shx"))||(fn.endsWith("dbf"))){
                        if (fn.contains(base)) accept=true;
                    }
                    return accept;
                }
            };
            File[] fs = td.listFiles(ff);
            System.out.println("Deleting temporary files:");
            for (int f=0;f<fs.length;f++) {
                System.out.println(fs[f].getName());
                fs[f].delete();
            }
        } catch (URISyntaxException ex) {
            Logger.getLogger(ShapefileCreator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createShapefile() 
            throws IllegalAttributeException, SchemaException, MalformedURLException, IOException {
        FeatureCollectionCreator fcc = new FeatureCollectionCreator();
        fcc.setAttributeClasses(this.attClasses);
        fcc.setAttributeNames(this.attNames);
        fcc.setAttributesArray(this.shapeAttribs);
        if (useShapeCoords) {
            fcc.setCoordinatesArray(this.shapeCoords);
        } else {
            fcc.setGeometriesArray(this.shapeGeoms);
        }
        fcc.setGeometryClass(JTSUtilities.findBestGeometryClass(this.shapeType));
        fcc.setSchemaBaseName(this.getFeatureTypeName());
        FeatureCollection fc = fcc.createFeatureCollection();
        createShapefile(fc);
    }
    
    public void createShapefile(FeatureCollection fc) 
                                    throws MalformedURLException, IOException  {
        //use a ShapefileDataStore instance to write the featureCollection to
        //disk as a shapefile
        ShapefileDataStore sds = new ShapefileDataStore(shapefileURL);
        writeFeatures(sds, fc);
        cleanup();
    }
    
    private void writeFeatures(ShapefileDataStore s,FeatureCollection fc) 
                                                        throws IOException {
        s.createSchema(fc.features().next().getFeatureType());
        FeatureStore fs = (FeatureStore) s.getFeatureSource();
        FeatureReader fr = DataUtilities.reader(fc);
        fs.addFeatures(fr);
    }
    
    public void addToShapefile() 
            throws IllegalAttributeException, SchemaException, MalformedURLException, IOException {
        FeatureCollectionCreator fcc = new FeatureCollectionCreator();
        fcc.setAttributeClasses(this.attClasses);
        fcc.setAttributeNames(this.attNames);
        fcc.setAttributesArray(this.shapeAttribs);
        if (useShapeCoords) {
            fcc.setCoordinatesArray(this.shapeCoords);
        } else {
            fcc.setGeometriesArray(this.shapeGeoms);
        }
        fcc.setGeometryClass(JTSUtilities.findBestGeometryClass(this.shapeType));
        fcc.setSchemaBaseName(this.getFeatureTypeName());
        FeatureCollection fc = fcc.createFeatureCollection();
        addToShapefile(fc);
    }
    
    public void addToShapefile(FeatureCollection fc) throws IOException {
        //use a ShapefileDataStore instance to write the featureCollection to
        //disk as a shapefile
        ShapefileDataStore sds = new ShapefileDataStore(shapefileURL);
        appendFeatures(sds, fc);
        cleanup();
    }
    
    private void appendFeatures(ShapefileDataStore s,FeatureCollection fc) 
                                                        throws IOException {
        FeatureStore fs = (FeatureStore) s.getFeatureSource();
        FeatureReader fr = DataUtilities.reader(fc);
        fs.addFeatures(fr);
        fs = null;
    }
    
    private String getFeatureTypeName() {
        String path = shapefileURL.getPath();
        int dot = path.lastIndexOf('.');
        if (dot < 0) {
          dot = path.length();
        }
        int slash = path.lastIndexOf('/') + 1;
        return path.substring(slash,dot);
    } 
  
    public URL getShapefileURL(){
        return shapefileURL;
    }
    public void setShapefileURL(URL u){
        shapefileURL = u;
    }
    
    /*
     * Getter for a description of shape type (e.g., point, arc, polygon).
     * @returns type: string describing the shapepefile type
     */
    public String getShapeTypeDescription(){
        return shapeType.toString();
    }
    
    /*
     * Getter for the shape type (e.g., point, arc, polygon).
     * @returns type: int denoting one of the shapepefile types 
     *      (i.e. ShapeType.Point, ShapeType.Arc, ShapeType.Polygon, etc.)
     */
    public ShapeType getShapeType() {
        return shapeType;
    }
    
    /*
     * Setter for the shape type (e.g., point, arc, polygon).
     * @param type: int denoting one of the shapepefile types 
     *      (i.e. ShapeType.Point, ShapeType.Arc, ShapeType.Polygon, etc.)
     */
    public void setShapeType(ShapeType type){
        shapeType = type;
    }
    
    /*
     * Setter for the names of the attributes associated with each shape.
     * @param types: Class[] denoting the attribute names in
     *               the order they are found in the attributes data
     */
    public void setAttributeNames(String[] names) {
        attNames = new String[names.length];
        for (int i=0;i<names.length;i++){
            attNames[i] = names[i];
        }
        //System.out.println("setAttributeNames");
    }
    
    /*
     * Setter for the attribute types (e.g., Boolean, Int, Double, etc.).
     * @param types: Class[] denoting the attribute class types in
     *               the order they are found in the attributes data
     */
    public void setAttributeClasses(Class[] types) {
        attClasses = new Class[types.length];
        for (int i=0;i<types.length;i++){
            attClasses[i] = types[i];
        }
        //System.out.println("setAttributeTypes");
    }
    
    /*
     * Setter for the shape attributes data in an ArrayList format.
     * @param coords: ArrayList in the following format:
     *      - Object j in attribs should be an ArrayList which contains the
     *          attribute data associated with shape j
     */
    public void setAttributesArray(ArrayList attribs) {
        shapeAttribs = attribs;
    }
    
    /*
     * Setter for the shape coordinates in an ArrayList format.
     * @param coords: an ArrayList of ArrayLists. Each "interior" ArrayList
     *                contains the coordinates of the geometric shape in one
     *                of the following formats:
     *                  - an ArrayList<java.awt.Point2D>
     *                  - an ArrayList<com.vividsolutions.jts.geom.Coordinate> 
     */
    public void setCoordinatesArray(ArrayList coords) {
        useShapeCoords = true;
        shapeCoords = coords;
    }
    
    /*
     * Setter for the shape geomtries in an ArrayList format.
     * @param coords: ArrayList of geometries
     */
    public void setGeometriesArray(ArrayList<Geometry> geoms) {
        useShapeCoords = false;
        shapeGeoms = geoms;
    }
}
