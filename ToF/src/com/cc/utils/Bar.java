/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils;

import static com.cc.utils.Bar.Behavior.ACCEPT;
import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.List;

/**
 * Bars are used to represent information about the player (skills, health bar)... 
 * @author Ivan Canet
 */
public class Bar {
    
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
     * Increments the value of this bar by a set number.
     * <p>Note that this is not the same as a bonus. See {@link #addBonus(int,int)}.
     * @param value how much you'd like to increment to value of this bar
     * @param mode what should do this method if the maximum value is reached.
     * @throws IllegalArgumentException for negative values
     */
    public void add(int value, Behavior mode){
        if(value < 0)
            throw new IllegalArgumentException("Negative increments are not "
                    + "allowed. See Bar.remove(int,int).");
        
        if(real + value <= maximum)
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
        
        if(real - value >= minimum)
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
        bonuses.removeIf(b -> b.first-- <= 0);
        
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
