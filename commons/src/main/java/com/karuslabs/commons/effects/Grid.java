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
package com.karuslabs.commons.effects;

import com.karuslabs.annotations.Immutable;
import com.karuslabs.commons.effect.*;
import com.karuslabs.commons.effect.particles.Particles;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.rotateAroundYAxis;


@Immutable
public class Grid implements Effect {
    
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
    public void render(Context context, Location location, Location target, Vector offset) {
        renderRows(context, location, offset);
        renderColumns(context, location, offset);
    }
    
    void renderRows(Context context, Location location, Vector offset) {
        for (int i = 0; i <= (rows + 1); i++) {
            for (int j = 0; j < perWidth * (columns + 1); j += particles.getAmount()) {
                offset.setY(i * cellHeight).setX(j * cellWidth / perWidth);
                renderParticles(context, location, offset);
            }
        }
    }
    
    void renderColumns(Context context, Location location, Vector offset) {
        for (int i = 0; i <= (columns + 1); i += particles.getAmount()) {
            for (int j = 0; j < perHeight * (rows + 1); j++) {
                offset.setX(i * cellWidth).setY(j * cellHeight / perHeight);
                renderParticles(context, location, offset);
            }
        }
    }

    void renderParticles(Context context, Location location, Vector offset) {
        offset.setZ(0);
        rotateAroundYAxis(offset, rotation);
        context.render(particles, location, offset);
    }
    
}
