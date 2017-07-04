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
package com.karuslabs.commons.menu;

import com.karuslabs.commons.menu.regions.Region;

import org.bukkit.inventory.Inventory;

import org.junit.Test;

import static org.mockito.Mockito.*;


public class MenuTest {
    
    private Menu menu;
    private Region region;
    
    
    public MenuTest() {
        region = mock(Region.class);
        menu = new Menu(mock(Inventory.class));
        menu.getRegions().add(region);
    }
    
    
    @Test
    public void click() {
        menu.click(null);
        
        verify(region).click(menu, null);
    }
    
    
    @Test
    public void drag() {
        menu.drag(null);
        
        verify(region).drag(menu, null);
    }
    
    
    @Test
    public void open() {
        menu.open(null);
        
        verify(region).open(menu, null);
    }
    
    
    @Test
    public void close() {
        menu.close(null);
        
        verify(region).close(menu, null);
    }
    
}
