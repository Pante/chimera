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
package com.karuslabs.commons.menu.buttons;

import com.karuslabs.commons.menu.Menu;

import java.util.function.BiFunction;

import junitparams.*;

import org.bukkit.event.inventory.InventoryClickEvent;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class CyclicButtonTest {
    
    private BiFunction<Menu, InventoryClickEvent, Boolean> state1;
    private BiFunction<Menu, InventoryClickEvent, Boolean> state2;
    
    
    public CyclicButtonTest() {
        state1 = mock(BiFunction.class);
        state2 = mock(BiFunction.class);
    }

    
    @Test
    @Parameters({"true", "false"})
    public void click(boolean next) {
        CyclicButton button = new CyclicButton(newArrayList(state1, state2), false);
        when(state1.apply(any(), any())).thenReturn(next);
        
        button.click(null, null);
        
        verify(state1).apply(any(), any());
        assertEquals(next, button.getCurrentState() == state2);
    }
    
    
    @Test
    @Parameters({"true, 1", "false, 0"})
    public void close(boolean reset, int closeTimes) {
        CyclicButton button = spy(new CyclicButton(newArrayList(state1, state2), reset));
        button.nextState();
        button.nextState();
        
        button.close(null, null, 0);
        
        verify(button, times(closeTimes)).onClose(null, null, 0);
        assertTrue(button.getCurrentState() == state1);
        assertEquals(reset, button.resets());
    }
    
}
