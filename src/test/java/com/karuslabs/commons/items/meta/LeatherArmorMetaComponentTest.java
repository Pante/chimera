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
package com.karuslabs.commons.items.meta;

import com.karuslabs.commons.items.meta.LeatherArmorMetaComponent;
import com.karuslabs.commons.core.test.XMLResource;

import org.bukkit.Color;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import org.junit.*;

import static org.mockito.Mockito.*;


public class LeatherArmorMetaComponentTest {
    
    @Rule
    public XMLResource xml = new XMLResource().load(getClass().getClassLoader().getResourceAsStream("items/meta/LeatherArmorMeta.xml"), null);
    
    @Rule
    public ItemMetaResource<LeatherArmorMeta> resource = new ItemMetaResource(new LeatherArmorMetaComponent(), mock(LeatherArmorMeta.class));
    
    
    @Test
    public void parse() {
        resource.parse(xml.getRoot());
        resource.assertMeta();
        
        LeatherArmorMeta meta = resource.getMeta();
        
        verify(meta, times(1)).setColor(Color.fromRGB(1, 2, 3));
    }
    
}
