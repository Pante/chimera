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

import java.util.Set;
import java.util.stream.*;

import org.bukkit.inventory.Inventory;


/**
 * Represents a free-form region in an in-game menu.
 * @see com.karuslabs.commons.menu.Menu
 */
public class FreeFormRegion extends Region {
    
    private Set<Integer> slots;

    
    /**
     * Creates a new region with the backing <code>Inventory</code> and slots specified.
     * 
     * @param inventory the inventory
     * @param slots the slots
     */
    public FreeFormRegion(Inventory inventory, int... slots) {
        this(inventory, Button.CANCEL, slots);
    }
    
    /**
     * Creates a new inventory with the backing <code>Inventory</code>, default button, and slots specified.
     * 
     * @param inventory the inventory
     * @param defaultButton the default button
     * @param slots the slots
     */
    public FreeFormRegion(Inventory inventory, Button defaultButton, int... slots) {
        super(inventory, defaultButton);
        int size = inventory.getSize();
        this.slots = IntStream.of(slots).boxed().collect(Collectors.toSet());
        this.slots.forEach(slot -> Preconditions.checkElementIndex(slot, size));
    }
    
    
    /**
     * Determines if the slot specified is within the region.
     * 
     * @param slot the slot
     * @return true, if within the region; else false
     */
    @Override
    public boolean within(int slot) {
        return slots.contains(slot);
    }
    
    
    /**
     * @return the slots that the region is composed of
     */
    public Set<Integer> getSlots() {
        return slots;
    }
    
}
