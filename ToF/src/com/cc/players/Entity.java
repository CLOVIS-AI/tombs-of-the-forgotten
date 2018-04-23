/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.players;

import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
import static com.cc.utils.Translator.LINES;
import com.cc.world.Direction;
import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.Timable;
import com.cc.world.World;
import java.util.ArrayList;
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
    
    private Location location;
    private World world;
    
    private Optional<Entity> opponent;
    
    public Entity(int maxHealth, int maxStrength, int maxMana){
        this(maxHealth, maxStrength, maxMana, new Location());
    }
    
    public Entity(int maxHealth, int maxStrength, int maxMana, Location l){
        health = new Bar(LINES.get("health"), 0, maxHealth, maxHealth);
        stamina = new Bar(LINES.get("stamina"), 0, maxStrength, maxStrength);
        mana = new Bar(LINES.get("mana"), 0, maxMana, 0);
        location = l;
        opponent = Optional.empty();
    }
    
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
        return a;
    }
    
}
