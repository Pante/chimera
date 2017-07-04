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
