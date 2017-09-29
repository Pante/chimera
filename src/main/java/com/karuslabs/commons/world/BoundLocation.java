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


public abstract class BoundLocation {
    
    protected Location location;
    protected Vector offset;
    protected float yaw;
    protected float pitch;
    protected boolean offsetRelative;
    
    
    public BoundLocation(Location location, Vector offset, float offsetYaw, float offsetPitch, boolean offsetRelative) {
        this.location = location;
        this.offset = offset;
        this.yaw = offsetYaw;
        this.pitch = offsetPitch;
        this.offsetRelative = offsetRelative;
    }
    
    
    public void addOffset(Vector offset) {
        this.offset.add(offset);
        updateOffset();
    }
    
    
    public abstract boolean validate();
    
    public abstract void update();
    
    public abstract void updateOffset();
    
    
    public void setDirection(Vector direction) {
        location.setDirection(direction);
        updateDirection();
    }

    public void updateDirection() {
        if (offsetRelative) {
            location.setYaw(location.getYaw() + yaw);
            location.setPitch(location.getPitch() + pitch);
            
        } else {
            location.setYaw(yaw);
            location.setPitch(pitch);
        }
    }

    
    public Location getLocation() {
        return location;
    }
    
    public Vector getOffset() {
        return offset;
    }
    
    public boolean isOffsetRelative() {
        return offsetRelative;
    }
    
    public void setOffsetRelative(boolean offsetRelative) {
        this.offsetRelative = offsetRelative;
    }
    
}
