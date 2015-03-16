/*
 * CRSCreator.java
 *
 * Created on June 22, 2006, 2:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.GIS.crs;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCartesianCS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.factory.FactoryGroup;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.OperationNotFoundException;
import wts.GIS.crs.cs.GeodeticLonLat;
import wts.GIS.crs.datums.NAD83;
import wts.GIS.crs.ellipsoids.GRS_1980;


/**
 *
 * @author William Stockhausen
 */
public class CRSCreator {
    
    
    /** Creates a new instance of CRSCreator */
    private CRSCreator() {
    }
    
    public static GeographicCRS getWGS84() {
        GeographicCRS crs = DefaultGeographicCRS.WGS84;
        return crs;
    }
    
    public static GeographicCRS getNAD83() throws FactoryException {
        GeographicCRS crs = null;
        CRSFactory crsFac = FactoryFinder.getCRSFactory();
                
        Map map = new HashMap();
        
        //Construct geodetic datum
        GeodeticDatum geodeticDatum = new NAD83();
        //Construct ellipsoidal coordinate system
        EllipsoidalCS ellipsoidalCS = new GeodeticLonLat();
        
        //Construct CRS
        map.clear();
        map.put("name","NAD 83");
        map.put("authority","9999");
        crs = crsFac.createGeographicCRS(map,geodeticDatum,ellipsoidalCS);
        
        return crs;
    }
    
    public static ProjectedCRS getAlbers(GeographicCRS gCRS) throws FactoryException {
        ProjectedCRS crs = null;
        CoordinateOperationFactory ctFac = FactoryFinder.getCoordinateOperationFactory();
        CRSFactory crsFac = FactoryFinder.getCRSFactory();
        MathTransformFactory mtFac = FactoryFinder.getMathTransformFactory();
        
        OperationMethod opMethod = null;
        
        MathTransform mathTransform = null;
        ParameterValueGroup pvg = mtFac.getDefaultParameters("Albers_Conic_Equal_Area");
        Object[] pns = pvg.getDescriptor().getIdentifiers().toArray();
        String str = "\t"+pns[0].toString();
        for (int i=1;i<pns.length;i++) {
            str = str+"\n\t"+pns[i].toString();
        }
        System.out.println("Parameter list:"+"\n"+str);
        Ellipsoid ellipse = ((GeodeticDatum) gCRS.getDatum()).getEllipsoid();
        pvg.parameter("semi_major").setValue(ellipse.getSemiMajorAxis());
        pvg.parameter("semi_minor").setValue(ellipse.getSemiMinorAxis());
        pvg.parameter("central_meridian").setValue(-154.0);
        pvg.parameter("latitude_of_origin").setValue(50.0);
        pvg.parameter("standard_parallel_1").setValue(55.0);
        pvg.parameter("standard_parallel_2").setValue(65.0);
        pvg.parameter("false_easting").setValue(0.0);
        pvg.parameter("false_northing").setValue(0.0);
        mathTransform = mtFac.createParameterizedTransform(pvg);
        
        CartesianCS cartesianCS = DefaultCartesianCS.GENERIC_2D;
        
        Map map = Collections.singletonMap("name","Albers"+gCRS.getName());
//      crs =  crsFac.createProjectedCRS(map,
//                                  opMethod,
//                                  gCRS,
//                                  mathTransform,
//                                  cartesianCS);
        FactoryGroup fg = new FactoryGroup();
        crs = fg.createProjectedCRS(map,gCRS,opMethod,pvg,cartesianCS);
        return crs;
    }

    public static MathTransform getCRSTransform(GeographicCRS gCRS, ProjectedCRS pCRS) 
                            throws OperationNotFoundException, FactoryException {
        CoordinateOperationFactory fac = FactoryFinder.getCoordinateOperationFactory();
        CoordinateOperation op = fac.createOperation(gCRS,pCRS);
        MathTransform mt = op.getMathTransform();
        return mt;
    }
}
