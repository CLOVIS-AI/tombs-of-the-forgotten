/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.players;

import com.cc.items.Item;
import com.cc.items.ItemContainer;
import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
import static com.cc.utils.Translator.LINES;
import com.cc.world.Direction;
import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.Timable;
import com.cc.world.World;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * A class that represents an Entity.
 * @author Ivan Canet
 */
public abstract class Entity implements Timable {
    
    /** Health bar. Game over when 0. */
    private Bar health;
    
    /** Strength bar. Used for physical attacks. */
    private Bar stamina;
    
    /** Mana bar. Used for magical attacks. */
    private Bar mana;
    
    /** Weigth bar. Can't add item into the inventory when full.*/
    private Bar weight;

    
    private Location location;
    private World world;
    
    private Optional<Entity> opponent;
    
    private ItemContainer inventory;
    
    
    public Entity(int maxHealth, int maxStrength, int maxMana, int maxWeight){
        this(maxHealth, maxStrength, maxMana, maxWeight, new Location());
    }
    
    public Entity(int maxHealth, int maxStrength, int maxMana, int maxWeight,
            Location l){
        health = new Bar(LINES.get("health"), 0, maxHealth, maxHealth);
        stamina = new Bar(LINES.get("stamina"), 0, maxStrength, maxStrength);
        mana = new Bar(LINES.get("mana"), 0, maxMana, 0);
        weight = new Bar(LINES.get("weight"), 0, maxWeight, 0);
        
        location = l;
        opponent = Optional.empty();
    }
    
    /**
     * Set the world.
     * @param w The world.
     */
    public void setWorld(World w){
        if(world != null)
            throw new IllegalStateException("This method can only be called once.");
        
        world = w;
    }
    
    /**
     * Increases the health of the entity.
     * @param n how much this should heal
     * @throws IllegalArgumentException for negative values
     */
    public final void heal(int n){
        health.add(n, ACCEPT);
    }
    
    /**
     * Decreases the health of the entity.
     * @param n how much this should hurt
     * @throws IllegalArgumentException for negative values
     */
    public final void hurt(int n){
        health.remove(n, ACCEPT);
    }
    
    /**
     * Moves this entity in a direction.
     * @param d The direction in which the entity moves
     */
    public final void move(Direction d){
        location = location.add(d);
    }
    
    /**
     * The location of this entity.
     * @return The location of this entity.
     */
    public final Location getLocation(){
        return location;
    }

    @Override
    public void nextTick() {
        health.nextTick();
        stamina.nextTick();
        mana.nextTick();
    }
    
    /**
     * Gets the last opponent of this entity.
     * @return The last opponent of this entity.
     */
    public Optional<Entity> getOpponent() {
        return opponent;
    }
    
    /**
     * The stamina level of the player.
     * @return The stamina of the player.
     */
    public int getStamina(){
        return stamina.getCurrent();
    }
    
    /**
     * Uses stamina.
     * @param value how much stamina is lost
     */
    public void useStamina(int value){
        stamina.remove(value, ACCEPT);
    }
    
    /**
     * Gets the mana amount of the player.
     * @return The mana of the player.
     */
    public int getMana(){
        return mana.getCurrent();
    }
    
    /**
     * Uses mana.
     * @param amount the amount to remove
     */
    public void useMana(int amount){
        mana.remove(amount, ACCEPT);
    }
    
    /**
     * Verifies that one player can add a specific item to the inventory (based
     * on the weight one can carry).
     * @param item The item
     * @return Whether one player is strong enough to take this item.
     */
    public boolean canAddItem(Item item) {
        return weight.getCurrent() + item.getWeight() < weight.getMaximum();
    }
    
     /**
     * Adds an item to one's inventory.
     * <p>If the item is too heavy for the player, nothing is done. See 
     * {@link #canAddItem(com.cc.items.Item) canAddItem(Item)}.
     * @param item The item to be added.
     */
    public void addItem(Item item) {
        if (canAddItem(item)) {
            inventory.add(item);
            weight.add(item.getWeight(), ACCEPT);
        }
    }
    
    /**
     * Adds as many items as possible from a Collection of items.
     * <p>An item can be added if its weight is not too much for a player to
     * carry. {@link #canAddItem(com.cc.items.Item) canAddItem(Item)}.
     * @param items a Collection of items.
     * @return The items that couldn't be added. If none, an empty list is returned.
     */
    public List<Item> addItemsIfPossible(Collection<Item> items){
        List<Item> couldnt = new ArrayList<>();
        
        for(Item i : items){
            if(canAddItem(i))
                addItem(i);
            else
                couldnt.add(i);
        }
        
        return couldnt;
    }
    
    /**
     * The Room where the Entity is.
     * @return The Room where the Entity is.
     * @see #getCurrentRoom() Without Optional
     */
    public Optional<Room> getCurrentRoomOptional(){
        return world.getRoom(location);
    }
    
    /**
     * The Room where the Entity is.
     * @return The Room where the Entity is.
     * @see #getCurrentRoomOptional() Using an Optional instead of an exception.
     * @throws IllegalStateException if no Room is found
     */
    public Room getCurrentRoom(){
        return getCurrentRoomOptional()
                .orElseThrow(() -> new IllegalStateException("No room was found"
                        + " for this entity ("+this+") !"));
    }
    
    /**
     * Can the Entity move in that Direction?
     * @param d the direction
     * @return Whether the Entity can move in that Direction, according to 
     *         {@link Room#canMove(com.cc.world.Direction) Room.canMove(Direction)}.
     */
    public boolean canMoveTo(Direction d){
        return getCurrentRoom().canMove(d);
    }
    
    /**
     * Moves this Entity in that Direction (if allowed).
     * @param d the direction
     * @throws IllegalStateException if the Entity cannot move in that Direction.
     */
    public void moveTo(Direction d){
        if(!canMoveTo(d))
            throw new IllegalStateException("This Entity ("+this+") located in "
                    +location+" cannot move in that Direction ("+d+")");
        
        location = getCurrentRoom()
                .getNeighbor(d)
                .orElseThrow(() -> new RuntimeException("The call of"
                        + " Entity#canMoveTo passed but no neighbor was found "
                        + "by Room#getNeighbor, this shouldn't ever happen."))
                .getLocation();
        
        stamina.remove(world.getGameState().MOVING_STAMINA_COST, ACCEPT);
    }
    
    /**
     * Moves this Entity to that Room, only is possible (the Room must be a 
     * neighbor of the Entity's current room, and must be reachable by the 
     * Entity (see {@link #canMoveTo(com.cc.world.Room) })).
     * @param r the Room
     */
    public void moveTo(Room r){
        moveTo(getCurrentRoom().getDirectionTo(r));
    }
    
    /**
     * Can the Entity move in one move to that Room?
     * @param r the Room
     * @return Whether the Entity can move to that Room, according to 
     *         {@link Room#canMove(com.cc.world.Room) Room.canMove(Room)}.
     */
    public boolean canMoveTo(Room r){
        return getCurrentRoom().canMove(r);
    }
    
    /**
     * Returns all the bars of this entity.
     * @return The bars of this entity.
     */
    public List<Bar> getBars(){
        ArrayList<Bar> a = new ArrayList<>();
        a.add(health);
        a.add(mana);
        a.add(stamina);
        a.add(weight);
        return a;
    }
    
}
