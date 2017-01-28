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

import com.karusmc.commons.core.test.XMLResource;
import com.karusmc.commons.core.xml.Component;

import org.jdom2.Element;

import org.junit.*;

import static org.mockito.Mockito.*;


public class ItemParserTest {
    
    @Rule
    public XMLResource resource = new XMLResource().load(getClass().getClassLoader().getResourceAsStream("empty-items.xml"), null);
    
    
    private ItemParser parser;
    private Component<ValueStack> component;
    
    
    public ItemParserTest() {
        component = mock(Component.class);
        parser = new ItemParser(component);
    }
    
    
    @Test
    public void parse() {
        ValueStack item = mock(ValueStack.class);
        when(item.getName()).thenReturn("name");
        when(component.parse(any(Element.class))).thenReturn(item);
        
        parser.parse(resource.getRoot());
        
        verify(component, times(4)).parse(any(Element.class));
    }
    
}
