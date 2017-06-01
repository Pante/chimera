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
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * Represents a specialised <code>Builder</code> for building items with <code>LeatherArmorMeta</code>.
 */
public class LeatherArmorBuilder extends Builder<LeatherArmorBuilder, LeatherArmorMeta> {
    
	/**
	 * Constructs a <code>LeatherArmorBuilder</code> with the specified <code>ItemStack</code>.
	 * 
	 * @param item the ItemStack
	 */
    public LeatherArmorBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Copy constructor which constructs a <code>LeatherArmorBuilder</code> with the specified <code>Builder</code>.
     * 
     * @param builder the Builder
     */
    public LeatherArmorBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Sets the color of the builder
     * 
     * @param color the color
     * @return this
     */
    public LeatherArmorBuilder color(Color color) {
        meta.setColor(color);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected LeatherArmorBuilder getThis() {
        return this;
    }
    
}
