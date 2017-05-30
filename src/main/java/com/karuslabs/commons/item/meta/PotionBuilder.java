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

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

/**
 * A builder system used to modify and create potion data. 
 */
public class PotionBuilder extends Builder<PotionBuilder, PotionMeta> {
    
	/**
	 * Constructs a <code>PotionBuilder</code> from the specified <code>ItemStack</code>.
	 * The material type of the <code>ItemStack</code> should be a potion
	 */
    public PotionBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Constructs a <code>PotionBuilder</code> from the specified <code>Builder</code>.
     */
    public PotionBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Sets the color data of the builder.
     * 
     * @param color the color
     * @return the modified <code>PotionBuilder</code> instance 
     */
    public PotionBuilder color(Color color) {
        meta.setColor(color);
        return this;
    }
    
    /**
     * Sets the effect of the potion and whether it should override prexisting effects.
     * 
     * @param effect the potion effect
     * @param whether the effect should be able to override prexisting effects
     * @return the modified <code>PotionBuilder</code> instance
     */
    public PotionBuilder effect(PotionEffect effect, boolean override) {
        meta.addCustomEffect(effect, override);
        return this;
    }
    
    /**
     * Sets the data for the potion.
     * 
     * @param data the potion data
     * @return the modified <code>PotionBuilder</code> instance
     */
    public PotionBuilder data(PotionData data) {
        meta.setBasePotionData(data);
        return this;
    }
    
    /**
     * @return the current <code>PotionBuilder</code> instance
     */
    @Override
    protected PotionBuilder getThis() {
        return this;
    }
    
}
