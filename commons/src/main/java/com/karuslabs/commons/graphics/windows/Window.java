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
import com.karuslabs.commons.locale.*;

import java.util.*;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import static java.util.Arrays.asList;
import static java.util.Collections.*;


/**
 * Represents a GUI using a backing {@code Inventory} which may be reset.
 */
public abstract class Window implements Listener, Translatable, InventoryHolder, Resettable {
    
    /**
     * The set which contains invalid slots.
     */
    @JDK9
    protected static final @Immutable Set<Integer> INVALID = unmodifiableSet(new HashSet<>(asList(-1, -999)));
    
    /**
     * The point which indicates the outline of an inventory.
     */
    protected static final Point OUTLINE = new Point(-1, 0);
    /**
     * The point which indicates the outside of an inventory.
     */
    protected static final Point OUTSIDE = new Point(-999, 0);
    
    /**
     * The regions this window contains.
     */
    protected List<Region> regions;
    /**
     * The translation.
     */
    protected MessageTranslation translation;
    /**
     * The backing inventory.
     */
    protected @Nullable Inventory inventory;
    /**
     * Whether this window should reset.
     */
    protected boolean reset;
    
    
    /**
     * Constructs a {@code Window} with the specified regions, translation and reset value.
     * 
     * @param regions the regions
     * @param translation the translation
     * @param reset true if this window should reset; else false
     */
    public Window(List<Region> regions, MessageTranslation translation, boolean reset) {
        this.regions = regions;
        this.translation = translation;
        this.reset = reset;
    }
    
    
    /**
     * Renders this window to the specified {@code Player}s.
     * 
     * @param players the players
     */
    public void render(List<Player> players) {
        players.forEach(this::render);
    }
    
    /**
     * Renders this window to the specified {@code Player}.
     * 
     * @param player the player
     */
    public void render(Player player) {
        player.openInventory(inventory);
    }
    
    
    /**
     * Returns a {@code Point} which represents the specified slot.
     * 
     * @param slot the slot
     * @return a Point which represents the specified slot
     */
    public @Immutable Point at(int slot) {
        switch (slot) {     
            case -1:
                return OUTLINE;
                
            case -999:
                return OUTSIDE;
                
            default:
                return inside(slot);
        }
    }
    
    /**
     * Creates a point which represents the specified slot within this {@code Window}.
     * 
     * @param slot the slot
     * @return the point which represents the specified slot within this window
     */
    protected abstract @Immutable Point inside(int slot);
    
    
    /**
     * Delegates the handling of the specified event to {@link #onClick(InventoryClickEvent)} and the regions in order,
     * or {@link #outside(InventoryClickEvent)} if the clicked slot is not within the window; does nothing if this window
     * is not equal to the window returned by the event.
     * 
     * @param event the event
     */
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
    
    /**
     * Handles the specified event when this window is clicked.
     * This method is invoked by {@link #click(InventoryClickEvent)} if this window and the window clicked are the same, 
     * and the clicked slot is within this window.
     * 
     * The default implementation does nothing.
     * 
     * Subclasses should override this method to customise the handling of the specified event.
     * 
     * @param event the event
     */
    protected void onClick(InventoryClickEvent event) {
        
    }
    
    /**
     * Handles the specified event when the area outside of this window is clicked.
     * This method is invoked by {@link #click(InventoryClickEvent)} if this window and the window clicked are the same,
     * and the clicked slot it outside this window.
     * 
     * The default implementation does nothing.
     * 
     * Subclasses should override this method to customise the handling of the specified event
     * 
     * @param event the event
     */
    protected void outside(InventoryClickEvent event) {
        
    }
    
    
    /**
     * Delegates the handling of the specified event to {@link #onDrag(InventoryDragEvent)} and the regions in order,
     * or {@link #outside(InventoryDragEvent)} if any of the dragged slots is not within the window; does nothing if this window
     * is not equal to the window returned by the event.
     * 
     * @param event the event
     */
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
    
    /**
     * Handles the specified event when the slots within this window are dragged.
     * This method is invoked by {@link #drag(InventoryDragEvent)} if this window and the window dragged are the same, 
     * and the dragged slots are within this window.
     * 
     * The default implementation does nothing.
     * 
     * Subclasses should override this method to customise the handling of the specified event.
     * 
     * @param event the event
     */
    protected void onDrag(InventoryDragEvent event) {
        
    }
    
    /**
     * Handles the specified event when the area outside of this window is dragged.
     * This method is invoked by {@link #drag(InventoryDragEvent)} if this window and the window dragged are the same,
     * and any of the dragged slots is outside this window.
     * 
     * The default implementation cancels the specified event.
     * 
     * Subclasses should override this method to customise the handling of the specified event
     * 
     * @param event the event
     */
    protected void outside(InventoryDragEvent event) {
        event.setCancelled(true);
    }
    
    
    /**
     * Delegates the handling of the specified event to {@link #onOpen(InventoryOpenEvent)} and the regions in order,
     * or does nothing if this window is not equal to the window returned by the event.
     * 
     * @param event the event
     */
    @EventHandler
    public void open(InventoryOpenEvent event) {
        if (this == event.getInventory().getHolder()) {
            onOpen(event);
            for (Region region : regions) {
                region.open(this, event);
            }
        }
    }
    
    /**
     * Handles the specified event when this window is opened.
     * This method is invoked by {@link #open(InventoryOpenEvent)} if this window and the window returned by the specified event are the same.
     * 
     * The default implementation does nothing.
     * 
     * Subclasses should override this method to customise the handling of the specified event.
     * 
     * @param event the event
     */
    protected void onOpen(InventoryOpenEvent event) {
        
    }
    
    
    /**
     * Does nothing if this window is not equal to the window returned by the specified event.
     * 
     * Delegates the handling of the specified event to {@link #onClose(InventoryCloseEvent)} and the regions in order;
     * delegates the resetting of this window to {@link #onReset(InventoryCloseEvent)} and the regions in order if
     * this window should reset.
     * 
     * @param event the event
     */
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
    
    /**
     * Handles the specified event when this window is closed.
     * This method is invoked by {@link #close(InventoryCloseEvent)} if this window and the window returned by the specified event are the same.
     * 
     * The default implementation does nothing.
     * 
     * Subclasses should override this method to customise the handling of the specified event.
     * 
     * @param event the event
     */
    protected void onClose(InventoryCloseEvent event) {
        
    }
    
    /**
     * Resets the window when this window is closed.
     * This method is invoked by {@link #close(InventoryCloseEvent)} if this window and the window returned by the specified event are the same
     * and the window should reset.
     * 
     * The default implementation does nothing.
     * 
     * Subclasses should override this method to customise the resetting of this window.
     * 
     * @param event the event
     */
    protected void onReset(InventoryCloseEvent event) {
        
    }
    
    
    /**
     * Returns the regions which this window contains.
     * 
     * @return the regions
     */
    public List<Region> getRegions() {
        return regions;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public MessageTranslation getTranslation() {
        return translation;
    }
    
    /**
     * Returns the backing inventory.
     * 
     * @return the backing inventory
     */
    @Override
    public @Nullable Inventory getInventory() {
        return inventory;
    }
    
    /**
     * Sets the backing inventory.
     * 
     * @param inventory the backing inventory
     * @return this
     */
    public Window setInventory(@Nullable Inventory inventory) {
        this.inventory = inventory;
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean reset() {
        return reset;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void reset(boolean reset) {
        this.reset = reset;
    }
    
}
