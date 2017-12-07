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

import com.karuslabs.commons.effect.*;
import com.karuslabs.commons.effect.particles.Particles;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


public class Dragon extends ResettableEffect {
    
    private Particles particles;
    private float pitch;
    private int arcs;
    private int perArc;
    private float length;
    private float[] floats;
    private double[] angles;
    private int stepsPerIteration;
    
    
    public Dragon(Particles particles) {
        this(particles, 200, 0.1F, 20, 30, 4, 2);
    }
    
    public Dragon(Particles particles, int steps, float pitch, int arcs, int perArc, float length, int stepsPerIteration) {
        super(steps);
        this.particles = particles;
        this.pitch = pitch;
        this.arcs = arcs;
        this.perArc = perArc;
        this.length = length;
        this.stepsPerIteration = stepsPerIteration;
        floats = new float[arcs];
        angles = new double[arcs];
    }
    
    
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        int step = context.steps();
        for (int j = 0; j < stepsPerIteration; j++, step++) {
            if (step % perArc == 0) {
                populate(ThreadLocalRandom.current());
            }
            
            renderArcs(context, origin, offset, step);
        }
        context.steps(step);
    }
    
    void populate(ThreadLocalRandom random) {
        for (int i = 0; i < arcs; i++) {
            floats[i] = random.nextFloat();
            angles[i] = randomAngle();
        }
    }

    void renderArcs(Context context, Location location, Vector offset, int step) {
        float yaw = (float) toRadians(-(location.getYaw() + 90));
        float pitch = (float) toRadians(-location.getPitch());
        
        for (int i = 0; i < arcs; i++) {
            float x = (step % perArc) * length / perArc;
            float y = (float) ((floats[i] * 2 * this.pitch - this.pitch) * pow(x, 2));

            offset.setX(x).setY(y).setZ(0);

            rotateAroundXAxis(offset, angles[i]);
            rotateAroundYAxis(offset, yaw);
            rotateAroundZAxis(offset, pitch);
                
            context.render(particles, location, offset);
        }
    }

    
    @Override
    public Dragon get() {
        return new Dragon(particles, steps, pitch, arcs, perArc, length, stepsPerIteration);
    }
    
}
