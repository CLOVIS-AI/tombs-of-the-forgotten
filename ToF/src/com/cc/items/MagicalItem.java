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

import static com.cc.items.Action.Operation.REMOVE;
import static com.cc.items.Action.Stat.HEALTH;
import static com.cc.items.Action.Target.OPPONENT;
import com.cc.players.Entity;
import com.eclipsesource.json.JsonObject;

/**
 * A Magical Item.
 * <p>Magical Items can only be used by consuming a certain amount of mana.
 * @author Ivan Canet
 */
public class MagicalItem extends AbstractItem {

    protected int manaCost;
    protected Action action;
    
    public MagicalItem(JsonObject json) {
        super(json);
        this.manaCost = json.getInt("mana-cost", -1);
        //this.action = ?
    }
    
    /**
     * Constructs a Magical Item using a lambda-expression as an effect.
     * @param name the name of the item
     * @param weight the weight of the item
     * @param description the description of the item
     * @param rarity the rarity of the item
     * @param manaCost the mana cost of the item
     * @param action what happens when you use it
     * @see #MagicalItem(java.lang.String, int, java.lang.String, com.cc.items.Rarity, int, int) Magical Weapon
     */
    public MagicalItem(String name, int weight, String description, Rarity rarity, 
            int manaCost, Action action) {
        super(name, weight, description, rarity);
        this.manaCost = manaCost;
        this.action = action;
    }
    
    /**
     * Constructs a Magical weapon.
     * @param name the name of the weapon
     * @param weight the weight of the weapon
     * @param description the description of the weapon
     * @param rarity the rarity of the weapon
     * @param manaCost the mana cost of using the weapon
     * @param damage the damage dealt by the weapon (see {@link Entity#hurt(int) })
     * @see #MagicalItem(java.lang.String, int, java.lang.String, com.cc.items.Rarity, int, java.util.function.Consumer) Magical Item
     */
    public MagicalItem(String name, int weight, String description, Rarity rarity, 
            int manaCost, int damage) {
        this(name, weight, description, rarity, manaCost, 
                new Action(OPPONENT, REMOVE, HEALTH, damage));
    }

    @Override
    public void use(Entity entity) {
        if(entity.getMana() < manaCost)
            return;
        
        action.execute(entity);
        entity.useMana(manaCost);
    }
    
}
