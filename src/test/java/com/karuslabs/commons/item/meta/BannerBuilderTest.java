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

import java.util.*;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


public class BannerBuilderTest {
    
    private BannerBuilder builder;
    private BannerMeta meta;
    
    
    public BannerBuilderTest() {
        meta = mock(BannerMeta.class);
        builder = new BannerBuilder((ItemStack) when(mock(ItemStack.class).getItemMeta()).thenReturn(meta).getMock());
    }
    
    
    @Test
    public void build() {
        List<Pattern> patterns = Collections.singletonList(new Pattern(DyeColor.BLUE, PatternType.BASE));                
        when(meta.getPatterns()).thenReturn(new ArrayList<>());
        
        builder.color(DyeColor.YELLOW).pattern(new Pattern(DyeColor.BLACK, PatternType.BASE)).patterns(patterns);
        
        verify(meta).setBaseColor(DyeColor.YELLOW);
        verify(meta).addPattern(new Pattern(DyeColor.BLACK, PatternType.BASE));
        assertThat(meta.getPatterns(), equalTo(patterns));
    }
    
}
