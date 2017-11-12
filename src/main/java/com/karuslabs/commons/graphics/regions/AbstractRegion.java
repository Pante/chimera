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
package com.karuslabs.commons.graphics.regions;

import com.karuslabs.commons.graphics.*;
import com.karuslabs.commons.graphics.buttons.Button;
import com.karuslabs.commons.graphics.windows.Window;

import java.util.Map;

import org.bukkit.event.inventory.*;


public abstract class AbstractRegion<GenericButton extends Button> extends ResettableComponent implements Region {
    
    protected final Buttons<GenericButton> buttons;
    
    
    public AbstractRegion(Map<Integer, GenericButton> map) {
        this(map, false);
    }
    
    public AbstractRegion(Map<Integer, GenericButton> map, boolean reset) {
        super(reset);
        buttons = new Buttons<>(this, map);
    }

    
    @Override
    public void click(ClickEvent event) {
        if (!contains(event.getRawSlot())) {
            return;
        }
        
        GenericButton button = buttons.get(event.getRawSlot());
        if (button != null) {
            button.click(event);
            
        } else {
            clickBlank(event);
        }
    }
    
    protected void clickBlank(ClickEvent event) {
        event.setCancelled(true);
    }
    
    
    @Override
    public void drag(DragEvent event) {
        for (int dragged : event.getRawSlots()) {
            if (event.isCancelled()) {
                return;
            }
            
            if (contains(dragged)) {
                GenericButton button = buttons.get(dragged);
                if (button != null) {
                    button.drag(event, dragged);

                } else {
                    dragBlank(event, dragged);
                }
            }
        }
    }
    
    protected void dragBlank(DragEvent event, int dragged) {
        event.setCancelled(true);
    }
    
    
    @Override
    public void open(Window window, InventoryOpenEvent event) {
        onOpen(window, event);
        buttons.values().forEach(button -> button.open(window, event));
    }
    
    protected void onOpen(Window window, InventoryOpenEvent event) {
        
    }
    
    
    @Override
    public void close(Window window, InventoryCloseEvent event) {
        onClose(window, event);
        buttons.values().forEach(button -> button.close(window, event));
        
    }
    
    protected void onClose(Window window, InventoryCloseEvent event) {
        
    }
    
    
    @Override
    public void reset(Window window, InventoryCloseEvent event) {
        if (reset) {
            onReset(window, event);
            buttons.values().forEach(button -> button.reset(window, event));
        }
    }
    
    
    public Buttons<GenericButton> getButtons() {
        return buttons;
    }

}
