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
package com.karuslabs.commons.graphics.regions;

import com.karuslabs.commons.graphics.buttons.Button;

import java.util.*;


/**
 * An implementation of {@code Region} which represents a shapeless region.
 * 
 * @param <GenericButton> the button type
 */
public class Shapeless<GenericButton extends Button> extends AbstractRegion<GenericButton> {
    
    private Set<Integer> slots;
    
    
    /**
     * Constructs a {@code Shapeless} with a backing {@code HashMap} and specified slots.
     * 
     * @param slots the slots which this region contains
     */
    public Shapeless(Set<Integer> slots) {
        this(new HashMap<>(), slots);
    }
    
    /**
     * Constructs a {@code Shapeless} with the specified backing {@code HashMap} and slots.
     * 
     * @param map the backing map
     * @param slots the slots which this region contains
     */
    public Shapeless(Map<Integer, GenericButton> map, Set<Integer> slots) {
        super(map);
        this.slots = slots;
    }

    
    /**
     * {@inheritDoc} 
     */
    @Override
    public boolean contains(int slot) {
        return slots.contains(slot);
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public int size() {
        return slots.size();
    }    
    
}
