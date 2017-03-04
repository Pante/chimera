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

import com.karuslabs.commons.test.StubServer;
import com.karuslabs.commons.menu.regions.Region;

import junitparams.*;

import org.bukkit.event.inventory.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class MenuTest {
    
    private Menu menu;
    private Button button;
    
    public MenuTest() {
        button = mock(Button.class);
        menu = spy(new Menu(StubServer.INSTANCE.createInventory(null, 27), button));
    }
    
    
    @Test
    @Parameters({"true, 1, 0", "false, 0, 1"})
    public void onClick(boolean within, int regionTimes, int menuTimes) {
        InventoryClickEvent event = mock(InventoryClickEvent.class);
        when(event.getRawSlot()).thenReturn(10);
        
        Region region = mock(Region.class);
        Button button = mock(Button.class);
        when(region.getButtonOrDefault(any(int.class))).thenReturn(button);
        when(region.within(any(int.class))).thenReturn(within);
        
        menu.getRegions().add(region);
        
        menu.onClick(event);
        
        verify(button, times(regionTimes)).onClick(menu, event);
        verify(this.button, times(menuTimes)).onClick(menu, event);
    }
    
    
    @Test
    public void onDrag() {
        InventoryDragEvent event = mock(InventoryDragEvent.class);
        
        menu.onDrag(event);
        
        verify(event, times(1)).setCancelled(true);
    }
    
    
    @Test
    @Parameters({"-1", "28"})
    public void within(int slot) {
        assertFalse(menu.within(slot));
    }
    
}
