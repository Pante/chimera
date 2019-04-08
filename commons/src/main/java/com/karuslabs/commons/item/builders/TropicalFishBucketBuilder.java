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
import org.bukkit.entity.TropicalFish.Pattern;
import org.bukkit.inventory.meta.*;


/**
 * A builder for items with {@code TropicalFishBucketMeta}.
 */
public class TropicalFishBucketBuilder extends Builder<TropicalFishBucketMeta, TropicalFishBucketBuilder> {
    
    /**
     * Creates a {@code TropicalFishBucketBuilder} for the given material.
     * 
     * @param material the material
     * @return a {@code TropicalFishBucketBuilder}
     */
    public static TropicalFishBucketBuilder of(Material material) {
        return new TropicalFishBucketBuilder(material);
    }
    
    TropicalFishBucketBuilder(Material material) {
        super(material);
    }
    
    TropicalFishBucketBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    
    /**
     * Sets the body colour.
     * 
     * @param colour the body colour
     * @return {@code this}
     */
    public TropicalFishBucketBuilder body(DyeColor colour) {
        meta.setBodyColor(colour);
        return this;
    }
    
    /**
     * Sets the pattern.
     * 
     * @param pattern the pattern
     * @return {@code this}
     */
    public TropicalFishBucketBuilder pattern(Pattern pattern) {
        meta.setPattern(pattern);
        return this;
    }
    
    /**
     * Sets the pattern colour.
     * 
     * @param colour the pattern colour
     * @return {@code this}
     */
    public TropicalFishBucketBuilder pattern(DyeColor colour) {
        meta.setPatternColor(colour);
        return this;
    }
    

    @Override
    protected TropicalFishBucketBuilder self() {
        return this;
    }
    
}
