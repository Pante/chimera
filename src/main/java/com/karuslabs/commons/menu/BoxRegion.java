/*
 * Copyright (C) 2017 Karus Labs
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.karuslabs.commons.menu;

import java.util.*;

import org.bukkit.event.inventory.*;


public class BoxRegion extends Region {
    
    private int length;
    protected int min;
    protected int max;
    
    private double x1, y1;
    private double x2, y2;
    
    
    public BoxRegion() {
        this(new HashMap<>(), "", "", 9, 0, 0);
    }
    
    public BoxRegion(Map<Integer, Button> buttons, String permission, String message, int length, int min, int max) {
        super(buttons, permission, message);
        this.length = length;
        this.min = min;
        this.max = max;
        
        x1 = min % length; 
        y1 = (double) min / length;
        
        x2 = max % length;
        y2 = (double) max / length;
    }
    
    
    @Override
    public boolean contains(int slot) {
        int x = slot % length;
        double y = slot / (double) length;
        
        boolean containsX = x >= x1 && x <= x2;
        boolean containsY = y >= y1 && y <= y2;
        
        return containsX && containsY;
    }

    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        if (event.getWhoClicked().hasPermission(permission)) {
            if (contains(event.getRawSlot())) {
                buttons.getOrDefault(event.getRawSlot(), Button.NONE).click(menu, event);
            }

        } else {
            onInvalidPermission(menu, event);
        }
    }

    
    @Override
    public void drag(Menu menu, InventoryDragEvent event) {
        if (event.getWhoClicked().hasPermission(permission)) {
            event.getRawSlots().forEach(slot -> {
                if (contains(slot)) {
                    buttons.getOrDefault(slot, Button.NONE).drag(menu, event);
                }
            });

        } else {
            onInvalidPermission(menu, event);
        }
    }

    
    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
    
    
    public static BoxRegionBuilder builder() {
        return new BoxRegionBuilder(new BoxRegion());
    }
    
    
    public static class BoxRegionBuilder extends RegionBuilder<BoxRegion> {
        
        public BoxRegionBuilder(BoxRegion region) {
            super(region);
        }
        
    }
    
}
