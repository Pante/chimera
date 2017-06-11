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
 * Represents a skeletal implementation of a builder for <code>ItemStack</code>s.
 * <p>
 * To implement a builder for a <code>ItemStack</code>, the programmer needs only to extend this class and provide an implementation
 * for the <code>getThis</code> method which returns an instance of the overriding subclass. This is done to allow the implementing subclass to 
 * leverage on the existing methods while allowing the subclass to include additional methods.
 * <p>
 * For more details please read this article on the <a href = "https://en.wikipedia.org/wiki/Curiously_recurring_template_pattern">
 * Curiously recurring template pattern</a>.
 * 
 * @param <GenericBuilder> the type of builder to be returned by the getThis() method, generally the implementing subclass
 * @param <GenericMeta> the type of ItemMeta this builder is to contain
 */
public abstract class Builder<GenericBuilder extends Builder, GenericMeta extends ItemMeta> { 
    
    /**
     * The <code>ItemStack</code> to build, exposed to facilitate subclassing.
     */
    protected ItemStack item;
    /**
     * The <code>ItemMeta</code> subclass to build, exposed to facilitate subclassing.
     */
    protected GenericMeta meta;

    
    /**
     * Constructs a <code>Builder</code> with the specified material.
     * 
     * @param material the material of the item
     */
    public Builder(Material material) {
        this(new ItemStack(material));
    }
    
    /**
     * Constructs a <code>Builder</code> with the specified <code>ItemStack</code>.
     * 
     * @param item the ItemStack to build
     */
    public Builder(ItemStack item) {
        this(item, (GenericMeta) item.getItemMeta());
    }
    
    /**
     * Copy constructor which constructs a <code>Builder</code> with the specified builder.
     * 
     * @param builder the Builder
     */
    public Builder(Builder builder) {
        this(builder.item, (GenericMeta) builder.meta);
    }
    
    /**
     * Constructs a <code>Builder</code> with the specified <code>ItemStack</code> and <code>ItemMeta</code> subclass.
     * 
     * @param item the ItemStack to build
     * @param meta the ItemMeta subclass to build
     */
    protected Builder(ItemStack item, GenericMeta meta) {
        this.item = item;
        this.meta = meta;
    }

    
    /**
     * Sets the amount.
     * 
     * @param amount the amount
     * @return this
     */
    public GenericBuilder amount(int amount) {
        item.setAmount(amount);
        return getThis();
    }
    
    /**
     * Sets the durability.
     * 
     * @param data the durability
     * @return this
     */
    public GenericBuilder durability(short data) {
        item.setDurability(data);
        return getThis();
    }
    
    /**
     * Adds the specified enchantment and corresponding level.
     * 
     * @param enchantment the enchantment
     * @param level the enchantment level
     * @return this
     */
    public GenericBuilder enchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return getThis();
    }
    
    /**
     * Adds the specified enchantments.
     * 
     * @param enchantments the enchantments
     * @return this
     */
    public GenericBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        item.addUnsafeEnchantments(enchantments);
        return getThis();
    }
    
    /**
     * Sets the the display.
     * 
     * @param name the name
     * @return this
     */
    public GenericBuilder name(String name) {
        meta.setDisplayName(name);
        return getThis();
    }
    
    
    /**
     * Adds the specified line of lore.
     * 
     * @param lore the line
     * @return  this
     */
    public GenericBuilder lore(String lore) {
        if (!meta.hasLore()) {
            meta.setLore(new ArrayList<>());
        }
        meta.getLore().add(lore);
        
        return getThis();
    }
    
    /**
     * Adds the specified lines of lore.
     * 
     * @param lore the lore
     * @return this
     */
    public GenericBuilder lore(List<String> lore) {
        if (!meta.hasLore()) {
            meta.setLore(new ArrayList<>());
        }
        meta.getLore().addAll(lore);
        
        return getThis();
    }

    
    /**
     * Adds the specified <code>ItemFlag</code>s.
     * 
     * @param flags the ItemFlags
     * @return this
     */
    public GenericBuilder flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return getThis();
    }

    
    /**
     * Builds the <code>ItemStack</code>.
     * 
     * @return the ItemStack
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
    
    
    /**
     * @return a Builder subclass, generally the implementing subclass
     */
    protected abstract GenericBuilder getThis();
    
}
