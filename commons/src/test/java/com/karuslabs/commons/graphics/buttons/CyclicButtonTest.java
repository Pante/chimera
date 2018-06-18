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
package com.karuslabs.commons.graphics.buttons;

import com.karuslabs.commons.graphics.ClickEvent;
import com.karuslabs.commons.graphics.windows.Window;

import org.bukkit.event.inventory.InventoryCloseEvent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CyclicButtonTest {
    
    CyclicButton<String> button = spy(new CyclicButton<String>("a", "b", "c") {
        @Override
        protected void onReset(Window window, InventoryCloseEvent event) {
            event.getInventory();
        }
    });
    
    
    @Test
    void cyclicButton_ThrowsException() {
        assertEquals(
            "Invalid number of states", 
            assertThrows(IllegalArgumentException.class, () -> new CyclicButton<String>() {}).getMessage()
        );
    }
    
    
    @Test
    void click() {
        button.index = 0;
        ClickEvent event = mock(ClickEvent.class);
        
        button.click(event);
        
        verify(button).onClick(event, "a");
        assertEquals("b", button.current());
        assertEquals(1, button.index());
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 1, 0", "false, 0, 3"})
    void reset(boolean reset, int times, int index) {
        button.reset(reset);
        button.index = 3;
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        
        button.reset(mock(Window.class), event);
        
        verify(event, times(times)).getInventory();
        assertEquals(index, button.index());
    }
    
    
    @ParameterizedTest
    @CsvSource({"1, c, 2", "2, a, 0"})
    void next(int index, String state, int expected) {
        button.index = index;
        
        assertEquals(state, button.next());
        assertEquals(expected, button.index());
        assertEquals(3, button.length());
    }
    
}
