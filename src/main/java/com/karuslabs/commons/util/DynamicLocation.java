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
    
    protected Location origin;
    protected Location current;
    
    protected boolean relative;
    protected Vector offset;
    protected Vector direction;
    
    
    public DynamicLocation(Location origin, boolean relative, Vector offset, Vector direction) {
        this.origin = origin;
        this.current = origin.clone();
        
        relative = true;
        this.offset = offset;
        this.direction = direction;
    }
    
    
    public void update() {
        
    }
    
    
    public void from(Location location) {
        origin = location;
        current = location.clone();
        
        Vector aOffset = offset;
        if (relative) {
            aOffset = Vectors.rotate(location, offset);
        }
        
        current.add(aOffset);
    }
    
    
    
    public Location getOrigin() {
        return origin;
    }
    
    public Location getCurrent() {
        return current;
    }
    
}
