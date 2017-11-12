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

import com.karuslabs.commons.graphics.windows.Window;

import java.util.function.Function;
import java.util.stream.Stream;

import org.bukkit.event.inventory.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import static org.junit.jupiter.params.provider.Arguments.of;
import static org.mockito.Mockito.*;


class ComponentTest {
    
    static Component component = e -> {};
    Window window = mock(Window.class);
    
    
    @ParameterizedTest
    @MethodSource("parameters")
    void component(Function<Window, InventoryEvent> function) {
        InventoryEvent event = function.apply(window);
        
        verifyZeroInteractions(window);
        verifyZeroInteractions(event);
    }
    
    static Stream<Arguments> parameters() {
        return Stream.of(
            of(wrap(window -> {
                InventoryOpenEvent event = mock(InventoryOpenEvent.class);
                component.open(window, event);
                return event;
            })),
            of(wrap(window -> {
                InventoryCloseEvent event = mock(InventoryCloseEvent.class);
                component.close(window, event);
                return event;
            })),
            of(wrap(window -> {
                InventoryCloseEvent event = mock(InventoryCloseEvent.class);
                component.reset(window, event);
                return event;
            }))
        );
    }
    
    static Object wrap(Function<Window, InventoryEvent> function) {
        return function;
    }
    
}
