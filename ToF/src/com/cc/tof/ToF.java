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

import com.cc.players.Entity;
import com.cc.utils.messages.Message;
import com.cc.view.InterfaceController;
import com.cc.view.View;
import com.cc.world.World;
import com.cc.world.generator.DefaultGenerator;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.swing.JFileChooser;

/**
 * The main game interface
 *
 * @author Ivan Canet
 */
public class ToF extends Application {

    public static World world;
    private static final long TIME_TURN = 1;
    private static Stage stage;
    
    private static File SAVE_DIR;

    static void gameTick() {
        if (world == null) {
            throw new IllegalStateException("This method should not be called "
                    + "when no world is loaded.");
        }

        world.nextTick();
    }

    public static World getWorld() {
        return world;
    }
    
    public static void opponentDied(Entity opponent) {
        InterfaceController.lootPopup(opponent.getInventory(), false);
    }

    /**
     * The player rests.
     *
     * @param turns number of turns
     */
    @SuppressWarnings("SleepWhileInLoop")
    public static void rest(int turns) {
        if (turns <= 0) {
            throw new IllegalArgumentException("'turns' shouldn't be negative "
                    + "or null: " + turns);
        }

        world.newMessage(new Message()
                .add("You are sleeping for " + turns + " turns.")
        );

        for (int i = 0; i < turns; i++) {
            world.getPlayer().restATick();
            world.nextTick();
        }
    }

    /**
     * Gets a resource of the project. Resources should be located in the
     * 'ToF/resources' directory.
     *
     * @param name the name of the file (including extension).
     * @return The specified resource.
     */
    public static URL getResource(String name) {
        Enumeration<URL> enm;
        try {
            enm = new ToF().getClass().getClassLoader().getResources(name);
            URL ret = enm.nextElement();
            println("Data", "Using resource " + ret);
            return ret;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Cannot open resource '" + name + "'", ex);
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Found no resource of the name '" + name + "'", ex);
        }
    }
    
    /**
     * Creates a Stream of the lines in the resource.
     * <p>This method is not optimized for big files (the full file is read into
     * the memory before being returned).
     * @param name the name of the resource
     * @return A Stream of each line in the resource.
     */
    public static Stream<String> getResourceByLine(String name) {
        try {
            print("Data", "Opening resource '"+name+"'...");
            BufferedReader reader;
            {
                ClassLoader loader = new ToF().getClass().getClassLoader();
                InputStream input = loader.getResourceAsStream(name);
                InputStreamReader in = new InputStreamReader(input, StandardCharsets.UTF_8);
                reader = new BufferedReader(in);
            }
            println("reading...");
            return reader.lines();
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Found no resource of the name '"+name+"'", ex);
        }
    }
    
    /**
     * Creates a new game and prints a few stats about it.
     */
    public static void newGame() {
        long time = System.currentTimeMillis();

        DefaultGenerator gen = new DefaultGenerator();
        world = gen.generate();

        time -= System.currentTimeMillis();

        println("ToF", "A new game was successfully created! Stats:");
        println("ToF", "Time: " + (-time) + " ms");
        println("ToF", "Number of rooms: " + world.getRooms().size());
        println("ToF", "Average of neighbors: " + world.getRooms().stream()
                .mapToLong(r -> r.getAllLinks().count())
                .average()
                .getAsDouble()
        );
        println("ToF", "Average of items/room: " + world.getRooms().stream()
                .mapToInt(r -> r.getItems().getItems().size())
                .average()
                .getAsDouble());
        println("ToF", "Number of entities (player excluded): " + world.getEntities(false).count());
    }

    /**
     * Loads the game from a save file.
     * @return {@code true} if the loading was successful.
     */
    public static boolean load() {
        File file = ToF.selectFile("Load", SAVE_DIR);
        println("Load", "The user selected: " + file.getAbsolutePath());
        try {
            println("Load", "Reading the file...");
            byte[] encoded = Files.readAllBytes(file.toPath());
            
            println("Load", "Parsing the JSON...");
            JsonObject json = Json.parse(
                    new String(encoded, StandardCharsets.UTF_8)).asObject();
            
            print("Load", "Instantiating the world...");
            world = new World(json);
            done();
            return true;
        } catch (IOException ex) {
            if(ex instanceof NoSuchFileException)
                System.err.println("You canno't load a file that does not exist.");
            else
                throw new RuntimeException("Cannot load file", ex);
            return false;
        }
    }

    /**
     * Prompts the user for a file then saves the game into that file.
     */
    public static void save() {
        File file = ToF.selectFile("Save", SAVE_DIR);
        println("Save", "The user selected: " + file.getAbsolutePath());
        
        println("Save", "Generating the save of the game...");
        JsonObject json = world.save();
        String str = json.toString(WriterConfig.PRETTY_PRINT);
        println("Save", "Save is ready, opening the file...");
        try {
            FileWriter fw = new FileWriter(file);
            if (!file.exists()) {
                file.createNewFile();
                println("Save", "Created the file.");
            }
            Writer writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            
            print("Save", "Writing in the file...");
            bw.write(str);
            bw.close();
            done();
        } catch (IOException ex) {
            throw new RuntimeException("Cannot open file", ex);
        }
    }
    
    public static boolean win = false;
    public static boolean lose = false;
    
    public static void win(){
        println("ToF", "You win!");
        win = true;
    }
    
    public static void lose(){
        println("ToF", "You loose...");
        lose = true;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        View view = new View(this, primaryStage);
        ToF.stage = primaryStage;
        ToF.SAVE_DIR = getRootDir("saves");
        println("File", "Saves are stored in: " + SAVE_DIR.getAbsolutePath());
        
        println("ToF", "Loading general menu...");
        Parent menu = FXMLLoader.load(getResource("Menu.fxml"));
        println("ToF", "Menu loaded.");
        
        println("ToF", "Creating main scene...");
        Scene scene = new Scene(menu, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tombs of the Forgotten");
        println("ToF", "Launching the game.");
        primaryStage.show();
    }
    
    /**
     * The stage of the game.
     * @return The stage of the game.
     */
    public static Stage getStage(){
        return stage;
    }
    
    /**
     * Prompts a window asking to select a file to the user.
     * @param message what is written in the button (open...)
     * @param pos the position of the directory
     * @return A file object of what the user chose
     */
    public static File selectFile(String message, File pos){
        if(!pos.exists()){
            println("File", "Creating folder " + pos.getAbsolutePath());
            pos.mkdir();
        }
        
        println("File", "Opening prompt in " + pos.getAbsolutePath());
        JFileChooser jfc = new JFileChooser(pos);
        jfc.showDialog(null, message);
        jfc.setVisible(true);
        return jfc.getSelectedFile();
    }
    
    /**
     * Gets the root directory of the project.
     * @return The root directory of the project.
     * @see #getRootDir(java.lang.String) A sub-directory of the root directory
     */
    public static File getRoot(){
        // Inspired from "Get the root of an application"
        // http://www.rgagnon.com/javadetails/java-0581.html
        URL u = new ToF().getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            return new File(u.toURI()).getParentFile().getParentFile();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException("Could not get the root dir", ex);
        }
    }
    
    /**
     * Gets a directory in the root directory of the project.
     * @param name the name of the directory
     * @return A file object representing the directory.
     * @see #getRoot() The root directory
     */
    public static File getRootDir(String name){
        return new File(getRoot(), name);
    }
    
    private static String lastId = "";
    public static void println(String id, String text){
        if(lastId != id){
            lastId = id;
            System.out.println("[" + id + "]\t" + text);
        }else
            System.out.println("\t" + text);
    }
    public static void print(String id, String text){
        if(lastId != id){
            lastId = id;
            System.out.print("[" + id + "]\t" + text);
        }else
            System.out.print("\t" + text);
    }
    public static void println(String text){
        System.out.println(" " + text);
    }
    public static void done(){
        println("[Done]");
    }

}
