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
package com.karuslabs.commons.world;

import org.bukkit.Location;


public class StaticLocation extends BoundLocation {
    
    public StaticLocation(StaticLocation location) {
        super(location);
    }
    
    public StaticLocation(Location location, Position offset, boolean relative) {
        super(location, offset, relative);
    }
    
    
    @Override
    public boolean validate() {
        return true;
    }
    
    @Override
    public void update() {}
    
    
    public static StaticBuilder builder(Location location) {
        return new StaticBuilder(new StaticLocation(location, new Position(), false));
    }
    
    public static class StaticBuilder extends Builder<StaticBuilder, StaticLocation> {

        private StaticBuilder(StaticLocation location) {
            super(location);
        }

        @Override
        protected StaticBuilder getThis() {
            return this;
        }
    
    }
    
}
