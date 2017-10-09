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
package com.karuslabs.commons.animation.particles.tasks;

import com.karuslabs.commons.animation.particles.Particles;
import com.karuslabs.commons.animation.particles.effect.*;
import com.karuslabs.commons.world.BoundLocation;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.rotateVector;
import static java.lang.Math.*;


/**
 * @author <a href="http://forums.bukkit.org/members/qukie.90952701/">Qukie</a>
 */
public class AnimatedBall implements Task<BoundLocation, BoundLocation> {
    
    private Particles particles;
    private int total = 150;
    private int perIteration;
    private float size = 1F;
    private Vector factor;
    private Vector offset;
    private Vector rotation;
    
    private int step;
    private Vector vector;
    
    
    public AnimatedBall(Particles particles) {
        this(particles, 150, 10, 1F, new Vector(1, 2, 1), new Vector(0, 0.8, 0), new Vector(0, 0, 0));
    }
    
    public AnimatedBall(Particles particles, int total, int perIteration, float size, Vector factor, Vector offset, Vector rotation) {
        this.particles = particles;
        this.total = total;
        this.size = size;
        this.factor = factor;
        this.offset = offset;
        this.rotation = rotation;
        this.step = 0;
        this.vector = new Vector();
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        
        for (int i = 0; i < perIteration; i += particles.getAmount(), step++) {
            float t = (float) (PI / total) * step;
            float r = (float) sin(t) * size;
            float s = (float) (2 * PI * t);

            vector.setX(factor.getX() * r * cos(s) + offset.getX());
            vector.setZ(factor.getY() * r * sin(s) + offset.getY());
            vector.setY(factor.getZ() * size * cos(t) + offset.getZ());

            rotateVector(vector, rotation.getX(), rotation.getY(), rotation.getZ());

            context.render(particles, location.add(vector));
            location.subtract(vector);
        }
    }
    
}
