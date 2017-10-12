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

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;

import static com.karuslabs.commons.world.Vectors.randomCircle;


public class Smoke implements Task<Smoke, BoundLocation, BoundLocation> {
    
    private Particles particles;
    private int perIteration;
    private double width;
    private double height;
    
    
    public Smoke(Particles particles) {
        this(particles, 20, 0.6, 2);
    }
    
    public Smoke(Particles particles, int perIteration, double width, double height) {
        this.particles = particles;
        this.perIteration = perIteration;
        this.width = width;
        this.height = height;
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        for (int i = 0; i < perIteration; i += particles.getAmount()) {
            location.add(randomCircle(context.getVector()).multiply(random.nextDouble(0, 0.6)));
            location.add(0, random.nextDouble(0, 2), 0);
            context.render(particles, location);
        }
    }

    @Override
    public Smoke get() {
        return this;
    }
    
}
