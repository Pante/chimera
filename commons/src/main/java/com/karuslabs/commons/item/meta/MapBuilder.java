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
import org.bukkit.inventory.meta.MapMeta;


/**
 * Represents a builder for items with {@code MapMeta}.
 */
public class MapBuilder extends Builder<MapBuilder, MapMeta> {

    /**
     * Constructs a {@code MapBuilder} for the specified {@code ItemStack}.
     *  
     * @param item the ItemStack
     */
    public MapBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Constructs a {@code MapBuilder} which copies the specified {@code Builder}.
     *  
     * @param builder the Builder to be copied
     */
    public MapBuilder(Builder builder) {
        super(builder);
    }
    
    
    /**
     * Sets the colour.
     * 
     * @param colour the colour
     * @return this
     */
    public MapBuilder colour(Color colour) {
        meta.setColor(colour);
        return this;
    }
    
    /**
     * Sets the name of the map location.
     * 
     * @param name the name
     * @return this
     */
    public MapBuilder locationName(String name) {
        meta.setLocationName(name);
        return this;
    }
    
    /**
     * Sets whether the map follows scaling.
     * 
     * @param value whether the map scales
     * @return this
     */
    public MapBuilder scaling(boolean value) {
        meta.setScaling(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @Nonnull MapBuilder getThis() {
        return this;
    }
    
}
