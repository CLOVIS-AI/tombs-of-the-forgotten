/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import static com.cc.world.Direction.DOWN;
import static com.cc.world.Direction.EAST;
import static com.cc.world.Direction.NORTH;
import static com.cc.world.Direction.SOUTH;
import static com.cc.world.Direction.UP;
import static com.cc.world.Direction.WEST;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
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
public class LocationTest {
    
    public LocationTest() {
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
     * Test of add method, of class Location.
     */
    @Test
    public void testAdd_Location() {
        System.out.println("add");
        Location l1 = new Location(0, 0, 0);
        Location l2 = new Location(1, 1, 1);
        assertEquals(l2, l1.add(l2));
        
        Location l3 = new Location(5, 3, 2);
        Location l4 = new Location(6, 4, 3);
        assertEquals(l4, l2.add(l3));
    }

    /**
     * Test of add method, of class Location.
     */
    @Test
    public void testAdd_Direction() {
        System.out.println("add");
        Location l1 = new Location(5, 5, 5);
        assertEquals(new Location(4, 5, 5), l1.add(NORTH));
        assertEquals(new Location(6, 5, 5), l1.add(SOUTH));
        assertEquals(new Location(5, 6, 5), l1.add(EAST));
        assertEquals(new Location(5, 4, 5), l1.add(WEST));
        assertEquals(new Location(5, 5, 6), l1.add(UP));
        assertEquals(new Location(5, 5, 4), l1.add(DOWN));
    }

    /**
     * Test of equals method, of class Location.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Location l1 = new Location(5, 3, 4);
        Location l2 = new Location(5, 3, 4);
        assertTrue(l1.equals(l2));
    }

    /**
     * Test of compareTo method, of class Location.
     */
    @Test
    public void testCompareTo() {
        System.out.println("compareTo");
        
        TreeSet<Location> t = new TreeSet<>();
        t.addAll(Arrays.asList(
                new Location(0, 0, 0),
                new Location(1, 1, 1),
                new Location(0, 1, 0),
                new Location(1, 0, 0),
                new Location(0, 0, 1)
        ));
        
        List<Location> l = t.stream()
                .collect(Collectors.toList());
        
        assertEquals(new Location(0, 0, 0), l.get(0));
        assertEquals(new Location(0, 1, 0), l.get(1));
        assertEquals(new Location(1, 0, 0), l.get(2));
        assertEquals(new Location(0, 0, 1), l.get(3));
        assertEquals(new Location(1, 1, 1), l.get(4));
        
        // -----------------------------------------
        
        TreeMap<Location, Integer> iii = new TreeMap();
        iii.put(new Location(0, 0, 0), 1);
        iii.put(new Location(-1, 0, 0), 0);
        iii.put(new Location(1, 0, 0), 3);
        iii.put(new Location(0, 1, 0), 2);
        iii.put(new Location(1, 1, 0), 4);
        iii.put(new Location(2, 0, 0), 5);
        
        iii.keySet().forEach(System.out::println);
        
        List<Integer> ii = iii
                .values()
                .stream()
                .collect(Collectors.toList());
        for(int i = 0; i < ii.size(); i++)
            assertEquals(i, (int)ii.get(i));
    }
    
}
