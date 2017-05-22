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
import com.karuslabs.commons.menu.buttons.Button;

import java.util.Map;

import org.bukkit.event.inventory.*;


public class BoxRegion extends Region<Button> {
    
    private Button defaultButton;
    
    private int width;
    private int min;
    private int max;
    
    private double x1, y1;
    private double x2, y2;
    
    
    public BoxRegion(Map<Integer, Button> buttons, String permission, Button defaultButton, int inventoryWidth, int minSlot, int maxSlot) {
        super(buttons, permission);
        this.defaultButton = defaultButton;
        
        this.width = inventoryWidth;
        this.min = minSlot;
        this.max = maxSlot;
        
        x1 = minSlot % inventoryWidth; 
        y1 = (double) minSlot / inventoryWidth;
        
        x2 = maxSlot % inventoryWidth;
        y2 = (double) maxSlot / inventoryWidth;
    }
    
    
    @Override
    public boolean contains(int slot) {
        int x = slot % width;
        double y = slot / (double) width;
        
        boolean containsX = x >= x1 && x <= x2;
        boolean containsY = y >= y1 && y <= y2;
        
        return containsX && containsY;
    }

    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        int slot = event.getRawSlot();
        Button button = buttons.get(slot);

        if (event.getWhoClicked().hasPermission(permission) && contains(slot)) {
            buttons.getOrDefault(slot, defaultButton).click(menu, event);
        }
    }

    
    @Override
    public void drag(Menu menu, InventoryDragEvent event) {
        if (event.getWhoClicked().hasPermission(permission)) {
            event.getRawSlots().forEach(slot -> {
                if (contains(slot)) {
                    buttons.getOrDefault(menu, defaultButton).drag(menu, event);
                }
            });
        }
    }

    
    public int getMinSlot() {
        return min;
    }
    
    public void setMinSlot(int min) {
        this.min = min;
        x1 = min % width; 
        y1 = (double) min / width;
    }
    
    
    public int getMaxSlot() {
        return max;
    }
    
    public void setMaxSlot(int max) {
        this.max = max;
        x2 = max % width;
        y2 = (double) max / width;
    }
    
}
