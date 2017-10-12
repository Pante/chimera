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
package com.karuslabs.commons.animation.particles.effects;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.animation.particles.effect.*;
import com.karuslabs.commons.world.BoundLocation;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.random;


public class Sphere implements Task<Sphere, BoundLocation, BoundLocation> {
    
    private Particles particles;
    private int perIteration = 50;
    private double radius = 0.6;
    private double yOffset = 0;
    private double increment = 0;
    
    
    public Sphere(Particles particles) {
        this(particles, 50, 0.6, 0, 0);
    }
    
    public Sphere(Particles particles, int perIteration, double radius, double yOffset, double increment) {
        this.particles = particles;
        this.perIteration = perIteration;
        this.radius = radius;
        this.yOffset = yOffset;
        this.increment = increment;
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Vector vector = context.getVector();
        Location location = context.getOrigin().getLocation();
        location.add(0, yOffset, 0);
        
        double radius = this.radius + context.getCurrent() * increment;

        for (int i = 0; i < perIteration; i += particles.getAmount()) {
            random(vector).multiply(radius);
            context.render(particles, location, vector);
        }
    }

    @Override
    public Sphere get() {
        return this;
    }
    
}
