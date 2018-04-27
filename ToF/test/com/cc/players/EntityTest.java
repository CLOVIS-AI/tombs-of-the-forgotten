/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.players;

import com.cc.items.Item;
import com.cc.items.Rarity;
import com.cc.items.Weapon;
import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.World;
import java.util.TreeMap;
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
public class EntityTest {
    
    public EntityTest() {
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
     * Test of hasItem method, of class Entity.
     */
    @Test
    public void testHasItem() {
        System.out.println("Entity#hasItem");
        Item i = new Weapon("weapon", 110, "description", Rarity.RARE, 20, 20, 20);
        Item n = new Weapon("weapon 2", 10, "description", Rarity.RARE, 20, 20, 20);
        Player p = new Player(100, 50, 50, 200);
        p.addItem(i);
        
        assertTrue(p.hasItem(i));
        assertFalse(p.hasItem(n));
    }

    /**
     * Test of removeItem method, of class Entity.
     */
    @Test
    public void testRemoveItem() {
        System.out.println("Entity#removeItem");
        Item i = new Weapon("weapon", 110, "description", Rarity.RARE, 20, 20, 20);
        Player p = new Player(100, 50, 50, 200);
        p.addItem(i);
        p.removeItem(i);
        
        assertFalse(p.hasItem(i));
    }

    /**
     * Test of dropItem method, of class Entity.
     */
    @Test
    public void testDropItem() {
        System.out.println("Entity#dropItem");
        Item i = new Weapon("weapon", 110, "description", Rarity.RARE, 20, 20, 20);
        Player p = new Player(100, 50, 50, 200);
        Location l = new Location(0, 0, 0);
        TreeMap<Location, Room> map = new TreeMap<>();
        World w = new World(map, p);
        Room r = new Room("d", l, w);
        
        map.put(l, r);
        p.addItem(i);
        p.dropItem(i);
        
        assertTrue(p.getCurrentRoom().getItems().contains(i));
        assertFalse(p.hasItem(i));
    }
    
}
