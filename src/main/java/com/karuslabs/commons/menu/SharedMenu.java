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
package com.karuslabs.commons.menu;

import java.util.*;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;


/**
 * Represents a static menu that can be shared among multiple players.
 */
public class SharedMenu implements Menu {
    
    private Inventory inventory;
    private Button defaultButton;
    private Map<Integer, Button> buttons;
    
    
    /**
     * Constructs this with the specified inventory.
     * 
     * @param inventory The backing inventory
     */
    public SharedMenu(Inventory inventory) {
        this(inventory, e -> {});
    }
    
    /**
     * Constructs this with the specified inventory and default button.
     * 
     * @param inventory The backing inventory
     * @param defaultButton The button that handles events if there is no button binded to the specified slot
     */
    public SharedMenu(Inventory inventory, Button defaultButton) {
        this.inventory = inventory;
        this.defaultButton = defaultButton;
        buttons = new HashMap<>();
    }
    
    
    /**
     * Delegates event handling to a {@link Button} instance binded to the slot.
     * 
     * @param event A InventoryClickEvent instance
     */
    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        buttons.getOrDefault(event.getRawSlot(), defaultButton).onClick(event);
    }
    
    
    /**
     * Binds a button instance to the specified slot.
     * 
     * @param slot The slot to bind the button to
     * @param button The button to be binded
     * @return this
     */
    public SharedMenu bind(int slot, Button button) {
        buttons.put(slot, button);
        return this;
    }
    

    /**
     * Returns the backing Inventory instance.
     * Modifications made to the inventory directly affects the SharedMenu.
     * 
     * @return the backing Inventory
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    
    /**
     * Returns the menu's slots and buttons binded to them.
     * 
     * @return the menu's slots and binded buttons.
     */
    @Override
    public Map<Integer, Button> getButtons() {
        return buttons;
    }
    
    
    /**
     * Returns the default behavior for when no button has been binded to a slot.
     * 
     * @return the default button
     */
    public Button getDefaultButton() {
        return defaultButton;
    }
    
    /**
     * Sets the default behavior for slots for which no buttons have been binded.
     * 
     * @param defaultButton 
     */
    public void setDefaultButton(Button defaultButton) {
        this.defaultButton = defaultButton;
    }
    
}
