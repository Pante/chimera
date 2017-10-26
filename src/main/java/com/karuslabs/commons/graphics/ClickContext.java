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

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;


public class ClickContext extends InventoryClickEvent {
    
    private MessageTranslation translation;
    private InventoryClickEvent event;
    
    
    public ClickContext(MessageTranslation translation, InventoryClickEvent event) {
        super(event.getView(), event.getSlotType(), event.getRawSlot(), event.getClick(), event.getAction(), event.getHotbarButton());
        this.translation = translation;
        this.event = event;
    }
    
    
    public MessageTranslation getTranslation() {
        return translation;
    }
    
    @Override
    public ItemStack getCurrentItem() {
        return event.getCurrentItem();
    }
    
    @Override
    public void setCurrentItem(ItemStack item) {
        event.setCurrentItem(item);
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
