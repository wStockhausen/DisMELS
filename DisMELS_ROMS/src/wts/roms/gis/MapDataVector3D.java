/*
 * ModelGrid2DMapData.java
 *
 * Created on January 9, 2006, 3:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gis;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.ProjectedCoordinateSystem;
import org.geotools.ct.MathTransform;
import org.geotools.factory.FactoryConfigurationError;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.styling.Style;
import org.opengis.referencing.operation.TransformException;
import wts.GIS.shapefile.ShapefileCreator;
import wts.GIS.styling.VectorStyle;
import wts.GIS.utils.FeatureCollectionUtilities;
import wts.models.utilities.DateTime;
import wts.roms.model.GlobalInfo;
import wts.roms.model.Interpolator3D;
import wts.roms.model.ModelGrid3D;

/**
 *
 * @author William Stockhausen
 */
public class MapDataVector3D implements MapDataInterfaceVector3D {

    GlobalInfo romsGI;
    
    private DateTime date = null;
        
    private GeometryFactory gf   = null;
    private MathTransform mt     = null; //gt2.1-
    private FeatureType ft       = null;
    private FeatureCollection fc = null;

    /** field name of vector x-component */
    private String xComp = null;
    /** field name of vector y-component */
    private String yComp = null;
    /** ROMS spatial interpolator object */
    private Interpolator3D i3d = null;

    private double min = Double.POSITIVE_INFINITY;
    private double max = Double.NEGATIVE_INFINITY;

    /** style used to create and visualize vector field */
    private VectorStyle style = null;
    
    private static final Logger logger = Logger.getLogger(MapDataVector3D.class.getName());

    public MapDataVector3D(String u, String v, Interpolator3D i3d, DateTime date) throws Exception {
        romsGI = GlobalInfo.getInstance();
        this.date = date;
        this.xComp   = u;
        this.yComp   = v;
        this.i3d = i3d;
        fc = FeatureCollections.newCollection();
        initialize();
    }

    private void initialize() throws Exception {
        //Create the coordinate transformer--Geotools 2.0/2.1
        GeographicCoordinateSystem nad83 =
                CSCreator.createNAD_83();
        ProjectedCoordinateSystem nad83albers =
                CSCreator.createAlbersPCS(CSCreator.NAD_83);
        mt = CSCreator.getTransformation(nad83,nad83albers).getMathTransform();

        //Create the GeometryFactory
        gf = new GeometryFactory();

        //Create feature type for vector map data
        AttributeType[] aTypes = new AttributeType[6];
        GeometryAttributeType gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.LineString.class);
//                    ,
//                    true,0,null,nad83albers);
        aTypes[0] = gat;
        aTypes[1] = AttributeTypeFactory.newAttributeType("ID",
                             java.lang.String.class);
        aTypes[2] = AttributeTypeFactory.newAttributeType("lat",
                             java.lang.Double.class);
        aTypes[3] = AttributeTypeFactory.newAttributeType("lon",
                             java.lang.Double.class);
        aTypes[4] = AttributeTypeFactory.newAttributeType("speed",
                             java.lang.Double.class);
        aTypes[5] = AttributeTypeFactory.newAttributeType("angle",
                             java.lang.Double.class);
        ft = FeatureTypeFactory.newFeatureType(aTypes,"uv");
    }

    @Override
    public FeatureType getFeatureType() {
        return ft;
    }
    
    @Override
    public double getMin() {
        return min;
    }

    @Override
    public double getMax() {
        return max;
    }

    /**
     * Gets name of vector field.  The name is a concatenation of the component fields' names.
     * @return 
     */
    @Override
    public String getName() {
        return xComp+"_"+yComp;//concatenation of component field names
    }

    @Override
    public double getStride() {
        return style.getStride();
    }

    @Override
    public void setStride(double s) {
        style.setStride(s);
    }
    
    @Override
    public VectorStyle getStyle() {
        return style;
    }
    
    /**
     * Tests whether a double value is finite (as opposed to being infinite or NaN).
     * @param val
     * @return 
     */
    private boolean isFinite(double val){
        return !(Double.isInfinite(val)||Double.isInfinite(-val)||Double.isNaN(val));
    }

    /**
     * Returns the vector arrow shape for plotting as a Coordinate vector. 
     * The resulting arrow shape is scaled consistent with useFixedLength,
     * useFixedArrowheadSize, style.getMapLength(), style.getStandardScale(), style.getExponentScale(), style.getArrowScale() and style.getArrowheadAngle().
     * 
     * @param sVal   - unscaled vector magnitude
     * @param aVal   - vector angle
     * @param dstPts - location of vector base
     * @param aa     - pi minus the arrowhead angle
     * 
     * @return - Coordinate[5] object holding map coordinates of arrow vertices
     */
    @Override
    public Coordinate[] createArrow(double sVal, double aVal, double[] dstPts, double aa){
        Coordinate[] c = new Coordinate[5];
        double mapLength = 1000*style.getMapLength();//scale from km to m
        double vmag;//vector magnitude
        if (style.getUseFixedLength()) {
            vmag = mapLength;
        } else {
            vmag = mapLength*Math.pow(sVal/style.getStandardScale(),style.getExponentScale());
        }
        double amag;//arrow head magnitude
        if (style.getUseFixedHeadSize()){
            amag = style.getArrowScale()*mapLength;
        } else {
            amag = style.getArrowScale()*mapLength*Math.pow(sVal/style.getStandardScale(),style.getExponentScale());
        }
        c[0] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
        c[1] = new Coordinate(c[0].x+vmag*Math.cos(aVal),
                                c[0].y+vmag*Math.sin(aVal));
        c[2] = new Coordinate(c[1].x+amag*Math.cos(aVal+aa),
                                c[1].y+amag*Math.sin(aVal+aa));
        c[3] = c[1];
        c[4] = new Coordinate(c[1].x+amag*Math.cos(aVal-aa),
                                c[1].y+amag*Math.sin(aVal-aa));
        return c;
    }

    /**
     * Creates and returns a FeatureCollection for the 3D field sliced at
     * vertical index k.
     *
     * @param k - integer; depth index (0 or 1 to N).
     * @return FeatureCollection.
     */
    @Override
    public FeatureCollection createFeatureCollection(int k)
            throws SchemaException, IllegalAttributeException, TransformException {
        gf = new GeometryFactory();

        ModelGrid3D grid = romsGI.getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c;
        LineString ls;
        Feature f;
        double uVal;
        double vVal;
        double sVal;
        double aVal;
        double rVal;
        double aa   = Math.toRadians(180-style.getArrowheadAngle());
        double[] pos = new double[3];
        //need to create a line feature that creates the vector shape
        double stride = style.getStride();
        for (double j=Math.max(1, stride);j<=Mm;j=j+stride) {//TODO: make sure indices are correct!
            for (double i=Math.max(1,stride);i<=Lm;i=i+stride) {
                if (grid.getMask_RHO((int)i,(int)j)>0.0) {
                    pos[0] = i; pos[1] = j; pos[2] = (double) k;
                    lon = PrimeMeridian.adjustToGISlon(i3d.interpolateLon(pos));
                    lat = i3d.interpolateLat(pos);
                    uVal = i3d.interpolateValue(pos, xComp, Interpolator3D.INTERP_VAL);
                    vVal = i3d.interpolateValue(pos, yComp, Interpolator3D.INTERP_VAL);
                    sVal  = Math.sqrt(uVal*uVal+vVal*vVal);//speed
                    rVal = i3d.interpolateValue(pos, "angle", null, Interpolator3D.INTERP_VAL);//angle between xi axis & east in radians
                    //System.out.println("rVal = "+Math.toDegrees(rVal));
                    if (!isFinite(sVal)) {
                        sVal = 0.0;
                        aVal = rVal;
                    } else {
                        aVal = Math.atan2(vVal, uVal)+rVal;//CCW angle in radians from east (if rVal interpreted corrctly)
                    }
                    srcPts[0] = lon;
                    srcPts[1] = lat;
                    AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
                    c = createArrow(sVal,aVal,dstPts,aa);
                    if (c[0]!=null) {
                        ls = gf.createLineString(c);
                        f = ft.create(new Object[] {ls,""+(int)i+"_"+(int)j,new Double(lat),new Double(lon),
                                new Double(sVal), new Double(Math.toDegrees(aVal))});
                        fc.add(f);
                    }
                }
            }
        }
        double[] mm = FeatureCollectionUtilities.findMinMax("speed",fc);
        min = mm[0];
        max = mm[1];
        return fc;
    }
    
    /**
     * Creates and returns a FeatureCollection for the 3D field sliced at
     * constant depth z (<0 m);
     *
     * @param z - double; depth (<0 m).
     * @return FeatureCollection.
     */
    @Override
    public FeatureCollection createFeatureCollection(double z)
            throws SchemaException, IllegalAttributeException, TransformException {
        gf = new GeometryFactory();

        ModelGrid3D grid = romsGI.getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c;
        LineString ls;
        Feature f;
        double uVal;
        double vVal;
        double sVal;
        double aVal;
        double rVal;
        double aa   = Math.toRadians(180-style.getArrowheadAngle());
        double[] pos = new double[3];
        //need to create a line feature that creates the vector shape
        double stride = style.getStride();
        for (double j=Math.max(1, stride);j<=Mm;j=j+stride) {//TODO: make sure indices are correct!
            for (double i=Math.max(1,stride);i<=Lm;i=i+stride) {
                if (grid.getMask_RHO((int)i,(int)j)>0.0) {
                    pos[0] = i; pos[1] = j; pos[2]=0;
                    lon = PrimeMeridian.adjustToGISlon(i3d.interpolateLon(pos));
                    lat = i3d.interpolateLat(pos);
                    try {
                        if (-z > i3d.interpolateBathymetricDepth(pos)){
                            //requested depth greater than bathymetric depth, so skip
                        } else if (-z < i3d.interpolateSSH(pos)){
                            //requested depth is less than sea surface height, so skip
                        } else {
                            //requested depth is within bounds
                            pos[2] = i3d.calcKfromZ(i, j, z);//calculate sigma from depth
                            uVal = i3d.interpolateValue(pos, xComp, Interpolator3D.INTERP_VAL);
                            vVal = i3d.interpolateValue(pos, yComp, Interpolator3D.INTERP_VAL);
                            sVal  = Math.sqrt(uVal*uVal+vVal*vVal);//speed
                            rVal = i3d.interpolateValue(pos, "angle", null, Interpolator3D.INTERP_VAL);//angle between xi axis & east in radians
                            //System.out.println("rVal = "+Math.toDegrees(rVal));
                            if (!isFinite(sVal)) {
                                sVal = 0.0;
                                aVal = rVal;
                            } else {
                                aVal = Math.atan2(vVal, uVal)+rVal;//CCW angle in radians from east (if rVal interpreted corrctly)
                            }
                            srcPts[0] = lon;
                            srcPts[1] = lat;
                            AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
                            c = createArrow(sVal,aVal,dstPts,aa);
                            if (c[0]!=null) {
                                ls = gf.createLineString(c);
                                f = ft.create(new Object[] {ls,""+(int)i+"_"+(int)j,new Double(lat),new Double(lon),
                                        new Double(sVal), new Double(Math.toDegrees(aVal))});
                                fc.add(f);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException ex){
                        System.out.println("Could not interpolate to "+z+"at ["+lon+","+lat+"]");
                    }
                }
            }
        }
        double[] mm = FeatureCollectionUtilities.findMinMax("speed",fc);
        min = mm[0];
        max = mm[1];
        return fc;
    }

    /**
     * Creates and returns a FeatureCollection for a 2D field suitable for
     * export to a shapefile.
     *
     * @return FeatureCollection.
     */
    private FeatureCollection createFeatureCollection1()
            throws SchemaException, IllegalAttributeException, TransformException {
        int nf = 0;
        String sDate = date.getDateTimeString();
        FeatureCollection fcp = FeatureCollections.newCollection();
        //Create feature type for export
        AttributeType[] aTypes = new AttributeType[4];
        GeometryAttributeType gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.Point.class);
        int idx = 0;
        aTypes[idx++] = gat;
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("date",
                             java.lang.String.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("speed",
                             java.lang.Double.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("angle",
                             java.lang.Double.class);
        FeatureType ftp = FeatureTypeFactory.newFeatureType(aTypes,"uv");
        //Create the mask features and add them to the feature collection
        gf = new GeometryFactory();

        ModelGrid3D grid = romsGI.getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate c;
        Point pt;
        Feature f;
        double uVal;
        double vVal;
        double sVal;
        double aVal;
        double rVal;
        double aa   = Math.toRadians(180-style.getArrowheadAngle());
        double[] pos = new double[2];
        //need to create a line feature that creates the vector shape
        double stride = style.getStride();
        for (double j=Math.max(1, stride);j<=Mm;j=j+stride) {//TODO: make sure indices are correct!
            for (double i=Math.max(1,stride);i<=Lm;i=i+stride) {
                if (grid.getMask_RHO((int)i,(int)j)>0.0) {
                    pos[0] = i; pos[1] = j;
                    lon = PrimeMeridian.adjustToGISlon(i3d.interpolateLon(pos));
                    lat = i3d.interpolateLat(pos);
                    uVal = i3d.interpolateValue(pos, xComp, Interpolator3D.INTERP_VAL);
                    vVal = i3d.interpolateValue(pos, yComp, Interpolator3D.INTERP_VAL);
                    sVal  = Math.sqrt(uVal*uVal+vVal*vVal);//speed
                    rVal = i3d.interpolateValue(pos, "angle", null, Interpolator3D.INTERP_VAL);//angle between xi axis & east in radians
                    //System.out.println("rVal = "+Math.toDegrees(rVal));
                    if (!isFinite(sVal)) {
                        sVal = 0.0;
                        aVal = rVal;
                    } else {
                        aVal = Math.atan2(vVal, uVal)+rVal;//CCW angle in radians from east (if rVal interpreted corrctly)
                    }
                    srcPts[0] = lon;
                    srcPts[1] = lat;
                    AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
                    c = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                    if (c!=null) {
                        nf++;
                        pt = gf.createPoint(c);
                        f = ftp.create(new Object[] {pt, sDate, lat, lon, sVal, Math.toDegrees(aVal)});
                        fcp.add(f);
                    }
                }
            }
        }
        System.out.println("Created "+nf+" features.");
        return fcp;
    }

    /**
     * Creates and returns a FeatureCollection for a 3D field sliced at fixed k
     * for export to a shapefile.
     *
     * @param k - integer; depth index (0 or 1 to N).
     * @return FeatureCollection.
     */
    private FeatureCollection createFeatureCollection1(int k)
            throws SchemaException, IllegalAttributeException, TransformException {
        int nf = 0;
        String sDate = date.getDateTimeString();
        System.out.println("Creating export FeatureCollection for k = "+k+", date = "+sDate);
        Integer kSlice = k;
        FeatureCollection fcp = FeatureCollections.newCollection();
        AttributeType[] aTypes = new AttributeType[5];
        GeometryAttributeType gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.Point.class);
        int idx = 0;
        aTypes[idx++] = gat;
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("date",
                             java.lang.String.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("k",
                             java.lang.Integer.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("speed",
                             java.lang.Double.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("angle",
                             java.lang.Double.class);
        FeatureType ftp = FeatureTypeFactory.newFeatureType(aTypes,"uv");

        gf = new GeometryFactory();

        ModelGrid3D grid = romsGI.getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate c; Point pt; Feature f;
        double uVal; double vVal; double sVal; double aVal; double rVal;
        double[] pos = new double[3];
        double stride = style.getStride();
        for (double j=Math.max(1, stride);j<=Mm;j=j+stride) {//TODO: make sure indices are correct!
            for (double i=Math.max(1,stride);i<=Lm;i=i+stride) {
                if (grid.getMask_RHO((int)i,(int)j)>0.0) {
                    pos[0] = i; pos[1] = j; pos[2] = (double) k;
                    lon = PrimeMeridian.adjustToGISlon(i3d.interpolateLon(pos));
                    lat = i3d.interpolateLat(pos);
                    srcPts[0] = lon;
                    srcPts[1] = lat;
                    AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
                    c = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                    uVal = i3d.interpolateValue(pos, xComp, Interpolator3D.INTERP_VAL);
                    vVal = i3d.interpolateValue(pos, yComp, Interpolator3D.INTERP_VAL);
                    sVal  = Math.sqrt(uVal*uVal+vVal*vVal);//speed
                    rVal = i3d.interpolateValue(pos, "angle", null, Interpolator3D.INTERP_VAL);//angle between xi axis & east in radians
                    //System.out.println("rVal = "+Math.toDegrees(rVal));
                    if (!isFinite(sVal)) {
                        sVal = 0.0;
                        aVal = rVal;
                    } else {
                        aVal = Math.atan2(vVal, uVal)+rVal;//CCW angle in radians from east (if rVal interpreted corrctly)
                    }
                    if (c!=null) {
                        nf++;
                        pt = gf.createPoint(c);
                        f = ftp.create(new Object[] {
                                pt,
                                sDate,
                                kSlice,
                                new Double(sVal),
                                new Double(Math.toDegrees(aVal))});
                        fcp.add(f);
                    }
                }
            }
        }
        System.out.println("Created "+nf+" features.");
        return fcp;
    }

    /**
     * Creates and returns a FeatureCollection for a 3D field sliced at fixed z
     * (<0 m) for export to a shapefile.
     *
     * @param z - double; depth (<0 m).
     * @return FeatureCollection.
     */
    private FeatureCollection createFeatureCollection1(double z)
            throws SchemaException, IllegalAttributeException, TransformException {
        int nf = 0;
        String sDate = date.getDateTimeString();
        System.out.println("Creating export FeatureCollection for z = "+z+"; date = "+sDate);
        Double zSlice = z;
        FeatureCollection fcp = FeatureCollections.newCollection();
        AttributeType[] aTypes = new AttributeType[5];
        GeometryAttributeType gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.Point.class);
        int idx = 0;
        aTypes[idx++] = gat;
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("date",
                             java.lang.String.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("z",
                             java.lang.Double.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("speed",
                             java.lang.Double.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("angle",
                             java.lang.Double.class);
        FeatureType ftp = FeatureTypeFactory.newFeatureType(aTypes,"uv");

        gf = new GeometryFactory();

        ModelGrid3D grid = romsGI.getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate c; Point pt; Feature f;
        double uVal; double vVal; double sVal; double aVal; double rVal;
        double[] pos = new double[3];
        double stride = style.getStride();
        for (double j=Math.max(1, stride);j<=Mm;j=j+stride) {//TODO: make sure indices are correct!
            for (double i=Math.max(1,stride);i<=Lm;i=i+stride) {
                if (grid.getMask_RHO((int)i,(int)j)>0.0) {
                    pos[0] = i; pos[1] = j; pos[2]=0;
                    lon = PrimeMeridian.adjustToGISlon(i3d.interpolateLon(pos));
                    lat = i3d.interpolateLat(pos);
                    try {
                        if (-z > i3d.interpolateBathymetricDepth(pos)){
                            //requested depth greater than bathymetric depth, so skip
                        } else if (-z < i3d.interpolateSSH(pos)){
                            //requested depth is less than sea surface height, so skip
                        } else {
                            //requested depth is within bounds
                            pos[2] = i3d.calcKfromZ(i, j, z);//calculate sigma from depth
                            srcPts[0] = lon;
                            srcPts[1] = lat;
                            AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
                            c = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                            uVal = i3d.interpolateValue(pos, xComp, Interpolator3D.INTERP_VAL);
                            vVal = i3d.interpolateValue(pos, yComp, Interpolator3D.INTERP_VAL);
                            sVal  = Math.sqrt(uVal*uVal+vVal*vVal);//speed
                            rVal = i3d.interpolateValue(pos, "angle", null, Interpolator3D.INTERP_VAL);//angle between xi axis & east in radians
                            //System.out.println("rVal = "+Math.toDegrees(rVal));
                            if (!isFinite(sVal)) {
                                sVal = 0.0;
                                aVal = rVal;
                            } else {
                                aVal = Math.atan2(vVal, uVal)+rVal;//CCW angle in radians from east (if rVal interpreted corrctly)
                            }
                            if (c!=null) {
                                nf++;
                                pt = gf.createPoint(c);
                                f = ftp.create(new Object[] {pt, sDate, zSlice, sVal, Math.toDegrees(aVal)});
                                fcp.add(f);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException ex){
                        System.out.println("Could not interpolate to "+z+"at ["+lon+","+lat+"]");
                    }
                }
            }
        }
        System.out.println("Created "+nf+" features.");
        return fcp;
    }

    /**
     * Exports a constant k-sliced plane of the vector field to a point shapefile.
     * Shapefile fields are date (as String), k (depth index), speed, and angle.
     *
     * @param k       - integer; depth index at which to slice 3D field
     * @param shpFile - String;  name of shapefile to export to
     * @param add     - boolean; flag to create new (false) or add to existing (true) shapefile
     *
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws org.geotools.factory.FactoryConfigurationError
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    @Override
    public void exportToShapefile(int k, String shpFile, boolean add)
            throws MalformedURLException, IOException, FactoryConfigurationError,
                   SchemaException, IllegalAttributeException,
                   TransformException {
        URL url = (new File(shpFile)).toURI().toURL();
        //Create feature type for vector map data
        FeatureCollection fcp = createFeatureCollection1(k);

        if (!fcp.isEmpty()) {
            ShapefileCreator sc = new ShapefileCreator();
            sc.setShapefileURL(url);
            if (add) {
                sc.addToShapefile(fcp);
            } else {
                sc.createShapefile(fcp);
            }
        } else {
            System.out.println("FeatureCollection was empty!");
        }
    }

    /**
     * Exports a constant depth-sliced plane of the vector field to a point shapefile.
     * Shapefile fields are date (as String), z (depth), speed, and angle.
     *
     * @param z       -  double; depth (<0 m) at which to slice 3D field
     * @param shpFile - String;  name of shapefile to export to
     * @param add     - boolean; flag to create new (false) or add to existing (true) shapefile
     *
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     * @throws org.geotools.factory.FactoryConfigurationError
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    @Override
    public void exportToShapefile(double z, String shpFile, boolean add)
            throws MalformedURLException, IOException, FactoryConfigurationError,
                   SchemaException, IllegalAttributeException, TransformException {
        URL url = (new File(shpFile)).toURI().toURL();
        FeatureCollection fcp = createFeatureCollection1(z);

        if (!fcp.isEmpty()) {
            ShapefileCreator sc = new ShapefileCreator();
            sc.setShapefileURL(url);
            if (add) {
                sc.addToShapefile(fcp);
            } else {
                sc.createShapefile(fcp);
            }
        } else {
            System.out.println("FeatureCollection was empty!");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(VectorStyle.PROP_STYLE)){
            logger.info("Style changed");
        }
    }

    /**
     * Sets the VectorStyle to apply in future calls to 
     * createFeatureCollection(...) and exportToShapefile(...). 
     * @param s 
     */
    @Override
    public void setStyle(Style s) {
        if (s instanceof VectorStyle){
            if (style!=null) style.removePropertyChangeListener(this);
            style = (VectorStyle)s;
            style.addPropertyChangeListener(this);
        }
    }
}
