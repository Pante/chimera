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

import static com.karuslabs.commons.animation.particles.effects.Constants.ANGULAR_VELOCITY;
import static com.karuslabs.commons.world.Vectors.*;
import static java.lang.Math.PI;


public class Cube implements Task<Cube, BoundLocation, BoundLocation> {
    
    private Particles particles;
    private float edge;
    private float half;
    private Vector angularVelocity;
    int perRow;
    boolean rotate;
    boolean solid;
    Vector vector;
    Vector rotation;
    
    
    public Cube(Particles particles) {
        this(particles, 3, ANGULAR_VELOCITY, 8, true, true);
    }
    
    public Cube(Particles particles, float edge, Vector angularVelocity, int perRow, boolean rotate, boolean solid) {
        this.particles = particles;
        this.edge = edge;
        this.half = edge / 2;
        this.angularVelocity = angularVelocity;
        this.perRow = perRow;
        this.rotate = rotate;
        this.solid = solid;
        vector = new Vector();
        rotation = new Vector();
    }
    
    
    @Override
    public void render(Context<BoundLocation, BoundLocation> context) {
        Location location = context.getOrigin().getLocation();
        long current = context.getCurrent();
        if (rotate) {
            rotation.setX(current * angularVelocity.getX()).setY(current * angularVelocity.getY()).setZ(current * angularVelocity.getZ());
        }
        
        if (solid) {
            renderWalls(context, location);
            
        } else {
            renderOutline(context, location, 4 , 2);
        }
    }
    
    protected void renderOutline(Context<BoundLocation, BoundLocation> context, Location location, int sides, int points) {
        // top and bottom
        double angleX, angleY;
        for (int i = 0; i < sides; i++) {
            angleY = i * PI / 2;
            for (int j = 0; j < points; j++) {
                angleX = j * PI;
                for (int p = 0; p <= perRow; p += particles.getAmount()) {
                    vector.setX(half).setY(half).setZ(edge * p / perRow - half);
                    
                    rotateAroundXAxis(vector, angleX);
                    rotateAroundYAxis(vector, angleY);

                    if (rotate) {
                        rotate(vector, rotation);
                    }
                    
                    context.render(particles, location, vector);
                }
            }
            renderPillarOutlines(context, location, angleY);
        }
    }
    
    protected void renderPillarOutlines(Context<BoundLocation, BoundLocation> context, Location location, double angle) {
        for (int p = 0; p <= perRow; p += particles.getAmount()) {
            vector.setX(half).setZ(half).setY(edge * p / perRow - half);
            rotateAroundYAxis(vector, angle);

            if (rotate) {
                rotate(vector, rotation);
            }

            context.render(particles, location, vector);
        }
    }

    protected void renderWalls(Context<BoundLocation, BoundLocation> context, Location location) {
        for (int x = 0; x <= perRow; x += particles.getAmount()) {
            float posX = edge * ((float) x / perRow) - half;
            
            for (int y = 0; y <= perRow; y += particles.getAmount()) {
                float posY = edge * ((float) y / perRow) - half;

                for (int z = 0; z <= perRow; z += particles.getAmount()) {
                    if (x != 0 && x != perRow && y != 0 && y != perRow && z != 0 && z != perRow) {
                        continue;
                    }
                   
                    float posZ = edge * ((float) z / perRow) - half;
                    vector.setX(posX).setY(posY).setZ(posZ);
                    if (rotate) {
                        rotate(vector, rotation);
                    }
                    
                    context.render(particles, location, vector);
                }
            }
        }
    }

    @Override
    public Cube get() {
        return new Cube(particles, edge, angularVelocity, perRow, rotate, solid);
    }
    
}
