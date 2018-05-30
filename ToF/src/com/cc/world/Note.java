/*
 * The MIT License
 *
 * Copyright 2018 Canet Ivan & Chourouq Sarah.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cc.world;

import com.cc.tof.ToF;
import com.cc.utils.Save;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A note is an information that can be found in a Room.
 * @author Ivan Canet
 */
public class Note implements Save<JsonValue> {
    
    private final int ID;
    private final String text;

    private Note(int ID, String text) {
        this.ID = ID;
        this.text = text;
    }
    
    /**
     * Creates a note from an ID.
     * @param ID the ID
     */
    public Note(int ID) {
        this.ID = ID;
        text = notes.get(ID);
        
        if(text == null)
            throw new IllegalArgumentException("No note was found with the ID: " + ID);
    }
    
    /**
     * Loads a note from a JSON value.
     * @param json the saved data
     */
    public Note(JsonValue json){
        this(json.asInt());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.ID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Note other = (Note) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        return true;
    }
    
    @Override
    public JsonValue save() {
        return Json.value(ID);
    }
    
    @Override
    public String toString() {
        return "note #" + ID;
    }
    
    /**
     * Returns the text of this note.
     * @return The text of this note.
     */
    public String print() {
        return text;
    }
    
    // ************************************************************* S T A T I C
    
    private static final Map<Integer, String> notes;
    
    static {
        System.out.println("[Notes]\tLoading...");
        notes = loadNotes();
        System.out.println("[Notes]\tReady.");
    }
    
    static Map<Integer, String> loadNotes(){
        Map<Integer, String> ret = new HashMap<>();
        
        ToF.getResourceByLine("notes.txt")
                .filter(l -> l != null && l.length() != 0)
                .filter(l -> l.charAt(0) != '#')            // Remove comments
                .map(l -> l.split("\\s+", 2))               // Split ID/text
                .filter(l -> l[0].matches("\\A\\d+\\z"))    // Check if ID correct
                .forEach(l -> ret.put(Integer.valueOf(l[0]), l[1]));
        
        return ret;
    }
    
    /**
     * A Set of all the available Note IDs.
     * @return The IDs.
     */
    public static final Set<Integer> getIDs(){
        return notes.keySet();
    }
    
    // *************************************************************** I N N E R
    
    /**
     * An ArrayList overload for Notes, that adds a constructor with the IDs and
     * implements Save.
     */
    public static class Notes extends ArrayList<Note> implements Save<JsonArray> {

        /**
         * Creates a new list of Notes.
         */
        public Notes(){
            super();
        }
        
        /**
         * Creates a new list of Notes from JSON.
         * @param json the saved data
         */
        public Notes(JsonArray json){
            super(json.size());
            json.forEach(v -> add(new Note(v)));
        }
        
        @Override
        public JsonArray save() {
            JsonArray json = new JsonArray();
            super.forEach(n -> json.add(n.save()));
            return json;
        }
        
    }
    
}
