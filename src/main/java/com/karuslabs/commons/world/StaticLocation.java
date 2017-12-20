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


/**
 * A subclass of {@code BoundLocation} which is static and may not be dynamically updated.
 */
public class StaticLocation extends BoundLocation {
    
    /**
     * Constructs a {@code StaticLocation} which copies the specified location.
     * 
     * @param location the location
     */
    public StaticLocation(StaticLocation location) {
        super(location);
    }
    
    /**
     * Constructs a {@code StaticLocation} with the specified location, offset and offset relativity.
     * 
     * @param location the location
     * @param offset the offset
     * @param relative true if the offset is relative to the direction of the location
     */
    public StaticLocation(Location location, Position offset, boolean relative) {
        super(location, offset, relative);
    }
    
    
    /**
     * Returns {@code true}.
     * 
     * @return true
     */
    @Override
    public boolean validate() {
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {}
    
    
    /**
     * Creates a {@code StaticLocation} builder with the specified location.
     * 
     * @param location the location
     * @return the builder
     */
    public static StaticBuilder builder(Location location) {
        return new StaticBuilder(new StaticLocation(location, new Position(), false));
    }
    
    /**
     * Represents a builder for {@code StaticLocation}s.
     */
    public static class StaticBuilder extends Builder<StaticBuilder, StaticLocation> {

        private StaticBuilder(StaticLocation location) {
            super(location);
        }

        /**
         * {@inheritDoc} 
         */
        @Override
        protected StaticBuilder getThis() {
            return this;
        }
    
    }
    
}
