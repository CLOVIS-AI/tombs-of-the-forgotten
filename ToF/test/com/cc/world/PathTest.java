/*
 * The MIT License
 *
 * Copyright 2018 Canet Ivan & Chourouq Sarah.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cc.world;

import com.cc.players.Player;
import com.cc.world.links.Opening;
import java.util.Arrays;
import java.util.List;
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
public class PathTest {
    
    public PathTest() {
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
     * Test of createPath method, of class Path.
     */
    @Test
    public void testCreatePath() {
        System.out.println("createPath");
        /* 123
         *   489 a
         * 567 0
         */
        
        Room r1 = new Room("1").setLocation(new Location(0, 0, 0));
        Room r2 = new Room("2").setLocation(new Location(0, 1, 0));
        Room r3 = new Room("3").setLocation(new Location(0, 2, 0));
        Room r4 = new Room("4").setLocation(new Location(1, 2, 0));
        Room r5 = new Room("5").setLocation(new Location(2, 0, 0));
        Room r6 = new Room("6").setLocation(new Location(2, 1, 0));
        Room r7 = new Room("7").setLocation(new Location(2, 2, 0));
        Room r8 = new Room("8").setLocation(new Location(1, 3, 0));
        Room r9 = new Room("9").setLocation(new Location(1, 4, 0));
        Room r0 = new Room("0").setLocation(new Location(2, 4, 0));
        Room ra = new Room("a").setLocation(new Location(1, 6, 0));
        
        new Opening(r1, r2).autoLink();
        new Opening(r2, r3).autoLink();
        new Opening(r3, r4).autoLink();
        new Opening(r4, r7).autoLink();
        new Opening(r7, r6).autoLink();
        new Opening(r6, r5).autoLink();
        new Opening(r4, r8).autoLink();
        new Opening(r8, r9).autoLink();
        new Opening(r9, r0).autoLink();
        
        List<Room> rooms = Arrays.asList(
                r0, r1, r2, r3, r4, r5, r6, r7, r8, r9
        );
        Player p = new Player("p", 1, 1, 1, 1);
        World world = new World(World.createTreeMap(rooms), p);
        
        try {
            Path from1to2 = r1.pathTo(r2, p);
            assertEquals(r1, from1to2.moveToNext());
            assertEquals(r2, from1to2.moveToNext());
        } catch (Path.UnreachableRoomException ex) {
            fail("The path r1-r2 should have been reachable:"+ex.getMessage());
        }
        
        try {
            Path from1to4 = r1.pathTo(r4, p);
            assertEquals(r1, from1to4.moveToNext());
            assertEquals(r2, from1to4.moveToNext());
            assertEquals(r3, from1to4.moveToNext());
            assertEquals(r4, from1to4.moveToNext());
        } catch (Path.UnreachableRoomException ex) {
            fail("The path r1-r4 should have been reachable:"+ex.getMessage());
        }
        
        try {
            Path from6to5 = r6.pathTo(r5, p);
            assertEquals(r6, from6to5.moveToNext());
            assertEquals(r5, from6to5.moveToNext());
        } catch (Path.UnreachableRoomException ex) {
            fail("The path r6-r5 should have been reachable:"+ex.getMessage());
        }
        
        try {
            Path from4to5 = r4.pathTo(r5, p);
            assertEquals(r4, from4to5.moveToNext());
            assertEquals(r7, from4to5.moveToNext());
            assertEquals(r6, from4to5.moveToNext());
            assertEquals(r5, from4to5.moveToNext());
        } catch (Path.UnreachableRoomException ex) {
            fail("The path r4-r5 should have been reachable:"+ex.getMessage());
        }
        
        try {
            Path from5to1 = r5.pathTo(r1, p);
            assertEquals(r5, from5to1.moveToNext());
            assertEquals(r6, from5to1.moveToNext());
            assertEquals(r7, from5to1.moveToNext());
            assertEquals(r4, from5to1.moveToNext());
            assertEquals(r3, from5to1.moveToNext());
            assertEquals(r2, from5to1.moveToNext());
            assertEquals(r1, from5to1.moveToNext());
        } catch (Path.UnreachableRoomException ex) {
            fail("The path r5-r1 should have been reachable:"+ex.getMessage());
        }
        
        try {
            Path from1to0 = r1.pathTo(r0, p);
            assertEquals(r1, from1to0.moveToNext());
            assertEquals(r2, from1to0.moveToNext());
            assertEquals(r3, from1to0.moveToNext());
            assertEquals(r4, from1to0.moveToNext());
            assertEquals(r8, from1to0.moveToNext());
            assertEquals(r9, from1to0.moveToNext());
            assertEquals(r0, from1to0.moveToNext());
        } catch (Path.UnreachableRoomException ex) {
            fail("The path r1-r0 should have been reachable:"+ex.getMessage());
        }
        
        try {
            Path from4toa = r4.pathTo(ra, p);
            fail("There is no path between r4 and ra!");
        } catch (Path.UnreachableRoomException ex) {
            assertTrue(true);
        }
    }
    
}
