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

import com.karusmc.commons.menu.xml.InventoryParser;
import com.karusmc.commons.menu.xml.ItemStackComponent;
import com.karusmc.commons.core.test.XMLResource;
import com.karusmc.commons.core.xml.ParserException;

import junitparams.*;

import org.bukkit.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import org.jdom2.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class InventoryParserTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    @Rule
    public XMLResource resource = new XMLResource().load(getClass().getClassLoader().getResourceAsStream("menu/menu.xml"), null);
    
    private InventoryParser parser;
    private Server server;
    private ItemStackComponent component;
    
    
    public InventoryParserTest() {
        component = mock(ItemStackComponent.class);
        server = mock(Server.class);
        parser = spy(new InventoryParser(server, component));
    }
    
    
    @Test
    @Parameters
    public void createInventory(InventoryType type, int sizeTimes, int typeTimes) {
        parser.createInventory("", type, 9);
        
        verify(server, times(sizeTimes)).createInventory(null, 9, "");
        verify(server, times(typeTimes)).createInventory(null, type, "");
    }
    
    
    protected Object[] parametersForCreateInventory() {
        return new Object[] {
            new Object[] {InventoryType.CRAFTING, 0, 1},
            new Object[] {InventoryType.CHEST, 1, 0}
        };
    }
    
    
    @Test
    public void setSlot() throws DataConversionException {
        Inventory inventory = mock(Inventory.class);
        when(inventory.getSize()).thenReturn(18);
        when(component.parse(any(Element.class))).thenReturn(new ItemStack(Material.BANNER));
        
        parser.setSlot(resource.getRoot().getChild("slot"), inventory);
        
        verify(inventory, times(1)).setItem(1, new ItemStack(Material.BANNER));
    }
    
    @Test
    public void setSlot_ThrowsException() throws DataConversionException {
        exception.expect(ParserException.class);
        exception.expectMessage("Invalid slot: " + 1);
        
        Inventory inventory = mock(Inventory.class);
        when(inventory.getSize()).thenReturn(-999);
        
        parser.setSlot(resource.getRoot().getChild("slot"), inventory);
    }
    
    
    @Test
    public void parse() throws DataConversionException {
        doNothing().when(parser).setSlot(any(), any());
        
        parser.parse(resource.getRoot());
        
        verify(parser, times(1)).createInventory("Menu Title", InventoryType.CHEST, 9);
        verify(parser, times(1)).setSlot(any(), any());
    }
    
}
