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

import com.karuslabs.commons.graphics.windows.Window;

import org.bukkit.event.inventory.*;


/**
 * Represents a component in a {@code Window}.
 */
@FunctionalInterface
public interface Component {
    
    /**
     * Handles the specified event when this component is clicked.
     * 
     * @param event the event
     */
    public void click(ClickEvent event);
    
    
    /**
     * Handles the specified event when the {@code Window} which contains this component is opened.
     * The default implementation does nothing.
     * 
     * Implementations should override this method to cusotmise the handling of the specified event.
     * 
     * @param window the window which contains this component
     * @param event the event
     */
    public default void open(Window window, InventoryOpenEvent event) {
        
    }
    
    /**
     * Handles the specified event when the {@code Window} which contains this component is closed.
     * The default implementation does nothing.
     * 
     * Implementations should override this method to cusotmise the handling of the specified event.
     * 
     * @param window the window which contains this component
     * @param event the event
     */
    public default void close(Window window, InventoryCloseEvent event) {
        
    }
    
    /**
     * Handles the specified event when the {@code Window} which contains this component is reset.
     * The default implementation does nothing.
     * 
     * Implementations should override this method to cusotmise the handling of the specified event.
     * 
     * @param window the window which contains this component
     * @param event the event
     */
    public default void reset(Window window, InventoryCloseEvent event) {
        
    }
    
}
