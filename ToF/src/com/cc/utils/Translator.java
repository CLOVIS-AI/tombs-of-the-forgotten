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
package com.cc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

/**
 * Class used to handle translations.
 * @author Ivan Canet
 */
public enum Translator {
    LINES(() -> loadLines("generic", ".txt"));
    
    Supplier<Map<String, String>> loader;
    
    Map<String, String> lines;
    
    /**
     * Creates a Translator.
     * @param loader how to load the translations
     */
    Translator(Supplier<Map<String, String>> loader){
        this.loader = loader;
    }
    
    private void load(){
        lines = loader.get();
    }
    
    /**
     * Retreives the translation of a line.
     * @param token the ID of the line.
     * @return The translation.
     */
    public String get(String token){
        if(lines == null)
            load();
        return lines.get(token.toUpperCase());
    }
    
    /**
     * Loads the translations using a simple syntax of line-by-line reading.
     * <p>The first space is used to split the text. Here is an example of the 
     * syntax:
     * <pre>
     *  TOKEN_NAME This is the translation of the line.
     * </pre>
     * @param path the path and name of the file
     * @param extension the extension of the file
     * @return The map of translations
     */
    private static Map<String, String> loadLines(String path, String extension){
        Map<String, String> lines = new HashMap<>();
        
        try {
            Scanner f = new Scanner(new File(path + LANGUAGE + extension), "UTF-8");
            
            while(f.hasNextLine()){
                String line = f.nextLine();
                
                int index = line.indexOf(' ');
                lines.put(line.substring(0, index).toUpperCase(), line.substring(index+1));
            }
        } catch (FileNotFoundException ex) {
            throw new NullPointerException("Cannot read the file: " + path + 
                    "\n" + ex.getLocalizedMessage());
        }
        
        return lines;
    }
    
    static private String LANGUAGE = "EN";
    
    /**
     * Sets the language.
     * <p>Use 2-letters country codes, example:
     * <ul>
     *  <li>EN</li> <li>FR</li> <li>...</li>
     * </ul>
     * @param language The language.
     */
    static public void setLanguage(String language){
        LANGUAGE = language;
    }
    
    /**
     * Reloads all the translators. Use this method if you changed the language
     * after some of the translators were loaded.
     * @deprecated Use {@link #smartReload() } instead.
     * @see #setLanguage(java.lang.String) Set the language
     */
    @Deprecated
    static public void reload(){
        for (Translator translator : values())
            translator.load();
    }
    
    /**
     * Reloads the unloaded translators. Use this method is you changed the 
     * language after some of the translators were loaded.
     * <p>Warning: if any class possesses the result of a translation, its
     * reference will not be modified.
     * @see #setLanguage(java.lang.String) Set the language
     */
    static public void smartReload(){
        for(Translator translator : values())
            if(translator.lines != null)
                translator.load();
        System.gc();
    }
}
