/*
 * NAD83.java
 *
 * Created on June 22, 2006, 4:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.GIS.crs.datums;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.extent.Extent;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.util.InternationalString;
import wts.GIS.crs.FactoryFinder;
import wts.GIS.crs.ellipsoids.GRS_1980;
import wts.GIS.crs.primemeridians.Greenwich;

/**
 *
 * @author William Stockhausen
 */
public class NAD83 implements GeodeticDatum {
    
    public static final String name = "North American Datum 1983";
    
    private GeodeticDatum d;
    
    /** Creates a new instance of NAD83 */
    public NAD83() throws FactoryException {
        Ellipsoid e = new GRS_1980();
        PrimeMeridian pm = new Greenwich();
        final BursaWolfParameters toWGS84 = new BursaWolfParameters(DefaultGeodeticDatum.WGS84);
        //Bursa-Wolf parameter values for NAD83(HARN) to WGS84, 
        //EPSG coordinate transformation code 1901
        toWGS84.dx = -0.991; //meters
        toWGS84.dy = 1.9072;
        toWGS84.dz = 0.5129;
        toWGS84.ex = -1.25033e-7;//radians
        toWGS84.ey = -4.6785e-8;
        toWGS84.ez = -5.6529e-8;
        toWGS84.ppm = 0.0;
        System.out.println(toWGS84.toWKT());
        HashMap map = new HashMap();
        map.put("name",name);
        map.put(DefaultGeodeticDatum.BURSA_WOLF_KEY,toWGS84);
        DatumFactory dFac = FactoryFinder.getDatumFactory();
        d = dFac.createGeodeticDatum(map,e,pm);
//        System.out.println(toWKT());
    }

    public Ellipsoid getEllipsoid() {
        return d.getEllipsoid();
    }

    public PrimeMeridian getPrimeMeridian() {
        return d.getPrimeMeridian();
    }

    public InternationalString getAnchorPoint() {
        return d.getAnchorPoint();
    }

    public Date getRealizationEpoch() {
        return d.getRealizationEpoch();
    }

    public Extent getValidArea() {
        return d.getValidArea();
    }

    public InternationalString getScope() {
        return d.getScope();
    }

    public Identifier getName() {
        return d.getName();
    }

    public Collection getAlias() {
        return d.getAlias();
    }

    public Set getIdentifiers() {
        return d.getIdentifiers();
    }

    public InternationalString getRemarks() {
        return d.getRemarks();
    }

    public String toString() {
        return d.toString();
    }

    public String toWKT() throws UnsupportedOperationException {
        return d.toWKT();
    }
    
}
