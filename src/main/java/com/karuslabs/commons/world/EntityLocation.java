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

import com.karuslabs.commons.util.Weak;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.rotateVector;


public class EntityLocation<GenericEntity extends Entity> extends BoundLocation {
    
    private Weak<GenericEntity> entity;
    private Vector entityOffset;
    private boolean nullable;
    private boolean updateLocation;
    private boolean updateDirection;

    
    public EntityLocation(GenericEntity entity, EntityLocation<GenericEntity> location) {
        super(location);
        this.entity = new Weak<>(entity);
        entityOffset = location.entityOffset.clone();
        nullable = location.nullable;
        updateDirection = location.updateDirection;
        updateLocation = location.updateLocation;
    }
    
    public EntityLocation(GenericEntity entity, Location location, Vector entityOffset, boolean nullable, Vector offset, float yaw, float pitch, boolean relative, boolean updateLocation, boolean updateDirection) {
        super(location, offset, yaw, pitch, relative);
        this.entity = new Weak<>(entity);
        this.entityOffset = entityOffset;
        this.nullable = nullable;
        this.updateLocation = updateLocation;
        this.updateDirection = updateDirection;
    }

    
    @Override
    public boolean validate() {
        return nullable || entity.isPresent();
    }
    
    @Override
    public void update() {
        entity.ifPreset(entity -> update(entity.getLocation()));
    }
    
    protected void update(Location current) {
        if (updateDirection) {
            setDirection(current.getDirection());
        }
        if (updateLocation) {
            location.setX(current.getX());
            location.setY(current.getY());
            location.setZ(current.getZ());
            updateOffset();
        }
    }
    
    @Override
    public void updateOffset() {
        location.add(entityOffset);
        if (relative) {
            location.add(rotateVector(offset, location));
            
        } else {
            location.add(offset);
        }
    }

    
    public Weak<GenericEntity> getEntity() {
        return entity;
    }

    public Vector getEntityOffset() {
        return entityOffset;
    }

    public boolean isUpdateLocation() {
        return updateLocation;
    }

    public boolean isUpdateDirection() {
        return updateDirection;
    }
    
}
