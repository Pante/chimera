/*
 * The MIT License
 *
 * Copyright 2020 Karus Labs.
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
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.meta.*;

/**
 * A {@code BlockData} builder.
 */
public final class BlockDataBuilder extends Builder<BlockDataMeta, BlockDataBuilder> {
    
    /**
     * Creates a {@code BlockDataBuilder} for the given material.
     * 
     * @param material the material
     * @return a {@code BlockDataBuilder}
     */
    public static BlockDataBuilder of(Material material) {
        return new BlockDataBuilder(material);
    }
    
    BlockDataBuilder(Material material) {
        super(material);
    }
    
    BlockDataBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    /**
     * Sets the {@code BlockData}.
     * 
     * @param data the {@code BlockData}
     * @return {@code this}
     */
    public BlockDataBuilder data(BlockData data) {
        meta.setBlockData(data);
        return this;
    }
    
    @Override
    BlockDataBuilder self() {
        return this;
    }

}
