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

import org.bukkit.event.inventory.*;
    

/**
 * Represents an action to be performed when a {@link Menu} slot is clicked.
 */
public interface Button {
    
    /**
     * Button which cancels the event.
     */
    public static final Button CANCEL = (menu, event) -> event.setCancelled(true);
    
    
    /**
     * Processes the event specified.
     * 
     * @param menu a menu
     * @param event a <code>InventoryClickEvent</code>
     */
    public void onClick(Menu menu, InventoryClickEvent event);

}
