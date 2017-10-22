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
package com.karuslabs.commons.item.meta;

import com.karuslabs.commons.item.Builder;

import javax.annotation.Nonnull;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;


/**
 * Represents a builder for items with {@code PotionMeta}.
 */
public class PotionBuilder extends Builder<PotionBuilder, PotionMeta> {
    
    /**
     * Constructs a {@code PotionBuilder} for the specified {@code ItemStack}.
     *  
     * @param item the ItemStack
     */
    public PotionBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Constructs a {@code PotionBuilder} which copies the specified {@code Builder}.
     *  
     * @param builder the Builder to be copied
     */
    public PotionBuilder(Builder builder) {
        super(builder);
    }
    
    
    /**
     * Sets the colour.
     * 
     * @param colour the colour
     * @return this
     */
    public PotionBuilder colour(Color colour) {
        meta.setColor(colour);
        return this;
    }
    
    /**
     * Adds the specified effect and whether it should override preexisting effects.
     * 
     * @param effect the potion effect
     * @param override whether the effect should override preexisting effects
     * @return this
     */
    public PotionBuilder effect(PotionEffect effect, boolean override) {
        meta.addCustomEffect(effect, override);
        return this;
    }
    
    /**
     * Sets the data.
     * 
     * @param data the potion data
     * @return this
     */
    public PotionBuilder data(PotionData data) {
        meta.setBasePotionData(data);
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected @Nonnull PotionBuilder getThis() {
        return this;
    }
    
}
