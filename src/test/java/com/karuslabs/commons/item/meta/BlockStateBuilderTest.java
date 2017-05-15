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

import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class BlockStateBuilderTest {
    
    private BlockStateBuilder builder;
    private BlockStateMeta meta;
    
    
    public BlockStateBuilderTest() {
        meta = mock(BlockStateMeta.class);
        builder = new BlockStateBuilder((ItemStack) when(mock(ItemStack.class).getItemMeta()).thenReturn(meta).getMock());
    }
    
    
    @Test
    public void build() {
        BlockState state = mock(BlockState.class);
        builder.state(state);
        
        verify(meta).setBlockState(state);
    }
    
}
