/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils;

import static com.cc.utils.Bar.Behavior.ACCEPT;
import static com.cc.utils.Bar.Behavior.DENY;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ivan
 */
public class BarTest {
    
    public BarTest() {
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
     * Test of add method, of class Bar.
     */
    @Test
    public void addAccept() {
        System.out.println("add:accept");
        Bar b = new Bar(null, 5, 10, 9);
        b.add(2, ACCEPT);
        assertEquals(10, b.getReal());
    }
    
    @Test
    public void addNormal() {
        System.out.println("add:normal");
        Bar b = new Bar(null, 0, 10, 0);
        
        b.add(1, ACCEPT);
        assertEquals(1, b.getReal());
        
        b.add(1, DENY);
        assertEquals(2, b.getReal());
    }
    
    @Test
    public void addDeny() {
        System.out.println("add:deny");
        Bar b = new Bar(null, 0, 10, 10);
        try{
            b.add(1, DENY);
            fail();
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
    }

    @Test
    public void removeAccept() {
        System.out.println("remove:accept");
        Bar b = new Bar(null, 5, 10, 6);
        b.remove(2, ACCEPT);
        assertEquals(5, b.getReal());
    }
    
    @Test
    public void removeNormal() {
        System.out.println("remove:normal");
        Bar b = new Bar(null, 0, 10, 10);
        
        b.remove(1, ACCEPT);
        assertEquals(9, b.getReal());
        
        b.remove(1, DENY);
        assertEquals(8, b.getReal());
    }
    
    @Test
    public void removeNegatives() {
        System.out.println("remove:negatives");
        Bar b = new Bar(null, 0, 5, 5);
        
        try{
            b.remove(-1, ACCEPT);
            fail();
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
        
        try{
            b.remove(-1, DENY);
            fail();
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
    }
    
    @Test
    public void removeDeny() {
        System.out.println("remove:deny");
        Bar b = new Bar(null, 0, 10, 0);
        try{
            b.remove(1, DENY);
            fail();
        }catch(IllegalArgumentException e){
            assertTrue(true);
        }
    }
    
    @Test
    public void copy() {
        System.out.println("Bar#copy");
        Bar a = new Bar("Bonjour", 1, 5, 2);
        a.add(2, ACCEPT);
        a.addBonus(12, 4, true);
        a.decreaseMinimum(1);
        a.nextTick();
        Bar b = new Bar(a);
        
        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
    }
}
