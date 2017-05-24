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

import junitparams.*;

import org.bukkit.event.inventory.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnitParamsRunner.class)
public class CheckBoxTest {
    
    private Menu menu;
    
    private InventoryClickEvent click;
    private InventoryCloseEvent close;
    
    
    public CheckBoxTest() {
        menu = mock(Menu.class);
        
        click = mock(InventoryClickEvent.class);
        close = mock(InventoryCloseEvent.class);
    }
    
    
    @Test
    @Parameters({"true, 0, 1, false", "false, 1, 0, true"})
    public void click(boolean checked, int checkTimes, int uncheckTimes, boolean isChecked) {
        CheckBox box = spy(new StubCheckBox(checked, false));
        
        box.click(menu, click);
        
        verify(box, times(checkTimes)).check(menu, click);
        verify(box, times(uncheckTimes)).uncheck(menu, click);
        
        assertEquals(isChecked, box.isChecked());
    }
    
    
    @Test
    @Parameters({"true, 1, false", "false, 0, true"})
    public void close(boolean reset, int resetTimes, boolean checked) {
        CheckBox box = spy(new StubCheckBox(true, reset));
        
        box.close(menu, close, 0);
        
        verify(box, times(resetTimes)).onClose(menu, close, 0);
        
        assertEquals(reset, box.resets());
        assertEquals(checked, box.isChecked());
    }
    
    
    private static class StubCheckBox extends CheckBox {
        
        public StubCheckBox(boolean checked, boolean reset) {
            super(checked, reset);
        }
        
    }
    
}
