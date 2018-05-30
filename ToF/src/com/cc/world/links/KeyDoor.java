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
package com.cc.world.links;

import com.cc.players.Entity;

/**
 *
 * @author icanet
 */
public class KeyDoor extends Link {
    
    private int id;
    
    private boolean hasKey(Entity e){
        return e.getInventory().stream().anyMatch(i -> i.getId() == id);
    }

    @Override
    public boolean canOpen(Entity e) {
        return hasKey(e);
    }

    @Override
    public boolean canClose(Entity e) {
        return hasKey(e);
    }

    @Override
    public boolean open(Entity e) {
        return !isOpen && canOpen(e);
    }

    @Override
    public boolean close(Entity e) {
        return isOpen && canClose(e);
    }
    
}