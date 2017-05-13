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
package com.karuslabs.commons.menu.items;

import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;


public class BannerBuilder extends Builder<BannerBuilder, BannerMeta> {
    
    protected BannerBuilder(ItemStack item, BannerMeta meta) {
        super(item, meta);
    }
    
    
    public BannerBuilder color(DyeColor color) {
        meta.setBaseColor(color);
        return this;
    }
    
    public BannerBuilder pattern(Pattern pattern) {
        meta.addPattern(pattern);
        return this;
    }
    
    public BannerBuilder pattern(int index, Pattern pattern) {
        meta.setPattern(index, pattern);
        return this;
    }
    
    public BannerBuilder patterns(List<Pattern> patterns) {
        meta.setPatterns(patterns);
        return this;
    }
    

    @Override
    protected BannerBuilder getThis() {
        return this;
    }
    
}
