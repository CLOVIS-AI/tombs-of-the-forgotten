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
package com.cc.tof;

import com.cc.javafx.View;
import com.cc.players.Player;
import com.cc.world.Direction;
import com.cc.world.Location;
import com.cc.world.Room;
import com.cc.world.World;
import java.net.URL;
import java.util.Scanner;
import java.util.TreeMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The main game interface
 * @author Ivan Canet
 */
public class ToF extends Application {
    
    private static World world;
    private Parent menu;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
        System.out.println("Tomb of the Forgotten Memory\n"
                + "PROTOTYPE\n"
                + "Available commands:\n"
                + "\tmove [north|south|up|down|east|west]\n"
                + "\texit\n");
        
        TreeMap<Location, Room> rooms = new TreeMap<>();
        rooms.put(new Location(0, 0, 0), new Room(""));
        rooms.put(new Location(1, 0, 0), new Room(""));
        rooms.put(new Location(0, 1, 0), new Room(""));
        rooms.put(new Location(1, 0, 0), new Room(""));
        rooms.put(new Location(1, 2, 0), new Room(""));
        rooms.put(new Location(0, 2, 0), new Room(""));
        rooms.put(new Location(2, 2, 0), new Room(""));
        
        Player p = new Player("p", 1, 1, 1, 1);
        
        world = new World(rooms, p);
        gameLoop();
    }
    
    static void gameLoop(){
        while(true){
            System.out.println("\n");
            Action input = analyseInput(getInput());
            switch(input.getCommand()){
                case "move":
                    Direction d = Direction.valueOf(input.getParameters()[0].toUpperCase());
                    world.movePlayer(d);
                    break;
                case "exit":
                    System.exit(0);
                    break;
            }
            gameTick();
            System.out.println(world.floorToString(0));
        }
    }
    
    static void gameTick(){
        if(world == null)
            throw new IllegalStateException("This method should not be called "
                    + "when no world is loaded.");
        
        world.nextTick();
    }
    
    /**
     * The player rests.
     * @param turns number of turns
     * @param time amount of time 
     */
    @SuppressWarnings("SleepWhileInLoop")
    public void rest(int turns, long time) {
        if(turns <= 0)
            throw new IllegalArgumentException("'turns' shouldn't be negative "
                    + "or null: " + turns);
        if(time <= 0)
            throw new IllegalArgumentException("'time' shouldn't be negative or"
                    + " null: " + time);
        
        try {
            for (int i = 0; i < turns; i++) {
                world.getPlayer().restATick();
                world.nextTick();
                Thread.sleep(time);
            }
        } catch (InterruptedException ex) {}
    }
    
    /**
     * Prompts the user for input.
     * @return The input
     */
    public static String getInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your command : ");
        String input = scanner.nextLine();
        return input;
    }
    
    /**
     * Parses the input to get the different parts.
     * @param input the input
     * @return An action object
     */
    public static Action analyseInput(String input) {
        String[] s = input.split(" ");
        String[] parameters = new String[s.length - 1];
        
        for (int i = 0 ; i < s.length - 1 ; i++ ) {
            parameters[i] = s[i+1];
        }
        
        return new Action(s[0], parameters);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View(this, primaryStage);
        
        URL url = getClass().getClassLoader().getResources("Menu.fxml").nextElement();
        System.out.println("Loading FXML from: " + url);
        menu = FXMLLoader.load(url);
        
        Scene scene = new Scene(menu, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tomb Of The Forgotten");
        primaryStage.show();
    }
    
}
