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

import java.util.*;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.bukkit.enchantments.Enchantment.OXYGEN;
import static org.bukkit.inventory.ItemFlag.HIDE_POTION_EFFECTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class BuilderTest {
    
    private ItemMeta meta = mock(ItemMeta.class);
    private ItemStack item = when(mock(ItemStack.class).getItemMeta()).thenReturn(meta).getMock();
    private Builder builder = new StubBuilder(item);

    
    @Test
    public void builder() {
        Builder other = new StubBuilder(builder);
        
        assertTrue(builder.item == other.item);
        assertTrue(builder.meta == other.meta);
    }
    
    
    @Test
    public void build() {
        builder.amount(1).durability((short) 8).enchantment(OXYGEN, 2).enchantments(EMPTY_MAP).flags(HIDE_POTION_EFFECTS).name("lol").build();
        
        verify(item).setAmount(1);
        verify(item).setDurability((short) 8);
        verify(item).addUnsafeEnchantment(OXYGEN, 2);
        verify(item).addUnsafeEnchantments(EMPTY_MAP);
        
        verify(meta).addItemFlags(HIDE_POTION_EFFECTS);
        verify(meta).setDisplayName("lol");
        
        verify(item).setItemMeta(meta);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 0", "false, 1"})
    public void lore_String(boolean hasLore, int times) {
        when(meta.hasLore()).thenReturn(hasLore);
        when(meta.getLore()).thenReturn(new ArrayList<>());
        
        builder.lore("line");
        
        verify(meta, times(times)).setLore(eq(EMPTY_LIST));
        assertEquals(singletonList("line"), meta.getLore());
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 0", "false, 1"})
    public void lore_List(boolean hasLore, int setTimes) {
        when(meta.hasLore()).thenReturn(hasLore);
        when(meta.getLore()).thenReturn(new ArrayList<>());
        
        List<String> lines = asList("line 1", "line 2"); 
        builder.lore(lines);
        
        verify(meta, times(setTimes)).setLore(eq(EMPTY_LIST));
        assertEquals(lines, meta.getLore());
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
