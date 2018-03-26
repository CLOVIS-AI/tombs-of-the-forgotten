/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

import java.util.Map;

/**
 * An ingame room.
 * @author Ivan Canet
 */
public class Room {
    
    private Map<Direction, Room> neighbors;
    
    private String description;
    
}
