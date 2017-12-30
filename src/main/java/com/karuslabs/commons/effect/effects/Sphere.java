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
package com.karuslabs.commons.effect.effects;

import com.karuslabs.commons.annotation.Immutable;
import com.karuslabs.commons.effect.*;
import com.karuslabs.commons.effect.particles.Particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.random;


/**
 * Represents a sphere.
 * <br>
 * <img src = "https://thumbs.gfycat.com/DistantEntireEmperorshrimp-size_restricted.gif" alt = "Sphere.gif">
 */
@Immutable
public class Sphere extends IncrementalEffect {
    
    private Particles particles;
    private int perIteration;
    private double radius;
    private double yOffset;
    private double increment;
    
    
    /**
     * Constructs a {@code Sphere} with the specified particles, 
     * the default total number of steps, 500, particles per iteration, 50, radius, 0.6, Y offset, 0 and increment, 0.
     * 
     * @param particles the particles
     */
    public Sphere(Particles particles) {
        this(particles, 500, 50, 0.6, 0, 0);
    }
    
    /**
     * Constructs a {@code Sphere} with the specified particles, total number of steps, particles per iteration, radius, Y offset and increment.
     * 
     * @param particles the particles
     * @param steps the number of steps
     * @param perIteration the particles per iteration
     * @param radius the radius
     * @param yOffset the Y offset
     * @param increment the increment
     */
    public Sphere(Particles particles, int steps, int perIteration, double radius, double yOffset, double increment) {
        super(steps);
        this.particles = particles;
        this.perIteration = perIteration;
        this.radius = radius;
        this.yOffset = yOffset;
        this.increment = increment;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        double radius = this.radius + context.steps() * increment;
        
        for (int i = 0; i < perIteration; i += particles.getAmount()) {
            random(offset).multiply(radius);
            context.render(particles, origin, offset.setY(offset.getY() + yOffset));
        }
    }
    
}
