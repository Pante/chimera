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
package com.karuslabs.commons.menu.buttons;

import com.karuslabs.commons.menu.Menu;

import org.bukkit.event.inventory.InventoryClickEvent;


public abstract class RadioButton implements Button {
    
    private boolean selected;
    
    
    public RadioButton() {
        this(false);
    }
    
    public RadioButton(boolean selected) {
        this.selected = selected;
    }
    
    
    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        if (!selected) {
            onSelect(menu, event);
            
        } else {
            onUnselect(menu, event);
        }
    }
    
    
    public void select(Menu menu, InventoryClickEvent event) {
        if (!selected) {
            selected = onSelect(menu, event);
        }
    }
    
    protected boolean onSelect(Menu menu, InventoryClickEvent event) {
        return true;
    }
    
    
    public void unselect(Menu menu, InventoryClickEvent event) {
        if (selected) {
            selected = onUnselect(menu, event);
        }
    }
    
    protected boolean onUnselect(Menu menu, InventoryClickEvent event) {
        return false;
    }
    
    
    public boolean isSelected() {
        return selected;
    }
    
}
