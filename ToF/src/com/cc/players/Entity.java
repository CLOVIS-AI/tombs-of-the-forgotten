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
import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
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

/**
 * A class that represents an Entity.
 *
 * @author Ivan Canet
 */
public abstract class Entity implements Timable, Save<JsonObject> {

    /**
     * Health bar. Game over when 0.
     */
    private Bar health;

    /**
     * Strength bar. Used for physical attacks.
     */
    private Bar stamina;

    /**
     * Mana bar. Used for magical attacks.
     */
    private Bar mana;

    private Location location;
    private World world;

    private Optional<Entity> opponent;

    private final Inventory inventory;

    /**
     * Constructs a new Entity.
     * @param maxHealth the maximum health
     * @param maxStrength the maximum strength
     * @param maxMana the maximum mana
     * @param maxWeight the maximum weight it can carry
     */
    public Entity(int maxHealth, int maxStrength, int maxMana, int maxWeight) {
        this(maxHealth, maxStrength, maxMana, maxWeight, new Location());
    }

    /**
     * Constructs a new Entity.
     * @param maxHealth the maximum health
     * @param maxStrength the maximum strength
     * @param maxMana the maximum mana
     * @param maxWeight the maximum weight it can carry
     * @param l its position in the world
     */
    public Entity(int maxHealth, int maxStrength, int maxMana, int maxWeight,
            Location l) {
        
        this(new Bar("Health", 0, maxHealth, maxHealth),
             new Bar("Stamina", 0, maxStrength, maxStrength),
             new Bar("Mana", 0, maxMana, 0),
             l,
             null,
             new Inventory("Inventory", maxWeight));
    }
    
    /**
     * Constructs a new Entity
     * @param health its health
     * @param stamina its stamina
     * @param mana its mana
     * @param location its location in the World
     * @param opponent its opponent if any (or {@code null})
     * @param inventory its inventory
     */
    public Entity(Bar health, Bar stamina, Bar mana, Location location, 
            Entity opponent, Inventory inventory) {
        this.health = new Bar(health);
        this.stamina = new Bar(stamina);
        this.mana = new Bar(mana);
        this.location = location;
        this.opponent = Optional.ofNullable(opponent);
        this.inventory = inventory;
    }
    
    /**
     * Creates an Entity from a JSON object.
     * @param json the saved data
     */
    public Entity(JsonObject json) {
        this(new Bar(json.get("health").asObject()),
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
        if(n >= 0)
            health.add(n, ACCEPT);
        else
            hurt(n);
    }

    /**
     * Decreases the health of the entity.
     *
     * @param n how much this should hurt
     * @throws IllegalArgumentException for negative values
     */
    public final void hurt(int n) {
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
        health.nextTick();
        stamina.nextTick();
        mana.nextTick();
        
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
        if(amount >= 0)
            stamina.add(amount, ACCEPT);
        else
            useStamina(amount);
    }
    
    /**
     * Uses stamina.
     *
     * @param value how much stamina is lost
     * @see #addStamina(int) Add stamina
     */
    public void useStamina(int value) {
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
        if(amount >= 0)
            mana.add(amount, ACCEPT);
        else
            useMana(amount);
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
        return getCurrentRoom().canMove(d);
    }

    /**
     * Moves this Entity in that Direction (if allowed).
     *
     * @param d the direction
     * @throws IllegalStateException if the Entity cannot move in that
     * Direction.
     */
    public void moveTo(Direction d) {
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
        opponent = findHere();
    }
    
    private Optional<Entity> findHere(){
        return world
                .getEntities()
                .filter(e -> e.getLocation().equals(location))
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
        moveTo(getCurrentRoom().getDirectionTo(r).orElseThrow(
                ()->new IllegalArgumentException("The given room ("
                        +r.getLocation()+") is not in a distance of 1 from the "
                        +"Entity ("+getLocation()+")")
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
        return inventory.canAdd(item);
    }

    /**
     * Adds an item to the inventorty.
     *
     * @param item An item.
     * @see Inventory#add(com.cc.items.Item) add
     */
    public void addItem(Item item) {
        inventory.add(item);
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
        if (hasItem(item)) {
            inventory.remove(item);
        } else {
            throw new IllegalArgumentException("You can't throw away an item you"
                    + "don't possess: " + item);
        }
    }
    
    /**
     * Drop an item from the player's inventory to the current room.
     * @param item An item
     */
    public void dropItem(Item item) {
        this.removeItem(item);
        this.getCurrentRoom().getItems().add(item);
    }
    
    /**
     * The player's inventory.
     * @return The player's inventory.
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * The world this entity is in.
     *
     * @return The world this entity is in.
     */
    protected final World getWorld() {
        return world;
    }

    @Override
    public JsonObject save() {
        return new JsonObject()
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
}
