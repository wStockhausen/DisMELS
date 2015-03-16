/*
 * Greenwich.java
 *
 * Created on June 22, 2006, 3:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.GIS.crs.primemeridians;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.units.NonSI;
import javax.units.Unit;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.datum.PrimeMeridian;
import org.opengis.util.InternationalString;
import wts.GIS.crs.FactoryFinder;

/**
 *
 * @author William Stockhausen
 */
public class IDL implements PrimeMeridian {
    
    public static final String name = "IDL";
    
    private PrimeMeridian pm;
    
    /** Creates a new instance of Greenwich */
    public IDL() throws FactoryException {
        DatumFactory dFac = FactoryFinder.getDatumFactory();
        Map map = Collections.singletonMap("name",name);
        pm = dFac.createPrimeMeridian(map,-180.0,NonSI.DEGREE_ANGLE);
    }

    public double getGreenwichLongitude() {
        return pm.getGreenwichLongitude();
    }

    public Unit getAngularUnit() {
        return pm.getAngularUnit();
    }

    public Identifier getName() {
        return pm.getName();
    }

    public Collection getAlias() {
        return pm.getAlias();
    }

    public Set getIdentifiers() {
        return pm.getIdentifiers();
    }

    public InternationalString getRemarks() {
        return pm.getRemarks();
    }

    public String toWKT() throws UnsupportedOperationException {
        return pm.toWKT();
    }
    
}
