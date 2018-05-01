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
package com.cc.items;

import com.cc.players.Entity;
import com.eclipsesource.json.JsonObject;

/**
 * UniqueUsage items cannot be used twice; as soon as they are used they will
 * reject any further call to their {@link #use(com.cc.players.Entity) } method.
 * <p>Note for the devs: when implementing an item than can only be used once
 * that has a complicated action, look at {@link UniqueUsage}.
 * @author Ivan Canet
 */
public class UniqueLambda extends UniqueUsage {

    protected Action action;
    
    public UniqueLambda(JsonObject json){
        super(json);
        action = new Action(json.get("action").asObject());
    }
    
    /**
     * Constructs a single-use item with a lambda-expression.
     * @param name its name
     * @param weight its weight
     * @param description its description
     * @param rarity its rarity
     * @param action what happens when an entity uses the item
     */
    public UniqueLambda(String name, int weight, String description, 
            Rarity rarity, Action action) {
        super(name, weight, description, rarity);
        this.action = action;
    }

    @Override
    protected void act(Entity entity) {
        action.execute(entity);
    }
    
    @Override
    public JsonObject save(){
        return super.save()
                .add("action", action.save());
    }
    
}
