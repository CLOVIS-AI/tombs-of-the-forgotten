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
