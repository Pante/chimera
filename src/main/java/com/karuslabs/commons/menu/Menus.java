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

import com.google.common.collect.Lists;

import com.karuslabs.commons.menu.regions.Region;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;


public class Menus {
    
    public static Menu singleRegionMenu(InventoryType type) {
        return new Menu(Bukkit.createInventory(null, type), Lists.newArrayList(new Region()));
    }
    
    public static Menu singleRegionMenu(String title, InventoryType type) {
        return new Menu(Bukkit.createInventory(null, type, title), Lists.newArrayList(new Region()));
    }
    
    public static Menu singleRegionMenu(int size) {
        return new Menu(Bukkit.createInventory(null, size), Lists.newArrayList(new Region()));
    }
    
    public static Menu singleRegionMenu(String title, int size) {
        return new Menu(Bukkit.createInventory(null, size, title), Lists.newArrayList(new Region()));
    }
    
}
