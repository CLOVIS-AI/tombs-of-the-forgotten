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

import static com.cc.items.Action.Target.SELF;
import com.cc.players.Entity;
import com.cc.utils.Save;
import com.eclipsesource.json.JsonObject;
import java.util.function.BiConsumer;

/**
 * The action that is executed when an Item is used (for instance).
 * @author Ivan Canet
 */
public class Action implements Save<JsonObject> {

    private final Stat stat;
    private final Target target;
    private final int value;
    
    public Action(Target target, Stat stat, int value){
        this.target = target;
        this.stat = stat;
        this.value = value;
    }
    
    @Override
    public JsonObject save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void execute(Entity e){
        Entity entity = target == SELF ?
                      e : e.getOpponent()
                              .orElseThrow(() -> new IllegalArgumentException("Cannot "
                                      + "use this item in this case, the entity"
                                      + " ("+e+") does not have any opponent!"));
        
        
    }
    
    public enum Stat {
        MANA((e,v) -> e.addMana(v)),
        HEALTH((e,v) -> e.heal(v)),
        STAMINA((e,v) -> e.addStamina(v));
        
        private final BiConsumer<Entity,Integer> consumer;
        Stat(BiConsumer<Entity,Integer> c){consumer=c;}
        public void apply(Entity e, int value){consumer.accept(e, value);};
    }
    
    public enum Target {
        SELF,
        OPPONENT
    }
    
}
