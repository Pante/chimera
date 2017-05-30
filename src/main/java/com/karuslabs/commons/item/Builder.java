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
package com.karuslabs.commons.item;

import java.util.*;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * 
 * @param <GenericBuilder>
 * @param <GenericMeta> 
 */
public abstract class Builder<GenericBuilder extends Builder, GenericMeta extends ItemMeta> { 
    
    /**
     * 
     */
    protected ItemStack item;
    /**
     * 
     */
    protected GenericMeta meta;

    
    /**
     * 
     * @param material 
     */
    public Builder(Material material) {
        this(new ItemStack(material));
    }
    
    /**
     * 
     * @param item 
     */
    public Builder(ItemStack item) {
        this(item, (GenericMeta) item.getItemMeta());
    }
    
    /**
     * 
     * @param builder 
     */
    public Builder(Builder builder) {
        this(builder.item, (GenericMeta) builder.meta);
    }
    
    /**
     * 
     * @param item
     * @param meta 
     */
    protected Builder(ItemStack item, GenericMeta meta) {
        this.item = item;
        this.meta = meta;
    }

    
    /**
     * 
     * @param amount
     * @return 
     */
    public GenericBuilder amount(int amount) {
        item.setAmount(amount);
        return getThis();
    }
    
    /**
     * 
     * @param data
     * @return 
     */
    public GenericBuilder durability(short data) {
        item.setDurability(data);
        return getThis();
    }
    
    /**
     * 
     * @param enchantment
     * @param level
     * @return 
     */
    public GenericBuilder enchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return getThis();
    }
    
    /**
     * 
     * @param enchantments
     * @return 
     */
    public GenericBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        item.addUnsafeEnchantments(enchantments);
        return getThis();
    }
    
    /**
     * 
     * @param name
     * @return 
     */
    public GenericBuilder name(String name) {
        meta.setDisplayName(name);
        return getThis();
    }
    
    
    /**
     * 
     * @param lore
     * @return 
     */
    public GenericBuilder lore(String lore) {
        if (!meta.hasLore()) {
            meta.setLore(new ArrayList<>());
        }
        meta.getLore().add(lore);
        
        return getThis();
    }
    
    /**
     * 
     * @param lore
     * @return 
     */
    public GenericBuilder lore(List<String> lore) {
        if (!meta.hasLore()) {
            meta.setLore(new ArrayList<>());
        }
        meta.getLore().addAll(lore);
        
        return getThis();
    }

    
    /**
     * 
     * @param flags
     * @return 
     */
    public GenericBuilder flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return getThis();
    }

    
    /**
     * 
     * @return 
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
    
    
    /**
     * 
     * @return 
     */
    protected abstract GenericBuilder getThis();
    
}
