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

import static com.karuslabs.commons.world.Vectors.copy;
import static java.lang.Math.pow;


/**
 * Represents an arc.
 * <br>
 * <img src = "https://thumbs.gfycat.com/FalseGargantuanAdmiralbutterfly-size_restricted.gif" alt = "Arc.gif">
 */
@Immutable
public class Arc implements Effect {

    private Particles particles;
    private float height;
    private int total;
    
    
    /**
     * Constructs an {@code Arc} with the specified particles, the default height, 2 and total, 200.
     * 
     * @param particles the particles
     */
    public Arc(Particles particles) {
        this(particles, 2, 100);
    }
    
    /**
     * Constructs an {@code Arc} with the specified particles, height and total.
     * 
     * @param particles the particles
     * @param height the height
     * @param total the total
     */
    public Arc(Particles particles, float height, int total) {
        this.particles = particles;
        this.height = height;
        this.total = total;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        Vector link = target.toVector().subtract(copy(origin, offset));

        double length = link.length();
        double pitch = 4 * height / pow(length, 2);
        
        link.normalize();
        for (int i = 0; i < total; i += particles.getAmount()) {
            offset.copy(link);
            offset.multiply((float) length * i / total);
            
            double x = ((double) i / total) * length - (length / 2);
            double y = -pitch * pow(x, 2) + height;
            
            offset.setY(offset.getY() + y);
            context.render(particles, origin, offset);
        }
    }
    
}
