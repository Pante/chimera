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

import static com.karuslabs.commons.effect.effects.Constants.NONE;
import static com.karuslabs.commons.world.Vectors.rotate;
import static java.lang.Math.*;


/**
 * Represents a torus.
 * <br>
 * <img src = "https://thumbs.gfycat.com/FakeParallelKangaroo-size_restricted.gif" alt = "Donut.gif">
 */
@Immutable
public class Donut implements Effect {
    
    private Particles particles;
    private int perCircle;
    private int circles;
    private float donutRadius;
    private float tubeRadius;
    private Vector rotation;
    
    
    /**
     * Constructs a {@code Donut} with the specified particles, the default particles per circle, 10, number of circles, 36, total radius of donut, 2, 
     * radius of tube, 0.5 and rotation, (0, 0, 0).
     * 
     * @param particles the particles
     */
    public Donut(Particles particles) {
        this(particles, 10, 36, 2, 0.5F, NONE);
    }
    
    /**
     * Constructs a {@code Donut} with the specified particles, particles per circle, number of circles, total radius of donut, radius of tube
     * and rotation.
     * 
     * @param particles the particles
     * @param perCircle the total number of particles per per circle
     * @param circles the total number of circles
     * @param donutRadius the total radius of the donut
     * @param tubeRadius the radius of the tube
     * @param rotation the rotation in radians
     */
    public Donut(Particles particles, int perCircle, int circles, float donutRadius, float tubeRadius, Vector rotation) {
        this.particles = particles;
        this.perCircle = perCircle;
        this.circles = circles;
        this.donutRadius = donutRadius;
        this.tubeRadius = tubeRadius;
        this.rotation = rotation;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        for (int i = 0; i < circles; i++) {
            double theta = 2 * PI * i / circles;
            double cosTheta = cos(theta);
            double sinTheta = sin(theta);
            
            for (int j = 0; j < perCircle; j += particles.getAmount()) {
                double phi = 2 * PI * j / perCircle;
                double cosPhi = cos(phi);
                
                offset.setX((donutRadius + tubeRadius * cosPhi) * cosTheta);
                offset.setY((donutRadius + tubeRadius * cosPhi) * sinTheta);
                offset.setZ(tubeRadius * sin(phi));

                rotate(offset, rotation);
                
                context.render(particles, origin, offset);
            }
        }
    }
    
}
