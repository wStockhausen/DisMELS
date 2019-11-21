/*
 * CoordinateSystemCreatorTester.java
 *
 * Created on June 15, 2006, 5:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package wts.GIS.cs;

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
            CoordinateSystemCreator.createAlbersPCS(CoordinateSystemCreator.NAD_83);
            CoordinateSystemCreator.createAlbersPCS(CoordinateSystemCreator.WGS_84);
        } catch (FactoryException ex) {
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
