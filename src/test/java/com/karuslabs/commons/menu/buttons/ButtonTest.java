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

import org.bukkit.event.inventory.*;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class ButtonTest {
    
    private Menu menu;
    
    private InventoryClickEvent click;
    private InventoryDragEvent drag;
    private InventoryOpenEvent open;
    private InventoryCloseEvent close;
    
    
    public ButtonTest() {
        menu = mock(Menu.class);
        
        click = mock(InventoryClickEvent.class);
        drag = mock(InventoryDragEvent.class);
        open = mock(InventoryOpenEvent.class);
        close = mock(InventoryCloseEvent.class);
    }
    
    
    @Test
    public void cancel() {
        Button.CANCEL.click(menu, click);
        
        verifyZeroInteractions(menu);
        verify(click, times(1)).setCancelled(true);
    }
    
    
    @Test
    public void none() {
        Button.NONE.click(menu, click);
        Button.NONE.drag(menu, drag);
        
        verifyZeroInteractions(menu, click, drag);
    }
    
    
    @Test
    public void stub() {
        Button stub = (menu, event) -> {};
        
        stub.drag(menu, drag);
        stub.open(menu, open, 0);
        stub.close(menu, close, 0);
        
        verify(drag, times(1)).setCancelled(true);
        verifyZeroInteractions(menu, open, close);
    }
    
}
