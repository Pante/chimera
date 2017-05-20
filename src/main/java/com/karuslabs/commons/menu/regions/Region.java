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

import com.karuslabs.commons.menu.Menu;
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
        this.permission = permission;
    }
    
    
    public boolean contains(int slot) {
        return buttons.containsKey(slot);
    }
    
    
    public void click(Menu menu, InventoryClickEvent event) {
        GenericButton button = buttons.get(event.getRawSlot());
        if (button != null && event.getWhoClicked().hasPermission(permission)) {
            button.click(menu, event);
        }
    }
    
    public void drag(Menu menu, InventoryDragEvent event) {
        event.setCancelled(true);
    }
    
    
    public void reset(Menu menu) {
        
    }
    
    
    public Map<Integer, GenericButton> getButtons() {
        return buttons;
    }
    
    public String getPermission() {
        return permission;
    }
    
    
    public static RegionBuilder builder() {
        return new RegionBuilder(new Region());
    }
    
    
    public static class RegionBuilder<GenericRegion extends Region> extends Builder<RegionBuilder, GenericRegion, Button> {

        public RegionBuilder(GenericRegion region) {
            super(region);
        }

        @Override
        protected RegionBuilder getThis() {
            return this;
        }
        
    }
    
    
    public static abstract class Builder<GenericBuilder extends Builder, GenericRegion extends Region, GenericButton extends Button> {

        protected GenericRegion region;

        
        public Builder(GenericRegion region) {
            this.region = region;
        }

        
        public GenericBuilder permission(String permission) {
            region.permission = permission;
            return getThis();
        }

        public GenericBuilder button(int slot, Button button) {
            region.getButtons().put(slot, slot);
            return getThis();
        }

        
        public GenericRegion build() {
            return region;
        }

        protected abstract GenericBuilder getThis();

    }
    
}
