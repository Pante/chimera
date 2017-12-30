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

import static com.karuslabs.commons.world.Vectors.rotateAroundYAxis;
import static java.lang.Math.*;


/**
 * Represents a hill.
 * <br>
 * <img src = "https://thumbs.gfycat.com/QuerulousMarvelousFairyfly-size_restricted.gif" alt = "Hill.gif">
 */
@Immutable
public class Hill implements Effect {
    
    private Particles particles;
    private float perRow;
    private float length;
    private float height;
    private float yRotation;
    
    
    /**
     * Constructs a {@code Heart} with the specified particles, the default number of particles per row, 30, 
     * length, 6.5, height, 2.5 and rotation along the Y axis, π / 7.
     * 
     * @param particles the particles
     */
    public Hill(Particles particles) {
        this(particles, 30, 6.5F, 2.5F, (float) PI / 7);
    }
    
    /**
     * Constructs a {@code Heart} with the specified particles, particles per row, length, height and rotation along the Y axis.
     * 
     * @param particles the particles
     * @param perRow the particles per row
     * @param length the length of the edges
     * @param height the height
     * @param yRotation the rotation of the hill along the Y axis in radians
     */
    public Hill(Particles particles, float perRow, float length, float height, float yRotation) {
        this.particles = particles;
        this.perRow = perRow;
        this.length = length;
        this.height = height;
        this.yRotation = yRotation;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        for (int x = 0; x <= perRow; x += particles.getAmount()) {
            double y1 = sin(PI * x / perRow);
            double x1 = length * x / perRow;
            
            for (int z = 0; z <= perRow; z += particles.getAmount()) {
                double y2 = sin(PI * z / perRow);
                offset.setX(x1).setY(height * y1 * y2).setZ(length * z / perRow);
                
                rotateAroundYAxis(offset, yRotation);
     
                context.render(particles, origin, offset);
            }
        }
    }
    
}
