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
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.copy;
import static java.lang.Math.pow;


@Immutable
public class Arc implements Task<Arc> {
    
    private Particles particles;
    private float height;
    private int total;
    
    
    public Arc(Particles particles) {
        this(particles, 2, 100);
    }
    
    public Arc(Particles particles, float height, int total) {
        this.particles = particles;
        this.height = height;
        this.total = total;
    }
    
    
    @Override
    public void render(Context context) {
        Location origin = context.getOrigin().getLocation();
        Location target = context.getTarget().getLocation();

        Vector link = target.toVector().subtract(origin.toVector());

        double length = link.length();
        double pitch = 4 * height / pow(length, 2);
        
        link.normalize();
        Vector offset = context.getVector();
        for (int i = 0; i < total; i += particles.getAmount()) {
            offset = copy(link, offset);
            offset.multiply((float) length * i / total);
            
            double x = ((double) i / total) * length - length / 2;
            double y = -pitch * pow(x, 2) + height;
            
            offset.setY(offset.getY() + y);
            context.render(particles, origin, offset);
        }
    }

    @Override
    public @Immutable Arc get() {
        return this;
    }
    
}