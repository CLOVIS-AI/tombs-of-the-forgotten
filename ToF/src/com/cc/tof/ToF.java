/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.tof;

import static com.cc.world.GameState.EXPLORE;
import com.cc.world.World;

/**
 * The main game interface
 * @author Ivan Canet
 */
public class ToF {
    
    private static World world;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
    static void gameLoop(){
        while(true){
            // TODO: Ask the user
            // TODO: Do what they said
            gameTick();
            System.out.println(world.floorToString(0));
        }
    }
    
    static void gameTick(){
        if(world == null)
            throw new IllegalStateException("This method should not be called "
                    + "when no world is loaded.");
        
        if(world.getGameState() == EXPLORE){
            world.nextTick();
        }else{
            
        }
    }
    
}
