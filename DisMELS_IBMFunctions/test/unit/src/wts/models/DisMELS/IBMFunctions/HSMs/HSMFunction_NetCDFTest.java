/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.HSMs;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import wts.models.DisMELS.framework.HSMs.HSM_NetCDF;
import wts.roms.model.Interpolator2D;

/**
 *
 * @author william.stockhausen
 */
public class HSMFunction_NetCDFTest {
    
    public static HSMFunction_NetCDF instance = null;
    public static Interpolator2D i2d = null;
    
    public HSMFunction_NetCDFTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        System.out.println("\nstarting setUpClass");
        System.out.println("os.name = '"+System.getProperty("os.name")+"'");
        System.out.println("os.arch = '"+System.getProperty("os.arch")+"'");
        String conn = "C:\\Projects\\EFH-IBMs_Shotwell\\GIS_Data\\HSM_BenthicNurseryHabitat\\HSM.nc";
        String g2df = "C:\\Projects\\ROMS_Data\\CGOA\\CGOA_grid_5.nc";
        System.out.println("'"+System.getProperty("os.name")+"' = 'Mac OS X'?: "+(System.getProperty("os.name").equals("Mac OS X")));
        if (System.getProperty("os.name").equals("Mac OS X")){
            conn = "/Users/WilliamStockhausen/Projects/EFH-IBMs_Shotwell/GIS_Data/HSM_BenthicNurseryHabitat/HSM.nc";
            g2df = "??";
        }
        System.out.println("\tsetting conn = '"+conn+"'");
        System.out.println("\tsetting g2df = '"+g2df+"'");
        wts.roms.model.GlobalInfo gi = wts.roms.model.GlobalInfo.getInstance();
        gi.setGridFile(g2df);
        i2d = gi.getInterpolator();
        instance = new HSMFunction_NetCDF();
        instance.setParameterValue(HSMFunction_NetCDF.PARAM_fileName, conn);
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
     * Test of calculate method of class HSMFunction_NetCDF.
     */
    @Test
    public void testCalculateWithDoubleArray() {
        System.out.println("testing HSMFunction_NetCDF.calculate(double[])");
        HSM_NetCDF hsm = instance.hsm;
        double xll = hsm.getXLL();
        double yll = hsm.getYLL();
        double csz = hsm.getCellSize();
        System.out.println("\txll, yll, csz = "+xll+", "+yll+", "+csz);
        double value = -1000;
        int[] i =          new int[]{  0,      2000,      3000,          0, 4000, 5000, 5500, 5900, 6000, 7000, 8000, 10000,19400, 19408, 19409, 19410};
        int[] j =          new int[]{  0,      1000,      1000,       3000, 4000, 5000, 5500, 5900, 6000, 7000, 8000,  7000, 8000,  8182,  8183,  8184};
        double[] vals = new double[]{0.0, 0.2970694, 0.3329274, 0.05058163,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,   0.0,  0.0,   0.0,   0.0,   0.0};
        for (int k=0;k<i.length;k++){
            double[] pos = new double[]{xll+i[k]*csz,yll+j[k]*csz};
            System.out.println("i,j   = {"+i[k]+", "+j[k]+"}");
            System.out.println("\tpos[] = {"+pos[0]+", "+pos[1]+"}");
            value = ((double[])instance.calculate(pos))[0];
            System.out.println("\t\tvalue = "+value+"; exp. value = "+vals[k]+"; diff = "+(value-vals[k]));
            String msg = "Extracted value ("+value+") does not match extracted value("+vals[k]+") for\n"+
                    "i, j = {"+i[k]+", "+j[k]+"}";
            assertEquals(msg,vals[k],value,0.0001);
        }
        System.out.println("finished testing HSMFunction_NetCDF.calculate(double[])");
    }

    /**
     * Test of calculate method  of class HSMFunction_NetCDF using ArayList input.
     */
    @Test
    public void testCalculateWithArrayList() {
        System.out.println("testing HSMFunction_NetCDF.calculate(ArrayList)");
        HSM_NetCDF hsm = instance.hsm;
        double xll = hsm.getXLL();
        double yll = hsm.getYLL();
        double csz = hsm.getCellSize();
        System.out.println("\txll, yll, csz = "+xll+", "+yll+", "+csz);
        double value = -1000;
        int[] i =          new int[]{  0,      2000,      3000,          0, 4000, 5000, 5500, 5900, 6000, 7000, 8000, 10000,19400, 19408, 19409, 19410};
        int[] j =          new int[]{  0,      1000,      1000,       3000, 4000, 5000, 5500, 5900, 6000, 7000, 8000,  7000, 8000,  8182,  8183,  8184};
        double[] vals = new double[]{0.0, 0.2970694, 0.3329274, 0.05058163,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,   0.0,  0.0,   0.0,   0.0,   0.0};
        for (int k=0;k<i.length;k++){
            double[] pos = new double[]{xll+i[k]*csz,yll+j[k]*csz};
            System.out.println("i,j   = {"+i[k]+", "+j[k]+"}");
            System.out.println("\tpos[]   = {"+pos[0]+", "+pos[1]+"}");
            double[] posIJ = i2d.getGrid().computeIJfromXY(pos[0], pos[1]);
            System.out.println("\tposIJ[] = {"+posIJ[0]+", "+posIJ[1]+"}");
            ArrayList al = new ArrayList(1);
            al.add(posIJ);
            value = ((double[])instance.calculate(al))[0];
            System.out.println("\t\tvalue = "+value+"; exp. value = "+vals[k]+"; diff = "+(value-vals[k]));
            String msg = "Extracted value ("+value+") does not match extracted value("+vals[k]+") for\n"+
                    "i, j = {"+i[k]+", "+j[k]+"}";
            assertEquals(msg,vals[k],value,0.0001);
        }
        //assertTrue(true);
    }

    /**
     * Test of calculate method  of class HSMFunction_NetCDF using ArayList input.
     */
    @Test(expected=Error.class)
    public void testCalculateWithShortDoubleArray() {
        System.out.println("testing HSMFunction_NetCDF.calculate(short double[])");
        HSM_NetCDF hsm = instance.hsm;
        double xll = hsm.getXLL();
        double yll = hsm.getYLL();
        double csz = hsm.getCellSize();
        System.out.println("\txll, yll, csz = "+xll+", "+yll+", "+csz);
        double value = -1000;
        int[] i =          new int[]{  0,      2000,      3000,          0, 4000, 5000, 5500, 5900, 6000, 7000, 8000, 10000,19400, 19408, 19409, 19410};
        int[] j =          new int[]{  0,      1000,      1000,       3000, 4000, 5000, 5500, 5900, 6000, 7000, 8000,  7000, 8000,  8182,  8183,  8184};
        double[] vals = new double[]{0.0, 0.2970694, 0.3329274, 0.05058163,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,  0.0,   0.0,  0.0,   0.0,   0.0,   0.0};
        for (int k=0;k<i.length;k++){
            double[] pos = new double[]{xll+i[k]*csz};//note this position vector is only 1 element--should be at least 2!
            System.out.println("i,j   = {"+i[k]+", "+j[k]+"}");
            System.out.println("\tpos[] = {"+pos[0]+"}");
            value = ((double[])instance.calculate(pos))[0];
            System.out.println("\t\tvalue = "+value+"; exp. value = "+vals[k]+"; diff = "+(value-vals[k]));
            String msg = "Extracted value ("+value+") does not match extracted value("+vals[k]+") for\n"+
                    "i, j = {"+i[k]+", "+j[k]+"}";
            assertEquals(msg,vals[k],value,0.0001);
        }
        //assertTrue(true);
    }
}
