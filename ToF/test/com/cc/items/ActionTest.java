/* MIT License
 *
 * Copyright (c) 2018 Canet Ivan & Chourouq Sarah
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.cc.items;

import static com.cc.items.EntityAction.Mode.MODIFICATION;
import static com.cc.items.EntityAction.Operation.ADD;
import static com.cc.items.EntityAction.Target.SELF;
import static com.cc.players.Entity.Stat.MANA;
import com.eclipsesource.json.JsonObject;
import java.util.Objects;
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
public class ActionTest {
    
    public ActionTest() {
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
     * Test of save method, of class EntityAction.
     */
    @Test
    public void testSave() {
        System.out.println("Action#save&load");
        EntityAction a = new EntityAction(SELF, ADD, MANA, 2, MODIFICATION);
        JsonObject json = a.save();
        EntityAction b = new EntityAction(json);
        assertTrue(Objects.equals(a, b));
        
        EntityAction c = new EntityAction(SELF, ADD, MANA, 3, MODIFICATION);
        assertFalse(Objects.equals(a, c));
    }

    /**
     * Test of equals method, of class EntityAction.
     */
    @Test
    public void testEquals() {
        System.out.println("Action#equals");
        EntityAction a = new EntityAction(SELF, ADD, MANA, 0, MODIFICATION);
        EntityAction b = new EntityAction(SELF, ADD, MANA, 0, MODIFICATION);
        EntityAction c = new EntityAction(SELF, ADD, MANA, 1, MODIFICATION);
        
        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
        assertTrue(b.equals(a));
        assertFalse(b.equals(c));
        assertFalse(c.equals(a));
        assertFalse(c.equals(b));
    }
    
}
