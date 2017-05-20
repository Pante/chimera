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

import org.bukkit.event.inventory.InventoryClickEvent;


public class RadioRegion extends Region<RadioButton> {
    
    protected int selected;
    protected int selectedByDefault;
    
    
    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        RadioButton button = buttons.get(event.getRawSlot());
        if (button != null && event.getWhoClicked().hasPermission(permission) && event.getRawSlot() != selected) {
            if (button.select(menu, event)) {
                selected = event.getRawSlot();
                buttons.forEach((slot, b) -> {
                    if (selected != slot) {
                        button.unselect(menu, event);
                    }
                });
            }
        }
    }
    
    
    @Override
    public void reset(Menu menu) {
        selected = selectedByDefault;
    }
    
}
