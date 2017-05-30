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
 * 
 */
public class BannerBuilder extends Builder<BannerBuilder, BannerMeta> {
    
    /**
     *  
     * @param item
     */
    public BannerBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     *  
     * @param builder
     */
    public BannerBuilder(Builder builder) {
        super(builder);
    }    
    
    
    /**
     * 
     * @param color
     * @return 
     */
    public BannerBuilder color(DyeColor color) {
        meta.setBaseColor(color);
        return this;
    }
    
    /**
     * 
     * @param pattern
     * @return 
     */
    public BannerBuilder pattern(Pattern pattern) {
        meta.addPattern(pattern);
        return this;
    }
    
    /**
     * 
     * @param patterns
     * @return 
     */
    public BannerBuilder patterns(List<Pattern> patterns) {
        meta.getPatterns().addAll(patterns);
        return this;
    }
    
    
    /**
     * 
     * @return 
     */
    @Override
    protected BannerBuilder getThis() {
        return this;
    }
    
}
