/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.item.meta;

import com.karuslabs.commons.item.Builder;

import java.util.*;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;


/**
 * Represents a specialised <code>Builder</code> for building items with <code>BannerMeta</code>.
 */
public class BannerBuilder extends Builder<BannerBuilder, BannerMeta> {
    
    /**
     *  Constructs a <code>BannerBuilder</code> with the specified <code>ItemStack</code>.
     *  
     * @param item the ItemStack
     */
    public BannerBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     * Copy constructor which constructs a <code>BannerBuilder</code> with the specified <code>Builder</code>.
     *  
     * @param builder the Builder
     */
    public BannerBuilder(Builder builder) {
        super(builder);
    }    
    
    
    /**
     * Sets the base colour to the specified <code>DyeColor</code>.
     * 
     * @param color the Color 
     * @return this
     */
    public BannerBuilder color(DyeColor color) {
        meta.setBaseColor(color);
        return this;
    }
    
    /**
     * Adds the specified <code>Pattern</code>.
     * 
     * @param pattern the Pattern
     * @return this
     */
    public BannerBuilder pattern(Pattern pattern) {
        meta.addPattern(pattern);
        return this;
    }
    
    /**
     * Adds the specified <code>Pattern</code>s.
     * 
     * @param patterns the Patterns list
     * @return this
     */
    public BannerBuilder patterns(List<Pattern> patterns) {
        meta.getPatterns().addAll(patterns);
        return this;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected BannerBuilder getThis() {
        return this;
    }
    
}
