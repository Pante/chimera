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

import java.util.Set;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;


public class Menu implements Region {
    
    private Inventory inventory;
    private Button[][] buttons;
    private Set<Region> regions;
    
    
    public Menu(Inventory inventory) {
        this.inventory = inventory;
    }
    
    
    @Override
    public boolean within(int slot) {
        return slot >= 0 && slot < inventory.getSize();
    }

    @Override
    public void click(InventoryClickEvent event) {
        regions.forEach(region -> region.click(event));
    }

    @Override
    public void drag(InventoryDragEvent event) {
        regions.forEach(region -> region.drag(event));
    }

    
    @Override
    public Button[][] getButtons() {
        return buttons;
    }

    @Override
    public Set<Region> getChildRegions() {
        return regions;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
}
