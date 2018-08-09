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

import com.karuslabs.commons.item.builders.Builder;
import com.karuslabs.commons.item.builders.ItemBuilder;
import java.util.*;

import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.bukkit.Material.*;
import static org.bukkit.enchantments.Enchantment.CHANNELING;
import static org.bukkit.inventory.ItemFlag.HIDE_DESTROYS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BuilderTest {
    
    @Mock ItemStack item;
    ItemMeta meta = StubBukkit.meta(ItemMeta.class);
    Builder<ItemMeta, ItemBuilder> builder = ItemBuilder.of(WATER);
    
    
    @Test
    void lore_collection() {
        builder.lore(List.of("A", "B")).lore(List.of("C"));
        assertEquals(builder.lore, List.of("A", "B", "C"));
    }
    
    
    @Test
    void lore_array() {
        builder.lore("A", "B").lore("C");
        assertEquals(builder.lore, List.of("A", "B", "C"));
    }
    
    
    @Test
    void build() {
        builder.item = item;
        builder.amount(10).durability((short) 20)
               .display("display name").localised("localised name")
               .enchantment(CHANNELING, 1).flags(Set.of(HIDE_DESTROYS))
               .unbreakable(true).build();
        
        verify(item).setAmount(10);
        verify(item).setDurability((short) 20);
        verify(meta).setDisplayName("display name");
        verify(meta).setLocalizedName("localised name");
        verify(meta).addEnchant(CHANNELING, 1, true);
        verify(meta).addItemFlags(HIDE_DESTROYS);
        verify(meta).setUnbreakable(true);
        verify(meta).setLore(null);
        verify(item).setItemMeta(meta);
    }
    
    
    @Test
    void reset() {
        var builder = ItemBuilder.of(APPLE).lore("A");
        
        var first = builder.build();
        var second = builder.reset(ACACIA_BOAT).build();
        
        assertNotSame(first, second);
        assertTrue(builder.lore.isEmpty());
    }
    
}
