/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    public Weapon(int weight, String description, Rarity rarity, int damage,
            double effect, int staminaLevel) {
        super(weight, description, rarity);
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
