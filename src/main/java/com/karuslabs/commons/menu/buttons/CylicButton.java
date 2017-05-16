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

import org.bukkit.event.inventory.InventoryClickEvent;


public abstract class CylicButton implements Button {
   
    public static interface State {
        
        public boolean click(Menu menu, InventoryClickEvent event);
        
    }
    
    
    private List<State> states;
    private PeekingIterator<State> cycle;
    
        
    public CylicButton() {
        this(new ArrayList<>());
    }
    
    public CylicButton(List<State> states) {
        this.states = states;
        cycle = Iterators.peekingIterator(Iterators.cycle(states));
    }
    
    
    @Override
    public void click(Menu menu, InventoryClickEvent event) {
        if (cycle.hasNext() && cycle.peek().click(menu, event)) {
            cycle.next();
        }
    }
    
    
    public List<State> getStates() {
        return states;
    }
    
    public State getCurrentState() {
        return cycle.peek();
    }
    
}
