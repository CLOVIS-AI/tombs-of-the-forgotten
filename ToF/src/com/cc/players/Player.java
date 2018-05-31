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

import com.cc.items.Item;
import com.cc.tof.ToF;
import com.cc.utils.messages.Message;
import com.cc.world.Direction;
import com.eclipsesource.json.JsonObject;


/**
 * A Player.
 * @author Ivan Canet
 */
public class Player extends Entity {
        
    private int searchLuck;
    
    /**
     * Creates a default player with default values.
     */
    public Player(){
        this("Player", 20, 2, 5, 10);
    }
    
    /**
     * Creates a player.
     * @param name Its name
     * @param maxHealth The maximum health
     * @param maxStrength The maximum strength
     * @param maxMana The maximum mana
     * @param maxWeight The maximum weight he can carry
     */
    public Player(String name, int maxHealth, int maxStrength, int maxMana, int maxWeight) {
        this(name, maxHealth, maxStrength, maxMana, maxWeight, 0);
    }
    
    /**
     * Creates a player.
     * @param name Its name
     * @param maxHealth The maximum health
     * @param maxStrength The maximum strength
     * @param maxMana The maximum mana
     * @param maxWeight The maximum weight he can carry
     * @param luck The luck of finding a good item
     */
    public Player(String name, int maxHealth, int maxStrength, int maxMana, int maxWeight,
            int luck) {
        super(name, maxHealth, maxStrength, maxMana, maxWeight);
        searchLuck = luck;
    }
    
    /**
     * Creates a player from JSON.
     * @param json the saved data
     */
    public Player(JsonObject json) {
        super(json);
        searchLuck = json.getInt("luck", 0);
    }
    
    @Override
    public void moveTo(Direction d) {
        super.moveTo(d);
        super.getWorld().newMessage(new Message().add("You go " + d.toString()));
        super.getCurrentRoom().explore();
        
        if(isFighting())
            getWorld().newMessage(new Message().add("You have an opponent!"));
    }
    
    public void use(Item item) {
        getInventory().use(item, this);
    }
    
    @Override
    public void nextTick() {

        getOpponent()
                .filter(Entity::isDead)
                .ifPresent(ToF::opponentDied);
        
        super.nextTick();
    }
    
    @Override
    public JsonObject save() {
        return super.save()
                .add("luck", searchLuck);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.searchLuck;
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
        final Player other = (Player) obj;
        if (this.searchLuck != other.searchLuck) {
            return false;
        }
        return true;
    }
    
    /**
     * Replenishes player's stamina per tick.
     */
    public void restATick() {
        addStamina(2);
    }
    
}
