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

import static com.karuslabs.commons.world.Vectors.randomCircle;
import static java.lang.Math.*;


public class Fountain implements Task<Fountain, BoundLocation, BoundLocation> {
    
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
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        renderStrands(context, location);
        renderSpout(context, location, ThreadLocalRandom.current());
    }
    
    protected void renderStrands(Context<BoundLocation, BoundLocation> context, Location location) {
        Vector vector = context.getVector();
        for (int i = 1; i <= strands; i++) {
            double angle = 2 * i * PI / strands + rotation;
            for (int j = 1; j <= perStrand; j += particles.getAmount()) {
                float ratio = (float) j / perStrand;

                vector.setX(cos(angle) * radius * ratio);
                vector.setY(sin(PI * j / perStrand) * height);
                vector.setZ(sin(angle) * radius * ratio);
                
                context.render(particles, location, vector);
            }
        }
    }
    
    protected void renderSpout(Context<BoundLocation, BoundLocation> context, Location location, ThreadLocalRandom random) {
        Vector vector = context.getVector();
        for (int i = 0; i < perSpout; i += particles.getAmount()) {
            randomCircle(vector).multiply(random.nextDouble(0, radius * radiusSpout));
            vector.setY(random.nextDouble(0, heightSpout));
            
            context.render(particles, location, vector);
        }
    }
    

    @Override
    public Fountain get() {
        return this;
    }
    
}
