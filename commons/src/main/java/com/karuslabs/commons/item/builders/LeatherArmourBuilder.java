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

import org.bukkit.*;
import org.bukkit.inventory.meta.*;

import org.checkerframework.checker.nullness.qual.Nullable;


public final class LeatherArmourBuilder extends Builder<LeatherArmorMeta, LeatherArmourBuilder> {
    
    public static LeatherArmourBuilder helmet() {
        return new LeatherArmourBuilder(Material.LEATHER_HELMET);
    }
    
    public static LeatherArmourBuilder chestplate() {
        return new LeatherArmourBuilder(Material.LEATHER_CHESTPLATE);
    }
    
    public static LeatherArmourBuilder leggings() {
        return new LeatherArmourBuilder(Material.LEATHER_LEGGINGS);
    }
    
    public static LeatherArmourBuilder boots() {
        return new LeatherArmourBuilder(Material.LEATHER_BOOTS);
    }
    
    LeatherArmourBuilder(Material material) {
        super(material);
    }
    
    LeatherArmourBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    
    public LeatherArmourBuilder colour(@Nullable Color colour) {
        meta.setColor(colour);
        return this;
    }
    
    
    @Override
    LeatherArmourBuilder self() {
        return this;
    }
    
}
