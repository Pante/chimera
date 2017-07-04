/* 
 * The MIT License
 *
 * Copyright 2017 Karus Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
