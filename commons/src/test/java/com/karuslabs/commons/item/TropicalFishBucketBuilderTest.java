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
package com.karuslabs.commons.item;

import org.bukkit.inventory.meta.*;

import org.junit.jupiter.api.Test;

import static org.bukkit.DyeColor.*;
import static org.bukkit.Material.WATER;
import static org.bukkit.entity.TropicalFish.Pattern.DASHER;
import static org.mockito.Mockito.*;


class TropicalFishBucketBuilderTest {
    
    TropicalFishBucketMeta meta = StubBukkit.meta(TropicalFishBucketMeta.class);
    TropicalFishBucketBuilder builder = TropicalFishBucketBuilder.of(WATER);
    
    
    @Test
    void build() {
        builder.self().body(BLUE).pattern(RED).pattern(DASHER);
        
        verify(meta).setBodyColor(BLUE);
        verify(meta).setPatternColor(RED);
        verify(meta).setPattern(DASHER);
    }
    
}
