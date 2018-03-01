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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class CheckBoxTest {
    
    CheckBox checkbox = spy(new CheckBox(false) {
        @Override
        protected void onReset(Window window, InventoryCloseEvent event) {
            event.getInventory();
        }
    });
    
    
    @ParameterizedTest
    @CsvSource({"true, 0, 1", "false, 1, 0"})
    void click(boolean checked, int check, int uncheck) {
        checkbox.checked = checked;
        
        ClickEvent event = mock(ClickEvent.class);
        
        checkbox.click(event);
        
        verify(checkbox, times(check)).check(event);
        verify(checkbox, times(uncheck)).uncheck(event);
        assertEquals(!checked, checkbox.checked);
    }
    
    
    @ParameterizedTest
    @CsvSource({"true, 1, false", "false, 0, true"})
    void reset(boolean reset, int times, boolean expected) {
        checkbox.checked = true;
        checkbox.reset(reset);
        
        Window window = mock(Window.class);
        InventoryCloseEvent event = mock(InventoryCloseEvent.class);
        
        checkbox.reset(window, event);
        
        verify(event, times(times)).getInventory();
        assertEquals(expected, checkbox.checked);
    }
    
}
