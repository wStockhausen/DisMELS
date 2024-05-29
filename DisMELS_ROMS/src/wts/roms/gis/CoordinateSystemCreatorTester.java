/*
 * CoordinateSystemCreatorTester.java
 *
 * Created on June 15, 2006, 5:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.roms.gis;

import org.geotools.cs.CoordinateSystem;
import org.geotools.cs.GeographicCoordinateSystem;
import org.geotools.cs.ProjectedCoordinateSystem;
import org.geotools.ct.MathTransform;
import org.opengis.referencing.FactoryException;

/**
 *
 * @author William Stockhausen
 */
public class CoordinateSystemCreatorTester {
    
    /** Creates a new instance of CoordinateSystemCreatorTester */
    public CoordinateSystemCreatorTester() {
    }
    
    public void runTests() {
        try {
            CSCreator.createAlbersPCS(CSCreator.WGS_84);
            GeographicCoordinateSystem nad83 = 
                    CSCreator.createNAD_83();
            ProjectedCoordinateSystem nad83albers = 
                    CSCreator.createAlbersPCS(CSCreator.NAD_83);
            MathTransform mt = 
                    CSCreator.getTransformation(nad83,nad83albers).getMathTransform();
            double[] srcPts = new double[] {-160,55};
            double[] dstPts = new double[2];
            mt.transform(srcPts,0,dstPts,0,1);
            System.out.println("Source pts "+srcPts[0]+","+srcPts[1]);
            System.out.println("Dest pts   "+dstPts[0]+","+dstPts[1]);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        CoordinateSystemCreatorTester tester = 
                new CoordinateSystemCreatorTester();
        tester.runTests();
    }
    
}
