/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.players;

import com.cc.utils.Bar;
import static com.cc.utils.Bar.Behavior.ACCEPT;
import static com.cc.utils.Translator.LINES;

/**
 * A class that represents an Entity.
 * @author Ivan Canet
 */
public abstract class Entity {
    
    /** Health bar. Game over when 0. */
    Bar health;
    
    /** Strength bar. Used for physical attacks. */
    Bar strength;
    
    /** Mana bar. Used for magical attacks. */
    Bar mana;
    
    public Entity(int maxHealth, int maxStrength, int maxMana){
        health = new Bar(LINES.get("health"), 0, maxHealth, maxHealth);
        strength = new Bar(LINES.get("strength"), 0, maxStrength, maxStrength);
        mana = new Bar(LINES.get("mana"), 0, maxMana, 0);
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
    
}
