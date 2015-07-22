/*
 * CSCreator.java
 *
 * Created on June 15, 2006, 2:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gis;

import java.sql.SQLException;
import java.util.logging.Logger;
import org.geotools.cs.AxisInfo;
import org.geotools.cs.CoordinateSystem;
import org.geotools.cs.CoordinateSystemAuthorityFactory;
import org.geotools.cs.CoordinateSystemEPSGFactory;
import org.geotools.cs.CoordinateSystemFactory;
import org.geotools.cs.DatumType;
import org.geotools.cs.Ellipsoid;
import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.HorizontalDatum;
import org.geotools.cs.PrimeMeridian;
import org.geotools.cs.ProjectedCoordinateSystem;
import org.geotools.cs.Projection;
import org.geotools.cs.WGS84ConversionInfo;
import org.geotools.ct.CannotCreateTransformException;
import org.geotools.ct.CoordinateTransformation;
import org.geotools.ct.CoordinateTransformationFactory;
import org.geotools.ct.MathTransformFactory;
import org.geotools.units.Unit;
import org.opengis.referencing.FactoryException;

/**
 *
 * @author William Stockhausen
 */
public class CSCreator {

    /** Use to specify Albers projection for Alaska region */
    public static final String REGION_ALASKA = "Alaska";
    /** Use to specify Albers projection for Republic of Korea region */
    public static final String REGION_ROK = "ROK";
    public static final String NAD_83 = "NAD_83";
    public static final String WGS_84 = "WGS_84";
    public static final double GRS_1980_SEMIMAJOR_AXIS = 6378137.0;
    public static final double GRS_1980_SEMIMINOR_AXIS = 6356752.3141403561;

    private static String region = REGION_ALASKA;
    
    public static final CoordinateSystemFactory csf = 
            CoordinateSystemFactory.getDefault();
    
    private static final Logger logger = Logger.getLogger(CSCreator.class.getName());

    public static boolean isValidRegion(String newRegion) {
        boolean res = (newRegion.equals(REGION_ALASKA))||(newRegion.equals(REGION_ROK));
        return res;
    }
    
    /**
     * Creates a new instance of CSCreator
     */
    private CSCreator() {
    }

    public static String getRegion() {
        return region;
    }

    public static void setRegion(String reg){
        logger.info("Setting region to "+reg);
        region = reg;
    }
    
    public static GeographicCoordinateSystem createWGS_84() {
        GeographicCoordinateSystem cs = GeographicCoordinateSystem.WGS84;
        logger.info(WGS_84+": \n"+cs.toWKT());
        return cs;
    }
    
    public static GeographicCoordinateSystem createNAD_83() throws FactoryException {
        GeographicCoordinateSystem cs = null;
        
        Unit unit = Unit.DEGREE;
        
        Ellipsoid ellipsoid = csf.createEllipsoid(
                "GRS_1980",
                GRS_1980_SEMIMAJOR_AXIS,
                GRS_1980_SEMIMINOR_AXIS,
                Unit.METRE);
        
        WGS84ConversionInfo convert = new WGS84ConversionInfo();
        convert.dx = 0.0;
        convert.dy = 0.0;
        convert.dz = 0.0;
        
        HorizontalDatum datum = csf.createHorizontalDatum(
                NAD_83,
                DatumType.CLASSIC,
                ellipsoid,
                convert);
        
        PrimeMeridian meridian = PrimeMeridian.GREENWICH;
        
        cs = csf.createGeographicCoordinateSystem(
                NAD_83,
                unit,
                datum,
                meridian,
                AxisInfo.LONGITUDE,
                AxisInfo.LATITUDE);
         
        logger.info(NAD_83+": \n"+cs.toWKT());
         
        return cs;
    }
    
    public static ProjectedCoordinateSystem createAlbersPCS(String gcsName) 
                                                    throws FactoryException {
        String classification = "Albers_Conic_Equal_Area";
        ProjectedCoordinateSystem prjCS = null;
        GeographicCoordinateSystem gcs = null;
        if (gcsName.equalsIgnoreCase(NAD_83)) {
            gcs = createNAD_83();
        } else
        if (gcsName.equalsIgnoreCase(WGS_84)) {
            gcs = createWGS_84();
        }        
        
        javax.media.jai.ParameterList params = 
                csf.createProjectionParameterList(classification);
        String[] pns = params.getParameterListDescriptor().getParamNames();
        String str = "\t"+pns[0];
        for (int i=1;i<pns.length;i++) {
            str = str+"\n\t"+pns[i];
        }
        logger.info("Parameter list:"+"\n"+str);
        Ellipsoid ellipse = gcs.getHorizontalDatum().getEllipsoid();
        params.setParameter("semi_major",ellipse.getSemiMajorAxis());
        params.setParameter("semi_minor",ellipse.getSemiMinorAxis());
        if (region.equals(REGION_ALASKA)) {
            params.setParameter("central_meridian",-154.0);
            params.setParameter("latitude_of_origin",50.0);
            params.setParameter("standard_parallel_1",55.0);
            params.setParameter("standard_parallel_2",65.0);
        } else
        if (region.equals(REGION_ROK)) {
            params.setParameter("central_meridian",125.0);
            params.setParameter("latitude_of_origin",30.0);
            params.setParameter("standard_parallel_1",25.0);
            params.setParameter("standard_parallel_2",35.0);
        }
        params.setParameter("false_easting",0.0);
        params.setParameter("false_northing",0.0);
        Projection projection = csf.createProjection("Albers"+region,classification,params);
        Unit linearUnit = Unit.METRE;
        prjCS = csf.createProjectedCoordinateSystem(
                "Albers_"+gcsName+"_"+region,
                gcs,
                projection,
                linearUnit,
                AxisInfo.X,
                AxisInfo.Y);
        
        logger.info("prjCS:\n"+prjCS.toWKT());
        return prjCS;
    }
    
    public static CoordinateSystem createCoordinateSystem(String epsg) 
                                        throws SQLException, FactoryException {
        CoordinateSystemAuthorityFactory fac = 
                CoordinateSystemEPSGFactory.getDefault();
        CoordinateSystem cs = fac.createCoordinateSystem(epsg);
        logger.info(cs.toWKT());
        fac.dispose();
        return cs;
    }
    
    public static String[] getAvaialbleTransforms() {
        MathTransformFactory mtf  = MathTransformFactory.getDefault();
        String[] tfs = mtf.getAvailableTransforms();
        return tfs;
    }
    
    public static CoordinateTransformation getTransformation(
            CoordinateSystem sourceCS, CoordinateSystem targetCS) 
            throws CannotCreateTransformException {
        CoordinateTransformationFactory ctf = 
                CoordinateTransformationFactory.getDefault();
        CoordinateTransformation ct = 
                ctf.createFromCoordinateSystems(sourceCS,targetCS);
        return ct;
    }
}
