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
package com.karuslabs.commons.graphics.regions;

import com.karuslabs.commons.graphics.*;
import com.karuslabs.commons.graphics.buttons.Button;
import com.karuslabs.commons.graphics.windows.Window;

import org.bukkit.event.inventory.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static java.util.Collections.*;
import static org.mockito.Mockito.*;


class AbstractRegionTest {
    
    Button button = mock(Button.class);
    AbstractRegion region = spy(new AbstractRegion(singletonMap(10, button)) {
        @Override
        public boolean contains(int slot) {
            return slot == 9 || slot == 10;
        }
        
        @Override
        public int size() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        
        @Override
        protected void onReset(Window window, InventoryCloseEvent event) {
            event.getInventory();
        }
    });
    
    
    @ParameterizedTest
    @CsvSource({"0, 0, 0", "10, 1, 0", "9, 0, 1"})
    void click(int slot, int times, int blank) {
        ClickEvent event = when(mock(ClickEvent.class).getRawSlot()).thenReturn(slot).getMock();
        
        region.click(event);
        
        verify(button, times(times)).click(event);
        verify(region, times(blank)).clickBlank(event);
    }
    
    
    @ParameterizedTest
    @CsvSource({"10, true, 0, 0", "0, false, 0, 0", "10, false, 1, 0", "9, false, 0, 1"})
    void drag(int slot, boolean cancelled, int times, int blank) {
        DragEvent event = when(mock(DragEvent.class).getRawSlots()).thenReturn(singleton(slot)).getMock();
        when(event.isCancelled()).thenReturn(cancelled).getMock();
        
        region.drag(event);
        
        verify(button, times(times)).drag(event, slot);
        verify(region, times(blank)).dragBlank(event, slot);
    }
    
    
    @Test
    void dragBlank() {
        DragEvent event = mock(DragEvent.class);
        
        region.dragBlank(event, 0);
        
        verify(event).setCancelled(true);
    }
    
    
    @Test
    void open() {
        Window window = mock(Window.class);
        InventoryOpenEvent event = mock(InventoryOpenEvent.class);
        
        region.open(window, event);
        
        verify(region).onOpen(window, event);
        verify(button).open(window, event);
    }
    
    
    @Test
    void close() {
        Window window = mock(Window.class);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        
        region.close(window, event);
        
        verify(region).onClose(window, event);
        verify(button).close(window, event);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 1", "false, 0"})
    void reset(boolean reset, int times) {
        Window window = mock(Window.class);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        
        region.reset(reset);
        
        region.reset(window, event);
        
        verify(event, times(times)).getInventory();
        verify(button, times(times)).reset(window, event);
    }
    
}
