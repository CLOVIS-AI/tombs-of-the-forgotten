/*
 * The MIT License
 *
 * Copyright 2018 Canet Ivan & Chourouq Sarah.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.cc.world.generator;

import com.cc.world.World;
import java.util.Random;

/**
 * A Generator is an object that is designed to randomly (procedurally) build a
 * new World.
 * @author Ivan Canet
 */
public interface Generator {
    
    /**
     * Generates a new World.
     * @param randomizer the source of randomness
     * @return The newly generated World.
     */
    World generate(Random randomizer);
    
    /**
     * Generates a new World from a seed.
     * @param seed the source of randomness
     * @return The newly generated World.
     */
    default World generate(long seed) {
        return generate(new Random(seed));
    }
    
    /**
     * Generates a new World, using a random seed.
     * <p>The seed is the current time according to {@link System#nanoTime() }.
     * @return The newly generated World.
     */
    default World generate() {
        return generate(System.nanoTime());
    }
    
}
