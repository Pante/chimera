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
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;


public class LivingEntityLocation<GenericEntity extends LivingEntity> extends EntityLocation<GenericEntity> {
    
    public LivingEntityLocation(GenericEntity entity, Location location) {
        this(entity, location, true, true);
    }
    
    public LivingEntityLocation(GenericEntity entity, Location location, boolean updateLocation, boolean updateDirection) {
        this(entity, location, true, location.toVector().subtract(entity.getEyeLocation().toVector()), updateLocation, updateDirection);
    }
    
    public LivingEntityLocation(GenericEntity entity, Location location, boolean relative, Vector offset, boolean updateLocation, boolean updateDirection) {
        super(entity, location, relative, offset, updateLocation, updateDirection);
    }
    
    
    @Override
    protected Location getEntityLocation() {
        LivingEntity entity = reference.get();
        if (entity != null) {
            return entity.getEyeLocation();
            
        } else {
            return null;
        }
    }
    
}
