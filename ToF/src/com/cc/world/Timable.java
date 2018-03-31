/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.world;

/**
 * Represents that an object can be updated according to a time event.
 * @author Ivan Canet
 */
public interface Timable {
    
    /**
     * Notifies an object that time has passed. This is useful for objects that
     * have regeneration, or other effects that act over-time.
     */
    public void nextTick();
    
}
