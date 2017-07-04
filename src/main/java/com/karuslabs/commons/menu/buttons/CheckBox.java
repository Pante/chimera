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


public abstract class CheckBox implements Button {
    
    private boolean checked;
    private boolean reset;
    
    
    public CheckBox(boolean checked, boolean reset) {
        this.checked = checked;
        this.reset = reset;
    }
    
    
    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        if (checked) {
            checked = uncheck(menu, event);
            
        } else {
            checked = check(menu, event);
        }
    }
    
    protected boolean check(Menu menu, InventoryClickEvent event) {
        return true;
    }
    
    protected boolean uncheck(Menu menu, InventoryClickEvent event) {
        return false;
    }
    
    
    @Override
    public void close(Menu menu, InventoryCloseEvent event, int slot) {
        if (reset) {
            checked = onClose(menu, event, slot);
        }
    }
    
    protected boolean onClose(Menu menu, InventoryCloseEvent event, int slot) {
        return false;
    }
    
    
    public boolean isChecked() {
        return checked;
    }
    
    public boolean resets() {
        return reset;
    }
    
}
