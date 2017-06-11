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
