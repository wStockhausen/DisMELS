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
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;
import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.ProjectedCoordinateSystem;
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
//import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
//import wts.GIS.crs.CRSCreator;
import wts.GIS.shapefile.ShapefileCreator;
import wts.GIS.styling.ColorBarStyle;
import wts.GIS.utils.FeatureCollectionUtilities;
import wts.models.utilities.DateTime;
import wts.roms.model.GlobalInfo;
import wts.roms.model.Interpolator3D;
import wts.roms.model.MaskData;
import wts.roms.model.ModelData;
import wts.roms.model.ModelGrid3D;

/**
 *
 * @author William Stockhausen
 */
public class MapDataScalar3D extends AbstractMapDataScalar3D {

    protected ModelData field  = null;

    protected ColorBarStyle style = null;
    
    private static final Logger logger = Logger.getLogger(MapDataScalar3D.class.getName());

    /**
     * Constructor for the scalar field encapsulated in a ModelData instance.
     *
     * @param md   - ModelData representation of field
     * @param i3d  - Interpolator3D instance (does not need to encapsulate a PhysicalEnvironment instance)
     * @param date - a DateTime instance of the time corresponding to the data
     *
     * @throws java.lang.Exception
     */
    public MapDataScalar3D(ModelData md, Interpolator3D i3d, DateTime date) throws Exception {
        super();
        this.field = md;
        this.i3d   = i3d;
        this.date  = date;
        String maskField = GlobalInfo.getInstance().getMaskForField(md.getName());
        logger.info("maskField for "+md.getName()+" is "+maskField);
        this.mask  = (MaskData) GlobalInfo.getInstance().getGrid3D().getGridField(maskField);
        if (this.mask == null) logger.info("--mask field not found!");
        this.fc    = FeatureCollections.newCollection();
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

        //Create feature type for bathymetry
        AttributeType[] aTypes = new AttributeType[5];
        GeometryAttributeType gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.Polygon.class);
        aTypes[0] = gat;
        aTypes[1] = AttributeTypeFactory.newAttributeType("ID",
                             java.lang.String.class);
        aTypes[2] = AttributeTypeFactory.newAttributeType("xi",
                             java.lang.Integer.class);
        aTypes[3] = AttributeTypeFactory.newAttributeType("eta",
                             java.lang.Integer.class);
        aTypes[4] = AttributeTypeFactory.newAttributeType("Value",
                             java.lang.Double.class);
        ft = FeatureTypeFactory.newFeatureType(aTypes,field.getName());
    }
    
    @Override
    public ColorBarStyle getStyle() {
        return style;
    }

    @Override
    public void setStyle(Style s) {
        if (s instanceof ColorBarStyle) style = (ColorBarStyle) s;
    }
    /**
     * Creates and returns a FeatureCollection for the 3D field sliced at
     * vertical grid index k.
     * @return FeatureCollection.
     */
    @Override
    public FeatureCollection createFeatureCollection(int k)
            throws SchemaException, IllegalAttributeException, TransformException {       
        ModelGrid3D grid = GlobalInfo.getInstance().getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c; Polygon p; LinearRing lr; Feature f; double value;
        //Get coords along lines of constant eta
        int[] jj = new int[] {0,0,1,1,0};
        int[] ii = new int[] {0,1,1,0,0};
        for (int j=1;j<=Mm;j++) {//TODO: make sure indices are correct!
            for (int i=1;i<=Lm;i++) {
                if (grid.getMask_RHO(i,j)>0.0) {
                    value = field.getValue(i, j, k);
                    c = new Coordinate[5];
                    for (int m=0;m<5;m++) {
                            lon = PrimeMeridian.adjustToGISlon(grid.getLon_PSI(i+ii[m],j+jj[m]));
                            lat = grid.getLat_PSI(i+ii[m],j+jj[m]);
                            srcPts[0] = lon;
                            srcPts[1] = lat;
                            AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
//                          c[m] = new Coordinate(lon,lat);//gt2.1+
                            c[m] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                    }
                    if (c[0]!=null) {
                        lr = gf.createLinearRing(c);
                        p = gf.createPolygon(lr,null);
    //                    p = gcst.transformPolygon(gf.createPolygon(lr,null),gf);//gt2.1+
                        f = ft.create(new Object[] {p,""+i+"_"+j,new Integer(i),new Integer(j),new Double(value)});
                        fc.add(f);
                    }
                }
            }
        }
        double[] mm = FeatureCollectionUtilities.findMinMax("Value",fc);
        min = mm[0];
        max = mm[1];
        return fc;
    }

    /**
     * Returns a FeatureCollection for the field interpolated to depth z
     * @param z - depth (m; >0) at which to interpolate field
     * @return FeatureCollection.
     */
    @Override
    public FeatureCollection createFeatureCollection(double z)
            throws SchemaException, IllegalAttributeException, TransformException {
        ModelGrid3D grid = GlobalInfo.getInstance().getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c; Polygon p; LinearRing lr; Feature f;
        //Get coords along lines of constant eta
        int[] jj = new int[] {0,0,1,1,0};
        int[] ii = new int[] {0,1,1,0,0};
        double[] pos = new double[]{0,0,0};
        for (int j=1;j<=Mm;j++) {//TODO: make sure indices are correct!
            for (int i=1;i<=Lm;i++) {
                try {
                    if (grid.getMask_RHO(i,j)>0.0) {
                        pos[0] = (double) i; pos[1] = (double) j;
                        if (-z > i3d.interpolateBathymetricDepth(pos)){
                            //requested depth greater than bathymetric depth, so skip
                        } else if (-z < i3d.interpolateSSH(pos)){
                            //requested depth is less than sea surface height, so skip
                        } else {
                            //requested depth is within bounds
                            pos[2] = i3d.calcKfromZ((double) i, (double)j, z);
                            double value = i3d.interpolateValue3D(pos, field, mask, Interpolator3D.INTERP_VAL);
                            c = new Coordinate[5];
                            for (int m=0;m<5;m++) {
                                    lon = PrimeMeridian.adjustToGISlon(grid.getLon_PSI(i+ii[m],j+jj[m]));
                                    lat = grid.getLat_PSI(i+ii[m],j+jj[m]);
                                    srcPts[0] = lon;
                                    srcPts[1] = lat;
                                    AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
        //                          c[m] = new Coordinate(lon,lat);//gt2.1+
                                    c[m] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                            }
                            if (c[0]!=null) {
                                lr = gf.createLinearRing(c);
                                p = gf.createPolygon(lr,null);
                                f = ft.create(new Object[] {p,""+i+"_"+j,new Integer(i),new Integer(j),new Double(value)});
                                fc.add(f);
                            }
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ex){
                    logger.info("Could not interpolate to "+z+"at polygon ["+i+","+j+"]");
                }
            }
        }
        double[] mm = FeatureCollectionUtilities.findMinMax("Value",fc);
        min = mm[0];
        max = mm[1];
        return fc;
    }

    /**
     * Creates and returns a FeatureCollection for a 2D field.
     * @return FeatureCollection.
     */
    private FeatureCollection createFeatureCollection1()
            throws SchemaException, IllegalAttributeException, TransformException {
        int nf = 0;
        String sDate = date.getDateTimeString();
        logger.info("Creating export FeatureCollection for date = "+sDate);
        FeatureCollection fcp = FeatureCollections.newCollection();
        //Create feature type for export
        AttributeType[] aTypes = new AttributeType[6];
        GeometryAttributeType gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.MultiPolygon.class);
        int idx = 0;
        aTypes[idx++] = gat;
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("ID",
                             java.lang.String.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("xi",
                             java.lang.Integer.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("eta",
                             java.lang.Integer.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("date",
                             java.lang.String.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("value",
                             java.lang.Double.class);
        FeatureType ftp = FeatureTypeFactory.newFeatureType(aTypes,field.getName());

        ModelGrid3D grid = GlobalInfo.getInstance().getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c; MultiPolygon mp; LinearRing lr; Feature f; double value;
        //Get coords along lines of constant eta
        int[] jj = new int[] {0,0,1,1,0};
        int[] ii = new int[] {0,1,1,0,0};
        for (int j=1;j<=Mm;j++) {//TODO: make sure indices are correct!
            for (int i=1;i<=Lm;i++) {
                if (grid.getMask_RHO(i,j)>0.0) {
                    value = field.getValue(i, j);
                    c = new Coordinate[5];
                    for (int m=0;m<5;m++) {
                            lon = PrimeMeridian.adjustToGISlon(grid.getLon_PSI(i+ii[m],j+jj[m]));
                            lat = grid.getLat_PSI(i+ii[m],j+jj[m]);
                            srcPts[0] = lon;
                            srcPts[1] = lat;
                            AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
                            c[m] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                    }
                    if (c[0]!=null) {
                        nf++;
                        lr = gf.createLinearRing(c);
                        mp = gf.createMultiPolygon(new Polygon[] {gf.createPolygon(lr,null)});
                        f = ftp.create(new Object[] {
                            mp,
                            ""+i+"_"+j,
                            new Integer(i),
                            new Integer(j),
                            sDate,
                            new Double(value)});
                        fcp.add(f);
                    }
                }
            }
        }
        logger.info("Created "+nf+" scalar features.");
        return fcp;
    }
    
    /**
     * Creates and returns a FeatureCollection for the 3D field sliced at
     * vertical index k.
     * @return FeatureCollection.
     */
    private FeatureCollection createFeatureCollection1(int k)
            throws SchemaException, IllegalAttributeException, TransformException {
        int nf = 0;
        String sDate = date.getDateTimeString();
        Integer kSlice = new Integer(k);
        logger.info("Creating export FeatureCollection for k = "+k+", date = "+sDate);
        FeatureCollection fcp = FeatureCollections.newCollection();
        //Create feature type for export
        AttributeType[] aTypes = new AttributeType[7];
        GeometryAttributeType gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.MultiPolygon.class);
        int idx = 0;
        aTypes[idx++] = gat;
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("ID",
                             java.lang.String.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("xi",
                             java.lang.Integer.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("eta",
                             java.lang.Integer.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("date",
                             java.lang.String.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("k",
                             java.lang.Integer.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("value",
                             java.lang.Double.class);
        FeatureType ftp = FeatureTypeFactory.newFeatureType(aTypes,field.getName());

        ModelGrid3D grid = GlobalInfo.getInstance().getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c; MultiPolygon mp; LinearRing lr; Feature f; double value;
        //Get coords along lines of constant eta
        int[] jj = new int[] {0,0,1,1,0};
        int[] ii = new int[] {0,1,1,0,0};
        for (int j=1;j<=Mm;j++) {//TODO: make sure indices are correct!
            for (int i=1;i<=Lm;i++) {
                if (grid.getMask_RHO(i,j)>0.0) {
                    value = field.getValue(i, j, k);
                    c = new Coordinate[5];
                    for (int m=0;m<5;m++) {
                            lon = PrimeMeridian.adjustToGISlon(grid.getLon_PSI(i+ii[m],j+jj[m]));
                            lat = grid.getLat_PSI(i+ii[m],j+jj[m]);
                            srcPts[0] = lon;
                            srcPts[1] = lat;
                            AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
//                          c[m] = new Coordinate(lon,lat);//gt2.1+
                            c[m] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                    }
                    if (c[0]!=null) {
                        nf++;
                        lr = gf.createLinearRing(c);
                        mp = gf.createMultiPolygon(new Polygon[] {gf.createPolygon(lr,null)});
                        f = ftp.create(new Object[] {
                            mp,
                            ""+i+"_"+j,
                            new Integer(i),
                            new Integer(j),
                            sDate,
                            kSlice,
                            new Double(value)});
                        fcp.add(f);
                    }
                }
            }
        }
        logger.info("Created "+nf+" scalar features.");
        return fcp;
    }

    /**
     * Returns a FeatureCollection for the field interpolated to depth z
     * @param z - depth (m; >0) at which to interpolate field
     * @return FeatureCollection.
     */
    private FeatureCollection createFeatureCollection1(double z)
            throws SchemaException, IllegalAttributeException, TransformException {
        int nf = 0;
        String sDate = date.getDateTimeString();
        Double zSlice = new Double(z);
        logger.info("Creating export FeatureCollection for z = "+z+"; date = "+sDate);
        FeatureCollection fcp = FeatureCollections.newCollection();
        //Create feature type for export
        AttributeType[] aTypes = new AttributeType[7];
        GeometryAttributeType gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.Polygon.class);
        int idx = 0;
        aTypes[idx++] = gat;
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("ID",
                             java.lang.String.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("xi",
                             java.lang.Integer.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("eta",
                             java.lang.Integer.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("date",
                             java.lang.String.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("z",
                             java.lang.Double.class);
        aTypes[idx++] = AttributeTypeFactory.newAttributeType("value",
                             java.lang.Double.class);
        FeatureType ftp = FeatureTypeFactory.newFeatureType(aTypes,field.getName());

        ModelGrid3D grid = GlobalInfo.getInstance().getGrid3D();
        int Lm = grid.getLm();
        int Mm = grid.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c; Polygon p; LinearRing lr; Feature f; double value;
        //Get coords along lines of constant eta
        int[] jj = new int[] {0,0,1,1,0};
        int[] ii = new int[] {0,1,1,0,0};
        double[] pos = new double[]{0,0,0};
        for (int j=1;j<=Mm;j++) {//TODO: make sure indices are correct!
            for (int i=1;i<=Lm;i++) {
                try {
                    if (grid.getMask_RHO(i,j)>0.0) {
                        pos[0] = (double) i; pos[1] = (double) j;
                        if (-z > i3d.interpolateBathymetricDepth(pos)){
                            //requested depth greater than bathymetric depth, so skip
                        } else if (-z < i3d.interpolateSSH(pos)){
                            //requested depth is less than sea surface height, so skip
                        } else {
                            //requested depth is within bounds
                            pos[2] = i3d.calcKfromZ((double) i, (double)j, z);
                            value = i3d.interpolateValue3D(pos, field, mask, Interpolator3D.INTERP_VAL);
                            c = new Coordinate[5];
                            for (int m=0;m<5;m++) {
                                    lon = PrimeMeridian.adjustToGISlon(grid.getLon_PSI(i+ii[m],j+jj[m]));
                                    lat = grid.getLat_PSI(i+ii[m],j+jj[m]);
                                    srcPts[0] = lon;
                                    srcPts[1] = lat;
                                    AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
                                    c[m] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                            }
                            if (c[0]!=null) {
                                nf++;
                                lr = gf.createLinearRing(c);
                                p = gf.createPolygon(lr,null);
                                f = ftp.create(new Object[] {
                                    p,
                                    ""+i+"_"+j,
                                    new Integer(i),
                                    new Integer(j),
                                    sDate,
                                    zSlice,
                                    new Double(value)});
                                fcp.add(f);
                            }
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException ex){
                    logger.info("Could not interpolate to "+z+"at polygon ["+i+","+j+"]");
                }
            }
        }
        logger.info("Created "+nf+" scalar features.");
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
                   SchemaException, IllegalAttributeException, TransformException {
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
            logger.info("FeatureCollection was empty!");
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
            logger.info("FeatureCollection was empty!");
        }
    }

    @Override
    public String getName() {
        return field.getName();
    }
}
