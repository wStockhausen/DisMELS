/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.framework.HSMs;

import java.io.IOException;
import java.util.ArrayList;
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
        conn = "C:\\Projects\\EFH-IBMs_Shotwell\\GIS_Data\\HSM_BenthicNurseryHabitat\\HSM.nc";
        String g2df = "C:\\Projects\\ROMS_Data\\CGOA\\CGOA_grid_5.nc";
        System.out.println("'"+System.getProperty("os.name")+"' = 'Mac OS X'?: "+(System.getProperty("os.name").equals("Mac OS X")));
        if (System.getProperty("os.name").equals("Mac OS X")){
            conn = "/Users/WilliamStockhausen/Projects/EFH-IBMs_Shotwell/GIS_Data/HSM_BenthicNurseryHabitat/HSM.nc";
            g2df = "/Users/WilliamStockhausen/Projects/ROMS/CGOA/CGOA_grid_5.nc";
        }
        System.out.println("\tsetting conn = '"+conn+"'");
        System.out.println("\tsetting g2df = '"+g2df+"'");
        wts.roms.model.GlobalInfo gi = wts.roms.model.GlobalInfo.getInstance();
        gi.setGridFile(g2df);
        i2d = gi.getInterpolator();
        System.out.println("finished setUpClass");
    }
    
    @AfterClass
    public static void tearDownClass() {
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
        }
        System.out.println("finished setConnectionString(conn)");
    }

    /**
     * Test of HSM_NetCDF.calcValue(ArrayList).
     * 
     * @throws java.io.IOException
     */
    @Test
    public void testCalcValue_ArrayList() throws IOException {
        System.out.println("\n\ntesting calcValue(ArrayList)");
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
            double[] posXY = new double[]{xll+i[k]*csz,yll+j[k]*csz};
            System.out.println("i,j   = {"+i[k]+", "+j[k]+"}");
            System.out.println("\tposXY[] = {"+posXY[0]+", "+posXY[1]+"}");
            try {
                ArrayList arrList = new ArrayList();
                arrList.add(posXY);
                value = (Double)instance.calcValue(arrList);
                System.out.println("\t\tvalue = "+value+"; exp. value = "+vals[k]+"; diff = "+(value-vals[k]));
                String msg = "Extracted value ("+value+") does not match extracted value("+vals[k]+") for\n"+
                             "i, j = {"+i[k]+", "+j[k]+"}";
                assertEquals(msg,vals[k],value,0.0001);
            } catch (InvalidRangeException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        System.out.println("finished testing calcValue(ArrayList)\n\n");
    }
    
    /**
     * Test of HSM_NetCDF.calcValue(double[]).
     * 
     * @throws java.io.IOException
     */
    @Test
    public void testCalcValue_DoubleVector() throws IOException {
        System.out.println("\n\ntesting calcValue(double[])");
        if (!instance.isConnected()) instance.setConnectionString(conn);
        double value;
        double[] v   = new double[]{ 0.783076012,  0.781201196,  0.779940957,  0.777802259,  0.774819291, 0.771905005,  0.769363391,  0.766910815,  0.767519039};
        double[] x   = new double[]{ 6399.021159,  6499.021159,  6599.021159,  6699.021159,  6799.021159, 6899.021159,  6999.021159,  7099.021159,  7199.021159};
        double[] y   = new double[]{ 824428.2249,  824428.2249,  824428.2249,  824428.2249,  824428.2249, 824428.2249,  824428.2249,  824428.2249,  824428.2249};
        double[] lon = new double[]{-153.8932141, -153.8915454, -153.8898766, -153.8882078, -153.886539, -153.8848702, -153.8832014, -153.8815327, -153.8798639};
        double[] lat = new double[]{ 57.42426562,  57.42426417,  57.42426269,   57.4242612, 57.42425968,  57.42425814,  57.42425657,  57.42425499,  57.42425338};
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
