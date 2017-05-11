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

import com.karuslabs.commons.menu.buttons.Button;
import java.util.*;

import org.bukkit.event.inventory.*;


public class Region {
    
    protected Map<Integer, Button> buttons;
    protected String permission;
    protected String message;
    
    
    public Region() {
        this(new HashMap<>(), "", "");
    }
    
    public Region(Map<Integer, Button> buttons, String permission, String message) {  
        this.buttons = buttons;
        this.permission = permission;
        this.message = message;
    }
    
    
    public boolean contains(int slot) {
        return buttons.containsKey(slot);
    }
    
    
    public void click(Menu menu, InventoryClickEvent event) {
        if (event.getWhoClicked().hasPermission(permission)) {
            buttons.getOrDefault(event.getRawSlot(), Button.NONE).click(menu, event);
            
        } else {
            onInvalidPermission(menu, event);
        }
    }
    
    protected void onInvalidPermission(Menu menu, InventoryClickEvent event) {
        event.setCancelled(true);
    }
    
    
    public void drag(Menu menu, InventoryDragEvent event) {
        if (event.getWhoClicked().hasPermission(permission)) {
            event.getRawSlots().forEach(slot -> buttons.getOrDefault(slot, Button.NONE).drag(menu, event));
            
        } else {
            onInvalidPermission(menu, event);
        } 
    }
    
    protected void onInvalidPermission(Menu menu, InventoryDragEvent event) {
        event.setCancelled(true);
    }
    
    
    public Map<Integer, Button> getButtons() {
        return buttons;
    }

    
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    
    public String getPermissionMessage() {
        return message;
    }

    public void setPermissionMessage(String message) {
        this.message = message;
    }
    
    
    public static RegionBuilder builder() {
        return new RegionBuilder(new Region());
    }
    
    
    public static class RegionBuilder<GenericRegion extends Region> {
        
        protected GenericRegion region;
        
        
        protected RegionBuilder(GenericRegion region) {
            this.region = region;
        }
        
        
        public RegionBuilder button(int slot, Button button) {
            region.buttons.put(slot, button);
            return this;
        }
        
        public RegionBuilder buttons(Button button, int... slots) {
            for (int slot : slots) {
                region.buttons.put(slot, button);
            }
            return this;
        }
        
        
        public RegionBuilder permission(String permission) {
            region.permission = permission;
            return this;
        }
        
        public RegionBuilder message(String message) {
            region.message = message;
            return this;
        }
        
    }
    
}
