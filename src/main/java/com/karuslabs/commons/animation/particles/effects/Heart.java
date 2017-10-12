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

import static com.karuslabs.commons.animation.particles.effects.Constants.NONE;
import static com.karuslabs.commons.world.Vectors.rotate;
import static java.lang.Math.*;


public class Heart implements Task<Heart, BoundLocation, BoundLocation> {
    
    private Particles particles;
    private int perIteration;
    private Vector rotation;
    private double xFactor;
    private double yFactor;
    private double spike;
    private double yFactorCompression;
    private float compilation;
    
    
    public Heart(Particles particles) {
        this(particles, 50, NONE, 1, 1, 0.8, 2, 2F);
    }
    
    public Heart(Particles particles, int perIteration, Vector rotation, double xFactor, double yFactor, double spike, double yFactorCompression, float compilation) {
        this.particles = particles;
        this.perIteration = perIteration;
        this.rotation = rotation;
        this.xFactor = xFactor;
        this.yFactor = yFactor;
        this.spike = spike;
        this.yFactorCompression = yFactorCompression;
        this.compilation = compilation;
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        Vector vector = context.getVector();
        
        for (int i = 0; i < perIteration; i += particles.getAmount()) {
            double alpha = PI / compilation / perIteration * i;
            double phi = pow(abs(sin(2 * compilation * alpha)) + spike * abs(sin(compilation * alpha)), 1 / yFactorCompression);

            vector.setY(phi * (sin(alpha) + cos(alpha)) * yFactor);
            vector.setZ(phi * (cos(alpha) - sin(alpha)) * xFactor);

            rotate(vector, rotation);

            context.render(particles, location, vector);
        }
    }

    @Override
    public Heart get() {
        return this;
    }
    
}
