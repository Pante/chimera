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
package com.karuslabs.commons.graphics.buttons;

import com.karuslabs.commons.graphics.*;
import com.karuslabs.commons.graphics.windows.Window;

import org.bukkit.event.inventory.*;


/**
 * An implementation of {@code Button} which contains several cyclable bound states and may be reset.
 * 
 * @param <State> the state type
 */
public abstract class CyclicButton<State> extends ResettableComponent implements Button {
    
    State[] states;
    int index;

    
    /**
     * Constructs a {@code CyclicButton} which does not reset, with the specified states.
     * 
     * @param states the states
     * @throws IllegalArgumentException if no states were specified 
     */
    public CyclicButton(State... states) {
        this(false, states);
    } 
    
    /**
     * Constructs a {@code CyclicButton} with the specified reset value and states.
     * 
     * @param reset true if this button should reset; else false
     * @param states the states
     * @throws IllegalArgumentException if no states were specified
     */
    public CyclicButton(boolean reset, State... states) {
        super(reset);
        if (states.length == 0) {
            throw new IllegalArgumentException("Invalid number of states");
        }
        this.states = states;
        this.index = 0;
    }
    
    
    /**
     * Delegates the handling of the specified event to {@link #onClick(ClickEvent, Object)} and sets the next state as the current state
     * when this button is clicked.
     * 
     * @param event the event
     */
    @Override
    public void click(ClickEvent event) {
        onClick(event, states[index]);
        next();
    }
    
    /**
     * Handles the specified event when this button is clicked.
     * This method is invoked by {@link #click(ClickEvent)}.
     * The default implementation does nothing.
     * 
     * Subclasses should override this method to cusotmise the handling of the specified event.
     * 
     * @param event the event
     * @param state the current state
     */
    protected void onClick(ClickEvent event, State state) {
        
    }
    
    
    /**
     * Delegates the resetting of this button to {@link #onReset(Window, InventoryCloseEvent)} and sets the original state as the current state, 
     * or does nothing if this button should not reset.
     * 
     * @param window the window which contains this button
     * @param event the event
     */
    @Override
    public void reset(Window window, InventoryCloseEvent event) {
        if (reset) {
            onReset(window, event);
            index = 0;
        }
    }
    
    
    /**
     * Returns the current state.
     * 
     * @return the current state
     */
    public State current() {
        return states[index];
    }
    
    /**
     * Sets the next state as the current state and returns the current state.
     * 
     * @return the current state
     */
    public State next() {
        index = ++index < states.length ? index : 0;
        return states[index];
    }
    
    /**
     * Returns the index of the current state.
     * 
     * @return the index of the current state
     */
    public int index() {
        return index;
    }
    
    /**
     * Returns the total number of states.
     * 
     * @return the total number of states
     */
    public int length() {
        return states.length;
    }
    
}
