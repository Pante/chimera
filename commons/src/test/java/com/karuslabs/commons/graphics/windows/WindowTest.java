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
package com.karuslabs.commons.graphics.windows;

import com.karuslabs.commons.graphics.*;
import com.karuslabs.commons.graphics.regions.Region;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.bukkit.Material.AIR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class WindowTest {
    
    Region region = mock(Region.class);
    Window window = spy(new Window(asList(region), null, false) {
        @Override
        protected Point inside(int slot) {
            return new Point(1, 0);
        }
    });
    
    
    @Test
    void render_players() {
        Player player = mock(Player.class);
        window.setInventory(mock(Inventory.class)).render(asList(player));
        
        verify(player).openInventory(window.getInventory());
    }
    
    
    @ParameterizedTest
    @CsvSource({"-1, -1", "-999, -999", "5, 1"})
    void at(int slot, int expected) {
        assertEquals(new Point(expected, 0), window.at(slot));
    }
    
    
    @ParameterizedTest
    @CsvSource({"false, 10, 0, 0", "true, 10, 1, 0", "true, -1, 0, 1"})
    void click(boolean holder, int slot, int click, int outside) {
        InventoryClickEvent event = new InventoryClickEvent(view(holder), null, slot, null, null);
        
        window.click(event);
        
        verify(window, times(click)).onClick(event);
        verify(region, times(click)).click(any(ClickEvent.class));
        verify(window, times(outside)).outside(event);
    }
    
    
    @ParameterizedTest
    @CsvSource({"false, 10, 0, 0", "true, 10, 1, 0", "true, -1, 0, 1"})
    void drag(boolean holder, int slot, int click, int outside) {
        InventoryDragEvent event = new InventoryDragEvent(view(holder), null, new ItemStack(AIR), true, singletonMap(slot, new ItemStack(AIR)));
        
        window.drag(event);
        
        verify(window, times(click)).onDrag(event);
        verify(region, times(click)).drag(any(DragEvent.class));
        verify(window, times(outside)).outside(event);
    }
    
    
    @Test
    void outside() {
        InventoryDragEvent event = mock(InventoryDragEvent.class);
        
        window.outside(event);
        
        verify(event).setCancelled(true);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 1", "false, 0"})
    void open(boolean holder, int times) {
        InventoryOpenEvent event = new InventoryOpenEvent(view(holder));
        
        window.open(event);
        
        verify(window, times(times)).onOpen(event);
        verify(region, times(times)).open(window, event);
    }
    
    
    @ParameterizedTest
    @CsvSource({"false, true, 0, 0", "true, false, 1, 0", "true, true, 1, 1"})
    void close(boolean holder, boolean reset, int close, int times) {
        InventoryCloseEvent event = new InventoryCloseEvent(view(holder));
        
        window.reset(reset);
        window.close(event);
        
        verify(window, times(close)).onClose(event);
        verify(region, times(close)).close(window, event);
        
        verify(window, times(times)).onReset(event);
        verify(region, times(times)).reset(window, event);
        
        assertEquals(reset, window.reset());
    }
    
    
    InventoryView view(boolean holder) {
        Inventory inventory = when(mock(Inventory.class).getHolder()).thenReturn(holder ?  window : null).getMock();
        InventoryView view = when(mock(InventoryView.class).getTopInventory()).thenReturn(inventory).getMock();
        return view;
    }
    
}
