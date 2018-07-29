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


public abstract class Builder<GenericBuilder extends Builder, GenericMeta extends ItemMeta> { 
    
    protected ItemStack item;
    protected GenericMeta meta;

    
    public Builder(@Nonnull Material material) {
        this(new ItemStack(material));
    }
    
    public Builder(@Nonnull ItemStack item) {
        this(item, (GenericMeta) item.getItemMeta());
    }
    
    public Builder(@Nonnull Builder<GenericBuilder, GenericMeta> builder) {
        this(builder.item, builder.meta);
    }

    protected Builder(ItemStack item, GenericMeta meta) {
        this.item = item;
        this.meta = meta;
    }   
    
    
    protected @Nonnull abstract GenericBuilder self();
    
    
    public GenericBuilder amount(int amount) {
        item.setAmount(amount);
        return self();
    }

    public GenericBuilder durability(short data) {
        item.setDurability(data);
        return self();
    }

    
    public GenericBuilder enchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return self();
    }

    public GenericBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        item.addUnsafeEnchantments(enchantments);
        return self();
    }

    
    public GenericBuilder display(String name) {
        meta.setDisplayName(name);
        return self();
    }
    
    public GenericBuilder localised(String name) {
        meta.setLocalizedName(name);
        return self();
    }
    

    public GenericBuilder lore(String lore) {
        if (!meta.hasLore()) {
            meta.setLore(new ArrayList<>(1));
        }
        meta.getLore().add(lore);
        
        return self();
    }
    
    public GenericBuilder lore(List<String> lore) {
        if (!meta.hasLore()) {
            meta.setLore(new ArrayList<>());
        }
        meta.getLore().addAll(lore);
        
        return self();
    }

    
    public GenericBuilder flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return self();
    }

    
    public @Nonnull ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
    
}
