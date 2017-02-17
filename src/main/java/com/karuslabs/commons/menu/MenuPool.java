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


/**
 * Serves as a singleton entry point for the menu framework and should be registered to the server.
 * Delegation of event handling requires the client to manually register and unregister menus and the players viewing them.
 */
public class MenuPool implements Listener {
    
    public static final MenuPool INSTANCE = new MenuPool();
    
    
    private ConcurrentHashMap<String, Menu> pooledMenus;
    private WeakHashMap<HumanEntity, Menu> menus;
    
    
    protected MenuPool() {
        pooledMenus = new ConcurrentHashMap<>();
        menus = new WeakHashMap<>();
    }
    
    
    /**
     * Checks if there are any corresponding players registered to the MenuPool
     * and delegates event handling to the menu registered with the player if present.
     * 
     * @param event The InventoryClickEvent instance
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (menus.containsKey(event.getWhoClicked())) {
            menus.get(event.getWhoClicked()).onClick(event);
        }
    }
    
    
    /**
     * Checks if there are any corresponding players registered to the MenuPool
     * and delegates event handling to the menu registered with the player if present.
     * 
     * @param event The InvntoryDragEvent event
     */
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (menus.containsKey(event.getWhoClicked())) {
            menus.get(event.getWhoClicked()).onDrag(event);
        }
    }

    
    /**
     * Returns a thread-safe view of the pooled menus with their titles as keys.
     * 
     * @return A thread-safe map containing the pooled menus.
     */
    public ConcurrentHashMap<String, Menu> getPooledMenus() {
        return pooledMenus;
    }

    
    /**
     * Returns the currently registered players and menus.
     * 
     * @return The registered players and menus
     */
    public Map<HumanEntity, Menu> getMenus() {
        return menus;
    }
    
}
