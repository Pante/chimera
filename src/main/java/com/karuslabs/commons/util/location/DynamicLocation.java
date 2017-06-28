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
package com.karuslabs.commons.util.location;

import org.bukkit.Location;
import org.bukkit.util.Vector;


public class DynamicLocation {
    
    protected Location location;
    
    protected Vector offset;
    protected float offsetYaw;
    protected float offsetPitch;
    protected boolean relative;
    
    
    public DynamicLocation(Location location) {
        this(location, new Vector(0, 0, 0), 0, 0, false);
    }
    
    public DynamicLocation(Location location, Vector offset, float offsetYaw, float offsetPitch, boolean relative) {
        this.location = location;
        this.offset = offset;
        this.offsetYaw = offsetYaw;
        this.offsetPitch = offsetPitch;
        this.relative = relative;
    }
    
    
    public void update() {
        
    }
    
    public void from(Location location) {
        this.location.setWorld(location.getWorld());
        this.location.setX(location.getX());
        this.location.setY(location.getY());
        this.location.setZ(location.getZ());
        updateOffsets();
    }
    
    public void updateOffsets() {
        if (relative) {
            location.add(Vectors.rotate(location, offset));
            //TODO offset
            
        } else {
            location.add(offset);
            location.setYaw(location.getYaw() + offsetYaw);
            location.setPitch(location.getPitch() + offsetPitch);
        }
    }
    
    
    public Location getLocation() {
        return location;
    }

    
    public Vector getOffset() {
        return offset;
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
    }

    public float getOffsetYaw() {
        return offsetYaw;
    }

    public void setOffsetYaw(float offsetYaw) {
        this.offsetYaw = offsetYaw;
    }

    public float getOffsetPitch() {
        return offsetPitch;
    }

    public void setOffsetPitch(float offsetPitch) {
        this.offsetPitch = offsetPitch;
    }

    public boolean isRelative() {
        return relative;
    }

    public void setRelative(boolean relative) {
        this.relative = relative;
    }
    
}
