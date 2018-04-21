/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils;

import java.util.function.Consumer;

/**
 * A Bar that accepts Events (such as "the bar is full", "the bar is empty"...).
 * @author Ivan Canet
 */
public class EventBar extends Bar {
    
    private Consumer<EventBar> onFull, onEmpty;
    
    /**
     * Creates a Bar.
     * @param name display name of this bar
     * @param minimum minimum value of this bar
     * @param maximum maximum value of this bar
     * @param value beginning value of this bar
     */
    public EventBar(String name, int minimum, int maximum, int value) {
        super(name, minimum, maximum, value);
    }

    /**
     * Creates a Bar.
     * @param name display name of this bar
     * @param minimum minimum value of this bar
     * @param maximum maximum value of this bar
     */
    public EventBar(String name, int minimum, int maximum) {
        super(name, minimum, maximum);
    }

    /**
     * Sets the action to be taken on the event of the bar being full.
     * @param onFull the action (the parameter is this bar)
     */
    public void setOnFull(Consumer<EventBar> onFull) {
        this.onFull = onFull;
    }

    /**
     * Sets the action to be taken on the event of the bar being empty.
     * @param onEmpty the action (the parameter is this bar)
     */
    public void setOnEmpty(Consumer<EventBar> onEmpty) {
        this.onEmpty = onEmpty;
    }
    
    @Override
    public void add(int value, Behavior mode){
        super.add(value, mode);
        
        if(onFull != null && getCurrent() == getMaximum())
            onFull.accept(this);
    }
    
    @Override
    public void remove(int value, Behavior mode){
        super.remove(value, mode);
        
        if(onEmpty != null && getCurrent() == getMinimum())
            onEmpty.accept(this);
    }
}
