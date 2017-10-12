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

import static com.karuslabs.commons.world.Vectors.rotateAroundYAxis;


public class Grid implements Task<Grid, BoundLocation, BoundLocation> {
    
    private Particles particles;
    private int rows;
    private int columns;
    private float cellWidth;
    private float cellHeight;
    private int perWidth;
    private int perHeight;
    private double rotation;
    
    
    public Grid(Particles particles) {
        this(particles, 5, 10, 1, 1, 4, 3, 0);
    }
    
    public Grid(Particles particles, int rows, int columns, float cellWidth, float cellHeight, int perWidth, int perHeight, double rotation) {
        this.particles = particles;
        this.rows = rows;
        this.columns = columns;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.perWidth = perWidth;
        this.perHeight = perHeight;
        this.rotation = rotation;
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        renderRows(context, location);
        renderColumns(context, location);
    }
    
    protected void renderRows(Context<BoundLocation, BoundLocation> context, Location location) {
        Vector vector = context.getVector();
        for (int i = 0; i <= (rows + 1); i++) {
            for (int j = 0; j < perWidth * (columns + 1); j += particles.getAmount()) {
                vector.setY(i * cellHeight);
                vector.setX(j * cellWidth / perWidth);
                render(context, location);
            }
        }
    }
    
    protected void renderColumns(Context<BoundLocation, BoundLocation> context, Location location) {
        Vector vector = context.getVector();
        for (int i = 0; i <= (columns + 1); i += particles.getAmount()) {
            for (int j = 0; j < perHeight * (rows + 1); j++) {
                vector.setX(i * cellWidth);
                vector.setY(j * cellHeight / perHeight);
                render(context, location);
            }
        }
    }

    protected void render(Context<BoundLocation, BoundLocation> context, Location location) {
        Vector vector = context.getVector();
        vector.setZ(0);
        rotateAroundYAxis(vector, rotation);
        context.render(particles, location, vector);
    }

    
    @Override
    public Grid get() {
        return this;
    }
    
}
