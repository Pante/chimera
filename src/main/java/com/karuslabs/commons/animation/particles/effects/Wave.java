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

import java.util.*;

import org.bukkit.*;
import org.bukkit.util.Vector;

import static com.karuslabs.commons.world.Vectors.rotateAroundYAxis;
import static java.lang.Math.*;


@Immutable
public class Wave implements Task<Wave> {
    
    private static final Vector FRONT = new Vector(-1.5, 0, 0);
    private static final Vector BACK = new Vector(3, 0, 0);
    private static final Vector RISE = new Vector(0.75, 0.5, 0);
    
    private Particles water;
    private Particles cloud;
    private int countFront;
    private int countBack;
    private int rows;
    private Vector front;
    private Vector back;
    private Vector rise;
    private float depth;
    private float heightBack;
    private float width;
    protected final Collection<Vector> waters, clouds;
    
    
    public Wave(Particles water, Particles cloud) {
        this(water, cloud, 10, 10, 20, FRONT, BACK, RISE, 1, 2, 5);
    }

    public Wave(Particles water, Particles cloud, int countFront, int countBack, int rows, Vector front, Vector back, Vector rise, float depth, float heightBack, float width) {
        this.water = water;
        this.cloud = cloud;
        this.countFront = countFront;
        this.countBack = countBack;
        this.rows = rows;
        this.front = front;
        this.back = back;
        this.rise = rise;
        this.depth = depth;
        this.heightBack = heightBack;
        this.width = width;
        this.waters = new HashSet<>();
        this.clouds = new HashSet<>();
    }

    public void invalidate(Location location) {
        waters.clear();
        clouds.clear();

        Vector n1, n2, n_s1ToH, n_s2ToH, c1, c2, s1ToH, s2ToH;
        float len_s1ToH, len_s2ToH, yaw;
        
        // may be cached
        s1ToH = rise.clone().subtract(front);
        c1 = front.clone().add(s1ToH.clone().multiply(0.5));
        len_s1ToH = (float) s1ToH.length();
        n_s1ToH = s1ToH.clone().multiply(1f / len_s1ToH);
        n1 = new Vector(s1ToH.getY(), -s1ToH.getX(), 0).normalize();
        //
        
        if (n1.getX() < 0) {
            n1.multiply(-1);
        }
        
        // may be cached
        s2ToH = rise.clone().subtract(back);
        c2 = back.clone().add(s2ToH.clone().multiply(0.5));
        len_s2ToH = (float) s2ToH.length();
        n_s2ToH = s2ToH.clone().multiply(1f / len_s2ToH);
        n2 = new Vector(s2ToH.getY(), -s2ToH.getX(), 0).normalize();
        // may be cached
        
        if (n2.getX() < 0) {
            n2.multiply(-1);
        }

        yaw = (float) ((-location.getYaw() + 90) * (PI / 180));
        
        // extract
        for (int i = 0; i < countFront; i++) {
            float ratio = (float) i / countFront;
            float x = (ratio - .5f) * len_s1ToH;
            float y = (float) (-depth / pow((len_s1ToH / 2), 2) * pow(x, 2) + depth);
            // copy to holder instead of clone
            Vector v = c1.clone();
            
            v.add(n_s1ToH.clone().multiply(x));
            v.add(n1.clone().multiply(y));
            //
            
            // extract
            for (int j = 0; j < rows; j++) {
                float z = ((float) j / rows - .5f) * width;
                //Copy holder
                Vector vec = v.clone().setZ(v.getZ() + z);
                //
                rotateAroundYAxis(vec, yaw);
                
                // vectors as parameter
                if (i == 0 || i == countFront - 1) {
                    clouds.add(vec);
                } else {
                    waters.add(vec);
                }
            }
            //
        }
        //
        
        // extract
        for (int i = 0; i < countBack; i++) {
            float ratio = (float) i / countBack;
            float x = (ratio - .5f) * len_s2ToH;
            float y = (float) (-heightBack / pow((len_s2ToH / 2), 2) * pow(x, 2) + heightBack);
            // copy to holder instead of clone
            Vector v = c2.clone();
            
            v.add(n_s2ToH.clone().multiply(x));
            v.add(n2.clone().multiply(y));
            //
            
            // extract
            for (int j = 0; j < rows; j++) {
                float z = ((float) j / rows - .5f) * width;
                // Copy holder;
                Vector vec = v.clone().setZ(v.getZ() + z);
                //
                rotateAroundYAxis(vec, yaw);
                
                //vectors as parameter
                if (i == countFront - 1) {
                    clouds.add(vec);
                } else {
                    waters.add(vec);
                }
            }
            //
        }
        //
    }

    @Override
    public void render(Context context) {
        Location location = context.getOrigin().getLocation();
        Vector velocity = context.getVector();

        if (context.getCurrent() == 0) {
            velocity.copy(location.getDirection().setY(0).normalize().multiply(0.2));
            invalidate(location);
        }

        location.add(velocity);

        for (Vector v : clouds) {
            context.render(cloud, location, v);
        }

        for (Vector v : waters) {
            context.render(water, location, v);
        }
    }

    @Override
    public @Immutable Wave get() {
        return this;
    }

}
