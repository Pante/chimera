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
import org.bukkit.inventory.*;


public class Menu implements InventoryHolder {
    
    private Inventory inventory;
    private List<Region> regions;
    
    
    public Menu(Inventory inventory) {
        this(inventory, new ArrayList<>());
    }
    
    public Menu(Inventory inventory, List<Region> regions) {
        this.inventory = inventory;
        this.regions = regions;
    }
    
    
    public void click(InventoryClickEvent event) {
        regions.forEach(region -> region.click(this, event));
    }
    
    public void drag(InventoryDragEvent event) {
        regions.forEach(region -> region.drag(this, event));
    }
    
    
    public void open(InventoryOpenEvent event) {
        regions.forEach(region -> region.open(this, event));
    }
    
    public void close(InventoryCloseEvent event) {
        regions.forEach(region -> region.close(this, event));
    }
            
    
    public String getTitle() {
        return inventory.getTitle();
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    public List<Region> getRegions() {
        return regions;
    }
    
}
