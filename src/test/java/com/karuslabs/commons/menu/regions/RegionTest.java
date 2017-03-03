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
package com.karuslabs.commons.menu.regions;

import com.karuslabs.commons.menu.Button;

import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.inventory.*;

import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


public class RegionTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    private Region region;
    private Button button;
    private ItemStack item;
    private Consumer<Integer> binder;
    
    
    public RegionTest() {
        button = mock(Button.class);
        region = new Region(mock(Inventory.class), button) {
            @Override
            public boolean within(int slot) {
                return slot >= 0;
            }
        };
        item = new ItemStack(Material.ANVIL);
        binder = mock(Consumer.class);
    }
    
    
    @Before
    public void setup() {
        region.getButtons().clear();
    }
    
    
    @Test
    public void bind_ItemStack_Button() {
        region.bind(item, button, 1);
        
        verify(region.getInventory(), times(1)).setItem(1, item);
        assertEquals(button, region.getButtons().get(1));
    }
    
    
    @Test
    public void bind_ItemStack() {
        region.bind(item, 1);
        
        verify(region.getInventory(), times(1)).setItem(1, item);
    }
    
    
    @Test
    public void bind_Button() {
        region.bind(button, 1);
        
        assertEquals(button, region.getButtons().get(1));
    }
    
    
    @Test
    public void bind_binder_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Slot is out of bound: -1");
        
        region.bind(binder, -1);
        
        verify(binder, times(0)).accept(any(int.class));
    }
    
    
    @Test
    public void bind_binder() {
        region.bind(binder, 1, 2, 3);
        
        verify(binder, times(3)).accept(any(int.class));
    }
    
}
