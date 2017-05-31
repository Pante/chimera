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

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

/**
 * A builder system used to store information relevant to enchantments.
 */
public class EnchantmentStorageBuilder extends Builder<EnchantmentStorageBuilder, EnchantmentStorageMeta> {
    
	/**
	 * Constructs an <code>EnchantmentStorageBuilder</code> from the specified <code>ItemStack</code>.
	 */
    public EnchantmentStorageBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Constructs an <code>EnchantmentStorageBuilder</code> from the specified <code>Builder</code>.
     */
    public EnchantmentStorageBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Stores an enchantment based on its type, level, and if it ignores restrictions.
     * 
     * @param enchantment the enchantment
     * @param level the level
     * @param ignoreRestriction if the applied enchant should ignore vanilla limits
     * @return the modified <code>EnchantmentStorageBuilder</code> 
     */
    public EnchantmentStorageBuilder stored(Enchantment enchantment, int level, boolean ignoreRestriction) {
        meta.addStoredEnchant(enchantment, level, ignoreRestriction);
        return this;
    }
    
    /**
     * @return the current <code>EnchantmentStorageBuilder</code> instance
     */
    @Override
    protected EnchantmentStorageBuilder getThis() {
        return this;
    }
    
}
