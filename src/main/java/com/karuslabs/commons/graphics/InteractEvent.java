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
import com.karuslabs.commons.locale.MessageTranslation;

import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryInteractEvent;


interface InteractEvent<GenericEvent extends InventoryInteractEvent> {
    
    public Window getWindow();
    
    public default MessageTranslation getTranslation() {
        return getWindow().getTranslation();
    }
    
    
    public GenericEvent getEvent();
    
    
    public default Result getResult() {
        System.out.println("getResult() called");
        return getEvent().getResult();
    }
    
    public default void setResult(Result result) {
        getEvent().setResult(result);
    }

    
    public default boolean isCancelled() {
        return getEvent().isCancelled();
    }

    public default void setCancelled(boolean cancelled) {
        getEvent().setCancelled(cancelled);
    }
    
}
