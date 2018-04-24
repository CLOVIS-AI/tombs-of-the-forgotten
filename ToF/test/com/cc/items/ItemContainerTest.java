/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

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
public class ItemContainerTest {
    
    public ItemContainerTest() {
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
     * Test of getDescription method, of class Chest.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        ItemContainer c1 = new ItemContainer(Optional.empty());
        assertFalse(c1.getDescription().isPresent());
        
        ItemContainer c2 = new ItemContainer("description");
        assertTrue(c2.getDescription().isPresent());
        assertEquals("description", c2.getDescription().get());
    }

    /**
     * Test of equals method, of class Chest.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Item i = new Weapon("d", 1, "d", Rarity.RARE, 1, 0, 1);
        ItemContainer c1 = new ItemContainer("d");
        c1.add(i);
        
        ItemContainer c2 = new ItemContainer("d");
        c2.add(i);
        
        assertTrue(c1.equals(c2));
    }

    /**
     * Test of removeItem method, of class Chest.
     */
    @Test
    public void testRemoveItem() {
        System.out.println("remove, add, contains");
        Item i = new Weapon("d", 1, "d", Rarity.RARE, 1, 0, 1);
        
        ItemContainer c1 = new ItemContainer("d");
        c1.add(i);
        assertTrue(c1.contains(i));
        
        c1.remove(i);
        assertFalse(c1.contains(i));
    }
    
}
