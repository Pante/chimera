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

import java.util.Collections;

import org.bukkit.event.inventory.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.karuslabs.commons.graphics.regions.Region.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class SingletonTest {
    
    Button button = mock(Button.class);
    Region region = singleton(10, button);
    
    
    @ParameterizedTest
    @CsvSource({"10, true", "5, false"})
    void contains(int slot, boolean contains) {
        assertEquals(contains, region.contains(slot));
    }
    
    
    @ParameterizedTest
    @CsvSource({"10, 1", "5, 0"})
    void click(int slot, int times) {
        ClickEvent event = when(mock(ClickEvent.class).getRawSlot()).thenReturn(slot).getMock();
        
        region.click(event);
        
        verify(button, times(times)).click(event);
    }
    
    
    @ParameterizedTest
    @CsvSource({"false, 10, 1", "true, 10, 0", "false, 5, 0"})
    void drag(boolean cancelled, int slot, int times) {
        DragEvent event = when(mock(DragEvent.class).isCancelled()).thenReturn(cancelled).getMock();
        when(event.getRawSlots()).thenReturn(Collections.singleton(slot));
        
        region.drag(event);
        
        verify(button, times(times)).drag(event, 10);
    }
    
    
    @Test
    void open() {
        Window window = mock(Window.class);
        InventoryOpenEvent event = mock(InventoryOpenEvent.class);
        
        region.open(window, event);
        
        verify(button).open(window, event);
    }
    
    
    @Test
    void close() {
        Window window = mock(Window.class);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        
        region.close(window, event);
        
        verify(button).close(window, event);
    }
    
    
    @Test
    void size() {
        assertEquals(1, region.size());
    }
    
}
