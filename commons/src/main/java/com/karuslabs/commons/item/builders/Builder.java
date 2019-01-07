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


public abstract class Builder<Meta extends ItemMeta, Self extends Builder> {
    
    ItemStack item;
    Meta meta;
    @Nullable List<String> lore;
    
    
    public Builder(Material material) {
        item = new ItemStack(material);
        meta = (Meta) item.getItemMeta();
    }
    
    public Builder(Builder<ItemMeta, ?> source) {
        item = source.item;
        meta = (Meta) source.meta;
        source.item = null;
        source.meta = null;
    }
    
    
    public Self amount(int amount) {
        item.setAmount(amount);
        return self();
    }
    
    public Self durability(short durability) {
        item.setDurability(durability);
        return self();
    }
    
    
    public Self display(String name) {
        meta.setDisplayName(name);
        return self();
    }
    
    public Self localised(String name) {
        meta.setLocalizedName(name);
        return self();
    }
    
    
    public Self attribute(Attribute attribute, AttributeModifier modifier) {
        meta.addAttributeModifier(attribute, modifier);
        return self();
    }
    
    public Self enchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return self();
    }
    
    
    public Self flags(Collection<ItemFlag> flags) {
        return flags(flags.toArray(new ItemFlag[0]));
    }
    
    public Self flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return self();
    }
    
    
    public Self lore(Collection<String> lines) {
        if (lore == null) {
            lore = new ArrayList<>(lines);
            
        } else {
           lore.addAll(lines);
        }
        return self();
    }
    
    public Self lore(String... lines) {
        if (lore == null) {
            lore = new ArrayList<>(lines.length);
        }
        
        lore.addAll(List.of(lines));

        return self();
    }
    
    
    public <T, V> Self tag(NamespacedKey key, ItemTagType<T, V> type, V value) {
        meta.getCustomTagContainer().setCustomTag(key, type, value);
        return self();
    }
    
    
    public Self unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return self();
    }
    
    
    public ItemStack build() {
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    
    protected abstract Self self();
    
}
