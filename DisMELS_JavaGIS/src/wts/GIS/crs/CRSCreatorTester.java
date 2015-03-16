/*
 * CRSCreatorTester.java
 *
 * Created on June 15, 2006, 5:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.GIS.crs;

import org.geotools.geometry.GeneralDirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.spatialschema.geometry.DirectPosition;
import org.opengis.spatialschema.geometry.MismatchedDimensionException;

/**
 *
 * @author William Stockhausen
 */
public class CRSCreatorTester {
    
    /**
     * Creates a new instance of CRSCreatorTester
     */
    public CRSCreatorTester() {
    }
    
    public void runTests() {
        try {
            GeographicCRS gCRS = null;
            gCRS = CRSCreator.getWGS84();
            System.out.println(gCRS.toWKT());
            
            gCRS = CRSCreator.getNAD83();
            System.out.println("NAD83 successfully created!");
            
            ProjectedCRS pCRS = CRSCreator.getAlbers(gCRS);
            System.out.println("Albers successfully created!");
            
            MathTransform mt = CRSCreator.getCRSTransform(gCRS,pCRS);
            DirectPosition pt = new GeneralDirectPosition(-160,55);
            try {
                DirectPosition ptp = mt.transform(pt,null);
                System.out.println("pt = "+pt);
                System.out.println("ptp = "+ptp);
                System.out.println("inv pt = "+mt.inverse().transform(ptp,null));
            } catch (MismatchedDimensionException ex) {
                ex.printStackTrace();
            } catch (TransformException ex) {
                ex.printStackTrace();
            }
            
//            System.out.println(crs.toWKT());
//            CRSCreator.createAlbersPCS(CoordinateSystemCreator.NAD_83);
//            CRSCreator.createAlbersPCS(CoordinateSystemCreator.WGS_84);
        } catch (FactoryException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CRSCreatorTester tester = 
                new CRSCreatorTester();
        tester.runTests();
    }
    
}
