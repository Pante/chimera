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

import org.bukkit.inventory.Inventory;


public class RectangularRegion extends Region {
    
    private int length;
    private int min, max;
    
    
    public RectangularRegion(Inventory inventory, int length, int min, int max) {
        this(inventory, Button.CANCEL, length, min, max);
    }
    
    public RectangularRegion(Inventory inventory, Button defaultButton, int length, int min, int max) {
        super(inventory, defaultButton);
        this.length = length;
        this.min = min;
        this.max = max;
    }
    

    @Override
    public boolean within(int slot) {
        int row = slot % length;
        double column = slot / (double) length;
        
        boolean withinLength = row >= min % length && row <= max % length;
        boolean withinColumn = column >= min / length && column <= max / length;
        
        return withinLength && withinColumn;
    }
    
    
    public int getLength() {
        return length;
    }
    
    public int getMin() {
        return min;
    }
    
    public int getMax() {
        return max;
    }
    
}
