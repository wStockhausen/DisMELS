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
import org.junit.Ignore;
import wts.roms.gis.AlbersNAD83;
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
            g2df = "/Users/WilliamStockhausen/Projects/ROMS/CGOA/CGOA_grid_5.nc";
        }
        System.out.println("\tsetting conn = '"+conn+"'");
        System.out.println("\tsetting g2df = '"+g2df+"'");
        wts.roms.model.GlobalInfo gi = wts.roms.model.GlobalInfo.getInstance();
        gi.setGridFile(g2df);
        i2d = gi.getInterpolator();
        instance = new HSMFunction_NetCDF();
        instance.setParameterValue(HSMFunction_NetCDF.PARAM_fileName, conn);
        if (instance.hsm==null){
            throw(new Error("could not create hsm"));
        }
        System.out.println("finished setUpClass");
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        //System.out.println("starting setUp");
        //System.out.println("finished setUp");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of calculate method of class HSMFunction_NetCDF with lon, lat inputs.
     * 
     * <pre>
     * Comparisons:
     * 1. Input coordinates are converted from lon, lat to x,y to compare with expected x,y.
     * 2. Input coordinates (lon, lat) are used to calculate HSM at the location and compared with expected value.
     * </pre>
     */
    @Test
    public void testCalculateWithLLs() {
        System.out.println("\n\ntesting HSMFunction_NetCDF.calculate(double[]) with LLs");
        double[] v   = new double[]{ 0.783076012,  0.781201196,  0.779940957,  0.777802259,  0.774819291, 0.771905005,  0.769363391,  0.766910815,  0.767519039};
        double[] x   = new double[]{ 6399.021159,  6499.021159,  6599.021159,  6699.021159,  6799.021159, 6899.021159,  6999.021159,  7099.021159,  7199.021159};
        double[] y   = new double[]{ 824428.2249,  824428.2249,  824428.2249,  824428.2249,  824428.2249, 824428.2249,  824428.2249,  824428.2249,  824428.2249};
        double[] lon = new double[]{-153.8932141, -153.8915454, -153.8898766, -153.8882078, -153.886539, -153.8848702, -153.8832014, -153.8815327, -153.8798639};
        double[] lat = new double[]{ 57.42426562,  57.42426417,  57.42426269,   57.4242612, 57.42425968,  57.42425814,  57.42425657,  57.42425499,  57.42425338};
        double value; String msg;
        for (int k=0;k<lon.length;k++){
            double[] posLL = new double[]{lon[k],lat[k]};
            System.out.println("\tposLL[] = {"+lon[k]+", "+lat[k]+"} [lon,lat]");
            double[] posXY = AlbersNAD83.transformGtoP(posLL);
            System.out.println("\tposXY[] = {"+posXY[0]+", "+posXY[1]+"}");
            System.out.println("\t    x,y = {"+x[k]+", "+y[k]+"}");
            System.out.println("\t  dx,dy = {"+(x[k]-posXY[0])+", "+(y[k]-posXY[1])+"}");
            msg = "Expected x position ("+x[k]+") does not match transformed value("+posXY[0]+")";
            assertEquals(msg,x[k],posXY[0],0.00001);
            msg = "Expected y position ("+y[k]+") does not match transformed value("+posXY[1]+")";
            assertEquals(msg,y[k],posXY[0],0.00001);
            value = instance.calculate(posLL);
            System.out.println("\t\tvalue = "+value+"; exp. value = "+v[k]+"; diff = "+(value-v[k]));
            msg = "Extracted value ("+value+") does not match extracted value("+v[k]+") for\n"+
                    "x, y = {"+lon[k]+", "+lat[k]+"}";
            assertEquals(msg,v[k],value,0.00001);
        }
        System.out.println("finished testing HSMFunction_NetCDF.calculate(ArrayList) with LL->IJ");
        //assertTrue(true);
    }
}
