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

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.*;

public final class EnchantmentStorageBuilder extends Builder<EnchantmentStorageMeta, EnchantmentStorageBuilder> {
    
    public static EnchantmentStorageBuilder of() {
        return new EnchantmentStorageBuilder();
    }
    
    EnchantmentStorageBuilder() {
        super(Material.ENCHANTED_BOOK);
    }
    
    EnchantmentStorageBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    public EnchantmentStorageBuilder stored(Enchantment enchantment, int level) {
        meta.addStoredEnchant(enchantment, level, true);
        return this;
    }
    
    @Override
    EnchantmentStorageBuilder self() {
        return this;
    }
    
}
