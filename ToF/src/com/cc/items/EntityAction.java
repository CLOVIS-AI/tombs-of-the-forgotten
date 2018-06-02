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

import static com.cc.items.EntityAction.Mode.MODIFICATION;
import static com.cc.items.EntityAction.Operation.ADD;
import static com.cc.items.EntityAction.Target.OPPONENT;
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
    private final int value, turns;
    private final Operation operation;
    private final Mode mode;
    
    /**
     * Creates a new Action
     * @param target the target of the action
     * @param operation the operation that will be executed on that target
     * @param stat the stat that will be modified
     * @param value how much it will be modified
     * @param mode the mode of use of this item
     */
    public EntityAction(Target target, Operation operation, Stat stat, int value,
            Mode mode){
        this.target = target;
        this.stat = stat;
        this.value = value;
        this.operation = operation;
        this.mode = mode;
        this.turns = 1;
    }
    
    /**
     * Creates a new Action
     * @param target the target of the action
     * @param operation the operation that will be executed on that target 
     */
    public EntityAction(Target target, Operation operation, Mode mode){
        this(target, operation, null, 0, mode);
    }
    
    /**
     * Creates an Action object from a JSON object.
     * @param json the saved data
     */
    public EntityAction(JsonObject json) {
        this(Target.valueOf(   json.getString("target", null)),
             Operation.valueOf(json.getString("operation", null)),
             Stat.valueOf(     json.getString("stat", null)),
                               json.getInt("value", 0),
             Mode.valueOf(     json.getString("mode", null)));
    }
    
    @Override
    public JsonObject save() {
        return new JsonObject()
                .add("target", target.name())
                .add("stat", stat.name())
                .add("type", "entity")
                .add("value", value)
                .add("operation", operation.name())
                .add("mode", mode.name());
    }
    
    /**
     * Executes the action, based on the specified parameters.
     * @param e the entity that executes this item.
     */
    @Override
    public void execute(Entity e){
        mode.execute(target.select(e), stat, operation.getValue(value), turns);
    }

    @Override
    public Message getEffects() {
        return new Message()
                .add(target.toString())
                .add(":")
                .add(stat)
                .add(operation == ADD ? "+" : "-")
                .add(value)
                .add(mode != MODIFICATION ? " ("+mode+")" : "");
    }

    @Override
    public boolean canUse(Entity entity) {
        try{
            if(target == OPPONENT){
                target.select(entity);
                return true;
            }else
                return mode.canExecute(entity, stat, operation.getValue(value));
        }catch(CannotUseItem e){
            return false;
        }
    }

    @Override
    public int getWear(Stat requested) {
        return mode.getWear(stat, requested, value);
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
        
        @Override
        public String toString(){
            return name().substring(0, 1) + name().substring(1).toLowerCase();
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
         * Calculates the effect of an operation.
         * @param value how effective this operation is
         * @return The amount of effect this operation has.
         */
        public int getValue(int value){
            return modifier.apply(value);
        }
    }
    
    public enum Mode {
        MODIFICATION {
            @Override
            public void execute(Entity e, Stat s, int value, int turns) {
                s.increment(e, value);
            }
            @Override
            public boolean canExecute(Entity e, Stat s, int value) {
                return s.canIncrement(e, value);
            }
        }, BONUS {
            @Override
            public void execute(Entity e, Stat s, int value, int turns) {
                s.newBonus(e, turns, value);
            }
        }, PERMANENT {
            @Override
            public void execute(Entity e, Stat s, int value, int turns) {
                throw new IllegalStateException("You cannot execute a permanent item!");
            }
            @Override
            public boolean canExecute(Entity e, Stat s, int value) {
                return false;
            }
            @Override
            public int getWear(Stat current, Stat requested, int value) {
                return current == requested ? value : 0;
            }
        };
        
        public abstract void execute(Entity e, Stat s, int value, int turns);
        public boolean canExecute(Entity e, Stat s, int value){ return true; }
        public int getWear(Stat current, Stat requested, int value){ 
            return 0;
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
        hash = 37 * hash + Objects.hashCode(this.mode);
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
        if (this.mode != other.mode) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return mode + ":" + target + ":" + stat + ":" + operation + ":" + value;
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
    
    // *********************************************************** G E T T E R S
    
    public Stat getStat() {
        return stat;
    }

    public Target getTarget() {
        return target;
    }

    public int getValue() {
        return value;
    }

    public int getTurns() {
        return turns;
    }

    public Operation getOperation() {
        return operation;
    }

    public Mode getMode() {
        return mode;
    }
}
