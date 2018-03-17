/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils;

/**
 * Bars are used to represent information about the player (skills, health bar)... 
 * @author Ivan Canet
 */
public final class Bar {
    
    final int minimum, maximum;
    
    int real;
    
    int bonusTotal;
    
    // Pair<Integer, Integer> bonuses;

    /**
     * Creates a Bar.
     * @param minimum minimum value of this bar
     * @param maximum maximum value of this bar
     * @param value beginning value of this bar
     */
    public Bar(int minimum, int maximum, int value) {
        this.minimum = minimum;
        this.maximum = maximum;
        this.real = value;
        bonusTotal = 0;
    }
    
    /**
     * Creates a Bar.
     * @param minimum minimum value of this bar
     * @param maximum maximum value of this bar
     */
    public Bar(int minimum, int maximum){
        this(minimum, maximum, minimum);
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
     * @return The current value of this bar.
     * @see #getBonusTotal() Sum of the bonuses
     * @see #getReal() The value of this bar without the bonuses
     */
    public int getCurrent() {
        return real + bonusTotal;
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
    
    
    
}
