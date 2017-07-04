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
package com.karuslabs.commons.menu.regions;

import com.karuslabs.commons.menu.*;
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
        this.buttons = buttons;
        this.permission = permission;
    }
    
    
    public boolean contains(int slot) {
        return buttons.containsKey(slot);
    }
    
    
    public void click(Menu menu, InventoryClickEvent event) {
        Button button = buttons.get(event.getRawSlot());
        if (event.getWhoClicked().hasPermission(permission) && button != null) {
            button.click(menu, event);
        }
    }
    
    public void drag(Menu menu, InventoryDragEvent event) {
        if (event.getWhoClicked().hasPermission(permission)) {
            event.getRawSlots().forEach(slot -> {
                Button button = buttons.get(slot);
                if (button != null) {
                    button.drag(menu, event);
                }
            });
        }
    }
    
    public void open(Menu menu, InventoryOpenEvent event) {
        buttons.forEach((slot, button) -> button.open(menu, event, slot));
    }

    public void close(Menu menu, InventoryCloseEvent event) {
        buttons.forEach((slot, button) -> button.close(menu, event, slot));
    }
    
    
    public Map<Integer, GenericButton> getButtons() {
        return buttons;
    }
    
    public String getPermission() {
        return permission;
    }
    
    public void setPermission(String permission) {
        this.permission = permission;
    }
    
    
    public static RegionBuilder newRegion() {
        return new RegionBuilder(new Region());
    }
    
    
    public static class RegionBuilder extends Builder<RegionBuilder, Region, Button> {

        public RegionBuilder(Region region) {
            super(region);
        }

        
        @Override
        protected RegionBuilder getThis() {
            return this;
        }

    }
    
}
