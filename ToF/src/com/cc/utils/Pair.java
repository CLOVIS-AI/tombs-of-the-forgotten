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

import java.util.Objects;

/**
 * Management of the first value or the second value.
 * @author a.chourouq
 * @param <T> The first element of this pair
 * @param <U> The second element of this pair
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
     * @return {@code true} if this object is the same as the obj argument,
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
