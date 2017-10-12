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
import org.bukkit.util.Vector;

import static com.karuslabs.commons.animation.particles.effects.Constants.*;
import static com.karuslabs.commons.world.Vectors.rotate;
import static java.lang.Math.*;


public class Circle implements Task<Circle, BoundLocation, BoundLocation> {
    
    private Particles particles;
    private int total;
    private float radius;
    private Vector rotation;
    private Vector angularVelocity;
    private Vector subtract;
    private boolean rotate;
    
    
    public Circle(Particles particles) {
        this(particles, 20, 0.4F, NONE, ANGULAR_VELOCITY, NONE, true);
    }
    
    public Circle(Particles particles, int total, float radius, Vector rotation, Vector angularVelocity, Vector subtract, boolean rotate) {
        this.particles = particles;
        this.total = total;
        this.radius = radius;
        this.rotation = rotation;
        this.angularVelocity = angularVelocity;
        this.subtract = subtract;
        this.rotate = rotate;
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation().subtract(subtract);
        Vector vector = context.getVector();
        long current = context.getCurrent();
        
        double increment = (2 * PI) / total;
        double angle = current * increment;
        vector.setX(cos(angle) * radius).setY(0).setZ(sin(angle) * radius);
        
        rotate(vector, rotation);
        if (rotate) {
            rotate(vector, angularVelocity.getX() * current, angularVelocity.getY() * current, angularVelocity.getZ() * current);
        }
        
        context.render(particles, location, vector);
    }

    @Override
    public Circle get() {
        return this;
    }
    
}
