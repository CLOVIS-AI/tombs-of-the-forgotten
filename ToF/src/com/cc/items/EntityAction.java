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

import static com.cc.items.EntityAction.Operation.ADD;
import com.cc.players.Entity;
import com.cc.players.Entity.Stat;
import com.cc.utils.messages.Message;
import com.eclipsesource.json.JsonObject;
import java.util.Objects;
import java.util.function.Function;

/**
 * The action that is executed when an Item is used (for instance).
 * @author Ivan Canet
 */
public class EntityAction implements Action {

    private final Stat stat;
    private final Target target;
    private final int value;
    private final Operation operation;
    
    /**
     * Creates a new Action
     * @param target the target of the action
     * @param operation the operation that will be executed on that target
     * @param stat the stat that will be modified
     * @param value how much it will be modified
     */
    public EntityAction(Target target, Operation operation, Stat stat, int value){
        this.target = target;
        this.stat = stat;
        this.value = value;
        this.operation = operation;
    }
    
    /**
     * Creates a new Action
     * @param target the target of the action
     * @param operation the operation that will be executed on that target 
     */
    public EntityAction(Target target, Operation operation){
        this(target, operation, null, 0);
    }
    
    /**
     * Creates an Action object from a JSON object.
     * @param json the saved data
     */
    public EntityAction(JsonObject json) {
        this(Target.valueOf(   json.getString("target", null)),
             Operation.valueOf(json.getString("operation", null)),
             Stat.valueOf(     json.getString("stat", null)),
                               json.getInt("value", 0));
    }
    
    @Override
    public JsonObject save() {
        return new JsonObject()
                .add("target", target.toString())
                .add("stat", stat.toString())
                .add("value", value)
                .add("operation", operation.toString());
    }
    
    /**
     * Executes the action, based on the specified parameters.
     * @param e the entity that executes this item.
     */
    @Override
    public void execute(Entity e){
        operation.execute(target.select(e), stat, value);
    }

    @Override
    public Message getEffects() {
        return new Message()
                .add(stat)
                .add(": ")
                .add(operation == ADD ? value : -value);
    }

    @Override
    public boolean canUse(Entity entity) {
        return operation.canExecute(target.select(entity), stat, value);
    }
    
    // ************************************************************* S T A T I C
    
    /**
     * What is targeted by this action?
     */
    public enum Target {
        /** The action will do something on the entity that uses the Item. */
        SELF(e -> e),
        /** The action will do something on the opponent of the entity that uses
         the item. */
        OPPONENT(e -> e.getOpponent().orElseThrow(CannotUseItem::noOpponent));
        
        private final Function<Entity, Entity> f;
        Target(Function<Entity, Entity> ff){
            f = ff;
        }
        Entity select(Entity e){
            return f.apply(e);
        }
    }
    
    /**
     * The operation executed by this action.
     */
    public enum Operation {
        /** Add a value to the player's stats. */
        ADD(v -> v),
        /** Remove a value to the player's stats. */
        REMOVE(v -> -v);
        
        private final Function<Integer, Integer> modifier;
        Operation(Function<Integer, Integer> mod){
            modifier = mod;
        }
        
        /**
         * Executes this operation.
         * @param entity the entity the operation is executed on
         * @param stat the entity's stat that is modified
         * @param value how effective this operation is
         */
        public void execute(Entity entity, Stat stat, int value){
            stat.increment(entity, modifier.apply(value));
        }
        
        /**
         * Is it possible to execute this operation?
         * @param entity the entity the operation is executed one
         * @param stat the entity's stat that is modified
         * @param value how effective the operation is
         * @return {@code true} if the operation can be performed.
         */
        public boolean canExecute(Entity entity, Stat stat, int value){
            return stat.canIncrement(entity, modifier.apply(value));
        }
    }
    
    // *************************************************************** O T H E R

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.stat);
        hash = 37 * hash + Objects.hashCode(this.target);
        hash = 37 * hash + this.value;
        hash = 37 * hash + Objects.hashCode(this.operation);
        return hash;
    }

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
        final EntityAction other = (EntityAction) obj;
        if (this.value != other.value) {
            return false;
        }
        if (this.stat != other.stat) {
            return false;
        }
        if (this.target != other.target) {
            return false;
        }
        if (this.operation != other.operation) {
            return false;
        }
        return true;
    }
    
    /**
     * Thrown when the user tries to use an item that is not available.
     */
    public static class CannotUseItem extends RuntimeException {
        public CannotUseItem(String str){
            super(str);
        }
        public static CannotUseItem noOpponent(){
            return new CannotUseItem("No opponent was found.");
        }
    }
}
