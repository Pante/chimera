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
import java.util.function.BiConsumer;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;


public class Menu extends Region {
    
    private Set<Region> regions;
    private BiConsumer<Menu, InventoryCloseEvent> close;
    
    
    public Menu(Inventory inventory) {
        super(inventory, Button.CANCEL);
        close = (menu, event) -> {};
    }
    
    public Menu(Inventory inventory, Button defaultButton, BiConsumer<Menu, InventoryCloseEvent> close) {
        super(inventory, defaultButton);
        regions = new HashSet<>();
        this.close = close;
    }
    
    
    //REDO
    public void onClick(InventoryClickEvent event) {
        int slot = event.getRawSlot();
        if (within(slot)) {
            for (Region region : regions) {
                if (region.within(slot)) {
                    region.getButton(slot).onClick(this, event);
                    return;
                }
            }
            
            buttons.getOrDefault(slot, defaultButton).onClick(this, event);
        }
        
    }
    
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }
    
    public void onClose(InventoryCloseEvent event) {
        close.accept(this, event);
    }
    
    
    public Set<Region> getRegions() {
        return regions;
    }

    @Override
    public boolean within(int slot) {
        return slot >= 0 && slot < inventory.getSize();
    }
    
}
