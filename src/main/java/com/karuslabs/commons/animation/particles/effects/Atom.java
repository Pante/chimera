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

import com.karuslabs.commons.animation.particles.ColouredParticles;
import com.karuslabs.commons.animation.particles.effect.*;
import com.karuslabs.commons.world.BoundLocation;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


public class Atom implements Task<BoundLocation, BoundLocation> {

    private ColouredParticles nucleus;
    private ColouredParticles orbital;
    private int nucleusTotal;
    private int orbitialTotal;
    private double radius;
    private float nucleusRadius;
    private int orbitals;
    private double rotation;
    private double angularVelocity;
    private int step;
    private Vector vector;
    
    
    public Atom(ColouredParticles nucleus, ColouredParticles orbital) {
        this(nucleus, orbital, 10, 10, 3, 0.2F, 3, 0, PI / 80);
    }
    
    public Atom(ColouredParticles nucleus, ColouredParticles orbital, int nucleusTotal, int orbitialTotal, double radius, float nucleusRadius, int orbitals, double rotation, double angularVelocity) {
        this.nucleus = nucleus;
        this.orbital = orbital;
        this.nucleusTotal = nucleusTotal;
        this.orbitialTotal = orbitialTotal;
        this.radius = radius;
        this.nucleusRadius = nucleusRadius;
        this.orbitals = orbitals;
        this.rotation = rotation;
        this.angularVelocity = angularVelocity;
        step = 0;
        vector = new Vector();
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        
        renderNucleus(context, location);
        renderOrbitals(context, location);
    }
    
    protected void renderNucleus(Context<BoundLocation, BoundLocation> context, Location location) {
        for (int i = 0; i < nucleusTotal; i += nucleus.getAmount()) {
            random(vector);
            vector.multiply(radius * nucleusRadius);

            context.render(nucleus, location.add(vector));
            location.subtract(vector);
        }
    }
    
    protected void renderOrbitals(Context<BoundLocation, BoundLocation> context, Location location) {
        for (int i = 0; i < orbitialTotal; i += orbital.getAmount()) {
            double angle = step * angularVelocity;
            
            for (int j = 0; j < orbitals; j++) {        
                vector.setX(cos(angle)).setY(sin(angle)).setZ(0).multiply(radius);
                
                double xRotation = (PI / orbitals) * j;

                rotateAroundXAxis(vector, xRotation);
                rotateAroundYAxis(vector, rotation);
                
                context.render(orbital, location.add(vector));
                location.subtract(vector);
            }
            step++;
        }
    }
    
}
