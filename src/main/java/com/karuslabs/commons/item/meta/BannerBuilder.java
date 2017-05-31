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
 * A builder system specifically used to modify and create banners.
 */
public class BannerBuilder extends Builder<BannerBuilder, BannerMeta> {
    
    /**
     *  Constructs a <code>BannerBuilder</code> from the specified <code>ItemStack</code>.
     *  The material type of the specified <code>ItemStack</code> must be BANNER.
     *  
     * @param item the Banner
     */
    public BannerBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     *  Constructs a <code>BannerBuilder</code> from an existing <code>Builder</code>.
     *  
     * @param builder the Builder
     */
    public BannerBuilder(Builder builder) {
        super(builder);
    }    
    
    
    /**
     * Sets the base color to the specified <code>DyeColor</code>.
     * 
     * @param color the Color 
     * @return the modified <code>BannerBuilder</code> instance
     */
    public BannerBuilder color(DyeColor color) {
        meta.setBaseColor(color);
        return this;
    }
    
    /**
     * Adds the specified <code>Pattern</code>.
     * 
     * @param pattern the Pattern
     * @return the modified <code>BannerBuilder</code> instance
     */
    public BannerBuilder pattern(Pattern pattern) {
        meta.addPattern(pattern);
        return this;
    }
    
    /**
     * Adds all the specified <code>Pattern</code> to the meta.
     * 
     * @param patterns the Patterns list
     * @return the modified <code>BannerBuilder</code> instance
     */
    public BannerBuilder patterns(List<Pattern> patterns) {
        meta.getPatterns().addAll(patterns);
        return this;
    }
    
    
    /**
     * @return the current <code>BannerBuilder</code> instance
     */
    @Override
    protected BannerBuilder getThis() {
        return this;
    }
    
}
