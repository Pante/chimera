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
 * Represents a specialised <code>Builder</code> for building items with <code>FireworkkMeta</code>.
 */
public class FireworkBuilder extends Builder<FireworkBuilder, FireworkMeta> {
    
    /**
     * Constructs a <code>FireworkBuilder</code> with the specified <code>ItemStack</code>.
     * 
     * @param item the ItemStack
     */
    public FireworkBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Copy constructor which constructs a <code>FireworkBuilder</code> with the specified <code>Builder</code>.
     * 
     * @param builder the Builder
     */
    public FireworkBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Adds the specified firework effects.
     * 
     * @param effects the effects
     * @return this
     */
    public FireworkBuilder effects(FireworkEffect... effects) {
        meta.addEffects(effects);
        return this;
    }
    
    /**
     * Adds the specified firework effects.
     * 
     * @param effects the effects
     * @return this
     */
    public FireworkBuilder effects(List<FireworkEffect> effects) {
        meta.addEffects(effects);
        return this;
    }
    
    /**
     * Sets the power of the firework.
     * 
     * @param power the power
     * @return this
     */
    public FireworkBuilder power(int power) {
        meta.setPower(power);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected FireworkBuilder getThis() {
        return this;
    }
    
}
