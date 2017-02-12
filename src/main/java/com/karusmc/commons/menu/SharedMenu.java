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

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;


public class SharedMenu implements Menu {
    
    private Inventory inventory;
    private Button defaultButton;
    private Map<Integer, Button> buttons;
    
    
    public SharedMenu(Inventory inventory) {
        this(inventory, event -> {});
    }
    
    public SharedMenu(Inventory inventory, Button defaultButton) {
        this.inventory = inventory;
        this.defaultButton = defaultButton;
        buttons = new HashMap<>(inventory.getSize());
    }
    
    
    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        buttons.getOrDefault(event.getRawSlot(), defaultButton).onClick(event);
    }
    
    
    public SharedMenu bind(int slot, Button button) {
        buttons.put(slot, button);
        return this;
    }
    

    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
}
