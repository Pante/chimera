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

import com.karuslabs.commons.menu.Menu;
import com.karuslabs.commons.menu.buttons.Button;

import java.util.Collections;

import junitparams.*;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class RegionTest {
    
    private Menu menu;
    private Region<Button> region;
    private Button button;
    private Player player;
    
    
    public RegionTest() {
        menu = mock(Menu.class);
        button = mock(Button.class);
        region = new Region<>();
        region.getButtons().put(1, button);
        player = mock(Player.class);
    }
    
    
    @Test
    @Parameters({"1, true", "0, false"})
    public void contains(int slot, boolean contains) {
        assertEquals(contains, region.contains(slot));
    }
    
    
    @Test
    @Parameters({"true, 1, 1", "false, 1, 0", "true, 0, 0", "false, 0, 0"})
    public void click(boolean hasPermission, int slot, int clickTimes) {
        InventoryClickEvent event = when(mock(InventoryClickEvent.class).getWhoClicked()).thenReturn(player).getMock();
        when(event.getRawSlot()).thenReturn(slot);
        when(player.hasPermission("")).thenReturn(hasPermission);
        
        region.click(menu, event);
        
        verify(button, times(clickTimes)).click(menu, event);
    }
    
    
    @Test
    @Parameters({"true, 1, 1", "false, 1, 0", "true, 0, 0", "false, 0, 0"})
    public void drag(boolean hasPermission, int slot, int dragTimes) {
        InventoryDragEvent event = when(mock(InventoryDragEvent.class).getWhoClicked()).thenReturn(player).getMock();
        when(event.getRawSlots()).thenReturn(Collections.singleton(slot));
        when(player.hasPermission("")).thenReturn(hasPermission);
        
        region.drag(menu, event);
        
        verify(button, times(dragTimes)).drag(menu, event);
    }
    
    
    @Test
    public void open() {
        InventoryOpenEvent event = mock(InventoryOpenEvent.class);
        
        region.open(menu, event);
        
        verify(button).open(menu, event, 1);
    }
    
    
    @Test
    public void close() {
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        
        region.close(menu, event);
        
        verify(button).close(menu, event, 1);
    }
    
    
    @Test
    public void newRegion() {
        Region region = Region.newRegion().permission("test").button(10, button).build();
        
        assertEquals("test", region.getPermission());
        assertEquals(button, region.getButtons().get(10));
    }
    
}
