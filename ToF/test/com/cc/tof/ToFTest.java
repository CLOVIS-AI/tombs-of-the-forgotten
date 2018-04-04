/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.tof;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author a.chourouq
 */
public class ToFTest {
    
    public ToFTest() {
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
     * Test of commands method, of class ToF.
     */
    @Test
    public void testCommands() {
        System.out.println("Testing commands...");
        Action test1 = new Action("move", new String[] {"north"});
        Action result1 = ToF.commands("move north");
        assertEquals(test1, result1);
        
        Action test2 = new Action("rest", new String[] {""});
        Action result2 = ToF.commands("rest");
        assertEquals(test2, result2);

        Action test3 = new Action("drop", new String[] {"2", "sword"});
        Action result3 = ToF.commands("drop 2 swords");
        assertEquals(test3, result3);
    }
    
}
