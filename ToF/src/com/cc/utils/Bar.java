/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils;

import static java.lang.Integer.max;

/**
 * Bars are used to represent information about the player (skills, health bar)... 
 * @author Ivan Canet
 */
public final class Bar {
    
    int minimum, maximum;
    
    int real;
    
    int bonusTotal;
    
    // Pair<Integer, Integer> bonuses;
    
    final String name;

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
     * <p>Note that this is not the same as a bonus. See {@link #newBonus(int,int)}.
     * @param value how much you'd like to increment to value of this bar
     * @param mode what should do this method if the maximum value is reached.
     * @throws IllegalArgumentException for negative values
     */
    public void add(int value, Behavior mode){
        if(value < 0)
            throw new IllegalArgumentException("Negative increments are not "
                    + "allowed. See Bar.remove(int,int).");
        
        if(real + value < maximum)
           real += value;
        else{
            if(mode.BOOLEAN)
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
     * <p>Note that this is not the same as a bonus. See {@link #newBonus(int,int)}.
     * @param value how much you'd like to decrement to value of this bar
     * @param mode what should do this method if the minimum value is reached.
     * @throws IllegalArgumentException for negative values
     */
    public void remove(int value, Behavior mode){
        if(value < 0)
            throw new IllegalArgumentException("Negative decrements are not "
                    + "allowed. See Bar.add(int,int).");
        
        if(real - value > minimum)
           real -= value;
        else{
            if(mode.BOOLEAN)
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
    
    final void updateBonus(){
        
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
        return max(real + bonusTotal, maximum);
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
     * {@link #add(int, boolean) add } or 
     * {@link #remove(int, boolean) remove } should be.
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
