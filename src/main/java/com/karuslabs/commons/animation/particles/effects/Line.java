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

import static com.karuslabs.commons.world.Vectors.copy;


public class Line implements Task<Line, BoundLocation, BoundLocation> {
    
    private static final Vector OFFSET = new Vector(0,0.1,0);
    
    private Particles particles;
    private int perArc = 100;
    private double length = 0;
    private boolean zigzag = false;
    private int zigzags = 10;
    private Vector offset;
    private boolean direction;
    private int step = 0;
    private Vector vector;
    private Vector link;
    private Vector distance;
    
    
    public Line(Particles particles) {
        this(particles, 100, 0, false, 10, OFFSET);
    }
    
    public Line(Particles particles, int perArc, double length, boolean zigzag, int zigzags, Vector offset) {
        this.particles = particles;
        this.perArc = perArc;
        this.length = length;
        this.zigzag = zigzag;
        this.zigzags = zigzags;
        this.offset = offset;
        direction = false;
        step = 0;
        vector = new Vector();
        link = new Vector();
        distance = new Vector();
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        resolveLink(context);

        double amount = perArc / zigzags;
        double ratio = link.length() / perArc;
                
        link.normalize().multiply(ratio);
        distance.setX(0).setY(0).setZ(0);
        for (int i = 0; i < perArc; i += particles.getAmount(), step++) {
            if (zigzag) {
                if (direction) {
                    distance.add(offset);
                } else {
                    distance.subtract(offset);
                }
            }
            
            if (step >= amount) {
                direction = !direction;
                step = 0;
            }

            context.render(particles, location.add(distance));
        }
    }
    
    protected void resolveLink(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        copy(location, vector);
        
        if (length > 0) {
            copy(location, link).add(location.getDirection().normalize().multiply(length));
        } else {
            copy(context.getTarget().getLocation(), link);
        }
        
        link.subtract(vector);
    }
    

    @Override
    public Line get() {
        return new Line(particles, perArc, length, zigzag, zigzags, offset);
    }
    
}
