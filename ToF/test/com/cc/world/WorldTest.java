/*
 * The MIT License
 *
 * Copyright 2018 CanetChourouq.
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
import com.cc.world.links.Door;
import com.cc.world.links.Link;
import com.eclipsesource.json.WriterConfig;
import java.util.TreeMap;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ivan
 */
public class WorldTest {
    
    public WorldTest() {
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
     * Test of save method, of class World.
     */
    @Test
    public void testSave() {
        System.out.println("World#save");
        TreeMap<Location, Room> map = new TreeMap<>();
        Room r1 = new Room("1st room");
        Room r2 = new Room("2nd room");
        map.put(new Location(0, 0, 0), r1);
        map.put(new Location(0, 0, 1), r2);
        Link l = new Door(r1, r2);
        Player p = new Player("p", 20, 1, 5, 5);
        World w = new World(map, p);
        l.autoLink();
        System.out.println(w.save().toString(WriterConfig.MINIMAL));
        World ww = new World(w.save());
        System.out.println(ww.save().toString(WriterConfig.MINIMAL));
        
        assertEquals(w.getPlayer(), ww.getPlayer());
        assertEquals(w.rooms, ww.rooms);
        assertTrue(w.equals(ww));
    }
    
}
