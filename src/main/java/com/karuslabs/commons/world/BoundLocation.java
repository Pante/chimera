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
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.rotate;


/**
 * Represents a location which may be dynamically updated.
 */
public abstract class BoundLocation {
    
    /**
     * The {@code Location} which is updated.
     */
    protected Location location;
    /**
     * The offset which is applied when this {@code BoundLocation} is updated.
     */
    protected Position offset;
    /**
     * The relativity of the offset to the direction of this {@code BoundLocation}.
     */
    protected boolean relative;
    
    
    /**
     * Constructs a {@code BoundLocation} which copies the specified {@code BoundLocation}.
     * 
     * @param location the location
     */
    public BoundLocation(BoundLocation location) {
        this(location.location.clone(), location.offset.clone(), location.relative);
    }
    
    /**
     * Constructs a {@code BoundLocation} with the specified {@code Location}, offset and offset relativity.
     * 
     * @param location the location
     * @param offset the offset
     * @param relative true if the offset is relative to the direction of the location; else false
     */
    public BoundLocation(Location location, Position offset, boolean relative) {
        this.location = location;
        this.offset = offset;
        this.relative = relative;
    }
    
    
    /**
     * Adds the specified offset, yaw and pitch to this {@code BoundLocation}.
     * 
     * @param offset the offset
     * @param yaw the yaw
     * @param pitch the pitch
     */
    public void addOffset(Vector offset, float yaw, float pitch) {
        this.offset.add(offset);
        this.offset.setYaw(this.offset.getYaw() + yaw);
        this.offset.setPitch(this.offset.getPitch() + pitch);
        updateOffset();
    }
    
    /**
     * Updates the offset.
     */
    public void updateOffset() {
        if (relative) {
            location.add(rotate(offset, location));
            
        } else {
            location.add(offset);
        }
    }
    
    
    /**
     * Returns {@code true}, or {@code false} if this {@code BoundLocation} is not valid.
     * 
     * @return true if this BoundLocation is valid; else false
     */
    public abstract boolean validate();
    
    /**
     * Updates the location.
     */
    public abstract void update();
    
    
    /**
     * Sets the direction .
     * 
     * @param direction the direction
     */
    public void setDirection(Vector direction) {
        location.setDirection(direction);
        updateDirection();
    }
    
    /**
     * Updates the direction for this {@code BoundLocation}.
     */
    public void updateDirection() {
        if (relative) {
            location.setYaw(location.getYaw() + offset.getYaw());
            location.setPitch(location.getPitch() + offset.getPitch());
            
        } else {
            location.setYaw(offset.getYaw());
            location.setPitch(offset.getPitch());
        }
    }

    
    /**
     * Returns the current {@code Location}.
     * 
     * @return the location
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Returns the offset.
     * 
     * @return the offset
     */
    public Position getOffset() {
        return offset;
    }
    
    /**
     * Returns {@code true}, or {@code false} if the offset is not relative to the direction of this location.
     * 
     * @return true if the offset is relative to the direction of this location; else false
     */
    public boolean isRelative() {
        return relative;
    }
    
    /**
     * Sets whether the offset is relative to the direction of this location.
     * 
     * @param relative true if the offset is relative to the direction of this location; else false
     */
    public void setRelative(boolean relative) {
        this.relative = relative;
    }
    
    
    /**
     * Represents a builder for {@code BoundLocation}s.
     * 
     * @param <GenericBuilder> the subclass of Builder to return
     * @param <GenericLocation> the subclass of BoundLocation to build
     */
    public static abstract class Builder<GenericBuilder extends Builder, GenericLocation extends BoundLocation> {
        
        /**
         * The {@code GenericLocation} to build.
         */
        protected GenericLocation location;
        
        
        /**
         * Constructs a {@code Builder} for the specified {@code GenericLocation}.
         * 
         * @param location the location
         */
        public Builder(GenericLocation location) {
            this.location = location;
        }
 
        
        /**
         * Sets the offset.
         * 
         * @param offset the offset
         * @return this
         */
        public GenericBuilder offset(Position offset) {
            location.offset = offset;
            return getThis();
        }
        
        /**
         * Sets whether the offset is relative to the direction of the location.
         * 
         * @param relative true if the offset is relative to the direction of the location; else false
         * @return this
         */
        public GenericBuilder relative(boolean relative) {
            location.relative = relative;
            return getThis();
        }
        
        /**
         * Returns a generic version of {@code this}.
         *
         * @return this
         */
        protected abstract GenericBuilder getThis();
        
        
        /**
         * Builds the {@code GenericLocation}.
         * 
         * @return the location
         */
        public GenericLocation build() {
            return location;
        }
        
    }
    
}
