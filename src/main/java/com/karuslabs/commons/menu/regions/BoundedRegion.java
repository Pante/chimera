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
package com.karuslabs.commons.menu.regions;

import com.google.common.base.Preconditions;

import com.karuslabs.commons.menu.Button;

import org.bukkit.inventory.*;


public class BoundedRegion extends Region {
    
    private int length;
    private int min, max;
    
    
    public BoundedRegion(Inventory inventory, int min, int max) {
        this(inventory, Button.CANCEL, min, max);
    }
    
    public BoundedRegion(Inventory inventory, Button defaultButton, int min, int max) {
        super(inventory, defaultButton);
        length = BoundedRegionUtility.getLength(inventory.getType());
        
        int size = inventory.getSize();
        
        this.min = Preconditions.checkElementIndex(min, size);
        this.max = Preconditions.checkElementIndex(max, size);
    }
    

    @Override
    public boolean within(int slot) {
        int row = slot % length;
        double column = slot / (double) length;
        
        boolean withinRows = row >= min % length && row <= max % length;
        boolean withinColumns = column >= min / length && column <= max / length;
        
        return withinRows && withinColumns;
    }
    
    
    public int getMin() {
        return min;
    }
    
    public int getMax() {
        return max;
    }
    
}
