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
package com.karuslabs.commons.menu.regions;

import com.karuslabs.commons.menu.Button;

import java.util.*;
import java.util.function.Consumer;

import org.bukkit.inventory.*;


/**
 * Represents a region in an in-game menu.
 * @see com.karuslabs.commons.menu.Menu
 */
public abstract class Region implements InventoryHolder {
    
    protected Inventory inventory;
    protected Map<Integer, Button> buttons;
    protected Button defaultButton;
    
    
    /**
     * Creates a new region with the backing inventory specified.
     * 
     * @param inventory the inventory
     */
    public Region(Inventory inventory) {
        this(inventory, Button.CANCEL);
    }
    
    /**
     * Creates a new region with the backing inventory and default button specified.
     * 
     * @param inventory the inventory
     * @param defaultButton the default button
     */
    public Region(Inventory inventory, Button defaultButton) {
        this.inventory = inventory;
        buttons = new HashMap<>();
        this.defaultButton = defaultButton;
    }
    
    
    /**
     * Determines if the slot specified is within the region.
     * 
     * @param slot the slot
     * @return <code>true</code>, if within the region; else <code>false</code>
     */
    public abstract boolean within(int slot);
    
    /**
     * Binds an <code>ItemStack</code> and button to the slots specified.
     * 
     * @param item the <code>ItemStack</code>
     * @param button the button
     * @param slots the slots
     * @return <code>this</code>
     */
    public Region bind(ItemStack item, Button button, int... slots) {
        return bind(slot -> {inventory.setItem(slot, item); buttons.put(slot, button);}, slots);
    }
    
    /**
     * Binds an <code>ItemStack</code> to the slots specified.
     * 
     * @param item the <code>ItemStack</code>
     * @param slots the slots
     * @return <code>this</code>
     */
    public Region bind(ItemStack item, int... slots) {
        return bind(slot -> inventory.setItem(slot, item), slots);
    }
    
    /**
     * Binds a button to the slots specified.
     * 
     * @param button the button
     * @param slots the slots
     * @return <code>this</code>
     */
    public Region bind(Button button, int... slots) {
        return bind(slot -> buttons.put(slot, button), slots);
    }
 
    
    /**
     * Executes the block for each slot specified.
     * 
     * @param binder the block to be executed for each slot specified
     * @param slots the slots
     * @return <code>this</code>
     * @throws IllegalArgumentException if the slot is not within the region
     */
    protected Region bind(Consumer<Integer> binder, int... slots) {
        for (int slot : slots) {
            if (within(slot)) {
                binder.accept(slot);
            } else {
                throw new IllegalArgumentException("Slot is out of bound: " + slot);
            }
        }
        return this;
    }
    
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }      
    
    
    /**
     * Returns the button associated with the slot specified, if present; else the default button.
     * 
     * @param slot the slot
     * @return the button associated with the slot specified, if present; else the default button
     */
    public Button getButtonOrDefault(int slot) {
        return buttons.getOrDefault(slot, defaultButton);
    }
    
    public Map<Integer, Button> getButtons() {
        return buttons;
    }
    

    public Button getDefaultButton() {
        return defaultButton;
    }

    public void setDefaultButton(Button defaultButton) {
        this.defaultButton = defaultButton;
    }
    
}
