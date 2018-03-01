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


/**
 * A skeletal implementation of {@code Region}.
 * 
 * @param <GenericButton> the button type
 */
public abstract class AbstractRegion<GenericButton extends Button> extends ResettableComponent implements Region {
    
    /**
     * The buttons which this region contains.
     */
    protected final Buttons<GenericButton> buttons;
    
    
    /**
     * Constructs an {@code AbstractRegion} which does not reset, with the specified backing map.
     * 
     * @param map the backing map
     */
    public AbstractRegion(Map<Integer, GenericButton> map) {
        this(map, false);
    }
    
    /**
     * Constructs an {@code AbstractRegion} with the specified backing map and reset value.
     * 
     * @param map the backing map
     * @param reset true if this region should reset; else false
     */
    public AbstractRegion(Map<Integer, GenericButton> map, boolean reset) {
        super(reset);
        buttons = new Buttons<>(this, map);
    }

    
    /**
     * Delegates the handling of the specified event to the {@code Button} associated with the slot which
     * was clicked, or {@link #clickBlank(ClickEvent)} if no button was associated with the slot; 
     * does nothing if this region does not contain the slot which was clicked.
     * 
     * @param event the event
     */
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
    
    /**
     * Handles the specified event if no button was associated with the slot which was clicked.
     * This method is invoked by {@link #click(ClickEvent)} if this region contains the slot and no button was associated
     * with the slot.
     * 
     * The default implementation cancels the event.
     * 
     * Subclasses should override this method to customise the handling of the specified event.
     * 
     * @param event the event
     */
    protected void clickBlank(ClickEvent event) {
        event.setCancelled(true);
    }
    
    
    /**
     * Delegates the handling of specified event to the button associated with each slot which was dragged,
     * or {@link #dragBlank(DragEvent, int)} if this region contains the slot which was dragged; else does nothing.
     * 
     * Iteration through the slots which were dragged will terminate if the event is canceled.
     * 
     * @param event the event
     */
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
    
    /**
     * Handles the specified event if no button associated with the slot which was dragged.
     * The method is invoked by {@link #drag(DragEvent)} if this region contains the slot and the event is not cancelled.
     * 
     * The default implementation cancels the event.
     * 
     * @param event the event
     * @param dragged the slot which was dragged
     */
    protected void dragBlank(DragEvent event, int dragged) {
        event.setCancelled(true);
    }
    
    
    /**
     * Delegates the handling of the specified event to {@link #onOpen(Window, InventoryOpenEvent)}
     * and the buttons in order when the {@code Window} which contains this region is opened.
     * 
     * @param window the window which contains this region
     * @param event the event
     */
    @Override
    public void open(Window window, InventoryOpenEvent event) {
        onOpen(window, event);
        buttons.values().forEach(button -> button.open(window, event));
    }
    
    /**
     * Handles the specified event when the {@code Window} which contains this region is opened.
     * This method is invoked by {@link #open(Window, InventoryOpenEvent)}.
     * The default implementation does nothing.
     * 
     * Subclasses should override this method to customise the handling of the specified event.
     * 
     * @param window the window which contains this region
     * @param event the event
     */
    protected void onOpen(Window window, InventoryOpenEvent event) {
        
    }
    
    
    /**
     * Delegates the handling of the specified event to {@link #onClose(Window, InventoryCloseEvent)}
     * and the buttons in order when the {@code Window} which contains this region is closed.
     * 
     * @param window the window which contains this region
     * @param event the event
     */
    @Override
    public void close(Window window, InventoryCloseEvent event) {
        onClose(window, event);
        buttons.values().forEach(button -> button.close(window, event));
        
    }
    
    /**
     * Handles the specified event when the {@code Window} which contains this region is closed.
     * This method is invoked by {@link #close(Window, InventoryCloseEvent)}.
     * The default implementation does nothing.
     * 
     * Subclasses should override this method to customise the handling of the specified event.
     * 
     * @param window the window which contains this region
     * @param event the event
     */
    protected void onClose(Window window, InventoryCloseEvent event) {
        
    }
    
    
    /**
     * Delegates the handling of the specified event to {@link #onReset(Window, InventoryCloseEvent)}
     * and the buttons in order when the {@code Window} which contains this region is reset, 
     * or does nothing if this region should not reset.
     * 
     * @param window the window which contains this region
     * @param event the event
     */
    @Override
    public void reset(Window window, InventoryCloseEvent event) {
        if (reset) {
            onReset(window, event);
            buttons.values().forEach(button -> button.reset(window, event));
        }
    }
    
    
    /**
     * Returns the buttons which this region contains.
     * 
     * @return the buttons
     */
    public Buttons<GenericButton> getButtons() {
        return buttons;
    }

}
