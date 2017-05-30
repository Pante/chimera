/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.item.meta;

import com.karuslabs.commons.item.Builder;

import java.util.List;

import org.bukkit.FireworkEffect;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * A builder system used to modify and create fireworks.
 */
public class FireworkBuilder extends Builder<FireworkBuilder, FireworkMeta> {
    
	/**
	 * Constructs a <code>FireworkBuilder</code> from the specified <code>ItemStack</code>.
	 */
    public FireworkBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Constructs a <code>FireworkBuilder</code> from the specified <code>Builder</code>.
     */
    public FireworkBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Adds the specified array of firework effects.
     * 
     * @param effects the array of effects
     * @return the modified <code>FireworkBuilder</code> instance
     */
    public FireworkBuilder effects(FireworkEffect... effects) {
        meta.addEffects(effects);
        return this;
    }
    
    /**
     * Adds the specified list of firework effects.
     * 
     * @param effects the list of effects
     * @return the modified <code>FireworkBuilder</code> instance 
     */
    public FireworkBuilder effects(List<FireworkEffect> effects) {
        meta.addEffects(effects);
        return this;
    }
    
    /**
     * Sets the power of the firework.
     * 
     * @param power the power
     * @return the modified <code>FireworkBuilder</code> instance 
     */
    public FireworkBuilder power(int power) {
        meta.setPower(power);
        return this;
    }
    
    /**
     * @return the current <code>FireworkBuilder</code> instance.
     */
    @Override
    protected FireworkBuilder getThis() {
        return this;
    }
    
}
