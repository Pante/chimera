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

import org.bukkit.*;
import org.bukkit.inventory.meta.*;

/**
 * A compass builder.
 */
public final class CompassBuilder extends Builder<CompassMeta, CompassBuilder> {
    
    /**
     * Creates a {@code CompassBuilder}.
     * 
     * @return a {@code CompassBuilder}
     */
    public static CompassBuilder of() {
        return new CompassBuilder();
    }
    
    CompassBuilder() {
        super(Material.COMPASS);
    }
    
    CompassBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    /**
     * Sets the lodestone.
     * 
     * @param lodestone the location of the lodestone
     * @return {@code this}
     */
    public CompassBuilder lodestone(Location lodestone) {
        meta.setLodestone(lodestone);
        return this;
    }
    
    /**
     * Sets whether to track a lodestone.
     * 
     * @param tracked {@code true} if the compass should track a lodestone; else {@code false}
     * @return {@code this}
     */
    public CompassBuilder track(boolean tracked) {
        meta.setLodestoneTracked(tracked);
        return this;
    }

    @Override
    CompassBuilder self() {
        return this;
    }

}
