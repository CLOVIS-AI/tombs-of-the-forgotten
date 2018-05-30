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
import com.cc.world.Room;
import com.cc.world.World;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.WriterConfig;

/**
 *
 * @author icanet
 */
public class KeyDoor extends Link {
    
    private final int id;
    
    public KeyDoor(int id, Room[] rooms){
        super(rooms);
        this.id = id;
    }
    
    public KeyDoor(World world, JsonObject json){
        super(world, json);
        id = json.getInt("id", 0);
        
        if(id == 0)
            throw new IllegalStateException("Found no key in the JSON: " +
                    json.toString(WriterConfig.PRETTY_PRINT));
    }
    
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
    public JsonObject save(){
        return super.save()
                .add("type", "key")
                .add("key", id);
    }
    
}
