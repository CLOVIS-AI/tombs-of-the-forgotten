/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.players;

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
        this(20, 2, 5, 10);
    }
    
    /**
     * Creates a player.
     * @param maxHealth The maximum health
     * @param maxStrength The maximum strength
     * @param maxMana The maximum mana
     * @param maxWeight The maximum weight he can carry
     */
    public Player(int maxHealth, int maxStrength, int maxMana, int maxWeight) {
        super(maxHealth, maxStrength, maxMana, maxWeight);
        searchLuck = 0;        
    }   
}
