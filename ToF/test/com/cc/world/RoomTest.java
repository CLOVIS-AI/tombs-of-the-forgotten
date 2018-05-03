/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import com.cc.players.Entity;
import com.cc.world.links.Link;
import com.cc.world.links.LinkTest;
import java.util.Collection;
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
public class RoomTest {
    
    public RoomTest() {
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
     * Test of getDirectionTo method, of class Room.
     */
    @Test
    public void testGetDirectionTo() {
        System.out.println("getDirectionTo");
        Room r1 = new Room("1").setLocation(new Location(0, 0, 0));
        Room r2 = new Room("2").setLocation(new Location(0, 1, 0));
        Link l = new LinkTest.LinkImpl(r1, r2, true);
        l.autoLink();
        Direction d = r1.getDirectionTo(r2);
        assertEquals(Direction.fromCoordinates(0, 1, 0), d);
    }
}
