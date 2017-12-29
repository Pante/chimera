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

import static com.karuslabs.commons.effect.effects.Constants.*;
import static com.karuslabs.commons.world.Vectors.rotate;
import static java.lang.Math.*;


/**
 * Represents an animated ball.
 * <img src = "https://thumbs.gfycat.com/TinyFantasticAuklet-size_restricted.gif" alt = "AnimatedBall.gif">
 */
@Immutable
public class AnimatedBall extends ResettableEffect {
    
    private static final Vector FACTOR = new Vector(1, 2, 1);
    
    private Particles particles;
    private int total;
    private int perIteration;
    private float size;
    private Vector factor;
    private Vector rotation;
    
    
    /**
     * Constructs an {@code AnimatedBall} with the specified particles and 
     * the default total number of steps (500), total (150), particles per iteration (10), size (1), factor (1, 2, 1) and rotation (0, 0, 0). 
     * 
     * @param particles the particles
     */
    public AnimatedBall(Particles particles) {
        this(particles, 500, 150, 10, 1F, FACTOR, NONE);
    }
    
    /**
     * Constructs an {@code AnimatedBall} with the specified particles, total number of steps, total, particles per iteration , size, factor and rotation.
     * 
     * @param particles the particles
     * @param steps the total number of steps
     * @param total the total number of particles
     * @param perIteration the number of particles per iteration
     * @param size the size of the animated ball
     * @param factor the proportions of the animated ball
     * @param rotation the rotation in radians
     */
    public AnimatedBall(Particles particles, int steps, int total, int perIteration, float size, Vector factor, Vector rotation) {
        super(steps);
        this.particles = particles;
        this.total = total;
        this.perIteration = perIteration;
        this.size = size;
        this.factor = factor;
        this.rotation = rotation;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        int steps = context.steps();
        
        for (int i = 0; i < perIteration; i += particles.getAmount(), steps++) {
            float t = (float) (PI / total) * steps;
            float r = (float) sin(t) * size;
            float s = (float) (2 * PI * t);
            
            offset.setX(factor.getX() * r * cos(s));
            offset.setY(factor.getY() * size * cos(t));
            offset.setZ(factor.getZ() * r * sin(s));
            
            rotate(offset, rotation);
            
            context.render(particles, origin, offset);
        }
        context.steps(steps);
    }
    
}
