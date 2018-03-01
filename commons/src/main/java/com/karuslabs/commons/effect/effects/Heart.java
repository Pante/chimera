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

import static com.karuslabs.commons.effect.effects.Constants.NONE;
import static com.karuslabs.commons.world.Vectors.rotate;
import static java.lang.Math.*;


/**
 * Represents a heart.
 * <br>
 * <img src = "https://thumbs.gfycat.com/PeriodicChiefAlligatorsnappingturtle-size_restricted.gif" alt = "Heart.gif">
 */
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
    
    
    /**
     * Constructs a {@code Heart} with the specified particles, the default number of particles per iteration, 50, rotation, (0, 0, 0), 
     * stretch/compress factor for the X axis, 1 and Y axis, 1, the spike of the inner line, 0.8, the compression along the Y axis, 2 and compilation, 2.
     * 
     * @param particles the particles
     */
    public Heart(Particles particles) {
        this(particles, 50, NONE, 1, 1, 0.8F, 2, 2F);
    }
    
    /**
     * Constructs a {@code Heart} with the specified particles, number of particles per iteration, rotation, 
     * stretch/compress factor for the X axis and Y axis, the spike of the inner line, the compression along the Y axis and compilation.
     * 
     * @param particles the particles
     * @param perIteration the particles per iteration
     * @param rotation the rotation in radians
     * @param xFactor the stretch/compress factor along the X axis
     * @param yFactor the stretch/compress factor along the Y axis
     * @param spike the spike of the inner line
     * @param yFactorCompression the compression along the Y axis
     * @param compilation the compilation
     */
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
    
    
    /**
     * {@inheritDoc}
     */
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
