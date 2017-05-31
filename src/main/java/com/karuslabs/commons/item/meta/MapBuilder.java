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
 * A builder system used to create and modify map information.
 */
public class MapBuilder extends Builder<MapBuilder, MapMeta> {

	/**
	 * Constructs a <code>MapBuilder</code> from the specified <code>ItemStack</code>.
	 */
    public MapBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Constructs a <code>MapBuilder</code> from the specified <code>Builder</code>.
     */
    public MapBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Sets the color for the builder
     * 
     * @param color the color
     * @return the modified <code>MapBuilder</code> instance.
     */
    public MapBuilder color(Color color) {
        meta.setColor(color);
        return this;
    }
    
    /**
     * Sets the name of the map's location
     * 
     * @param name the name
     * @return the modified <code>MapBuilder</code> instance.
     */
    public MapBuilder locationName(String name) {
        meta.setLocationName(name);
        return this;
    }
    
    /**
     * Sets if the map follows scaling
     * 
     * @param value if the map scales
     * @return the modified <code>MapBuilder</code> instance. 
     */
    public MapBuilder scaling(boolean value) {
        meta.setScaling(value);
        return this;
    }

    /**
     * @return the current <code>MapBuilder</code> instance
     */
    @Override
    protected MapBuilder getThis() {
        return this;
    }
    
}
