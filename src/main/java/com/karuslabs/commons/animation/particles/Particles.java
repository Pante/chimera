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

import java.util.Collection;

import org.bukkit.*;
import org.bukkit.entity.Player;


/**
 * Represents {@code Particles} which consist of a {@code Particle} type and amount.
 */
@Immutable
public abstract class Particles {
    
    /**
     * The {@code Particle} type.
     */
    protected Particle particle;
    /**
     * The amount of {@code Particle}s to render.
     */
    protected int amount;
    
    
    /**
     * Constructs a {@code Particles} with the specified {@code Particle} type and amount.
     * 
     * @param particle the Particle type
     * @param amount the amount
     */
    public Particles(Particle particle, int amount) {
        this.particle = particle;
        this.amount = amount;
    }
    
    
    /**
     * Renders this {@code Particles} at the specified {@code Location}.
     * 
     * @param location the location
     */
    public abstract void render(Location location);
    
    
    /**
     * Renders this {@code Particles} for the specified {@code Player} only at the {@code Location} of the specified {@code Player}.
     * 
     * @param player the Player
     */
    public void render(Player player) {
        render(player, player.getLocation());
    }
    
    /**
     * Renders this {@code Particles} for the specified {@code Player}s only at the specified {@code Location}.
     * 
     * @param players the Players
     * @param location the Location
     */
    public void render(Collection<Player> players, Location location) {
        players.forEach(player -> render(player, location));
    }
    
    /**
     * Renders this {@code Particles} for the specified {@code Player} only at the specified {@code Location}.
     * 
     * @param player the Player
     * @param location the Location
     */
    public abstract void render(Player player, Location location);
    
    
    /**
     * Returns the the {@code Particle} type.
     * 
     * @return the Particle type
     */
    public Particle getParticle() {
        return particle;
    }
    
    /**
     * Returns the amount of {@code Particle}s to render.
     * 
     * @return the amount of Particles
     */
    public int getAmount() {
        return amount;
    }
    
    
    /**
     * Represent a builder for {@code Particles}.
     * 
     * @param <GenericBuilder> the type of Builder to return
     * @param <GenericParticles> the type of the Particles to build
     */
    public static abstract class Builder<GenericBuilder extends Builder, GenericParticles extends Particles> {
        
        /**
         * The {@code Particles} to build.
         */
        protected GenericParticles particles;
        
        
        /**
         * Constructs a {@code Builder} for the specified {@code GenericParticles}.
         * 
         * @param particles the GenericParticles
         */
        public Builder(GenericParticles particles) {
            this.particles = particles;
        }
        
        
        /**
         * Sets the {@code Particle} type.
         * 
         * @param particle the Particle type
         * @return this
         */
        public GenericBuilder particle(Particle particle) {
            particles.particle = particle;
            return getThis();
        }
        
        /**
         * Sets the amount of {@code Particle}s to spawn.
         * 
         * @param amount the amount
         * @return this
         */
        public GenericBuilder amount(int amount) {
            particles.amount = amount;
            return getThis();
        }
        
        /**
         * Returns a generic version of {@code this}.
         * @return this
         */
        protected abstract GenericBuilder getThis();
        
        
        /**
         * Builds the {@code GenericParticles}.
         * 
         * @return the GenericParticles
         */
        public GenericParticles build() {
            return particles;
        }
        
    }
    
}
