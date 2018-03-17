/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils;

import java.util.Objects;

/**
 * Management of the first value or the second value.
 * @author a.chourouq
 */
public class Pair<T, U> {
    T first;
    U second;
    
    /**
     * Constructor.
     * @param first first value.
     * @param second second value.
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Get the first value.
     * @return first value.
     */
    public T getFirst() {
        return first;
    }
    
    /**
     * Set the first value.
     * @param first value.
     */
    public void setFirst(T first) {
        this.first = first;
    }

    /**
     * Get the second value.
     * @return second second value.
     */
    public U getSecond() {
        return second;
    }
    
    /**
     * Set the second value.
     * @param second value.
     */
    public void setSecond(U second) {
        this.second = second;
    }
    
    /**
     * A hash code value for the object.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.first);
        hash = 67 * hash + Objects.hashCode(this.second);
        return hash;
    }
    
    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return true if this object is the same as the obj argument,
     * false otherwise.
     */
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
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        if (!Objects.equals(this.first, other.first)) {
            return false;
        }
        if (!Objects.equals(this.second, other.second)) {
            return false;
        }
        return true;
    }
    
    
}
