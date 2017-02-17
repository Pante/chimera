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
 * Processes click and drag events for a {@link Menu} slot binded to it.
 */
@FunctionalInterface
public interface Button {
    
    /**
     * Processes a InventoryClickEvent for a {@link Menu} slot binded to it.
     * 
     * @param event A InventoryClickEvent instance
     */
    public void onClick(InventoryClickEvent event);
    
    /**
     * Processes a InventoryDragEvent for a {@link Menu} slot binded to it.
     * Default implementation ignores the event.
     * 
     * @param event A InventoryDragEvent instance
     */
    public default void onDrag(InventoryDragEvent event) {}
    
}
