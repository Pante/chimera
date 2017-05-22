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
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;


public class MenuManager implements Listener {
    
    private ConcurrentHashMap<String, Menu> pooled;
    private WeakHashMap<HumanEntity, Menu> active;
    
    
    public MenuManager() {
        pooled = new ConcurrentHashMap<>();
        active = new WeakHashMap<>();
    }
    
    
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Menu menu = active.get(event.getWhoClicked());
        if (menu != null) {
            menu.click(event);
        }
    }
    
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Menu menu = active.get(event.getWhoClicked());
        if (menu != null) {
            menu.drag(event);
        }
    }
   
    
    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Menu menu = active.get(event.getPlayer());
        if (menu != null) {
            menu.open(event);
        }
    }
    
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Menu menu = active.get(event.getPlayer());
        if (menu != null) {
            menu.close(event);
        }
    }
    
    
    public ConcurrentHashMap<String, Menu> getPooled() {
        return pooled;
    }

    public WeakHashMap<HumanEntity, Menu> getActive() {
        return active;
    }
    
}
