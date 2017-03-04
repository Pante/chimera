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

import com.karuslabs.commons.menu.regions.Region;

import java.util.*;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;


/**
 * Represents an in-game menu which may contain regions.
 */
public class Menu extends Region {
    
    private Set<Region> regions;
    
    
    /**
     * Creates a new menu with the backing inventory specified.
     * 
     * @param inventory the inventory
     */
    public Menu(Inventory inventory) {
        this(inventory, Button.CANCEL);
    }
    
    /**
     * Creates a new menu with the backing inventory and default button specified.
     * 
     * @param inventory the inventory
     * @param defaultButton the button
     */    
    public Menu(Inventory inventory, Button defaultButton) {
        super(inventory, defaultButton);
        regions = new HashSet<>();
    }
    
    
    /**
     * Delegates event handling to a region, if the slot is within a region; else the internally associated button.
     * 
     * @param event the <code>InventoryClickEvent</code>
     */
    public void onClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (within(slot)) {
            for (Region region : regions) {
                if (region.within(slot)) {
                    region.getButtonOrDefault(slot).onClick(this, event);
                    return;
                }
            }
            
            getButtonOrDefault(slot).onClick(this, event);
        }
    }
    
    /**
     * Cancels the event.
     * 
     * @param event the <code>InventoryDragEvent</code>
     */
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }
    
    /**
     * Removes the menu from the <code>MenuPool</code>.
     * @see MenuPool
     * 
     * @param event the <code>InventoryClosEvent</code>
     */
    public void onClose(InventoryCloseEvent event) {
        MenuPool.INSTANCE.getActive().remove(event.getPlayer());
    }
     
    
    /**
     * Determines if the slot specified is within the menu.
     * 
     * @param slot the slot
     * @return <code>true</code>, if the slot is not negative and is less than the backing inventory size; else <code>false</code>
     */
    @Override
    public boolean within(int slot) {
        return slot >= 0 && slot < inventory.getSize();
    }
    
    
    public Set<Region> getRegions() {
        return regions;
    }
    
}
