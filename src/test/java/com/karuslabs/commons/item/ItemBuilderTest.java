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

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


public class ItemBuilderTest {
    
    private ItemStack item;
    
    
    public ItemBuilderTest() {
        item = mock(ItemStack.class);
    }
    
    
    @Test
    public void asBanner() {
        when(item.getItemMeta()).thenReturn(mock(BannerMeta.class));
        assertNotNull(new ItemBuilder(item).asBanner());
    }
    
    @Test
    public void asBlockState() {
        when(item.getItemMeta()).thenReturn(mock(BlockStateMeta.class));
        assertNotNull(new ItemBuilder(item).asBlockState());
    }
    
    @Test
    public void asBook() {
        when(item.getItemMeta()).thenReturn(mock(BookMeta.class));
        assertNotNull(new ItemBuilder(item).asBook());
    }
    
    @Test
    public void asEnchantmentStorage() {
        when(item.getItemMeta()).thenReturn(mock(EnchantmentStorageMeta.class));
        assertNotNull(new ItemBuilder(item).asEnchantmentStorage());
    }
    
    @Test
    public void asFirework() {
        when(item.getItemMeta()).thenReturn(mock(FireworkMeta.class));
        assertNotNull(new ItemBuilder(item).asFirework());
    }
    
    @Test
    public void asFireworkEffect() {
        when(item.getItemMeta()).thenReturn(mock(FireworkEffectMeta.class));
        assertNotNull(new ItemBuilder(item).asFireworkEffect());
    }
    
    @Test
    public void asKnowledgeBook() {
        when(item.getItemMeta()).thenReturn(mock(KnowledgeBookMeta.class));
        assertNotNull(new ItemBuilder(item).asKnowledgeBook());
    }
    
    @Test
    public void asLeatherArmor() {
        when(item.getItemMeta()).thenReturn(mock(LeatherArmorMeta.class));
        assertNotNull(new ItemBuilder(item).asLeatherArmor());
    }
    
    @Test
    public void asMap() {
        when(item.getItemMeta()).thenReturn(mock(MapMeta.class));
        assertNotNull(new ItemBuilder(item).asMap());
    }
    
    @Test
    public void asPotion() {
        when(item.getItemMeta()).thenReturn(mock(PotionMeta.class));
        assertNotNull(new ItemBuilder(item).asPotion());
    }
    
    @Test
    public void asSkull() {
        when(item.getItemMeta()).thenReturn(mock(SkullMeta.class));
        assertNotNull(new ItemBuilder(item).asSkull());
    }
    
    @Test
    public void asSpawnEgg() {
        when(item.getItemMeta()).thenReturn(mock(SpawnEggMeta.class));
        assertNotNull(new ItemBuilder(item).asSpawnEgg());
    }
    
}
