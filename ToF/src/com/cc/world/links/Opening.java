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
package com.cc.world.links;

import com.cc.players.Entity;
import com.cc.world.Room;
import com.cc.world.World;
import com.eclipsesource.json.JsonObject;

/**
 * An opening is a Link that is always open.
 * @author Ivan Canet
 */
public final class Opening extends Link {

    /**
     * Creates an opening.
     * @param r1 the first room
     * @param r2 the second room
     */
    public Opening(Room r1, Room r2) {
        super(r1, r2, true);
    }
    
    /**
     * Creates an opening.
     * @param world the first room
     * @param json the second room
     */
    public Opening(World world, JsonObject json) {
        super(world, json);
        isOpen = true;
    }
    
    /**
     * Creates an opening.
     * @param rooms the two rooms
     */
    public Opening(Room... rooms){
        this(rooms[0], rooms[1]);
    }

    @Override
    public boolean canOpen(Entity e) {
        return false;
    }

    @Override
    public boolean canClose(Entity e) {
        return false;
    }
    
    @Override
    public JsonObject save() {
        return super.save()
                .add("type", "openning");
    }
    
}
