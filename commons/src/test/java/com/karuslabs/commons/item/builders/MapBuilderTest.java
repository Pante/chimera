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

import com.karuslabs.commons.item.builders.MapBuilder;
import org.bukkit.inventory.meta.MapMeta;

import org.junit.jupiter.api.Test;

import static org.bukkit.Color.SILVER;
import static org.bukkit.Material.WATER;
import static org.mockito.Mockito.*;


class MapBuilderTest {
    
    MapMeta meta = StubBukkit.meta(MapMeta.class);
    
    
    @Test
    void build() {
        MapBuilder.of(WATER).self().colour(SILVER).id(1).location("name").scaling(true);
        
        verify(meta).setColor(SILVER);
        verify(meta).setMapId(1);
        verify(meta).setLocationName("name");
        verify(meta).setScaling(true);
    }
    
}
