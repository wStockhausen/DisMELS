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
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.ProjectedCoordinateSystem;
import org.geotools.ct.MathTransform;
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
//import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import wts.roms.model.GlobalInfo;
//import wts.GIS.crs.CRSCreator;
import wts.roms.model.Interpolator2D;
import wts.roms.model.ModelGrid2D;

/**
 *
 * @author William Stockhausen
 */
public class ModelGrid2DMapData {
    
    private GlobalInfo romsGI;
    
    private FeatureType ftLines;
    private FeatureType ftMask;
    private FeatureType ftBathym;
    private GeometryFactory gf;
//    private GeometryCoordinateSequenceTransformer gcst;//gt2.1+
    private MathTransform mt; //gt2.1-
    
    /**
     * Creates a new instance of ModelGrid2DMapData
     */
    public ModelGrid2DMapData() throws Exception {        
        romsGI = GlobalInfo.getInstance();
        
        //Create the coordinate transformer--Geotools 2.0/2.1
        GeographicCoordinateSystem nad83 = 
                CSCreator.createNAD_83();
        ProjectedCoordinateSystem nad83albers = 
                CSCreator.createAlbersPCS(CSCreator.NAD_83);
        mt = CSCreator.getTransformation(nad83,nad83albers).getMathTransform();
        
        //Create the GeometryFactory
        gf = new GeometryFactory();        
        
        //Create feature type for grid lines
        AttributeType[] aTypes = new AttributeType[3];
        GeometryAttributeType gat = 
                (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                            "Geometry",
                             com.vividsolutions.jts.geom.LineString.class);
//                            ,
//                             true,0,null,nad83albers);
        aTypes[0] = gat;
        aTypes[1] = AttributeTypeFactory.newAttributeType("xi",
                             java.lang.Integer.class);
        aTypes[2] = AttributeTypeFactory.newAttributeType("eta",
                             java.lang.Integer.class);
        //Create the FeatureType
        ftLines = FeatureTypeFactory.newFeatureType(aTypes,"GridLine");
        
        //Create feature type for polygon mask
        aTypes = new AttributeType[4];
        gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.Polygon.class);
//                    ,
//                    true,0,null,nad83albers);
        aTypes[0] = gat;
        aTypes[1] = AttributeTypeFactory.newAttributeType("ID",
                             java.lang.String.class);
        aTypes[2] = AttributeTypeFactory.newAttributeType("xi",
                             java.lang.Integer.class);
        aTypes[3] = AttributeTypeFactory.newAttributeType("eta",
                             java.lang.Integer.class);
        ftMask = FeatureTypeFactory.newFeatureType(aTypes,"Mask");
//        FeatureTypeFactory.transform(ftMask,nad83);
        
        //Create feature type for bathymetry
        aTypes = new AttributeType[5];
        gat = (GeometryAttributeType)AttributeTypeFactory.newAttributeType(
                    "Geometry",
                    com.vividsolutions.jts.geom.Polygon.class);
//                    ,
//                    true,0,null,nad83albers);
        aTypes[0] = gat;
        aTypes[1] = AttributeTypeFactory.newAttributeType("ID",
                             java.lang.String.class);
        aTypes[2] = AttributeTypeFactory.newAttributeType("xi",
                             java.lang.Integer.class);
        aTypes[3] = AttributeTypeFactory.newAttributeType("eta",
                             java.lang.Integer.class);
        aTypes[4] = AttributeTypeFactory.newAttributeType("Z",
                             java.lang.Double.class);
        ftBathym = FeatureTypeFactory.newFeatureType(aTypes,"Bathymetry");
//        FeatureTypeFactory.transform(ftMask,nad83);
        
        //Create the coordinate transformer--Geotools 2.1 and higher
//        gcst = new GeometryCoordinateSequenceTransformer();
//        GeographicCRS nad83      = CRSCreator.getNAD83();
//        ProjectedCRS nad83albers = CRSCreator.getAlbers(nad83);
//        MathTransform mt         = CRSCreator.getCRSTransform(nad83,nad83albers);
//        gcst.setMathTransform(mt);
    }

    /**
     * Getter for property featureCollection.
     * @return Value of property featureCollection.
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public FeatureCollection getGridLines() 
            throws SchemaException,IllegalAttributeException, TransformException {
        return getGridLines(1);
    }

    /**
     * Getter for property featureCollection.
     * @param spc - grid interval to output
     * @return Value of property featureCollection.
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public FeatureCollection getGridLines(int spc) 
            throws SchemaException, IllegalAttributeException, TransformException {
        
        //Now create the features and add them to the feature collection
        FeatureCollection fc = FeatureCollections.newCollection();
        
        ModelGrid2D grid2D = romsGI.getGrid();
        int L = grid2D.getL();
        int M = grid2D.getM();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c = null;
        LineString ls = null;
        Feature f;
        //Get coords along lines of constant eta
        for (int j=0;j<=M;j=j+spc) {
            c = new Coordinate[L+1];
            for (int i=0;i<=L;i++) {
                lon = PrimeMeridian.adjustToGISlon(grid2D.getLon_RHO(i,j));
                lat = grid2D.getLat_RHO(i,j);
                srcPts[0] = lon;
                srcPts[1] = lat;
                AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
//                c[i] = new Coordinate(lon,lat);//gt2.1+
                c[i] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
            }
            ls = gf.createLineString(c);//gt2.1-
//            ls = gcst.transformLineString(gf.createLineString(c),gf);//gt2.1+
            f = ftLines.create(new Object[] {ls,new Integer(-1),new Integer(j)});
//            System.out.println("const j line bounds: "+j+f.getBounds().toString());
            fc.add(f);
        }
        //Now get coords along lines of constant xi
        for (int i=0;i<=L;i=i+spc) {
            c = new Coordinate[M+1];
            for (int j=0;j<=M;j++) {
                lon = PrimeMeridian.adjustToGISlon(grid2D.getLon_RHO(i,j));
                lat = grid2D.getLat_RHO(i,j);
                srcPts[0] = lon;
                srcPts[1] = lat;
                AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
//                c[j] = new Coordinate(lon,lat);//gt2.1+
                c[j] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
              }
            ls = gf.createLineString(c);//gt2.1-
//            ls = gcst.transformLineString(gf.createLineString(c),gf);//gt2.1+
            f = ftLines.create(new Object[] {ls, i, -1});
//            System.out.println("const i line bounds: "+i+f.getBounds().toString());
            fc.add(f);
        }
        return fc;
    }
    
    /**
     * Getter for property featureCollection.
     * @return Value of property featureCollection.
     * @throws SchemaException
     * @throws TransformException
     * @throws IllegalAttributeException
     */
    public FeatureCollection getMask() 
            throws SchemaException, IllegalAttributeException, TransformException {       
        //Create the mask features and add them to the feature collection
        FeatureCollection fc = FeatureCollections.newCollection();

        ModelGrid2D grid2D = romsGI.getGrid();
        int Lm = grid2D.getLm();
        int Mm = grid2D.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c = null;
        Polygon p = null;
        LinearRing lr = null;
        Feature f;
        //Get coords along lines of constant eta
        int[] jj = new int[] {0,0,1,1,0};
        int[] ii = new int[] {0,1,1,0,0};
        int ij = 0;
        for (int j=1;j<=Mm;j++) {
            for (int i=1;i<=Lm;i++) {
                c = new Coordinate[5];
                int mflag = grid2D.getMask_RHO(i,j);
                if (mflag<0.5) {
                    ij++;
                    for (int k=0;k<5;k++) {
                            lon = PrimeMeridian.adjustToGISlon(grid2D.getLon_PSI(i+ii[k],j+jj[k]));
                            lat = grid2D.getLat_PSI(i+ii[k],j+jj[k]);
                            srcPts[0] = lon;
                            srcPts[1] = lat;
                            AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
//                          c[k] = new Coordinate(lon,lat);//gt2.1+
                            c[k] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                    }
                }
                if (c[0]!=null) {
                    lr = gf.createLinearRing(c);
                    p = gf.createPolygon(lr,null);
//                    p = gcst.transformPolygon(gf.createPolygon(lr,null),gf);//gt2.1+
                    f = ftMask.create(new Object[] {p,""+i+"_"+j, i, j});
                    fc.add(f);
                }
            }
        }
        System.out.println("Creating landmask: mask uses "+ij+" cells of "+Mm*Lm+" available");
        return fc;
    }
    
    /**
     * Returns grid bathymetry as a FeatureCollection
     * @return FeatureCollection.
     * @throws org.geotools.feature.SchemaException
     * @throws org.geotools.feature.IllegalAttributeException
     * @throws org.opengis.referencing.operation.TransformException
     */
    public FeatureCollection getBathymetry() 
            throws SchemaException, IllegalAttributeException, TransformException {       
        //Create the mask features and add them to the feature collection
        FeatureCollection fc = FeatureCollections.newCollection();

        ModelGrid2D grid2D = romsGI.getGrid();
        int Lm = grid2D.getLm();
        int Mm = grid2D.getMm();
        double lon,lat;
        double[] srcPts = new double[2];//gt2.1-
        double[] dstPts = new double[2];//gt2.1-
        Coordinate[] c = null;
        Polygon p = null;
        LinearRing lr = null;
        Feature f = null;
        double bathym = 0;
        //Get coords along lines of constant eta
        int[] jj = new int[] {0,0,1,1,0};
        int[] ii = new int[] {0,1,1,0,0};
        for (int j=1;j<=Mm;j++) {
            for (int i=1;i<=Lm;i++) {
                if (grid2D.getMask_RHO(i,j)>0.0) {
                    bathym=grid2D.getH(i, j);//i,j NOT a masked cell
                    c = new Coordinate[5];
                    for (int k=0;k<5;k++) {
                            lon = PrimeMeridian.adjustToGISlon(grid2D.getLon_PSI(i+ii[k],j+jj[k]));
                            lat = grid2D.getLat_PSI(i+ii[k],j+jj[k]);
                            srcPts[0] = lon;
                            srcPts[1] = lat;
                            AlbersNAD83.transformGtoP(srcPts,0,dstPts,0,1);//gt2.1-
    //                          c[k] = new Coordinate(lon,lat);//gt2.1+
                            c[k] = new Coordinate(dstPts[0],dstPts[1]);//gt2.1-
                    }
                    if (c[0]!=null) {
                        lr = gf.createLinearRing(c);
                        p = gf.createPolygon(lr,null);
    //                    p = gcst.transformPolygon(gf.createPolygon(lr,null),gf);//gt2.1+
                        f = ftBathym.create(new Object[] {p,""+i+"_"+j, i, j, bathym});
                        fc.add(f);
                    }
                }
            }
        }
        return fc;
    }
    
    /**
     * Interpolates the bathymetric depth grid to the input
     * lat, lon coordinates.
     *
     *@param lon - NAD83 longitude (deg) [PM Greenwich, -180 to 180]
     *@param lat - NAD83 latitude (deg), [-90 to 90]
     *@return - bathymetric depth (d > 0)
     */
    public double interpolateBathymetricDepth(double lon, double lat) {
//        lon = PrimeMeridian.adjustToROMSlon(lon);
        //now compute location in grid coordinates
        double[] posIJ = romsGI.getGrid().computeIJfromLL(lat,lon);
        return romsGI.getInterpolator().interpolateBathymetricDepth(posIJ);
    }
    
    /**
     * Interpolates the bathymetric depth grid to the input
     * grid position vector (xi,eta).
     *
     *@param posIJ - horizontal position vector relative to grid (xi,eta).
     *@return - bathymetric depth (d > 0)
     */
    public double interpolateBathymetricDepth(double[] posIJ) {
        return romsGI.getInterpolator().interpolateBathymetricDepth(posIJ);
    }
    
    /**
     * Tests whether position is on land.
     * @param pos
     * @return 
     */
    public boolean isOnLand(double[] pos){
        return romsGI.getGrid().isOnLand(pos);
    }
}
