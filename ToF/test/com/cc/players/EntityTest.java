/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.players;

import com.cc.items.Item;
import com.cc.items.Rarity;
import static com.cc.world.Direction.NORTH;
import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.World;
import com.cc.world.links.Link;
import com.cc.world.links.Opening;
import static java.util.Arrays.asList;
import java.util.TreeMap;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author schourouq
 * @author Ivan Canet
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
        Item i = new Item("weapon", "description", Rarity.RARE, 20, 20);
        Item n = new Item("weapon 2", "description", Rarity.RARE, 20, 20);
        Player p = new Player("p", 100, 50, 50, 200);
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
        Item i = new Item("weapon", "description", Rarity.RARE, 20, 20);
        Player p = new Player("p", 100, 50, 50, 200);
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
        Item i = new Item("weapon", "description", Rarity.RARE, 20, 20);
        Player p = new Player("p", 100, 50, 50, 200);
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
    
    @Test
    public void testNextTick() {
        System.out.println("Entity#nextTick");
        Entity e1 = new Entity("1", 1, 1, 1, 1) {};
        Entity e2 = new Entity("2", 2, 2, 2, 2) {};
        assertFalse(e1.equals(e2)); // check that the test won't break
        
        Player p = new Player("p", 3, 3, 3, 3);
        Room r1 = new Room("r1").setLocation(new Location(0, 0, 0));
        Room r2 = new Room("r2").setLocation(new Location(-1, 0, 0));
        Link link = new Opening(r1, r2);
        link.autoLink();
        TreeMap<Location, Room> map = World.createTreeMap(asList(r1, r2));
        World w = new World(map, p, asList(e1, e2));
        
        assertTrue(e2.canMoveTo(NORTH));
        e2.moveTo(NORTH);
        w.nextTick();
        
        assertEquals(new Location(), p.getLocation());
        assertEquals(new Location(), e1.getLocation());
        assertEquals(new Location(-1, 0, 0), e2.getLocation());
        
        assertTrue(w.getEntities(true).anyMatch(e -> e.equals(e1)));
        assertTrue(w.getEntities(true).anyMatch(e -> e.equals(e2)));
        
        assertTrue("Expected e1, found none", p.getOpponent().isPresent());
        assertTrue("Expected p, found none", e1.getOpponent().isPresent());
        System.out.println(e2.getOpponent());
        assertFalse("Expected none, found " + e2.getOpponent(), e2.getOpponent().isPresent());
        
        assertEquals(e1, p.getOpponent().get());
        assertEquals(p, e1.getOpponent().get());
    }
    
}
