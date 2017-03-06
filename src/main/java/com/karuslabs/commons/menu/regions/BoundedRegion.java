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


/**
 * Represents a rectangular region in an in-game menu.
 * @see com.karuslabs.commons.menu.Menu
 */
public class BoundedRegion extends Region {
    
    private int length;
    private int min, max;
    
    private int minRow, maxRow;
    private double minColumn, maxColumn;
    
    
    /**
     * Creates a new region with the backing <code>Inventory</code> and corners specified.
     * 
     * @param inventory the inventory
     * @param min the first corner of the region
     * @param max the second corner of the region
     */
    public BoundedRegion(Inventory inventory, int min, int max) {
        this(inventory, Button.CANCEL, min, max);
    }
    
    /**
     * Creates a new region with the backing <code>Inventory</code>, default button and corners specified.
     * 
     * @param inventory the inventory
     * @param defaultButton the button
     * @param min the first corner of the region
     * @param max the second corner of the region
     */
    public BoundedRegion(Inventory inventory, Button defaultButton, int min, int max) {
        super(inventory, defaultButton);
        length = BoundedRegionUtility.getLength(inventory.getType());
        
        int size = inventory.getSize();
        
        this.min = Preconditions.checkElementIndex(min, size);
        this.max = Preconditions.checkElementIndex(max, size);
        
        minRow = min % length; maxRow = max % length;
        minColumn = (double) min / length; maxColumn = (double) max / length;
    }
    
    
    /**
     * Determines if the slot specified is within the region.
     * 
     * @param slot the slot
     * @return true, if within the rectangular region; else false
     */
    @Override
    public boolean within(int slot) {
        int row = slot % length;
        double column = slot / (double) length;
        
        boolean withinRows = row >= minRow && row <= maxRow;
        boolean withinColumns = column >= minColumn && column <= maxColumn;
        
        return withinRows && withinColumns;
    }
    
    
    public int getMin() {
        return min;
    }
    
    public int getMax() {
        return max;
    }
    
}
