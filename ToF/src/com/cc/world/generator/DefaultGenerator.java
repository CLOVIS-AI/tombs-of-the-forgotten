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
package com.cc.world.generator;

import com.cc.utils.Pair;
import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.World;
import com.cc.world.links.Door;
import com.cc.world.links.Link;
import com.cc.world.links.Opening;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * The default generator of the game.
 * 
 * <p>This generator is limited by different rules in the project:
 * <ul>
 *  <li>A link cannot be created before the two rooms it links to.</li>
 *  <li>There are links/doors that should be placed sequentially (a door and 
 *      the room its key is in)</li>
 * </ul>
 * Therefore, it follows this algorithm:
 * <ol>
 *  <li>Generates two pairs of a Room and a Link to be placed:
 *    <ul>
 *      <li>At most, one of these pairs can be locked.</li>
 *      <li>When the pairs are generated, swaps their link (so if there's a 
 *          link/door couple, they don't appear side-by-side).</li>
 *    </ul>
 *  </li>
 *  <li>Places both pairs:
 *    <ol>
 *      <li>Picks a random room in the already-generated ones and a random 
 *          adjacent location that is empty,</li>
 *      <li>Puts the selected room there,</li>
 *      <li>Links them with the selected link,</li>
 *      <li>Loops to do the same with the other pair.</li>
 *    </ol>
 *  </li>
 * </ol>
 * 
 * @author Ivan Canet
 */
public class DefaultGenerator implements Generator {

    Random random;
    TreeMap<Location, Room> rooms;
    boolean isGenerated = false;
    
    @Override
    public World generate(Random randomizer) {
        if(isGenerated)
            throw new IllegalStateException("This generator has already been used.");
        
        random = randomizer;
        
        
        isGenerated = true;
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    void iteration(){
        Pair<Room, Function<Room[], Link>> p1 = createPair();
        Pair<Room, Function<Room[], Link>> p2 = createPair();
        
        {   // Swap the links
            Function<Room[], Link> temp = p1.getSecond();
            p1.setSecond(p2.getSecond());
            p2.setSecond(temp);
        }
        
        for(Pair<Room, Function<Room[], Link>> p : Arrays.asList(p1, p2)){
            Pair<Room, Location> begin = pickRoom();
            rooms.put(begin.getSecond(), p.getFirst());
            p.getSecond()
                    .apply(
                        new Room[] { begin.getFirst(), p.getFirst() }
                    ).autoLink();
        }
    }
    
    Pair<Room, Function<Room[], Link>> createPair(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    Pair<Room, Location> pickRoom(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // ************************************************************* S T A T I C
    
    private static final Map<Integer, Pair<Room, Function<Room[], Link>>> PAIRS;
    private static final int TOTAL;
    
    static {
        PAIRS = new HashMap<>();
        PAIRS.put(15, new Pair<>(new Room("Random room"), Opening::new));
        PAIRS.put( 5, new Pair<>(new Room("Random room"), Door::new));
        
        TOTAL = PAIRS.keySet().stream()
                .mapToInt(v -> (int)v)
                .sum();
    }
    
}
