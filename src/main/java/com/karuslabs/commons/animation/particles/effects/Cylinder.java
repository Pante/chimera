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

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.animation.particles.effects.Constants.*;
import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


public class Cylinder implements Task<BoundLocation, BoundLocation> {
  
    private Particles particles;
    private float radius;
    private float height;
    private float ratio;
    private Vector angularVelocity;
    private Vector rotation;
    private int row;
    private boolean rotate;
    private boolean solid;
    private int step;
    private Vector vector;
    private Random random;
    
    
    public Cylinder(Particles particles) {
        this(particles, 1, 3, 0.4884726437F, ANGULAR_VELOCITY, NONE, 100, true, false);
    }
    
    public Cylinder(Particles particles, float radius, float height, float ratio, Vector angularVelocity, Vector rotation, int row, boolean rotate, boolean solid) {
        this.particles = particles;
        this.radius = radius;
        this.height = height;
        this.ratio = ratio;
        this.angularVelocity = angularVelocity;
        this.rotation = rotation;
        this.row = row;
        this.rotate = rotate;
        this.solid = solid;
        step = 0;
        vector = new Vector();
        random = new Random();
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        double x = rotation.getX(), y = rotation.getY(), z = rotation.getZ();
        if (rotate) {
            x += step * angularVelocity.getX();
            y += step * angularVelocity.getY();
            z += step * angularVelocity.getZ();
        }
        
        for (int i = 0; i < row; i += particles.getAmount(), step++) {
            randomCircle(vector);
            vector.multiply(radius);
            
            calculate(solid ? random.nextFloat() : 1);
            
            if (rotate) {
                rotateVector(vector, x, y, z);
            }
            
            context.render(particles, location.add(vector));
            location.subtract(vector);
        }
        context.render(particles, location);
    }
    
    protected void calculate(float multiple) {
        if (random.nextFloat() <= ratio) {
            // SIDE PARTICLE
            vector.multiply(multiple).setY((random.nextFloat() * 2 - 1) * (height / 2));
        } else {
            // GROUND PARTICLE
            vector.multiply(random.nextFloat());
            if (random.nextFloat() < 0.5) {
                // TOP
                vector.setY(multiple * (height / 2));
            } else {
                // BOTTOM
                vector.setY(-multiple * (height / 2));
            }
        }
    }
    
}
