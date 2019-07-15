/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.HSMs;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openide.util.Exceptions;
import ucar.ma2.InvalidRangeException;
import wts.models.DisMELS.framework.HSMs.HSM_NetCDF;

/**
 *
 * @author william.stockhausen
 */
public class HSMFunction_NetCDFTest {
    
    public static String conn = null;
    public HSMFunction_NetCDF instance = null;
    
    public HSMFunction_NetCDFTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("\nstarting setUpClass");
        System.out.println("os.name = '"+System.getProperty("os.name")+"'");
        System.out.println("os.arch = '"+System.getProperty("os.arch")+"'");
        conn = "C:\\Projects\\EFH-IBMs_Shotwell\\GIS_Data\\HSM_BenthicNurseryHabitat\\HSM.nc";
        System.out.println("'"+System.getProperty("os.name")+"' = 'Mac OS X'?: "+(System.getProperty("os.name").equals("Mac OS X")));
        if (System.getProperty("os.name").equals("Mac OS X"))
            conn = "/Users/WilliamStockhausen/Projects/EFH-IBMs_Shotwell/GIS_Data/HSM_BenthicNurseryHabitat/HSM.nc";
        System.out.println("\tconn = '"+conn+"'");
        System.out.println("finished setUpClass");
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        System.out.println("starting setUp");
        instance = new HSMFunction_NetCDF();
        System.out.println("finished setUp");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of clone method, of class HSMFunction_NetCDF.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        HSMFunction_NetCDF instance = new HSMFunction_NetCDF();
        HSMFunction_NetCDF expResult = null;
        HSMFunction_NetCDF result = instance.clone();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameterValue method, of class HSMFunction_NetCDF.
     */
    @Test
    public void testSetParameterValue() {
        System.out.println("setParameterValue");
        HSMFunction_NetCDF instance = new HSMFunction_NetCDF();
        boolean result = instance.setParameterValue(HSMFunction_NetCDF.PARAM_fileName, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of calculate method, of class HSMFunction_NetCDF.
     */
    @Test
    public void testCalculate() {
        System.out.println("testing calculate()");
        HSMFunction_NetCDF instance = new HSMFunction_NetCdF();
        instance.setParameterValue(HSM_NetCDF., this)
        if (!instance.isConnected()) instance.setConnectionString(conn);
        double xll = instance.xll;
        double yll = instance.yll;
        double csz = instance.csz;
        System.out.println("\txll, yll, csz = "+xll+", "+yll+", "+csz);
        double value;
        int[] i =          new int[]{  0,      2000,      3000,          0, 4000, 5000, 5500, 5900, 6000, 7000, 8000, 10000,19400, 19408, 19409, 19410};
        int[] j =          new int[]{  0,      1000,      1000,       3000, 4000, 5000, 5500, 5900, 6000, 7000, 8000,  7000, 8000,  8182,  8183,  8184};
        double[] vals = new double[]{0.0, 0.2970694, 0.3329274, 0.05058163,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,   0.0,  0.0,   0.0,   0.0,   0.0};
        for (int k=0;k<i.length;k++){
            double[] pos = new double[]{xll+i[k]*csz,yll+j[k]*csz};
            System.out.println("i,j   = {"+i[k]+", "+j[k]+"}");
            System.out.println("\tpos[] = {"+pos[0]+", "+pos[1]+"}");
            try {
                value = (Double)instance.calcValue(pos);
                System.out.println("\t\tvalue = "+value+"; exp. value = "+vals[k]+"; diff = "+(value-vals[k]));
                String msg = "Extracted value ("+value+") does not match extracted value("+vals[k]+") for\n"+
                             "i, j = {"+i[k]+", "+j[k]+"}";
                assertEquals(msg,vals[k],value,0.0001);
            } catch (InvalidRangeException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        assertTrue(true);
    }
    
}
