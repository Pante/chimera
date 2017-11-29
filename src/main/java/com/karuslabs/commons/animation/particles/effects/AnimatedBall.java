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

import static com.karuslabs.commons.animation.particles.effects.Constants.NONE;
import static com.karuslabs.commons.world.Vectors.rotate;
import static java.lang.Math.*;


@Immutable
public class AnimatedBall implements Task<AnimatedBall> {
    
    private static final Vector FACTOR = new Vector(1, 2, 1);
    private static final Vector OFFSET = new Vector(0, 0.8, 0);
    
    private Particles particles;
    private int total;
    private int perIteration;
    private float size;
    private Vector factor;
    private Vector offset;
    private Vector rotation;
    
    
    public AnimatedBall(Particles particles) {
        this(particles, 150, 10, 1F, FACTOR, OFFSET, NONE);
    }
    
    public AnimatedBall(Particles particles, int total, int perIteration, float size, Vector factor, Vector offset, Vector rotation) {
        this.particles = particles;
        this.total = total;
        this.perIteration = perIteration;
        this.size = size;
        this.factor = factor;
        this.offset = offset;
        this.rotation = rotation;
    }
    
    
    @Override
    public void render(Context context) {
        Location location = context.getOrigin().getLocation();
        Vector vector = context.getVector();
        int count = (int) context.count();
        
        for (int i = 0; i < perIteration; i += particles.getAmount(), count++) {
            float t = (float) (PI / total) * count;
            float r = (float) sin(t) * size;
            float s = (float) (2 * PI * t);

            vector.setX(factor.getX() * r * cos(s) + offset.getX());
            vector.setY(factor.getY() * size * cos(t) + offset.getY());
            vector.setZ(factor.getZ() * r * sin(s) + offset.getZ());

            rotate(vector, rotation);
            
            context.render(particles, location, vector);
        }
        context.count(count);
    }

    @Override
    public @Immutable AnimatedBall get() {
        return this;
    }
    
}
