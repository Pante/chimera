/* 
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
