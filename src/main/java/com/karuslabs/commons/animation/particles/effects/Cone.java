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

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


@Immutable
public class Cone implements Task<Cone> {
    
    private Particles particles;
    private float lengthGrowth;
    private float radiusGrowth;
    private double angularVelocity;
    private int size;
    private int perIteration;
    private double rotation;
    private boolean randomize;
    
    
    public Cone(Particles particles) {
        this(particles, 0.5F, 0.006F, PI / 16, 180, 10, 0, false);
    }
    
    public Cone(Particles particles, float lengthGrowth, float radiusGrowth, double angularVelocity, int size, int perIteration, double rotation, boolean randomize) {
        this.particles = particles;
        this.lengthGrowth = lengthGrowth;
        this.radiusGrowth = radiusGrowth;
        this.angularVelocity = angularVelocity;
        this.size = size;
        this.perIteration = perIteration;
        this.rotation = rotation;
        this.randomize = randomize;
    }
    
    
    @Override
    public void render(Context context) {
        Location location = context.getOrigin().getLocation();
        Vector vector = context.getVector();
        int count = context.count();
        
        for (int x = 0; x < perIteration; x += particles.getAmount(), count++) {
            if (count > size) {
                count = 0;
            }
            if (count == 0 && randomize) {
                rotation = randomAngle();
            }
            
            double angle = count * angularVelocity + rotation;
            float length = count * lengthGrowth;
            float radius = count * radiusGrowth;
            
            vector.setX(cos(angle) * radius).setY(length).setZ(sin(angle) * radius);
            rotateAroundXAxis(vector, toRadians(location.getPitch() + 90));
            rotateAroundYAxis(vector, toRadians(-location.getYaw()));

            context.render(particles, location, vector);
        }
        context.count(count);
    }

    @Override
    public @Immutable Cone get() {
        return this;
    }
    
}
