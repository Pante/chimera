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

import org.bukkit.event.inventory.*;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class ButtonTest {
    
    private Menu menu;
    
    private InventoryClickEvent click;
    private InventoryDragEvent drag;
    private InventoryOpenEvent open;
    private InventoryCloseEvent close;
    
    
    public ButtonTest() {
        menu = mock(Menu.class);
        
        click = mock(InventoryClickEvent.class);
        drag = mock(InventoryDragEvent.class);
        open = mock(InventoryOpenEvent.class);
        close = mock(InventoryCloseEvent.class);
    }
    
    
    @Test
    public void cancel() {
        Button.CANCEL.click(menu, click);
        
        verifyZeroInteractions(menu);
        verify(click, times(1)).setCancelled(true);
    }
    
    
    @Test
    public void none() {
        Button.NONE.click(menu, click);
        Button.NONE.drag(menu, drag);
        
        verifyZeroInteractions(menu, click, drag);
    }
    
    
    @Test
    public void stub() {
        Button stub = (menu, event) -> {};
        
        stub.drag(menu, drag);
        stub.open(menu, open, 0);
        stub.close(menu, close, 0);
        
        verify(drag, times(1)).setCancelled(true);
        verifyZeroInteractions(menu, open, close);
    }
    
}
