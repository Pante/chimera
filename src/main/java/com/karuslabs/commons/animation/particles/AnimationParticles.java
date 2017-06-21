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

import org.bukkit.*;
import org.bukkit.entity.Player;


public class AnimationParticles extends Particles {
    
    protected double offsetX;
    protected double offsetY;
    protected double offsetZ;
    protected double speed;
    
    
    public AnimationParticles(Particle type, int amount) {
        this(type, amount, 0, 0, 0, 1);
    }
    
    public AnimationParticles(Particle type, int amount, double offsetX, double offsetY, double offsetZ, double speed) {
        super(type, amount);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
    }

    
    @Override
    public void render(Player player, Location location) {
        player.spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, speed);
    }

    @Override
    public void render(Location location) {
        location.getWorld().spawnParticle(type, location, amount, offsetX, offsetY, offsetZ, speed);
    }

    
    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    public double getSpeed() {
        return speed;
    }
    
    
    public static AnimationParticlesBuilder newAnimationParticles() {
        return new AnimationParticlesBuilder(new AnimationParticles(Particle.BARRIER, 0));
    }
    
    
    public static class AnimationParticlesBuilder extends ParticlesBuilder<AnimationParticlesBuilder, AnimationParticles> {

        public AnimationParticlesBuilder(AnimationParticles particles) {
            super(particles);
        }

        @Override
        protected AnimationParticlesBuilder getThis() {
            return this;
        }
    }

    
    public static abstract class ParticlesBuilder<GenericBuilder extends ParticlesBuilder, GenericParticles extends AnimationParticles> extends Builder<GenericBuilder, GenericParticles> {
        
        protected ParticlesBuilder(GenericParticles particles) {
            super(particles);
        }
        
        
        public GenericBuilder offsetX(double x) {
            particles.offsetX = x;
            return getThis();
        }
        
        public GenericBuilder offsetY(double y) {
            particles.offsetY = y;
            return getThis();
        }
        
        public GenericBuilder offsetZ(double z) {
            particles.offsetZ = z;
            return getThis();
        }
        
        public GenericBuilder speed(double speed) {
            particles.speed = speed;
            return getThis();
        }
    
    }
    
}
