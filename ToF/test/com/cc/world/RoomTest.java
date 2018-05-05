/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import com.cc.players.Entity;
import com.cc.players.Player;
import com.cc.world.links.Link;
import com.cc.world.links.LinkTest;
import com.cc.world.links.Opening;
import java.util.Arrays;
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
        Direction d = r1.getDirectionTo(r2).get();
        assertEquals(Direction.fromCoordinates(0, 1, 0), d);
    }
    
    @Test
    public void canMove() {
        System.out.println("Room#canMove:recursive");
        Room r1 = new Room("1").setLocation(new Location(0, 0, 0));
        Room r2 = new Room("2").setLocation(new Location(0, 0, 1));
        Room r3 = new Room("3").setLocation(new Location(0, 0, 2));
        Room r4 = new Room("4").setLocation(new Location(0, 0, 3));
        Room r5 = new Room("5").setLocation(new Location(0, 0, 5));
        
        /*World w = new World(World.createTreeMap(Arrays.asList(
                r1, r2, r3, r4, r5
        )), new Player(1, 1, 1, 1));*/
        
        new Opening(r1, r2).autoLink();
        new Opening(r2, r3).autoLink();
        new Opening(r3, r4).autoLink();
        
        Entity e = new Player(1, 1, 1, 1);
        assertEquals(true, r1.canMove(r2));
        assertEquals(true, r1.canMove(r2, 1, e));
        assertEquals(true, r1.canMove(r3, 2, e));
        assertEquals(true, r1.canMove(r3, 3, e));
        assertEquals(false, r1.canMove(r4, 2, e));
        assertEquals(true, r1.canMove(r4, 4, e));
        assertEquals(false, r1.canMove(r5, 2, e));
        assertEquals(false, r1.canMove(r5, 12, e));
    }
}
