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

import java.util.function.Consumer;

/**
 * A Bar that accepts Events (such as "the bar is full", "the bar is empty"...).
 * @author Ivan Canet
 */
public class EventBar extends Bar {
    
    private Consumer<EventBar> onFull, onEmpty;
    private Consumer<EventBar> onBonusUpdate;
    
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
     * Creates an EventBar from the contents an other bar.
     * <p>If the other bar is also an EventBar, the events are ommited.
     * @param other the other bar
     */
    public EventBar(Bar other) {
        super(other);
    }

    /**
     * Sets the action to be taken on the event of the bar being full.
     * @param onFull the action (the parameter is this bar)
     * @return This object, to allow method-chaining.
     */
    public EventBar setOnFull(Consumer<EventBar> onFull) {
        this.onFull = onFull;
        return this;
    }

    /**
     * Sets the action to be taken on the event of the bar being empty.
     * @param onEmpty the action (the parameter is this bar)
     * @return This object, to allow method-chaining.
     */
    public EventBar setOnEmpty(Consumer<EventBar> onEmpty) {
        this.onEmpty = onEmpty;
        return this;
    }
    
    /**
     * Sets the action to be taken on the event of the bar updated.
     * @param onUpdate the action (the parameter is this bar)
     * @return This object, to allow method-chaining.
     */
    public EventBar setOnBonusUpdate(Consumer<EventBar> onUpdate) {
        this.onBonusUpdate = onUpdate;
        return this;
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
    
    @Override
    public void updateBonus() {
        super.updateBonus();
        
        if(onBonusUpdate != null)
            onBonusUpdate.accept(this);
    }
}
