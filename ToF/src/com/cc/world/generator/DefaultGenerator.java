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

import com.cc.items.EntityAction;
import static com.cc.items.EntityAction.Mode.MODIFICATION;
import static com.cc.items.EntityAction.Operation.ADD;
import static com.cc.items.EntityAction.Operation.REMOVE;
import static com.cc.items.EntityAction.Target.OPPONENT;
import static com.cc.items.EntityAction.Target.SELF;
import com.cc.items.Item;
import com.cc.items.Item.ItemBuilder;
import static com.cc.items.Rarity.COMMON;
import static com.cc.items.Rarity.EPIC;
import static com.cc.items.Rarity.RARE;
import com.cc.players.Entity;
import static com.cc.players.Entity.Stat.HEALTH;
import static com.cc.players.Entity.Stat.STAMINA;
import com.cc.players.Player;
import com.cc.players.SimpleAI;
import static com.cc.tof.ToF.println;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;
import java.util.function.Function;
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
    List<Entity> entities;
    boolean isGenerated = false;
    
    private static final int NBR_ROOMS_MAX = 200;
    private static final int NBR_ROOMS_MIN = 100;
    
    private static final int NBR_SHORT_MAX = 50;
    private static final int NBR_SHORT_MIN = 10;
    
    private static final int ROOM_PICK_MAX_TRIES = 100;
    
    private static final int NBR_NOTES_MAX = 3;
    
    private static final int NBR_ITEMS_MAX = 5;
    
    @Override
    public World generate(Random randomizer) {
        if(isGenerated)
            throw new IllegalStateException("This generator has already been used.");
        
        println("RNG", "Beginning random generation...");
        random = randomizer;
        rooms = new TreeMap<>();
        
        println("RNG", "Adding the spawn...");
        addRoom(new Room("This is where you spawn.").explore(), new Location());
        
        int nbrLines = rdmNbr(2, 15);
        println("RNG", "Adding " + nbrLines + " lines...");
        for(int i = 0; i < nbrLines; i++)
            line();
        println("RNG", "Generated the lines.");
        
        int number = rdmNbr(NBR_ROOMS_MIN, NBR_ROOMS_MAX);
        println("RNG", "Generation of the rooms & links: " + number + " iterations.");
        for(int i = 0; i < number; i++)
            iteration();
        println("RNG", "Generated " + rooms.size() + " rooms.");
        
        int nbrShortcuts = rdmNbr(NBR_SHORT_MIN, NBR_SHORT_MAX);
        println("RNG", "Adding shortcuts: " + nbrShortcuts + " shortcuts expected.");
        for(int i = 0; i < nbrShortcuts; i++)
            shortcut();
        
        entities = new ArrayList<>();
        int nbrEntities = rdmNbr(NBR_ROOMS_MIN/5, NBR_ROOMS_MAX/2);
        println("RNG", "Generation of the AIs: " + nbrEntities + " entities.");
        for(int i = 0; i < nbrEntities; i++)
            entities.add(createRandomEntity());

        println("RNG", "Generation of the player & validation.");
        isGenerated = true;
        return new World(rooms.values(), new Player("Player", 20, 30, 20, 20000, 0), entities);
    }
    
    boolean shortcut(){
        Room r1 = new ArrayList<>(rooms.values())
                .get(random.nextInt(rooms.size()));
        
        Optional<Room> r2 = Stream.of(Direction.values())                       // For each direction
                .filter(d -> !r1.getNeighbor(d).isPresent())                    // where there are no neighbors
                .map(d -> rooms.get(r1.getLocation().add(d)))
                .filter(r -> r != null)                                         // but there is a room
                .findAny();                                                     // Choose one
        
        if(r2.isPresent()){
            createRandomLink(new Room[]{r1, r2.get()}).autoLink();
            return true;
        } else
            return false;
    }
    
    void iteration(){
        Pair<Room, Function<Room[], Link>> pair1 = new Pair<>(createRandomRoom(), this::createRandomLink);
        Function<Room[], Pair<Room, Link>> pair2 = createPair();
        
        /* How it works:
         *      Room   Link    Pick
         * 1    2      6       1
         * 2    5      3       4
         * 
         * The rooms are placed in 6 stages.
         */
        
        // 1 Pick 'p1': (Room, Location)
        Pair<Room, Location> p1 = pickRoom();
        
        // 2 Create 'r1': Room
        Room r1 = pair1.getFirst();
        addRoom(r1, p1.getSecond());
        
        // 3 Create 'l2': Link of (r1, p1.first)
        Pair<Room, Link> pl2 = pair2.apply(new Room[]{r1, p1.getFirst()});
        Link l2 = pl2.getSecond();
        l2.autoLink();
        
        // 4 Pick 'p2': (Room, Location)
        Pair<Room, Location> p2 = pickRoom();
        
        // 5 Create 'r2': Room
        Room r2 = pl2.getFirst();
        addRoom(r2, p2.getSecond());
        
        // 6 Create 'l1': Link of (r2, p2.first)
        Link l1 = pair1.getSecond().apply(new Room[]{r2, p2.getFirst()});
        l1.autoLink();
        
        if(random.nextInt(100) < 10)
            line();
    }
    
    void line(){
        Direction dir = Direction.values()[random.nextInt(4)];
        
        int maxSize = random.nextInt(15);
        Pair<Room, Location> r = pickRoom();
        for(int i = 0; i < maxSize; i++){
            Room other = createRandomRoom();
            addRoom(other, r.getSecond());
            createRandomLink(new Room[]{r.getFirst(), other}).autoLink();
            
            Optional<Location> lo = Stream.of(dir)
                    .filter(d -> !other.getNeighbor(d).isPresent())
                    .map(d -> other.getLocation().add(d))
                    .filter(l -> !rooms.containsKey(l))
                    .findAny();
            
            if(lo.isPresent())
                r = new Pair<>(other, lo.get());
            else break;
        }
    }
    
    void addRoom(Room r, Location l){
        if(rooms.containsKey(l))
            throw new IllegalStateException("There is already a room here!");
        
        rooms.put(l, r);
        r.setLocation(l);
    }
    
    Function<Room[], Pair<Room, Link>> createPair(){
        int choice = random.nextInt(TOTAL+1);
        for (Entry<Integer, Function<Room[], Pair<Room, Link>>> pair : PAIRS.entrySet()) {
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
                    .filter(d -> !rooms.containsKey(ro.getLocation().add(d)))
                    .map(d -> ro.getLocation().add(d))
                    .collect(Collectors.toList());
            l = locs.isEmpty() ? null : locs.get(random.nextInt(locs.size()));
            r = ro; // two variables so the Stream doesn't complain about not final
            
            if(safeguard++ > ROOM_PICK_MAX_TRIES)
                throw new IllegalStateException("Detected an infinite loop!");
        } while(l == null);
        
        if(rooms.containsKey(l))
            throw new IllegalStateException("The generated location is already used!");
        
        return new Pair<>(r, l);
    }
    
    private final Map<Integer, Function<Room[], Pair<Room, Link>>> PAIRS;
    private final int TOTAL;
    
    {
        PAIRS = new HashMap<>();
        PAIRS.put(15, (rs) -> new Pair<>(createRandomRoom(), new Opening(rs)));
        PAIRS.put( 5, (rs) -> new Pair<>(createRandomRoom(), new Door(rs)));
        PAIRS.put( 1, this::lockedDoor);
        
        TOTAL = PAIRS.keySet().stream()
                .mapToInt(v -> (int)v)
                .sum();
    }
    
    private Room createRandomRoom(){
        Room randomizedRoom = new Room("Random room " + random.nextInt());
        
        int number = random.nextInt(NBR_NOTES_MAX);
        for(int i = 0; i < number; i++){
            List<Integer> notes = new ArrayList<>(Note.getIDs());
            randomizedRoom.addNote(new Note(notes.get(random.nextInt(notes.size()))));
        }
        
        number = random.nextInt(NBR_ITEMS_MAX);
        for(int i = 0; i < number; i++)
            randomizedRoom.addItem(createRandomItem());
        
        return randomizedRoom;
    }
    
    private Item createRandomItem(){
        int luck = random.nextInt(100);
        return luck < 10 ? new ItemBuilder("Iron Sword", "", COMMON, rdmNbr(4000, 6000), rdmNbr(300, 400))
                            .add(new EntityAction(OPPONENT, REMOVE, HEALTH, rdmNbr(2, 5), MODIFICATION))
                            .add(new EntityAction(SELF, REMOVE, STAMINA, rdmNbr(1, 2), MODIFICATION)).get():
                
               /*else...*/ new ItemBuilder("Bread", "", COMMON, rdmNbr(100, 300), 1)
                            .add(new EntityAction(SELF, ADD, HEALTH, rdmNbr(1, 5), MODIFICATION)).get();
    }
    
    private Entity createRandomEntity(){
        List<Location> available = new ArrayList<>(rooms.keySet());
        Location l = available.get(random.nextInt(available.size()));
        
        int luck = random.nextInt(100);
        return luck < 5 ? new SimpleAI("Miner", rdmNbr(45, 60), rdmNbr(10, 15), rdmNbr(20, 30), rdmNbr(50000, 100000), l)
                            .addItem(new ItemBuilder("Pickaxe", "", EPIC, 10000, 1000)
                                .add(new EntityAction(OPPONENT, REMOVE, HEALTH, 10, MODIFICATION)).get()):
                           new SimpleAI("Zombie", rdmNbr(10, 30), rdmNbr(5, 10), rdmNbr(5, 10), rdmNbr(30000, 50000), l)
                            .addItem(createRandomItem())
                            .addItem(createRandomItem())
                            .addItem(createRandomItem())
                            .addItem(new ItemBuilder("Bat", "", COMMON, 15, 300)
                                .add(new EntityAction(OPPONENT, REMOVE, HEALTH, 1, MODIFICATION)).get());
    }
    
    private int rdmNbr(int min, int max){
        return random.nextInt(max - min) + min;
    }
    
    private Link createRandomLink(Room[] rooms){
        int luck = random.nextInt(100);
        return luck < 75 ? new Opening(rooms):
                           new Door(rooms);
    }
    
    private Pair<Room, Link> lockedDoor(Room[] rooms){
        
        int key = abs(random.nextInt(Integer.MAX_VALUE-1)+1);
        Room room2 = createRandomRoom();
        Item item = new Item("Key", "", RARE, 1, 1);
        item.setId(key);
        room2.getItems().add(item);
        
        return new Pair<>(room2, new KeyDoor(key, rooms));
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
