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

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


@Immutable
public class Atom extends ResettableEffect {
    
    private Particles nucleus;
    private Particles orbital;
    private int nucleusTotal;
    private int orbitalTotal;
    private float radius;
    private float nucleusRadius;
    private int orbitals;
    private double rotation;
    private double angularVelocity;
    
    
    public Atom(Particles nucleus, Particles orbital) {
        this(nucleus, orbital, 200, 10, 10, 3, 0.2F, 3, 0, PI / 80);
    }
    
    public Atom(Particles nucleus, Particles orbital, int steps, int nucleusTotal, int orbitalTotal, float radius, float nucleusRadius, int orbitals, double rotation, double angularVelocity) {
        super(steps);
        this.nucleus = nucleus;
        this.orbital = orbital;
        this.nucleusTotal = nucleusTotal;
        this.orbitalTotal = orbitalTotal;
        this.radius = radius;
        this.nucleusRadius = nucleusRadius;
        this.orbitals = orbitals;
        this.rotation = rotation;
        this.angularVelocity = angularVelocity;
    }

    
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        renderNucleus(context, origin, offset);
        renderOrbitals(context, origin, offset);
    }
    
    void renderNucleus(Context context, Location location, Vector offset) {
        for (int i = 0; i < nucleusTotal; i += nucleus.getAmount()) {
            random(offset).multiply(radius * nucleusRadius);
            context.render(nucleus, location, offset);
        }
    }
    
    void renderOrbitals(Context context, Location location, Vector offset) {
        int steps = context.steps();
        for (int i = 0; i < orbitalTotal; i += orbital.getAmount(), steps++) {
            double angle = steps * angularVelocity;
            double x = cos(angle);
            double y = sin(angle);
            
            for (int j = 0; j < orbitals; j++) {        
                offset.setX(x).setY(y).setZ(0).multiply(radius);
                
                float xRotation = (float) ((PI / orbitals) * j);

                rotateAroundXAxis(offset, xRotation);
                rotateAroundYAxis(offset, rotation);
                
                context.render(orbital, location, offset);
            }
        }
        context.steps(steps);
    }
    
}
