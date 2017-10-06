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
import org.bukkit.entity.LivingEntity;


public class LivingEntityLocation<GenericEntity extends LivingEntity> extends EntityLocation<GenericEntity> {
    
    public LivingEntityLocation(GenericEntity entity, LivingEntityLocation<GenericEntity> location) {
        super(entity, location);
    }
    
    public LivingEntityLocation(GenericEntity entity, PathVector offset, boolean nullable, boolean relative, boolean update) {
        super(entity, offset, nullable, relative, update);
    }
    
    public LivingEntityLocation(GenericEntity entity, Location location, PathVector offset, boolean nullable, boolean relative, boolean update) {
        super(entity, location, nullable, relative, update, offset.add(location.toVector().subtract(entity.getEyeLocation().toVector())));
    }
    
    
    @Override
    public void update() {
        entity.ifPreset(entity -> update(entity.getEyeLocation()));
    }
    
    
    public static<GenericEntity extends LivingEntity> LivingEntityBuilder<GenericEntity> builder(GenericEntity entity) {
        return new LivingEntityBuilder<>(new LivingEntityLocation<>(entity, new PathVector(), false, false, false));
    }
    
    public static<GenericEntity extends LivingEntity> LivingEntityBuilder<GenericEntity> builder(GenericEntity entity, Location location) {
        return new LivingEntityBuilder<>(new LivingEntityLocation<>(entity, location, new PathVector(), false, false, false));
    }
    
    public static class LivingEntityBuilder<GenericEntity extends LivingEntity> extends AbstractBuilder<LivingEntityBuilder<GenericEntity>, LivingEntityLocation<?>> {

        public LivingEntityBuilder(LivingEntityLocation location) {
            super(location);
        }
        
        @Override
        protected LivingEntityBuilder<GenericEntity> getThis() {
            return this;
        }
        
        @Override
        public LivingEntityLocation<GenericEntity> build() {
            return (LivingEntityLocation<GenericEntity>) location;
        }
        
    }
    
}
