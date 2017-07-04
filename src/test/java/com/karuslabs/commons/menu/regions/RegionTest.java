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
