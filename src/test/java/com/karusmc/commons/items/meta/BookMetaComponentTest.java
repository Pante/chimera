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
package com.karusmc.commons.items.meta;

import com.karusmc.commons.core.test.XMLResource;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.BookMeta;

import org.junit.*;

import static org.mockito.Mockito.*;


public class BookMetaComponentTest {
    
    @Rule
    public XMLResource xml = new XMLResource().load(getClass().getClassLoader().getResourceAsStream("items/meta/BookMeta.xml"), null);
    
    @Rule
    public ItemMetaResource<BookMeta> resource = new ItemMetaResource(new BookMetaComponent(), mock(BookMeta.class));
    
    
    @Test
    public void parse() {
        resource.parse(xml.getRoot());
        resource.assertMeta();
        
        BookMeta meta = resource.getMeta();
        
        verify(meta, times(1)).setAuthor("Aldous Huxley");
        verify(meta, times(1)).setTitle(ChatColor.translateAlternateColorCodes('&', "&cBrave New World"));
        verify(meta, times(1)).addPage(ChatColor.translateAlternateColorCodes('&',"&cSQUAT grey building of only thirty-four stories."));
    }
}
