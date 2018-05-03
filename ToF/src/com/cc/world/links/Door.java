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
 * A Door is a Link that can be opened by any entity.
 * @author Ivan Canet
 */
public class Door extends Link {

    /**
     * Creates a closed door.
     * @param r1 one of the linked rooms
     * @param r2 the other linked room
     */
    public Door(Room r1, Room r2) {
        this(r1, r2, false);
    }
    
    /**
     * Creates a door.
     * @param r1 one of the linked rooms
     * @param r2 the other linked room
     * @param openByDefault {@code true} if the door is opened
     */
    public Door(Room r1, Room r2, boolean openByDefault) {
        super(r1, r2, openByDefault);
    }
    
    public Door(World world, JsonObject json){
        super(world, json);
    }

    @Override
    public boolean canOpen(Entity e) {
        return true;
    }

    @Override
    public boolean canClose(Entity e) {
        return true;
    }

    @Override
    public boolean open(Entity e) {
        return isOpen = true;
    }

    @Override
    public boolean close(Entity e) {
        return isOpen = false;
    }
    
    @Override
    public JsonObject save() {
        return super.save()
                .add("type", "door");
    }
    
}
