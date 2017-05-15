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
package com.karuslabs.commons.item;

import com.google.common.collect.Lists;

import java.util.*;

import junitparams.*;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.Collections.EMPTY_MAP;
import static org.bukkit.enchantments.Enchantment.OXYGEN;
import static org.bukkit.inventory.ItemFlag.HIDE_POTION_EFFECTS;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class BuilderTest {
    
    private ItemStack item;
    private ItemMeta meta;
    private Builder builder;
    
    
    public BuilderTest() {
        meta = mock(ItemMeta.class);
        item = when(mock(ItemStack.class).getItemMeta()).thenReturn(meta).getMock();
        builder = new StubBuilder(item);
    }
    
    
    @Test
    public void builder() {
        Builder aBuilder = new StubBuilder(builder);
        
        assertTrue(aBuilder.item == builder.item);
        assertTrue(aBuilder.meta == builder.meta);
    }
    
    
    @Test
    public void build() {
        builder.amount(1).durability((short) 8).enchantment(OXYGEN, 2).enchantments(EMPTY_MAP).flags(HIDE_POTION_EFFECTS).name("lol").build();
        
        verify(item).setAmount(1);
        verify(item).setDurability((short) 8);
        verify(item).addUnsafeEnchantment(Enchantment.OXYGEN, 2);
        verify(item).addUnsafeEnchantments(Collections.EMPTY_MAP);
        
        verify(meta).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        verify(meta).setDisplayName("lol");
        
        verify(item).setItemMeta(meta);
    }
    
    
    @Test
    @Parameters({"true, 0", "false, 1"})
    public void lore_String(boolean hasLore, int setTimes) {
        when(meta.hasLore()).thenReturn(hasLore);
        when(meta.getLore()).thenReturn(new ArrayList<>());
        
        builder.lore("line");
        
        verify(meta, times(setTimes)).setLore(eq(Collections.EMPTY_LIST));
        assertThat(meta.getLore(), equalTo(Collections.singletonList("line")));
    }
    
    
    @Test
    @Parameters({"true, 0", "false, 1"})
    public void lore_List(boolean hasLore, int setTimes) {
        when(meta.hasLore()).thenReturn(hasLore);
        when(meta.getLore()).thenReturn(new ArrayList<>());
        
        List<String> lines = Lists.newArrayList("line 1", "line 2");
        
        builder.lore(lines);
        
        verify(meta, times(setTimes)).setLore(eq(Collections.EMPTY_LIST));
        assertThat(meta.getLore(), equalTo(lines));
    }
    
    
    private static class StubBuilder extends Builder<StubBuilder, ItemMeta> {
        
        public StubBuilder(Builder builder) {
            super(builder);
        }
        
        public StubBuilder(ItemStack item) {
            super(item);
        }

        @Override
        protected StubBuilder getThis() {
            return this;
        }
        
    }
    
    
}
