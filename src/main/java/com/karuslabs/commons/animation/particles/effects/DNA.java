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
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Vector vector = context.getVector();
        int count = context.count();
        
        for (int j = 0; j < perHelix; j += helix.getAmount(), count++) {
            if (count * growth > length) {
                count = 0;
            }
            
            renderHelix(context, vector, count);
            
            if (count % interval == 0) {
                renderBase(context, base1, vector, count, 1, perBase);
                renderBase(context, base2, vector, count, -perBase, -1);
            }
        }
        context.count(count);
    }
    
    protected void renderHelix(Context<BoundLocation, BoundLocation> context, Vector vector, int count) {
        for (int i = 0; i < 2; i++) {
            double angle = count * radials + PI * i;
            vector.setX(cos(angle) * radius).setY(count * growth).setZ(sin(angle) * radius);
            render(context, helix, vector);
        }
    }
    
    protected void renderBase(Context<BoundLocation, BoundLocation> context, Particles particles, Vector vector, int count, int initial, int total) {
        for (int i = initial; i <= total; i += particles.getAmount()) {
            double angle = count * radials;
            vector.setX(cos(angle)).setY(0).setZ(sin(angle)).multiply(radius * i / perBase).setY(count * growth);
            render(context, particles, vector);
        }
    }
    
    protected void render(Context<BoundLocation, BoundLocation> context, Particles particles, Vector vector) {
        Location location = context.getOrigin().getLocation();

        rotateAroundXAxis(vector, toRadians(location.getPitch() + 90));
        rotateAroundYAxis(vector, toRadians(-location.getYaw()));
        
        context.render(particles, location, vector);
    }

    @Override
    public DNA get() {
        return this;
    }
    
}
