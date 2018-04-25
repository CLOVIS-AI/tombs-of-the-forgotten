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

/**
 * A Weapon. Weapons attack your enemy using your stamina.
 * @author Ivan Canet
 */
public class Weapon extends AbstractItem {

    protected int damage;
    protected double effect;
    protected int neededStamina;
    
    /**
     * Creates a Weapon.
     * @param name the weapon's name
     * @param weight the weight of the weapon
     * @param description the description of the weapon
     * @param rarity the rarity of the weapon
     * @param damage the damage dealt by the weapon
     * @param effect how much does this item 'improves' when extra stamina is 
     *      provided
     * @param staminaLevel the needed stamina level to use this weapon
     * @see #use(com.cc.players.Entity) More information on the effect and 
     *      staminaLevel
     */
    public Weapon(String name, int weight, String description, Rarity rarity, 
            int damage, double effect, int staminaLevel) {
        super(name, weight, description, rarity);
        
        if(damage <= 0)
            throw new IllegalArgumentException("Damage cannot be negative or "
                    + "null: " + damage);
        if(staminaLevel <= 0)
            throw new IllegalArgumentException("Stamina level cannot be negative"
                    + " or null: " + staminaLevel);
        
        this.damage = damage;
        this.effect = effect;
        this.neededStamina = staminaLevel;
    }

    /**
     * Notifies this object that the specified entity is using it to attack its
     * opponent.
     * <p>An amount of stamina equal to the weight of this object is consumed after
     * this object is used.
     * <p>The calculation of the provided damage is done knowing that:
     *  <ul>
     *      <li>If the player has exactly the stamina needed to use this item, 
     *      the damage is exactly the expected value.</li>
     *      <li>If the player has more or less stamina, the 'effect' determines 
     *      by how much the damage is incremented per extra stamina level.</li>
     *  </ul>
     * @param entity the entity that uses this item. 
     */
    @Override
    public void use(Entity entity) {
        entity.getOpponent()
                .ifPresent(e -> e.hurt(
                        (int)(damage + effect/
                                (e.getStamina() - neededStamina)
                             )
                ));
        entity.useStamina(weight);
    }
    
}
