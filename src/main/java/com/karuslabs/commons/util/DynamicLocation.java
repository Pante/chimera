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
package com.karuslabs.commons.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;


public class DynamicLocation {
    
    protected Location location;
    protected boolean relative;
    protected Vector offset;
    
    
    public DynamicLocation(Location location) {
        this(location, true, new Vector(0, 0, 0));
    }
    
    public DynamicLocation(Location location, boolean relative, Vector offset) {
        this.location = location;
        this.relative = relative;
        this.offset = offset;
    }
    
    
    public void update() {
        
    }
    
    public void from(Location location) {
        this.location = location;
        if (relative) {
            location.add(Vectors.rotate(location, offset));
            
        } else {
            location.add(offset);
        }
    }
    
    
    public Location getLocation() {
        return location;
    }

    
    public boolean isRelative() {
        return relative;
    }

    public void setRelative(boolean relative) {
        this.relative = relative;
    }

    
    public Vector getOffset() {
        return offset;
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
    }
    
}
