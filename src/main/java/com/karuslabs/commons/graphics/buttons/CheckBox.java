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


/**
 * An implementation of {@code Button} which represents a checkbox.
 */
public abstract class CheckBox extends ResettableComponent implements Button {
    
    boolean checked;
    boolean original;
    
    
    /**
     * Constructs a {@code CheckBox} which is unchecked by default, with the specified reset value.
     * 
     * @param reset true if this checkbox should reset; else false
     */
    public CheckBox(boolean reset) {
        this(false, false);
    }
    
    /**
     * Constructs a {@code CheckBox} with the specified default checking and reset value.
     * 
     * @param checked true if this checkbox should be checked by default; else false
     * @param reset true if this checkbox should reset; else false
     */
    public CheckBox(boolean checked, boolean reset) {
        super(reset);
        this.checked = checked;
        this.original = checked;
    }
    
    
    /**
     * Delegates the handling of the specified event to {@link #uncheck(ClickEvent)} if the checkbox is checked;
     * else {@link #check(ClickEvent)}, and updates whether the checkbox is checked accordingly when this checkbox
     * is clicked.
     * 
     * @param event the event
     */
    @Override
    public void click(ClickEvent event) {
        if (checked) {
            checked = uncheck(event);
            
        } else {
            checked = check(event);
        }
    }
    
    /**
     * Handles the specified when this checkbox is clicked and unchecked.
     * This method is invoked by {@link #click(ClickEvent)} if this checkbox is unchecked.
     * The default implementation returns {@code true}.
     * 
     * Subclasses should override this method to customise the handling of the specified event.
     * 
     * @param event the event
     * @return true if the checkbox should be checked; else false
     */
    protected boolean check(ClickEvent event) {
        return true;
    }
    
    /**
     * Handles the specified when this checkbox is clicked and the checkbox is checked.
     * This method is invoked by {@link #click(ClickEvent)} if this checkbox is checked.
     * The default implementation returns {@code false}.
     * 
     * Subclasses should override this method to customise the handling of the specified event.
     * 
     * @param event the event
     * @return true if the checkbox should be checked; else false
     */
    protected boolean uncheck(ClickEvent event) {
        return false;
    }
    
    
    /**
     * Delegates the resetting of this checkbox to {@link #onReset(Window, InventoryCloseEvent)} and sets the checked status to the original,
     * or does nothing if this checkbox should not reset.
     * 
     * @param window the window which contains this checkbox
     * @param event the event
     */
    @Override
    public void reset(Window window, InventoryCloseEvent event) {
        if (reset) {
            onReset(window, event);
            checked = original;
        }
    }
    
}
