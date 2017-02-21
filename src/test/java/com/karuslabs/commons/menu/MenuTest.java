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

import java.util.Map;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class MenuTest {
    
    private Menu stub;
    private InventoryDragEvent event;
    
    
    public MenuTest() {
        stub = new Menu() {
            @Override
            public void onClick(InventoryClickEvent event) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Inventory getInventory() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Map<Integer, Button> getButtons() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        
        event = mock(InventoryDragEvent.class);
    }
    
    
    @Test
    public void onDrag() {
        stub.onDrag(event);
        
        verify(event, times(1)).setCancelled(true);
    }
    
}
