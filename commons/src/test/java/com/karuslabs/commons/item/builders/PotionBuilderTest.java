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

import com.karuslabs.commons.MockBukkit;

import org.bukkit.Material;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.*;

import org.junit.jupiter.api.Test;

import static org.bukkit.Color.SILVER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PotionBuilderTest {
    
    PotionMeta meta = MockBukkit.meta(PotionMeta.class);
    PotionData data = mock(PotionData.class);
    PotionEffect effect = mock(PotionEffect.class);
    
    @Test
    void constructors() {
        assertEquals(PotionBuilder.lingering().build().getType(), Material.LINGERING_POTION);
        assertEquals(PotionBuilder.potion().build().getType(), Material.POTION);
        assertEquals(PotionBuilder.splash().build().getType(), Material.SPLASH_POTION);
    }
    
    @Test
    void build() {
        PotionBuilder.potion().self().colour(SILVER).data(data).effect(effect);
        
        verify(meta).setColor(SILVER);
        verify(meta).setBasePotionData(data);
        verify(meta).addCustomEffect(effect, true);
    }
    
}
