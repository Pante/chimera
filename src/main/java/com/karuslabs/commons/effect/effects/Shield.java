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

import static com.karuslabs.commons.world.Vectors.random;
import static java.lang.Math.abs;


@Immutable
public class Shield implements Effect {
    
    private Particles particles;
    public int perIteration;
    public int radius;
    public boolean sphere;
    
    
    public Shield(Particles particles) {
        this(particles, 50, 3, false);
    }
    
    public Shield(Particles particles, int perIteration, int radius, boolean sphere) {
        this.particles = particles;
        this.perIteration = perIteration;
        this.radius = radius;
        this.sphere = sphere;
    }
    
    
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        for (int i = 0; i < perIteration; i += particles.getAmount()) {
            random(offset).multiply(radius);
            if (!sphere) {
                offset.setY(abs(offset.getY()));
            }
            
            context.render(particles, origin, offset);
        }
    }
    
}
