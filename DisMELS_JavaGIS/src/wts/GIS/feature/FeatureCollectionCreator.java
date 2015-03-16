/*
 * FeatureCollectionCreator.java
 *
 * Created on September 14, 2003, 12:25 PM
 *
 *Creates a feature collection from:
 *      1. a Geometry type 
 *              [setGeometryType()]
 *      2. a base name for the schema 
 *              [setSchemaBaseName()]
 *      2. a set of Attribute names and classes 
 *              [setAttributeNames(),setAttributeClasses()]
 *      3. an ArrayList of ArrayLists of coordinates defining the geometric features
 *              [setCoordinatesArray() or setGeometriesArray()]
 *      4. an ArrayList of ArrayLists of non-spatial attributes
 *              [setAttributesArray()]
 *Once these are set, the feature collection is created using
 *      createFeatureCollection()
 *
 */

package wts.GIS.feature;

/**
 *
 * @author  William Stockhausen
 */

import com.wtstockhausen.utils.ObjectTransformer;

import com.vividsolutions.jts.algorithm.CGAlgorithms;
import com.vividsolutions.jts.algorithm.RobustCGAlgorithms;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.Point;
import org.geotools.data.DataSourceException;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureFactory;
import org.geotools.feature.DefaultFeature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.DefaultFeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;


import org.geotools.filter.Filter;

import java.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.List;

public class FeatureCollectionCreator {
    
    /* */
    protected static CGAlgorithms cga = new RobustCGAlgorithms();
    
    /* geometry type to create as Class */
    protected Class geometryClass;
    
    /* the schema */
    protected FeatureType schema;
    
    /* the base name for the schema. the geometry class will be appended
     * to create the full name */
    protected String schemaBaseName;
     
    /* ArrayList of ArrayLists of non-geometric feature attributes */
    protected ArrayList attData;  
    
    /* Array of the names of the geometry attributes */
    protected String[] attNames;
    
    /* Array of the classes of the geometry attributes */
    protected Class[] attClasses;
    
    /* ArrayList of vector of coordinates defining geometrys */
    protected ArrayList geometryCoords;
    
    /* ArrayList<Geometry> of geometries */
    protected ArrayList<Geometry> geomsList;

    /* 
     *  Flag to indicate whether to use coordinates array to create geometries
     *  or to use an input set of geometries.
     */
    private boolean useCoordinatesArray = true;
    
    /** Creates a new instance of FeatureCollectionCreator */
    public FeatureCollectionCreator() {
    }
    
    /** creates the feature collection from 
     *  the coordinate and attribute data 
     */
    public FeatureCollection createFeatureCollection() 
                            throws IllegalAttributeException, SchemaException {
        FeatureCollection fc = FeatureCollections.newCollection();
        int i = 0;
        if (schema==null) createSchema();
            //Build the FeatureCollecion
        if (useCoordinatesArray) {
            createGeometries();  //create geomsList from geometryCoords
        }
        List features = new ArrayList();
        Feature feature;
        int ns = geomsList.size();
        int j,nCol;
        Object obj, obj1;
        Object[] row;
        ArrayList attribs;
        //loop over geometric geometrys and associated attributes
        for (i=0;i<ns;i++) {
            if (attData==null) {
                row = new Object[2];
                row[1] = new Integer(i);
            } else {
                attribs = (ArrayList)attData.get(i);
                nCol = attribs.size();
                row = new Object[nCol+1];
                for (j=0;j<nCol;j++){
                    obj  = attribs.get(j);
                    obj1 = null;
                    if (obj.getClass().isAssignableFrom(attClasses[j])){
                        obj1 = attribs.get(j);
                    } else {
                        obj1 = ObjectTransformer.transform(obj,attClasses[j]);
                    }
                    row[j+1] = obj1;
                }
            }
            row[0] = (Geometry) geomsList.get(i);
            feature = schema.create(row);
            fc.add(feature);
        }

        System.out.println("FeatureCollection size = "+fc.size());
        return fc;
    }
    
    public FeatureType createSchema() throws SchemaException {
            //Build the FeatureType (i.e., the schema)        
        // 1. define the geometry attribute type
        AttributeType geomAttType = AttributeTypeFactory.newAttributeType(
                                    "the_geom",
                                    geometryClass);
        System.out.println("createFeatureCollection: geometryClass = "+
                                            geometryClass.getName());
        ArrayList attTypeList = new ArrayList();
        attTypeList.add(geomAttType);
        // 2. add associated non-geometric attirbutes to attribs
        //      loop over column names/column types,
        //      add new attributes to attrib using
        //          attTypeList.add(new AttributeTypeDefault(colName, colType));
        //
        if (attNames!=null) {
            int nc = attNames.length;
            for (int i=0;i<nc;i++){
                attTypeList.add(AttributeTypeFactory.newAttributeType(attNames[i],
                                                                      attClasses[i]));
            }
        } else {
            attTypeList.add(AttributeTypeFactory.newAttributeType("ID",
                                                                  Integer.class));
        }
        AttributeType[] attTypes = (AttributeType[]) attTypeList.toArray(
                                            new AttributeType[attTypeList.size()]);
        schema = FeatureTypeFactory.newFeatureType(
                                                attTypes,getSchemaName());
        return schema;
    }
    
    protected LineString createLineString(ArrayList coords, 
                                          GeometryFactory gf){
        // code based on MultiLineHandler
        if (coords==null) return null;
        
        if (!(coords.get(0) instanceof ArrayList)) {
            // create simple line
            int numPoints = coords.size();
            Coordinate[] points = new Coordinate[numPoints];
            if (coords.get(0) instanceof Point2D) {
                for (int t = 0; t < numPoints; t++) {
                    Point2D pt = (Point2D) coords.get(t);
                    points[t] = new Coordinate(pt.getX(),pt.getY());
                }
            } else if (coords.get(0) instanceof Coordinate) {
                points = (Coordinate[]) coords.toArray(points);
            }
            
            LineString line = gf.createLineString(points);
            return line;
        }    
        return null;
    }
    
    protected MultiLineString createMultiLineString(ArrayList coords, 
                                                    GeometryFactory gf){
        // code based on MultiLineHandler
        if (coords==null) return null;
        
        if (!(coords.get(0) instanceof ArrayList)) {
            // create simple line
            LineString line = createLineString(coords,gf);
            LineString[] lv = {line};
            MultiLineString ml = gf.createMultiLineString(lv);
            return ml;
        }    
        //TODO: add capabilities for multiple lines
        return null;
    }
    
    protected MultiPoint createMultiPoint(ArrayList coords, 
                                          GeometryFactory gf){
        // code based on PolygonHandler
        if (coords==null) return null;
        
        if (!(coords.get(0) instanceof ArrayList)) {
            // create single Point
            Coordinate ptC = null;
            if ((coords.get(0) instanceof Point2D)) {
                Point2D pt2d   = (Point2D) coords.get(0);
                ptC = new Coordinate(pt2d.getX(),pt2d.getY());
            } else if ((coords.get(0) instanceof Coordinate)) {
                ptC = (Coordinate) coords.get(0);
            }
            Point pt = createPoint(coords,gf);
            Point[] ptV = {pt};
            MultiPoint mpt = gf.createMultiPoint(ptV);
            return mpt;
        }    
        //TODO: add capabilities for MultiPoints
        return null;
    }
    
    protected MultiPolygon createMultiPolygon(ArrayList coords, 
                                              GeometryFactory gf){
        // code based on PolygonHandler
        if (coords==null) return null;
        
        if (!(coords.get(0) instanceof ArrayList)) {
            // create simple polygon with no holes
            Polygon pol = createPolygon(coords,gf);
            Polygon[] polV = {pol};
            MultiPolygon mp = gf.createMultiPolygon(polV);
            return mp;
        }    
        //TODO: add capabilities for MultiPolygon
        return null;
    }
    
    protected Point createPoint(ArrayList coords, 
                                GeometryFactory gf){
        if (coords==null) return null;
        
        if (!(coords.get(0) instanceof ArrayList)) {
            // create single Point
            Coordinate ptC = null;
            if ((coords.get(0) instanceof Point2D)) {
                Point2D pt2d   = (Point2D) coords.get(0);
                ptC = new Coordinate(pt2d.getX(),pt2d.getY());
            } else if ((coords.get(0) instanceof Coordinate)) {
                ptC = (Coordinate) coords.get(0);
            }
            Point pt = gf.createPoint(ptC);
            return pt;
        }    
        return null;
    }
    
    protected Polygon createPolygon(ArrayList coords, 
                                    GeometryFactory gf){
        if (coords==null) return null;
        
        if (!(coords.get(0) instanceof ArrayList)) {
            // create simple polygon with no holes
            int numPoints = coords.size();
            Coordinate[] points = new Coordinate[numPoints];
            if (coords.get(0) instanceof Point2D) {
                for (int t = 0; t < numPoints; t++) {
                    Point2D pt = (Point2D) coords.get(t);
                    points[t] = new Coordinate(pt.getX(),pt.getY());
                }
            } else if (coords.get(0) instanceof Coordinate) {
                points = (Coordinate[]) coords.toArray(points);
            }
            
            if (cga.isCCW(points)) {
                //reverse order of points
                Coordinate[] rPoints = new Coordinate[numPoints];
                for (int t = 0; t < numPoints; t++) {
                    rPoints[t] = points[numPoints-1-t];
                }
                points = rPoints;
            }
            
            LinearRing ring = gf.createLinearRing(points);
            Polygon pol = gf.createPolygon(ring,new LinearRing[0]);
            return pol;
        }    
        //TODO: add capabilities for polygon with holes
        return null;
    }
    
    /* create the com.vividsolutions.jts.geom objects from
     * the ArrayList geometryCoords and the geometryClass
     */
    protected void createGeometries() {
        geomsList = new ArrayList<Geometry>();
        GeometryFactory gf = new GeometryFactory();
        int ns = geometryCoords.size();
        ArrayList coords;
        Geometry geom;
        if (geometryClass.equals(Point.class)) {
            for (int i=0;i<ns;i++) {
                coords = (ArrayList) geometryCoords.get(i);
                geom = null;
                geom = createPoint(coords,gf);
                geomsList.add(geom);
            }
        } else if (geometryClass.equals(MultiPoint.class)) {
            for (int i=0;i<ns;i++) {
                coords = (ArrayList) geometryCoords.get(i);
                geom = null;
                geom = createMultiPoint(coords,gf);
                geomsList.add(geom);
            }
        } else if (geometryClass.equals(LineString.class)) {
            for (int i=0;i<ns;i++) {
                coords = (ArrayList) geometryCoords.get(i);
                geom = null;
                geom = createLineString(coords,gf);
                geomsList.add(geom);
            }
        } else if (geometryClass.equals(MultiLineString.class)) {
            for (int i=0;i<ns;i++) {
                coords = (ArrayList) geometryCoords.get(i);
                geom = null;
                geom = createMultiLineString(coords,gf);
                geomsList.add(geom);
            }
        } else if (geometryClass.equals(Polygon.class)) {
            for (int i=0;i<ns;i++) {
                coords = (ArrayList) geometryCoords.get(i);
                geom = null;
                geom = createPolygon(coords,gf);
                geomsList.add(geom);
            }
        } else if (geometryClass.equals(MultiPolygon.class)) {
            for (int i=0;i<ns;i++) {
                coords = (ArrayList) geometryCoords.get(i);
                geom = null;
                geom = createMultiPolygon(coords,gf);
                geomsList.add(geom);
            }
        }
    }
    
    public String getSchemaName() {
        if ((geometryClass!=null)&&(schemaBaseName!=null)) {
            return schemaBaseName+geometryClass.getName(); 
        } else {
            return null;
        }
    } 

    public String getSchemaBaseName() {
        return schemaBaseName;
    }
    
    public void setSchemaBaseName(String s) {
        schemaBaseName = s;
    }
    
    /*
     * Getter for a description of geometry type (e.g., point, arc, polygon).
     * @returns type: string describing the geometrypefile type
     */
    public FeatureType getSchema(){
        return schema;
    }
    
    /*
     * Setter for the schema (FeatureType)
     */
    public void setSchema(FeatureType newSchema){
        schema = newSchema;
    }
    
    /*
     * Getter for the geometry type (e.g., point, arc, polygon).
     * @returns type: class denoting one of the geometry types 
     *      (i.e. Point, LineString, Polygon, etc.)
     */
    public Class getGeometryClass() {
        return geometryClass;
    }
    
    /*
     * Setter for the geometry type (e.g., point, arc, polygon).
     * @param type: class denoting one of the geometrypefile types 
     *      (i.e. Point, LineString, Polygon, etc.)
     */
    public void setGeometryClass(Class clazz){
        geometryClass = clazz;
    }
    
    /*
     * Setter for the names of the attributes associated with each geometry.
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
     * Setter for the geometry attributes data in an ArrayList format.
     * @param coords: ArrayList in the following format:
     *      - Object j in attribs should be an ArrayList which contains the
     *          attribute data associated with geometry j
     */
    public void setAttributesArray(ArrayList attribs) {
        attData = new ArrayList();
        int nc = attribs.size();
        for (int i=0;i<nc;i++) {
            attData.add(attribs.get(i));
        }
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
        useCoordinatesArray = true;
        geometryCoords = coords;
    }
    
    /*
     * Setter for the geometries.
     * @param geoms: ArrayList of geometries
     */
    public void setGeometriesArray(ArrayList<Geometry> geoms) {
        useCoordinatesArray = false;
        geomsList = geoms;
    }
}
