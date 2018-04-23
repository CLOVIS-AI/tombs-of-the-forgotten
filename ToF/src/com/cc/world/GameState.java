/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

/**
 * The game state.
 * @author Ivan Canet
 */
public enum GameState {
    
    /**
     * The player is fighting an enemy.
     */
    FIGHT(5),
    
    /**
     * The player is exploring.
     */
    EXPLORE(1);
    
    public final int MOVING_STAMINA_COST;
    
    GameState(int movingStaminaCost){
        MOVING_STAMINA_COST = movingStaminaCost;
    }
    
}
