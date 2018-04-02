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
     * Test of getInput method, of class ToF.
     */
    @Test
    public void testGetInput() {
        System.out.println("getInput");
        String expResult = "";
        String result = ToF.getInput();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
