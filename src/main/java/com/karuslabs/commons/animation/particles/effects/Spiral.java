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
import com.karuslabs.commons.annotation.Immutable;

import org.bukkit.Location;

import static java.lang.Math.*;


@Immutable
public class Spiral implements Task<Spiral> {
    
    private Particles particles;
    private int strands;
    private int perStrand;
    private float radius;
    private float curve;
    private double rotation;
    
    
    public Spiral(Particles particles) {
        this(particles, 8, 80, 10, 10, PI / 4);
    }
    
    public Spiral(Particles particles, int strands, int perStrand, float radius, float curve, double rotation) {
        this.particles = particles;
        this.strands = strands;
        this.perStrand = perStrand;
        this.radius = radius;
        this.curve = curve;
        this.rotation = rotation;
    }
    
    
    @Override
    public void render(Context context) {
        Location location = context.getOrigin().getLocation();

        for (int i = 1; i <= strands; i++) {
            for (int j = 1; j <= perStrand; j += particles.getAmount()) {
                float ratio = (float) j / perStrand;
                double angle = curve * ratio * 2 * PI / strands + (2 * PI * i / strands) + rotation;
                double x = cos(angle) * ratio * radius;
                double z = sin(angle) * ratio * radius;
                
                context.render(particles, location.add(x, 0, z));
                location.subtract(x, 0, z);
            }
        }
    }

    @Override
    public Spiral get() {
        return this;
    }
    
}
