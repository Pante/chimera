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
package com.karusmc.commons.menu;

import junitparams.*;

import org.bukkit.event.inventory.InventoryClickEvent;

import org.junit.*;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class SharedMenuTest {
    
    private static Button button = mock(Button.class);
    private static Button defaultButton = mock(Button.class);
    
    private SharedMenu menu;
    
    
    public SharedMenuTest() {
        menu = new SharedMenu(null, defaultButton);
    }
    
    
    @Before
    public void setup() {
        menu.getButtons().clear();
    }
    
    
    @Test
    @Parameters({"0, 1, 0", "69, 0, 1"})
    public void onClick(int slot, int buttonTimes, int defaultTimes) {
        menu.getButtons().put(slot, button);
        InventoryClickEvent event = mock(InventoryClickEvent.class);
        when(event.getRawSlot()).thenReturn(0);
        
        menu.onClick(event);
        
        verify(event, times(1)).setCancelled(true);
        verify(button, times(buttonTimes)).onClick(event);
        verify(defaultButton, times(defaultTimes)).onClick(event);
    }
    
}
