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

import com.karusmc.commons.core.test.ItemFactoryResource;
import com.karusmc.commons.items.meta.ItemMetaResource;

import org.junit.*;


public class ItemParserIT {
    
    @Rule
    public ItemFactoryResource resource = new ItemFactoryResource();
    
    @Rule
    public ItemMetaResource.get
    
    
    private ItemParser parser;
    
    
    public ItemParserIT() {
        parser = new ItemParser(new ValueStackComponent(), getClass().getClassLoader().getResource("items.xsd").getPath());
    }
    
    
    @Test
    public void parse() {
        parser.parse(getClass().getClassLoader().getResourceAsStream("items.xml"));
    }
    
}
