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
