/*
 * GeomteyTypeChecker.java
 *
 * Created on September 29, 2003, 9:01 PM
 */

package wts.GIS.utils;

/**
 *
 * @author  wstockha
 */

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;

public class GeometryTypeChecker {
    
    private static Class lineStringClass = LineString.class;
    private static Class pointClass = Point.class;
    private static Class polygonClass = Polygon.class;
    
    private static Class multiLineStringClass = MultiLineString.class;
    private static Class multiPointClass = MultiPoint.class;
    private static Class multiPolygonClass = MultiPolygon.class;
    
    /** Creates a new instance of GeomteyTypeChecker */
    public GeometryTypeChecker() {
    }
    
    public static boolean isLineString(Object obj) {
        return lineStringClass.isInstance(obj);
    }
    
    public static boolean isLineString(Class clz) {
        return lineStringClass.equals(clz);
    }
    
    public static boolean isPoint(Object obj) {
        return pointClass.isInstance(obj);
    }
    
    public static boolean isPoint(Class clz) {
        return pointClass.equals(clz);
    }
    
    public static boolean isPolygon(Object obj) {
        return polygonClass.isInstance(obj);
    }
    
    public static boolean isPolygon(Class clz) {
        return polygonClass.equals(clz);
    }
    
    public static boolean isMultiLineString(Object obj) {
        return multiLineStringClass.isInstance(obj);
    }
    
    public static boolean isMultiLineString(Class clz) {
        return multiLineStringClass.equals(clz);
    }
    
    public static boolean isMultiPoint(Object obj) {
        return multiPointClass.isInstance(obj);
    }
    
    public static boolean isMultiPoint(Class clz) {
        return multiPointClass.equals(clz);
    }
    
    public static boolean isMultiPolygon(Object obj) {
        return multiPolygonClass.isInstance(obj);
    }
    
    public static boolean isMultiPolygon(Class clz) {
        return multiPolygonClass.equals(clz);
    }
    
}
