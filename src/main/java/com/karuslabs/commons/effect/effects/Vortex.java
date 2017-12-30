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


/**
 * Represents a vortex.
 * <br>
 * <img src = "https://thumbs.gfycat.com/HeftyFrayedAustraliankestrel-size_restricted.gif" alt = "Vortex.gif">
 */
@Immutable
public class Vortex extends IncrementalEffect {

    private Particles particles;
    private float radius;
    private float grow;
    private double radials;
    private int circles;
    private int helixes;
    
    
    /**
     * Constructs a {@code Vortex} with the specified particles, the default total number of steps, 200, radius, 2, 
     * Y axis increment per iteration, 0.5, radials per iteration, π / 16, number of circles, 3 and number of helixes, 4.
     * 
     * @param particles the particles
     */
    public Vortex(Particles particles) {
        this(particles, 200, 2, 0.5f, PI / 16, 3, 4);
    }
    
    /**
     * Constructs a {@code Vortex} with the specified particles, total number of steps, radius, Y axis increment per iteration, 
     * radials per iteration, number of circles and number of helixes.
     * 
     * @param particles the particles
     * @param steps the total number of steps
     * @param radius the radius
     * @param grow the Y axis increment per iteration
     * @param radials the radials per iteration
     * @param circles the number of circles
     * @param helixes the number of helixes
     */
    public Vortex(Particles particles, int steps, float radius, float grow, double radials, int circles, int helixes) {
        super(steps);
        this.particles = particles;
        this.radius = radius;
        this.grow = grow;
        this.radials = radials;
        this.circles = circles;
        this.helixes = helixes;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        int steps = context.steps();
        float y = steps * grow;
        double pitch = toRadians(origin.getPitch() + 90);
        double yaw = toRadians(-origin.getYaw());
        
        for (int x = 0; x < circles; x++) {
            for (int i = 0; i < helixes; i++) {
                double angle = steps * radials + (2 * PI * i / helixes);
                offset.setX(cos(angle) * radius);
                offset.setY(y);
                offset.setZ(sin(angle) * radius);

                rotateAroundXAxis(offset, pitch);
                rotateAroundYAxis(offset, yaw);

                context.render(particles, origin, offset);
            }
        }
    }
    
}
