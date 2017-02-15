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
package com.karusmc.commons.menu.xml;

import com.karusmc.commons.menu.xml.ItemStackComponent;
import com.karusmc.commons.core.test.*;
import com.karusmc.commons.items.meta.ItemMetaComponent;

import org.bukkit.*;
import org.bukkit.inventory.ItemStack;

import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class ItemStackComponentTest {
    
    @Rule
    public XMLResource resource = new XMLResource().load(getClass().getClassLoader().getResourceAsStream("menu/itemstack.xml"), null);

    private Server server;
    private ItemStackComponent component;
    private ItemMetaComponent meta;
    
    
    public ItemStackComponentTest() {
        server = StubServer.INSTANCE;
        component = new ItemStackComponent();
        meta = mock(ItemMetaComponent.class);
        
        component.getComponents().put("mock-meta", meta);
    }
    
    
    @Test
    public void parse() {
        ItemStack item = component.parse(resource.getRoot());
        
        verify(meta, times(1)).parse(any(), any());
        assertEquals(new ItemStack(Material.BANNER, 16, (short) 0), item);
    }
    
}
