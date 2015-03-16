/*
 * FactoryFinder.java
 *
 * Created on June 22, 2006, 3:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.GIS.crs;

import org.opengis.referencing.crs.CRSFactory;
import org.opengis.referencing.cs.CSFactory;
import org.opengis.referencing.datum.DatumFactory;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransformFactory;

/**
 *
 * @author William Stockhausen
 */
public class FactoryFinder {
    

    private static CoordinateOperationFactory coordOpFactory;
    private static CRSFactory crsFactory;
    private static CSFactory csFactory;
    private static DatumFactory datumFactory;
    private static MathTransformFactory mathTransformFactory;
    
    /** Creates a new instance of FactoryFinder */
    private FactoryFinder() {
    }
    
    public static CRSFactory getCRSFactory() {
        if (crsFactory==null) 
            crsFactory = org.geotools.referencing.FactoryFinder.getCRSFactory(null);
        return crsFactory;
    }
    
    public static CSFactory getCSFactory() {
        if (csFactory==null) 
            csFactory = org.geotools.referencing.FactoryFinder.getCSFactory(null);
        return csFactory;
    }
    
    public static DatumFactory getDatumFactory() {
        if (datumFactory==null) 
            datumFactory = org.geotools.referencing.FactoryFinder.getDatumFactory(null);
        return datumFactory;
    }
    
    public static CoordinateOperationFactory getCoordinateOperationFactory() {
        if (coordOpFactory==null) 
           coordOpFactory = org.geotools.referencing.FactoryFinder.getCoordinateOperationFactory(null);
        return coordOpFactory;
    }
    
    public static MathTransformFactory getMathTransformFactory() {
        if (mathTransformFactory==null) 
           mathTransformFactory = org.geotools.referencing.FactoryFinder.getMathTransformFactory(null);
        return mathTransformFactory;
    }
}
