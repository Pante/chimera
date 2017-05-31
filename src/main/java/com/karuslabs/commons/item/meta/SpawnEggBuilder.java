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

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

/**
 * Represents a specialised <code>Builder</code> for building items with <code>SpawnEggMeta</code>.
 */
public class SpawnEggBuilder extends Builder<SpawnEggBuilder, SpawnEggMeta> {

	/**
	 * Constructs a <code>SpawnEggBuilder</code> with the specified <code>ItemStack</code>.
	 * The material type of the <code>ItemStack</code> should be MONSTER_EGG
	 * 
	 * @param item the ItemStack
	 */
    public SpawnEggBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Copy constructor which constructs a <code>SpawnEggBuilder</code> with the specified <code>ItemStack</code>.
     * 
     * @param builder the Builder 
     */
    public SpawnEggBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Sets the entity the spawn egg will spawn.
     * 
     * @params type the entity type
     * @return the modified <code>SpawnEggBuilder</code> instance
     */
    public SpawnEggBuilder spawned(EntityType type) {
        meta.setSpawnedType(type);
        return this;
    }
    
    /**
     * @return the current <code>SpawnEggBuilder</code> instance
     */
    @Override
    protected SpawnEggBuilder getThis() {
        return this;
    }
    
}
