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
 * Represents a specialised <code>Builder</code> for building items with <code>SkullMeta</code>.
 */
public class SkullBuilder extends Builder<SkullBuilder, SkullMeta> {
    
    /**
     * Constructs a <code>SkullBuilder</code> with the specified <code>ItemStack</code>.
     * 
     * @param item the ItemStack
     */
    public SkullBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Copy constructor which constructs a <code>SkullBuilder</code> with the specified <code>Builder</code>.
     * 
     * @param builder the Builder
     */
    public SkullBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Sets the name of the skulls owner.
     * 
     * @param name the skull owner's name
     * @return this
     */
    public SkullBuilder owner(String name) {
        meta.setOwner(name);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected SkullBuilder getThis() {
        return this;
    }
    
}
