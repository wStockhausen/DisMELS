/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMFunctions.HSMs;

import java.awt.Component;
import java.awt.Dialog;
import javax.swing.JDialog;
import javax.swing.JFrame;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionCustomizer;

/**
 *
 * @author william.stockhausen
 */
public class HSMFunction_NetCDFCustomizerTest {
    
    private static HSMFunction_NetCDF hsm;
    private static IBMFunctionCustomizer fc;
    private static JDialog ed;
    
    public HSMFunction_NetCDFCustomizerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        hsm = new HSMFunction_NetCDF();
        ed = new JDialog();
        fc = new IBMFunctionCustomizer();
        ed.add(fc);
        ed.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
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
     * Test of setObject method, of class HSMFunction_NetCDFCustomizer.
     */
    @Test
    public void testSetObject() throws Exception {
        System.out.println("running testSetObject");
        Object bean = new HSMFunction_NetCDF();
        fc.setObject(bean);
        ed.pack();
        javax.swing.SwingUtilities.invokeAndWait(new Runnable () {
            @Override
            public void run () {
              ed.setVisible(true);
              ed.repaint();
              System.out.println("ed set visible");
            }
        });
        
        // TODO review the generated test code and remove the default call to fail.
        System.out.println("finished running testSetObject");
        assert(true);
    }
    
}
