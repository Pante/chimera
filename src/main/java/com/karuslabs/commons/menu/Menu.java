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


public class Menu {
    
    protected Handler handler;
    protected Inventory inventory;
    protected Set<Region> regions;
    
    
    public void click(InventoryClickEvent event) {
        handler.click(this, event);
    }
    
    public void drag(InventoryDragEvent event) {
        handler.drag(this, event);
    }
    
    
    public void open(InventoryOpenEvent event) {
        handler.open(this, event);
    }
    
    public void close(InventoryCloseEvent event) {
        handler.close(this, event);
    }
    
    
    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    
    public Inventory getInventory() {
        return inventory;
    }

    
    public Set<Region> getRegions() {
        return regions;
    }
    
}
