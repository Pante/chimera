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

import static java.lang.Math.*;


@Immutable
public class Warp extends IncrementalEffect {

    private Particles particles;
    private float radius;
    private int total;
    private float grow;
    
    
    public Warp(Particles particles) {
        this(particles, 12, 1, 20, 0.2f);
    }

    public Warp(Particles particles, int rings, float radius, int total, float grow) {
        super(rings);
        this.particles = particles;
        this.radius = radius;
        this.total = total;
        this.grow = grow;
    }

    
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        offset.setY(context.steps() * grow);
        for (int i = 0; i < total; i += particles.getAmount()) {
            double angle = 2 * PI * i / total;

            offset.setX(cos(angle) * radius);
            offset.setZ(sin(angle) * radius);
            
            context.render(particles, origin, offset);
        }
        
    }
    
}
