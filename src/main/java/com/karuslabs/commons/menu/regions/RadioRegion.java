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
import com.karuslabs.commons.menu.buttons.RadioButton;

import java.util.*;

import org.bukkit.event.inventory.*;


public class RadioRegion extends Region<RadioButton> {
    
    private int selected;
    private boolean reset;
    
    
    public RadioRegion(int selected, boolean reset) {
        this(new HashMap<>(), "", selected, reset);
    }
    
    public RadioRegion(Map<Integer, RadioButton> buttons, String permission, int selected, boolean reset) {
        super(buttons, permission);
        this.selected = selected;
        this.reset = reset;
    }
    
    
    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        int slot = event.getRawSlot();
        RadioButton button = buttons.get(slot);
        
        if (event.getWhoClicked().hasPermission(permission) && slot != selected && button != null && button.select(menu, event)) {
            RadioButton oldSelected = buttons.get(selected);
            if (oldSelected != null) {
                oldSelected.unselect(menu, event);
            }
            
            selected = slot;
        }
    }
    
    
    @Override
    public void close(Menu menu, InventoryCloseEvent event) {
        if (reset) {
            onClose(menu, event);
        }
    } 
    
    protected void onClose(Menu menu, InventoryCloseEvent event) {
        RadioButton button = buttons.get(selected);
        if (button != null) {
            button.close(menu, event, selected);
        }
    }
    
    
    public int getSelectedSlot() {
        return selected;
    }
    
    public boolean resets() {
        return reset;
    }
    
    
    public static RadioRegionBuilder newRadionRegion() {
        return new RadioRegionBuilder(new RadioRegion(-9999, false));
    }
    
    
    public static class RadioRegionBuilder extends Builder<RadioRegionBuilder, RadioRegion, RadioButton> {

        public RadioRegionBuilder(RadioRegion region) {
            super(region);
        }
        
        
        public RadioRegionBuilder selected(int selected) {
            region.selected = selected;
            return this;
        }
        
        public RadioRegionBuilder reset(boolean reset) {
            region.reset = reset;
            return this;
        }
        

        @Override
        protected RadioRegionBuilder getThis() {
            return this;
        }

    }
    
}
