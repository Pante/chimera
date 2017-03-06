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
 * Represents a singleton facade for accessing menu-related functions and contains pool <code>Menu</code>s.
 * May listen for inventory related events and delegate events to registered menus.
 */
public class MenuPool implements Listener {
    
    public static final MenuPool INSTANCE = new MenuPool();
    
    
    private ConcurrentHashMap<String, Menu> pooled;
    private WeakHashMap<HumanEntity, Menu> active;
    
    
    protected MenuPool() {
        pooled = new ConcurrentHashMap<>();
        active = new WeakHashMap<>();
    }
    
    
    /**
     * Delegates event handling to the menu, if the menu is currently used by a player; else ignore.
     * 
     * @param event the InventoryClickEvent
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        HumanEntity player = event.getWhoClicked();
        if (active.containsKey(player)) {
            active.get(player).onClick(event);
        }
    }
    
    
    /**
     * Delegates event handling to the menu, if the menu is currently used by a player; else ignore.
     * 
     * @param event the InvntoryDragEvent
     */
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        HumanEntity player = event.getWhoClicked();
        if (active.containsKey(player)) {
            active.get(player).onDrag(event);
        }
    }
    
    
    /**
     * Delegates event handling to the menu, if the menu is currently used by a player; else ignore.
     * 
     * @param event the InventoryCloseEvent
     */
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        if (active.containsKey(player)) {
            active.get(player).onClose(event);
        }
    }

    
    /**
     * @return the pooled Menus
     */
    public ConcurrentHashMap<String, Menu> getPooled() {
        return pooled;
    }

    
    /**
     * @return the menus currently in use by players
     */
    public Map<HumanEntity, Menu> getActive() {
        return active;
    }
    
}
