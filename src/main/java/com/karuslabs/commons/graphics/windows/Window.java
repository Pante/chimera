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
package com.karuslabs.commons.graphics.windows;

import com.karuslabs.commons.annotation.*;
import com.karuslabs.commons.graphics.*;
import com.karuslabs.commons.graphics.regions.Region;
import com.karuslabs.commons.locale.MessageTranslation;

import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import static java.util.Arrays.asList;
import static java.util.Collections.*;


public abstract class Window implements Listener, InventoryHolder, Resettable {    
    
    @JDK9
    protected static final @Immutable Set<Integer> INVALID = unmodifiableSet(new HashSet<>(asList(-1 -999)));
    
    protected static final Point OUTLINE = new Point(-1, 0);
    protected static final Point OUTSIDE = new Point(-999, 0);
    
    protected List<Region> regions;
    protected MessageTranslation translation;
    protected @Nullable Inventory inventory;
    protected boolean reset;
    
    
    public Window(List<Region> regions, MessageTranslation translation, boolean reset) {
        this.regions = regions;
        this.translation = translation;
        this.reset = reset;
    }
    
    
    public @Immutable Point at(int slot) {
        switch (slot) {
            case -999:
                return OUTSIDE;
                
            case -1:
                return OUTLINE;
                
            default:
                return inside(slot);
        }
    }
    
    protected abstract @Immutable Point inside(int slot);
    
    
    @EventHandler
    public void click(InventoryClickEvent event) {
        if (this != event.getInventory().getHolder()) {
            return;
        }

        if (event.getRawSlot() >= 0) {
            onClick(event);
            ClickEvent click = new ClickEvent(this, event);
            for (Region region : regions) {
                region.click(click);
            }
            
        } else {
            outside(event);
        }
    }
    
    protected void onClick(InventoryClickEvent event) {
        
    }
        
    protected void outside(InventoryClickEvent event) {
        
    }
    
    
    @EventHandler
    public void drag(InventoryDragEvent event) {
        if (this != event.getInventory().getHolder()) {
            return;
        }

        if (disjoint(event.getRawSlots(), INVALID)) {
            onDrag(event);
            DragEvent drag = new DragEvent(this, event);
            for (Region region : regions) {
                region.drag(drag);
            }
            
        } else {
            outside(event);
        }
    }
    
    protected void onDrag(InventoryDragEvent event) {
        
    }
    
    protected void outside(InventoryDragEvent event) {
        event.setCancelled(true);
    }
    
    
    @EventHandler
    public void open(InventoryOpenEvent event) {
        if (this == event.getInventory().getHolder()) {
            onOpen(event);
            for (Region region : regions) {
                region.open(this, event);
            }
        }
    }
    
    protected void onOpen(InventoryOpenEvent event) {
        
    }
    
    
    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (this != event.getInventory().getHolder()) {
            return;
        }
        
        onClose(event);
        for (Region region : regions) {
            region.close(this, event);
        }
        
        if (reset) {
            onReset(event);
            for (Region region : regions) {
                region.reset(this, event);
            }
        }
    }
    
    protected void onClose(InventoryCloseEvent event) {
        
    }
    
    protected void onReset(InventoryCloseEvent event) {
        
    }
    
    
    public List<Region> getRegions() {
        return regions;
    }
    
    public MessageTranslation getTranslation() {
        return translation;
    }
    
    @Override
    public @Nullable Inventory getInventory() {
        return inventory;
    }
    
    public void setInventory(@Nullable Inventory inventory) {
        this.inventory = inventory;
    }
    
    @Override
    public boolean reset() {
        return reset;
    }
    
    @Override
    public void reset(boolean reset) {
        this.reset = reset;
    }
    
}
