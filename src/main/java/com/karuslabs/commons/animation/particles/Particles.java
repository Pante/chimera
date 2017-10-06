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

import com.karuslabs.commons.animation.particles.ColouredParticles.ColouredBuilder;
import com.karuslabs.commons.animation.particles.ItemParticles.ItemBuilder;
import com.karuslabs.commons.animation.particles.MaterialParticles.MaterialBuilder;
import com.karuslabs.commons.animation.particles.StandardParticles.StandardBuilder;

import java.util.Collection;

import org.bukkit.*;
import org.bukkit.entity.Player;


public abstract class Particles {
    
    public static ColouredBuilder coloured() {
        return new ColouredBuilder(new ColouredParticles(null, 0, Color.WHITE));
    }
    
    public static ItemBuilder item() {
        return new ItemBuilder(new ItemParticles(null, 0, 0, 0, 0, 0, null));
    }
    
    public static MaterialBuilder material() {
        return new MaterialBuilder(new MaterialParticles(null, 0, 0, 0, 0, 0, null));
    }
    
    public static StandardBuilder standard() {
        return new StandardBuilder(new StandardParticles(null, 0, 0, 0, 0, 0));
    }
    
    
    protected Particle particle;
    protected int amount;
    
    
    public Particles(Particle particle, int amount) {
        this.particle = particle;
        this.amount = amount;
    }
    
    
    public void render(Player player) {
        render(player, player.getLocation());
    }
    
    public void render(Collection<Player> players, Location location) {
        players.forEach(player -> render(player, location));
    }
    
    public abstract void render(Player player, Location location);
    
    public abstract void render(Location location);

    
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
            return getThis();
        }
        
        public GenericBuilder amount(int amount) {
            particles.amount = amount;
            return getThis();
        }
        
        protected abstract GenericBuilder getThis();
                
        public GenericParticles build() {
            return particles;
        }
        
    }
    
}
