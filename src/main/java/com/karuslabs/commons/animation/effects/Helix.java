/*
 * The MIT License
 *
 * Copyright 2017 .
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
package com.karuslabs.commons.animation.effects;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.world.BoundLocation;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import static java.lang.Math.*;


public class Helix extends AsynchronousEffect<Particles, BoundLocation, BoundLocation> {
    
    private int strands;
    private float radius;
    private float curve;
    private double rotation;
    
    
    public Helix(Plugin plugin, Particles particles, boolean orientate, int strands, float radius, float curve, double rotation, long iterations, long delay, long period, TimeUnit unit) {
        super(plugin, particles, orientate, iterations, delay, period, unit);
        this.strands = strands;
        this.radius = radius;
        this.curve = curve;
        this.rotation = rotation;
    }

    @Override
    protected Task task(BiConsumer render, BoundLocation origin, BoundLocation target) {
        return new HelixTask(particles, render, origin, target, strands, radius, curve, rotation, orientate, iterations);
    }
    
    
    static class HelixTask extends Task<Particles, BoundLocation, BoundLocation> {
        
        private int strands;
        private float radius;
        private float curve;
        private double rotation;

        public HelixTask(Particles particles, BiConsumer render, BoundLocation origin, BoundLocation target, int strands, float radius, float curve, double rotation, boolean orientate, long iterations) {
            super(particles, render, origin, target, orientate, iterations);
            this.strands = strands;
            this.radius = radius;
            this.curve = curve;
            this.rotation = rotation;
        }

        @Override
        protected void render() {
            Location location = origin.getLocation();
            int amount = particles.getAmount();
            
            for (int i = 1; i <= strands; i++) {
                for (int j = 1; j <= amount; j++) {
                    float ratio = (float) j / amount;
                    double angle = curve * ratio * 2 * PI / strands + (2 * PI * i / strands) + rotation;
                    double x = cos(angle) * ratio * radius;
                    double z = sin(angle) * ratio * radius;
                    location.add(x, 0, z);
                    render.accept(particles, location);
                    location.subtract(x, 0, z);
                }
            }
        }
        
    }
    
}
