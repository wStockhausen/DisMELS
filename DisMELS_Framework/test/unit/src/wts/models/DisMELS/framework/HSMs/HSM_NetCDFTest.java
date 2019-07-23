/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.HSMs;

import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.openide.util.Exceptions;
import ucar.ma2.InvalidRangeException;
import wts.roms.model.Interpolator2D;

/**
 *
 * @author william.stockhausen
 */
public class HSM_NetCDFTest {
    
    public static String conn = null;
    public static Interpolator2D i2d = null;
    public HSM_NetCDF instance = null;
    
    public HSM_NetCDFTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("\nstarting setUpClass");
        System.out.println("os.name = '"+System.getProperty("os.name")+"'");
        System.out.println("os.arch = '"+System.getProperty("os.arch")+"'");
        conn = "C:\\Projects\\EFH-IBMs_Shotwell\\GIS_Data\\HSM_BenthicNurseryHabitat\\HSM.10km.nc";
        String g2df = "C:\\Projects\\ROMS_Data\\CGOA\\CGOA_grid_5.nc";
        System.out.println("'"+System.getProperty("os.name")+"' = 'Mac OS X'?: "+(System.getProperty("os.name").equals("Mac OS X")));
        if (System.getProperty("os.name").equals("Mac OS X")){
            conn = "/Users/WilliamStockhausen/Projects/EFH-IBMs_Shotwell/GIS_Data/HSM_BenthicNurseryHabitat/HSM.10km.nc";
            g2df = "/Users/WilliamStockhausen/Projects/ROMS/CGOA/CGOA_grid_5.nc";
        }
        System.out.println("\tsetting conn = '"+conn+"'");
        System.out.println("\tsetting g2df = '"+g2df+"'");
        wts.roms.model.GlobalInfo gi = wts.roms.model.GlobalInfo.getInstance();
        gi.setGridFile(g2df);
        i2d = gi.getInterpolator();
        
        HSM_NetCDF.debug = true;
        System.out.println("finished setUpClass");
    }
    
    @AfterClass
    public static void tearDownClass() {
        HSM_NetCDF.debug = false;
        System.out.println("starting tearDownClass");
    }
    
    @Before
    public void setUp() {
        System.out.println("starting setUp");
        instance = new HSM_NetCDF();
        System.out.println("finished setUp");
    }
    
    @After
    public void tearDown() {
        try {
            System.out.println("starting tearDown");
            if (instance!=null) instance.ds.close();
            System.out.println("finished tearDown");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Test of getConnectionString method, of class HSM_NetCDF.
     */
    @Ignore("sure this works")
    public void testGetConnectionString() {
        System.out.println("getConnectionString");
        String expResult = "";
        String result = instance.getConnectionString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setConnectionString method, of class HSM_NetCDF.
     */
    @Test
    public void testSetConnectionString() {
        System.out.println("starting setConnectionString(conn)");
        try {
            boolean expResult = true;
            boolean result = instance.setConnectionString(conn);
            assertEquals("Could not set connection string \n\t'"+conn+"'",expResult, result);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            fail("Could not set connection string \n\t'"+conn+"'");
        } catch (InvalidRangeException ex) {
            Exceptions.printStackTrace(ex);
            fail("Invalid range extenstion!");
        }
        System.out.println("finished setConnectionString(conn)");
    }

    /**
     * Test of HSM_NetCDF.calcValue(double[]).
     * 
     * @throws java.io.IOException
     */
    @Test
    public void testCalcValue_DoubleVector() throws IOException, InvalidRangeException {
        System.out.println("\n\ntesting calcValue(double[])");
        if (!instance.isConnected()) instance.setConnectionString(conn);
        double value;
        double[] v   = new double[]{           10,            0,            0,  0.209374607,  0.231614202,            0,  0.01291544,  0.002007595,  0.003730938,  0.134112999,  0.288781911,           0,             0,           40};
        double[] x   = new double[]{ -559050.9788, -549050.9788,  210949.0212,  220949.0212,  230949.0212,  240949.0212, 360949.0212,  370949.0212,  380949.0212,  390949.0212,  400949.0212,  1350949.021,  1360949.021,  1370949.021};
        double[] y   = new double[]{  1271078.225,  1271078.225,  1271078.225,  1271078.225,  1271078.225,  1271078.225, 1221078.225,  1221078.225,  1221078.225,  1221078.225,  1221078.225,  471078.2249,  471078.2249,  471078.2249};
        double[] lon = new double[] {-164.4224016, -164.2389377, -150.0393661, -149.8520899, -149.6648797, -149.4777385, -147.332203, -147.1487639, -146.9654296, -146.7822029, -146.5990864, -133.9283754, -133.7887767, -133.6493621};
        double[] lat = new double[]{  61.02668528,  61.04053722,  61.36345491,  61.35799992,  61.35229335,  61.34633531, 60.81052634,  60.80144189,  60.79211187,  60.78253648,  60.77271594,  52.40057123,  52.37362109,  52.34648864};
        for (int k=0;k<v.length;k++){
            System.out.println("\tposXY[] = {"+x[k]+", "+y[k]+"}");
            System.out.println("\tposLL[] = {"+lon[k]+", "+lat[k]+"}");
            double[] posLL = new double[]{lon[k],lat[k]};
            try {
                value = (Double)instance.calcValue(posLL);
                System.out.println("\t\tvalue = "+value+"; exp. value = "+v[k]+"; diff = "+(value-v[k]));
                String msg = "Extracted value ("+value+") does not match extracted value("+v[k]+") for\n"+
                             "k = "+k;
                assertEquals(msg,v[k],value,0.0001);
            } catch (InvalidRangeException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        System.out.println("finished testing calcValue(double[])\n\n");
    }
}
