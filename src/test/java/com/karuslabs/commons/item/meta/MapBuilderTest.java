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

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class MapBuilderTest {
        
    private MapBuilder builder;
    private MapMeta meta;
    
    
    public MapBuilderTest() {
        meta = mock(MapMeta.class);
        builder = new MapBuilder((ItemStack) when(mock(ItemStack.class).getItemMeta()).thenReturn(meta).getMock());
    }
    
    
    @Test
    public void build() {
        builder.color(Color.SILVER).locationName("name").scaling(true);
        
        verify(meta).setColor(Color.SILVER);
        verify(meta).setLocationName("name");
        verify(meta).setScaling(true);
    }
    
}
