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
package com.karuslabs.commons.menu.buttons;

import com.google.common.collect.*;

import com.karuslabs.commons.menu.Menu;

import java.util.*;
import java.util.function.BiFunction;

import org.bukkit.event.inventory.*;


public class CyclicButton implements Button {
    
    private List<BiFunction<Menu, InventoryClickEvent, Boolean>> states;
    private PeekingIterator<BiFunction<Menu, InventoryClickEvent, Boolean>> cycle;
    private boolean reset;
    
    
    public CyclicButton(boolean reset) {
        this(new ArrayList<>(), reset);
    }
    
    public CyclicButton(List<BiFunction<Menu, InventoryClickEvent, Boolean>> states, boolean reset) {
        this.states = states;
        this.cycle = Iterators.peekingIterator(Iterators.cycle(states));
        this.reset = reset;
    }
    
    
    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        if (cycle.hasNext() && cycle.peek().apply(menu, event)) {
            cycle.next();
        }
    }

    
    @Override
    public void close(Menu menu, InventoryCloseEvent event, int slot) {
        if (reset) {
            onClose(menu, event, slot);
        }
    }
    
    protected void onClose(Menu menu, InventoryCloseEvent event, int slot) {
        cycle = Iterators.peekingIterator(Iterators.cycle(states));
    }
    
    
    public BiFunction<Menu, InventoryClickEvent, Boolean> getCurrentState() {
        return cycle.peek();
    }
    
    public List<BiFunction<Menu, InventoryClickEvent, Boolean>> getStates() {
        return states;
    }
    
    public void nextState() {
        if (cycle.hasNext()) {
            cycle.next();
        }
    }
    
    public boolean resets() {
        return reset;
    }
    
}
