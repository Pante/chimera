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


public abstract class BoundLocation {
    
    protected Location location;
    protected PathVector offset;
    protected boolean relative;
    
    
    public BoundLocation(BoundLocation location) {
        this(location.location.clone(), location.offset.clone(), location.relative);
    }
    
    public BoundLocation(Location location, PathVector offset, boolean relative) {
        this.location = location;
        this.offset = offset;
        this.relative = relative;
    }
    
    
    public void addOffset(Vector offset, float yaw, float pitch) {
        this.offset.add(offset);
        this.offset.yaw(yaw);
        this.offset.pitch(pitch);
        updateOffset();
    }
        
    public void updateOffset() {
        if (relative) {
            location.add(rotate(offset, location));
            
        } else {
            location.add(offset);
        }
    }
    
    
    public abstract boolean validate();
    
    public abstract void update();
    
    
    public void setDirection(Vector direction) {
        location.setDirection(direction);
        updateDirection();
    }

    public void updateDirection() {
        if (relative) {
            location.setYaw(location.getYaw() + offset.yaw());
            location.setPitch(location.getPitch() + offset.pitch());
            
        } else {
            location.setYaw(offset.yaw());
            location.setPitch(offset.pitch());
        }
    }

    
    public Location getLocation() {
        return location;
    }
    
    public PathVector getOffset() {
        return offset;
    }
    
    public boolean isRelative() {
        return relative;
    }
    
    public void setRelative(boolean relative) {
        this.relative = relative;
    }
    
    
    public static abstract class Builder<GenericBuilder extends Builder, GenericLocation extends BoundLocation> {
        
        protected GenericLocation location;
        
        
        public Builder(GenericLocation location) {
            this.location = location;
        }
 
        
        public GenericBuilder offset(PathVector offset) {
            location.offset = offset;
            return getThis();
        }
        
        public GenericBuilder relative(boolean relative) {
            location.relative = relative;
            return getThis();
        }
        
        protected abstract GenericBuilder getThis();
        
        
        public GenericLocation build() {
            return location;
        }
        
    }
    
}
