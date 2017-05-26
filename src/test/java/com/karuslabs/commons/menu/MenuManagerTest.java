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

import junitparams.*;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class MenuManagerTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    
    private MenuManager manager;
    private Player player;
    private Menu menu;
    
    
    public MenuManagerTest() {
        manager = new MenuManager();
        player = mock(Player.class);
        menu = mock(Menu.class);
    }
    
    
    @Before
    public void setup() {
        manager.getPool().clear();
    }
    
    
    @Test
    public void setActiveFromPool() {
        manager.getPool().put("key", menu);
        manager.setActiveFromPool(player, "key");
        
        assertEquals(menu, manager.getActive().get(player));
    }
    
    
    @Test
    public void setActiveFromPool_ThrowsException() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("No such pooled menu with key: invalid");
        
        manager.setActiveFromPool(player, "invalid");
    }
    
    
    @Test
    @Parameters({"true, 1", "false, 0"})
    public void onClick(boolean contains, int times) {
        if (contains) {
            manager.getActive().put(player, menu);
        }
        
        InventoryClickEvent event = when(mock(InventoryClickEvent.class).getWhoClicked()).thenReturn(player).getMock();
        manager.onClick(event);
        
        verify(menu, times(times)).click(event);
    }
    
    
    @Test
    @Parameters({"true, 1", "false, 0"})
    public void onDrag(boolean contains, int times) {
        if (contains) {
            manager.getActive().put(player, menu);
        }
        
        InventoryDragEvent event = when(mock(InventoryDragEvent.class).getWhoClicked()).thenReturn(player).getMock();
        manager.onDrag(event);
        
        verify(menu, times(times)).drag(event);
    }
    
    
    @Test
    @Parameters({"true, 1", "false, 0"})
    public void onOpen(boolean contains, int times) {
        if (contains) {
            manager.getActive().put(player, menu);
        }
        
        InventoryOpenEvent event = when(mock(InventoryOpenEvent.class).getPlayer()).thenReturn(player).getMock();
        manager.onOpen(event);
        
        verify(menu, times(times)).open(event);
    }
    
    
    @Test
    @Parameters({"true, 1", "false, 0"})
    public void onClose(boolean contains, int times) {
        if (contains) {
            manager.getActive().put(player, menu);
        }
        
        InventoryCloseEvent event = when(mock(InventoryCloseEvent.class).getPlayer()).thenReturn(player).getMock();
        manager.onClose(event);
        
        verify(menu, times(times)).close(event);
    }
    
}
