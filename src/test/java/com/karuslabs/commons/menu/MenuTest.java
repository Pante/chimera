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
package com.karuslabs.commons.menu;

import com.karuslabs.commons.menu.regions.Region;

import org.bukkit.inventory.Inventory;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class MenuTest {
    
    private Menu menu;
    private Region region;
    
    
    public MenuTest() {
        region = mock(Region.class);
        menu = new Menu(mock(Inventory.class));
        menu.getRegions().add(region);
    }
    
    
    @Test
    public void click() {
        menu.click(null);
        
        verify(region).click(menu, null);
    }
    
    
    @Test
    public void drag() {
        menu.drag(null);
        
        verify(region).drag(menu, null);
    }
    
    
    @Test
    public void open() {
        menu.open(null);
        
        verify(region).open(menu, null);
    }
    
    
    @Test
    public void close() {
        menu.close(null);
        
        verify(region).close(menu, null);
    }
    
}
