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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;


public class MenuManager implements Listener {
    
    private ConcurrentHashMap<String, Menu> pool;
    private WeakHashMap<HumanEntity, Menu> active;
    
    
    public MenuManager() {
        pool = new ConcurrentHashMap<>();
        active = new WeakHashMap<>();
    }
    
    
    public void setActiveFromPool(Player player, String key) {
        Menu menu = pool.get(key);
        if (menu != null) {
            active.put(player, menu);
            
        } else {
            throw new IllegalArgumentException("No such pooled menu with key: " + key);
        }
    }
    
    
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Menu menu = active.get(event.getWhoClicked());
        if (menu != null) {
            menu.click(event);
        }
    }
    
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Menu menu = active.get(event.getWhoClicked());
        if (menu != null) {
            menu.drag(event);
        }
    }
   
    
    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Menu menu = active.get(event.getPlayer());
        if (menu != null) {
            menu.open(event);
        }
    }
    
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Menu menu = active.get(event.getPlayer());
        if (menu != null) {
            menu.close(event);
        }
    }
    
    
    public ConcurrentHashMap<String, Menu> getPool() {
        return pool;
    }

    public WeakHashMap<HumanEntity, Menu> getActive() {
        return active;
    }
    
}
