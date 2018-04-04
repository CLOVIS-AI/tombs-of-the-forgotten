/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.tof;

import static com.cc.world.GameState.EXPLORE;
import com.cc.world.World;
import java.util.Scanner;


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
            Action input = commands(getInput());
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
    
    public static String getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your command : ");
        String input = scanner.next();
        return input;
    }
    
    public static Action commands(String input){
        String[] s = input.split(" ");
        String[] parameters = new String[s.length - 1];
        
        for (int i = 0 ; i < s.length - 1 ; i++ ) {
            parameters[i] = s[i+1];
        }
        
        return new Action(s[0], parameters);
    }
    
}
