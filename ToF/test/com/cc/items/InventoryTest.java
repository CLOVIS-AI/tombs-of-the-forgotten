/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.items;

import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author schourouq
 */
public class InventoryTest {
    
    public InventoryTest() {
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
     * Test of canAdd method, of class Inventory.
     */
    @Test
    public void testAdd() {
        System.out.println("Inventory tests");
        Inventory bigbag = new Inventory("big", 1000);
        Inventory smallbag = new Inventory("small", 50);
        Item i = new Weapon("weapon",250 , "description", Rarity.RARE, 1, 1, 2);
        
        System.out.println("canAdd");
        assertTrue(bigbag.canAdd(i));
        assertFalse(smallbag.canAdd(i));
        
        System.out.println("add");
        bigbag.add(i);
        smallbag.add(i);
        assertTrue(bigbag.contains(i));
        assertFalse(smallbag.contains(i));
        
        System.out.println("addIfPossible");
        Inventory big = new Inventory("big", 2000);
        Inventory small = new Inventory("small", 50);
        Item a = new Weapon("A", 1000, "description", Rarity.RARE, 10, 1, 2);
        Item b = new Armor("B", 100, "description", Rarity.RARE, 150);
        Collection<Item> items = Arrays.asList(a, b);
        big.addIfPossible(items);        
        small.addIfPossible(items);
        assertTrue(big.contains(a));
        assertTrue(big.contains(b));
        assertFalse(small.contains(a));
        assertFalse(small.contains(b));
    }

}
