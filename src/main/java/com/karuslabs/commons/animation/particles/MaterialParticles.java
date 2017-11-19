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
package com.karuslabs.commons.animation.particles;

import com.karuslabs.commons.annotation.Immutable;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;


/**
 * A concrete subclass of {@code Particles} which additionally consists of a {@code MaterialData}, offset and speed.
 * <p>
 * This class may contain {@code Particle} type(s) of {@link Particle#BLOCK_CRACK}, {@link Particle#BLOCK_DUST} 
 * and {@link Particle#FALLING_DUST} only.
 */
@Immutable
public class MaterialParticles extends AbstractParticles {    
    
    private MaterialData data;
    
    
    /**
     * Constructs a {@code MaterialParticles} with the specified {@code Particle} type, {@code MaterialData}, amount, offset and speed.
     * 
     * @param type the Particle type
     * @param data the MaterialData
     * @param amount the amount
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @param speed the speed
     */
    public MaterialParticles(Particle type, MaterialData data, int amount, double offsetX, double offsetY, double offsetZ, double speed) {
        super(type, amount, offsetX, offsetY, offsetZ, speed);
        this.data = data;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Location location) {
        location.getWorld().spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed, data);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Player player, Location location) {
        player.spawnParticle(particle, location, amount, offsetX, offsetY, offsetZ, speed, data);
    }
    
    
    /**
     * Returns the {@code MaterialData}
     * 
     * @return the MaterialData
     */
    public MaterialData getData() {
        return data;
    }
    
    
    /**
     * Creates a {@code MaterialParticles} builder.
     * 
     * @return the builder
     */
    public static MaterialBuilder builder() {
        return new MaterialBuilder(new MaterialParticles(null, null, 0, 0, 0, 0, 0));
    }
    
    
    /**
     * Represents a builder for {@code MaterialParticles}.
     */
    public static class MaterialBuilder extends AbstractBuilder<MaterialBuilder, MaterialParticles> {

        private MaterialBuilder(MaterialParticles particles) {
            super(particles);
        }
        
        /**
         * Sets the {@code MaterialData}.
         * 
         * @param data the data
         * @return this
         */
        public MaterialBuilder data(MaterialData data) {
            particles.data = data;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected MaterialBuilder getThis() {
            return this;
        }
        
    }
    
}
