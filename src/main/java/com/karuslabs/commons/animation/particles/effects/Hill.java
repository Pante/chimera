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

import static com.karuslabs.commons.world.Vectors.rotateAroundYAxis;
import static java.lang.Math.*;


public class Hill implements Task<Hill, BoundLocation, BoundLocation> {
    
    private Particles particles;
    private float perRow;
    private float length;
    private float height;
    private double yRotation;
    
    
    public Hill(Particles particles) {
        this(particles, 30, 6.5F, 2.5F, PI / 7);
    }
    
    public Hill(Particles particles, float perRow, float length, float height, double yRotation) {
        this.particles = particles;
        this.perRow = perRow;
        this.length = length;
        this.height = height;
        this.yRotation = yRotation;
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        Vector vector = context.getVector();
        
        for (int x = 0; x <= perRow; x += particles.getAmount()) {
            double y1 = sin(PI * x / perRow);
            
            for (int z = 0; z <= perRow; z += particles.getAmount()) {
                double y2 = sin(PI * z / perRow);
                vector.setX(length * x / perRow).setZ(length * z / perRow);
                vector.setY(height * y1 * y2);
                
                rotateAroundYAxis(vector, yRotation);
                
                context.render(particles, location, vector);
            }
        }
    }

    @Override
    public Hill get() {
        return this;
    }
    
}
