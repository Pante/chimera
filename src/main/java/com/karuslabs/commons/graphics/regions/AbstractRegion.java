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
import com.karuslabs.commons.locale.MessageTranslation;

import java.util.Map;

import org.bukkit.event.inventory.*;


public abstract class AbstractRegion<GenericButton extends Button> extends ResettableComponent implements Region {
    
    private final RegionMap<GenericButton> map;
    
    
    public AbstractRegion(Map<Point, GenericButton> map) {
        this(map, false);
    }
    
    public AbstractRegion(Map<Point, GenericButton> map, boolean reset) {
        super(reset);
        this.map = new RegionMap<>(this, map);
    }

    
    @Override
    public void click(Point clicked, InventoryClickEvent event, MessageTranslation translation) {
        GenericButton button = map.get(clicked);
        if (button != null) {
            button.click(clicked, event, translation);
            
        } else {
            clickBlank(clicked, event, translation);
        }
    }
    
    protected void clickBlank(Point clicked, InventoryClickEvent event, MessageTranslation translation) {
        event.setCancelled(true);
    }
    
    
    @Override
    public void drag(Point[] dragged, InventoryDragEvent event, MessageTranslation translation) {
        for (Point point : dragged) {
            if (event.isCancelled()) {
                return;
            }
            
            if (contains(point)) {
                GenericButton button = map.get(point);
                if (button != null) {
                    button.drag(point, dragged, event, translation);

                } else {
                    dragBlank(point, dragged, event, translation);
                }
            }
        }
    }
    
    protected void dragBlank(Point point, Point[] dragged, InventoryDragEvent event, MessageTranslation translation) {
        event.setCancelled(true);
    }
    
    
    @Override
    public void open(InventoryOpenEvent event, MessageTranslation translation) {
        onOpen(event, translation);
        map.values().forEach(button -> button.open(event, translation));
    }
    
    protected void onOpen(InventoryOpenEvent event, MessageTranslation translation) {
        
    }
    
    
    @Override
    public void close(InventoryCloseEvent event, MessageTranslation translation) {
        onClose(event);
        map.values().forEach(button -> button.close(event, translation));
        
    }
    
    protected void onClose(InventoryCloseEvent event) {
        
    }
    
    
    @Override
    public void reset(InventoryCloseEvent event, MessageTranslation translation) {
        if (reset) {
            onReset(event, translation);
            map.values().forEach(button -> button.reset(event, translation));
        }
    }

}
