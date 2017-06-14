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

import java.lang.ref.WeakReference;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;


public class EntityLocation<GenericEntity extends Entity> extends DynamicLocation {
    
    protected WeakReference<GenericEntity> reference;
    private boolean updateLocation;
    private boolean updateDirection;
    
    
    public EntityLocation(GenericEntity entity, Location location) {
        this(entity, location, true, true);
    }
    
    public EntityLocation(GenericEntity entity, Location location, boolean updateLocation, boolean updateDirection) {
        this(entity, location, true, location.toVector().subtract(entity.getLocation().toVector()), updateLocation, updateDirection);
    }
    
    public EntityLocation(GenericEntity entity, Location location, boolean relative, Vector offset, boolean updateLocation, boolean updateDirection) {
        super(location, relative, offset);
        this.reference = new WeakReference<>(entity);
        this.updateLocation = updateLocation;
        this.updateDirection = updateDirection;
    }
    
    
    @Override
    public void update() {
        Location entityLocation = getEntityLocation();
        if (entityLocation != null) {
            if (updateLocation) {
                from(entityLocation);
            }
            
            if (updateDirection) {
                location.setDirection(entityLocation.getDirection());
            }
        }
    }
        
    protected Location getEntityLocation() {
        Entity entity = reference.get();
        if (entity != null) {
            return entity.getLocation();
            
        } else {
            return null;
        }
    }
    
    
    public GenericEntity getEntity() {
        return reference.get();
    }
    
}
