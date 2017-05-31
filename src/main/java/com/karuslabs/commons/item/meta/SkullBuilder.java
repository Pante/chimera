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

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * A builder system used for information pertaining to skulls.
 */
public class SkullBuilder extends Builder<SkullBuilder, SkullMeta> {
    
	/**
	 * Constructs a <code>SkullBuilder</code> from the specified <code>ItemStack</code>.
	 * The material type of the <code>ItemStack<code> should be a skull
	 */
    public SkullBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Constructs a <code>SkullBuilder</code> from the specified <code>Builder</code>.
     */
    public SkullBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Sets the skull owner's name.
     * 
     * @param name the skull owner's name
     * @return the modified <code>SkullBuilder</code> instance
     */
    public SkullBuilder owner(String name) {
        meta.setOwner(name);
        return this;
    }
    
    /**
     * @return the current <code>SkullBuilder</code> instance
     */
    @Override
    protected SkullBuilder getThis() {
        return this;
    }
    
}
