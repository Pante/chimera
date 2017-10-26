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

import com.karuslabs.commons.locale.MessageTranslation;
import java.util.*;

import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

import static java.util.Collections.EMPTY_MAP;


public class DragContext extends InventoryDragEvent {
    
    private Point[] dragged;
    private MessageTranslation translation;
    private InventoryDragEvent event;
    
    
    public DragContext(Point[] dragged, MessageTranslation translation, InventoryDragEvent event) {
        super(event.getView(), event.getCursor(), event.getOldCursor(), false, EMPTY_MAP);
        this.dragged = dragged;
        this.translation = translation;
        this.event = event;
    }
    
    
    public Point[] getDragged() {
        return dragged;
    }
    
    public MessageTranslation getTranslation() {
        return translation;
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
    public Result getResult() {
        return event.getResult();
    }
    
    @Override
    public void setResult(Result result) {
        event.setResult(result);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }
    
}
