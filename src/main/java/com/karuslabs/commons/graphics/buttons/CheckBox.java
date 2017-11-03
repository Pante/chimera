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

import com.karuslabs.commons.graphics.Point;
import com.karuslabs.commons.locale.MessageTranslation;

import org.bukkit.event.inventory.*;


public class CheckBox implements Button {
    
    private boolean checked;
    private boolean original;
    
    
    public CheckBox() {
        this(false);
    }
    
    public CheckBox(boolean checked) {
        this.checked = checked;
        this.original = checked;
    }
    
    
    @Override
    public void click(Point clicked, InventoryClickEvent event, MessageTranslation translation) {
        if (checked) {
            checked = uncheck(clicked, event, translation);
            
        } else {
            checked = check(clicked, event, translation);
        }
    }
    
    protected boolean check(Point clicked, InventoryClickEvent event, MessageTranslation translation) {
        return true;
    }
    
    protected boolean uncheck(Point clicked, InventoryClickEvent event, MessageTranslation translation) {
        return false;
    }
    
    
    @Override
    public void reset(InventoryCloseEvent event) {
        checked = original;
    }
    
    
    public boolean isChecked() {
        return checked;
    }
    
}
