/*
 * GeodeticLonLat.java
 *
 * Created on June 22, 2006, 4:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.GIS.crs.cs;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.util.InternationalString;
import wts.GIS.crs.FactoryFinder;

/**
 *
 * @author William Stockhausen
 */
public class GeodeticLonLat implements EllipsoidalCS {
    
    public static final String name = "<long>, <lat>";
    
    EllipsoidalCS cs;
    
    /** Creates a new instance of GeodeticLonLat */
    public GeodeticLonLat() throws FactoryException {
        CSFactory csFac = FactoryFinder.getCSFactory();
        Map map = Collections.singletonMap("name","<long>, <lat>");
        CoordinateSystemAxis lon = DefaultCoordinateSystemAxis.GEODETIC_LONGITUDE;
        CoordinateSystemAxis lat = DefaultCoordinateSystemAxis.GEODETIC_LATITUDE;
        cs = csFac.createEllipsoidalCS(map,lon,lat);
//        System.out.println(toWKT());
    }

    public int getDimension() {
        return cs.getDimension();
    }

    public CoordinateSystemAxis getAxis(int i) throws IndexOutOfBoundsException {
        return cs.getAxis(i);
    }

    public Identifier getName() {
        return cs.getName();
    }

    public Collection getAlias() {
        return cs.getAlias();
    }

    public Set getIdentifiers() {
        return cs.getIdentifiers();
    }

    public InternationalString getRemarks() {
        return cs.getRemarks();
    }

    public String toWKT() throws UnsupportedOperationException {
        return cs.toWKT();
    }
    
}
