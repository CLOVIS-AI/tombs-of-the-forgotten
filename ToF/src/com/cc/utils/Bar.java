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

import static com.cc.utils.Bar.Behavior.ACCEPT;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Bars are used to represent information about the player (skills, health bar)... 
 * @author Ivan Canet
 */
public class Bar implements Save<JsonObject> {
    
    private int minimum, maximum;
    
    private int real;
    
    private int bonusTotal;
    
    private List<Pair<Integer, Integer>> bonuses;
    
    private final String name;

    /**
     * Creates a Bar.
     * @param name display name of this bar
     * @param minimum minimum value of this bar
     * @param maximum maximum value of this bar
     * @param value beginning value of this bar
     */
    public Bar(String name, int minimum, int maximum, int value) {
        this.name = name;
        this.minimum = minimum;
        this.maximum = maximum;
        this.real = value;
        bonusTotal = 0;
        
        bonuses = new ArrayList<>();
    }
    
    /**
     * Creates a Bar.
     * @param name display name of this bar
     * @param minimum minimum value of this bar
     * @param maximum maximum value of this bar
     */
    public Bar(String name, int minimum, int maximum){
        this(name, minimum, maximum, minimum);
    }
    
    /**
     * Loads a Bar from JSON.
     * @param json the saved data
     */
    public Bar(JsonObject json){
        this(json.getString("name", null),
             json.getInt("min", 0),
             json.getInt("max", Integer.MAX_VALUE),
             json.getInt("value", 0));
        
        json.get("bonuses").asArray()
                .forEach(e -> bonuses.add(
                        new Pair<>(
                                e.asObject().getInt("time", 0),
                                e.asObject().getInt("value", 0)
                        )));
        
        updateBonus();
    }

    /**
     * Copies a bar.
     * @param other the origin of the copy
     */
    public Bar(Bar other) {
        this(other.name,
             other.minimum,
             other.maximum,
             other.real);
        
        bonuses.addAll(other.bonuses);
        updateBonus();
    }
    
    /**
     * Is it possible to increment the value of this bar?
     * @param value how much you'd like to increment the value of this bar
     * @return {@code true} if you can increment it.
     */
    public boolean canAdd(int value){
        return value >= 0 ? real + value <= maximum
                          : canRemove(value);
    }
    
    /**
     * Increments the value of this bar by a set number.
     * <p>Note that this is not the same as a bonus. See {@link #addBonus(int,int)}.
     * @param value how much you'd like to increment to value of this bar
     * @param mode what should do this method if the maximum value is reached.
     */
    public void add(int value, Behavior mode){
        if(value < 0)
            remove(value, mode);
        
        if(canAdd(value))
           real += value;
        else{
            if(mode == ACCEPT)
                real = maximum;
            else
                throw new IllegalArgumentException(String.format("Adding %d to "
                        + "the current value (%d) would result in %d, which is "
                        + "greater than the allowed maximum (%d)", value, real, 
                        value+real, maximum));
        }
    }
    
    /**
     * Is it possible to decrement the value of this bar?
     * @param value how much you'd like to decrement the value of this bar
     * @return {@code true} if you can decrement it.
     */
    public boolean canRemove(int value){
        return real - value >= minimum;
    }
    
    /**
     * Decrements the value of this bar by a set number.
     * <p>Note that this is not the same as a bonus. See {@link #addBonus(int,int)}.
     * @param value how much you'd like to decrement to value of this bar
     * @param mode what should do this method if the minimum value is reached.
     * @throws IllegalArgumentException for negative values
     */
    public void remove(int value, Behavior mode){
        if(value < 0)
            throw new IllegalArgumentException("Negative decrements are not "
                    + "allowed. See Bar.add(int,int).");
        
        if(canRemove(value))
           real -= value;
        else{
            if(mode == ACCEPT)
                real = minimum;
            else
                throw new IllegalArgumentException(String.format("Removing %d from "
                        + "the current value (%d) would result in %d, which is "
                        + "lesser than the allowed maximum (%d)", value, real, 
                        value+real, maximum));
        }
    }
    
    /**
     * Increments the maximum value.
     * @param value by how much you want to increment the maximum
     * @throws IllegalArgumentException for negative values
     */
    public void increaseMaximum(int value){
        if(value < 0)
            throw new IllegalArgumentException("Negative values are not allowed: " + value);
        
        maximum += value;
    }
    
    /**
     * Decrements the minimum value.
     * @param value by how much you want to decrement the minimum
     * @throws IllegalArgumentException for negative values
     */
    public void decreaseMinimum(int value){
        if(value < 0)
            throw new IllegalArgumentException("Negative values are not allowed: " + value);
        
        minimum -= value;
    }
    
    /**
     * Adds a bonus to this bar.
     * @param time How many ticks the bonus will last
     * @param value How much does this bonus increase the value
     */
    public void addBonus(int time, int value) {
        bonuses.add(new Pair<>(time, value));
        
        updateBonus();
    }
    
    final void updateBonus(){
        bonusTotal = 0;
        
        for(Pair<Integer, Integer> p : bonuses)
            bonusTotal += p.getSecond();
    }
    
    /**
     * Method that notifies this object that a tick has passed, and updates this
     * object.
     */
    public void nextTick(){
        bonuses.removeIf(b -> --b.first <= 0);
        
        updateBonus();
    }

    /**
     * The minimum value of this bar.
     * @return The minimum value of this bar.
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * The maximum value of this bar.
     * @return The maximum value of this bar.
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * The current value of this bar.
     * <p>This value is the sum of the value contained in this Bar and the sum 
     * of the bonuses, therefore it is the one that should be used.
     * <p>This value cannot exceed {@link #getMaximum() }.
     * @return The current value of this bar.
     * @see #getBonusTotal() Sum of the bonuses
     * @see #getReal() The value of this bar without the bonuses
     */
    public int getCurrent() {
        return min(real + bonusTotal, maximum);
    }
    
    /**
     * The real value of this bar.
     * <p>Attention, this value does not take into account the eventual bonuses.
     * To get the value of this bar AND the bonuses, use {@link #getCurrent() }.
     * @return The real value of this bar.
     * @see #getBonusTotal() Sum of the bonuses
     * @see #getCurrent() Sum of this value and the bonuses
     */
    public int getReal() {
        return real;
    }

    /**
     * The total of the bonuses at this current time.
     * @return The total of the bonuses.
     * @see #getCurrent() The value of this bar
     * @see #getReal() The value without the bonuses
     */
    public int getBonusTotal() {
        return bonusTotal;
    }
    
    /**
     * The display name of this Bar.
     * @return Display name of this Bar.
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.minimum;
        hash = 97 * hash + this.maximum;
        hash = 97 * hash + this.real;
        hash = 97 * hash + this.bonusTotal;
        hash = 97 * hash + Objects.hashCode(this.name);
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
        final Bar other = (Bar) obj;
        if (this.minimum != other.minimum) {
            return false;
        }
        if (this.maximum != other.maximum) {
            return false;
        }
        if (this.real != other.real) {
            return false;
        }
        if (this.bonusTotal != other.bonusTotal) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.bonuses, other.bonuses)) {
            return false;
        }
        return true;
    }

    @Override
    public JsonObject save() {
        JsonArray bonuses = new JsonArray();
        this.bonuses.forEach(
                e -> bonuses.add(new JsonObject()
                        .add("time", e.first)
                        .add("value", e.second)
                ));
        
        return new JsonObject()
                .add("name", name)
                .add("min", minimum)
                .add("max", maximum)
                .add("value", real)
                .add("bonuses", bonuses);
    }
    
    /**
     * Specifies what the behavior of methods such as 
     * {@link Bar#add(int, Behavior) add } or 
     * {@link Bar#remove(int, Behavior) remove } should be.
     */
    public enum Behavior{
        /**
         * If the maximum or minimum value is reached, sets the value to the
         * maximum or minimum value.
         */
        ACCEPT(true),
        
        /**
         * If the maximum or minimum value is reached, throws an {@link IllegalArgumentException}.
         */
        DENY(false);
        
        /** The value of this object; {@code true} for {@link #ACCEPT} and 
         * {@code false} for {@link #DENY}. */
        public final boolean BOOLEAN;
        
        Behavior(boolean b){
            BOOLEAN = b;
        }
    }
}
