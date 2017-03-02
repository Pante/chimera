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
import org.bukkit.inventory.Inventory;


public class Menu extends Region {
    
    private Set<Region> regions;
    
    
    public Menu(Inventory inventory) {
        super(inventory);
        regions = new HashSet<>();
    }
    
    
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
    
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }
    
    public void onClose(InventoryCloseEvent event) {
        MenuPool.INSTANCE.getActive().remove(event.getPlayer());
    }
    

    @Override
    public boolean within(int slot) {
        return slot >= 0 && slot < inventory.getSize();
    }
    
}
