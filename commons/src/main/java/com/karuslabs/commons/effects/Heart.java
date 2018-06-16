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
package com.karuslabs.commons.effects;

import com.karuslabs.annotations.Immutable;
import com.karuslabs.commons.effect.*;
import com.karuslabs.commons.effect.particles.Particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.effects.Constants.NONE;
import static com.karuslabs.commons.world.Vectors.rotate;
import static java.lang.Math.*;


@Immutable
public class Heart implements Effect {
    
    private Particles particles;
    private int perIteration;
    private Vector rotation;
    private float xFactor;
    private float yFactor;
    private float spike;
    private double yFactorCompression;
    private float compilation;
    
    
    public Heart(Particles particles) {
        this(particles, 50, NONE, 1, 1, 0.8F, 2, 2F);
    }
    
    public Heart(Particles particles, int perIteration, Vector rotation, float xFactor, float yFactor, float spike, double yFactorCompression, float compilation) {
        this.particles = particles;
        this.perIteration = perIteration;
        this.rotation = rotation;
        this.xFactor = xFactor;
        this.yFactor = yFactor;
        this.spike = spike;
        this.yFactorCompression = 1 / yFactorCompression;
        this.compilation = compilation;
    }
    
    
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        for (int i = 0; i < perIteration; i += particles.getAmount()) {
            double alpha = PI / compilation / perIteration * i;
            double phi = pow(abs(sin(2 * compilation * alpha)) + spike * abs(sin(compilation * alpha)), yFactorCompression);
            double sinAlpha = sin(alpha);
            double cosAlpha = cos(alpha);
            
            offset.setY(phi * (sinAlpha + cosAlpha) * yFactor);
            offset.setZ(phi * (cosAlpha - sinAlpha) * xFactor);

            rotate(offset, rotation);

            context.render(particles, origin, offset);
        }
    }
    
}
