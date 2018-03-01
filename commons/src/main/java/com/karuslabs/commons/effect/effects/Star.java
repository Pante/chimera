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

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


/**
 * Represents a star.
 * <br>
 * <img src = "https://thumbs.gfycat.com/GrimAllJay-size_restricted.gif" alt = "Star.gif">
 */
@Immutable
public class Star implements Effect {
    
    private Particles particles;
    private int perIteration;
    private float spikeHeight;
    private int spikeHalf;
    private double radius;
    
    
    /**
     * Constructs a {@code Star} with the specified particles, the default number of particles per iteration, 50, 
     * height of the spikes, 3.5, half the amount of spikes, 3 and radius, 0.5.
     * 
     * @param particles the particles
     */
    public Star(Particles particles) {
        this(particles, 50, 3.5F, 3, 0.5);
    }
    
    /**
     * Constructs a {@code Star} with the specified particles, particles per iteration, height of the spikes, half the amount of spikes and radius.
     * 
     * @param particles the particles
     * @param perIteration particles per iteration
     * @param spikeHeight the height of the spikes 
     * @param spikeHalf half the amount of spikes
     * @param radius the radius
     */
    public Star(Particles particles, int perIteration, float spikeHeight, int spikeHalf, double radius) {
        this.particles = particles;
        this.perIteration = perIteration;
        this.spikeHeight = spikeHeight;
        this.spikeHalf = spikeHalf;
        this.radius = radius;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        double radius = 3 * this.radius / sqrt(3);
        
        for (int i = 0; i < spikeHalf * 2; i++) {
            render(context, origin, offset, random, radius, i * PI / spikeHalf);
        }
    }
    
    void render(Context context, Location location, Vector vector, ThreadLocalRandom random, double radius, double rotation) {
        for (int x = 0; x < perIteration; x += particles.getAmount()) {
            double angle = 2 * PI * x / perIteration;
            float height = random.nextFloat() * spikeHeight;
            
            vector.setX(cos(angle)).setY(0).setZ(sin(angle));
            vector.multiply((spikeHeight - height) * radius / spikeHeight);
            vector.setY(this.radius + height);
            
            rotateAroundXAxis(vector, rotation);
            context.render(particles, location, vector);
            
            rotateAroundXAxis(vector, PI);
            rotateAroundYAxis(vector, PI / 2);
            context.render(particles, location, vector);
        }
    }

}
