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


/**
 * A concrete subclass of {@code BoundLocation} which is bound to an entity and may be dynamically updated.
 * 
 * @param <GenericEntity> the subclass of Entity
 */
public class EntityLocation<GenericEntity extends Entity> extends BoundLocation {
    
    Weak<GenericEntity> entity;
    Location current;
    boolean nullable;
    boolean update;
    
    
    /**
     * Constructs an {@code EntityLocation} for the specified {@code Entity}, which
     * copies the specified location.
     * 
     * @param entity the entity
     * @param location the location
     */
    public EntityLocation(GenericEntity entity, EntityLocation<GenericEntity> location) {
        super(location);
        this.entity = new Weak<>(entity);
        this.current = new Location(null, 0, 0, 0);
        nullable = location.nullable;
        update = location.update;
    }
    
    /**
     * Constructs an {@code EntityLocation} with the specified entity, offset, offset relativity, entity nullablity and whether to update
     * the location, using the location of the entity.
     * 
     * @param entity the entity
     * @param offset the offset
     * @param relative true if the offset is relative to the direction of this location; else false
     * @param nullable true if the entity may be null; else false
     * @param update true if the location should be updated to reflect the current location of the entity; else false
     */
    public EntityLocation(GenericEntity entity, Position offset, boolean relative, boolean nullable, boolean update) {
        this(entity, entity.getLocation(), relative, nullable, update, offset);
    }
    
    /**
     * Constructs an {@code EntityLocation} with the specified entity, location, offset, offset relativity, entity nullability and
     * whteher to update to the location. Adds the distance between the entity and location to the specified offset.
     * 
     * @param entity the entity
     * @param location the location
     * @param offset the offset
     * @param relative true if the offset is relative to the direction of this location; else false
     * @param nullable true if the entity may be null; else false
     * @param update true if the location should be updated to reflect the current location of the entity; else false
     */
    public EntityLocation(GenericEntity entity, Location location, Position offset, boolean relative, boolean nullable, boolean update) {
        this(entity, location, relative, nullable, update, offset.add(location.toVector().subtract(entity.getLocation().toVector())));
    }
    
    /**
     * Constructs an {@code EntityLocation} with the specifeid entity, location, offset relativity, offset nullability, whether to update the location
     * and the offset.
     * 
     * @param entity the entity
     * @param location the location
     * @param relative true if the offset is relative to the direction of this location; else false
     * @param nullable true if the entity may be null; else false
     * @param update true if the location should be updated to reflect the current location of the entity; else false
     * @param offset the offset
     */
    protected EntityLocation(GenericEntity entity, Location location, boolean relative, boolean nullable, boolean update, Position offset) {
        super(location, offset, relative);
        this.entity = new Weak<>(entity);
        this.current = new Location(null, 0, 0, 0);
        this.nullable = nullable;
        this.update = update;
    }

    
    /**
     * Returns {@code true}, or {@code false} if the entity may not be {@code null} and the entity
     * is not present.
     * 
     * @return true if the entity may be null or the entity is present; else false
     */
    @Override
    public boolean validate() {
        return nullable || entity.isPresent();
    }
    
    /**
     * Updates this {@code EntityLocation} if {@link #canUpdate()} is {@code true} and the entity 
     * is present.
     */
    @Override
    public void update() {
        entity.ifPreset(entity -> update(entity.getLocation(current)));
    }
    
    /**
     * Updates this {@code EntityLocation} using the specified current location.
     * 
     * @param current the current location
     */
    protected void update(Location current) {
        if (update) {
            setDirection(current.getDirection());
            location.setWorld(current.getWorld());
            location.setX(current.getX());
            location.setY(current.getY());
            location.setZ(current.getZ());
            updateOffset();
        }
    }

    
    /**
     * Returns the entity.
     * 
     * @return the entity
     */
    public Weak<GenericEntity> getEntity() {
        return entity;
    }

    /**
     * Returns {@code true}, or {@code false} if the location will not update.
     * 
     * @return true if the location will update; else false
     */
    public boolean canUpdate() {
        return update;
    }
    
    
    /**
     * Creates an {@code EntityLocation} builder for the specified entity.
     * 
     * @param <GenericEntity> the subclass of Entity
     * @param entity the entity to which the location is bound
     * @return the builder
     */
    public static<GenericEntity extends Entity> EntityBuilder<GenericEntity> builder(GenericEntity entity) {
        return new EntityBuilder<>(new EntityLocation<>(entity, new Position(), false, false, false));
    }
    
    /**
     * Creates an {@code EntityLocation} builder for the specified entity and location.
     * 
     * @param <GenericEntity> the subclass of Entity
     * @param entity the entity to which the location is bound
     * @param location the location
     * @return the builder
     */
    public static<GenericEntity extends Entity> EntityBuilder<GenericEntity> builder(GenericEntity entity, Location location) {
        return new EntityBuilder<>(new EntityLocation<>(entity, location, new Position(), false, false, false));
    }
    
    /**
     * Represents a builder for {@code EntityLocation}s.
     * 
     * @param <GenericEntity> the subclass of entity
     */
    public static class EntityBuilder<GenericEntity extends Entity> extends AbstractBuilder<EntityBuilder<GenericEntity>, EntityLocation<?>> {

        private EntityBuilder(EntityLocation<GenericEntity> location) {
            super(location);
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        protected EntityBuilder<GenericEntity> getThis() {
            return this;
        }
        
        /**
         * Builds the {@code EntityLocation}.
         * 
         * @return the EntityLocation
         */
        @Override
        public EntityLocation<GenericEntity> build() {
            return (EntityLocation<GenericEntity>) location;
        }
        
    }
    
    static abstract class AbstractBuilder<GenericBuilder extends AbstractBuilder, GenericLocation extends EntityLocation<?>> extends Builder<GenericBuilder, GenericLocation> {
        
        AbstractBuilder(GenericLocation location) {
            super(location);
        }
        
        /**
         * Sets the entity nullability.
         * 
         * @param nullable true if the entity may be null; else false
         * @return this
         */
        public GenericBuilder nullable(boolean nullable) {
            location.nullable = nullable;
            return getThis();
        }
        
        /**
         * Sets whether the {@code EntityLocation} will update.
         * 
         * @param update true if the EntityLocation will update; else false
         * @return this
         */
        public GenericBuilder update(boolean update) {
            location.update = update;
            return getThis();
        }
        
    }

}
