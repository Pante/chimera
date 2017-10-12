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


public class Vortex implements Task<Vortex, BoundLocation, BoundLocation> {

    private Particles particles;
    private float radius;
    private float grow;
    private double radials;
    private int circles;
    private int helixes;
    
    
    public Vortex(Particles particles) {
        this(particles, 2, 0.5f, PI / 16, 3, 4);
    }

    public Vortex(Particles particles, float radius, float grow, double radials, int circles, int helixes) {
        this.particles = particles;
        this.radius = radius;
        this.grow = grow;
        this.radials = radials;
        this.circles = circles;
        this.helixes = helixes;
    }

    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        Vector vector = context.getVector();
        
        for (int x = 0; x < circles; x++) {
            for (int i = 0; i < helixes; i++) {
                double angle = context.getCurrent() * radials + (2 * PI * i / helixes);
                vector.setX(cos(angle) * radius);
                vector.setY(context.getCurrent() * grow);
                vector.setZ(sin(angle) * radius);

                rotateAroundXAxis(vector, (location.getPitch() + 90) * (PI / 180));
                rotateAroundYAxis(vector, -location.getYaw() * (PI / 180));

                context.render(particles, location, vector);
            }
        }
    }

    @Override
    public Vortex get() {
        return this;
    }
    
}
