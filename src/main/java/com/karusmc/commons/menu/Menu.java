/*
 * Copyright (C) 2017 PanteLegacy @ karusmc.com
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
package com.karusmc.commons.menu;

import java.util.*;

import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;


/**
 * Represents a GUI menu that provides a graphical view and delegates event handling to the specific {@link Button} instances.
 */
public interface Menu {
    
    /**
     * Delegates event handling to a {@link Button} instance binded to the slot.
     * 
     * @param event A InventoryClickEvent instance
     */
    public void onClick(InventoryClickEvent event);
    
    /**
     * Delegates event handling to a {@link Button} instance binded to the slot.
     * Default implementation cancels and ignores the event.
     * 
     * @param event A InventoryDragEvent instance
     */
    public default void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }
    
    
    /**
     * Returns an Inventory instance.
     * 
     * @return The Inventory instance
     */
    public Inventory getInventory();
    
    
    /**
     * Returns the menu's slots and buttons binded to them.
     * 
     * @return the menu's slots and binded buttons.
     */
    public Map<Integer, Button> getButtons();
    
}
