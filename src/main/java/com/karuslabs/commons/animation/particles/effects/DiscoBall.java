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

import static com.karuslabs.commons.world.Vectors.random;


public class DiscoBall implements Task<DiscoBall, BoundLocation, BoundLocation> {
    
    public enum Direction {
        UP, DOWN, BOTH;
    }
    
    private Particles sphere;
    private Particles line;
    private int sphereTotal;
    private int lineTotal;
    private float radius;
    private int lines;
    private int lineLength;
    private Direction direction = Direction.DOWN;
    private Vector vector;
    private Vector distance;
    
    
    public DiscoBall(Particles sphere, Particles line) {
        this(sphere, line, 50, 100, 0.6F, 7, 15, Direction.DOWN);
    }
    
    public DiscoBall(Particles sphere, Particles line, int sphereTotal, int lineTotal, float radius, int lines, int lineLength, Direction direction) {
        this.sphere = sphere;
        this.line = line;
        this.sphereTotal = sphereTotal;
        this.lineTotal = lineTotal;
        this.radius = radius;
        this.lines = lines;
        this.lineLength = lineLength;
        this.direction = direction;
        vector = new Vector();
        distance = new Vector();
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        renderLines(context, location, random);
        renderSphere(context, location);
    }
    
    protected void renderLines(Context<BoundLocation, BoundLocation> context, Location location, ThreadLocalRandom random) {
        int max = random.nextInt(2, lines) * 2;
        for (int i = 0; i < max; i++) {
            double x = random.nextInt(-lineLength, lineLength);
            double y = random.nextInt(-lineLength, lineLength);
            double z = random.nextInt(-lineLength, lineLength);
            
            if (direction == Direction.DOWN) {
                y = random.nextInt(lineLength, lineLength * 2);
                
            } else if (direction == Direction.UP) {
                y = random.nextInt(-2 * lineLength, -lineLength);
            }
            
            vector.setX(-x).setY(-y).setZ(-z);
            double ratio = vector.length() / lineTotal;
            vector.normalize().multiply(ratio);
            
            distance.setX(0).setY(0).setZ(0);
            for (int j = 0; j < lineTotal; j += line.getAmount()) {
                context.render(line, location.add(distance));
                distance.add(vector);
            }
            location.subtract(distance);
        }
    }
    
    protected void renderSphere(Context<BoundLocation, BoundLocation> context, Location location) {
        for (int i = 0; i < sphereTotal; i += sphere.getAmount()) {
            random(vector).multiply(radius);
            context.render(sphere, location, vector);
        }
    }
    
    @Override
    public DiscoBall get() {
        return new DiscoBall(sphere, line, sphereTotal, lineTotal, radius, lines, lineLength, direction);
    }
}
