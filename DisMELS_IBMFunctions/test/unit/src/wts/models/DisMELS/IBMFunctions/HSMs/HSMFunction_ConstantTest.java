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

/**
 *
 * @author william.stockhausen
 */
public class HSMFunction_ConstantTest {
    
    public HSMFunction_ConstantTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setParameterValue method, of class HSMFunction_Constant.
     */
    @Test
    public void testSetParameterValue() {
        System.out.println("setParameterValue");
        double value = 99;
        HSMFunction_Constant instance = new HSMFunction_Constant();
        boolean expResult = true;
        boolean result = instance.setParameterValue(HSMFunction_Constant.PARAM_value, value);
        assertEquals(expResult, result);
    }

    /**
     * Test of calculate method, of class HSMFunction_Constant.
     */
    @Test
    public void testCalculate() {
        System.out.println("starting to test HSMFunction_Constant.calculate(pos)");
        Object vars = null;
        HSMFunction_Constant instance = new HSMFunction_Constant();
        instance.setParameterValue(HSMFunction_Constant.PARAM_value, 99.0);
        double[] expResult = new double[]{99.0};
        double[] result = instance.calculate(vars);
        assertArrayEquals(expResult, result,0.001);
        System.out.println("finished testing HSMFunction_Constant.calculate(pos)");
    }
    
}
