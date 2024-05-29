/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wtstockhausen.utils;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class FieldFinderTest {
    
    public FieldFinderTest() {
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
     * Test of getField method, of class FieldFinder.
     */
    @Test
    public void testGetField() {
        try {
            Logger.getLogger(FieldFinderTest.class.getName()).info("testing FieldFinder.getField");
            Class cls = com.wtstockhausen.table.DataTableModel.class;
            String name = "TYPE_FLOAT";
            Field result = FieldFinder.getField(cls, name);
            int expRes = com.wtstockhausen.table.DataTableModel.TYPE_FLOAT;
            int res = result.getInt(null);
            assertEquals(expRes, res);
            
            name = "columnIdentifiers";
            result = FieldFinder.getField(cls, name);
            assertNotNull(result);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(FieldFinderTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(FieldFinderTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
