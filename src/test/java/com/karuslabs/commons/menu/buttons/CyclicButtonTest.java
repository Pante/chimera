/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
