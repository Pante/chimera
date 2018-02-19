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

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.randomCircle;
import static java.lang.Math.*;


@Immutable
public class Fountain implements Effect {
    
    private Particles particles;
    private int strands;
    private int perStrand;
    private int perSpout;
    private float radius;
    private float radiusSpout;
    private float height;
    private float heightSpout;
    private double rotation;
    
    
    public Fountain(Particles particles) {
        this(particles, 10, 150, 200, 5, 0.1F, 3, 7, PI / 4);
    }
    
    public Fountain(Particles particles, int strands, int perStrand, int perSpout, float radius, float radiusSpout, float height, float heightSpout, double rotation) {
        this.particles = particles;
        this.strands = strands;
        this.perStrand = perStrand;
        this.perSpout = perSpout;
        this.radius = radius;
        this.radiusSpout = radiusSpout;
        this.height = height;
        this.heightSpout = heightSpout;
        this.rotation = rotation;
    }
    
    
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        renderStrands(context, origin, offset);
        renderSpout(context, origin, offset, ThreadLocalRandom.current());
    }
    
    void renderStrands(Context context, Location location, Vector offset) {
        for (int i = 1; i <= strands; i++) {
            double angle = 2 * i * PI / strands + rotation;
            double x = cos(angle) * radius;
            double z = sin(angle) * radius;
            
            for (int j = 1; j <= perStrand; j += particles.getAmount()) {
                float ratio = (float) j / perStrand;

                offset.setX(x * ratio);
                offset.setY(sin(PI * j / perStrand) * height);
                offset.setZ(z * ratio);

                context.render(particles, location, offset);
            }
        }
    }
    
    void renderSpout(Context context, Location location, Vector offset, ThreadLocalRandom random) {
        for (int i = 0; i < perSpout; i += particles.getAmount()) {
            randomCircle(offset).multiply(random.nextDouble(0, radius * radiusSpout));
            offset.setY(random.nextDouble(0, heightSpout));
            
            context.render(particles, location, offset);
        }
    }
    
}
