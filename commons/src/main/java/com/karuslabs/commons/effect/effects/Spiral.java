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

import com.karuslabs.annotations.Immutable;
import com.karuslabs.commons.effect.*;
import com.karuslabs.commons.effect.particles.Particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static java.lang.Math.*;


@Immutable
public class Spiral implements Effect {
    
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
    public void render(Context context, Location origin, Location target, Vector offset) {
        for (int i = 1; i <= strands; i++) {
            for (int j = 1; j <= perStrand; j += particles.getAmount()) {
                float ratio = (float) j / perStrand;
                double angle = curve * ratio * 2 * PI / strands + (2 * PI * i / strands) + rotation;
                double x = cos(angle) * ratio * radius;
                double z = sin(angle) * ratio * radius;
                
                context.render(particles, origin, offset.setX(x).setZ(z));
            }
        }
    }
    
}
