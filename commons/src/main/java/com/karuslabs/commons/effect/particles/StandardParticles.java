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

import org.bukkit.*;
import org.bukkit.entity.Player;


@Immutable
public class StandardParticles extends AbstractParticles {
    
    public StandardParticles(Particle particle, int amount, double offsetX, double offsetY, double offsetZ, double speed) {
        super(particle, amount, offsetX, offsetY, offsetZ, speed);
    }
    
    @Override
    protected void render(World world, double x, double y, double z) {
        world.spawnParticle(particle, x, y, z, amount, offsetX, offsetY, offsetZ, speed);
    }

    @Override
    protected void render(Player player, double x, double y, double z) {
        player.spawnParticle(particle, x, y, z, amount, offsetX, offsetY, offsetZ, speed);
    }
    
    
    public static StandardBuilder builder() {
        return new StandardBuilder(new StandardParticles(null, 0, 0, 0, 0, 0));
    }

    
    public static class StandardBuilder extends AbstractBuilder<StandardBuilder, StandardParticles> {

        private StandardBuilder(StandardParticles particles) {
            super(particles);
        }

        @Override
        protected StandardBuilder self() {
            return this;
        }
        
    }
    
}
