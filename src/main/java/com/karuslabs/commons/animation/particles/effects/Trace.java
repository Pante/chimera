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

import java.util.*;

import org.bukkit.*;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.copy;


public class Trace implements Task<Trace, BoundLocation, BoundLocation> {
    
    private Particles particles;
    private int refresh;
    private int max;
    List<Vector> waypoints;
    World world;
    
    
    public Trace(Particles particles) {
        this(particles, 5, 30);
    }
    
    public Trace(Particles particles, int refresh, int max) {
        this.particles = particles;
        this.refresh = refresh;
        this.max = max;
        waypoints = new ArrayList<>(max);
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        if (world == null) {
            world = location.getWorld();
            
        } else if (world.equals(location.getWorld())) {
            render(context, location);
            
        } else {
            context.cancel();
        }
    }
    
    protected void render(Context<BoundLocation, BoundLocation> context, Location location) {
        waypoints.add(process(location));
        
        if ((context.getCurrent() + 1) % refresh == 0) {
            location = new Location(world, 0, 0, 0);
            
            for (Vector position : waypoints) {
                location.setX(position.getX());
                location.setY(position.getY());
                location.setZ(position.getZ());
                
                context.render(particles, location);
            }
        }
    }
    
    protected Vector process(Location location) {
        if (waypoints.size() >= max) {
            Vector vector = waypoints.remove(0);
            return copy(location, vector);
            
        } else {
            return location.toVector();
        }
    }
    

    @Override
    public Trace get() {
        return new Trace(particles, refresh, max);
    }
    
}
