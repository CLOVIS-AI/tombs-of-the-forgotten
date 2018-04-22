/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

import java.util.Map;
import java.util.Optional;
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
public class ChestTest {
    
    public ChestTest() {
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
     * Test of getContainer method, of class Chest.
     */
    @Test
    public void testGetContainer() {
        System.out.println("getContainer");
        Chest instance = null;
        Map<String, Item> expResult = null;
        Map<String, Item> result = instance.getContainer();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDescription method, of class Chest.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        Chest c1 = new Chest(Optional.empty());
        assertFalse(c1.getDescription().isPresent());
        
        Chest c2 = new Chest("description");
        assertTrue(c2.getDescription().isPresent());
        assertEquals("description", c2.getDescription().get());
    }

    /**
     * Test of equals method, of class Chest.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Item i = new Weapon("d", 0, "d", Rarity.RARE, 0, 0, 0);
        Chest c1 = new Chest("d");
        c1.add(i);
        
        Chest c2 = new Chest("d");
        c2.add(i);
        
        assertTrue(c1.equals(c2));
    }

    /**
     * Test of removeItem method, of class Chest.
     */
    @Test
    public void testRemoveItem() {
        System.out.println("remove, add, contains");
        Item i = new Weapon("d", 0, "d", Rarity.RARE, 0, 0, 0);
        
        Chest c1 = new Chest("d");
        c1.add(i);
        assertTrue(c1.contains(i));
        
        c1.remove(i);
        assertFalse(c1.contains(i));
    }
    
}
