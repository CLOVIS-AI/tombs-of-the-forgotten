/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.tof;

import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.World;
import java.util.TreeMap;

/**
 *
 * @author ivan
 */
public class ToF {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        TreeMap<Location, Room> map = new TreeMap<>();
        map.put(new Location(0, 0, 0), new Room());
        map.put(new Location(1, 1, 0), new Room());
        map.put(new Location(2, 2, 0), new Room());
        map.put(new Location(0, 1, 0), new Room());
        //map.put(new Location(1, 0, 0), new Room());
        World w = new World(map);
        
        map.keySet().forEach(System.out::println);
        
        System.out.println("---");
        System.out.println(w.floorToString(0));
        System.out.println("---");
    }
    
}
