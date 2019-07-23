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
        String hsm = "HSM.10km.nc";
        hsm = "HSMarcmap.10km.nc";
        System.out.println("\nstarting setUpClass");
        System.out.println("os.name = '"+System.getProperty("os.name")+"'");
        System.out.println("os.arch = '"+System.getProperty("os.arch")+"'");
        String conn = "C:\\Projects\\EFH-IBMs_Shotwell\\GIS_Data\\HSM_BenthicNurseryHabitat\\"+hsm;
        String g2df = "C:\\Projects\\ROMS_Data\\CGOA\\CGOA_grid_5.nc";
        System.out.println("'"+System.getProperty("os.name")+"' = 'Mac OS X'?: "+(System.getProperty("os.name").equals("Mac OS X")));
        if (System.getProperty("os.name").equals("Mac OS X")){
            conn = "/Users/WilliamStockhausen/Projects/EFH-IBMs_Shotwell/GIS_Data/HSM_BenthicNurseryHabitat/"+hsm;
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
        
        HSMFunction_NetCDF.debug = true;
        System.out.println("finished setUpClass");
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("starting tearDownClass");
        HSMFunction_NetCDF.debug = false;
        System.out.println("finished tearDownClass");
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
        double[] v   = new double[]{           10,            0,            0,  0.209374607,  0.231614202,            0,  0.01291544,  0.002007595,  0.003730938,  0.134112999,  0.288781911,           0,             0,           40};
        double[] x   = new double[]{ -559050.9788, -549050.9788,  210949.0212,  220949.0212,  230949.0212,  240949.0212, 360949.0212,  370949.0212,  380949.0212,  390949.0212,  400949.0212,  1350949.021,  1360949.021,  1370949.021};
        double[] y   = new double[]{  1271078.225,  1271078.225,  1271078.225,  1271078.225,  1271078.225,  1271078.225, 1221078.225,  1221078.225,  1221078.225,  1221078.225,  1221078.225,  471078.2249,  471078.2249,  471078.2249};
        double[] lon = new double[] {-164.4224016, -164.2389377, -150.0393661, -149.8520899, -149.6648797, -149.4777385, -147.332203, -147.1487639, -146.9654296, -146.7822029, -146.5990864, -133.9283754, -133.7887767, -133.6493621};
        double[] lat = new double[]{  61.02668528,  61.04053722,  61.36345491,  61.35799992,  61.35229335,  61.34633531, 60.81052634,  60.80144189,  60.79211187,  60.78253648,  60.77271594,  52.40057123,  52.37362109,  52.34648864};
        double value; String msg;
        for (int k=0;k<lon.length;k++){
            double[] posLL = new double[]{lon[k],lat[k]};
            
            //test transform to projected CRS
            System.out.println("\tTest transform to projected coordinate system");
            System.out.println("\t--posLL[] = {"+lon[k]+", "+lat[k]+"} [lon,lat]");
            
            double[] posXY = AlbersNAD83.transformGtoP(posLL);
            System.out.println("\tposXY[] = {"+posXY[0]+", "+posXY[1]+"}");
            System.out.println("\t    x,y = {"+x[k]+", "+y[k]+"}");
            System.out.println("\t  dx,dy = {"+(x[k]-posXY[0])+", "+(y[k]-posXY[1])+"}");
            msg = "Expected x position ("+x[k]+") does not match transformed value("+posXY[0]+") within 1 m.";
            assertEquals(msg,x[k],posXY[0],1.0);
            msg = "Expected y position ("+y[k]+") does not match transformed value("+posXY[1]+") within 1 m";
            assertEquals(msg,y[k],posXY[1],1.0);
            
            //test value retrieval
            value = instance.calculate(posLL);
            System.out.println("\t\tvalue = "+value+"; exp. value = "+v[k]+"; diff = "+(value-v[k]));
            msg = "Extracted value ("+value+") does not match extracted value("+v[k]+") for\n"+
                    "x, y = {"+lon[k]+", "+lat[k]+"}";
            assertEquals(msg,v[k],value,0.00001);
        }
        System.out.println("finished testing HSMFunction_NetCDF.calculate(double[]) with LLs");
        //assertTrue(true);
    }
}
