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

import com.karuslabs.commons.graphics.buttons.Button;
import com.karuslabs.commons.graphics.*;
import com.karuslabs.commons.locale.MessageTranslation;

import org.bukkit.event.inventory.*;


class Singleton extends ResettableComponent implements Region {
    
    private Point point;
    private Button button;
    
    
    Singleton(Point point, Button button, boolean reset) {
        super(reset);
        this.point = point;
        this.button = button;
    }
    
    
    @Override
    public boolean contains(Point point) {
        return this.point.equals(point);
    }
    
    @Override
    public void click(Point clicked, InventoryClickEvent event, MessageTranslation translation) {
        if (clicked.equals(point)) {
            button.click(clicked, event, translation);
        }
    }
    
    @Override
    public void drag(Point[] dragged, InventoryDragEvent event, MessageTranslation translation) {
        if (event.isCancelled()) {
            return;
        }
        
        for (Point point : dragged) {
            if (contains(point)) {
                button.drag(point, dragged, event, translation);
                break;
            }
        }
    }
    
    @Override
    public void open(InventoryOpenEvent event, MessageTranslation translation) {
        button.open(event, translation);
    }
    
    @Override
    public void close(InventoryCloseEvent event, MessageTranslation translation) {
        button.close(event, translation);
    }

    @Override
    public int size() {
        return 1;
    }

}
