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


public class Menu<GenericInventory extends Inventory> implements InventoryHolder {
    
    public static final Consumer<HumanEntity> REMOVE = player -> MenuPool.INSTANCE.getActive().remove(player);
    
    
    protected GenericInventory inventory;
    protected Map<Integer, Button> buttons;
    
    protected Button defaultButton;
    protected Consumer<HumanEntity> closure;
    
    
    public Menu(GenericInventory inventory) {
        this(inventory, Button.CANCEL, REMOVE);
    }
    
    public Menu(GenericInventory inventory, Button defaultButton, Consumer<HumanEntity> closure) {
        this.inventory = inventory;
        buttons = new HashMap<>(inventory.getSize());
        
        this.defaultButton = defaultButton;
        this.closure = closure;
    }
    //TODO FIX DEFAULT BUTTON TRIGGERING IN PLAYER INVENTORY
    
    public void onClick(InventoryClickEvent event) {
        buttons.getOrDefault(event.getRawSlot(), defaultButton).onClick(this, event);
    }
    
    public void onDrag(InventoryDragEvent event) {
        event.getRawSlots().forEach(slot -> buttons.getOrDefault(slot, defaultButton).onDrag(this, event));
    }
    
    public void onClose(InventoryCloseEvent event) {
        closure.accept(event.getPlayer());
    }
    
    
    public Menu bind(ItemStack item, int... slots) {
        for (int slot : slots) {
            inventory.setItem(slot, item);
        }
        
        return this;
    }
    
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
