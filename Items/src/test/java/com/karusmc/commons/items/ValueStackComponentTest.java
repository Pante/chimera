/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.items;

import com.karusmc.commons.core.test.*;
import com.karusmc.commons.items.meta.ItemMetaComponent;

import java.util.HashMap;
import org.bukkit.Material;

import org.bukkit.inventory.ItemStack;

import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class ValueStackComponentTest {
    
    @Rule
    public XMLResource xml = new XMLResource().load(getClass().getClassLoader().getResourceAsStream("banner.xml"), null);
    
    @Rule
    public ItemFactoryResource resource = new ItemFactoryResource();
    
    
    private ValueStackComponent component;
    private ItemMetaComponent meta;
    
    
    public ValueStackComponentTest() {
        component = new ValueStackComponent(new HashMap<>(1));
        component.getComponents().put("banner-meta", meta = mock(ItemMetaComponent.class));
    }
    
    
    @Test
    public void parse() {
        ValueStack value = component.parse(xml.getRoot());
        ItemStack item = value.getItem();
        
        assertEquals("banner", value.getName());
        assertEquals(5, value.getBuy(), 0);
        assertEquals(10, value.getSell(), 0);
        
        assertEquals(Material.BANNER, item.getType());
        assertEquals(16, item.getAmount());
        assertEquals(3, item.getDurability());
        
        verify(meta, times(1)).parse(any(), any());
    }
    
}
