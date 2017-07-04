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

import com.karuslabs.commons.menu.regions.Region;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;


public class Menu implements InventoryHolder {
    
    protected Inventory inventory;
    protected List<Region> regions;
    
    
    public Menu(Inventory inventory) {
        this(inventory, new ArrayList<>());
    }
    
    public Menu(Inventory inventory, List<Region> regions) {
        this.inventory = inventory;
        this.regions = regions;
    }
    
    
    public void click(InventoryClickEvent event) {
        regions.forEach(region -> region.click(this, event));
    }
    
    public void drag(InventoryDragEvent event) {
        regions.forEach(region -> region.drag(this, event));
    }
    
    
    public void open(InventoryOpenEvent event) {
        regions.forEach(region -> region.open(this, event));
    }
    
    public void close(InventoryCloseEvent event) {
        regions.forEach(region -> region.close(this, event));
    }
            
    
    public String getTitle() {
        return inventory.getTitle();
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    public List<Region> getRegions() {
        return regions;
    }
    
    
    public static MenuBuilder newMenu() {
        return new MenuBuilder(new Menu(Bukkit.createInventory(null, 54)));
    }
    
    
    public static class MenuBuilder<GenericBuilder extends MenuBuilder, GenericMenu extends Menu> {
        
        private GenericMenu menu;
        
        
        public MenuBuilder(GenericMenu menu) {
            this.menu = menu;
        }
        
        
        public MenuBuilder inventory(Inventory inventory) {
            menu.inventory = inventory;
            return this;
        }
        
        public MenuBuilder region(Region region) {
            menu.regions.add(region);
            return this;
        }
        
        
        public GenericMenu build() {
            return menu;
        }
        
    }
    
}
