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


public class EntityLocation<GenericEntity extends Entity> extends BoundLocation {
    
    Weak<GenericEntity> entity;
    Location current;
    boolean nullable;
    boolean update;
    
    
    public EntityLocation(GenericEntity entity, EntityLocation<GenericEntity> location) {
        super(location);
        this.entity = new Weak<>(entity);
        this.current = new Location(null, 0, 0, 0);
        nullable = location.nullable;
        update = location.update;
    }
    
    public EntityLocation(GenericEntity entity, Position offset, boolean relative, boolean nullable, boolean update) {
        this(new Weak<>(entity), entity.getLocation(), offset, relative, nullable, update);
    }
    
    public EntityLocation(GenericEntity entity, Location location, Position offset, boolean relative, boolean nullable, boolean update) {
        this(new Weak<>(entity), location, offset.add(location.toVector().subtract(entity.getLocation().toVector())), relative, nullable, update);
    }
    
    protected EntityLocation(Weak<GenericEntity> entity, Location location, Position offset, boolean relative, boolean nullable, boolean update) {
        super(location, offset, relative);
        this.entity = entity;
        this.current = new Location(null, 0, 0, 0);
        this.nullable = nullable;
        this.update = update;
    }

    
    @Override
    public boolean validate() {
        return nullable || entity.isPresent();
    }
    
    @Override
    public void update() {
        entity.ifPreset(entity -> update(entity.getLocation(current)));
    }
    
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

    
    public Weak<GenericEntity> getEntity() {
        return entity;
    }

    public boolean canUpdate() {
        return update;
    }
    
    
    public static<GenericEntity extends Entity> EntityBuilder<GenericEntity> builder(GenericEntity entity) {
        return new EntityBuilder<>(new EntityLocation<>(entity, new Position(), false, false, false));
    }
    
    public static<GenericEntity extends Entity> EntityBuilder<GenericEntity> builder(GenericEntity entity, Location location) {
        return new EntityBuilder<>(new EntityLocation<>(entity, location, new Position(), false, false, false));
    }
    
    public static class EntityBuilder<GenericEntity extends Entity> extends AbstractBuilder<EntityBuilder<GenericEntity>, EntityLocation<?>> {

        private EntityBuilder(EntityLocation<GenericEntity> location) {
            super(location);
        }

        @Override
        protected EntityBuilder<GenericEntity> getThis() {
            return this;
        }
        
        @Override
        public EntityLocation<GenericEntity> build() {
            return (EntityLocation<GenericEntity>) location;
        }
        
    }
    
    static abstract class AbstractBuilder<GenericBuilder extends AbstractBuilder, GenericLocation extends EntityLocation<?>> extends Builder<GenericBuilder, GenericLocation> {
        
        public AbstractBuilder(GenericLocation location) {
            super(location);
        }
        
        public GenericBuilder nullable(boolean nullable) {
            location.nullable = nullable;
            return getThis();
        }
        
        public GenericBuilder update(boolean update) {
            location.update = update;
            return getThis();
        }
        
    }

}
