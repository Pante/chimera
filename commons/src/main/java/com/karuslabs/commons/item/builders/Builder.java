/*
 * The MIT License
 *
 * Copyright 2018 Karus Labs.
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
package com.karuslabs.commons.item.builders;

import java.util.*;

import org.bukkit.*;
import org.bukkit.attribute.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A {@code ItemStack} builder.
 * 
 * @param <Meta> the type of the ItemMeta
 * @param <Self> {@code this}
 */
public abstract class Builder<Meta extends ItemMeta, Self extends Builder> {
    
    ItemStack item;
    Meta meta;
    @Nullable List<String> lore;
    
    
    /**
     * Creates a {@code Builder} for the given material.
     * 
     * @param material the material
     */
    public Builder(Material material) {
        item = new ItemStack(material);
        meta = (Meta) item.getItemMeta();
    }
    
    /**
     * Creates a copy of the given {@code Builder}.
     * 
     * @param source the builder to copy
     */
    public Builder(Builder<ItemMeta, ?> source) {
        item = source.item;
        meta = (Meta) source.meta;
        source.item = null;
        source.meta = null;
    }
    
    
    /**
     * Sets the amount.
     * 
     * @param amount the amount
     * @return {@code this}
     */
    public Self amount(int amount) {
        item.setAmount(amount);
        return self();
    }
    
    /**
     * Sets the durability.
     * 
     * @param durability the durability
     * @return {@code this}
     */
    public Self durability(short durability) {
        item.setDurability(durability);
        return self();
    }
    
    
    /**
     * Sets the display name.
     * 
     * @param name the display name
     * @return {@code this}
     */
    public Self display(String name) {
        meta.setDisplayName(name);
        return self();
    }
    
    /**
     * Sets the localised name.
     * 
     * @param name the name
     * @return {@code this}
     */
    public Self localised(String name) {
        meta.setLocalizedName(name);
        return self();
    }
    
    
    /**
     * Adds a modifier to the given attribute.
     * 
     * @param attribute the attribute
     * @param modifier the attribute modifier
     * @return {@code this}
     */
    public Self attribute(Attribute attribute, AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        return self();
    }
    
    /**
     * Adds an enchantment with the given level.
     * 
     * @param enchantment the enchantment
     * @param level the enchantment level; level restrictions are ignored
     * @return {@code this}
     */
    public Self enchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return self();
    }
    
    
    /**
     * Adds the given flags.
     * 
     * @param flags the flags
     * @return {@code this}
     */
    public Self flags(Collection<ItemFlag> flags) {
        return flags(flags.toArray(new ItemFlag[0]));
    }
    
    /**
     * Adds the given flags.
     * 
     * @param flags the flags
     * @return {@code this}
     */
    public Self flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return self();
    }
    
    
    /**
     * Adds the given lore.
     * 
     * @param lines the lore
     * @return {@code this}
     */
    public Self lore(Collection<String> lines) {
        if (lore == null) {
            lore = new ArrayList<>(lines);
            
        } else {
           lore.addAll(lines);
        }
        return self();
    }
    
    /**
     * Adds the given lore.
     * 
     * @param lines the lore
     * @return {@code this}
     */
    public Self lore(String... lines) {
        if (lore == null) {
            lore = new ArrayList<>(lines.length);
        }
        
        lore.addAll(List.of(lines));

        return self();
    }
    
    
    /**
     * Sets the value of {@code type} for the given key.
     * 
     * @param <T> the underlying, primitive type of the value
     * @param <V> the type of the value
     * @param key the key
     * @param type the mapper for the value
     * @param value the value
     * @return {@code this}
     */
    public <T, V> Self tag(NamespacedKey key, ItemTagType<T, V> type, V value) {
        meta.getCustomTagContainer().setCustomTag(key, type, value);
        return self();
    }
    
    
    /**
     * Sets the breakability.
     * 
     * @param unbreakable the breakability
     * @return {@code this}
     */
    public Self unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return self();
    }
    
    
    /**
     * Builds an {@code ItemStack}.
     * 
     * @return the {@code ItemStack}
     */
    public ItemStack build() {
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    
    /**
     * @return {@code this}
     */
    protected abstract Self self();
    
}
