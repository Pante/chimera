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

import com.karuslabs.commons.graphics.regions.Region;
import com.karuslabs.commons.locale.MessageTranslation;

import javax.annotation.Nullable;

import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;


public abstract class Window<GenericInventory extends Inventory> implements Listener, InventoryHolder {
    
    protected MessageTranslation translation;
    protected @Nullable GenericInventory inventory;
    protected Region[] regions;
    
    
    public Window(MessageTranslation translation, @Nullable GenericInventory inventory, Region... regions) {
        this.translation = translation;
        this.inventory = inventory;
        this.regions = regions;
    }
    
        
    protected abstract Point from(int slot);
    
    
    @EventHandler
    public void click(InventoryClickEvent event) {
        Point point = from(event.getRawSlot());
        onClick(point, event);
        for (Region region : regions) {
            region.click(point, event, translation);
        }
    }
    
    protected void onClick(Point point, InventoryClickEvent event) {
        
    }
    
    
    @EventHandler
    public void drag(InventoryDragEvent event) {
        Point[] dragged = event.getRawSlots().stream().map(this::from).toArray(Point[]::new);
        for (Region region : regions) {
            region.drag(dragged, event, translation);
        }
    }
    
    protected void onDrag(Point[] dragged, InventoryDragEvent event) {
        
    }
    
    
    @EventHandler
    public void open(InventoryOpenEvent event) {
        for (Region region : regions) {
            region.open(event, translation);
        }
    }
    
    protected void onOpen(InventoryOpenEvent event) {
        
    }
    
    
    public MessageTranslation getTranslation() {
        return translation;
    }
    
    @Override
    public @Nullable GenericInventory getInventory() {
        return inventory;
    }
    
}
