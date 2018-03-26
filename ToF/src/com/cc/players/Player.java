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
    
    public Player(){
        super(20, 2, 5, 10, 0);
    }
    
    public Player(int maxHealth, int maxStrength, int maxMana, int weight, int maxWeight) {
        super(maxHealth, maxStrength, maxMana, weight, maxWeight);
    }
    
}
