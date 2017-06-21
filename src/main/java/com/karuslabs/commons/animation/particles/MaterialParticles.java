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
package com.karuslabs.commons.animation.particles;

import java.util.Set;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import static com.google.common.collect.Sets.immutableEnumSet;

import static org.bukkit.Particle.*;


public class MaterialParticles extends AnimationParticles {
    
    private static final Set<Particle> ALLOWED = immutableEnumSet(BLOCK_CRACK, BLOCK_DUST, FALLING_DUST);  
        
    protected static Particle validate(Particle type) {
        if (ALLOWED.contains(type)) {
            return type;
            
        } else {
            throw new IllegalArgumentException("Invalid Particle: " + type);
        }
    }
    
    
    private MaterialData data;
    
    
    public MaterialParticles(Particle type, int amount, MaterialData data) {
        this(type, amount, 0, 0, 0, 1, data);
    }
    
    public MaterialParticles(Particle type, int amount, double offsetX, double offsetY, double offsetZ, double speed, MaterialData data) {
        super(type, amount, offsetX, offsetY, offsetZ, speed);
        validate(type);
        this.data = data;
    }
    
    
    @Override
    public void render(Player player, Location location) {
        player.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, speed, data);
    }

    @Override
    public void render(Location location) {
        location.getWorld().spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, speed, data);
    }
    
    
    public MaterialData getData() {
        return data;
    }
    
    
    public static MaterialParticlesBuilder newMaterialParticles() {
        return new MaterialParticlesBuilder(new MaterialParticles(BLOCK_CRACK, 0, new MaterialData(Material.AIR)));
    }
    
    
    public static class MaterialParticlesBuilder extends ParticlesBuilder<MaterialParticlesBuilder, MaterialParticles> {

        protected MaterialParticlesBuilder(MaterialParticles particles) {
            super(particles);
        }
        
        
        @Override
        public MaterialParticlesBuilder type(Particle type) {
            particles.type = validate(type);
            return this;
        }
        
        public MaterialParticlesBuilder data(MaterialData data) {
            particles.data = data;
            return this;
        }

        
        @Override
        protected MaterialParticlesBuilder getThis() {
            return this;
        }
        
    }
    
}
