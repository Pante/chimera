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

import static java.lang.Math.*;


public class Warp implements Task<Warp, BoundLocation, BoundLocation> {

    private Particles particles;
    private float radius;
    private int total;
    private float grow;
    private int rings;
    private int step;
    
    
    public Warp(Particles particles) {
        this(particles, 1, 20, 0.2f, 12);
    }

    public Warp(Particles particles, float radius, int total, float grow, int rings) {
        this.particles = particles;
        this.radius = radius;
        this.total = total;
        this.grow = grow;
        this.rings = rings;
        this.step = 0;
    }

    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        double x, y, z;
        
        if (step > rings) {
            step = 0;
        }
        
        y = step * grow;
        location.add(0, y, 0);

        for (int i = 0; i < total; i += particles.getAmount()) {
            double angle = 2 * PI * i / total;

            x = cos(angle) * radius;
            z = sin(angle) * radius;
            
            context.render(particles, location.add(x, 0, z));
            location.subtract(x, 0, z);
        }

        location.subtract(0, y, 0);
    }

    @Override
    public Warp get() {
        return new Warp(particles, radius, total, grow, rings);
    }
    
}
