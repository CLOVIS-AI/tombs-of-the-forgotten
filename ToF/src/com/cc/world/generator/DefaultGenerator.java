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

import com.cc.items.Item;
import com.cc.players.Player;
import com.cc.utils.Pair;
import com.cc.world.Direction;
import com.cc.world.Location;
import com.cc.world.Note;
import com.cc.world.Room;
import com.cc.world.World;
import com.cc.world.links.Door;
import com.cc.world.links.KeyDoor;
import com.cc.world.links.Link;
import com.cc.world.links.Opening;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        rooms = new TreeMap<>();
        addRoom(new Room("This is where you spawn.").explore(), new Location());
        int number = random.nextInt(50) + 10;
        for(int i = 0; i < number; i++){
            iteration();
        }
        
        isGenerated = true;
        return new World(rooms.values(), new Player());
    }
    
    void iteration(){
        Pair<Supplier<Room>, Function<Room[], Link>> p1 = createPair();
        Pair<Supplier<Room>, Function<Room[], Link>> p2 = createPair();
        
        {   // Swap the links
            Function<Room[], Link> temp = p1.getSecond();
            p1.setSecond(p2.getSecond());
            p2.setSecond(temp);
        }
        for(Pair<Supplier<Room>, Function<Room[], Link>> p : Arrays.asList(p1, p2)){
            Pair<Room, Location> picked = pickRoom();
            Room candidate = p.getFirst().get();
            addRoom(candidate, picked.getSecond());
            p.getSecond()
                    .apply(new Room[] { picked.getFirst(), candidate }
                    ).autoLink();
        }
    }
    
    void addRoom(Room r, Location l){
        rooms.put(l, r);
        r.setLocation(l);
    }
    
    Pair<Supplier<Room>, Function<Room[], Link>> createPair(){
        int choice = random.nextInt(TOTAL+1);
        for (Entry<Integer, Pair<Supplier<Room>, Function<Room[], Link>>> pair : PAIRS.entrySet()) {
            int poss = pair.getKey();
            if(poss > (choice -= poss))
                return pair.getValue();
        }
        
        throw new IllegalStateException("Because of the way pairs work, there "
                + "shouldn't be a way to NOT find one.\n"
                + "The random value was " + choice + "\n"
                + "The assocations are: \n"
                + exportPairs());
    }
    
    Pair<Room, Location> pickRoom(){
        Room r;
        Location l;
        final List<Room> candidates = new ArrayList<>(rooms.values());
        
        int safeguard = 0;
        do {
            final Room ro = candidates.get(random.nextInt(candidates.size()));
            List<Location> locs = Stream.of(Direction.values())
                    .filter((Direction d) -> !ro.getNeighbor(d).isPresent())
                    .map(d -> ro.getLocation().add(d))
                    .collect(Collectors.toList());
            l = locs.isEmpty() ? null : locs.get(random.nextInt(locs.size()));
            r = ro; // two variables so the Stream doesn't complain about not final
            
            if(safeguard++ > 10)
                throw new IllegalStateException("Detected an infinite loop!");
        } while(l == null);
        
        return new Pair<>(r, l);
    }
    
    private final Map<Integer, Pair<Supplier<Room>, Function<Room[], Link>>> PAIRS;
    private final int TOTAL;
    
    {
        PAIRS = new HashMap<>();
        PAIRS.put(15, new Pair<>(this::createRandomRoom, Opening::new));
        PAIRS.put( 5, new Pair<>(this::createRandomRoom, Door::new));
        PAIRS.put( 1, this::lockedDoor);
        
        TOTAL = PAIRS.keySet().stream()
                .mapToInt(v -> (int)v)
                .sum();
    }
    
    private Room createRandomRoom(){
        Room randomizedRoom = new Room("Random room");
        
        for(int i = 0; i < random.nextInt(3); i++){
            List<Integer> notes = new ArrayList<>(Note.getIDs());
            randomizedRoom.addNote(new Note(notes.get(random.nextInt(notes.size()))));
        }
        
        return randomizedRoom;
    }
    
    private Item createRandomItem(){
        return null;
    }
    
    private Pair<Supplier<Room>, Function<Room[], Link>> lockedDoor(){
        
        int key = abs(random.nextInt(Integer.MAX_VALUE-1)+1);
        Room room = createRandomRoom();
        Item item = createRandomItem();
        item.setId(key);
        room.getItems().add(item);
        
        return new Pair<>(this::createRandomRoom, (Room[] r) -> new KeyDoor(key, r));
    }
    
    private String exportPairs(){
        StringBuilder sb = new StringBuilder();
        
        PAIRS.forEach((key, value) -> sb
                .append(key)
                .append(": ")
                .append(value)
                .append("\n")
        );
        
        return sb.toString();
    }
    
}
