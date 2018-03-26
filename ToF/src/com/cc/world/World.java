/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import com.cc.players.Entity;
import com.cc.players.Player;
import java.util.List;
import java.util.Map;

/**
 * The world in which the game is taking place.
 * @author Ivan Canet
 */
public class World {
    
    private Map<Location, Room> rooms;
    
    private Player player;
    
    private List<Entity> entities;
    
    private GameState gameState;
    
}
