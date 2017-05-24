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

import com.karuslabs.commons.menu.*;
import com.karuslabs.commons.menu.buttons.Button;

import java.util.*;

import org.bukkit.event.inventory.*;


public class Region<GenericButton extends Button> {
    
    protected Map<Integer, GenericButton> buttons;
    protected String permission;
    
    
    public Region() {
        this(new HashMap<>(), "");
    }
    
    public Region(Map<Integer, GenericButton> buttons, String permission) {
        this.buttons = buttons;
        this.permission = permission;
    }
    
    
    public boolean contains(int slot) {
        return buttons.containsKey(slot);
    }
    
    
    public void click(Menu menu, InventoryClickEvent event) {
        Button button = buttons.get(event.getRawSlot());
        if (event.getWhoClicked().hasPermission(permission) && button != null) {
            button.click(menu, event);
        }
    }
    
    public void drag(Menu menu, InventoryDragEvent event) {
        if (event.getWhoClicked().hasPermission(permission)) {
            event.getRawSlots().forEach(slot -> {
                Button button = buttons.get(slot);
                if (button != null) {
                    button.drag(menu, event);
                }
            });
        }
    }
    
    public void open(Menu menu, InventoryOpenEvent event) {
        buttons.forEach((slot, button) -> button.open(menu, event, slot));
    }

    public void close(Menu menu, InventoryCloseEvent event) {
        buttons.forEach((slot, button) -> button.close(menu, event, slot));
    }
    
    
    public Map<Integer, GenericButton> getButtons() {
        return buttons;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    
    public static RegionBuilder newRegion() {
        return new RegionBuilder(new Region());
    }
    
    
    public static class RegionBuilder extends Builder<RegionBuilder, Region, Button> {

        public RegionBuilder(Region region) {
            super(region);
        }

        
        @Override
        protected RegionBuilder getThis() {
            return this;
        }

    }
    
}
