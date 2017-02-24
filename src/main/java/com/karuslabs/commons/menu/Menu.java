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
import java.util.function.Consumer;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;


/**
 * Represents a graphical item menu.
 * 
 * @param <GenericInventory> A inventory type
 */
public class Menu<GenericInventory extends Inventory> implements InventoryHolder {
    
    /**
     * Consumer which unbinds the menu from the player in {@link MenuPool} when the menu is closed.
     */
    public static final Consumer<HumanEntity> REMOVE = player -> MenuPool.INSTANCE.getActive().remove(player);
    
    
    private GenericInventory inventory;
    private Map<Integer, Button> buttons;
    
    private Button defaultButton;
    private Consumer<HumanEntity> closure;
    
    
    /**
     * Constructs this with the specified inventory, {@link #REMOVE} and {{@link Button#CANCEL}.
     * 
     * @param inventory An inventory
     */
    public Menu(GenericInventory inventory) {
        this(inventory, Button.CANCEL, REMOVE);
    }
    
    /**
     * Constructs this with the specified inventory, default button and consumer.
     * 
     * @param inventory An inventory
     * @param defaultButton A button
     * @param closure The consumer which is called when {@link #onClose(org.bukkit.event.inventory.InventoryCloseEvent)} is called.
     */
    public Menu(GenericInventory inventory, Button defaultButton, Consumer<HumanEntity> closure) {
        this.inventory = inventory;
        buttons = new HashMap<>(inventory.getSize());
        
        this.defaultButton = defaultButton;
        this.closure = closure;
    }
    
    
    /**
     * Delegates event handling to the binded button or the default if no button is binded to the slot.
     * 
     * @param event An InventoryClickEvent
     */
    public void onClick(InventoryClickEvent event) {
        buttons.getOrDefault(event.getRawSlot(), defaultButton).onClick(this, event);
    }
    
    /**
     * Delegates event handling to the binded button or the default if no button is binded to the slot.
     * 
     * @param event An InventoryDragEvent
     */
    public void onDrag(InventoryDragEvent event) {
        event.getRawSlots().forEach(slot -> buttons.getOrDefault(slot, defaultButton).onDrag(this, event));
    }
    
    /**
     * Delegates event handling to the binded button or the default if no button is binded to the slot.
     * 
     * @param event An InventoryClosEvent
     */
    public void onClose(InventoryCloseEvent event) {
        closure.accept(event.getPlayer());
    }
    
    
    /**
     * Binds the item(s) to the specified slots.
     * 
     * @param item The item(s) to bind
     * @param slots The slots to bind the specified item(s) to
     * @return this
     */
    public Menu bind(ItemStack item, int... slots) {
        for (int slot : slots) {
            inventory.setItem(slot, item);
        }
        
        return this;
    }
    
    /**
     * Binds the button(s) to the specified slots.
     * 
     * @param button The button(s) to bind
     * @param slots The slots to bind the specified button(s) to
     * @return this
     */
    public Menu bind(Button button, int... slots) {
        for (int slot : slots) {
            buttons.put(slot, button);
        }
        
        return this;
    }
     
        
    @Override
    public GenericInventory getInventory() {
        return inventory;
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
    
    
    public Consumer<HumanEntity> getClosure() {
        return closure;
    }
    
    public void setClosure(Consumer<HumanEntity> closure) {
        this.closure = closure;
    }
    
}
