/*
 * GRS_1980.java
 *
 * Created on June 22, 2006, 2:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.GIS.crs.ellipsoids;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.units.SI;
import javax.units.Unit;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.util.InternationalString;
import wts.GIS.crs.FactoryFinder;

/**
 *
 * @author William Stockhausen
 */
public class GRS_1980 implements Ellipsoid {

    public static final String name = "GRS_1980";
    public static final double semimajor = 6378137.0; //meters
    public static final double semiminor = 6356752.3141403561; //meters
    
    private Ellipsoid e;
    
    /** Creates a new instance of GRS_1980 */
    public GRS_1980() throws FactoryException {
        DatumFactory dFac = FactoryFinder.getDatumFactory();
        Map map = Collections.singletonMap("name",name);
        e = dFac.createEllipsoid(map,
                                 semimajor,
                                 semiminor,
                                 SI.METER);
//        System.out.println(toWKT());
    }

    public Unit getAxisUnit() {
        return e.getAxisUnit();
    }

    public double getSemiMajorAxis() {
        return e.getSemiMajorAxis();
    }

    public double getSemiMinorAxis() {
        return e.getSemiMinorAxis();
    }

    public double getInverseFlattening() {
        return e.getInverseFlattening();
    }

    public boolean isIvfDefinitive() {
        return e.isIvfDefinitive();
    }

    public boolean isSphere() {
        return e.isSphere();
    }

    public Identifier getName() {
        return e.getName();
    }

    public Collection getAlias() {
        return e.getAlias();
    }

    public Set getIdentifiers() {
        return e.getIdentifiers();
    }

    public InternationalString getRemarks() {
        return e.getRemarks();
    }

    public String toString() {
        return e.toString();
    }
    
    public String toWKT() throws UnsupportedOperationException {
        return e.toWKT();
    }
    
}
