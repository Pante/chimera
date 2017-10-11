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

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


public class Dragon implements Task<Dragon, BoundLocation, BoundLocation> {
    
    private Particles particles;
    private float pitch;
    private int arcs;
    private int perArc;
    private float length;
    private float[] floats;
    private double[] angles;
    private int step;
    private int stepsPerIteration;
    private Vector vector;
    
    
    public Dragon(Particles particles) {
        this(particles, 0.1F, 20, 30, 4, 2);
    }
    
    public Dragon(Particles particles, float pitch, int arcs, int perArc, float length, int stepsPerIteration) {
        this.particles = particles;
        this.pitch = pitch;
        this.arcs = arcs;
        this.perArc = perArc;
        this.length = length;
        this.stepsPerIteration = stepsPerIteration;
        floats = new float[arcs];
        angles = new double[arcs];
        step = 0;
        vector = new Vector();
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        for (int j = 0; j < stepsPerIteration; j++, step++) {
            if (step % perArc == 0) {
                populate(ThreadLocalRandom.current());
            }
            
            for (int i = 0; i < arcs; i++) {
                float pitch = floats[i] * 2 * this.pitch - this.pitch;
                float x = (step % perArc) * length / perArc;
                float y = (float) (pitch * pow(x, 2));
                
                vector.setX(x).setY(y).setZ(0);

                rotateAroundXAxis(vector, angles[i]);
                rotateAroundYAxis(vector, toRadians(-(location.getYaw() + 90)));
                rotateAroundZAxis(vector, toRadians(-location.getPitch()));
                
                context.render(particles, location, vector);
            }
        }
    }
    
    protected void populate(ThreadLocalRandom random) {
        for (int i = 0; i < arcs; i++) {
            floats[i] = random.nextFloat();
            angles[i] = randomAngle();
        }
    }

    @Override
    public Dragon get() {
        return new Dragon(particles, pitch, arcs, perArc, length, stepsPerIteration);
    }
    
}
