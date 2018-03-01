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


/**
 * Represents a grid.
 * <br>
 * <img src = "https://thumbs.gfycat.com/BetterDisfiguredAddax-size_restricted.gif" alt = "Grid.gif">
 */
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
    
    
    /**
     * Constructs a {@code Grid} with the specified particles, the default number of rows, 5, number of columns 10, 
     * width per cell, 1, height per cell, 1, number of particles per width, 4, number of particles per height, 3 and rotation, 0.
     * 
     * @param particles the particles
     */
    public Grid(Particles particles) {
        this(particles, 5, 10, 1, 1, 4, 3, 0);
    }
    
    /**
     * Constructs a {@code Grid} with the specified particles, number of rows, number of columns, width per cell, height per cell,
     * number of particles per width, number of particles per height and rotation.
     * 
     * @param particles the particles
     * @param rows the number of rows
     * @param columns the number of columns
     * @param cellWidth the width of each cell
     * @param cellHeight the height of each cell
     * @param perWidth the number of particles per width
     * @param perHeight the number of particles per height
     * @param rotation  the rotation in radians
     */
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
    
    
    /**
     * {@inheritDoc}
     */
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
