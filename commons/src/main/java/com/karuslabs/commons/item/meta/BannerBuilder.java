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

import java.util.*;
import javax.annotation.Nonnull;

import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import static org.bukkit.Material.BANNER;


public class BannerBuilder extends Builder<BannerBuilder, BannerMeta> {
    
    public BannerBuilder() {
        this(new ItemStack(BANNER));
    }
    
    public BannerBuilder(ItemStack item) {
        super(item);
    }
    
    public BannerBuilder(Builder builder) {
        super(builder);
    }    

    
    public BannerBuilder pattern(Pattern pattern) {
        meta.addPattern(pattern);
        return this;
    }
    
    public BannerBuilder patterns(List<Pattern> patterns) {
        meta.getPatterns().addAll(patterns);
        return this;
    }
    

    @Override
    protected @Nonnull BannerBuilder self() {
        return this;
    }
    
}
