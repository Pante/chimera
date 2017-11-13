/* 
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.karuslabs.commons.item;

import java.util.*;
import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;


/**
 * Represents a builder for {@code ItemStack}s.
 * 
 * @param <GenericBuilder> the type of Builder to return
 * @param <GenericMeta> the type of ItemMeta the ItemStack to build contains
 */
public abstract class Builder<GenericBuilder extends Builder, GenericMeta extends ItemMeta> { 
    
    /**
     * The {@code ItemStack} to build.
     */
    protected ItemStack item;
    /**
     * The {@code ItemMeta} of the {@code ItemStack} to build.
     */
    protected GenericMeta meta;

    
    /**
     * Constructs a {@code Builder} for an {@code ItemStack} with the specified material.
     * 
     * @param material the material of the ItemStack
     */
    public Builder(@Nonnull Material material) {
        this(new ItemStack(material));
    }
    
    /**
     * Constructs a {@code Builder} for the specified {@code ItemStack}.
     *  
     * @param item the ItemStack
     */
    public Builder(@Nonnull ItemStack item) {
        this(item, (GenericMeta) item.getItemMeta());
    }
    
    /**
     * Constructs a {@code Builder} which copies the specified {@code Builder}.
     *  
     * @param builder the Builder to be copied
     */
    public Builder(@Nonnull Builder<GenericBuilder, GenericMeta> builder) {
        this(builder.item, builder.meta);
    }

    /**
     * Constructs a {@code Builder} for the specified {@code ItemStack} and {@code ItemMeta}.
     * 
     * @param item the ItemStack
     * @param meta the ItemMeta
     */
    protected Builder(ItemStack item, GenericMeta meta) {
        this.item = item;
        this.meta = meta;
    }   
    
    
    /**
     * Workaround for returning the generic {@code this}.
     * 
     * @return this
     */
    protected @Nonnull abstract GenericBuilder getThis();
    
    
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
     * Adds the enchantment with the specified level.
     * Ignores vanilla restrictions.
     * 
     * @param enchantment the enchantment
     * @param level the level
     * @return this
     */
    public GenericBuilder enchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return getThis();
    }

    /**
     * Adds the specified enchantments and their respective levels.
     * Ignores vanilla restrictions.
     * 
     * @param enchantments the enchantments and respective levels
     * @return this
     */
    public GenericBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        item.addUnsafeEnchantments(enchantments);
        return getThis();
    }

    
    /**
     * Sets the name.
     * 
     * @param name the name
     * @return this
     */
    public GenericBuilder name(String name) {
        meta.setDisplayName(name);
        return getThis();
    }
    

    /**
     * Adds the specified lore.
     * 
     * @param lore the lore
     * @return this
     */
    public GenericBuilder lore(String lore) {
        if (!meta.hasLore()) {
            meta.setLore(new ArrayList<>());
        }
        meta.getLore().add(lore);
        
        return getThis();
    }
    
    /**
     * Adds the specified lore.
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
     * Adds the specified {@code ItemFlag}s.
     * 
     * @param flags the flags
     * @return this
     */
    public GenericBuilder flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return getThis();
    }

    
    /**
     * Builds the {@code ItemStack}.
     * 
     * @return the ItemStack
     * @throws ClassCastException if the ItemStack built is not compatible with the ItemMeta subclass
     */
    public @Nonnull ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
    
}
