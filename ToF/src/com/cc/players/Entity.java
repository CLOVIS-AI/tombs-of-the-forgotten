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
package com.cc.players;

import com.cc.items.Inventory;
import com.cc.items.Item;
import static com.cc.players.Entity.Stat.HEALTH;
import static com.cc.players.Entity.Stat.MANA;
import static com.cc.players.Entity.Stat.PODS;
import static com.cc.players.Entity.Stat.STAMINA;
import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
import com.cc.utils.EventBar;
import com.cc.utils.Save;
import com.cc.world.Direction;
import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.Timable;
import com.cc.world.World;
import com.eclipsesource.json.JsonObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * A class that represents an Entity.
 *
 * @author Ivan Canet
 */
public abstract class Entity implements Timable, Save<JsonObject> {

    /**
     * Health bar. Game over when 0.
     */
    private final EventBar health;

    /**
     * Strength bar. Used for physical attacks.
     */
    private final EventBar stamina;

    /**
     * Mana bar. Used for magical attacks.
     */
    private final EventBar mana;
    
    private Location location;
    private World world;
    
    private final String name;

    private Optional<Entity> opponent;

    private final Inventory inventory;

    /**
     * Constructs a new Entity.
     * @param name its name
     * @param maxHealth the maximum health
     * @param maxStrength the maximum strength
     * @param maxMana the maximum mana
     * @param maxWeight the maximum weight it can carry
     */
    public Entity(String name, int maxHealth, int maxStrength, int maxMana, int maxWeight) {
        this(name, maxHealth, maxStrength, maxMana, maxWeight, new Location());
    }

    /**
     * Constructs a new Entity.
     * @param name its name
     * @param maxHealth the maximum health
     * @param maxStrength the maximum strength
     * @param maxMana the maximum mana
     * @param maxWeight the maximum weight it can carry
     * @param l its position in the world
     */
    public Entity(String name, int maxHealth, int maxStrength, int maxMana, int maxWeight,
            Location l) {
        
        this(name,
             new Bar("Health", 0, maxHealth, maxHealth),
             new Bar("Stamina", 0, maxStrength, maxStrength),
             new Bar("Mana", 0, maxMana, 0),
             l,
             null,
             new Inventory("Inventory", maxWeight));
    }
    
    /**
     * Constructs a new Entity
     * @param name its name
     * @param health its health
     * @param stamina its stamina
     * @param mana its mana
     * @param location its location in the World
     * @param opponent its opponent if any (or {@code null})
     * @param inventory its inventory
     */
    public Entity(String name, Bar health, Bar stamina, Bar mana, Location location, 
            Entity opponent, Inventory inventory) {
        this.name = name;
        this.health = new EventBar(health)
                .setOnBonusUpdate(t -> inventory.applyWears(HEALTH, t));
        this.stamina = new EventBar(stamina)
                .setOnBonusUpdate(t -> inventory.applyWears(STAMINA, t));
        this.mana = new EventBar(mana)
                .setOnBonusUpdate(t -> inventory.applyWears(MANA, t));
        this.location = location;
        this.opponent = Optional.ofNullable(opponent);
        this.inventory = inventory;
        this.inventory.getWeightBar()
                .setOnBonusUpdate(t -> inventory.applyWears(PODS, t));
                
    }
    
    /**
     * Creates an Entity from a JSON object.
     * @param json the saved data
     */
    public Entity(JsonObject json) {
        this(json.getString("name", null),
             new Bar(json.get("health").asObject()),
             new Bar(json.get("stamina").asObject()),
             new Bar(json.get("mana").asObject()),
             new Location(json.get("location").asObject()),
             null,
             new Inventory(json.get("inventory").asObject()));
    }

    /**
     * Set the world.
     *
     * @param w The world.
     */
    public void setWorld(World w) {
        if (world != null) {
            throw new IllegalStateException("This method can only be called once.");
        }

        world = w;
    }

    /**
     * Increases the health of the entity.
     * @param n how much this should heal. Negative values are allowed (to hurt
     * the entity)
     */
    public final void heal(int n){
        if(isDead())
            return;
        
        health.add(n, ACCEPT);
    }

    /**
     * Decreases the health of the entity.
     *
     * @param n how much this should hurt
     * @throws IllegalArgumentException for negative values
     */
    public final void hurt(int n) {
        if(isDead())
            return;
        
        health.remove(n, ACCEPT);
    }
    
    /**
     * Is this entity dead?
     * @return {@code true} if the entity is dead.
     */
    public final boolean isDead() {
        return health.getCurrent() <= 0;
    }

    /**
     * The location of this entity.
     *
     * @return The location of this entity.
     */
    public final Location getLocation() {
        return location;
    }

    @Override
    public void nextTick() {
        if(isDead())
            return;
        
        getBars().forEach(Bar::nextTick);
        
        mana.add(1, ACCEPT);
        
        opponent = findHere();
        
        if(opponent.isPresent())
            if(opponent.get().isDead())
                opponent = Optional.empty();
    }
    
    /**
     * Is this entity currently fighting?
     * @return {@code true} if this entity is fighting.
     */
    public boolean isFighting() {
        return opponent.isPresent();
    }

    /**
     * Gets the last opponent of this entity.
     *
     * @return The last opponent of this entity.
     */
    public Optional<Entity> getOpponent() {
        return opponent;
    }

    /**
     * The stamina level of the player.
     *
     * @return The stamina of the player.
     */
    public int getStamina() {
        return stamina.getCurrent();
    }

    /**
     * Adds stamina to this entity.
     * @param amount the amount of stamina added (can be negative to remove stamina)
     * @see #useStamina(int) Remove stamina
     */
    public void addStamina(int amount){
        if(isDead())
            return;
        
        stamina.add(amount, ACCEPT);
    }
    
    /**
     * Uses stamina.
     *
     * @param value how much stamina is lost
     * @see #addStamina(int) Add stamina
     */
    public void useStamina(int value) {
        if(isDead())
            return;
        
        stamina.remove(value, ACCEPT);
    }

    /**
     * Gets the mana amount of the player.
     *
     * @return The mana of the player.
     */
    public int getMana() {
        return mana.getCurrent();
    }

    /**
     * Adds mana to this entity.
     * @param amount the amount of mana added (can be negative to remove mana)
     * @see #useMana(int) Remove mana
     */
    public void addMana(int amount){
        mana.add(amount, ACCEPT);
    }
    
    /**
     * Uses mana.
     *
     * @param amount the amount to remove
     * @see #addMana(int) Add mana
     */
    public void useMana(int amount) {
        mana.remove(amount, ACCEPT);
    }

    /**
     * The Room where the Entity is.
     *
     * @return The Room where the Entity is.
     * @see #getCurrentRoom() Without Optional
     */
    public Optional<Room> getCurrentRoomOptional() {
        return world.getRoom(location);
    }

    /**
     * The Room where the Entity is.
     *
     * @return The Room where the Entity is.
     * @see #getCurrentRoomOptional() Using an Optional instead of an exception.
     * @throws IllegalStateException if no Room is found
     */
    public Room getCurrentRoom() {
        return getCurrentRoomOptional()
                .orElseThrow(() -> new IllegalStateException("No room was found"
                + " for this entity (" + this + ") !"));
    }

    /**
     * Can the Entity move in that Direction?
     *
     * @param d the direction
     * @return Whether the Entity can move in that Direction, according to
     * {@link Room#canMove(com.cc.world.Direction) Room.canMove(Direction)}.
     */
    public boolean canMoveTo(Direction d) {
        if(isDead())
            return false;
        
        return getCurrentRoom().canMove(d);
    }
    
    /**
     * Can the entity reach that Direction?
     * 
     * @param d the direction
     * @return Whether the Entity can move in that Direction, according to
     * {@link Room#canReach(com.cc.world.Direction, com.cc.players.Entity) Room.canReach(Direction,Entity)}.
     */
    public boolean canReach(Direction d) {
        if(isDead())
            return false;
        
        return getCurrentRoom().canReach(d, this);
    }

    /**
     * Moves this Entity in that Direction (if allowed).
     *
     * @param d the direction
     * @throws IllegalStateException if the Entity cannot move in that
     * Direction.
     */
    public void moveTo(Direction d) {
        if(isDead())
            return;
        
        Room crt = getCurrentRoom();
        if (!crt.canMove(d) && crt.canOpen(d, this))
            crt.open(d, this);
        
        if (!canMoveTo(d)) {
            throw new IllegalStateException("This Entity (" + this + ") located in "
                    + location + " cannot move in that Direction (" + d + ")");
        }

        location = getCurrentRoom()
                .getNeighbor(d)
                .orElseThrow(() -> new RuntimeException("The call of"
                + " Entity#canMoveTo passed but no neighbor was found "
                + "by Room#getNeighbor, this shouldn't ever happen."))
                .getLocation();

        stamina.remove(1, ACCEPT);
    }
    
    private Optional<Entity> findHere(){
        return world
                .getEntities(true)
                .filter(e -> !e.equals(this) && e.getLocation().equals(location))
                .findAny();
    }
    
    /**
     * Moves this Entity to that Room, only is possible (the Room must be a
     * neighbor of the Entity's current room, and must be reachable by the
     * Entity (see {@link #canMoveTo(com.cc.world.Room) })).
     *
     * @param r the Room
     */
    public void moveTo(Room r) {
        if(isDead())
            return;
        
        moveTo(getCurrentRoom().getDirectionTo(r).orElseThrow(
                ()->new IllegalArgumentException("The given room ("
                        +r.getLocation()+") is not in a distance of 1 from the "
                        +"Entity '"+this+"'")
        ));
    }

    /**
     * Can the Entity move in one move to that Room?
     *
     * @param r the Room
     * @return Whether the Entity can move to that Room, according to
     * {@link Room#canMove(com.cc.world.Room) Room.canMove(Room)}.
     */
    public boolean canMoveTo(Room r) {
        return getCurrentRoom().canMove(r);
    }

    /**
     * Can the item be added to the inventory?
     *
     * @param item An item.
     * @return Whether one player is strong enough to take this item.
     * @see Inventory#canAdd(com.cc.items.Item) canAdd
     */
    public boolean canAddItem(Item item) {
        if(isDead())
            return false;
        
        return inventory.canAdd(item);
    }

    /**
     * Adds an item to the inventorty.
     *
     * @param item An item.
     * @return This entity itself, to allow method-chaining. 
     * @see Inventory#add(com.cc.items.Item) add
     */
    public Entity addItem(Item item) {
        if(isDead())
            return this;
        
        inventory.add(item);
        getBars().forEach(Bar::updateBonus);
        
        return this;
    }
    
    /**
     * Drops an item.
     * @param item The item.
     */
    public void drop(Item item) {
        if(isDead())
            return;
        
        inventory.remove(item);
        getCurrentRoom().addItem(item);
    }

    /**
     * How much item can be added to the inventory without exceeding the weight
     * amount.
     *
     * @param items An item.
     * @return The items that couldn't be added. If none, an empty list is
     * returned.
     * @see Inventory#addIfPossible(java.util.Collection) addIfPossible
     */
    public List<Item> addItemIfPossible(Collection<Item> items) {
        if(isDead())
            return new ArrayList<>(items);
        
        return inventory.addIfPossible(items);
    }

    /**
     * Returns all the bars of this entity.
     *
     * @return The bars of this entity.
     */
    public List<Bar> getBars() {
        ArrayList<Bar> a = new ArrayList<>();
        a.add(health);
        a.add(mana);
        a.add(stamina);
        a.add(inventory.getWeightBar());
        return a;
    }
    
    public Bar getHealthBar() {
        return health;
    }
    
    public Bar getManaBar() {
        return mana;
    }
    
    public Bar getStaminaBar() {
        return stamina;
    }
    
    public Bar getWeightBar() {
        return inventory.getWeightBar();
    }

    /**
     * Verifies if a specific item is contained in the player's inventory.
     * @param item An item.
     * @return {@code true} if the item is contained in an inventory.
     * {@code false} otherwise.
     */
    public boolean hasItem(Item item) {
        return inventory.contains(item);
    }

    /**
     * Removes an item from the player's inventory.
     * @param item An item
     * @throws IllegalArgumentException if the player doesn't have this item in
     * his inventory.
     */
    public void removeItem(Item item) {
        if(isDead())
            return;
        
        if (hasItem(item)) {
            inventory.remove(item);
            getBars().forEach(Bar::updateBonus);
        } else {
            throw new IllegalArgumentException("You can't throw away an item you"
                    + "don't possess: " + item);
        }
    }
    
    /**
     * The player's inventory.
     * @return The player's inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }
    
    public Stream<Item> selectUsableItems() {
        return inventory.stream()
                .filter(i -> i.canUse(this));
    }

    /**
     * The world this entity is in.
     *
     * @return The world this entity is in.
     */
    protected final World getWorld() {
        return world;
    }

    /**
     * The name of the entity.
     * @return The name of the entity.
     */
    public String getName() {
        return name;
    }

    @Override
    public JsonObject save() {
        return new JsonObject()
                .add("name",        name)
                .add("health",      health.save())
                .add("stamina",     stamina.save())
                .add("mana",        mana.save())
                .add("location",    location.save())
                .add("inventory",   inventory.save());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.health);
        hash = 53 * hash + Objects.hashCode(this.stamina);
        hash = 53 * hash + Objects.hashCode(this.mana);
        hash = 53 * hash + Objects.hashCode(this.location);
        hash = 53 * hash + Objects.hashCode(this.inventory);
        hash = 53 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entity other = (Entity) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.health, other.health)) {
            return false;
        }
        if (!Objects.equals(this.stamina, other.stamina)) {
            return false;
        }
        if (!Objects.equals(this.mana, other.mana)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        if (!Objects.equals(this.inventory, other.inventory)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString(){
        return name + (isDead() ? " [DEAD] " : " ") + location;
    }
    
    /**
     * Represents a statistic of an Entity, such as its health.
     */
    public enum Stat {
        /** The mana of an Entity. */
        MANA(e -> e.mana),
        /** The health of an Entity. */
        HEALTH(e -> e.health),
        /** The stamina of an Entity. */
        STAMINA(e -> e.stamina),
        /** The pods of an Entity. */
        PODS(e -> e.inventory.getWeightBar());
        
        private final Function<Entity, Bar> bar;
        Stat(Function<Entity, Bar> bar){
            this.bar = bar;
        }
        
        /**
         * Increments or decrements a stat.
         * @param e the entity that is affected
         * @param value by how much the value is incremented (negative values 
         * for decrements)
         */
        public void increment(Entity e, int value){
            bar.apply(e).add(value, ACCEPT);
        }
        
        /**
         * Checks if the stat can be incremented or decremented.
         * @param e the entity that is affected
         * @param value by how much the value is incremented (negative values
         * for decrements)
         * @return {@code true} if the stat can be incremented/decremented.
         */
        public boolean canIncrement(Entity e, int value){
            return bar.apply(e).canAdd(value);
        }
        
        /**
         * Adds a bonus to the entity
         * @param e the entity that is affected
         * @param turns how many turns the bonus will last
         * @param value by how much the bonus is affected
         */
        public void newBonus(Entity e, int turns, int value){
            bar.apply(e).addBonus(turns, value, true);
        }
        
        @Override
        public String toString(){
            return name().substring(0, 1) + name().substring(1).toLowerCase();
        }
    }
}
