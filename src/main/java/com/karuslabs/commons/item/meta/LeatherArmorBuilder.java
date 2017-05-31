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
 * A builder system used to modify and create information for leather armor.
 */
public class LeatherArmorBuilder extends Builder<LeatherArmorBuilder, LeatherArmorMeta> {
    
	/**
	 * Constructs a <code>LeatherArmorBuilder</code> from the specified <code>ItemStack</code>.
	 */
    public LeatherArmorBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Constructs a <code>LeatherArmorBuilder</code> from the specified <code>Builder</code>.
     */
    public LeatherArmorBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Sets the color of the builder
     * 
     * @param color the color
     * @return the modified <code>LeatherArmorBuilder</code> instance.
     */
    public LeatherArmorBuilder color(Color color) {
        meta.setColor(color);
        return this;
    }
    
    /**
     * @return the current <code>LeatherArmorBuilder</code> instance
     */
    @Override
    protected LeatherArmorBuilder getThis() {
        return this;
    }
    
}
