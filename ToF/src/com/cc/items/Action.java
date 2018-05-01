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
    private final Operation operation;
    
    public Action(Target target, Operation operation, Stat stat, int value){
        this.target = target;
        this.stat = stat;
        this.value = value;
        this.operation = operation;
    }
    
    public Action(Target target, Operation operation){
        this(target, operation, null, 0);
    }
    
    @Override
    public JsonObject save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Executes the action, based on the specified parameters.
     * @param e the entity that executes this item.
     */
    public void execute(Entity e){
        Entity entity = target == SELF ?
                      e : e.getOpponent()
                              .orElseThrow(() -> new IllegalArgumentException("Cannot "
                                      + "use this item in this case, the entity"
                                      + " ("+e+") does not have any opponent!"));
        
        operation.accept(entity, this);
    }
    
    /**
     * Adds/removes the specified value from the target's stat.
     * @param target the target
     */
    void add(Entity target){
        switch(stat){
            case MANA: target.addMana(value); break;
            case HEALTH: target.heal(value); break;
            case STAMINA: target.addStamina(value); break;
        }
    }
    
    public enum Stat {
        MANA,
        HEALTH,
        STAMINA
    }
    
    public enum Target {
        SELF,
        OPPONENT
    }
    
    public enum Operation implements BiConsumer<Entity, Action> {
        ADD((e, a) -> a.add(e));
        
        private final BiConsumer<Entity, Action> callback;
        Operation(BiConsumer<Entity, Action> bc){callback = bc;}
        @Override public void accept(Entity t, Action u) {
            callback.accept(t, u);
        }
    }
}
