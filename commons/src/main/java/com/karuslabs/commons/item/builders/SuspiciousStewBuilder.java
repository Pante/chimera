/*
 * The MIT License
 *
 * Copyright 2019 Karus Labs.
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
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;


/**
 * A builder for suspicious stews.
 */
public class SuspiciousStewBuilder extends Builder<SuspiciousStewMeta, SuspiciousStewBuilder> {
    
    /**
     * Creates a {@code SuspiciousStewBuilder}.
     * 
     * @return a {@code SuspiciousStewBuilder}
     */
    public static SuspiciousStewBuilder of() {
        return new SuspiciousStewBuilder(Material.SUSPICIOUS_STEW);
    }
    
    SuspiciousStewBuilder(Material material) {
        super(material);
    }
    
    SuspiciousStewBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    
    /**
     * Adds the given effect.
     * 
     * @param effect the effect
     * @param overwrite {@code true} to override an effect if present; else {@code false}
     * @return {@code this}
     */
    public SuspiciousStewBuilder effect(PotionEffect effect, boolean overwrite) {
        meta.addCustomEffect(effect, overwrite);
        return this;
    }
    

    @Override
    SuspiciousStewBuilder self() {
        return this;
    }
    
}
