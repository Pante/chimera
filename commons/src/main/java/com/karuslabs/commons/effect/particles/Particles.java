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

import com.karuslabs.annotations.Immutable;

import java.util.Collection;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


@Immutable
public abstract class Particles {
    
    protected Particle particle;
    protected int amount;
    
    
    public Particles(Particle particle, int amount) {
        this.particle = particle;
        this.amount = amount;
    }
    
        
    public void render(Location location) {
        render(location.getWorld(), location.getX(), location.getY(), location.getZ());
    }
    
    public void render(Location location, Vector offset) {
        render(location.getWorld(), location.getX() + offset.getX(), location.getY() + offset.getY(), location.getZ() + offset.getZ());
    }
        
    protected abstract void render(World world, double x, double y, double z);
    
    
    public void render(Player player) {
        render(player, player.getLocation());
    }
    
    public void render(Collection<Player> players, Location location) {
        for (Player player : players) {
            render(player, location);
        }
    }
    
    public void render(Player player, Location location) {
        render(player, location.getX(), location.getY(), location.getZ());
    }
    
    
    public void render(Player player, Vector offset) {
        render(player, player.getLocation(), offset);
    }
    
    public void render(Collection<Player> players, Location location, Vector offset) {
        double x = location.getX() + offset.getX();
        double y = location.getY() + offset.getY();
        double z = location.getZ() + offset.getZ();
        
        for (Player player : players) {
            render(player, x, y, z);
        }
    }
    
    public void render(Player player, Location location, Vector offset) {
        render(player, location.getX() + offset.getX(), location.getY() + offset.getY(), location.getZ() + offset.getZ());
    }
    
    
    protected abstract void render(Player player, double x, double y, double z);
    

    public Particle getParticle() {
        return particle;
    }
    
    public int getAmount() {
        return amount;
    }
    
    
    public static abstract class Builder<GenericBuilder extends Builder, GenericParticles extends Particles> {
        
        protected GenericParticles particles;
        
        
        public Builder(GenericParticles particles) {
            this.particles = particles;
        }
        
        
        public GenericBuilder particle(Particle particle) {
            particles.particle = particle;
            return self();
        }
        
        public GenericBuilder amount(int amount) {
            particles.amount = amount;
            return self();
        }
        
        protected abstract GenericBuilder self();
        
        
        public GenericParticles build() {
            return particles;
        }
        
    }
    
}
