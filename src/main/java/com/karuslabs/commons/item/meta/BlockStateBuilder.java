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

import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

/**
 * A builder system specifically used to modify and create various block states. 
 */
public class BlockStateBuilder extends Builder<BlockStateBuilder, BlockStateMeta> {
    
    /**
     *  Constructs a <code>BlockStateBuilder</code> from the specified <code>ItemStack</code>.
     *  
     * @param item the ItemStack
     */
    public BlockStateBuilder(ItemStack item) {
        super(item);
    }
    
    /**
     *  Constructs a <code>BlockStateBuilder</code> from the specified <code>Builder</code>.
     *  
     * @param builder the Builder
     */
    public BlockStateBuilder(Builder builder) {
        super(builder);
    }
    
    /**
     * Sets the block state to the specified <code>BlockState</code>.
     * 
     * @param state the Blockstate
     * @return the modified <code>BlockStateBuilder</code> instance
     */
    public BlockStateBuilder state(BlockState state) {
        meta.setBlockState(state);
        return this;
    }
    
    /**
     * @return the current <code>BlockStateBuilder</code> instance
     */
    @Override
    protected BlockStateBuilder getThis() {
        return this;
    }
    
}
