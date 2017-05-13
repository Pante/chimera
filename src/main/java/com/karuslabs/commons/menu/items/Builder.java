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
package com.karuslabs.commons.menu.items;

import java.util.*;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;


public abstract class Builder<GenericBuilder extends Builder, GenericMeta extends ItemMeta> { 
    
    protected ItemStack item;
    protected GenericMeta meta;

    
    public Builder(ItemStack item) {
        this(item, (GenericMeta) item.getItemMeta());
    }
    
    public Builder(ItemStack item, GenericMeta meta) {
        this.item = item;
        this.meta = meta;
    }

    
    protected abstract GenericBuilder getThis();

    
    public GenericBuilder amount(int amount) {
        item.setAmount(amount);
        return getThis();
    }

    public GenericBuilder durability(short data) {
        item.setDurability(data);
        return getThis();
    }

    public GenericBuilder enchantment(Enchantment enchantment, int level) {
        item.addEnchantment(enchantment, level);
        return getThis();
    }

    public GenericBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        item.addUnsafeEnchantments(enchantments);
        return getThis();
    }

    public GenericBuilder name(String name) {
        meta.setDisplayName(name);
        return getThis();
    }

    public GenericBuilder lore(List<String> lore) {
        meta.setLore(lore);
        return getThis();
    }

    public GenericBuilder lore(String lore) {
        if (!meta.hasLore()) {
            meta.setLore(new ArrayList<>());
        }
        meta.getLore().add(lore);

        return getThis();
    }

    public GenericBuilder flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return getThis();
    }

    
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
    
}
