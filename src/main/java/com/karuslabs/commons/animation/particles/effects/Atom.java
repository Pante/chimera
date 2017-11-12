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
import com.karuslabs.commons.annotation.Immutable;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.*;


@Immutable
public class Atom implements Task<Atom> {

    private Particles nucleus;
    private Particles orbital;
    private int nucleusTotal;
    private int orbitalTotal;
    private double radius;
    private float nucleusRadius;
    private int orbitals;
    private double rotation;
    private double angularVelocity;
    
    
    public Atom(Particles nucleus, Particles orbital) {
        this(nucleus, orbital, 10, 10, 3, 0.2F, 3, 0, PI / 80);
    }
    
    public Atom(Particles nucleus, Particles orbital, int nucleusTotal, int orbitalTotal, double radius, float nucleusRadius, int orbitals, double rotation, double angularVelocity) {
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
    public void render(Context context) {
        Location location = context.getOrigin().getLocation();
        Vector vector = context.getVector();
        
        renderNucleus(context, location, vector);
        renderOrbitals(context, location, vector, (int) context.count());
    }
    
    void renderNucleus(Context context, Location location, Vector vector) {
        for (int i = 0; i < nucleusTotal; i += nucleus.getAmount()) {
            random(vector).multiply(radius * nucleusRadius);

            context.render(nucleus, location, vector);
        }
    }
    
    void renderOrbitals(Context context, Location location, Vector vector, int count) {
        for (int i = 0; i < orbitalTotal; i += orbital.getAmount(), count++) {
            double angle = count * angularVelocity;
            for (int j = 0; j < orbitals; j++) {        
                vector.setX(cos(angle)).setY(sin(angle)).setZ(0).multiply(radius);
                
                double xRotation = (PI / orbitals) * j;

                rotateAroundXAxis(vector, xRotation);
                rotateAroundYAxis(vector, rotation);
                
                context.render(orbital, location, vector);
            }
        }
        context.count(count);
    }

    @Override
    public @Immutable Atom get() {
        return this;
    }
    
}
