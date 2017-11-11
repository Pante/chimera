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
package com.karuslabs.commons.graphics;

import com.karuslabs.commons.graphics.windows.ShapelessWindow;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;

import org.junit.jupiter.api.Test;

import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;


class InteractEventTest {
    
    InventoryClickEvent click = when(mock(InventoryClickEvent.class).getView()).thenReturn(mock(InventoryView.class)).getMock();
    InteractEvent<InventoryClickEvent> event = new ClickEvent(new ShapelessWindow(null, NONE, false), click);

    
    @Test
    void getTranslation() {
        assertSame(event.getWindow().getTranslation(), event.getTranslation());
    }
    
    
    @Test
    void result() {
        event.getResult();
        verify(event.getEvent()).getResult();
        
        event.setResult(Event.Result.DEFAULT);
        verify(event.getEvent()).setResult(Event.Result.DEFAULT);
    }
    
    
    @Test
    void cancelled() {
        event.isCancelled();
        verify(event.getEvent()).isCancelled();
        
        event.setCancelled(true);
        verify(event.getEvent()).setCancelled(true);
    }
    
}
