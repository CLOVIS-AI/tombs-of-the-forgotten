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
     * Test of main method, of class ToF.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        ToF.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of gameLoop method, of class ToF.
     */
    @Test
    public void testGameLoop() {
        System.out.println("gameLoop");
        ToF.gameLoop();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of gameTick method, of class ToF.
     */
    @Test
    public void testGameTick() {
        System.out.println("gameTick");
        ToF.gameTick();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of action method, of class ToF.
     */
    @Test
    public void testAction() {
        System.out.println("move north");
        ToF instance = new ToF();
        Action expResult = new Action("move", new String[] {"north"});
        Action result = instance.action();
        assertEquals(expResult, result);
    }
    
}
