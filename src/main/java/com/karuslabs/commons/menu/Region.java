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

import org.bukkit.inventory.ItemStack;


public class Region {
    
    private int layer;
    private int length;
    private int min, max;
    
    private Map<Integer, ItemStack> items;
    private Map<Integer, Button> buttons;
    
    
    public Region(int layer, int length, int min, int max) {
        this.layer = layer;   
        this.length = length;
        
        this.min = min;
        this.max = max;
        
        items = new HashMap<>();
        buttons = new HashMap<>();
    }
    
    
    public boolean within(int slot) {
        int row = slot % length;
        double column = slot / (double) length;
        
        boolean withinLength = row >= min % length && row <= max % length;
        boolean withinColumn = column >= min / length && column <= max / length;
        
        return withinLength && withinColumn;
    }
    
    
    public Region bind(ItemStack item, int... slots) {
        for (int slot : slots) {
            items.put(slot, item);
        }
        return this;
    }
    
    public Region bind(Button button, int... slots) {
        for (int slot : slots) {
            buttons.put(slot, button);
        }
        return this;
    }
    
    
    public Map<Integer, ItemStack> getItems() {
        return items;
    }
    
    public Map<Integer, Button> getButtons() {
        return buttons;
    }
    
}
