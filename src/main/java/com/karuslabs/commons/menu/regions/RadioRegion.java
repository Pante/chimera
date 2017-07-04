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

import com.karuslabs.commons.menu.Menu;
import com.karuslabs.commons.menu.buttons.RadioButton;

import java.util.*;

import org.bukkit.event.inventory.*;


public class RadioRegion extends Region<RadioButton> {
    
    private int selected;
    private boolean reset;
    
    
    public RadioRegion(int selected, boolean reset) {
        this(new HashMap<>(), "", selected, reset);
    }
    
    public RadioRegion(Map<Integer, RadioButton> buttons, String permission, int selected, boolean reset) {
        super(buttons, permission);
        this.selected = selected;
        this.reset = reset;
    }
    
    
    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        int slot = event.getRawSlot();
        RadioButton button = buttons.get(slot);
        
        if (event.getWhoClicked().hasPermission(permission) && slot != selected && button != null && button.select(menu, event)) {
            RadioButton oldSelected = buttons.get(selected);
            if (oldSelected != null) {
                oldSelected.unselect(menu, event);
            }
            
            selected = slot;
        }
    }
    
    
    @Override
    public void close(Menu menu, InventoryCloseEvent event) {
        if (reset) {
            onClose(menu, event);
        }
    } 
    
    protected void onClose(Menu menu, InventoryCloseEvent event) {
        RadioButton button = buttons.get(selected);
        if (button != null) {
            button.close(menu, event, selected);
        }
    }
    
    
    public int getSelectedSlot() {
        return selected;
    }
    
    public boolean resets() {
        return reset;
    }
    
    
    public static RadioRegionBuilder newRadionRegion() {
        return new RadioRegionBuilder(new RadioRegion(-9999, false));
    }
    
    
    public static class RadioRegionBuilder extends Builder<RadioRegionBuilder, RadioRegion, RadioButton> {

        public RadioRegionBuilder(RadioRegion region) {
            super(region);
        }
        
        
        public RadioRegionBuilder selected(int selected) {
            region.selected = selected;
            return this;
        }
        
        public RadioRegionBuilder reset(boolean reset) {
            region.reset = reset;
            return this;
        }
        

        @Override
        protected RadioRegionBuilder getThis() {
            return this;
        }

    }
    
}
