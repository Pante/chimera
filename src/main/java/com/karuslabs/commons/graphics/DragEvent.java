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
package com.karuslabs.commons.graphics;

import com.karuslabs.commons.annotation.Shared;
import com.karuslabs.commons.graphics.windows.Window;

import java.util.*;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import static java.util.Collections.EMPTY_MAP;


public class DragEvent extends InventoryDragEvent implements InteractEvent<InventoryDragEvent> {
    
    private Window window;
    private InventoryDragEvent event;
    private Point[] dragged;
    
    
    public DragEvent(Window window, InventoryDragEvent event) {
        super(event.getView(), null, event.getOldCursor(), false, EMPTY_MAP);
        this.window = window;
        this.event = event;
    }
    
        
    @Override
    public Window getWindow() {
        return window;
    }
    
    
    public @Shared Point[] getDragged() {
        if (dragged == null) {
            dragged = event.getRawSlots().stream().map(window::at).toArray(Point[]::new);
        }
        
        return dragged;
    }
    
    @Override
    public Map<Integer, ItemStack> getNewItems() {
        return event.getNewItems();
    }

    @Override
    public Set<Integer> getRawSlots() {
        return event.getRawSlots();
    }

    @Override
    public Set<Integer> getInventorySlots() {
        return event.getInventorySlots();
    }

    @Override
    public ItemStack getCursor() {
        return event.getCursor();
    }

    @Override
    public void setCursor(ItemStack cusor) {
        event.setCursor(cusor);
    }
    
    @Override
    public DragType getType() {
        return event.getType();
    }

    @Override
    public InventoryDragEvent getEvent() {
        return event;
    }
    
}
