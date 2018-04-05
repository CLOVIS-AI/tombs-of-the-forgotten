/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cc.utils;

import java.util.Arrays;
import java.util.List;

/**
 * A Pair where the two elements are of the same type.
 * @author Ivan Canet
 * @param <T> The type of the elements.
 */
public class SamePair<T> extends Pair<T,T> {
    
    /**
     * Creates a pair of two elements, where the two elements are of the same
     * type.
     * @param first the first element
     * @param second the second element
     */
    public SamePair(T first, T second) {
        super(first, second);
    }
    
    /**
     * Constructs a pair of two elements.
     * @param elements the two elements.
     * @throws ArrayIndexOutOfBoundsException if less than 2 elements are provided.
     * @throws IllegalArgumentException if more than 2 elements are provided.
     */
    public SamePair(T... elements){
        super(elements[0], elements[1]);
        
        if(elements.length != 2)
            throw new IllegalArgumentException("There should only be "
                    + "2 elements.");
    }
    
    /**
     * Gets both elements in a List.
     * @return Both elements.
     */
    public List<T> getBoth(){
        return Arrays.asList(getFirst(), getSecond());
    }
    
}
