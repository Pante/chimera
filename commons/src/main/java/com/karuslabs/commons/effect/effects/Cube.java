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

import static com.karuslabs.commons.effect.effects.Constants.ANGULAR_VELOCITY;
import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.PI;


@Immutable
public class Cube extends IncrementalEffect {
    
    private Particles particles;
    private float edge;
    private float half;
    private Vector angularVelocity;
    int perRow;
    boolean rotate;
    boolean solid;
    
    
    public Cube(Particles particles) {
        this(particles, 200, 3, ANGULAR_VELOCITY, 8, true, true);
    }
    
    public Cube(Particles particles, int steps, float edge, Vector angularVelocity, int perRow, boolean rotate, boolean solid) {
        super(steps);
        this.particles = particles;
        this.edge = edge;
        this.half = edge / 2;
        this.angularVelocity = angularVelocity;
        this.perRow = perRow;
        this.rotate = rotate;
        this.solid = solid;
    }
    
    
    @Override
    public void render(Context context, Location origin, Location target, Vector offset) {
        int steps = context.steps();
        float x = 0, y = 0, z = 0;
        if (rotate) {
            x = (float) (steps * angularVelocity.getX());
            y = (float) (steps * angularVelocity.getY());
            z = (float) (steps * angularVelocity.getZ());
        }
        
        if (solid) {
            renderWalls(context, origin, offset, x, y, z);
            
        } else {
            renderOutline(context, origin, offset, x, y, z);
        }
    }
    
    
    void renderWalls(Context context, Location location, Vector offset, float rotationX, float rotationY, float rotationZ) {
        for (int i = 0; i <= perRow; i += particles.getAmount()) {
            float posX = edge * ((float) i / perRow) - half;
            
            for (int y = 0; y <= perRow; y += particles.getAmount()) {
                float posY = edge * ((float) y / perRow) - half;

                for (int z = 0; z <= perRow; z += particles.getAmount()) {
                    if (i != 0 && i != perRow && y != 0 && y != perRow && z != 0 && z != perRow) {
                        continue;
                    }
                   
                    float posZ = edge * ((float) z / perRow) - half;
                    offset.setX(posX).setY(posY).setZ(posZ);
                    if (rotate) {
                        rotate(offset, rotationX, rotationY, rotationZ);
                    }
                    
                    context.render(particles, location, offset);
                }
            }
        }
    }
    
    void renderOutline(Context context, Location origin, Vector offset, float x, float y, float z) {
        for (int i = 0; i < 4; i++) {
            float angleY = (float) (i * PI / 2);
            
            for (int j = 0; j < 2; j++) {
                float angleX = (float) (j * PI);
                
                for (int p = 0; p <= perRow; p += particles.getAmount()) {
                    offset.setX(half).setY(half).setZ(edge * p / perRow - half);

                    rotateAroundXAxis(offset, angleX);
                    rotateAroundYAxis(offset, angleY);

                    if (rotate) {
                        rotate(offset, x, y, z);
                    }

                    context.render(particles, origin, offset);
                }
            }

            renderPillarOutline(context, origin, angleY, offset, x, y, z);
        }
    }
    
    void renderPillarOutline(Context context, Location location, float angle, Vector offset, float x, float y, float z) {
        for (int p = 0; p <= perRow; p += particles.getAmount()) {
            offset.setX(half).setY(edge * p / perRow - half).setZ(half);
            
            rotateAroundYAxis(offset, angle);

            if (rotate) {
                rotate(offset, x, y, z);
            }

            context.render(particles, location, offset);
        }
    }
    
}
