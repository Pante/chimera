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
package com.karuslabs.commons.graphics.buttons;

import com.karuslabs.commons.graphics.*;
import com.karuslabs.commons.graphics.windows.Window;

import org.bukkit.event.inventory.*;


public abstract class CheckBox extends ResettableComponent implements Button {
    
    boolean checked;
    boolean original;
    
    
    public CheckBox(boolean reset) {
        this(false, false);
    }
    
    public CheckBox(boolean checked, boolean reset) {
        super(reset);
        this.checked = checked;
        this.original = checked;
    }
    
    
    @Override
    public void click(ClickEvent event) {
        if (checked) {
            checked = uncheck(event);
            
        } else {
            checked = check(event);
        }
    }
    
    protected boolean check(ClickEvent event) {
        return true;
    }
    
    protected boolean uncheck(ClickEvent event) {
        return false;
    }
    
    
    @Override
    public void reset(Window window, InventoryCloseEvent event) {
        if (reset) {
            onReset(window, event);
            checked = original;
        }
    }
    
}
