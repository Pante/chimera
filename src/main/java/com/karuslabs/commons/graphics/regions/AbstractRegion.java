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
import com.karuslabs.commons.collection.ProxiedMap;
import com.karuslabs.commons.graphics.*;

import java.util.Map;


public abstract class AbstractRegion<GenericButton extends Button> extends ProxiedMap<Point, GenericButton> implements Region {
    
    public AbstractRegion(Map<Point, GenericButton> map) {
        super(map);
    }

    
    @Override
    public void click(ClickContext context) {
        GenericButton button = map.get(context.getPoint());
        if (button != null) {
            button.click(context);
            
        } else {
            clickBlank(context);
        }
    }
    
    protected void clickBlank(ClickContext context) {
        context.setCancelled(true);
    }
    
    
    @Override
    public void drag(DragContext context) {
        for (Point point : context.getDragged()) {
            if (!context.isCancelled()) {
                GenericButton button = map.get(point);
                if (button != null) {
                    button.drag(point, context);
                    
                } else {
                    dragBlank(point, context);
                }
                
            } else {
                break;
            }
        }
    }
    
    protected void dragBlank(Point point, DragContext context) {
        context.setCancelled(true);
    }
    
    @Override
    public GenericButton put(Point point, GenericButton button) {
        if (contains(point)) {
            return map.put(point, button);
            
        } else {
            throw new IllegalArgumentException("Point must be within region");
        }
    }
    
    @Override
    public abstract int size();
    
    public int buttons() {
        return map.size();
    }
    
}
