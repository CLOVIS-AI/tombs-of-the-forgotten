/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils;

import static com.cc.utils.Bar.Behavior.ACCEPT;
import java.util.function.Consumer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ivan
 */
public class EventBarTest {
    
    public EventBarTest() {
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
     * Test of setOnFull method, of class EventBar.
     */
    @Test
    public void testSetOnFull() {
        System.out.println("EventBar#setOnFull:none");
        EventBar e2 = new EventBar("test", 0, 5, 3);
        e2.setOnFull(b -> fail("The bar is not full"));
        e2.add(1, ACCEPT);
        
        System.out.println("EventBar#setOnFull:add");
        EventBar e1 = new EventBar("test", 0, 5, 4);
        e1.setOnFull(b -> b.remove(5, ACCEPT));
        e1.add(1, ACCEPT);
        switch (e1.getReal()) {
            case 0:  assertTrue(true); break;
            case 5:  fail("The lambda-exp was not called."); break;
            default: fail("Unknown reason; value=" + e1.getReal()); break;
        }
        
        System.out.println("EventBar#setOnFull:addBonus");
        EventBar e3 = new EventBar("test", 0, 5, 4);
        e3.setOnFull(b -> b.remove(5, ACCEPT));
        e3.addBonus(1, 1);
        switch (e1.getReal()) {
            case 0:  assertTrue(true); break;
            case 5:  fail("The lambda-exp was not called."); break;
            default: fail("Unknown reason; value=" + e1.getReal()); break;
        }
    }

    /**
     * Test of setOnEmpty method, of class EventBar.
     */
    @Test
    public void testSetOnEmpty() {
        System.out.println("EventBar#setOnEmpty:none");
        EventBar e2 = new EventBar("test", 0, 5, 3);
        e2.setOnEmpty(b -> fail("The bar is not empty"));
        e2.remove(1, ACCEPT);
        
        System.out.println("EventBar#setOnEmpty:remove");
        EventBar e1 = new EventBar("test", 0, 5, 1);
        e1.setOnEmpty(b -> b.add(5, ACCEPT));
        e1.remove(1, ACCEPT);
        switch (e1.getReal()) {
            case 5:  assertTrue(true); break;
            case 0:  fail("The lambda-exp was not called."); break;
            default: fail("Unknown reason; value=" + e1.getReal()); break;
        }
        
        System.out.println("EventBar#setOnEmpty:end of a bonus");
        EventBar e3 = new EventBar("test", 0, 5, 0);
        e3.setOnEmpty(b -> b.add(5, ACCEPT));
        e3.addBonus(1, 1);
        e3.nextTick();
        switch (e1.getReal()) {
            case 5:  assertTrue(true); break;
            case 0:  fail("The lambda-exp was not called."); break;
            default: fail("Unknown reason; value=" + e1.getReal()); break;
        }
    }
    
}
