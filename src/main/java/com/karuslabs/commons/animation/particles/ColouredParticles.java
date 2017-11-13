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

import static org.bukkit.Color.WHITE;


@Immutable
public class ColouredParticles extends Particles {
    
    private Color colour;
    private double r, g, b;
    
    
    public ColouredParticles(Particle type, Color colour, int amount) {
        super(type, amount);
        setColour(colour);
    }
        
    private void setColour(Color colour) {
        this.colour = colour;
        r = colour.getRed() / 255.0;
        g = colour.getGreen() / 255.0;
        b = colour.getBlue() / 255.0;
    }

    
    @Override
    public void render(Location location) {
        location.getWorld().spawnParticle(particle, location, amount, r, g, b, 1);
    }
    
    @Override
    public void render(Player player, Location location) {
        player.spawnParticle(particle, location, amount, r, g, b, 1);
    }

    
    public Color getColour() {
        return colour;
    }
    
    
    public static ColouredBuilder builder() {
        return new ColouredBuilder(new ColouredParticles(null, WHITE, 0));
    }
    
    public static class ColouredBuilder extends Builder<ColouredBuilder, ColouredParticles> {

        private ColouredBuilder(ColouredParticles particles) {
            super(particles);
        }
        
        
        public ColouredBuilder colour(Color colour) {
            particles.setColour(colour);
            return this;
        }

        @Override
        protected ColouredBuilder getThis() {
            return this;
        }
        
    }
    
}
