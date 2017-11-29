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

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.copy;


public class Line implements Task<Line> {
    
    private static final Vector OFFSET = new Vector(0,0.1,0);
    
    private Particles particles;
    private int perArc;
    double length;
    boolean zigzag;
    private int zigzags;
    private Vector offset;
    boolean direction;
    int step;
    Vector vector;
    Vector link;
    
    
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
    }
    
    
    @Override
    public void render(Context context) {
        Location location = context.getOrigin().getLocation();
        resolveLink(context, location);
        
        int amount = perArc / zigzags;
        double ratio = link.length() / perArc;

        vector = copy(location, vector);
        link.subtract(vector);
        link.normalize().multiply(ratio);
        
        vector.setX(0).setY(0).setZ(0).subtract(link);
        for (int i = 0; i < perArc; i += particles.getAmount(), step++) {
            if (zigzag) {
                if (direction) {
                    vector.add(offset);
                    
                } else {
                    vector.subtract(offset);
                }
            }
            
            if (step >= amount) {
                direction = !direction;
                step = 0;
            }
            
            context.render(particles, location, vector.add(link));
        }
    }
    
    void resolveLink(Context context, Location origin) {
        if (length > 0) {
            copy(origin, link).add(origin.getDirection().normalize().multiply(length));
            
        } else {
            copy(context.getTarget().getLocation(), link);
        }
    }
    

    @Override
    public Line get() {
        return new Line(particles, perArc, length, zigzag, zigzags, offset);
    }
    
}
