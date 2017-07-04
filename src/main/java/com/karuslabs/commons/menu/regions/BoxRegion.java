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
package com.karuslabs.commons.menu.regions;

import com.karuslabs.commons.menu.Menu;
import com.karuslabs.commons.menu.buttons.Button;

import java.util.*;

import org.bukkit.event.inventory.*;


public class BoxRegion extends Region<Button> {
    
    protected Button defaultButton;
    
    protected int width;
    private int min;
    private int max;
    
    private double x1, y1;
    private double x2, y2;
    
    
    public BoxRegion(int inventoryWidth, int minSlot, int maxSlot) {
        this(new HashMap<>(), "", Button.CANCEL, inventoryWidth, minSlot, maxSlot);
    }
    
    public BoxRegion(Map<Integer, Button> buttons, String permission, Button defaultButton, int inventoryWidth, int min, int max) {
        super(buttons, permission);
        this.defaultButton = defaultButton;
        
        this.width = inventoryWidth;
        this.min = min;
        this.max = max;
        
        x1 = min % inventoryWidth; 
        y1 = (double) min / inventoryWidth;
        
        x2 = max % inventoryWidth;
        y2 = (double) max / inventoryWidth;
    }
    
    
    @Override
    public boolean contains(int slot) {
        int x = slot % width;
        double y = slot / (double) width;
        
        boolean containsX;
        boolean containsY;
        
        if (x1 <= x2 && y1 <= y2) {
            containsX = x1 <= x && x <= x2;
            containsY = y1 <= y && y <= y2;
            
        } else {
            containsX = x2 <= x && x <= x1;
            containsY = y2 <= y && y <= y1;
        }
        
        return containsX && containsY;
    }

    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (event.getWhoClicked().hasPermission(permission) && contains(slot)) {
            buttons.getOrDefault(slot, defaultButton).click(menu, event);
        }
    }
    
    @Override
    public void drag(Menu menu, InventoryDragEvent event) {
        if (event.getWhoClicked().hasPermission(permission)) {
            event.getRawSlots().forEach(slot -> {
                if (contains(slot)) {
                    buttons.getOrDefault(slot, defaultButton).drag(menu, event);
                }
            });
        }
    }
    
    
    public int getMin() {
        return min;
    }
    
    public void setMin(int min) {
        this.min = min;
        x1 = min % width;
        y1 = (double) min / width;
    }
    
    public int getMax() {
        return max;
    }
    
    public void setMax(int max) {
        this.max = max;
        x2 = max % width;
        y2 = (double) max / width; 
    }
    
    
    public static BoxRegionBuilder newBoxRegion() {
        return new BoxRegionBuilder(new BoxRegion(9, 0, 0));
    }
    
    
    public static class BoxRegionBuilder extends Builder<BoxRegionBuilder, BoxRegion, Button> {

        public BoxRegionBuilder(BoxRegion region) {
            super(region);
        }

        
        public BoxRegionBuilder defaultButton(Button defaultButton) {
            region.defaultButton = defaultButton;
            return this;
        }

        public BoxRegionBuilder inventoryWidth(int width) {
            region.width = width;
            return this;
        }

        public BoxRegionBuilder min(int min) {
            region.setMin(min);
            return this;
        }

        public BoxRegionBuilder max(int max) {
            region.setMax(max);
            return this;
        }

        
        @Override
        protected BoxRegionBuilder getThis() {
            return this;
        }

    }
    
}
