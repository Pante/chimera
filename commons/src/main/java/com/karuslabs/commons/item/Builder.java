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
package com.karuslabs.commons.item;

import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import static java.util.Arrays.asList;


public abstract class Builder<Meta extends ItemMeta, This extends Builder> {
    
    ItemStack item;
    Meta meta;
    private @Nullable List<String> lore;
    
    
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
    
    
    public This amount(int amount) {
        item.setAmount(amount);
        return self();
    }
    
    public This durability(short durability) {
        item.setDurability(durability);
        return self();
    }
    
    
    public This display(String name) {
        meta.setDisplayName(name);
        return self();
    }
    
    public This localised(String name) {
        meta.setLocalizedName(name);
        return self();
    }
    
    
    public This enchantment(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return self();
    }
    
    
    public This flags(Collection<ItemFlag> flags) {
        meta.addItemFlags(flags.toArray(new ItemFlag[0]));
        return self();
    }
    
    public This flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return self();
    }
    
    
    public This lore(Collection<String> lore) {
        if (this.lore == null) {
            this.lore = new ArrayList<>(lore);
            
        } else {
           this.lore.addAll(lore);
        }
        return self();
    }
    
    public This lore(String... lore) {
        if (this.lore == null) {
            this.lore = asList(lore);
            
        } else {
            for (var line : lore) {
                this.lore.add(line);
            }
        }
        return self();
    }
    
    
    public This unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        return self();
    }
    
    
    public ItemStack build() {
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    
    
    protected abstract This self();
    
}
