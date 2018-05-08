/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world.links;

import com.cc.players.Entity;
import com.cc.players.Player;
import com.cc.world.Direction;
import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.World;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
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
public class LinkTest {
    
    public LinkTest() {
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
     * Test of links method, of class Link.
     */
    @Test
    public void testLinks() {
        System.out.println("links");
        Room r1 = new Room("d");
        Link l = new LinkImpl(r1, new Room("e"), true);
        assertTrue(l.links(r1));
    }

    /**
     * Test of autoLink method, of class Link.
     */
    @Test
    public void testAutoLink() {
        Map<Direction, List<Location>> tests = new HashMap<>();
        tests.put(Direction.UP, asList(
                new Location(0, 0, 0), 
                new Location(0, 0, 1)));
        tests.put(Direction.DOWN, asList(
                new Location(0, 0, 0), 
                new Location(0, 0, -1)));
        tests.put(Direction.EAST, asList(
                new Location(0, 0, 0), 
                new Location(0, 1, 0)));
        tests.put(Direction.WEST, asList(
                new Location(0, 0, 0), 
                new Location(0, -1, 0)));
        tests.put(Direction.NORTH, asList(
                new Location(0, 0, 0), 
                new Location(-1, 0, 0)));
        tests.put(Direction.SOUTH, asList(
                new Location(0, 0, 0), 
                new Location(1, 0, 0)));
        
        for(Entry<Direction, List<Location>> e : tests.entrySet()){
            Direction d = e.getKey();
            System.out.println("Link#autoLink:" + d);
            Location departure = e.getValue().get(0);
            Location arrival = e.getValue().get(1);
            
            Room r1 = new Room("departure").setLocation(departure);
            Room r2 = new Room("arrival").setLocation(arrival);
            Link l = new LinkImpl(r1, r2, true);
            l.autoLink();
            
            Player p = new Player("p", 1, 1, 1, 1);
            World w = new World(World.createTreeMap(asList(r1, r2)), p);
            assertTrue(p.canMoveTo(r2));
            
            assertTrue(p.canMoveTo(d));
            
            Stream.of(Direction.values())
                    .filter(dir -> !dir.equals(d))
                    .forEach(dir -> assertFalse(p.canMoveTo(dir)));
        }
    }

    /**
     * Test of getOtherRoom method, of class Link.
     */
    @Test
    public void testGetOtherRoom() {
        System.out.println("getOtherRoom");
        Room r1 = new Room("d");
        Room r2 = new Room("e");
        Link l = new LinkImpl(r1, r2, true);
        assertEquals(r2, l.getOtherRoom(r1));
        assertEquals(r1, l.getOtherRoom(r2));
    }

    /**
     * Test of equals method, of class Link.
     */
    @Test
    public void testEqualsHashCode() {
        System.out.println("Link#equals & Link#hashCode");
        Room r1 = new Room("d").setLocation(new Location(0, 0, 0));
        Room r2 = new Room("e").setLocation(new Location(0, 1, 0));
        Room r3 = new Room("f").setLocation(new Location(0, 2, 1));
        
        Link l1 = new LinkImpl(r1, r2, true);
        Link l2 = new LinkImpl(r2, r1, true);
        Link l3 = new LinkImpl(r1, r3, true);
        
        assertFalse(l1.equals(l3));
        assertTrue(l1.equals(l2));
        assertTrue(l2.equals(l1));
        assertTrue(l1.hashCode() == l2.hashCode());
    }

    public static class LinkImpl extends Link {
        
        public LinkImpl(Room r1, Room r2, boolean openByDefault) {
            super(r1, r2, openByDefault);
        }

        @Override
        public boolean canOpen(Entity e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean canClose(Entity e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean open(Entity e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean close(Entity e) {
            throw new UnsupportedOperationException();
        }
    }
    
}
