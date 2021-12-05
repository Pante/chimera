/*
 * The MIT License
 *
 * Copyright 2021 Karus Labs.
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
import org.bukkit.entity.Axolotl.Variant;
import org.bukkit.inventory.meta.*;

/**
 * A axolotl bucket builder.
 */
public final class AxolotlBucketBuilder extends Builder<AxolotlBucketMeta, AxolotlBucketBuilder> {

    /**
     * Creates a {@code AxolotlBucketBuilder}.
     * 
     * @return a {@code AxolotlBucketBuilder}
     */
    public static AxolotlBucketBuilder of() {
        return new AxolotlBucketBuilder(Material.AXOLOTL_BUCKET);
    }
    
    AxolotlBucketBuilder(Material material) {
        super(material);
    }
    
    AxolotlBucketBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    /**
     * Sets the {@code Variant}.
     * 
     * @param variant the {@code Variant}
     * @return {@code this}
     */
    public AxolotlBucketBuilder variant(Variant variant) {
        meta.setVariant(variant);
        return this;
    }
    
    @Override
    AxolotlBucketBuilder self() {
        return this;
    }

}
