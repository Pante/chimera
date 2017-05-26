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

import com.karuslabs.commons.menu.buttons.Button;

import java.util.Collections;

import junitparams.*;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class BoxRegionTest {
    
    private BoxRegion region;
    private Player player;
    private Button button;
    
    
    public BoxRegionTest() {
        region = new BoxRegion(5, 12, 18);
        player = mock(Player.class);
        button = mock(Button.class);
        
        region.getButtons().put(13, button);
    }
    
    
    @Before
    public void setup() {
        region.setMin(12);
        region.setMax(18);
    }
    
    
    @Test
    @Parameters({"12, true", "18, true", "17, true", "7, false", "11, false", "19, false"})
    public void contains(int slot, boolean contains) {
        assertEquals(contains, region.contains(slot));
    }
    
    @Test
    @Parameters({"12, true", "18, true", "17, true", "7, false", "11, false", "19, false"})
    public void contains_inverted(int slot, boolean contains) {
        region.setMin(18);
        region.setMax(12);
        
        assertEquals(contains, region.contains(slot));
    }
    
    
    @Test
    @Parameters({"13, true, 1", "12, true, 0", "9, true, 0", "13, false, 0", "0, false, 0"})
    public void click(int slot, boolean hasPermission, int times) {
        InventoryClickEvent event = when(mock(InventoryClickEvent.class).getWhoClicked()).thenReturn(player).getMock();
        when(event.getRawSlot()).thenReturn(slot);
        when(player.hasPermission("")).thenReturn(hasPermission);
        
        region.click(null, event);
        
        verify(button, times(times)).click(null, event);
    }
    
    
    @Test
    @Parameters({"13, true, 1", "12, true, 0", "9, true, 0", "13, false, 0", "0, false, 0"})
    public void drag(int slot, boolean hasPermission, int times) {
        InventoryDragEvent event = when(mock(InventoryDragEvent.class).getWhoClicked()).thenReturn(player).getMock();
        when(event.getRawSlots()).thenReturn(Collections.singleton(slot));
        when(player.hasPermission("")).thenReturn(hasPermission);
        
        region.drag(null, event);
        
        verify(button, times(times)).drag(null, event);
    }
    
    
    @Test
    public void newBoxRegion() {
        BoxRegion region = BoxRegion.newBoxRegion().min(10).max(12).inventoryWidth(5).defaultButton(Button.NONE).build();
        
        assertEquals(10, region.getMin());
        assertEquals(12, region.getMax());
        assertEquals(5, region.width);
        assertEquals(true, Button.NONE == region.defaultButton);
    }
    
}
