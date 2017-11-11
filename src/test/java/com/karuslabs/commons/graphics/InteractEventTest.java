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

import java.util.stream.Stream;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.InventoryView;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static com.karuslabs.commons.locale.MessageTranslation.NONE;
import static java.util.Collections.EMPTY_MAP;
import static org.bukkit.Material.AIR;
import org.bukkit.inventory.ItemStack;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class InteractEventTest {
    
    @ParameterizedTest
    @MethodSource("parameters")
    void getTranslation(InteractEvent event, InventoryInteractEvent delegated) {
        assertSame(event.getWindow().getTranslation(), event.getTranslation());
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void result(InventoryInteractEvent event, InventoryInteractEvent delegated) {
        event.getResult();
        verify(delegated).getResult();
        
        event.setResult(Event.Result.DEFAULT);
        verify(delegated).setResult(Event.Result.DEFAULT);
    }
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void cancelled(InventoryInteractEvent event, InventoryInteractEvent delegated) {
        event.isCancelled();
        verify(delegated).isCancelled();
        
        event.setCancelled(true);
        verify(delegated).setCancelled(true);
    }
    
    
    static Stream<Arguments> parameters() {
        InventoryClickEvent click = when(mock(InventoryClickEvent.class).getView()).thenReturn(mock(InventoryView.class)).getMock();
        InventoryDragEvent drag = spy(new InventoryDragEvent(mock(InventoryView.class), null, new ItemStack(AIR), false, EMPTY_MAP));
        
        return Stream.of(
            of(new ClickEvent(new ShapelessWindow(null, NONE, false), click), click),
            of(new DragEvent(new ShapelessWindow(null, NONE, false), drag), drag)
        );
    }
    
}
