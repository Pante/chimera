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

import com.karuslabs.commons.collection.ProxiedMap;
import com.karuslabs.commons.graphics.buttons.Button;

import java.util.*;


public class Buttons<GenericButton extends Button> extends ProxiedMap<Integer, GenericButton> {
    
    private final Region region;
    
    
    public Buttons(Region region) {
        this(region, new HashMap<>());
    }
    
    public Buttons(Region region, Map<Integer, GenericButton> map) {
        super(map);
        this.region = region;
    }
    
    
    @Override
    public GenericButton put(Integer slot, GenericButton button) {
        if (region.contains(slot)) {
            return map.put(slot, button);
            
        } else {
            throw new IllegalArgumentException("Slot must be within region");
        }
    }
    
    
    public Region getRegion() {
        return region;
    }
        
}
