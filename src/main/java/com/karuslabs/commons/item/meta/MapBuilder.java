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
import org.bukkit.inventory.meta.MapMeta;

/**
 * Represents a specialised <code>Builder</code> for building items with <code>MapMeta</code>.
 */
public class MapBuilder extends Builder<MapBuilder, MapMeta> {

    /**
     * Constructs a <code>MapBuilder</code> with the specified <code>ItemStack</code>.
     * 
     * @param item the ItemStack
     */
    public MapBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Copy constructor which constructs a <code>MapBuilder</code> with the specified <code>Builder</code>.
     * 
     * @param builder the Builder
     */
    public MapBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Sets the color of the map.
     * 
     * @param color the color
     * @return this
     */
    public MapBuilder color(Color color) {
        meta.setColor(color);
        return this;
    }
    
    /**
     * Sets the name of the maps location
     * 
     * @param name the name
     * @return this
     */
    public MapBuilder locationName(String name) {
        meta.setLocationName(name);
        return this;
    }
    
    /**
     * Sets whether the map follows scaling
     * 
     * @param value whether the map scales
     * @return this
     */
    public MapBuilder scaling(boolean value) {
        meta.setScaling(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected MapBuilder getThis() {
        return this;
    }
    
}
