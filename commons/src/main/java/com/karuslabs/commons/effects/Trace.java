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
package com.karuslabs.commons.effects;

import com.karuslabs.commons.effect.*;
import com.karuslabs.commons.effect.particles.Particles;

import java.util.*;

import org.bukkit.*;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.copy;


public class Trace extends IncrementalEffect {
    
    private Particles particles;
    private int refresh;
    int max;
    List<Vector> waypoints;
    Location location;
    
    
    public Trace(Particles particles) {
        this(particles, 600, 5, 30);
    }
    
    public Trace(Particles particles, int steps, int refresh, int max) {
        super(steps);
        this.particles = particles;
        this.refresh = refresh;
        this.max = max;
        waypoints = new ArrayList<>(max);
        location = new Location(null, 0, 0, 0);
    }
    
    
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        if (location.getWorld() == null) {
            location.setWorld(origin.getWorld());
        } 
        
        if (origin.getWorld().equals(location.getWorld())) {
            render(context, origin);
            
        } else {
            context.cancel();
        }
    }
    
    void render(Context context, Location current) {
        waypoints.add(process(current));
        if ((context.steps() + 1) % refresh == 0) {
            for (Vector position : waypoints) {
                location.setX(position.getX());
                location.setY(position.getY());
                location.setZ(position.getZ());
                context.render(particles, location);
            }
        }
    }
    
    Vector process(Location location) {
        if (waypoints.size() >= max) {
            Vector vector = waypoints.remove(0);
            return copy(location, vector);
            
        } else {
            return location.toVector();
        }
    }
    

    @Override
    public Trace get() {
        return new Trace(particles, steps, refresh, max);
    }
    
}
