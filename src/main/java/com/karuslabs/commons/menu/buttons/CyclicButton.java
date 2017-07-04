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
