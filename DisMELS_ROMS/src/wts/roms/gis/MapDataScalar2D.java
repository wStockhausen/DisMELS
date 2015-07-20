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
import wts.roms.model.Interpolator2D;
import wts.roms.model.ModelData;
import wts.roms.model.ModelGrid2D;

/**
 * Map data class for 2D scalar fields.
 * 
 * @author William Stockhausen
 */
public class MapDataScalar2D extends AbstractMapDataScalar2D {

    protected ModelData field  = null;

    protected ColorBarStyle style = null;
    
    private static final Logger logger = Logger.getLogger(MapDataScalar2D.class.getName());

    /**
     * Constructor for the scalar field encapsulated in a ModelData instance.
     *
     * @param md   - ModelData representation of field
     *
     * @throws java.lang.Exception
     */
    public MapDataScalar2D(ModelData md) throws Exception {
        super();
        this.date = null;
        this.field = md;
        this.mask  = romsGI.getGrid().getGridMask(md.getName());
        this.fc    = FeatureCollections.newCollection();
        initialize();
    }

    /**
     * Constructor for the scalar field encapsulated in a ModelData instance.
     *
     * @param md   - ModelData representation of field
     * @param date - a DateTime instance of the time corresponding to the data
     *
     * @throws java.lang.Exception
     */
    public MapDataScalar2D(ModelData md, DateTime date) throws Exception {
        super();
        this.date = date;
        this.field = md;
        this.mask  = romsGI.getGrid().getGridMask(md.getName());
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
     * Creates and returns a FeatureCollection for a 2D field.
     * @return FeatureCollection.
     */
    @Override
    public FeatureCollection createFeatureCollection()
            throws SchemaException, IllegalAttributeException, TransformException {
        ModelGrid2D grid = romsGI.getGrid();
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
                    value = field.getValue(i, j);
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
     * Creates and returns a FeatureCollection for a 2D field.
     * @return FeatureCollection.
     */
    private FeatureCollection createFeatureCollection1()
            throws SchemaException, IllegalAttributeException, TransformException {
        ModelGrid2D grid = romsGI.getGrid();
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
     * Exports the 2D scalar field to a polygon shapefile.
     * Shapefile fields are ID, xi, eta, date (as String), value.
     *
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
    public void exportToShapefile(String shpFile,boolean add)
            throws MalformedURLException, IOException, FactoryConfigurationError, SchemaException,
                   IllegalAttributeException, TransformException {
        URL url = (new File(shpFile)).toURI().toURL();
        //Create feature type for vector map data
        FeatureCollection fcp = createFeatureCollection1();

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
