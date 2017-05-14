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


public class EnchantmentStorageBuilder extends Builder<EnchantmentStorageBuilder, EnchantmentStorageMeta> {
    
    public EnchantmentStorageBuilder(ItemStack item) {
        super(item);
    }
    
    public EnchantmentStorageBuilder(Builder builder) {
        super(builder);
    }
    
    
    public EnchantmentStorageBuilder stored(Enchantment enchantment, int level, boolean ignoreRestriction) {
        meta.addStoredEnchant(enchantment, level, ignoreRestriction);
        return this;
    }
    
    
    @Override
    protected EnchantmentStorageBuilder getThis() {
        return this;
    }
    
}
