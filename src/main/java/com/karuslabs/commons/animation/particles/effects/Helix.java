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
import static java.lang.Math.*;

import org.bukkit.Location;
import org.bukkit.util.Vector;


@Immutable
public class Helix implements Task<Helix> {
    
    private Particles particles;
    private int trail;
    private double radius;
    private double height;
    private double increment;
    
    
    public Helix(Particles particles) {
        this(particles, 10, 5, 10, 0.05);
    }
    
    public Helix(Particles particles, int trail, double radius, double height, double increment) {
        this.particles = particles;
        this.trail = trail;
        this.radius = radius;
        this.height = height;
        this.increment = increment;
    }
    
    
    @Override
    public void render(Context context) {
        Vector vector = context.getVector();
        Location location = context.getOrigin().getLocation();
        
        double y = context.count();
        for (int i = 0; i < trail && y <= height; i += particles.getAmount(), y += increment) {
            vector.setX(radius * cos(y)).setY(y).setZ(radius * sin(y));
            context.render(particles, location, vector);
        }
        context.count(y);
    }

    @Override
    public @Immutable Helix get() {
        return this;
    }
    
}
