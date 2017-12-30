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

import static com.karuslabs.commons.world.Vectors.randomCircle;


/**
 * Represents smoke.
 * <br>
 * <img src = "https://thumbs.gfycat.com/FearlessInformalDutchsmoushond-size_restricted.gif" alt = "Smoke.gif">
 */
@Immutable
public class Smoke implements Effect {
    
    private Particles particles;
    private int perIteration;
    private double width;
    private double height;
    
    
    /**
     * Constructs a {@code Smoke} with the specified particles, the default number of particles per iteration, 20, width, 0.6 and height, 2.
     * 
     * @param particles the particles
     */
    public Smoke(Particles particles) {
        this(particles, 20, 0.6, 2);
    }
    
    /**
     * Constructs a {@code Smoke} with the specified particles, particles per iteration, width and height.
     * 
     * @param particles the particles
     * @param perIteration the particles per iteration
     * @param width the width
     * @param height the height
     */
    public Smoke(Particles particles, int perIteration, double width, double height) {
        this.particles = particles;
        this.perIteration = perIteration;
        this.width = width;
        this.height = height;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        for (int i = 0; i < perIteration; i += particles.getAmount()) {
            randomCircle(offset).multiply(random.nextDouble(0, width));
            offset.setY(offset.getY() + random.nextDouble(0, height));

            context.render(particles, origin, offset);
        }
    }
    
}
