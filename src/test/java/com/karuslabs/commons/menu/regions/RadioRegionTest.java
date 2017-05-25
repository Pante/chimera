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

import com.karuslabs.commons.menu.buttons.RadioButton;

import junitparams.*;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class RadioRegionTest {
    
    private RadioButton button;
    private RadioButton oldButton;
    private Player player;
    
    
    public RadioRegionTest() {
        button = mock(RadioButton.class);
        oldButton = mock(RadioButton.class);
        player = mock(Player.class);
    }
    
    
    @Test
    @Parameters({"1, true, true, 1, true", "0, true, true, 0, true", "1, false, true, 0, false", "1, true, false, 0, false"})
    public void click(int slot, boolean hasPermission, boolean select, int unselectTimes, boolean updated) {
        RadioRegion region = new RadioRegion(0, false);
        region.getButtons().put(slot, button);
        region.getButtons().put(0, oldButton);
        
        when(player.hasPermission("")).thenReturn(hasPermission);
        InventoryClickEvent event = when(mock(InventoryClickEvent.class).getWhoClicked()).thenReturn(player).getMock();
        when(event.getRawSlot()).thenReturn(slot);
        
        when(button.select(null, event)).thenReturn(select);
        
        region.click(null, event);
        
        verify(oldButton, times(unselectTimes)).unselect(null, event);
        assertEquals(updated, region.getSelectedSlot() == slot);
    }
    
}
