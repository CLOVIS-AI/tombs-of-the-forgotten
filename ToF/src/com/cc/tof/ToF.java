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

import com.cc.utils.messages.Message;
import com.cc.view.View;
import com.cc.world.World;
import com.cc.world.generator.DefaultGenerator;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public static World world;
    private Parent menu;
    private static final long TIME_TURN = 1;
    
    static void gameTick(){
        if(world == null)
            throw new IllegalStateException("This method should not be called "
                    + "when no world is loaded.");
        
        world.nextTick();
    }
    
    public static World getWorld(){
        return world;
    }
    
    /**
     * The player rests.
     * @param turns number of turns 
     */
    @SuppressWarnings("SleepWhileInLoop")
    public static void rest(int turns) {
        if(turns <= 0)
            throw new IllegalArgumentException("'turns' shouldn't be negative "
                    + "or null: " + turns);
        
        world.newMessage(new Message()
            .add("You are sleeping for " + turns + " turns.")
        );
        
        for (int i = 0; i < turns; i++) {
            world.getPlayer().restATick();
            world.nextTick();
        }
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
    
    /**
     * Gets a resource of the project. Resources should be located in the 
     * 'ToF/resources' directory.
     * @param name the name of the file (including extension).
     * @return The specified resource.
     */
    public static URL getResource(String name) {
        Enumeration<URL> enm;
        try {
            enm = new ToF().getClass().getClassLoader().getResources(name);
            URL ret = enm.nextElement();
            System.out.println("Using resource " + ret);
            return ret;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Cannot open resource '"+name+"'", ex);
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Found no resource of the name '"+name+"'", ex);
        }
    }
    
    public static void newGame() {
        long time = System.currentTimeMillis();
        
        DefaultGenerator gen = new DefaultGenerator();
        world = gen.generate();
        
        time -= System.currentTimeMillis();
        
        System.out.println("A new game was successfully created! Stats:");
        System.out.println("> Time: " + (-time) + " ms");
        System.out.println("> Number of rooms: " + world.getRooms().size());
        System.out.println("> Average of neighbors: " + world.getRooms().stream()
                .mapToLong(r -> r.getAllLinks().count())
                .average()
                .getAsDouble()
        );
    }
    
    /**
     * Loads the game from a save file.
     */
    public static void load(File file) {
        try {
            byte[] encoded = Files.readAllBytes(file.toPath());
            JsonObject json = Json.parse (
                    new String(encoded, StandardCharsets.UTF_8)).asObject();
            world = new World(json);
        } catch (IOException ex) {
            Logger.getLogger(ToF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void save(File file) {
        // 1. Générer le JSON
        JsonObject json = world.save();
        // 2. Le convertir en String
        String str = json.toString(WriterConfig.PRETTY_PRINT);
        // 3. ENregistrer le String dans un fichier
        System.out.println(str); //FileWriter
        try {
            FileWriter fw = new FileWriter(file);
            if (!file.exists()) {
                file.createNewFile();
            }
            Writer writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(str);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(ToF.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View(this, primaryStage);
        
        menu = FXMLLoader.load(getResource("interface.fxml"));
        menu.relocate(-255, 0);
        Scene scene = new Scene(menu, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tomb of the Forgotten");
        primaryStage.show();
    }
    
}
