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

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


public class DNA implements Task<DNA, BoundLocation, BoundLocation> {
    
    private Particles helix;
    private Particles base1;
    private Particles base2; // no third base :(
    private double radials;
    private float radius;
    private int perHelix;
    private int perBase;
    private float length;
    private float growth;
    private float interval;
    private int step;
    private Vector vector;
    
    
    public DNA(Particles helix, Particles base1, Particles base2) {
        this(helix, base1, base2, PI / 30, 1.5F, 3, 15, 15, 0.2F, 10);
    }
    
    public DNA(Particles helix, Particles base1, Particles base2, double radials, float radius, int perHelix, int perBase, float length, float growth, float interval) {
        this.helix = helix;
        this.base1 = base1;
        this.base2 = base2;
        this.radials = radials;
        this.radius = radius;
        this.perHelix = perHelix;
        this.perBase = perBase;
        this.length = length;
        this.growth = growth;
        this.interval = interval;
        step = 0;
        vector = new Vector();
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        for (int j = 0; j < perHelix; j += helix.getAmount(), step++) {
            if (step * growth > length) {
                step = 0;
            }
            
            renderHelix(context);
            
            if (step % interval == 0) {
                renderBase(context, base1, 1, perBase);
                renderBase(context, base2, -perBase, -1);
            }
        }
    }
    
    protected void renderHelix(Context<BoundLocation, BoundLocation> context) {
        for (int i = 0; i < 2; i++) {
            double angle = step * radials + PI * i;
            vector.setX(cos(angle) * radius).setY(step * growth).setZ(sin(angle) * radius);
            render(context, helix);
        }
    }
    
    protected void renderBase(Context<BoundLocation, BoundLocation> context, Particles particles, int initial, int total) {
        for (int i = initial; i <= total; i += particles.getAmount()) {
            double angle = step * radials;
            vector.setX(cos(angle)).setY(0).setZ(sin(angle)).multiply(radius * i / perBase).setY(step * growth);
            render(context, particles);
        }
    }
    
    protected void render(Context<BoundLocation, BoundLocation> context, Particles particles) {
        Location location = context.getOrigin().getLocation();
        
        rotateAroundXAxis(vector, toRadians(location.getPitch() + 90));
        rotateAroundYAxis(vector, toRadians(-location.getYaw()));

        context.render(particles, location, vector);
    }

    @Override
    public DNA get() {
        return new DNA(helix, base1, base2, radials, radius, perHelix, perBase, length, growth, interval);
    }
    
}
