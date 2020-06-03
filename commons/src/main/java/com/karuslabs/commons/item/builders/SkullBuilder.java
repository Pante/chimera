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

import com.karuslabs.commons.item.Head;

import java.util.UUID;

import org.bukkit.*;
import org.bukkit.inventory.meta.*;

import static org.bukkit.Bukkit.getOfflinePlayer;

import org.checkerframework.checker.nullness.qual.Nullable;


/**
 * A skull builder.
 */
public class SkullBuilder extends Builder<SkullMeta, SkullBuilder> {
    
    /**
     * Creates a {@code SkullBuilder} for the given material.
     * 
     * @param material the material
     * @return a {@code SkullBuilder}
     */
    public static SkullBuilder of(Material material) {
        return new SkullBuilder(material);
    }
    
    SkullBuilder(Material material) {
        super(material);
    }
    
    SkullBuilder(Builder<ItemMeta, ?> source) {
        super(source);
    }
    
    
    /**
     * Sets the head.
     * 
     * @param head the head
     * @return {@code this}
     */
    public SkullBuilder head(Head head) {
        return head(head.id);
    }
    
    /**
     * Sets the UUID.
     * 
     * @param id the UUID
     * @return {@code this}
     */
    public SkullBuilder head(UUID id) {
        return head(getOfflinePlayer(id));
    }
    
    /**
     * Sets the owner of the skull.
     * 
     * @param player the owner of the skull
     * @return {@code this}
     */
    public SkullBuilder head(@Nullable OfflinePlayer player) {
        meta.setOwningPlayer(player);
        return this;
    }

    
    @Override
    SkullBuilder self() {
        return this;
    }
    
}
