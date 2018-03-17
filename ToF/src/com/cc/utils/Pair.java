/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils;

import java.util.Objects;

/**
 *
 * @author a.chourouq
 */
public class Pair<T, U> {
    T first;
    U second;
    
    /**
     * Constructor.
     * @param first
     * @param second 
     */
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * 
     * @return first
     */
    public T getFirst() {
        return first;
    }

    /**
     * 
     * @return second
     */
    public U getSecond() {
        return second;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.first);
        hash = 67 * hash + Objects.hashCode(this.second);
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
