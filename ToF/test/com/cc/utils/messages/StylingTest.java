/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils.messages;

import java.awt.Color;
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
public class StylingTest {
    
    public StylingTest() {
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
     * Test of getColor method, of class Styling.
     */
    @Test
    public void testGetters() {
        System.out.println("Styling#getters");
        Styling s1 = new Styling()
                .setColor(Color.BLACK);
        
        assertFalse(s1.isBold());
        assertFalse(s1.isItalic());
        assertEquals(Color.BLACK, s1.getColor());
        
        Styling s2 = new Styling()
                .setColor(Color.DARK_GRAY)
                .setBold()
                .setItalic();
        
        assertTrue(s2.isBold());
        assertTrue(s2.isItalic());
        assertEquals(Color.DARK_GRAY, s2.getColor());
    }

    @Test
    public void testLock() {
        System.out.println("Styling#lock");
        Styling s = new Styling()
                .lock();
        
        try{
            s.setBold();
            fail();
        }catch(Exception e){}
        
        try{
            s.setItalic();
            fail();
        }catch(Exception e){}
        
        try{
            s.setColor(Color.yellow);
            fail();
        }catch(Exception e){}
        
        assertTrue(true);
    }
    
}
