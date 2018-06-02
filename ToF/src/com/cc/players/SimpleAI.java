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
package com.cc.players;

import com.cc.world.Location;
import com.cc.world.Path;
import com.eclipsesource.json.JsonObject;
import java.util.Random;

/**
 * 
 * @author Ivan Canet
 */
public class SimpleAI extends Entity {
    
    private Path pathToPlayer;
    private final int PERCENT_CHANCE_USE_ITEM = 30;
    
    public SimpleAI(String name, int maxHealth, int maxStrength, int maxMana,
            int maxWeight, Location location) {
        super(name, maxHealth, maxStrength, maxMana, maxWeight, location);
    }
    
    public SimpleAI(JsonObject json) {
        super(json);
    }
    
    @Override
    public void nextTick(){
        super.nextTick();
        
        if(isFighting() || new Random().nextInt(100) < PERCENT_CHANCE_USE_ITEM){
            selectUsableItems()
                    .findAny()
                    .ifPresent(i -> getInventory().use(i, this));
            pathToPlayer = null;
        }
        
        if(!isPathAvailable() && isPlayerInRange() && new Random().nextInt(10) < 1){
            try {
                pathToPlayer = getCurrentRoom().pathTo(getWorld().getPlayer().getCurrentRoom(), this);
                pathToPlayer.moveToNext();
            } catch (Path.UnreachableRoomException ex) {return;}
            
            if(pathToPlayer.size() == 0){
                super.nextTick();
                return;
            }
        }
        if(isPathAvailable() && !isFighting())
            moveTo(pathToPlayer.moveToNext());
    }
    
    private boolean isPlayerInRange(){
        return getWorld().getPlayer().getLocation().dist(getLocation()) < 5;
    }
    
    private boolean isPathAvailable(){
        return pathToPlayer != null && pathToPlayer.size() > 0;
    }
}
