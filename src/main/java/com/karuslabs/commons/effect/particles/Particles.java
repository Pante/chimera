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
package com.karuslabs.commons.effect.particles;

import com.karuslabs.commons.annotation.Immutable;

import java.util.Collection;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


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
    public void render(Location location) {
        render(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }
    
    /**
     * Renders this {@code Particles} at the specified {@code Location} with the specified offset.
     * 
     * @param location the location
     * @param offset the offset
     */
    public void render(Location location, Vector offset) {
        render(location.getWorld(), location.getX() + offset.getX(), location.getY() + offset.getY(), location.getZ() + offset.getZ());
    }
    
    /**
     * Renders this {@code Particles} in the specified {@code World} at the specified coordinates.
     * 
     * @param world the world
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    protected abstract void render(World world, double x, double y, double z);
    
    
    /**
     * Renders this {@code Particles} for the specified {@code Player} only at the {@code Location} of the specified {@code Player}.
     * 
     * @param player the player
     */
    public void render(Player player) {
        render(player, player.getLocation());
    }
    
    /**
     * Renders this {@code Particles} for the specified {@code Player}s only at the specified {@code Location}.
     * 
     * @param players the players
     * @param location the location
     */
    public void render(Collection<Player> players, Location location) {
        for (Player player : players) {
            render(player, location);
        }
    }
    
    /**
     * Renders this {@code Particles} for the specified {@code Player} only at the specified {@code Location}.
     * 
     * @param player the player
     * @param location the location
     */
    public void render(Player player, Location location) {
        render(player, location.getX(), location.getY(), location.getZ());
    }
    
    
    /**
     * Renders this {@code Particles} for the specified {@code Player} only at the {@code Location} of the specified {@code Player}
     * with the offset.
     * 
     * @param player the player
     * @param offset the offset
     */
    public void render(Player player, Vector offset) {
        render(player, player.getLocation(), offset);
    }
    
    /**
     * Renders this {@code Particles} for the specified {@code Player}s only at the specified {@code Location}
     * with the offset.
     * 
     * @param players the players
     * @param location the location
     * @param offset the offset
     */
    public void render(Collection<Player> players, Location location, Vector offset) {
        double x = location.getX() + offset.getX();
        double y = location.getY() + offset.getY();
        double z = location.getZ() + offset.getZ();
        
        for (Player player : players) {
            render(player, x, y, z);
        }
    }
    
    /**
     * Renders this {@code Particles} for the specified {@code Player} only at the specified {@code Location}
     * with the offset.
     * 
     * @param player the player
     * @param location the location
     * @param offset the offset
     */
    public void render(Player player, Location location, Vector offset) {
        render(player, location.getX() + offset.getX(), location.getY() + offset.getY(), location.getZ() + offset.getZ());
    }
    
    
    /**
     * Renders this {@code Particles} for the specified {@code Player} only at the specified coordinates.
     * 
     * @param player the player
     * @param x the X coordinate
     * @param y the Y coordinate
     * @param z the Z coordinate
     */
    protected abstract void render(Player player, double x, double y, double z);
    
    
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
