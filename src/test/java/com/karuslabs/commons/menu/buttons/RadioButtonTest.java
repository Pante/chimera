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
package com.karuslabs.commons.menu.buttons;

import com.karuslabs.commons.menu.Menu;

import org.bukkit.event.inventory.InventoryClickEvent;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class RadioButtonTest {
    
    private Menu menu;
    private InventoryClickEvent event;
    
    
    public RadioButtonTest() {
        menu = mock(Menu.class);
        event = mock(InventoryClickEvent.class);
    }
    
    
    @Test
    public void click() {
        new StubButton().click(menu, event);
        
        verify(event).setCancelled(true);
    }
    
    
    private static class StubButton implements RadioButton {

        @Override
        public boolean select(Menu menu, InventoryClickEvent event) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void unselect(Menu menu, InventoryClickEvent event) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
    
}
