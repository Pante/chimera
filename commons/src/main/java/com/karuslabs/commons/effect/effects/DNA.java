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

import com.karuslabs.annotations.Immutable;
import com.karuslabs.commons.effect.*;
import com.karuslabs.commons.effect.particles.Particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


@Immutable
public class DNA extends ResettableEffect {
    
    private Particles helix;
    private Particles base1;
    private Particles base2; // no third base :(
    private float radials;
    private float radius;
    private int perHelix;
    private int perBase;
    private float length;
    private float growth;
    private float interval;
    
    
    public DNA(Particles helix, Particles base1, Particles base2) {
        this(helix, base1, base2, 500, (float) PI / 30, 1.5F, 3, 15, 15, 0.2F, 10);
    }
    
    public DNA(Particles helix, Particles base1, Particles base2, int steps, float radials, float radius, int perHelix, int perBase, float length, float growth, float interval) {
        super(500);
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
    public void render(Context context, Location origin, Location target, Vector offset) {
        int steps = context.steps();
        float pitch = (float) toRadians(origin.getPitch() + 90);
        float yaw = (float) toRadians(-origin.getYaw());
        
        for (int i = 0; i < perHelix; i += helix.getAmount(), steps++) {
            if (steps * growth > length) {
                steps = 0;
            }
            
            renderHelix(context, origin, offset, pitch, yaw, steps);
            
            if (steps % interval == 0) {
                renderBase(context, base1, origin, offset, pitch, yaw, steps, 1, perBase);
                renderBase(context, base2, origin, offset, pitch, yaw, steps, -perBase, -1);
            }
        }
        context.steps(steps);
    }
    
    void renderHelix(Context context, Location origin, Vector offset, float pitch, float yaw, int steps) {
        for (int i = 0; i < 2; i++) {
            double angle = steps * radials + PI * i;
            offset.setX(cos(angle) * radius).setY(steps * growth).setZ(sin(angle) * radius);
            render(context, helix, origin, offset, pitch, yaw);
        }
    }
    
    void renderBase(Context context, Particles particles, Location origin, Vector offset, float pitch, float yaw, int steps, int initial, int total) {
        double angle = steps * radials;
        for (int i = initial; i <= total; i += particles.getAmount()) {
            float base = radius * i / perBase;
            offset.setX(cos(angle) * base).setY(steps * growth).setZ(sin(angle) * base);
            render(context, particles, origin, offset, pitch, yaw);
        }
    }
    
    void render(Context context, Particles particles, Location origin, Vector offset, float pitch, float yaw) {
        rotateAroundXAxis(offset, pitch);
        rotateAroundYAxis(offset, yaw);
        
        context.render(particles, origin, offset);
    }
    
}
